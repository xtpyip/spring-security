package com.pyip.filter;

import com.pyip.exception.ValidateCodeException;
import com.pyip.service.impl.MyAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName: ValidateCodeFilter
 * @version: 1.0
 * @Author: pyipXt
 * @Description: 验证码过滤器
 *  * OncePerRequestFilter 一次请求只会经过一次
 **/
@Component
public class ValidateCodeFilter extends OncePerRequestFilter {
    public final static String REDIS_KEY_IMAGE_CODE = "REDIS_KEY_IMAGE_CODE";
    @Autowired
    public StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MyAuthenticationService myAuthenticationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
// 判断是不是登录请求，如果不是登录，则直接放行
        if (request.getRequestURI().equals("/login") && request.getMethod().equalsIgnoreCase("post")) {
            String imageCode = request.getParameter("imageCode");
            System.out.println(imageCode);
            // 验证码是否正确的验证流程
            try {
                validate(request, imageCode);
            } catch (ValidateCodeException e){
                myAuthenticationService.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
    private void validate(HttpServletRequest request, String imageCode) {
        String key = REDIS_KEY_IMAGE_CODE + "-" + request.getRemoteAddr();
        // 从redis中获取验证码
        String redisImageCode = stringRedisTemplate.boundValueOps(key).get();
        // 验证码判断
        // 1. 验证码为空
        if (!StringUtils.hasText(imageCode)){
            throw new ValidateCodeException("验证码不能为空");
        }
        // 2. redis中的验证码过期
        if (!StringUtils.hasText(redisImageCode)){
            throw new ValidateCodeException("验证码已过期");
        }
        // 3. 验证码不正确
        if (!imageCode.equals(redisImageCode)){
            throw new ValidateCodeException("验证码不正确");
        }
        // 验证完毕，删除验证码
        stringRedisTemplate.delete(key);
    }
}
