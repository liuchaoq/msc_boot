package com.msc.user.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.wxpay.sdk.WXPayUtil;
import com.msc.common.base.BaseController;
import com.msc.common.constant.CmsConstant;
import com.msc.common.properties.AliCloudOSSProperties;
import com.msc.common.redis.SignatureUtil;
import com.msc.common.util.HttpUtil;
import com.msc.common.util.RedisUtil;
import com.msc.common.util.WebToolUtils;
import com.msc.common.util.XmlMessageUtil;
import com.msc.common.util.oss.OssBootUtil;
import com.msc.common.vo.Result;
import com.msc.common.weixin.Enum.WxEventType;
import com.msc.common.weixin.Enum.WxMsgType;
import com.msc.common.weixin.TokenUtil;
import com.msc.common.weixin.WeiXinConstant;
import com.msc.common.weixin.entity.WeiXinMsg;
import com.msc.common.weixin.entity.WeiXinMsgXml;
import com.msc.common.weixin.entity.WeiXinUser;
import com.msc.common.weixin.entity.pay.WxPayNotifyInfo;
import com.msc.common.weixin.entity.pay.WxPrePayReq;
import com.msc.user.entity.SysUser;
import com.msc.user.service.SysUserService;
import com.msc.user.service.WxPayService;
import com.msc.user.service.WxService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @ClassName WeiXinController
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/6/6 16:39
 **/
@Slf4j
@RestController
public class WeiXinController extends BaseController {
    @Resource
    private RedisUtil redisUtil;
    @Autowired
    private WxService wxService;
    @Autowired
    private SysUserService sysUserService;
    @Resource
    private AliCloudOSSProperties properties;
    @Autowired
    private WxPayService wxPayService;
    @GetMapping("/wx")
    public void WeiXinListener(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // xml请求解析
        String message = XmlMessageUtil.getMessage(request);
        System.out.println(message);
        WeiXinMsgXml weiXinMsgXml = JSON.parseObject(message,WeiXinMsgXml.class);
        WeiXinMsg weiXinMsg = weiXinMsgXml.getXml();
        if (weiXinMsg == null) {
            String signature = request.getParameter("signature");
            String timestamp = request.getParameter("timestamp");
            String nonce = request.getParameter("nonce");
            String echostr = request.getParameter("echostr");
            if (StringUtils.isNotEmpty(signature) && check(timestamp, nonce, signature)) {
                System.out.println("接入成功");
                PrintWriter out = response.getWriter();
                out.print(echostr);
                out.flush();
                out.close();
            } else {
                Enumeration<String> enumeration = request.getAttributeNames();
                while (enumeration.hasMoreElements()) {
                    String name = enumeration.nextElement();
                    System.out.println(name+":"+request.getAttribute(name));
                }
            }
        }
    }

