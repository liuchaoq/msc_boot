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
 * 水站管理员所管村子
 * </p>
 *
 * @author LiuCq
 * @since 2022-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TUserVillageManager对象", description="水站管理员所管村子")
public class TUserVillageManager implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "系统用户id")
    private Long sysUserId;

    @ApiModelProperty(value = "村子id")
    private Long tVillageId;

    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private Integer createUserId;

    private Date updateTime;

    private Integer updateUserId;

    private String disabled;


}
