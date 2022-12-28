package com.msc.user.service;

import com.msc.common.base.PageEntity;
import com.msc.common.vo.Result;
import com.msc.user.entity.SysZone;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 地区信息表 服务类
 * </p>
 *
 * @author LiuCq
 * @since 2022-06-22
 */
public interface SysZoneService extends IService<SysZone> {

    Result<Object> getZoneList(String parentCode, PageEntity pageEntity);

    Result<Object> saveZone(SysZone sysZone);

    Result<Object> deleteZone(Integer id);
}
