package com.luxshare.demo.config.filterConfig;

import com.luxshare.demo.mvc.manualRegisterFilter.ManualFilter;
import com.luxshare.demo.mvc.manualRegisterFilter.ManualFilter2;
import com.luxshare.demo.mvc.manualRegisterFilter.ManualFilter3;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * manual filter config
 *
 * @author lion hua
 * @since 2019-11-29
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new ManualFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(100);
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new ManualFilter2());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(101);
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filter2() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new ManualFilter3());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(102);
        return filterRegistrationBean;
    }
}
