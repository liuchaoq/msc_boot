package com.msc.common.weixin.entity;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName WeiXinToken
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/6/6 15:56
 **/
@Data
public class WeiXinToken {
    private String access_token;
    private Long expires_in = 7200L;
    private Date expires_time;
    private Integer errcode = 0;
    private String errmsg;

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
        this.expires_time = new Date(System.currentTimeMillis() + expires_in*1000);
    }

    @Override
    public String toString() {
        return "WeiXinToken{" +
                "access_token='" + access_token + '\'' +
                ", expires_in=" + expires_in +
                ", expires_time=" + expires_time +
                '}';
    }
}
