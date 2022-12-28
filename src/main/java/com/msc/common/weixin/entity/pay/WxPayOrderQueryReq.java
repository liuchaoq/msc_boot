package com.msc.common.weixin.entity.pay;

import com.msc.common.weixin.WeiXinConstant;
import lombok.Data;

/**
 * @ClassName WxPayOrderQueryReq
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/7/26 10:18
 **/
@Data
public class WxPayOrderQueryReq {
    private String appid = WeiXinConstant.APP_ID;//	是	String(32)	wxd678efh567hg6787	微信支付分配的公众账号ID（企业号corpid即为此appid）
    private String mch_id = WeiXinConstant.MCH_ID;//	是	String(32)	1230000109	微信支付分配的商户号
    private String out_trade_no;//	String(32)	20150806125346	商户系统内部订单号，要求32个字符内（最少6个字符），只能是数字、大小写字母_-|*且在同一个商户号下唯一。详见商户订单号
    private String nonce_str;//	是	String(32)	C380BEC2BFD727A4B6845133519F3AD6	随机字符串，不长于32位。推荐随机数生成算法
    private String sign;//	是	String(32)	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	通过签名算法计算得出的签名值，详见签名生成算法
    private String sign_type = "MD5";
}
