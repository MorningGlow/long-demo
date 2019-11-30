package com.luxshare.demo.config.interceptor.config;

import com.luxshare.demo.mvc.interceptor.TestInterceptor;
import com.luxshare.demo.mvc.interceptor.TestInterceptor2;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器
 *
 * @author lion hua
 * @since 2019-11-29
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TestInterceptor())
                .order(200)
                .addPathPatterns("/*");

        registry.addInterceptor(new TestInterceptor2())
                .order(201)
                .addPathPatterns("/*");
    }
}
