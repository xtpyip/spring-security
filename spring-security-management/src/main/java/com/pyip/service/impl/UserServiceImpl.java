package com.pyip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pyip.domain.User;
import com.pyip.mapper.UserMapper;
import com.pyip.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;


    /**
     * 根据用户名查询用户
     *
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>();
        wrapper.lambda().eq(User::getUsername, username);
        return userMapper.selectOne(wrapper);
    }

}
