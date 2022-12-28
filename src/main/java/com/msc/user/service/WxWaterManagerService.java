package com.msc.user.service;

import com.alibaba.fastjson.JSONObject;
import com.msc.common.vo.Result;
import com.msc.user.dto.req.StatisticsReq;
import com.msc.user.entity.SysUser;

/**
 * @ClassName WxWaterManagerService
 * @DESCRIPTION 水站管理员业务处理service
 * @AUTHOR liuCq
 * @DATE 2022/7/15 0:29
 **/
public interface WxWaterManagerService {
    Result<Object> queryStatistics(SysUser sysUser, StatisticsReq statisticsReq);
}
