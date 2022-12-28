package com.msc.common.config;

import com.msc.common.interceptor.FunctionInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName InterceptorConfig
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/3/30 14:07
 **/
@Configuration
public class InterceptorConfig {
    @Bean
    public DefaultPointcutAdvisor defaultPointcutAdvisor3() {

        // AnnotationMatchingPointcut pointcut = new AnnotationMatchingPointcut(HfiTrace.class, true);
        JdkRegexpMethodPointcut pointcut = new JdkRegexpMethodPointcut();
        pointcut.setPatterns("com.msc.user.Controller.*");

        // 配置增强类advisor
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(pointcut);
        advisor.setAdvice(methodInterceptor());
        advisor.setOrder(Ordered.LOWEST_PRECEDENCE);
        return advisor;
    }

    @Bean
    public FunctionInterceptor methodInterceptor() {
        return new FunctionInterceptor();
    }
}
