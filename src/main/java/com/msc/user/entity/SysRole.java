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
 * 角色表
 * </p>
 *
 * @author LiuCq
 * @since 2022-06-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysRole对象", description="角色表")
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "角色编码")
    private String code;

    @ApiModelProperty(value = "失效标记，用于系统记录的逻辑删除，1：True表示删除")
    private String disabled;

    @ApiModelProperty(value = "创建人用户ID，登录账号表的loginId")
    private Integer createUserId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "最后一次修改人用户ID，登录账号表的loginId")
    private Integer updateUserId;

    @ApiModelProperty(value = "最后一次修改时间")
    private Date updateTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "该角色拥有的数据权限集合")
    private List<SysFunction> functionList;

    @TableField(exist = false)
    @ApiModelProperty(value = "该角色拥有的菜单集合")
    private List<SysMenu> menuList;
}
