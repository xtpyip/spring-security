package com.pyip.config;

import com.pyip.domain.Permission;
import com.pyip.filter.ValidateCodeFilter;
import com.pyip.handler.MyAccessDeniedHandler;
import com.pyip.service.PermissionService;
import com.pyip.service.impl.MyAuthenticationService;
import com.pyip.service.impl.MyUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.util.List;

/**
 * @ClassName: SecurityConfiguration
 * @version: 1.0
 * @Author: pyipXt
 * @Description: 自定义
 **/
@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启注解支持
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private MyUserDetailsServiceImpl myUserDetailsService;
    @Autowired
    private MyAuthenticationService myAuthenticationService;
    @Autowired
    private ValidateCodeFilter validateCodeFilter;
    @Autowired
    private PermissionService permissionService;

    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;

    /**
     * 身份安全管理器
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        super.configure(auth);
        auth.userDetailsService(myUserDetailsService);
    }

    /**
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        // 解决静态资源被拦截问题，忽略这些,以及生成验证码的接口
        web.ignoring().antMatchers("/css/**", "/images/**", "/js/**", "/code/**");
    }

    /**
     * 处理http请求
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 在用户名密码过滤器前面加入验证码过滤器
        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class);

        // 设置/user的请求需要admin权限才能访问
//        http.authorizeRequests().antMatchers("/user/**").hasRole("ADMIN");
        // 使用自定义bean完成权限判断
//        http.authorizeRequests().antMatchers("/user/**")
//                .access("@myAuthorizationService.check(authentication, request)");
//        http.authorizeRequests().antMatchers("/user/{id}")
//                .access("@myAuthorizationService.check(authentication, request,#id)");


        // 查数据库的权限列表
        List<Permission> permissionList = permissionService.list();
        for (Permission permission : permissionList) {
            // 添加请求权限
            http.authorizeRequests().antMatchers(permission.getPermissionUrl())
                    .hasAuthority(permission.getPermissionTag());
        }



        // 设置权限不足的处理器
        http.exceptionHandling().accessDeniedHandler(myAccessDeniedHandler);


        // 启用httpBasic认证  httpBasic是弹窗式登录，基本不用       授权请求          所有请求        需要认证
//        http.httpBasic().and().authorizeRequests().anyRequest().authenticated();// 所有请求都需要认证才能访问
        // 默认就是form login
//        http.formLogin().and().authorizeRequests().anyRequest().authenticated();
        // 自定义表单登录
//        http.formLogin().loginPage("/login.html").and().authorizeRequests().anyRequest().authenticated(); // 所有请求都要认证时，会出现重定向次数过多，因为死循环了

//        http.formLogin().loginPage("/toLoginPage")
//                .and().authorizeRequests().antMatchers("/toLoginPage").permitAll()// 放行登陆页面
//                .anyRequest().authenticated();

        http.formLogin() // 开启表单谁
                .loginPage("/toLoginPage") // 自定义登陆页面
                .loginProcessingUrl("/login") // 表单提交路径
                .usernameParameter("username") // 自定义input的name值
                .passwordParameter("password")
                .successForwardUrl("/")  // 登陆成功后的页面
                .successHandler(myAuthenticationService) // 登录成功的处理
                .failureHandler(myAuthenticationService) // 登录失败的处理
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(myAuthenticationService)
                .and()
                .rememberMe() // 开启记住我
                .tokenValiditySeconds(1209600)// 默认有效时间，两周
                .rememberMeParameter("remember-me")// cookie的名称
                .tokenRepository(getPersistentTokenRepository()) // 设置持久化到数据库
                .and().authorizeRequests().antMatchers("/toLoginPage").permitAll()// 放行登陆页面
                .anyRequest().authenticated();


        // 关闭跨域保护
        http.csrf().disable();
        // 开启防护，并声明不需要保护的接口
//        http.csrf().ignoringAntMatchers("/user/saveOrUpdate","/logout");

        // 允许加载同源域名下的iframe
        http.headers().frameOptions().sameOrigin();

        //允许跨域
        http.cors().configurationSource(corsConfigurationSource());


        // session 管理
//        http.sessionManagement()
//                .invalidSessionUrl("/toLoginPage")// session失效之后跳转的路径，默认是登录页
//                .maximumSessions(1) // session最大会话数量，指同一用户同一时间只能有1个用户可以登录，后登录的会把前面的踢掉
//                .maxSessionsPreventsLogin(true) // 如果达到最大session数量就禁止登录，即后登录的无法登录
//                .expiredUrl("/toLoginPage") // session过期跳转的页面
//        ;

    }


    @Autowired
    DataSource dataSource;

    @Bean
    public PersistentTokenRepository getPersistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        // 设置数据源
        tokenRepository.setDataSource(dataSource);
        // 第一次启动时设置为true，第二次之后需要注释掉或者设置为false
//        tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

    /**
     * 跨域配置信息源
     */
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 设置允许跨域的站点
        corsConfiguration.addAllowedOrigin("*");
        // 设置允许跨域的http方法
        corsConfiguration.addAllowedMethod("*");
        // 设置允许跨域的请求头
        corsConfiguration.addAllowedHeader("*");
        // 允许带凭证
        corsConfiguration.setAllowCredentials(true);
        // 对所有的url生效
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

}
