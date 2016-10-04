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
 * 拦截需要用户信息页面跳转到微信验证端获取用户信息
 * @author coki
 *
 */
public class NeedUserInfoFilter implements Filter {

    public static String domainStr = "wechat-web";
    @SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(NeedUserInfoFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub
        System.out.println("init");

    }

    @SuppressWarnings("unused")
	@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("################ TestFilter #####################");
        HttpServletRequest requestHttp = (HttpServletRequest) request;
        HttpServletResponse responseHttp = (HttpServletResponse) response;
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        System.out.println("destroy");

    }

}
