package org.bupt.aiop.platform.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 拦截器添加跨域支持
 * Created by ken on 2017/6/8.
 */
public class CrossOriginInterceptor implements Filter {


    private static final Logger logger = LoggerFactory.getLogger(CrossOriginInterceptor.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        logger.debug("进入CrossOriginInterceptor");

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, Content-Type, Accept, ACCESS_TOKEN, " +
                "Content-Range, Content-Disposition, Content-Description");
        filterChain.doFilter(servletRequest, servletResponse);
    }
    
    @Override
    public void destroy() {

    }
}