package com.msc.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.msc.common.util.encryption.EncryptedString;
import com.msc.user.dto.UserAddressDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author liuCq
 * @since 2022-04-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysUser对象", description="")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户编号")
    private String code;

    @ApiModelProperty(value = "手机号")
    private String username;

    @ApiModelProperty(value = "预留字段")
    private String password;

    @ApiModelProperty(value = "公众号open_id")
    private String openId;

    @TableField(exist = false)
    @ApiModelProperty(value = "乡村编码")
    private String villageCode;

    @ApiModelProperty(value = "绑定家户信息id")
    private Integer houseId;

    @ApiModelProperty(value = "个人姓名")
    private String name;

    @ApiModelProperty(value = "关注时间")
    private Date subTime;

    @ApiModelProperty(value = "头像")
    private String head;

    @ApiModelProperty(value = "微信昵称")
    private String nickname;

    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private String createUserId;

    private Date updateTime;

    private String updateUserId;

    private String disabled;

    @TableField(exist = false)
    private List<SysRole> roleList;

    @TableField(exist = false)
    private Integer oId;

    @TableField(exist = false)
    private String userType;

    @TableField(exist = false)
    private String address;

    @TableField(exist = false)
    private String roleNameStr;

    //当前用户已拥有角色名称集合
    @TableField(exist = false)
    private List<String> roleNames;

    //操作分配角色人员所拥有的角色集合
    @TableField(exist = false)
    private List<String> roles;

    public void setRoleNameStr(String roleNameStr) {
        this.roleNameStr = roleNameStr;
        if (StringUtils.isNotBlank(roleNameStr)) {
            List<String> list = new ArrayList<>();
            for (String roleName: roleNameStr.split(",")) {
                if (StringUtils.isNotBlank(roleName)) {
                    list.add(roleName);
                }
            }
            this.roleNames = list;
        }

    }

    @TableField(exist = false)
    private List<UserAddressDTO> addressList;

    @TableField(exist = false)
    private EncryptedString aesData;
}
