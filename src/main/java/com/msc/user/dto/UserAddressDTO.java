package com.msc.user.dto;

import lombok.Data;

/**
 * @ClassName UserAddressDTO
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/7/1 10:14
 **/
@Data
public class UserAddressDTO {
    private Integer id;//用户住址关系表id
    private Integer houseId;//住址id
    private Integer uid;
    private String province;
    private String city;
    private String area;
    private String town;
    private String village;
    private String homeNo;
}
