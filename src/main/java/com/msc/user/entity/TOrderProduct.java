package com.msc.user.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 订单产品明细表
 * </p>
 *
 * @author LiuCq
 * @since 2022-07-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TOrderProduct对象", description="订单产品明细表")
public class TOrderProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "订单编号")
    private String orderCode;

    @ApiModelProperty(value = "商品编码")
    private String productCode;

    @ApiModelProperty(value = "商品规格id")
    private Integer productSkuId;

    @ApiModelProperty(value = "购买数量")
    private Integer productSkuCount;

    @ApiModelProperty(value = "单价")
    private BigDecimal price;

    @ApiModelProperty(value = "明细总金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "0已创建1待发货2已发货3已送达4已签收5待退货6已退货")
    private Integer status;

    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private Integer createUserId;

    private Date updateTime;

    private Integer updateUserId;

    @ApiModelProperty(value = "1已关注，0已解绑")
    private String disabled;

    @TableField(exist = false)
    private String productName;

}
