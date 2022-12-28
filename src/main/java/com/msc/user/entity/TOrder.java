package com.msc.user.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Calendar;
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
 * 订单主表
 * </p>
 *
 * @author LiuCq
 * @since 2022-07-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TOrder对象", description="订单主表")
public class TOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "订单编号")
    private String code;

    @ApiModelProperty(value = "0水电费订单、1商城产品订单")
    private Integer orderType;

    @ApiModelProperty(value = "0待支付1已支付2已取消3已删除4待退款5已退款")
    private Integer payStatus;

    @ApiModelProperty(value = "订单总金额")
    private BigDecimal orderAmount;

    @ApiModelProperty(value = "订单收件人地址id——sys_user_address")
    private Integer userAddressId;

    @ApiModelProperty(value = "客户id")
    private Integer userId;

    @ApiModelProperty(value = "水电费使用，家户id，其他可空")
    private Integer houseId;

    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private Integer createUserId;

    private Date updateTime;

    private Integer updateUserId;

    @ApiModelProperty(value = "1已关注，0已解绑")
    private String disabled;

    public static void main(String[] args) {
        System.out.println(Calendar.getInstance().get(Calendar.MONTH));
        System.out.println(Calendar.getInstance().get(Calendar.YEAR));
    }
}
