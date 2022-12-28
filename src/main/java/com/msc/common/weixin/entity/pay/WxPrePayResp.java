package com.msc.common.weixin.entity.pay;

import lombok.Data;

/**
 * @ClassName WxPrePayResp
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/7/19 13:53
 **/
@Data
public class WxPrePayResp {
    /**
     *SUCCESS/FAIL
     *
     * 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
    **/
    private String return_code;
    /**
     *当return_code为FAIL时返回信息为错误原因 ，例如
     *
     * 签名失败
     *
     * 参数格式校验错误
    **/
    private String return_msg;


    private String appid;//	是	String(32)	wx8888888888888888	调用接口提交的公众账号ID
    private String mch_id;//	是	String(32)	1900000109	调用接口提交的商户号
    private String device_info;//	否	String(32)	013467007045764	自定义参数，可以为请求支付的终端设备号等
    private String nonce_str;//	是	String(32)	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	微信返回的随机字符串
    private String sign;//	是	String(32)	C380BEC2BFD727A4B6845133519F3AD6	微信返回的签名值，详见签名算法
    private String result_code;//	是	String(16)	SUCCESS	SUCCESS/FAIL
    /**
     *INVALID_REQUEST	参数错误	参数格式有误或者未按规则上传	订单重入时，要求参数值与原请求一致，请确认参数问题
     * NOAUTH	商户无此接口权限	商户未开通此接口权限	请商户前往申请此接口权限
     * ORDERPAID	商户订单已支付	商户订单已支付，无需重复操作	商户订单已支付，无需更多操作
     * ORDERCLOSED	订单已关闭	当前订单已关闭，无法支付	当前订单已关闭，请重新下单
     * SYSTEMERROR	系统错误	系统超时	系统异常，请用相同参数重新调用
     * APPID_NOT_EXIST	APPID不存在	参数中缺少APPID	请检查APPID是否正确
     * MCHID_NOT_EXIST	MCHID不存在	参数中缺少MCHID	请检查MCHID是否正确
     * APPID_MCHID_NOT_MATCH	appid和mch_id不匹配	appid和mch_id不匹配	请确认appid和mch_id是否匹配
     * LACK_PARAMS	缺少参数	缺少必要的请求参数	请检查参数是否齐全
     * OUT_TRADE_NO_USED	商户订单号重复	同一笔交易不能多次提交	请核实商户订单号是否重复提交
     * SIGNERROR	签名错误	参数签名结果不正确	请检查签名参数和方法是否都符合签名算法要求
     * XML_FORMAT_ERROR	XML格式错误	XML格式错误	请检查XML参数格式是否正确
     * REQUIRE_POST_METHOD	请使用post方法	未使用post传递参数 	请检查请求参数是否通过post方法提交
     * POST_DATA_EMPTY	post数据为空	post数据不能为空	请检查post数据是否为空
     * NOT_UTF8	编码格式错误	未使用指定编码格式	请使用UTF-8编码格式
    **/
    private String err_code;

    private String trade_type;

    /**
     *预支付交易会话标识
    **/
    private String prepay_id;

    private Long timeStamp;
}
