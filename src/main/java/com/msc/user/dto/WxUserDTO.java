package com.msc.user.dto;

import com.msc.common.util.encryption.EncryptedString;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName WxUserDTO
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/6/21 17:01
 **/
@Data
public class WxUserDTO extends UserDTO {
    @ApiModelProperty(value = "公众号open_id")
    private String openId;

    @ApiModelProperty(value = "微信昵称")
    private String nickname;

    @ApiModelProperty(value = "客户名字")
    private String name;

    @ApiModelProperty(value = "客户名字")
    private String head;

    @ApiModelProperty(value = "门牌号")
    private Integer houseId;

    private EncryptedString aesData;

}
