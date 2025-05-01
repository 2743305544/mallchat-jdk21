package com.shiyi.mallchat.common.config;


import com.shiyi.mallchat.common.interceptor.BlackInterceptor;
import com.shiyi.mallchat.common.interceptor.CollectorInterceptor;
import com.shiyi.mallchat.common.interceptor.TokenInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    private TokenInterceptor tokenInterceptor;

    @Resource
    private CollectorInterceptor collectorInterceptor;

    @Resource
    private BlackInterceptor blackInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/capi/**");
        registry.addInterceptor(collectorInterceptor).addPathPatterns("/capi/**");
        registry.addInterceptor(blackInterceptor).addPathPatterns("/capi/**");
    }
}
