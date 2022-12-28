package com.msc.common.weixin.entity.pay;

import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPayUtil;
import com.msc.common.util.WebToolUtils;
import com.msc.common.weixin.WeiXinConstant;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName WxPrePayReq
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/7/19 13:36
 **/
@Data
public class WxPrePayReq {
    /**
     *公众号id
    **/
    private String appid = WeiXinConstant.APP_ID;
    /**
     *商户号id
    **/
    private String mch_id = WeiXinConstant.MCH_ID;
    /**
     *随机字符串
    **/
    private String nonce_str;
    /**
     *签名
    **/
    private String sign;
    /**
     *商品名称
    **/
    private String body;
    /**
     *订单编号
    **/
    private String out_trade_no;
    /**
     *trade_type=JSAPI时（即JSAPI支付），此参数必传，
     * 此参数为微信用户在商户对应appid下的唯一标识。openid如何获取，
    **/
    private String openid;
    /**
     *订单金额*100，单位（分）
    **/
    private String total_fee;
    /**
     *服务器ip
    **/
    private String spbill_create_ip;
    /**
     *支付回调地址
    **/
    private String notify_url;
    /**
     *发起支付方式
     * JSAPI -JSAPI支付 公众号发起支付
     *
     * NATIVE -Native支付 二维码
     *
     * APP -APP支付
    **/
    private String trade_type = "JSAPI";

    public static void main(String[] args) {

        try {
//            WxPrePayReq wxPrePayReq = new WxPrePayReq();
//            wxPrePayReq.setBody("大有村水站税费标准");//商品名称
//            wxPrePayReq.setNonce_str(WXPayUtil.generateNonceStr());
//            wxPrePayReq.setOut_trade_no("dsadasdad446222dsd");//订单编号
//            wxPrePayReq.setSpbill_create_ip(WebToolUtils.getLocalIP());
//            wxPrePayReq.setNotify_url("http://www.villageserver.cn/wx/pay/executeResult");//水费缴纳支付回调地址
//            wxPrePayReq.setTotal_fee("2532");//订单金额*100.intValue()
//            //商户密钥
//            String key = "";
//            Map<String, String> params = JSON.parseObject(JSON.toJSONString(wxPrePayReq), Map.class);
//            //生成发起支付的xml参数
//            String xmlString = WXPayUtil.generateSignedXml(params,key);
//            System.out.println(xmlString);
            String xml = "<xml><appid><![CDATA[wxf46042cc6b84de5e]]></appid><bank_type><![CDATA[CMB_DEBIT]]></bank_type><cash_fee><![CDATA[410]]></cash_fee><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[Y]]></is_subscribe><mch_id><![CDATA[1628975450]]></mch_id><nonce_str><![CDATA[gdjOquWSyexWe4a1HtZwK3bTWn9Z3T8g]]></nonce_str><openid><![CDATA[or0hB5wrVdSgneBQi8SrL8kj3wBU]]></openid><out_trade_no><![CDATA[2c90027b8210b0b2018210b0b2330001]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[D787B99CE3D0FE8CA28DF407E7820F76]]></sign><time_end><![CDATA[20220725164906]]></time_end><total_fee>410</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[4200001424202207255559952060]]></transaction_id></xml>";
            Boolean flag = WXPayUtil.isSignatureValid(xml, WeiXinConstant.MCH_APIv2_KEY);
            System.out.println(flag);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
