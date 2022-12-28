package com.msc.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 数据权限表
 * </p>
 *
 * @author LiuCq
 * @since 2022-06-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysFunction对象", description="数据权限表")
public class SysFunction implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "权限cde")
    private String code;

    @ApiModelProperty(value = "权限注释")
    private String remark;

    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private Integer createUserId;

    private Date updateTime;

    private Integer updateUserId;

    @ApiModelProperty(value = "是否启用，1启用、0")
    private String disabled;

    @TableField(exist = false)
    private List<String> roleNames;

}
