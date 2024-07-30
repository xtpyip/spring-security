package com.pyip.controller;

import com.pyip.domain.User;
import com.pyip.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户处理类
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    /**
     * 查询所有用户
     *
     * @return
     */
    @RequestMapping("/findAll")
    @PreAuthorize("hasRole('ADMIN')")
    public String findAll(Model model) {
        List<User> userList = userService.list();
        model.addAttribute("userList", userList);
        return "user_list";
    }

    /**
     * 查询所有用户-返回json数据
     *
     * @return
     */
    @RequestMapping("/findAllTOJson")
    @ResponseBody
    @PostFilter("filterObject.id % 2 == 0") // 去除奇数ID的数据
    public List<User> findAllTOJson() {
        List<User> userList = userService.list();
        return userList;
    }

    /**
     * 用户修改页面跳转
     *
     * @return
     */
    @RequestMapping("/update/{id}")
    @PreAuthorize("#id<3")
    public String update(@PathVariable Integer id, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        return "user_update";
    }

    /**
     * 用户添加或修改
     *
     * @return
     */
    @PostMapping("/saveOrUpdate")
    public String saveOrUpdate(User user) {
        userService.saveOrUpdate(user);
        return "redirect:/user/findAll";
    }

    /**
     * 用户添加页面跳转
     *
     * @return
     */
    @RequestMapping("/add")
    public String add() {
        return "user_add";
    }

    /**
     * 用户删除
     *
     * @return
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        userService.removeById(id);
        return "redirect:/user/findAll";
    }

    /**
     * 用户删除-多选删除
     *
     * @return
     */
    @GetMapping("/delByIds")
    @PreFilter(filterTarget = "ids", value = "filterObject%2==0")//过滤掉参数为奇数的值
    public String delByIds(@RequestParam(value = "id") List<Integer> ids) {
        for (Integer id : ids) {
            System.out.println(id);
        }
        return "redirect:/user/findAll";
    }


    /**
     * 根据用户ID查询用户
     *
     * @return
     */
    @GetMapping("/{id}")
    @ResponseBody
    @PostAuthorize("returnObject.username==authentication.principal.username") // 只能自己查自己
    public User getById(@PathVariable Integer id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(RememberMeAuthenticationToken.class.isAssignableFrom(authentication.getClass())){
            throw new RememberMeAuthenticationException("认证来源于remember me");
        }

        User user = userService.getById(id);
        return user;
    }

    /**
     * 获取当前登录用户
     * @return
     */
    @GetMapping("/loginUser")
    @ResponseBody
    public UserDetails getCurrentUser(){
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal;
    }
    @GetMapping("/loginUser2")
    @ResponseBody
    public UserDetails getCurrentUser2(Authentication authentication){
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        return principal;
    }
    @GetMapping("/loginUser3")
    @ResponseBody
    public UserDetails getCurrentUser3(@AuthenticationPrincipal UserDetails userDetails){
        return userDetails;
    }

}
