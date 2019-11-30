package com.luxshare.demo.mvc.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * 第二个filter
 *
 * @author lion hua
 * @since 2019-11-29
 */
@WebFilter(filterName = "second", urlPatterns = {"/*"})
@Slf4j
@Order(101)
public class SecondFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("这是secondFilter中的 init...");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("这是secondFilter中的 doFilter...");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        log.info("这是secondFilter中的 destroy...");
    }
}
