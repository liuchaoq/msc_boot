package com.msc.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.msc.common.util.PasswordUtil;
import com.msc.common.util.RedisUtil;
import com.msc.common.util.encryption.EncryptedString;
import com.msc.common.vo.Result;
import com.msc.common.weixin.TokenUtil;
import com.msc.common.weixin.WeiXinConstant;
import com.msc.common.weixin.entity.WeiXinCodeMsg;
import com.msc.common.weixin.entity.WeiXinToken;
import com.msc.user.dto.WxUserDTO;
import com.msc.user.entity.SysMenu;
import com.msc.user.entity.SysRole;
import com.msc.user.entity.SysUser;
import com.msc.user.mapper.SysRoleMapper;
import com.msc.user.mapper.SysUserMapper;
import com.msc.user.service.WxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName WxServiceImpl
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/6/7 10:19
 **/
@Slf4j
@Service
public class WxServiceImpl implements WxService {
    @Resource
    private RedisUtil redisUtil;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public String getAccessToken() {
        if (redisUtil.get(WeiXinConstant.TOKEN) == null) {
            WeiXinToken weiXinToken = TokenUtil.getToken();
            if (weiXinToken != null && weiXinToken.getErrcode() == 0) {
                redisUtil.set(WeiXinConstant.TOKEN, weiXinToken.getAccess_token(), weiXinToken.getExpires_in());
                return weiXinToken.getAccess_token();
            } else {
                log.error("获取token失败：{}", weiXinToken.getErrmsg());
                return null;
            }
        } else {
            return redisUtil.get(WeiXinConstant.TOKEN).toString();
        }
    }

    @Override
    public String refreshAccessToken() {
        WeiXinToken weiXinToken = TokenUtil.getToken();
        if (weiXinToken != null && weiXinToken.getErrcode() == 0) {
            redisUtil.set(WeiXinConstant.TOKEN, weiXinToken.getAccess_token(), weiXinToken.getExpires_in());
            return weiXinToken.getAccess_token();
        } else {
            log.error("获取token失败：{}", weiXinToken.getErrmsg());
            return null;
        }
    }

    @Override
    public Result<Object> getOpenId(String code) {
        WeiXinCodeMsg weiXinCodeMsg = TokenUtil.getOpenIdByCode(code);
        if (weiXinCodeMsg != null && weiXinCodeMsg.getErrcode() == 0) {
            String openId = weiXinCodeMsg.getOpenid();
            QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("open_id", openId);
            List<SysUser> users = sysUserMapper.selectList(queryWrapper);
            Map<String, Object> resultMap = new HashMap<>();
            WxUserDTO wxUserDTO = new WxUserDTO();
            SysUser user = new SysUser();

            if (CollectionUtils.isEmpty(users)) {
                //客户未关注公众号
                user.setOpenId(openId);
                user.setCreateTime(new Date());
                return Result.error(301, "当前客户未关注公众号");
            } else {
                user = users.get(0);
                if (StringUtils.isEmpty(user.getHead())) {
                    if (StringUtils.isEmpty(weiXinCodeMsg.getHeadimgurl())) {
                        if("snsapi_userinfo".equals(weiXinCodeMsg.getScope())) {
                            TokenUtil.getSnsUserInfo(weiXinCodeMsg);
                        } else {
                            return Result.error(302, "当前客户未授权用户信息");
                        }

                    }
                    user.setHead(weiXinCodeMsg.getHeadimgurl());
                    user.setNickname(weiXinCodeMsg.getNickname());
                    sysUserMapper.updateById(user);
                }
                List<SysRole> roleList = sysRoleMapper.getRolesByUserId(user.getId());
                user.setAesData(new EncryptedString());
                BeanUtils.copyProperties(user, wxUserDTO);
                resultMap.put("user", wxUserDTO);
                resultMap.put("role", roleList);
                user.setRoleList(roleList);
            }
            String token = PasswordUtil.encrypt(openId+System.currentTimeMillis());
            resultMap.put("token", token);
            redisUtil.set(token,user);
            redisUtil.expire(token, 7200);//2小时
            log.error("回传userInfo：{}", JSON.toJSONString(resultMap));
            return Result.OK(resultMap);
        } else {
            log.error("获取用户openId失败：{}", weiXinCodeMsg.getErrmsg());
            return Result.error(weiXinCodeMsg.getErrmsg());
        }
    }
}
