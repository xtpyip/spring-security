package com.pyip.service.impl;

import com.pyip.domain.Permission;
import com.pyip.domain.User;
import com.pyip.service.PermissionService;
import com.pyip.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class MyUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;

    /**
     * @param username 传入的用户名
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户[" + username + "]不存在");
        }
        // 先声明一个权限集合, 因为构造方法里面不能传入null
        Collection<GrantedAuthority> authorities = new ArrayList<>();

//        if ("admin".equalsIgnoreCase(user.getUsername())) {
//            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//        } else {
//            authorities.add(new SimpleGrantedAuthority("ROLE_PRODUCT"));
//        }

        // 数据库查询权限
        List<Permission> permissionList = permissionService.findByUserId(user.getId());
        for (Permission permission : permissionList) {
            authorities.add(new SimpleGrantedAuthority(permission.getPermissionTag()));
        }

        UserDetails details = new org.springframework.security.core.userdetails.User(
                username,
                "{bcrypt}" + user.getPassword(), //{noop} 代表密码不加密;{bcrypt}: 使用bcrypt加密算法
                true,  // 用户是否启用
                true, // 用户是否过期
                true,// 用户凭证是否过期
                true, //用户是否锁定
                authorities
        );
        return details;
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode1 = encoder.encode("123456");
        String encode2 = encoder.encode("123456");
        System.out.println(encode1);
        System.out.println(encode2);
    }
}