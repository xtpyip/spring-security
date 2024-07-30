package com.pyip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pyip.domain.User;

import java.util.List;

public interface UserService extends IService<User> {

    // 根据用户名查询用户
    public User findByUsername(String username);

}
