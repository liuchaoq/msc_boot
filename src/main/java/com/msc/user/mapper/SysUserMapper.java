package com.msc.user.mapper;

import com.msc.common.base.PageEntity;
import com.msc.common.weixin.entity.WeiXinUser;
import com.msc.user.dto.LoginDTO;
import com.msc.user.dto.UserAddressDTO;
import com.msc.user.dto.UserDTO;
import com.msc.user.dto.req.LoginReq;
import com.msc.user.dto.resp.LoginResp;
import com.msc.user.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuCq
 * @since 2022-04-12
 */
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

    Integer updateUserSubStatus(@Param("user") WeiXinUser weiXinUser);

    List<LoginDTO> login(@Param("params") LoginReq loginReq);

    List<SysUser> getUserPage(@Param("userId") Long userId, @Param("page") PageEntity pageEntity);

    List<PageEntity> getPageCount(@Param("userId") Long userId, @Param("page") PageEntity pageEntity);

    List<SysUser> getUserById(@Param("managerId") Long managerId, @Param("userId") Integer userId);

    List<UserAddressDTO> getUserAddress(@Param("userIds") List<Long> ids);

    List<UserAddressDTO> getMyAddressInfos(@Param("userId") Integer oId);

    Integer resetPassword(@Param("user") SysUser user);

    List<UserDTO> getFamilyMember(@Param("user") SysUser user);

    SysUser getManagerByHouseInfoId(@Param("houseInfoId") Integer houseInfoId);

    Integer updatePassword(@Param("user") SysUser sysUser);

    Integer updateUserName(@Param("user") SysUser sysUser);
}
