package com.msc.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.msc.common.base.PageEntity;
import com.msc.common.constant.CmsConstant;
import com.msc.common.util.PasswordUtil;
import com.msc.common.util.RedisUtil;
import com.msc.common.util.encryption.EncryptedString;
import com.msc.common.vo.Result;
import com.msc.common.weixin.entity.WeiXinUser;
import com.msc.user.dto.LoginDTO;
import com.msc.user.dto.UserAddressDTO;
import com.msc.user.dto.UserDTO;
import com.msc.user.dto.req.LoginReq;
import com.msc.user.dto.req.WxSaveUserReq;
import com.msc.user.dto.resp.LoginResp;
import com.msc.user.entity.SysRole;
import com.msc.user.entity.SysUser;
import com.msc.user.entity.TRepairRecord;
import com.msc.user.entity.TUserHouseRelation;
import com.msc.user.mapper.*;
import com.msc.user.service.SysRoleService;
import com.msc.user.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuCq
 * @since 2022-04-12
 */
@Slf4j
@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private RedisUtil redisUtil;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private TRepairRecordMapper tRepairRecordMapper;
    @Autowired
    private THouseInfoMapper tHouseInfoMapper;
    @Autowired
    private TUserHouseRelationMapper tUserHouseRelationMapper;
    @Override
    public SysUser test(@RequestParam(required = true) String content) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<SysUser>();

        return sysUserMapper.selectList(wrapper).get(0);
    }
    @Override
    public SysUserService getServiceByType(String type) {
        if (type.equals("1")) {
            return this;
        }
        return null;
    }
    @Override
    public Integer saveSysUser(SysUser sysUser) {
        return sysUserMapper.insert(sysUser);
    }

    @Override
    public Integer selectCount(QueryWrapper<SysUser> queryWrapper) {
        return sysUserMapper.selectCount(queryWrapper);
    }

    @Override
    public Integer updateUserSubStatus(WeiXinUser weiXinUser) {
        return sysUserMapper.updateUserSubStatus(weiXinUser);
    }

    @Override
    public Result<Object> login(LoginReq loginReq, HttpServletRequest request) {
        List<LoginDTO> loginDtoList = sysUserMapper.login(loginReq);
        if (CollectionUtils.isEmpty(loginDtoList)) {
            return Result.error(301,"用户信息不存在");
        }
        if (!loginDtoList.get(0).getPassword().equals(PasswordUtil.encrypt(loginReq.getPassword()))) {
            return Result.error(302,"登录密码错误");
        }
        SysUser sysUser = loginDtoList.get(0).getSysUser();
        if (CmsConstant.ADMIN_ID == loginDtoList.get(0).getId().intValue()) {
            loginDtoList.get(0).setMenuList(sysMenuMapper.getMenuList(null,null, null));
        } else {
            Result<Object> roles = sysRoleService.getRoleList(null,sysUser.getId(),null);
            sysUser.setRoleList((List<SysRole>) roles.getResult());
        }
        String token = PasswordUtil.encrypt(loginReq.getCode()+System.currentTimeMillis());
        sysUser.setAesData(new EncryptedString());
        redisUtil.set(token,sysUser);
        redisUtil.expire(token, 7200);//2小时
        LoginResp loginResp = loginDtoList.get(0).getLoginResp();
        loginResp.getSysUser().setAesData(sysUser.getAesData());
        loginResp.setToken(token);
        //记录登录日志
        return Result.OK(loginResp);
    }

    @Override
    public Result<Object> getUserList(Long userId, PageEntity pageEntity) {
        try {
            List<SysUser> userList = sysUserMapper.getUserPage(userId, pageEntity);
            pageEntity.setRows(userList);
            if (CollectionUtils.isEmpty(userList)) {
                pageEntity.setPageCount(0);
            } else {
                List<UserAddressDTO> addressDTOS = sysUserMapper.getUserAddress(userList.stream()
                        .map(SysUser::getId).collect(Collectors.toList()));
                Map<Integer,List<UserAddressDTO>> map = addressDTOS.stream().collect(Collectors.groupingBy(UserAddressDTO::getUid));
                userList.forEach(v->{
                    v.setAddressList(map.get(v.getId().intValue()));
                });
                List<PageEntity> list = sysUserMapper.getPageCount(userId, pageEntity);
                pageEntity.setPageCount(list.get(0).getPageCount());
            }
            return Result.OK(pageEntity);
        } catch (Exception e) {
            log.error("获取用户列表报错:{}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    @Override
    public Integer updateUserByWxAuth(SysUser sysUser) {
        return sysUserMapper.updateById(sysUser);
    }

    /**
    *   管理人员根据用户id获取用户信息
    **/
    @Override
    public Result<Object> getUserById(Long managerId, Integer userId) {
        try {
            List<SysUser> userList = sysUserMapper.getUserById(managerId, userId);
            Result<Object> roleResult = sysRoleService.getRoleList(null, managerId, null);
            if (roleResult.isSuccess()) {
                List<SysRole> result = (List<SysRole>) roleResult.getResult();
                userList.forEach(v->{
                    v.setRoles(result.stream().map(SysRole::getName).collect(Collectors.toList()));
                });
            }
            return Result.OK(userList);
        } catch (Exception e) {
            log.error("管理人员根据用户id获取用户信息报错:{}", e.getMessage());
            return Result.error("获取用户信息失败,请联系管理员");
        }

    }

    @Override
    public Result<Object> saveUserHouseInfo(List<TUserHouseRelation> relationList) {
        try {
            relationList.forEach(v->{
                QueryWrapper<TUserHouseRelation> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("sys_user_id", v.getSysUserId())
                        .eq("house_info_id", v.getHouseInfoId());
                if (tUserHouseRelationMapper.selectCount(queryWrapper) <= 0) {
                    tUserHouseRelationMapper.insert(v);
                }
            });
            return Result.OK();
        } catch (Exception e) {
            log.error("保存用户住址信息失败,错误信息:{}",e.getMessage());
            return Result.error("保存用户住址信息失败,请联系管理员");
        }



    }

    @Override
    public List<UserAddressDTO> getMyAddressInfos(SysUser user) {
        return sysUserMapper.getMyAddressInfos(user.getOId()==null?user.getId().intValue():user.getOId());
    }

    @Override
    public void resetPassword(SysUser user) {
        sysUserMapper.resetPassword(user);
    }

    @Override
    public List<UserDTO> getFamilyMember(SysUser user) {
        return sysUserMapper.getFamilyMember(user);
    }

    @Override
    public Result<Object> saveOrUpdateRepairInfo(TRepairRecord tRepairRecord) throws Exception {
        if (tRepairRecord.getId() == null) {
            //保存用户报修信息
            tRepairRecord.setStatus(0);
            tRepairRecordMapper.insert(tRepairRecord);

            //,通知管理员
            SysUser manager = sysUserMapper.getManagerByHouseInfoId(tRepairRecord.getHouseInfoId());
        } else {
            //更新报修状态已修复，通知用户
            TRepairRecord model = tRepairRecordMapper.selectById(tRepairRecord.getId());
            model.setStatus(1);
            model.setUpdateTime(new Date());
            model.setUpdateUserId(tRepairRecord.getUpdateUserId());
            tRepairRecordMapper.updateById(model);
        }
        return Result.OK();
    }

    @Override
    public Result<Object> deleteHouseInfo(SysUser user, String houseInfoId) {
        Double d = Double.parseDouble(houseInfoId);
        Integer result = tUserHouseRelationMapper.deleteHouseInfo(user.getId(), d.intValue());
        if (result > 0) {
            String key = "MY_INFO_"+user.getOpenId();
            if (redisUtil.hasKey(key)) {
                redisUtil.del(key);
            }
            return Result.OK("删除住址成功");
        }
        return Result.error("住址删除失败,请重试");
    }

    @Override
    public Result<Object> editHouseInfoFromWx(SysUser wxUser, WxSaveUserReq wxSaveUserReq) {
        if (wxSaveUserReq.getHouseId() == null) {
            return Result.error("未能获取到相应的住址信息");
        }
        if (StringUtils.isEmpty(wxSaveUserReq.getHomeNo())) {
            return Result.error("请填写正确的门牌号");
        }
        QueryWrapper<TUserHouseRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sys_user_id", wxUser.getId())
                .eq("houseInfoId", wxSaveUserReq.getHouseId());
        if (tUserHouseRelationMapper.selectCount(queryWrapper) <= 0) {
            return Result.error("您无权修改当前住址信息");
        }
        Integer result = tHouseInfoMapper.updateHomeNo(wxSaveUserReq);
        if (result > 0) {
            return Result.OK();
        } else {
            return Result.error("该门牌号已存在，无法修改");
        }

    }
}
