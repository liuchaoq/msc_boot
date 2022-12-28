package com.msc.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.msc.common.base.PageEntity;
import com.msc.common.vo.Result;
import com.msc.common.weixin.entity.WeiXinUser;
import com.msc.user.dto.UserAddressDTO;
import com.msc.user.dto.UserDTO;
import com.msc.user.dto.req.LoginReq;
import com.msc.user.dto.req.WxSaveUserReq;
import com.msc.user.dto.resp.LoginResp;
import com.msc.user.entity.SysUser;
import com.msc.user.entity.TRepairRecord;
import com.msc.user.entity.TUserHouseRelation;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName TestService
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/4/11 14:30
 **/
public interface SysUserService {

    SysUser test(String content);

    SysUserService getServiceByType(String type);

    Integer saveSysUser(SysUser sysUser);

    Integer selectCount(QueryWrapper<SysUser> queryWrapper);

    Integer updateUserSubStatus(WeiXinUser weiXinUser);

    Result<Object> login(LoginReq loginReq, HttpServletRequest request);

    Result<Object> getUserList(Long userId, PageEntity pageEntity);

    Integer updateUserByWxAuth(SysUser sysUser);

    Result<Object> getUserById(Long managerId, Integer userId);

    /**
    *   保存用户与填写的住址关联关系
    **/
    Result<Object> saveUserHouseInfo(List<TUserHouseRelation> relationList);

    List<UserAddressDTO> getMyAddressInfos(SysUser user);

    void resetPassword(SysUser user);

    List<UserDTO> getFamilyMember(SysUser user);

    Result<Object> saveOrUpdateRepairInfo(TRepairRecord tRepairRecord) throws Exception;

    Result<Object> deleteHouseInfo(SysUser user, String houseInfoId);

    Result<Object> editHouseInfoFromWx(SysUser wxUser, WxSaveUserReq wxSaveUserReq);
}
