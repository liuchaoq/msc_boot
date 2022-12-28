package com.msc.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.msc.common.base.FunctionTypeEnum;
import com.msc.common.constant.CmsConstant;
import com.msc.common.util.FunctionRequired;
import com.msc.common.util.RedisUtil;
import com.msc.common.vo.Result;
import com.msc.user.entity.SysFunction;
import com.msc.user.entity.SysRole;
import com.msc.user.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FunctionInterceptor
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/3/30 13:49
 **/
@Slf4j
public class FunctionInterceptor implements MethodInterceptor {
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        FunctionRequired requiredInterceptor = AnnotationUtils.findAnnotation(invocation.getMethod(), FunctionRequired.class);
        if(requiredInterceptor!=null){
            log.error("方法名：" + invocation.getMethod().getName()+";标签名：" + requiredInterceptor.value());
            //你要做的逻辑代码
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String token = request.getHeader(CmsConstant.TOKEN_NAME);

            SysUser sysUser = (SysUser) redisUtil.get(token);
            if (sysUser.getId() != CmsConstant.ADMIN_ID) {
                List<SysRole> roleList = sysUser.getRoleList();
                if (CollectionUtils.isEmpty(roleList)) {
                    return Result.error("无操作权限");
                }
                boolean hasQx = false;
                for (SysRole sysRole: roleList) {
                    List<SysFunction> functionList = sysRole.getFunctionList();
                    if (!CollectionUtils.isEmpty(functionList)
                            && functionList.stream().anyMatch(v->v.getCode().equals(requiredInterceptor.value()))) {
                        hasQx = true;
                        break;
                    }
                }
                if (!hasQx) {
                    return Result.error("无操作权限");
                }
            }
        }
        return invocation.proceed();
    }
}
