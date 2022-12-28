package com.msc.common.config;

import com.msc.common.filter.LoginFilter;
import com.msc.common.properties.AliCloudOSSProperties;
import com.msc.common.util.SpringContextHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.util.unit.DataSize;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;

@Configuration
public class CommonConfig {

    /**
     * Spring上下文工具配置
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SpringContextHolder.class)
    public SpringContextHolder springContextHolder() {
        SpringContextHolder holder = new SpringContextHolder();
        return holder;
    }

    @Bean
    public FilterRegistrationBean FilterRegistrationBean2(){
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        //将filter注入进去
        filterFilterRegistrationBean.setFilter(securityFilter());
        //须要过滤的网址
        filterFilterRegistrationBean.addUrlPatterns(new String[]{"*.do","*.json"});
        //设置filter的等级
        filterFilterRegistrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
        //设置filter的名字
        filterFilterRegistrationBean.setName("loginFilter");

        return filterFilterRegistrationBean;
    }

    //注意上面	不能直接new 一个filter  必定要经过这种方式 来返回对象 要否则不能注入到spring boot里面
    @Bean
    public LoginFilter securityFilter() {
        return new LoginFilter();
    }

    @Bean(value = "AliCloudOSSProperties")
    public AliCloudOSSProperties getAliCloudOSSProperties() {
        AliCloudOSSProperties properties = new AliCloudOSSProperties();
        return properties;
    }

    @Bean
    public MultipartConfigElement multipartConfigElement(@Value("${file.multipart.maxFileSize}")String maxFileSize, @Value("${file.multipart.maxRequestSize}") String maxRequestSize) {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.parse(maxFileSize));
        factory.setMaxRequestSize(DataSize.parse(maxRequestSize));
        return factory.createMultipartConfig();
    }
}
