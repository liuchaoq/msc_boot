package com.msc;

import com.msc.common.filter.LoginFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

/**
 * @ClassName MscUserApplacation
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/4/8 9:41
 **/
@SpringBootApplication
@MapperScan("com.msc.user.mapper")
public class MscUserApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(com.msc.MscUserApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MscUserApplication.class);
    }

    /**
     *转移到CommonConfig配置过滤器
    **/
//    @Bean
//    public FilterRegistrationBean getFilterRegistrationBean(){
//        FilterRegistrationBean bean = new FilterRegistrationBean(new LoginFilter());
//        //bean.addUrlPatterns(new String[]{"*.do","*.jsp"});
//        bean.addUrlPatterns(new String[]{"*.do","*.json"});
//        //设置filter的等级
//        bean.setOrder(Ordered.LOWEST_PRECEDENCE);
//        //设置filter的名字
//        bean.setName("loginFilter");
//        return bean;
//    }
}
