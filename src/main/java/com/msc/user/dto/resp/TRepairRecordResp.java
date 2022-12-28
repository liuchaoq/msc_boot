package com.msc.user.dto.resp;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @ClassName TRepairRecordResp
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/6/24 16:05
 **/
@Data
public class TRepairRecordResp {
    private Integer id;
    private String name;//报修人姓名
    private Date createTime;
    private String status;
    private String username;//联系电话
    private String houseInfo;
    private String description;
}
