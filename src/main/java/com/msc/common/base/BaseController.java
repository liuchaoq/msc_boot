package com.msc.common.base;

import com.msc.common.constant.CmsConstant;
import com.msc.common.util.DateUtil;
import com.msc.common.util.RedisUtil;
import com.msc.user.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @ClassName BaseController
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/6/15 19:09
 **/
@Slf4j
@CrossOrigin(allowCredentials="true")
@Controller
public class BaseController {

    @Resource
    private RedisUtil redisUtil;

    public SysUser getUser(HttpServletRequest request) {
        String token = request.getHeader(CmsConstant.TOKEN_NAME);
        SysUser sysUser = (SysUser) redisUtil.get(token);
        if (sysUser != null) {
            redisUtil.expire(token, 7200);//2小时
        }
        sysUser.setOId(sysUser.getId().intValue());
        if (sysUser.getId() == CmsConstant.ADMIN_ID) {
            sysUser.setId(null);
        }
        return sysUser;
    }

    public SysUser getUser() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader(CmsConstant.TOKEN_NAME);
        SysUser sysUser = (SysUser) redisUtil.get(token);
        if (sysUser != null) {
            redisUtil.expire(token, 7200);//2小时
        }
        sysUser.setOId(sysUser.getId().intValue());
        if (sysUser.getId() == CmsConstant.ADMIN_ID) {
            sysUser.setId(null);
        }
        return sysUser;
    }

    public SysUser getWxUser() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader(CmsConstant.WX_TOKEN_NAME);
        log.error("微信请求Token:{},请求时间:{},请求路径:{},请求ip:{}"
                ,token
                , DateUtil.Date2Str(new Date())
                ,request.getRequestURI());
        SysUser sysUser = (SysUser) redisUtil.get(token);
        if (sysUser != null) {
            redisUtil.expire(token, 7200);//2小时
        }
        return sysUser;
    }

    public void updateWxUser(SysUser sysUser) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader(CmsConstant.WX_TOKEN_NAME);
        redisUtil.set(token, sysUser, 7200);
    }

    public RedisUtil getRedisUtil() {
        return redisUtil;
    }
}
