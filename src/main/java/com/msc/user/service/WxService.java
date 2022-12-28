package com.msc.user.service;


import com.msc.common.vo.Result;

/**
 * @ClassName WxService
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/6/7 10:09
 **/
public interface WxService {

    String getAccessToken();

    String refreshAccessToken();

    Result<Object> getOpenId(String code);
}
