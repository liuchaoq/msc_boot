package com.msc.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.msc.common.base.PageEntity;
import com.msc.common.vo.Result;
import com.msc.user.entity.SysFunction;
import com.msc.user.entity.SysRole;
import com.msc.user.entity.SysRoleFunction;
import com.msc.user.entity.SysUser;
import com.msc.user.mapper.SysFunctionMapper;
import com.msc.user.mapper.SysRoleFunctionMapper;
import com.msc.user.service.SysFunctionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 数据权限表 服务实现类
 * </p>
 *
 * @author LiuCq
 * @since 2022-06-23
 */
@Slf4j
@Service
public class SysFunctionServiceImpl extends ServiceImpl<SysFunctionMapper, SysFunction> implements SysFunctionService {

    @Autowired
    private SysFunctionMapper sysFunctionMapper;
    @Autowired
    private SysRoleFunctionMapper sysRoleFunctionMapper;

    @Override
    public Result<Object> selectList(SysUser sysUser, Integer id, PageEntity pageEntity) {
        try {
            List<SysFunction> rows = sysFunctionMapper.selectFunction(sysUser.getId(), id, pageEntity);
            if (id == null) {
                List<PageEntity> list = sysFunctionMapper.getPageCount(sysUser.getId(), pageEntity);
                pageEntity.setRows(rows);
                pageEntity.setPageCount(list.get(0).getPageCount());
                return Result.OK(pageEntity);
            } else {
                rows.forEach(v->{
                    List<SysRole> roles= sysFunctionMapper.getRoleNamesByFunctionId(v.getId());
                    v.setRoleNames(roles.stream().map(SysRole::getName).collect(Collectors.toList()));
                });
                return Result.OK(rows);
            }
        } catch (Exception e) {
            log.error("获取数据权限列表报错:{}", e.getMessage());
            return Result.error(e.getMessage());
        }

    }

    @Override
    public boolean checkState(Long userId, Integer funId) {
        List<SysFunction> list = sysFunctionMapper.checkState(userId, funId);
        return false;
    }
}
