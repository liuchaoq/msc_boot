package com.msc.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.msc.common.vo.Result;
import com.msc.user.dto.req.StatisticsReq;
import com.msc.user.entity.SysUser;
import com.msc.user.mapper.MeterRecordMapper;
import com.msc.user.service.WxWaterManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName WxWaterManagerServiceImpl
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/7/15 0:31
 **/
@Service
public class WxWaterManagerServiceImpl implements WxWaterManagerService {
    @Autowired
    private MeterRecordMapper meterRecordMapper;
    @Override
    public Result<Object> queryStatistics(SysUser sysUser, StatisticsReq statisticsReq) {
        Map<String,Object> resultMap = new HashMap<>();
        //查询管理员所管村子、住户用水收费情况，饼状图：list<map<'村名','金额'>>集合
        Map<String,Object> params = JSON.parseObject(JSON.toJSONString(statisticsReq),Map.class);
        params.put("userId", sysUser.getId());
        List<HashMap<String, Object>> pieData = meterRecordMapper.getPieData(params);
        resultMap.put("pieData",pieData);
        //查询所有月份收费对象map<String,list<BigDecimal>>包含应收、实收
        List<HashMap<String, Object>> amountQueryData = meterRecordMapper.getAmountData(params);
        List<BigDecimal> ysAmount = new ArrayList<>();
        List<BigDecimal> ssAmount = new ArrayList<>();
        for (Integer i = 1;i <= 12; i++) {
            Integer month = i;
            if (!CollectionUtils.isEmpty(amountQueryData) && amountQueryData.stream()
                    .anyMatch(v-> month.toString().equals(v.get("month").toString()))) {
                Map<String,Object> map = amountQueryData.stream()
                        .filter(v-> month.toString().equals(v.get("month").toString())).findAny().get();
                ysAmount.add(new BigDecimal(map.get("ys").toString()));
                ssAmount.add(new BigDecimal(map.get("ss").toString()));
            } else {
                ysAmount.add(BigDecimal.ZERO);
                ssAmount.add(BigDecimal.ZERO);
            }
            Map<String,List<BigDecimal>> amountData = new HashMap<>();
            amountData.put("应收金额", ysAmount);
            amountData.put("实收金额", ssAmount);
            resultMap.put("amountData", amountData);
        }
        return Result.OK(resultMap);
    }
}
