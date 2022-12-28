package com.msc.user.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.msc.user.entity.SysMenu;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName MenuDTO
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/6/15 10:55
 **/
@Data
public class MenuDTO {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "菜单资源名称")
    private String name;

    @ApiModelProperty(value = "父菜单ID")
    private Integer parentId;

    @ApiModelProperty(value = "前端路由路径")
    private String router;

    @ApiModelProperty(value = "是否主菜单")
    private String isMainMenu;

    @ApiModelProperty(value = "所属主菜单id")
    private Integer mainMenuId;

    @TableField(exist = false)
    private List<MenuDTO> children;
}
