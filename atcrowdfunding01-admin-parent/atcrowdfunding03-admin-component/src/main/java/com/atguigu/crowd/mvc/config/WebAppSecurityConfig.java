package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.constant.CrowdConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 表示当前类是一个配置类
@Configuration
// 启用web环境下权限控制功能
@EnableWebSecurity
// 启用全局方法权限控制功能，并且设置prePostEnabled = true。保证@PreAuthorize、@PostAuthorize、@PreFilter、@PostFilter 生效
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    // 如果在SpringSecurity的配置类中用@Bean注解将BCryptPasswordEncoder 对象存入IOC 容器，那么Service 组件将获取不到。
    /*@Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }*/

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // 与SpringSecurity环境下用户登录相关（为用户分配角色和权限）
    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        // 临时使用内存版登录的方法要求用户具有某种角色或权限
        // builder.inMemoryAuthentication().withUser("tom").password("123123").roles("ADMIN");
        // 正式功能中使用基于数据库的认证
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    // 与SpringSecurity环境下请求授权相关（要求用户具有某种角色或权限才能访问某种资源）
    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security.authorizeRequests()    // 对请求进行授权
                .antMatchers("/admin/to/login/page.html").permitAll()   // 针对登录页设置无条件放行
                // 针对静态资源设置，无条件访问
                .antMatchers("/bootstrap/**").permitAll()
                .antMatchers("/crowd/**").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/fonts/**").permitAll()
                .antMatchers("/jquery/**").permitAll()
                .antMatchers("/img/**").permitAll()
                .antMatchers("/layer/**").permitAll()
                .antMatchers("/script/**").permitAll()
                .antMatchers("/ztree/**").permitAll()
                // 要求访问Admin分页功能时具备“经理”角色或“user:get”权限（适用于传统请求）
                .antMatchers("/admin/get/page.html").access("hasRole('经理') OR hasAuthority('user:get')")
                .anyRequest().authenticated()   // 其它资源需要认证后才能访问
                .and()
                .csrf().disable()   // 禁用防跨站请求伪造功能
                .formLogin()    // 开启页面登录功能
                .loginPage("/admin/to/login/page.html") // 指定登录页面
                .loginProcessingUrl("/security/do/login.html")  // 指定登录的请求地址
                .defaultSuccessUrl("/admin/to/main/page.html")  // 指定登录成功后默认跳转的页面
                .usernameParameter("loginAcct") // 指定账号的请求参数名称
                .passwordParameter("userPswd") // 指定密码的请求参数名称
                .and()
                .logout()   // 开启退出登录功能
                .logoutUrl("/security/do/logout.html")  // 指定退出登录的请求地址
                .logoutSuccessUrl("/admin/to/login/page.html")  // 指定退出登录成功后默认跳转的页面
                .and()
                .exceptionHandling()    // 开启异常映射功能
                .accessDeniedHandler(new AccessDeniedHandler() {    // 指定访问被拒绝时的处理器
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
                        request.setAttribute("crowdError", CrowdConstant.MESSAGE_ACCESS_DENIED);
                        request.getRequestDispatcher("/WEB-INF/system-error.jsp").forward(request,response);// 适用于传统请求
                    }
                })
                ;
    }
}
