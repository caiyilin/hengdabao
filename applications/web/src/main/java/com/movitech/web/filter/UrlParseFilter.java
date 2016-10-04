package com.movitech.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * url解析，因为框架的问题url不是标准的spring mvc格式，所以把此类url转为标准格式
 * @author coki
 *
 */
public class UrlParseFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(UrlParseFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("init");

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("################ UrlParseFilter #####################");
        HttpServletRequest requestHttp = (HttpServletRequest) request;
        HttpServletResponse responseHttp = (HttpServletResponse) response;
        String contextPath = requestHttp.getQueryString();
        if(contextPath != null && contextPath.contains("page=")) {
            String url = contextPath.replaceFirst("page=", "").replaceFirst("\\.","/").replaceFirst("&","?");
            responseHttp.sendRedirect(url);
        } else {
            chain.doFilter(requestHttp, responseHttp);
        }
    }

    @Override
    public void destroy() {
        logger.info("destroy");

    }

}
