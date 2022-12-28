package com.msc.user.service;

import com.msc.common.base.PageEntity;
import com.msc.common.vo.Result;
import com.msc.user.entity.SysRole;
import com.msc.user.entity.SysUser;

import java.util.List;

/**
 * @ClassName SysRoleService
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/6/15 18:17
 **/
public interface SysRoleService {
    Result<Object> getRoleList(Integer roleId, Long userId, PageEntity pageEntity);

    Result<Object> saveRole(SysRole sysRole);

    Result<Object> deleteRole(Integer id);

    Result<Object> saveUserRole(SysUser sysUser);
}
