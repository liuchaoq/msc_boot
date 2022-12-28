package com.msc.user.mapper;

import com.msc.common.base.PageEntity;
import com.msc.user.entity.SysZone;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 地区信息表 Mapper 接口
 * </p>
 *
 * @author LiuCq
 * @since 2022-06-22
 */
@Repository
public interface SysZoneMapper extends BaseMapper<SysZone> {

    List<SysZone> getZoneList(@Param("parentCode") String parentCode, @Param("page") PageEntity pageEntity);

    List<PageEntity> getPageCount(@Param("parentCode") String parentCode, @Param("page") PageEntity pageEntity);

    List<SysZone> getChildrenByParentId(@Param("id") Integer id);
}
