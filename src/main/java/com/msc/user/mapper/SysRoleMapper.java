package com.msc.user.mapper;

import com.msc.common.base.BaseMap;
import com.msc.common.base.PageEntity;
import com.msc.user.entity.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author LiuCq
 * @since 2022-06-15
 */
@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<SysRole> getList(@Param("userId") Long userId, @Param("roleId") Integer roleId, @Param("page") PageEntity pageEntity);

    List<PageEntity> getPageCount(@Param("userId") Long userId, @Param("page") PageEntity pageEntity);

    List<SysRole> getRolesByUserId(@Param("userId") Long userId);
}
