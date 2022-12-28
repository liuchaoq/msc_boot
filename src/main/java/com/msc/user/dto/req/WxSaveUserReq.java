package com.msc.user.dto.req;

import lombok.Data;

/**
 * @ClassName WxSaveUserReq
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/6/28 14:22
 **/
@Data
public class WxSaveUserReq {
    private String areaCode;//: "144200000"
    private String authCode;//: "123456"
    private String cityCode;//: "15900"
    private Integer houseId;//: ""
    private String name;//: "刘超群"
    private String provinceCode;//: "16"
    private String townCode;//: "14200000001"
    private String username;//: "18550433074"
    private Integer villageId;//: ""
    private String homeNo;
    private Integer id;
}
