package com.msc.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.wxpay.sdk.WXPayUtil;
import com.msc.common.constant.CmsConstant;
import com.msc.common.util.*;
import com.msc.common.util.encryption.AesEncryptUtil;
import com.msc.common.util.encryption.EncryptedString;
import com.msc.common.vo.Result;
import com.msc.common.weixin.TemplateMessageUtil;
import com.msc.common.weixin.TokenUtil;
import com.msc.common.weixin.WeiXinConstant;
import com.msc.common.weixin.entity.WxTemplateMessage;
import com.msc.common.weixin.entity.WxTemplateMessageDetail;
import com.msc.common.weixin.entity.WxTemplateMessageDetailContent;
import com.msc.common.weixin.entity.pay.WxPayNotifyInfo;
import com.msc.common.weixin.entity.pay.WxPayOrderQueryReq;
import com.msc.common.weixin.entity.pay.WxPrePayReq;
import com.msc.common.weixin.entity.pay.WxPrePayResp;
import com.msc.user.dto.OrderInfoDTO;
import com.msc.user.entity.SysUser;
import com.msc.user.entity.TOrder;
import com.msc.user.entity.TOrderPayment;
import com.msc.user.mapper.TOrderMapper;
import com.msc.user.mapper.TOrderPaymentMapper;
import com.msc.user.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @ClassName WxPayServiceImpl
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/7/21 9:20
 **/
@Slf4j
@Service
public class WxPayServiceImpl implements WxPayService {
    @Autowired
    private TOrderMapper tOrderMapper;
    @Resource
    private RedisUtil redisUtil;
    @Autowired
    private TOrderPaymentMapper tOrderPaymentMapper;
    @Value("${template.model.paysuccess}")
    private String templateModelId;

    @Override
    public Result<Object> prePay(SysUser sysUser, String orderCode) throws Exception {
        EncryptedString encryptedString = sysUser.getAesData();
        orderCode = AesEncryptUtil.desEncrypt(orderCode,encryptedString.getKey(),encryptedString.getIv(),AesEncryptUtil.NO_PADDING);
        OrderInfoDTO orderInfoDTO = tOrderMapper.getOrderInfo(orderCode);
        if (orderInfoDTO == null) {
            return Result.error("????????????????????????,????????????");
        } else if (orderInfoDTO.getPayStatus() == 1) {
            return Result.error(302,"???????????????"+orderInfoDTO.getPayUserName()+"????????????");
        } else if (redisUtil.getExpire("prePay" + orderCode) > 0) {
            String name = redisUtil.get("prePay" + orderCode).toString();
            return Result.error(301,"??????????????????"+name+"?????????,???????????????");
        } else {
            redisUtil.set("prePay" + orderCode, sysUser.getName(), 600);
        }
        //?????????????????????id
        QueryWrapper<TOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", orderCode);
        TOrder tOrder = tOrderMapper.selectOne(queryWrapper);
        tOrder.setUserId(sysUser.getId().intValue());


        StringBuffer buffer = new StringBuffer();
        orderInfoDTO.getProductList().forEach(v->{
            buffer.append(v.getProductName()).append(";");
        });
        WxPrePayReq wxPrePayReq = new WxPrePayReq();
        wxPrePayReq.setBody(buffer.toString());//????????????
        wxPrePayReq.setNonce_str(WXPayUtil.generateNonceStr());
        wxPrePayReq.setOut_trade_no(orderInfoDTO.getCode());//????????????
        wxPrePayReq.setSpbill_create_ip(WebToolUtils.getLocalIP());
        wxPrePayReq.setOpenid(sysUser.getOpenId());
        wxPrePayReq.setNotify_url("http://www.villageserver.cn/msc/wx/pay/executeResult");//??????????????????
        wxPrePayReq.setTotal_fee((orderInfoDTO.getAmount().multiply(new BigDecimal(100))).intValue()+"");//????????????*100.intValue()
        //????????????
        Map<String, String> params = JSON.parseObject(JSON.toJSONString(wxPrePayReq), Map.class);
        //?????????????????????xml??????
        String xmlString = WXPayUtil.generateSignedXml(params, WeiXinConstant.MCH_APIv2_KEY);
        String resultString = HttpUtil.httpXml(WeiXinConstant.WX_MCH_PRE_PAY_URL, xmlString);
        Map<String,String> respMap = WXPayUtil.xmlToMap(resultString);
        log.error(respMap.toString());
        /**
         *                          "appId":data.appid,     //?????????ID??????????????????
         *                         "timeStamp":new Date().getTime(),         //???????????????1970??????????????????
         *                         "nonceStr":data.nonce_str, //?????????
         *                         "package":"prepay_id="+data.prepay_id,
         *                         "signType":"MD5",         //?????????????????????
         *                         "paySign":data.sign //????????????
        **/
        WxPrePayResp resp = JSON.parseObject(JSON.toJSONString(respMap), WxPrePayResp.class);
        resp.setTimeStamp(System.currentTimeMillis()/1000);
        //????????????????????????
        Map<String, String> signMap = new HashMap<>();
        signMap.put("appId", WeiXinConstant.APP_ID);
        signMap.put("timeStamp", resp.getTimeStamp().toString());
        signMap.put("nonceStr", resp.getNonce_str());
        signMap.put("package", "prepay_id="+resp.getPrepay_id());
        signMap.put("signType", "MD5");
        resp.setSign(WXPayUtil.generateSignature(signMap, WeiXinConstant.MCH_APIv2_KEY));
        if ("FAIL".equals(resp.getReturn_code())) {
            redisUtil.del("prePay" + orderCode);
        }
        return Result.OK(resp);
    }

