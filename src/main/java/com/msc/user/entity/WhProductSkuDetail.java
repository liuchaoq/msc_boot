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
 * 商品规格明细表
 * </p>
 *
 * @author LiuCq
 * @since 2022-07-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="WhProductSkuDetail对象", description="商品规格明细表")
public class WhProductSkuDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer productSkuId;

    @ApiModelProperty(value = "规格明细名称")
    private String skuName;

    @ApiModelProperty(value = "规格明细值")
    private String skuValue;

    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private Integer createUserId;

    private Date updateTime;

    private Integer updateUserId;

    @ApiModelProperty(value = "1已关注，0已解绑")
    private String disabled;


}
