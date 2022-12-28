package com.msc.user.dto.req;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName LoginReq
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/6/13 10:54
 **/
@Data
public class LoginReq implements Serializable {
    private String code;
    private String password;
}
