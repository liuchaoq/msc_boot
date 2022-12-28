package com.msc.user.mapper;

import com.msc.common.base.PageEntity;
import com.msc.user.dto.resp.MeterListResp;
import com.msc.user.dto.resp.MyRecordsResp;
import com.msc.user.entity.MeterRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiuCq
 * @since 2022-07-08
 */
@Repository
public interface MeterRecordMapper extends BaseMapper<MeterRecord> {

    List<MeterListResp> getRecordsByManger(@Param("page") PageEntity pageEntity);

    Integer getPageCount(@Param("page") PageEntity pageEntity);

    List<HashMap<String, Object>> getPieData(@Param("params") Map<String, Object> params);

    List<HashMap<String, Object>> getAmountData(@Param("params") Map<String, Object> params);

    List<MyRecordsResp> getMeterRecordsByUser(@Param("userId") Long userId, @Param("year") Integer year);
}
