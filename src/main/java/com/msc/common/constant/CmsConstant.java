package com.msc.common.constant;

import lombok.Data;

/**
 * @author liuyang
 * @description : [一句话描述该类的功能]
 * @date 2021/5/29 15:46
 */
@Data
public class CmsConstant {
    public final static String sourceSystem = "cms";
    /**
    *   系统传递TOKEN的header
    **/
    public final static String TOKEN_NAME = "AuthToken";
    /**
    *   微信传递TOKEN的header
    **/
    public final static String WX_TOKEN_NAME = "WxAuthToken";
    /**
    *   系统管理员用户id
    **/
    public final static Long ADMIN_ID = 1L;
    /**
    *   水站管理员角色id
    **/
    public final static Integer WATER_MANAGER_ROLE = 2;
    /**
    *   地区信息=中国的code
    **/
    public final static String ZONE_COUNTRY_CHINA_CODE = "c9037134-5f75-4790-b291-81d9d2d4e893";
    /**
     * 支付状态-未支付
    **/
    public final static Integer PAY_STATUS_NOT = 0;
    /**
     * 支付状态-已支付
     **/
    public final static Integer PAY_STATUS_YES = 1;
}
