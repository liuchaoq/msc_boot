package com.msc.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.msc.common.base.PageEntity;
import com.msc.common.util.PasswordUtil;
import com.msc.common.vo.Result;
import com.msc.user.entity.SysFunction;
import com.msc.user.entity.SysRole;
import com.msc.user.entity.SysUser;
import com.msc.user.entity.SysUserRole;
import com.msc.user.mapper.SysFunctionMapper;
import com.msc.user.mapper.SysRoleMapper;
import com.msc.user.mapper.SysUserMapper;
import com.msc.user.mapper.SysUserRoleMapper;
import com.msc.user.service.SysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName SysRoleServiceImpl
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/6/15 18:19
 **/
@Slf4j
@Service
public class SysRoleServiceImpl implements SysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysFunctionMapper sysFunctionMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Override
    public Result<Object> getRoleList(Integer roleId, Long userId, PageEntity pageEntity) {
        try {
            if (roleId == null && pageEntity != null) {
                pageEntity.setRows(sysRoleMapper.getList(userId, roleId, pageEntity));
                pageEntity.setPageCount(sysRoleMapper.getPageCount(userId, pageEntity).get(0).getPageCount());
                return Result.OK(pageEntity);
            } else {
                List<SysRole> rows = sysRoleMapper.getList(userId, roleId, null);
                rows.forEach(v->{
                    List<SysFunction> functionList = sysFunctionMapper.getFunctionsByRoleId(v.getId());
                    v.setFunctionList(functionList);
                });
                return Result.OK(rows);
            }
        } catch (Exception e) {
            log.error("查询角色失败，错误信息：{}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    @Override
    public Result<Object> saveRole(SysRole sysRole) {
        try {
            sysRole.setDisabled("1");
            if (sysRole.getId() == null) {
                sysRoleMapper.insert(sysRole);
            } else {
                sysRoleMapper.updateById(sysRole);
            }
            return Result.OK("操作成功");
        } catch (Exception e) {
            log.error("保存角色失败，错误信息：{}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    @Override
    public Result<Object> deleteRole(Integer id) {
        try {
            sysRoleMapper.deleteById(id);
            return Result.OK("操作成功");
        } catch (Exception e) {
            log.error("删除角色失败，错误信息：{}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    @Override
    public Result<Object> saveUserRole(SysUser sysUser) {
        try {
            QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("name", sysUser.getRoleNames());
            List<SysRole> roleList = sysRoleMapper.selectList(queryWrapper);

            QueryWrapper<SysUserRole> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("sys_user_id", sysUser.getId().intValue());
            sysUserRoleMapper.delete(deleteWrapper);

            if (!CollectionUtils.isEmpty(roleList)) {
                List<SysUserRole> sysUserRoleList = roleList.stream().map(v->{
                    SysUserRole sysUserRole = new SysUserRole();
                    sysUserRole.setSysRoleId(v.getId());
                    sysUserRole.setSysUserId(sysUser.getId().intValue());
                    sysUserRole.setCreateTime(new Date());
                    sysUserRole.setCreateUserId(sysUser.getOId());
                    sysUserRole.setDisabled("1");
                    return sysUserRole;
                }).collect(Collectors.toList());
                sysUserRoleList.forEach(v->{
                    sysUserRoleMapper.insert(v);
                });
            }

            String userName = sysUser.getUsername();
            sysUser.setPassword(PasswordUtil.encrypt(userName.substring(userName.length()-6,userName.length())));
            sysUserMapper.updatePassword(sysUser);
            return Result.OK();
        } catch (Exception e) {
            log.error("为用户分配角色报错:{}",e.getMessage());
            return Result.error("角色分配失败,请联系管理员");
        }
    }
}
