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
 * 在线报修记录
 * </p>
 *
 * @author LiuCq
 * @since 2022-06-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TRepairRecord对象", description="在线报修记录")
public class TRepairRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "报修人id	报修人id")
    private Integer sysUserId;

    @ApiModelProperty(value = "住址id")
    private Integer houseInfoId;

    @ApiModelProperty(value = "问题描述")
    private String description;

    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private Integer createUserId;

    private Date updateTime;

    private Integer updateUserId;

    @ApiModelProperty(value = "1已关注，0已解绑")
    private String disabled;

    @ApiModelProperty(value = "当前状态：0：待维修，1.已修复")
    private Integer status;


}
