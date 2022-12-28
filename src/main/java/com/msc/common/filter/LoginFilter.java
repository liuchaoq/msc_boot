package com.msc.common.filter;

import com.alibaba.fastjson.JSON;
import com.msc.common.constant.CmsConstant;
import com.msc.common.properties.AliCloudOSSProperties;
import com.msc.common.util.RedisUtil;
import com.msc.common.util.oss.OssBootUtil;
import com.msc.common.vo.Result;
import com.msc.user.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.yaml.snakeyaml.util.UriEncoder;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 *SpringBoot整合Filter 方式二
 *
 */
@Slf4j
public class LoginFilter implements Filter {
    private static final Long expireTime = 2L*3600;
    @Autowired
    private RedisUtil redisUtil;
    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }
    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) arg0;
        HttpServletResponse response = (HttpServletResponse) arg1;
        String token = request.getHeader(CmsConstant.TOKEN_NAME);
        String wxToken = request.getHeader(CmsConstant.WX_TOKEN_NAME);
        try {
            Long loginTime = StringHelper.isNullOrEmptyString(token)?null:redisUtil.getExpire(token);
            Long wxLoginTime = StringHelper.isNullOrEmptyString(wxToken)?null:redisUtil.getExpire(wxToken);
            if (request.getRequestURI().endsWith(".do")) {
                //后台管理系统过滤
                if (!request.getRequestURI().endsWith("login.do")) {
                    //判断是否登录超时
                    if (loginTime == null || loginTime <= 0) {
                        response.setCharacterEncoding("utf-8");
                        response.setHeader("Content-Type","text/html;charset=utf-8");
                        response.getWriter().write(JSON.toJSONString(Result.error(201, "请重新登录!")));
                        return;
                    } else {
                        redisUtil.expire(token, expireTime);
                        arg2.doFilter(arg0, arg1);
                    }
                } else {
                    arg2.doFilter(arg0, arg1);
                }
            } else if (request.getRequestURI().contains("/wx")) {
                //微信接口过滤
                if (!request.getRequestURI().endsWith("getUserInfo")
                        && !request.getRequestURI().endsWith("executeResult") ) {
                    //判断是否登录超时
                    if (wxLoginTime == null || wxLoginTime < 0) {
                        response.setCharacterEncoding("utf-8");
                        response.setHeader("Content-Type","text/html;charset=utf-8");
                        response.getWriter().write(JSON.toJSONString(Result.error(201, "请重新登录!")));
                        return;
                    } else {
                        redisUtil.expire(wxToken, expireTime);
                        arg2.doFilter(arg0, arg1);
                    }
                } else {
                    if (StringHelper.isNullOrEmptyString(wxToken)) {
                        redisUtil.expire(wxToken, expireTime);
                    }
                    arg2.doFilter(arg0, arg1);
                }
            }
        } catch (Exception e) {
            log.error("接口过滤器报错内容：" + e.getMessage());
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Type","text/html;charset=utf-8");
            response.getWriter().write(JSON.toJSONString(Result.error(202, "未知错误，请联系管理员")));
            return;
        }


    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub
    }
}