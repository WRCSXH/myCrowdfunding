package com.atguigu.security.config;

import com.atguigu.security.service.AppUserDetailsService;
import com.atguigu.security.util.AppPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
// 注意：本类必须在组件扫描器的扫描包下，否则所有配置都不会生效

// 将当前类标记为配置类
@Configuration

// 启用Web环境下权限控制功能
@EnableWebSecurity
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AppUserDetailsService userDetailsService;

    // @Autowired
    // private AppPasswordEncoder passwordEncoder;

    // 每次调用这个方法时会检查IOC 容器中是否有了对应的bean，如果有就不会真正执行这个函数，因为bean 默认是单例的
    // 可以使用@Scope(value="")注解控制是否单例
    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity security) throws Exception {
        // 准备仓库
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        // 重写源码，类加载器会优先加载我们重写的源码（就近原则），然后进行如下设置创建persistent_logins表存储记住我功能的cookie
        tokenRepository.setCreateTableOnStartup(true);
        tokenRepository.initDao();
        // 也可以自己手动使用sql语句创建persistent_logins表存储记住我功能的cookie

        // super.configure(security); // 禁用父类方法中的默认规则
        security.authorizeRequests()    // 对请求进行授权
                .antMatchers("/index.jsp","/layui/**")  // 使用ANT风格设置要授权的URL地址
                .permitAll()    // 允许无条件访问上面已授权的全部请求
                // 先做小范围具体设置
                .antMatchers("/level1/**")  // 针对/level1/**路径下的所有资源设置访问要求
                .hasRole("学徒")  // 要求用户具备“学徒”角色
                .antMatchers("/level2/**")  // 针对/level2/**路径下的所有资源设置访问要求
                .hasAuthority("内门弟子")   // 要求用户具备“内门弟子”权限
                .antMatchers("/level3/**")  // 针对/level3/**路径下的所有资源设置访问要求
                .hasAuthority("宗师")   // 要求用户具备“宗师”角色
                // 再做大范围模糊设置
                .anyRequest()   // 其他未授权的请求
                .authenticated()    // 需要登录以后才可以访问
                .and()
                .formLogin()    // 使用表单形式登录
                /*
                关于loginPage()方法的特殊说明：
                    指定登录页面的同时会影响到“提交登录表单的请求地址”、“退出登录的请求地址”、“登录失败后跳转的地址”
                指定登录页面前后地址变化
                    指定前：
                    /login GET - the login form
                    /login POST - process the credentials and if valid authenticate the user
                    /login?error GET - redirect here for failed authentication attempts
                    /login?logout GET - redirect here after successfully logging out
                    指定后：
                    /index.jsp GET - the login form     去登录页面
                    /index.jsp POST - process the credentials and if valid authenticate the user    提交登录表单
                    /index.jsp?error GET - redirect here for failed authentication attempts     登录失败
                    /index.jsp?logout GET - redirect here after successfully logging out    退出登录
                 */
                .loginPage("/index.jsp")    // 指定登录页（如果没有指定，用户的请求会跳转到SpringSecurity默认的登录页面：/login）
                // loginProcessingUrl()方法指定了登录地址，就会覆盖掉loginPage()方法中设置的默认地址：/index.jsp
                .loginProcessingUrl("/do/login.html")   // 指定提交登录表单的地址
                .usernameParameter("loginAcct")     // 定制登录账号的请求参数名
                .passwordParameter("userPswd")      // 定制登录密码的请求参数名
                .defaultSuccessUrl("/main.html")    // 设置登录成功后默认跳转的地址
                // .and()
                // .csrf().disable()   // 禁用csrf功能
                .and()
                .logout()   // 开启退出登录功能
                .logoutUrl("/do/logout.html")    // 指定退出登录的请求地址
                .logoutSuccessUrl("/index.jsp")     // 指定退出登录成功后跳转的地址
                .and()
                .exceptionHandling()    // 指定异常处理器
                // .accessDeniedPage("/to/no/auth/page.html")  // 指定访问被拒绝时跳转的地址
                // 一般要想DIY自己的java代码都是调用XXXHandler()方法
                .accessDeniedHandler(new AccessDeniedHandler() {
                    // 当访问被拒绝时，除了跳转到指定的页面，还想进行一些别的操作时，可以重写匿名内部类AccessDeniedHandler中的handle()方法
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
                        // 比如设置request域中message的值
                        request.setAttribute("message",e.getMessage());
                        // 再将请求转发到指定页面
                        request.getRequestDispatcher("/WEB-INF/views/no_auth.jsp").forward(request,response);
                    }
                })
                .and()
                .rememberMe()   // 开启记住我功能（内存版）
                .tokenRepository(tokenRepository)  // 开启记住我功能（数据库版）
                // 开启了数据库版，内存版就会失效
                ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        // super.configure(auth); // 禁用父类方法中的默认规则
        /*
            builder.inMemoryAuthentication()    // 在内存中完成账号密码的检验
                    .withUser("tom").password("123123")     //设置账号密码
                    // SpringSecurity 会在角色字符串前面加“ROLE_”前缀
                    *//*
                        将来从数据库查询得到的用户信息、角色信息、权限信息需要我们自己手动组装，
                        手动组装时需要我们自己给角色字符串前面加“ROLE_”前缀。
                     *//*
                    .roles("ADMIN","学徒")     // 设置用户的角色
                    .and()
                    .withUser("jerry").password("123123")   //设置另一组账号密码
                    .authorities("UPDATE","内门弟子")    // 设置用户的权限
                    .and()
                    .withUser("kevin").password("123123")
                    .roles("宗师")
                    ;
        */

        /*
            如果没有设置用户的角色或者权限，SpringSecurity会报错：Cannot pass a null GrantedAuthority collection
            因为通过SpringSecurity进行权限控制时，用户的请求必须经历认证（authentication）、授权（authorization）的过程
            才会是完整的主体（principal），才能访问对应的资源
        */

        // 装配userDetailsService
        builder.userDetailsService(userDetailsService).passwordEncoder(getBCryptPasswordEncoder());
    }
}
