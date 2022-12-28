package com.msc.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 地区信息表
 * </p>
 *
 * @author LiuCq
 * @since 2022-06-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysZone对象", description="地区信息表")
public class SysZone implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String code;

    private String areaName;

    private String areaType;

    private Integer level;

    private String parentCode;

    private String aliCode;

    private String wxCode;

    private Long createBy;

    private Long updateBy;

    private Date gmtCreate;

    private Date gmtModified;

    private Integer isDelete;

    private String nameSplice;

    private String codeSplice;


}
