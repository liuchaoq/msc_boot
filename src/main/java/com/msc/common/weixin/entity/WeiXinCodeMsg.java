package com.msc.common.weixin.entity;

import lombok.Data;

/**
 * @ClassName WxCodeMsg
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/6/21 10:42
 **/
@Data
public class WeiXinCodeMsg {
    private String access_token;
    private Integer expires_in;//":7200,
    private String refresh_token;
    private String openid;//":"OPENID",
    private String scope;//":"SCOPE"
    private Integer errcode = 0;//":40029,
    private String errmsg;//":"invalid code"
    private String nickname;//": NICKNAME,
    private String sex;//": 1,
    private String province;//":"PROVINCE",
    private String city;//":"CITY",
    private String country;//":"COUNTRY",
    private String headimgurl;//":"https://thirdwx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/46",
    private String privilege;//":[ "PRIVILEGE1" "PRIVILEGE2"     ],
    private String unionid;//": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
    private String ticket;
}
