package com.msc.user.service;

import com.msc.common.base.PageEntity;
import com.msc.common.vo.Result;
import com.msc.user.entity.SysMenu;

/**
 * @ClassName SysMenuService
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/6/15 18:34
 **/
public interface SysMenuService {
    Result<Object> getMenuList(Integer menuId, Long userId, PageEntity pageEntity);

    Result<Object> saveOrUpdate(SysMenu sysMenu);

    Result<Object> getMainMenuList(Long userId);

    Result<Object> getParentMenuList(int i, Long id, Object o);

    Result<Object> deleteMenu(Integer id);
}
