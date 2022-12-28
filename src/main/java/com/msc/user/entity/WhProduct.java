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
 * 产品表，水费（没有规格数据，price作为水费价格）、商品（必须有规格数据，价格按规格定价计算）,已审核通过的商品内容不可修改，如需修改，先下架，在发布一条修改过后的商品提交审核
 * </p>
 *
 * @author LiuCq
 * @since 2022-07-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="WhProduct对象", description="产品表，水费（没有规格数据，price作为水费价格）、商品（必须有规格数据，价格按规格定价计算）,已审核通过的商品内容不可修改，如需修改，先下架，在发布一条修改过后的商品提交审核")
public class WhProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "产品编码")
    private String code;

    @ApiModelProperty(value = "所属分类（type=2）时启用")
    private Integer productClassId;

    @ApiModelProperty(value = "1.水费，2.商品或服务")
    private Integer productType;

    @ApiModelProperty(value = "拥有人")
    private Integer ownerId;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "商品简述")
    private String description;

    @ApiModelProperty(value = "商品详情")
    private String intro;

    @ApiModelProperty(value = "发布状态0待编辑，1已发布，可以送审")
    private Integer showStatus;

    @ApiModelProperty(value = "审核状态：0待审核，1审核通过，2审核不通过")
    private Integer reviewStatus;

    @ApiModelProperty(value = "审核不通过原因")
    private String reviewMessage;

    @ApiModelProperty(value = "上架类型：0审核通过自动上架，1定时上架")
    private Integer shelvesType;

    @ApiModelProperty(value = "上架状态：0下架，1上架")
    private Integer shelvesStatus;

    @ApiModelProperty(value = "上架时间")
    private Date shelvesTime;

    @ApiModelProperty(value = "产品基础价格，具体价格有规格价格决定，不同规格定价不同")
    private BigDecimal price;

    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private Integer createUserId;

    private Date updateTime;

    private Integer updateUserId;

    @ApiModelProperty(value = "1已关注，0已解绑")
    private String disabled;


}
