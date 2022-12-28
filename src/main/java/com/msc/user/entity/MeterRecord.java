package com.msc.user.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiuCq
 * @since 2022-07-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="MeterRecord对象", description="")
public class MeterRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "乡村id")
    private Integer villageId;

    @ApiModelProperty(value = "住户id")
    private Integer houseId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "上次抄表时间（第一次使用需要初始化）")
    private Date lastCopyTime;

    @ApiModelProperty(value = "上次抄表读数（第一次使用需要初始化）")
    private Integer lastNum;

    @ApiModelProperty(value = "当前读数")
    private Integer num;

    @ApiModelProperty(value = "使用数量")
    private Integer useCount;

    @ApiModelProperty(value = "应收金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "支付状态0未支付1已支付")
    private Integer payStatus;

    @ApiModelProperty(value = "抄表年份")
    private Integer year;

    @ApiModelProperty(value = "抄表月份")
    private Integer month;

    @ApiModelProperty(value = "抄表时单价")
    private BigDecimal price;

    @ApiModelProperty(value = "抄表时水费产品编码")
    private String productCode;

    private Date createTime;

    @ApiModelProperty(value = "抄表人")
    private Integer createUserId;

    @ApiModelProperty(value = "抄表时间")
    private Date updateTime;

    private Integer updateUserId;

    @ApiModelProperty(value = "1已关注，0已解绑")
    private String disabled;

    @ApiModelProperty(value = "读数照片")
    private String img;

    @ApiModelProperty(value = "当月是否已抄表0否1是")
    @TableField(exist = false)
    private Integer isCopy;
}
