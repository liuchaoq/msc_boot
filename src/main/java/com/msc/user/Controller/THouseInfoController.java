package com.msc.user.Controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.msc.common.base.BaseController;
import com.msc.common.vo.Result;
import com.msc.user.entity.THouseInfo;
import com.msc.user.service.THouseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 家户水表信息 前端控制器
 * </p>
 *
 * @author LiuCq
 * @since 2022-06-28
 */
@Slf4j
@RestController
public class THouseInfoController extends BaseController {
    @Autowired
    private THouseInfoService tHouseInfoService;

    @GetMapping("wx/house/getHNumByVId")
    public Result<Object> getHNumByVId(@RequestParam("villageId") Integer villageId) {
        try {
            QueryWrapper<THouseInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("village_id",villageId).orderByAsc("id");
            return Result.OK(tHouseInfoService.list(queryWrapper));
        } catch (Exception e) {
            log.error("微信客户获取门牌列表失败:{}", e.getMessage());
            return Result.error("获取门牌信息失败,请联系管理员");
        }
    }

}
