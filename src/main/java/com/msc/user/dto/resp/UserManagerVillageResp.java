package com.msc.user.dto.resp;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName UserManagerVillageResp
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/6/29 14:13
 **/
@Data
public class UserManagerVillageResp implements Serializable {
    private Integer villageId;
    private String villageName;
}
