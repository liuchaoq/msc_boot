package com.msc.user.service;

import com.msc.common.base.PageEntity;
import com.msc.common.vo.Result;
import com.msc.user.entity.TRepairRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 在线报修记录 服务类
 * </p>
 *
 * @author LiuCq
 * @since 2022-06-24
 */
public interface TRepairRecordService extends IService<TRepairRecord> {

    /**
     *
    **/
    Result<Object> getRepairListByManager(Integer recordId, Long userId, PageEntity pageEntity);
}
