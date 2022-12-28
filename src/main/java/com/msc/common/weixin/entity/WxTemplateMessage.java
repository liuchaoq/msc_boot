package com.msc.common.weixin.entity;

import lombok.Data;

/**
 * @ClassName WxTempleteMessageDTO
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/7/18 15:25
 **/
@Data
public class WxTemplateMessage {
    //推送对象OPENID",
    private String touser;
    //模板消息模板id":"ngqIpbwh8bUfcSsECmogfXcV14J0tQlEpBO27izEYtY",
    private String template_id;
    //跳转url":"http://www.villageserver.cn/",
    private String url;
//            "miniprogram":{
//        "appid":"xiaochengxuappid12345",
//                "pagepath":"index?foo=bar"
//    },小程序用到的
    //订单id":"MSG_000001",
    private String client_msg_id;
    //消息主体
    private WxTemplateMessageDetail data;

}
