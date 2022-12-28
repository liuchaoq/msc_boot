package com.msc.user.mapper;

import com.msc.common.base.PageEntity;
import com.msc.user.entity.TVillageInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiuCq
 * @since 2022-06-22
 */
@Repository
public interface TVillageInfoMapper extends BaseMapper<TVillageInfo> {

    List<TVillageInfo> getListBySys(@Param("vId") Integer vId, @Param("uId") Long uId, @Param("page") PageEntity pageEntity);

    List<PageEntity> getListBySysCount(@Param("vId") Integer vId, @Param("uId") Long uId, @Param("page") PageEntity pageEntity);
}
