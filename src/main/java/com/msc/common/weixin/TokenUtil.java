package com.msc.common.weixin;

import com.alibaba.fastjson.JSON;
import com.msc.common.util.HttpUtil;
import com.msc.common.util.RedisUtil;
import com.msc.common.weixin.entity.WeiXinCodeMsg;
import com.msc.common.weixin.entity.WeiXinToken;
import com.msc.user.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName TokenUtil
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/6/6 15:46
 **/
@Slf4j
public class TokenUtil {
    public static WeiXinToken getToken() {
        String result = HttpUtil.httpGet(WeiXinConstant.TOKEN_URL);
        try {
            log.error("token获取结果:" + result);
            return JSON.parseObject(result, WeiXinToken.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static WeiXinCodeMsg getOpenIdByCode(String code) {
        String result = HttpUtil.httpGet(WeiXinConstant.GET_USER_OPENID_URL_FIRST + code
                + WeiXinConstant.GET_USER_OPENID_URL_SECOND);
        try {
            log.error("token获取结果:" + result);
            return JSON.parseObject(result, WeiXinCodeMsg.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static void getSnsUserInfo(WeiXinCodeMsg weiXinCodeMsg) {
        String result = HttpUtil.httpGet(WeiXinConstant.GET_SNS_USER_INFO_URL_FIRST + weiXinCodeMsg.getAccess_token()
                + WeiXinConstant.GET_SNS_USER_INFO_URL_SECOND + weiXinCodeMsg.getOpenid()
                + WeiXinConstant.GET_SNS_USER_INFO_URL_THIRD);
        try {
            log.error("token获取结果:" + result);
            WeiXinCodeMsg msg = JSON.parseObject(result, WeiXinCodeMsg.class);
            weiXinCodeMsg.setSex(msg.getSex());
            weiXinCodeMsg.setCity(msg.getCity());
            weiXinCodeMsg.setCountry(msg.getCountry());
            weiXinCodeMsg.setUnionid(msg.getUnionid());
            weiXinCodeMsg.setHeadimgurl(msg.getHeadimgurl());
            weiXinCodeMsg.setNickname(msg.getNickname());
            weiXinCodeMsg.setProvince(msg.getProvince());
            weiXinCodeMsg.setPrivilege(msg.getPrivilege());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     *  获取当前的redisKey，
     *  type：1.获取微信认证手机验证码---key
     *  type: 2.获取修改密码手机验证码---key
     *
    **/
    public static String getRedisKey(int type, SysUser sysUser) {
        return sysUser.getOpenId() + "_" + type;
    }

    /**
     *  获取随机验证码，
     *
     **/
    public static Integer getAuthCode() {
        Double d = (Math.random()*9 + 1)*100000;
        return d.intValue();
    }

    public static String getJsapiTicket(String accessToken) {
        if (StringUtils.isEmpty(accessToken)) {
            return null;
        }
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+accessToken+"&type=jsapi";
        String result = HttpUtil.httpGet(url);
        WeiXinCodeMsg weiXinCodeMsg = JSON.parseObject(result, WeiXinCodeMsg.class);
        System.out.println(weiXinCodeMsg.toString());
        if (weiXinCodeMsg.getErrcode() == 0) {
            return weiXinCodeMsg.getTicket();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getJsapiTicket(getToken().getAccess_token()));
    }
}
