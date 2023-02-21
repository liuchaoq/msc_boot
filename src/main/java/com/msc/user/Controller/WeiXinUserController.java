package com.msc.user.Controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.msc.common.base.BaseController;
import com.msc.common.util.JgMessageUtil;
import com.msc.common.util.RedisUtil;
import com.msc.common.util.encryption.AesEncryptUtil;
import com.msc.common.util.encryption.EncryptedString;
import com.msc.common.vo.Result;
import com.msc.common.weixin.TokenUtil;
import com.msc.user.dto.jgmessage.AuthCodeDTO;
import com.msc.user.dto.req.StatisticsReq;
import com.msc.user.dto.req.WxSaveUserReq;
import com.msc.user.entity.SysUser;
import com.msc.user.entity.THouseInfo;
import com.msc.user.entity.TRepairRecord;
import com.msc.user.entity.TUserHouseRelation;
import com.msc.user.mapper.TRepairRecordMapper;
import com.msc.user.service.MeterRecordService;
import com.msc.user.service.SysUserService;
import com.msc.user.service.THouseInfoService;
import com.msc.user.service.WxWaterManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @ClassName WeiXinUserController
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/6/27 15:50
 **/
@Slf4j
@RestController
public class WeiXinUserController extends BaseController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private THouseInfoService tHouseInfoService;
    @Autowired
    private WxWaterManagerService wxWaterManagerService;
    @Autowired
    private MeterRecordService meterRecordService;
    @Resource
    private RedisUtil redisUtil;

    @GetMapping("/wx/user/getAuthCode")
    public Result<Object> getAuthCode(HttpServletRequest request) {
        SysUser sysUser = getWxUser();
        String mobile = request.getParameter("username");
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", mobile);
        if (StringUtils.isEmpty(mobile)) {
            return Result.error(1000,"该填写正确的手机号码");
        }
        if (sysUserService.selectCount(queryWrapper) > 0) {
            return Result.error(1001,"该手机号已存在");
        }
        sysUser.setUsername(mobile);
        String key = TokenUtil.getRedisKey(1, sysUser);
        Integer code = TokenUtil.getAuthCode();
        redisUtil.set(key, code, 600);
        AuthCodeDTO authCodeDTO = new AuthCodeDTO(mobile, code);
        //极光发送手机验证码
        return JgMessageUtil.sendAuthCOde(authCodeDTO);
    }

    @PostMapping("/wx/user/saveUserInfo.json")
    public Result<Object> saveUserInfo(@RequestBody WxSaveUserReq wxSaveUserReq) {
        SysUser sysUser = getWxUser();
        String key = TokenUtil.getRedisKey(1, sysUser);
        if (redisUtil.get(key) == null
                || wxSaveUserReq == null
                || !wxSaveUserReq.getAuthCode().equals(redisUtil.get(key).toString())) {
            return Result.error("验证码校验不通过,请重新获取验证码！");
        }
        sysUser.setUsername(wxSaveUserReq.getUsername());
        sysUser.setHouseId(wxSaveUserReq.getHouseId());
        sysUser.setName(wxSaveUserReq.getName());
        redisUtil.removeAll(key);
        try {
            List<TUserHouseRelation> relationList = new ArrayList<>();
            //保存用户选择已存在的住址关联信息
            if (wxSaveUserReq.getHouseId() != null) {
                TUserHouseRelation tUserHouseRelation = new TUserHouseRelation();
                tUserHouseRelation.setSysUserId(sysUser.getId().intValue());
                tUserHouseRelation.setHouseInfoId(wxSaveUserReq.getHouseId());
                tUserHouseRelation.setCreateTime(new Date());
                tUserHouseRelation.setCreateUserId(sysUser.getId().intValue());
                relationList.add(tUserHouseRelation);
            }
            if (sysUser.getHouseId() == null
                    && !StringUtils.isEmpty(wxSaveUserReq.getTownCode())
                    && !StringUtils.isEmpty(wxSaveUserReq.getVillageId())
                    && !StringUtils.isEmpty(wxSaveUserReq.getHomeNo())) {
                String[] homeNos = wxSaveUserReq.getHomeNo().split("#");
                Set<String> homeNoSet = new HashSet<>();
                for (String homeNo: homeNos) {
                    if (!StringUtils.isEmpty(homeNo)) {
                        homeNoSet.add(homeNo);
                    }
                }
                homeNoSet.forEach(v->{
                    if (!StringUtils.isEmpty(v)) {
                        //创建用户住址关联关系
                        TUserHouseRelation tUserHouseRelation = new TUserHouseRelation();
                        tUserHouseRelation.setSysUserId(sysUser.getId().intValue());
                        tUserHouseRelation.setCreateTime(new Date());
                        tUserHouseRelation.setCreateUserId(sysUser.getId().intValue());
                        //查询输入的住址是否已存在
                        QueryWrapper<THouseInfo> rapper = new QueryWrapper<>();
                        rapper.eq("village_id", wxSaveUserReq.getVillageId())
                                .eq("home_no", v);
                        List<THouseInfo> infos = tHouseInfoService.list(rapper);
                        if (CollectionUtils.isEmpty(infos)) {
                            //不存在先创建住址信息
                            THouseInfo tHouseInfo = new THouseInfo();
                            tHouseInfo.setCreateTime(new Date());
                            tHouseInfo.setHomeNo(v);
                            tHouseInfo.setCreateUserId(sysUser.getId());
                            tHouseInfo.setVillageId(wxSaveUserReq.getVillageId());
                            tHouseInfoService.save(tHouseInfo);
                            //创建后住址的id加入关联关系
                            tUserHouseRelation.setHouseInfoId(tHouseInfo.getId());
                        } else {
                            //已存在的住址信息,直接关联其id
                            tUserHouseRelation.setHouseInfoId(infos.get(0).getId());
                        }
                        relationList.add(tUserHouseRelation);
                    }
                });
            }
            if (!CollectionUtils.isEmpty(relationList)) {
                Result<Object> result = sysUserService.saveUserHouseInfo(relationList);
                if (!result.isSuccess()) {
                    return result;
                }
            }
            //更新用户的认证信息
            Integer result = sysUserService.updateUserByWxAuth(sysUser);
            if (result == null || result <= 0){
                return Result.error("认证失败,请联系管理员");
            }
            updateWxUser(sysUser);
            String infoKey = "MY_INFO_"+sysUser.getOpenId();
            if (redisUtil.hasKey(infoKey)) {
                redisUtil.del(key);
            }
            return Result.OK("认证成功");
        } catch (Exception e) {
            log.error("客户认证失败,客户Id:{},失败原因:{}", sysUser.getId(), e.getMessage());
            return Result.error("认证失败,请联系管理员");
        }
    }

    /**
    * @Description: 用户新增自己的住户地址
    * @Param: [wxSaveUserReq]
    * @return: com.msc.common.vo.Result<java.lang.Object>
    * @Author: liuCq
    * @Date: 2022/8/1
    */
    @PostMapping("/wx/user/addHouseInfoFromWx.json")
    public Result<Object> addHouseInfoFromWx(@RequestBody WxSaveUserReq wxSaveUserReq) {
        SysUser sysUser = getWxUser();
        try {
            List<TUserHouseRelation> relationList = new ArrayList<>();
            //保存用户选择已存在的住址关联信息
            if (wxSaveUserReq.getHouseId() != null) {
                TUserHouseRelation tUserHouseRelation = new TUserHouseRelation();
                tUserHouseRelation.setSysUserId(sysUser.getId().intValue());
                tUserHouseRelation.setHouseInfoId(wxSaveUserReq.getHouseId());
                tUserHouseRelation.setCreateTime(new Date());
                tUserHouseRelation.setCreateUserId(sysUser.getId().intValue());
                relationList.add(tUserHouseRelation);
            }
            if (wxSaveUserReq.getHouseId() == null
                    && !StringUtils.isEmpty(wxSaveUserReq.getTownCode())
                    && !StringUtils.isEmpty(wxSaveUserReq.getVillageId())
                    && !StringUtils.isEmpty(wxSaveUserReq.getHomeNo())) {
                String[] homeNos = wxSaveUserReq.getHomeNo().split("#");
                Set<String> homeNoSet = new HashSet<>();
                for (String homeNo: homeNos) {
                    if (!StringUtils.isEmpty(homeNo)) {
                        homeNoSet.add(homeNo);
                    }
                }
                homeNoSet.forEach(v->{
                    //创建用户住址关联关系
                    TUserHouseRelation tUserHouseRelation = new TUserHouseRelation();
                    tUserHouseRelation.setSysUserId(sysUser.getId().intValue());
                    tUserHouseRelation.setCreateTime(new Date());
                    tUserHouseRelation.setCreateUserId(sysUser.getId().intValue());
                    //查询输入的住址是否已存在
                    QueryWrapper<THouseInfo> rapper = new QueryWrapper<>();
                    rapper.eq("village_id", wxSaveUserReq.getVillageId())
                            .eq("home_no", v);
                    List<THouseInfo> infos = tHouseInfoService.list(rapper);
                    if (CollectionUtils.isEmpty(infos)) {
                        //不存在先创建住址信息
                        THouseInfo tHouseInfo = new THouseInfo();
                        tHouseInfo.setCreateTime(new Date());
                        tHouseInfo.setHomeNo(v);
                        tHouseInfo.setCreateUserId(sysUser.getId());
                        tHouseInfo.setVillageId(wxSaveUserReq.getVillageId());
                        tHouseInfoService.save(tHouseInfo);
                        //创建后住址的id加入关联关系
                        tUserHouseRelation.setHouseInfoId(tHouseInfo.getId());
                    } else {
                        //已存在的住址信息,直接关联其id
                        tUserHouseRelation.setHouseInfoId(infos.get(0).getId());
                    }
                    relationList.add(tUserHouseRelation);
                });
            }
            String key = "MY_INFO_"+sysUser.getOpenId();
            if (redisUtil.hasKey(key)) {
                redisUtil.del(key);
            }
            return sysUserService.saveUserHouseInfo(relationList);
        } catch (Exception e) {
            return Result.error("添加住址失败,请联系管理员");
        }
    }

    @PostMapping("/wx/user/editHouseInfoFromWx.json")
    public Result<Object> editHouseInfoFromWx(@RequestBody WxSaveUserReq wxSaveUserReq) {
        return sysUserService.editHouseInfoFromWx(getWxUser(), wxSaveUserReq);
    }
    @PostMapping("/wx/user/editUserNameFromWx.json")
    public Result<Object> editUserNameFromWx(@RequestParam String name) {
        SysUser sysUser = getWxUser();
        sysUser.setName(name);
        updateWxUser(sysUser);
        String key = "MY_INFO_"+sysUser.getOpenId();
        Map<String,Object> resultMap = (Map<String, Object>) redisUtil.get(key);
        resultMap.put("name", name);
        redisUtil.set(key,resultMap, 7200);
        return sysUserService.updateUserName(sysUser);
    }

    /**
    * @Description: 水站管理员用水、水费收取统计情况，
    * @Param: [jsonObject] 年year、月month
    * @return: com.msc.common.vo.Result<java.lang.Object>
    * @Author: liuCq
    * @Date: 2022/7/15
    */
    @PostMapping("/wx/queryStatistics")
    public Result<Object> queryStatistics(@RequestBody StatisticsReq statisticsReq) {
        SysUser sysUser = getWxUser();
        if (sysUser == null) {
            return Result.error("您无权调用该接口!");
        }
        return wxWaterManagerService.queryStatistics(sysUser, statisticsReq);
    }

    /**
    * @Description: 普通用户获取关于自己的用水记录
    * @Param: [statisticsReq]
    * @return: com.msc.common.vo.Result<java.lang.Object>
    * @Author: liuCq
    * @Date: 2022/7/15
    */
    @PostMapping("/wx/getMeterRecordsByUser")
    public Result<Object> getMeterRecordsByUser(@RequestBody StatisticsReq statisticsReq) {
        SysUser sysUser = getWxUser();
        if (sysUser == null) {
            return Result.error("您无权调用该接口!");
        }
        return meterRecordService.getMeterRecordsByUser(sysUser, statisticsReq);
    }

    @PostMapping("/wx/getMyInfo.json")
    @ResponseBody
    public Result<Object> getMyWxInfo() {
        SysUser wxUser = getWxUser();
        String key = "MY_INFO_"+wxUser.getOpenId();
        if (redisUtil.hasKey(key)) {
            return Result.OK(redisUtil.get(key));
        }
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("username", wxUser.getUsername());
        resultMap.put("name", wxUser.getName());
        resultMap.put("addressList", sysUserService.getMyAddressInfos(wxUser));
        resultMap.put("members", sysUserService.getFamilyMember(wxUser));
        redisUtil.set(key,resultMap, 7200);
        return Result.OK(resultMap);
    }

    @GetMapping("wx/getUserHouseInfo.json")
    public Result<Object> getUserHouseInfo() throws Exception {
        return Result.OK(sysUserService.getMyAddressInfos(getWxUser()));
    }

    /**
     *微信用户删除自己的住址信息
    **/
    @PostMapping("wx/deleteHouseInfo.json")
    public Result<Object> getUserHouseInfo(@RequestParam("id") String id) throws Exception {
        SysUser user = getWxUser();
        EncryptedString encryptedString = user.getAesData();
        String idStr = AesEncryptUtil.desEncrypt(id,encryptedString.getKey(),encryptedString.getIv(),AesEncryptUtil.NO_PADDING);
        idStr = idStr.replaceFirst("houseInfoId","");
        return sysUserService.deleteHouseInfo(user, idStr);
    }

    /**
    * @Description: 用户报修或管理员更新报修状态
    * @Param: [tRepairRecord]
    * @return: com.msc.common.vo.Result<java.lang.Object>
    * @Author: liuCq
    * @Date: 2022/7/29
    */
    @PostMapping("wx/saveRepair.json")
    public Result<Object> saveRepair(@RequestBody TRepairRecord tRepairRecord) throws Exception {
        SysUser user = getWxUser();
        if (tRepairRecord.getId() == null) {
            tRepairRecord.setSysUserId(user.getId().intValue());
            tRepairRecord.setCreateTime(new Date());
        } else {
            tRepairRecord.setUpdateUserId(user.getId().intValue());
        }
        return sysUserService.saveOrUpdateRepairInfo(tRepairRecord);
    }
}
