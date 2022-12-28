package com.msc.user.mapper;

import com.msc.common.base.PageEntity;
import com.msc.user.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msc.user.entity.SysRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 菜单资源定义表 Mapper 接口
 * </p>
 *
 * @author LiuCq
 * @since 2022-06-13
 */
@Repository
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> getMenuList(@Param("menuId") Integer menuId, @Param("userId") Long userId, @Param("page")PageEntity pageEntity);

    List<PageEntity> getPageCount(@Param("userId") Long userId, @Param("page") PageEntity pageEntity);

    List<SysRole> getRoleNamesByFunctionId(@Param("id") Integer id);
}
