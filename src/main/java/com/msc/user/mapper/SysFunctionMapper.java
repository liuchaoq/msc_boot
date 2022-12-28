package com.msc.user.mapper;

import com.msc.common.base.PageEntity;
import com.msc.user.entity.SysFunction;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msc.user.entity.SysRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 数据权限表 Mapper 接口
 * </p>
 *
 * @author LiuCq
 * @since 2022-06-23
 */
@Repository
public interface SysFunctionMapper extends BaseMapper<SysFunction> {

    List<SysFunction> selectFunction(@Param("userId") Long userId, @Param("id") Integer id1, @Param("page") PageEntity pageEntity);

    List<PageEntity> getPageCount(@Param("userId") Long userId, @Param("page") PageEntity pageEntity);

    List<SysFunction> checkState(@Param("userId") Long userId, @Param("funId") Integer funId);

    List<SysRole> getRoleNamesByFunctionId(@Param("id") Integer id);

    List<SysFunction> getFunctionsByRoleId(@Param("id") Integer id);
}
