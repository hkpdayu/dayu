package com.xxxx.crm.config;

import com.xxxx.crm.exceptions.NoLoginException;
import com.xxxx.crm.interceptors.NoLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@Configuration//声明一个类你为配置类
public class MvcConfig extends WebMvcConfigurerAdapter {
    @Bean
    public NoLoginInterceptor noLoninInterceptor(){
        return new NoLoginInterceptor();
    }
    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //需要一个实现HandlerInterceptor接口的实例，这里使用的是NoLoginInterceptor
        registry.addInterceptor(noLoninInterceptor())//下面的操作声明在noLoninInterceptor()拦截器中需要拦截什么，放行什么
        //设置拦截器的过滤路径规则
                .addPathPatterns("/**")
                //设置不需要拦截的过滤规则
                .excludePathPatterns("/index","/user/login","/css/**","/images/**","/js/**","/lib/**");
    }
}
