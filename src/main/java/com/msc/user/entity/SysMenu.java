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
 * 菜单资源定义表
 * </p>
 *
 * @author LiuCq
 * @since 2022-06-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysMenu对象", description="菜单资源定义表")
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "菜单资源名称")
    private String name;

    @ApiModelProperty(value = "菜单编码")
    private String code;

    @ApiModelProperty(value = "父菜单ID")
    private Integer parentId;

    @ApiModelProperty(value = "前端路由路径")
    private String router;

    @ApiModelProperty(value = "菜单层级路径")
    private String path;

    @ApiModelProperty(value = "菜单名称层级路径")
    private String pathName;

    @ApiModelProperty(value = "操作提示")
    private String tips;
    @ApiModelProperty(value = "父级菜单名")
    @TableField(exist = false)
    private String pName;

    private String disabled;

    private Integer createUserId;

    private Date createTime;

    private Integer updateUserId;

    private Date updateTime;
    @ApiModelProperty(value = "是否是主菜单")
    private String isMainMenu;

    @ApiModelProperty(value = "所属主菜单id")
    private Integer mainMenuId;

    @TableField(exist = false)
    private List<String> roleNames;

}
