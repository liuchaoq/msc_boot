package com.msc.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.msc.common.base.PageEntity;
import com.msc.common.constant.CmsConstant;
import com.msc.common.vo.Result;
import com.msc.user.entity.SysMenu;
import com.msc.user.entity.SysRole;
import com.msc.user.entity.SysRoleFunction;
import com.msc.user.entity.SysRoleMenu;
import com.msc.user.mapper.SysMenuMapper;
import com.msc.user.mapper.SysRoleMapper;
import com.msc.user.mapper.SysRoleMenuMapper;
import com.msc.user.service.SysMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName SysMenuServiceImpl
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/6/15 18:34
 **/
@Slf4j
@Service
public class SysMenuServiceImpl implements SysMenuService {
    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Override
    public Result<Object> getMenuList(Integer menuId, Long userId, PageEntity pageEntity) {
        try {
            if (pageEntity != null) {
                List<PageEntity> list = sysMenuMapper.getPageCount(userId, pageEntity);
                pageEntity.setPageCount(list.get(0).getPageCount());
                pageEntity.setRows(sysMenuMapper.getMenuList(menuId, userId, pageEntity));
                return Result.OK(pageEntity);
            } else {
                List<SysMenu> rows = sysMenuMapper.getMenuList(menuId, userId, null);
                rows.forEach(v->{
                    List<SysRole> roles= sysMenuMapper.getRoleNamesByFunctionId(v.getId());
                    v.setRoleNames(roles.stream().map(SysRole::getName).collect(Collectors.toList()));
                });
                return Result.OK(rows);
            }
        } catch (Exception e) {
            log.error("获取菜单列表页数据报错：{}",e.getMessage());
            return Result.error(500, e.getMessage());
        }
    }

    @Override
    public Result<Object> saveOrUpdate(SysMenu sysMenu) {
        try {
            Integer sysUserId = 1;
            sysMenu.setDisabled("1");
            if (sysMenu.getId() == null) {
                sysUserId = sysMenu.getCreateUserId();
                sysMenuMapper.insert(sysMenu);
            } else {
                sysUserId = sysMenu.getUpdateUserId();
                sysMenuMapper.updateById(sysMenu);
            }
            QueryWrapper<SysRoleMenu> delWrapper = new QueryWrapper<>();
            delWrapper.eq("sys_menu_id", sysMenu.getId());
            sysRoleMenuMapper.delete(delWrapper);
            if (!CollectionUtils.isEmpty(sysMenu.getRoleNames())) {
                QueryWrapper<SysRole> queryWrapper = new QueryWrapper();
                queryWrapper.in("name", sysMenu.getRoleNames());
                List<SysRole> roleList = sysRoleMapper.selectList(queryWrapper);
                if (!CollectionUtils.isEmpty(roleList)) {
                    Integer id = sysUserId;
                    roleList.stream().forEach(v->{
                        SysRoleMenu sysRoleMenu = new SysRoleMenu();
                        sysRoleMenu.setCreateTime(new Date());
                        sysRoleMenu.setCreateUserId(id);
                        sysRoleMenu.setSysMenuId(sysMenu.getId());
                        sysRoleMenu.setSysRoleId(v.getId());
                        sysRoleMenuMapper.insert(sysRoleMenu);
                    });
                }
            }
        } catch (Exception e) {
            Result.error(500, e.getMessage());
        }
        return Result.OK();
    }

    @Override
    public Result<Object> getMainMenuList(Long userId) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_main_menu", 1).orderByAsc("id");
        return Result.OK(sysMenuMapper.selectList(queryWrapper));
    }

    @Override
    public Result<Object> getParentMenuList(int i, Long id, Object o) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_main_menu", 0)
                .eq("parent_id", 0).orderByAsc("id");
        return Result.OK(sysMenuMapper.selectList(queryWrapper));
    }

    @Override
    public Result<Object> deleteMenu(Integer id) {
        try {
            sysMenuMapper.deleteById(id);
            return Result.OK("删除成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
