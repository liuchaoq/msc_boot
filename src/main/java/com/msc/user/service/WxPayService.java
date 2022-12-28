package com.msc.user.service;

import com.msc.common.vo.Result;
import com.msc.common.weixin.entity.pay.WxPayNotifyInfo;
import com.msc.user.entity.SysUser;

import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName WxPayService
 * @DESCRIPTION 处理支付相关业务
 * @AUTHOR liuCq
 * @DATE 2022/7/21 9:17
 **/
public interface WxPayService {
    /**
     * 发起支付准备业务处理
    **/
    Result<Object> prePay(SysUser sysUser, String orderCode) throws Exception;

    void executeNotify(WxPayNotifyInfo wxPayNotifyInfo, HttpServletResponse response);

    Result<Object> checkOrderPay(SysUser wxUser, String orderCode);

    void cancelPayNotice(SysUser wxUser, String orderCode) throws Exception;
}
