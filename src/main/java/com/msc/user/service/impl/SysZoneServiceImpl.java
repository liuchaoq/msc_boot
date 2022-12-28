package com.msc.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.msc.common.base.PageEntity;
import com.msc.common.constant.DictConstant;
import com.msc.common.vo.Result;
import com.msc.user.entity.SysZone;
import com.msc.user.mapper.SysZoneMapper;
import com.msc.user.service.SysZoneService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 地区信息表 服务实现类
 * </p>
 *
 * @author LiuCq
 * @since 2022-06-22
 */
@Slf4j
@Service
public class SysZoneServiceImpl extends ServiceImpl<SysZoneMapper, SysZone> implements SysZoneService {

    @Autowired
    private SysZoneMapper sysZoneMapper;
    @Override
    public Result<Object> getZoneList(String parentCode, PageEntity pageEntity) {
        try {

            if (pageEntity == null) {
                return Result.OK(sysZoneMapper.getZoneList(parentCode, pageEntity));
            } else {
                List<PageEntity> list = sysZoneMapper.getPageCount(parentCode, pageEntity);
                pageEntity.setPageCount(list.get(0).getPageCount());
                pageEntity.setRows(sysZoneMapper.getZoneList(parentCode, pageEntity));
                return Result.OK(pageEntity);
            }
        } catch (Exception e){
            log.error("获取地区列表报错,parentCode:{},错误信息:{}",parentCode, e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    @Override
    public Result<Object> saveZone(SysZone sysZone) {
        QueryWrapper<SysZone> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", sysZone.getParentCode());
        List<SysZone> parent = sysZoneMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(parent)) {
            sysZone.setLevel(parent.get(0).getLevel() + 1);
            if (parent.get(0).getLevel() == 0) {
                sysZone.setAreaType(DictConstant.SYS_ZONE_TYPE_PROVINCE);
            } else if (parent.get(0).getLevel() == 1) {
                sysZone.setAreaType(DictConstant.SYS_ZONE_TYPE_CITY);
            } else if (parent.get(0).getLevel() == 2) {
                sysZone.setAreaType(DictConstant.SYS_ZONE_TYPE_AREA);
            } else if (parent.get(0).getLevel() == 3) {
                sysZone.setAreaType(DictConstant.SYS_ZONE_TYPE_TOWN);
            } else {
                return Result.error("乡镇为可维护的最低行政级别！");
            }
        }
        try {
            if (sysZone.getId() == null) {
                sysZoneMapper.insert(sysZone);
            } else {
                sysZoneMapper.updateById(sysZone);
            }
            return Result.OK("保存地区信息成功!");
        } catch (Exception e) {
            log.error("保存地区信息失败:{}",e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    @Override
    public Result<Object> deleteZone(Integer id) {
        try {
            List<SysZone> checkResult = sysZoneMapper.getChildrenByParentId(id);
            if (CollectionUtils.isEmpty(checkResult)) {
                sysZoneMapper.deleteById(id);
                return Result.OK("操作成功");
            } else {
                return Result.error("该地区还有已存在的下级地区，不可删除");
            }
        } catch (Exception e) {
            log.error("删除地区信息报错:{}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }
}
