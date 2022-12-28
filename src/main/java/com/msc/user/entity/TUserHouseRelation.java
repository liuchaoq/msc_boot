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
 * 住址与用户关系表，一条住址信息可能关联多个用户，一个用户可能关联多处地址
 * </p>
 *
 * @author LiuCq
 * @since 2022-06-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TUserHouseRelation对象", description="住址与用户关系表，一条住址信息可能关联多个用户，一个用户可能关联多处地址")
public class TUserHouseRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer sysUserId;

    private Integer houseInfoId;

    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private Integer createUserId;

    private Date updateTime;

    private Integer updateUserId;

    @ApiModelProperty(value = "1已关注，0已解绑")
    private String disabled;


}
