package com.msc.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Arrays;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiuCq
 * @since 2022-06-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TVillageInfo对象", description="")
public class TVillageInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "村名")
    private String name;

    @ApiModelProperty(value = "省份编码")
    private String provinceCode;

    @ApiModelProperty(value = "城市编码")
    private String cityCode;

    @ApiModelProperty(value = "区县code")
    private String areaCode;

    @ApiModelProperty(value = "乡镇code")
    private String townCode;

    private Long createUserId;

    private Date createTime;

    private Long updateUserId;

    private Date updateTime;

    private String disabled;

    @ApiModelProperty(value = "上级地区总的名字-省、市、区县、乡镇")
    @TableField(exist = false)
    private String parentName;

    @ApiModelProperty(value = "收费负责人")
    @TableField(exist = false)
    private String managerName;

    @ApiModelProperty(value = "收费负责人集合")
    @TableField(exist = false)
    private List<String> managerNameList;

    public void setManagerName(String managerName) {
        this.managerName = managerName;
        if (StringUtils.isNotBlank(managerName)) {
            this.managerNameList = Arrays.stream(managerName.split(",")).collect(Collectors.toList());
        }
    }
}