    @Override
    public void executeNotify(WxPayNotifyInfo wxPayNotifyInfo, HttpServletResponse response) {
        String orderCode = wxPayNotifyInfo.getOut_trade_no();
        Map<String, String> resultMap = new HashMap<>();
        QueryWrapper<TOrderPayment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_code", orderCode);
        if (tOrderPaymentMapper.selectCount(queryWrapper) > 0) {
            resultMap.put("return_code", "SUCCESS");
            resultMap.put("return_msg", "OK");
        } else if ("SUCCESS".equals(wxPayNotifyInfo.getReturn_code())
                && "SUCCESS".equals(wxPayNotifyInfo.getResult_code())) {
            CompletableFuture.runAsync(()->{
                execute(wxPayNotifyInfo);
            });
            resultMap.put("return_code", "SUCCESS");
            resultMap.put("return_msg", "OK");
        } else {
            resultMap.put("return_code", "FAIL");
        }
        try {
            response.setContentType("application/xml;charset=UTF-8");
            response.getWriter().write(WXPayUtil.mapToXml(resultMap));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public Result<Object> checkOrderPay(SysUser wxUser, String orderCode) {
        EncryptedString encryptedString = wxUser.getAesData();
        try {
            orderCode = AesEncryptUtil.desEncrypt(orderCode,
                    encryptedString.getKey(),
                    encryptedString.getIv(),
                    AesEncryptUtil.NO_PADDING);
            if (checkOrderStatus(orderCode)) {
                //????????????????????????????????????????????????????????????????????????
                redisUtil.del("prePay" + orderCode);
                return Result.OK();
            }

            WxPayOrderQueryReq wxPayOrderQueryReq = new WxPayOrderQueryReq();
            wxPayOrderQueryReq.setOut_trade_no(orderCode);
            wxPayOrderQueryReq.setNonce_str(WXPayUtil.generateNonceStr());//???????????????
            Map<String,String> params = JSON.parseObject(JSON.toJSONString(wxPayOrderQueryReq), Map.class);
            String xmlReq = WXPayUtil.generateSignedXml(params, WeiXinConstant.MCH_APIv2_KEY);
            String xmlResp = HttpUtil.httpXml(WeiXinConstant.WX_MCH_ORDER_QUERY_URL, xmlReq);
            //??????????????????
            Map<String,String> resultMap = WXPayUtil.xmlToMap(xmlResp);
            if ("SUCCESS".equals(resultMap.get("return_code"))
                    && "SUCCESS".equals(resultMap.get("result_code"))
                    && "SUCCESS".equals(resultMap.get("trade_state"))) {
                //????????????????????????????????????????????????????????????????????????
                redisUtil.del("prePay" + orderCode);
                //?????????????????????????????????????????????????????????????????????
                if (!checkOrderStatus(orderCode)) {
                    WxPayNotifyInfo wxPayNotifyInfo = JSON.parseObject(JSON.toJSONString(resultMap),WxPayNotifyInfo.class);
                    CompletableFuture.runAsync(()->{
                        execute(wxPayNotifyInfo);
                    });
                }
                return Result.OK();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("???????????????");
    }

    private boolean checkOrderStatus(String orderCode) {
        OrderInfoDTO orderInfoDTO = tOrderMapper.getOrderInfo(orderCode);
        //?????????????????????????????????????????????????????????????????????
        return orderInfoDTO.getPayStatus() == 1;
    }
    @Override
    public void cancelPayNotice(SysUser wxUser, String orderCode) throws Exception {
        checkOrderPay(wxUser, orderCode);
        orderCode = AesEncryptUtil.desEncrypt(orderCode,
                wxUser.getAesData().getKey(),
                wxUser.getAesData().getIv(),
                AesEncryptUtil.NO_PADDING);
        log.error("???????????????????????????" + orderCode);
        if (redisUtil.getExpire("prePay" + orderCode) > 0) {
            redisUtil.del("prePay" + orderCode);
        }
    }

    private void execute(WxPayNotifyInfo wxPayNotifyInfo) {
        //????????????????????????
        if (redisUtil.getExpire("EXECUTE" + wxPayNotifyInfo.getOut_trade_no()) > 0) {
            return;
        } else {
            redisUtil.set("EXECUTE" + wxPayNotifyInfo.getOut_trade_no(), wxPayNotifyInfo.getOpenid(), 600);
        }
        //?????????????????????????????????????????????
        BigDecimal payAmount = new BigDecimal(wxPayNotifyInfo.getTotal_fee());
        QueryWrapper<TOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", wxPayNotifyInfo.getOut_trade_no());
        TOrder tOrder = tOrderMapper.selectOne(queryWrapper);
        /**
         * ????????????
         * ??????????????????
        **/
        if (tOrder != null && payAmount.compareTo(tOrder.getOrderAmount().multiply(new BigDecimal(100)))==0) {
            String title = "";
            if (tOrder.getOrderType() == 0) {
                /**
                 *????????????????????????
                 * 1.?????????????????????????????????
                **/
                tOrderMapper.modifyMeterRecordStatus(tOrder.getCode(), CmsConstant.PAY_STATUS_YES);
                title = "??????????????????";
            }

            if (tOrder.getOrderType() == 1) {
                /**
                 *??????????????????????????????,
                 * 1.?????????????????????????????????????????????
                 * 2.???????????????
                 * 3.????????????????????????
                **/
                title = "??????????????????";
            }

            /**
             * ??????????????????
             * 1.????????????????????????
             * 2.????????????????????????
             * 3.????????????????????????
            **/
            tOrder.setPayStatus(CmsConstant.PAY_STATUS_YES);
            tOrder.setUpdateTime(new Date());
            tOrderMapper.updateById(tOrder);

            //????????????????????????
            TOrderPayment payment = new TOrderPayment();
            payment.setAmount(tOrder.getOrderAmount());
            payment.setOrderCode(tOrder.getCode());
            payment.setCode(UUIDGenerator.generate());
            payment.setCreateTime(new Date());
            payment.setCreateUserId(tOrder.getUserId());
            payment.setPayStatus(CmsConstant.PAY_STATUS_YES);
            payment.setOpenId(wxPayNotifyInfo.getOpenid());
            payment.setBankType(wxPayNotifyInfo.getBank_type());
            payment.setTimeEnd(wxPayNotifyInfo.getTime_end());
            payment.setTransactionId(wxPayNotifyInfo.getTransaction_id());
            tOrderPaymentMapper.insert(payment);

            //????????????????????????????????????
            WxTemplateMessage wxTemplateMessage = new WxTemplateMessage();
            wxTemplateMessage.setClient_msg_id(tOrder.getId().toString());
            wxTemplateMessage.setTemplate_id(templateModelId);
            wxTemplateMessage.setTouser(wxPayNotifyInfo.getOpenid());
            WxTemplateMessageDetail wxTemplateMessageDetail = new WxTemplateMessageDetail();
            wxTemplateMessageDetail.setFirst(new WxTemplateMessageDetailContent("??????????????????"));
            wxTemplateMessageDetail.setKeyword1(new WxTemplateMessageDetailContent(tOrder.getCode()));
            wxTemplateMessageDetail.setKeyword2(new WxTemplateMessageDetailContent(tOrder.getOrderAmount().toString()));
            wxTemplateMessageDetail.setKeyword3(new WxTemplateMessageDetailContent(DateUtil.Date2Str(new Date())));
            wxTemplateMessageDetail.setKeyword4(new WxTemplateMessageDetailContent("????????????"));
            wxTemplateMessage.setData(wxTemplateMessageDetail);
            //????????????????????????path
            String token = (String) redisUtil.get(WeiXinConstant.TOKEN);
            if (StringUtils.isEmpty(token)) {
                token = TokenUtil.getToken().getAccess_token();
            }
            TemplateMessageUtil.sendTemplateMsg(wxTemplateMessage, token);
        }
    }
}
