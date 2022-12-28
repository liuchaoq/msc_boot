package com.msc.user.Controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.msc.common.base.BaseController;
import com.msc.common.base.PageEntity;
import com.msc.common.vo.Result;
import com.msc.user.entity.SysUser;
import com.msc.user.entity.TVillageInfo;
import com.msc.user.mapper.THouseInfoMapper;
import com.msc.user.service.TVillageInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName SysVillageController
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/6/22 10:19
 **/
@RestController
public class TVillageController extends BaseController {
    @Autowired
    private TVillageInfoService tVillageInfoService;

    @GetMapping("/wx/village/getVillageByTown")
    public Result<Object> getVillageByTown(@RequestParam("townCode") String townCode) {
        if (StringUtils.isEmpty(townCode)) {
            return Result.error("请先选择所属乡镇！");
        }
        QueryWrapper<TVillageInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("town_code", townCode).orderByAsc("id");
        return Result.OK(tVillageInfoService.list(queryWrapper));
    }

    /**
    * @Description: 获取当前登录管理员负责的村子列表
    * @Param: []
    * @return: com.msc.common.vo.Result<java.lang.Object>
    * @Author: liuCq
    * @Date: 2022/7/1
    */
    @GetMapping("wx/village/getManagerVillage")
    public Result<Object> getManagerVillage() {
        SysUser user = getWxUser();
        if (user == null) {
            return Result.error("未获取到当前身份信息,请联系管理员");
        }
        return tVillageInfoService.getManagerVillage(user);
    }

    /**
    * @Description: 根据选择的村子获取所有的住户信息
    * @Param: []
    * @return: com.msc.common.vo.Result<java.lang.Object>
    * @Author: liuCq
    * @Date: 2022/7/1
    */
    @GetMapping("wx/village/houseList")
    public Result<Object> houseList(@RequestParam("id") Integer id) {
        SysUser user = getWxUser();
        if (user == null) {
            return Result.error("未获取到当前身份信息,请联系管理员");
        }
        return tVillageInfoService.houseList(user, id);
    }

    @GetMapping("/sys/village/list.do")
    public Result<Object> getListBySys(HttpServletRequest request) {
        SysUser sysUser = getUser();
        return tVillageInfoService.getListBySys(sysUser, request);
    }

    /**
    * @Description: 获取村子管理员列表
    * @Param: [request]
    * @return: com.msc.common.vo.Result<java.lang.Object>
    * @Author: liuCq
    * @Date: 2022/6/30
    */
    @GetMapping("/sys/village/getVillageManager.do")
    public Result<Object> getVillageManagerBySys() {
        SysUser sysUser = getUser();
        return tVillageInfoService.getVillageManagerBySys(sysUser);
    }

    @PostMapping("sys/village/saveVillageBySys.do")
    public Result<Object> saveVillageBySys(@RequestBody TVillageInfo tVillageInfo) {
        SysUser sysUser = getUser();
        return tVillageInfoService.saveVillageBySys(sysUser, tVillageInfo);
    }
}
