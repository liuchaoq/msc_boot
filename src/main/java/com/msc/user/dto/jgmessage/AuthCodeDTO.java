package com.msc.user.dto.jgmessage;

import lombok.Data;

/**
 * @ClassName AuthCodeDTO
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/6/27 16:51
 **/
@Data
public class AuthCodeDTO {
    //手机号
    private String mobile;
    //签名id
    private String sign_id = "";
    //模板id
    private Integer temp_id;
    //验证码
    private Integer code;

    public AuthCodeDTO(String mobile, Integer code) {
        this.mobile = mobile;
        this.code = code;
    }
}
