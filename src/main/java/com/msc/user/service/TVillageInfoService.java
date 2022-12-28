package com.msc.user.service;

import com.msc.common.vo.Result;
import com.msc.user.entity.SysUser;
import com.msc.user.entity.TVillageInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiuCq
 * @since 2022-06-22
 */
public interface TVillageInfoService extends IService<TVillageInfo> {

    Result<Object> getManagerVillage(SysUser user);

    Result<Object> getListBySys(SysUser sysUser, HttpServletRequest request);

    Result<Object> getVillageManagerBySys(SysUser sysUser);

    Result<Object> saveVillageBySys(SysUser sysUser, TVillageInfo tVillageInfo);

    /**
    * @Description: 获取负责人管辖的村子住户信息
    * @Param: [user 责任人信息, 村子id]
    * @return: com.msc.common.vo.Result<java.lang.Object>
    * @Author: liuCq
    * @Date: 2022/7/8
    */
    Result<Object> houseList(SysUser user, Integer id);
}
