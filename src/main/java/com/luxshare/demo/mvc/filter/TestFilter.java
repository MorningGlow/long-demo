package com.luxshare.demo.mvc.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * test filter  ServletComponentScan
 * 即使使用Order注解也不能保证优先级(所以推荐使用手动注册)
 *
 * @author lion hua
 * @since 2019-11-29
 */
@WebFilter(filterName = "test", urlPatterns = {"/*"})
@Slf4j
@Order(100)
public class TestFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("这是testFilter中的 init...");
    }

    @Override
    public void destroy() {
        log.info("这是testFilter中的 destroy...");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("这是testFilter中的 doFilter...");
        chain.doFilter(request, response);
    }
}
