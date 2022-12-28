package com.msc.user.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.msc.common.util.encryption.EncryptedString;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName UserDTO
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/6/15 10:54
 **/
@Data
public class UserDTO {

    @ApiModelProperty(value = "手机号")
    private String username;

    @ApiModelProperty(value = "个人姓名")
    private String name;

    @ApiModelProperty(value = "头像")
    private String head;

    private EncryptedString aesData;
}
