package com.msc.user.mapper;

import com.msc.common.base.PageEntity;
import com.msc.user.dto.resp.TRepairRecordResp;
import com.msc.user.entity.TRepairRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 在线报修记录 Mapper 接口
 * </p>
 *
 * @author LiuCq
 * @since 2022-06-24
 */
@Repository
public interface TRepairRecordMapper extends BaseMapper<TRepairRecord> {

    List<TRepairRecordResp> getRepairList(@Param("id") Integer recordId, @Param("userId") Long userId, @Param("page") PageEntity pageEntity);

    PageEntity getPageCount(@Param("userId") Long userId, @Param("page") PageEntity pageEntity);
}
