package com.luxshare.demo.mvc.manualRegisterFilter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;

/**
 * 手动注册 三
 *
 * @author lion hua
 * @since 2019-11-29
 */
@Slf4j
public class ManualFilter3 implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("manual3 filter init...");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("manual3 filter doFilter...");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        log.info("manual3 filter destroy...");
    }
}
