package com.pyip.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: MyAuthenticationService
 * @version: 1.0
 * @Author: pyipXt
 * @Description: 自定义登录成功或失败的处理器
 *  *  * 退出登录
 **/
@Service
public class MyAuthenticationService implements AuthenticationSuccessHandler, AuthenticationFailureHandler, LogoutSuccessHandler {

    @Autowired
    ObjectMapper objectMapper;

    /**
     * 登录失败后的处理逻辑
     * @param request
     * @param response
     * @param exception
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        System.out.println("登录失败！going......");
//        RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
//        redirectStrategy.sendRedirect(request, response, "/toLoginPage");

        Map result = new HashMap();
        result.put("code", HttpStatus.UNAUTHORIZED.value());// 设置响应码
        result.put("message", exception.getMessage());// 设置错误信息
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }

    /**
     * 登录成功后处理逻辑
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("登录成功！going......");
        // 重定向到index页面
//        RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
//        redirectStrategy.sendRedirect(request, response, "/");

        Map result = new HashMap();
        result.put("code", HttpStatus.OK.value());// 设置响应码
        result.put("message", "登录成功");// 设置响应信息
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("退出登录成功！going......");
        RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
        redirectStrategy.sendRedirect(request, response, "/toLoginPage");
    }
}
