package com.msc.user.entity;

import java.math.BigDecimal;
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
 * 商品规格表    交易订单主要依靠此表的操作来进行生成
 * </p>
 *
 * @author LiuCq
 * @since 2022-07-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="WhProductSku对象", description="商品规格表    交易订单主要依靠此表的操作来进行生成")
public class WhProductSku implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "所属商品编码")
    private String productCode;

    @ApiModelProperty(value = "计算单位：如件、箱。。。。等")
    private String unit;

    @ApiModelProperty(value = "规格对应的单价，默认取产品基础价格")
    private BigDecimal skuPrice;

    @ApiModelProperty(value = "是否默认规格，1是0否，每种商品必须有默认规格")
    private Integer isDefault;

    @ApiModelProperty(value = "在售数量（剩余数量）=上架数量-交易数量")
    private Integer sellNum;

    @ApiModelProperty(value = "上架数量")
    private Integer shelvesNum;

    @ApiModelProperty(value = "交易数量，创建订单+1，取消订单-1")
    private Integer tradeNum;

    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private Integer createUserId;

    private Date updateTime;

    private Integer updateUserId;

    @ApiModelProperty(value = "1已关注，0已解绑")
    private String disabled;


}