    @RequestMapping(value = "/wx",method = RequestMethod.POST)
    public void WeiXinListenerEvent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // xml请求解析
        String message = XmlMessageUtil.getMessage(request);
        System.out.println(message);
        WeiXinMsgXml weiXinMsgXml = JSON.parseObject(message,WeiXinMsgXml.class);
        WeiXinMsg weiXinMsg = weiXinMsgXml.getXml();
        if (weiXinMsg != null) {
            if (WxMsgType.EVENT.getName().equals(weiXinMsg.getMsgType())) {
                executeEvent(weiXinMsg);
            } else {
                executeMsg(weiXinMsg);
            }
        }
    }
    /**
    *   处理事件
    **/
    private String executeEvent(WeiXinMsg weiXinMsg) {
        String token = wxService.getAccessToken();
        //客户订阅操作
        if (WxEventType.SUBSCRIBE.getName().equals(weiXinMsg.getEvent()) && StringUtils.isNotEmpty(token)) {
            StringBuffer getUserInfoUrl = new StringBuffer(WeiXinConstant.GET_USER_INFO_URL_FIRST)
                    .append(token).append(WeiXinConstant.GET_USER_INFO_URL_SECOND)
                    .append(weiXinMsg.getFromUserName()).append(WeiXinConstant.GET_USER_INFO_URL_THIRD);
            String result = HttpUtil.httpGet(getUserInfoUrl.toString());
            if (result.contains("access_token is invalid")) {
                token = wxService.refreshAccessToken();
                getUserInfoUrl = new StringBuffer(WeiXinConstant.GET_USER_INFO_URL_FIRST)
                        .append(token).append(WeiXinConstant.GET_USER_INFO_URL_SECOND)
                        .append(weiXinMsg.getFromUserName()).append(WeiXinConstant.GET_USER_INFO_URL_THIRD);
                result = HttpUtil.httpGet(getUserInfoUrl.toString());
            }
            if (result.contains("errcode")) {
//                WeiXinMsg msg = JSON.parseObject(result, WeiXinMsg.class);
                log.error("获取关注用户基础信息失敗:{}", result);
            } else {
                WeiXinUser weiXinUser = JSON.parseObject(result, WeiXinUser.class);
                QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("open_id", weiXinUser.getOpenid());
                if (sysUserService.selectCount(queryWrapper) > 0) {
                    log.error("用户信息已存在:{},更新状态为已关注", result);
                    sysUserService.updateUserSubStatus(weiXinUser);
                } else {
                    SysUser sysUser = new SysUser();
                    sysUser.setCreateTime(new Date());
                    sysUser.setOpenId(weiXinUser.getOpenid());
                    sysUser.setSubTime(new Date(weiXinUser.getSubscribe_time()*1000));
                    sysUserService.saveSysUser(sysUser);
                }
            }
        }
        return "";
    }

    /**
     *   处理消息
     **/
    private String executeMsg(WeiXinMsg weiXinMsg) {
        return null;
    }

    private static boolean check(String timestamp, String nonce, String signature) {
        String[] strs = new String[]{WeiXinConstant.CHECK_TOKEN, timestamp, nonce};//junran
        Arrays.sort(strs);
        String str = strs[0] + strs[1] + strs[2];
        String mysig = sha1(str);
        System.out.println("mysig: " + mysig);
        System.out.println("signature: " + signature);
        return mysig.equalsIgnoreCase(signature);
    }

    private static String sha1(String src) {
        try {
            MessageDigest md = MessageDigest.getInstance("sha1");
            byte[] digest = md.digest(src.getBytes());
            char[] chars = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            StringBuilder sb = new StringBuilder();
            byte[] var5 = digest;
            int var6 = digest.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                byte b = var5[var7];
                sb.append(chars[b >> 4 & 15]);
                sb.append(chars[b & 15]);
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException var9) {
            var9.printStackTrace();
            return null;
        }
    }

    /**
     * @Description: 该接口用来解析检查公众号接口返回数据
     * @Param: [jsonObject]
     * @return: void
     * @Author: liuCq
     * @Date: 2022/6/21
     */
    @PostMapping("/wx/getOpenId")
    public Result<Object> getOpenId(@RequestParam("code") String code) {
        return wxService.getOpenId(code);
    }

    @PostMapping("/wx/getUserInfo")
    public Result<Object> getUserInfo(@RequestParam("code") String code) {
        return wxService.getOpenId(code);
    }

    /**
    * @Description: 该接口用来解析检查公众号接口返回数据
    * @Param: [jsonObject]
    * @return: void
    * @Author: liuCq
    * @Date: 2022/6/21
    */
    @PostMapping("/wx/checkData")
    public void checkData(@RequestBody JSONObject jsonObject) {
        log.error("接口返回数据:" + jsonObject.toJSONString());
    }

    @GetMapping("/wx/getJsApiConfig")
    public Result<Object> getJsApiConfig(HttpServletRequest request){
        if (getWxUser() == null) {
            return Result.error("您无权访问该接口");
        }
        String url = request.getParameter("pageUrl");
        String ticket = (String) redisUtil.get("jsapi_ticket");
        if (StringUtils.isEmpty(ticket)) {
            String token = (String) redisUtil.get(CmsConstant.TOKEN_NAME);
            if (StringUtils.isEmpty(token)) {
                token = TokenUtil.getToken().getAccess_token();
            }
            ticket = TokenUtil.getJsapiTicket(token);
            redisUtil.set("jsapi_ticket", ticket, 7200);
        }
        try {
            SignatureUtil signatureUtil = new SignatureUtil(ticket, url);
            return Result.OK(signatureUtil);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取微信配置项失败");
        }
    }

    @PostMapping("wx/uploadFile")
    public Result<Object> uploadFile(HttpServletRequest request) {
        Map<String,String> resultMap = new HashMap<>();
        String pathDir = request.getHeader("dir");
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        Iterator<String> fileNames = multipartHttpServletRequest.getFileNames();
        if (fileNames.hasNext()) {
            MultipartFile file = multipartHttpServletRequest.getFile(fileNames.next());
            log.error(file.getName()+"="+file.getOriginalFilename());
            String targetUrl = OssBootUtil.upload(properties,file,pathDir);
            resultMap.put(file.getOriginalFilename(), targetUrl);
        }
        if (MapUtils.isEmpty(resultMap)) {
            return Result.error("上传文件失败");
        }
        return Result.OK(resultMap);
    }

    @PostMapping("wx/prePay.json")
    public Result<Object> prePay(@RequestParam("orderCode") String orderCode) throws Exception {
        return wxPayService.prePay(getWxUser(), orderCode);
    }

    /** 
    * @Description: 支付回调地址 
    * @Param: [request, response] 
    * @return: void 
    * @Author: liuCq
    * @Date: 2022/7/26 
    */ 
    @PostMapping("wx/pay/executeResult")
    public void executeResult(HttpServletRequest request, HttpServletResponse response) {
        String message = XmlMessageUtil.getXmlMessage(request);
        try {
            //校验签名
            if (WXPayUtil.isSignatureValid(message, WeiXinConstant.MCH_APIv2_KEY)) {
                Map<String, String> mapResult = WXPayUtil.xmlToMap(message);
                WxPayNotifyInfo wxPayNotifyInfo = JSON.parseObject(JSON.toJSONString(mapResult), WxPayNotifyInfo.class);

                wxPayService.executeNotify(wxPayNotifyInfo, response);
            } else {
                response.setContentType("application/xml;charset=UTF-8");
                Map<String, String> resultMap = new HashMap<>();
                resultMap.put("return_code","FAIL");
                resultMap.put("return_msg", "签名校验失败");
                response.getWriter().write(WXPayUtil.mapToXml(resultMap));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    /** 
    * @Description: 校验订单是否完成支付 
    * @Param: [orderCode] 
    * @return: com.msc.common.vo.Result<java.lang.Object> 
    * @Author: liuCq
    * @Date: 2022/7/26 
    */ 
    @PostMapping("wx/checkOrderPay.json")
    public Result<Object> checkOrderPay(@RequestParam("orderCode") String orderCode) throws Exception {
        return wxPayService.checkOrderPay(getWxUser(), orderCode);
    }

    /**
    * @Description: 用户取消支付，或支付失败
    * @Param:
    * @return:
    * @Author: liuCq
    * @Date: 2022/7/26
    */
    @PostMapping("wx/cancelPayNotice.json")
    public void cancelPayNotice(@RequestParam("orderCode") String orderCode) throws Exception {
        wxPayService.cancelPayNotice(getWxUser(), orderCode);
    }

}
