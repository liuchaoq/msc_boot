package com.msc.user.service.impl;

import com.msc.common.base.PageEntity;
import com.msc.common.vo.Result;
import com.msc.user.dto.resp.TRepairRecordResp;
import com.msc.user.entity.TRepairRecord;
import com.msc.user.mapper.TRepairRecordMapper;
import com.msc.user.service.TRepairRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 在线报修记录 服务实现类
 * </p>
 *
 * @author LiuCq
 * @since 2022-06-24
 */
@Service
public class TRepairRecordServiceImpl extends ServiceImpl<TRepairRecordMapper, TRepairRecord> implements TRepairRecordService {
    @Autowired
    private TRepairRecordMapper tRepairRecordMapper;

    @Override
    public Result<Object> getRepairListByManager(Integer recordId, Long userId, PageEntity pageEntity) {
        List<TRepairRecordResp> list = tRepairRecordMapper.getRepairList(recordId,userId,pageEntity);
        if (recordId != null) {
            return Result.OK(list.get(0));
        }
        PageEntity page = tRepairRecordMapper.getPageCount(userId, pageEntity);
        pageEntity.setRows(list);
        pageEntity.setPageCount(page.getPageCount());
        return Result.OK(pageEntity);
    }
}
