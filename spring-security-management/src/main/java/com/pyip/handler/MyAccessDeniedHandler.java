package com.pyip.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName: MyAccessDeniedHandler
 * @version: 1.0
 * @Author: pyipXt
 * @Description: 自定义权限不足处理
 **/
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write("权限不足，请联系管理员");
    }
}