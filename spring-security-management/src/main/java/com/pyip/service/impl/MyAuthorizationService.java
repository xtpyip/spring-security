package com.pyip.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * @ClassName: MyAuthorizationService
 * @version: 1.0
 * @Author: pyipXt
 * @Description: 权限认证
 **/
@Component
public class MyAuthorizationService {

    /**
     * 检查用户是否有权限
     * @param authentication  认证信息
     * @param request         请求对象
     * @return
     */
    public boolean check (Authentication authentication, HttpServletRequest request) {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String username = principal.getUsername();

        // 获取权限集合
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) principal.getAuthorities();

        if ("admin".equals(username)) {
            return true;
        } else {
            // 获取请求路径
            String requestURI = request.getRequestURI();
            if (requestURI.contains("/user")) {
                for (GrantedAuthority authority : authorities) {
                    if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                        return true;
                    }
                }
            }
            if (requestURI.contains("/product")) {
                for (GrantedAuthority authority : authorities) {
                    if ("ROLE_PRODUCT".equals(authority.getAuthority())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    /**
     * 检查id是否大于10
     * @param authentication
     * @param request
     * @param id
     * @return
     */
    public boolean check (Authentication authentication, HttpServletRequest request,Integer id) {
        if (id < 10) {
            return true;
        }

        return false;
    }
}
