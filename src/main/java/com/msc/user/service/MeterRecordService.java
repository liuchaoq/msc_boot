package com.msc.user.service;

import com.msc.common.base.PageEntity;
import com.msc.common.vo.Result;
import com.msc.user.dto.req.MeterListReq;
import com.msc.user.dto.req.MeterReq;
import com.msc.user.dto.req.StatisticsReq;
import com.msc.user.entity.MeterRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.msc.user.entity.SysUser;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiuCq
 * @since 2022-07-08
 */
public interface MeterRecordService extends IService<MeterRecord> {

    Result<Object> saveRecords(SysUser sysUser, MeterReq meterReq);

    Result<Object> getRecordsByManger(SysUser sysUser, PageEntity pageEntity);

    Result<Object> getMeterRecordsByUser(SysUser sysUser, StatisticsReq statisticsReq);
}
