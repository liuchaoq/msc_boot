package com.msc.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.msc.common.base.PageEntity;
import com.msc.common.constant.CmsConstant;
import com.msc.common.vo.Result;
import com.msc.user.dto.resp.UserManagerVillageResp;
import com.msc.user.entity.SysUser;
import com.msc.user.entity.THouseInfo;
import com.msc.user.entity.TUserVillageManager;
import com.msc.user.entity.TVillageInfo;
import com.msc.user.mapper.SysUserMapper;
import com.msc.user.mapper.THouseInfoMapper;
import com.msc.user.mapper.TUserVillageManagerMapper;
import com.msc.user.mapper.TVillageInfoMapper;
import com.msc.user.service.TVillageInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiuCq
 * @since 2022-06-22
 */
@Slf4j
@Transactional
@Service
public class TVillageInfoServiceImpl extends ServiceImpl<TVillageInfoMapper, TVillageInfo> implements TVillageInfoService {

    @Autowired
    private TUserVillageManagerMapper tUserVillageManagerMapper;
    @Autowired
    private TVillageInfoMapper tVillageInfoMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private THouseInfoMapper tHouseInfoMapper;
    /**
    * @Description: 微信端根据管理员身份获取管辖村子列表
    * @Param: [user]
    * @return: com.msc.common.vo.Result<java.lang.Object>
    * @Author: liuCq
    * @Date: 2022/6/29
    */
    @Override
    public Result<Object> getManagerVillage(SysUser user) {
        if (CollectionUtils.isEmpty(user.getRoleList())
                || user.getRoleList().stream().noneMatch(v->v.getId() == CmsConstant.WATER_MANAGER_ROLE)) {
            return Result.error("您没有管理权限,请联系管理员");
        }
        try {
            List<UserManagerVillageResp> list = tUserVillageManagerMapper.getManagerVillage(user.getId());
            return Result.OK(list);
        } catch (Exception e) {
            log.error("获取管理村子信息报错,管理员id:{},报错信息:{}",user.getId(),e.getMessage());
            return Result.error("获取管理村子信息报错,请联系管理员");
        }
    }

    @Override
    public Result<Object> getListBySys(SysUser sysUser, HttpServletRequest request) {
        try {
            String idStr = request.getParameter("id");
            Integer id = StringUtils.isEmpty(idStr)?null:Integer.parseInt(idStr);
            PageEntity pageEntity = new PageEntity(request);
            List<TVillageInfo> villageInfoList = tVillageInfoMapper.getListBySys(id, sysUser.getId(), pageEntity);
            if (pageEntity != null) {
                pageEntity.setRows(villageInfoList);
                if (CollectionUtils.isEmpty(villageInfoList)) {
                    pageEntity.setPageCount(0);
                } else {
                    List<PageEntity> countList = tVillageInfoMapper.getListBySysCount(id, sysUser.getId(), pageEntity);
                    pageEntity.setPageCount(countList.get(0).getPageCount());
                }
                return Result.OK(pageEntity);
            } else {
                return Result.OK(villageInfoList);
            }
        } catch (Exception e) {
            log.error("系统维护乡村列表查询报错:{}",e.getMessage());
            return Result.error("获取村子列表失败,请联系管理员");
        }


    }

    @Override
    public Result<Object> getVillageManagerBySys(SysUser sysUser) {
        if (!checkUserRole(sysUser).isSuccess()) {
            return checkUserRole(sysUser);
        }
        List<HashMap<String, Object>> list = tUserVillageManagerMapper.getManagerList(sysUser.getId(), CmsConstant.WATER_MANAGER_ROLE);
        if (!CollectionUtils.isEmpty(list)) {
            return Result.OK(list.stream().map(v->{
                return v.get("manager");
            }).collect(Collectors.toList()));
        } else {
            return Result.OK();
        }

    }

    @Override
    public Result<Object> saveVillageBySys(SysUser sysUser, TVillageInfo tVillageInfo) {
        if (!checkUserRole(sysUser).isSuccess()) {
            return checkUserRole(sysUser);
        }
        if (tVillageInfo.getId() == null) {
            QueryWrapper<TVillageInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("town_code", tVillageInfo.getTownCode())
                    .eq("name", tVillageInfo.getName());
            Integer count = tVillageInfoMapper.selectCount(queryWrapper);
            if (count > 0) {
                return Result.error("该村名已存在");
            }
        }
        try {
            if (tVillageInfo.getId() == null) {
                tVillageInfo.setCreateUserId(Long.parseLong(sysUser.getOId().toString()));
                tVillageInfo.setCreateTime(new Date());
                tVillageInfoMapper.insert(tVillageInfo);
            }
            if (sysUser.getId() != null) {
                TUserVillageManager tUserVillageManager = new TUserVillageManager();
                tUserVillageManager.setSysUserId(Long.parseLong(sysUser.getOId().toString()));
                tUserVillageManager.setCreateTime(new Date());
                tUserVillageManager.setCreateUserId(sysUser.getOId());
                tUserVillageManager.setTVillageId(tVillageInfo.getId());
                tUserVillageManagerMapper.insert(tUserVillageManager);
            } else {
                Map<String,Object> params = new HashMap<>();
                params.put("t_village_id", tVillageInfo.getId());
                tUserVillageManagerMapper.deleteByMap(params);
                if (!CollectionUtils.isEmpty(tVillageInfo.getManagerNameList())) {
                    List<String> userNameList = tVillageInfo.getManagerNameList().stream().map(v->{
                        return v.split("-")[1];
                    }).collect(Collectors.toList());
                    QueryWrapper<SysUser> userQueryWrapper = new QueryWrapper<>();
                    userQueryWrapper.in("username", userNameList);
                    List<SysUser> userList = sysUserMapper.selectList(userQueryWrapper);
                    if (!CollectionUtils.isEmpty(userList)) {
                        for (SysUser user: userList) {
                            TUserVillageManager tUserVillageManager = new TUserVillageManager();
                            tUserVillageManager.setSysUserId(Long.parseLong(user.getId().toString()));
                            tUserVillageManager.setCreateTime(new Date());
                            tUserVillageManager.setCreateUserId(sysUser.getOId());
                            tUserVillageManager.setTVillageId(tVillageInfo.getId());
                            tUserVillageManagerMapper.insert(tUserVillageManager);
                        }
                    }
                } else {
                    return Result.OK("移除负责人成功");
                }
            }
            return Result.OK("添加成功");
        } catch (Exception e) {
            log.error("添加村子失败,用户id:{},错误内容:{}",sysUser.getOId(),e.getMessage());
            return Result.error("添加村子失败,请联系管理员");
        }
    }

    @Override
    public Result<Object> houseList(SysUser sysUser, Integer id) {
        if (!checkUserRole(sysUser).isSuccess()) {
            return checkUserRole(sysUser);
        }
        List<THouseInfo> houseInfos = tHouseInfoMapper.getHouseListByManagerAndVId(sysUser.getId(), id);
        return Result.OK(houseInfos);
    }

    private Result<Object> checkUserRole(SysUser sysUser){
        if (sysUser.getId() != null && CollectionUtils.isEmpty(sysUser.getRoleList())) {
            return Result.error("你没有权限进行此项操作");
        }
        if (sysUser.getId() != null && sysUser.getRoleList().stream().noneMatch(v->v.getId()==CmsConstant.WATER_MANAGER_ROLE)) {
            return Result.error("你没有权限进行此项操作");
        }
        return Result.OK();
    }
}
