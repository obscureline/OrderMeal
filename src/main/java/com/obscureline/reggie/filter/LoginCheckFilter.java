package com.obscureline.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.obscureline.reggie.common.BaseContext;
import com.obscureline.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否完成登录
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter" , urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    //路劲匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1.获得请求URI
        String requestURI = request.getRequestURI();
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login",
                "/test/1",
                "/test/2",
                "/test/3",
                "/test/4",
                "/test/5"
        };
        log.info("拦截到请求：{}" , requestURI);

        //2.判断请求是否需要处理
        boolean check = check(urls, requestURI);

        //3.不需要处理直接放行
        if (check){
            log.info("本次请求{}不需要处理" , requestURI);
            filterChain.doFilter(request , response);
            return;
        }

        //4*1.判断登录状态，已登陆放行
        if (request.getSession().getAttribute("employee") != null){
            log.info("用户已登陆，id为：{}" , request.getSession().getAttribute("employee"));

            Long empId = (Long) request.getSession().getAttribute("employee");
            Long id = Thread.currentThread().getId();
            BaseContext.setCurrentId(empId);
            log.info("线程id为：{}" , id);

            filterChain.doFilter(request,response);
            return;
        }

        //4*2.判断登录状态，已登陆放行
        if (request.getSession().getAttribute("user") != null){
            log.info("用户已登陆，id为：{}" , request.getSession().getAttribute("user"));

            Long empId = (Long) request.getSession().getAttribute("user");
            Long id = Thread.currentThread().getId();
            BaseContext.setCurrentId(empId);
            log.info("线程id为：{}" , id);

            filterChain.doFilter(request,response);
            return;
        }
        log.info("用户未登陆");

        //5.未登录请登录 ，通过输出流向客户端页面响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配比对  是否放行
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls ,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }
}