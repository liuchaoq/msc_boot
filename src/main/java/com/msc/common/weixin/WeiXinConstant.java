package com.msc.common.weixin;

/**
 * @ClassName WeiXinConstant
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/6/6 15:44
 **/
public interface WeiXinConstant {
    String APP_ID = "wxf46042cc6b84de5e";
    String MCH_ID = "1628975450";
    String MCH_APIv2_KEY = "EuQ1920EM19gMAgpudnTmd2MM0tlHPDS";
    String MCH_APIv3_KEY = "dcD5deKkfYrq89r7qp2k8ZEWB9athQAK";
    String APP_SECRET = "ebb3e114ca048a5b66c1992b21bf220b";
    String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+APP_ID+"&secret="+APP_SECRET;
    String CHECK_TOKEN = "GBGf1P6YIznWQjNkgrZzDhTsOtTrJ";
    String TOKEN = "ACCESS_TOKEN";
    //用户关注时，获取用户基础信息
    String GET_USER_INFO_URL_FIRST = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=";
    String GET_USER_INFO_URL_SECOND = "&openid=";
    String GET_USER_INFO_URL_THIRD = "&lang=zh_CN";
    //根据open_id获取基础信息
    String GET_USER_OPENID_URL_FIRST = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
            + APP_ID + "&secret=" + APP_SECRET + "&code=";
    String GET_USER_OPENID_URL_SECOND = "&grant_type=authorization_code";
    //客户授权sns根据open_id获取详细信息
    String GET_SNS_USER_INFO_URL_FIRST = "https://api.weixin.qq.com/sns/userinfo?access_token=";
    String GET_SNS_USER_INFO_URL_SECOND = "&openid=";
    String GET_SNS_USER_INFO_URL_THIRD = "&lang=zh_CN";

    /**
     *发起支付准备地址
    **/
    String WX_MCH_PRE_PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    /**
     * 微信支付订单查询接口
    **/
    String WX_MCH_ORDER_QUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
}
