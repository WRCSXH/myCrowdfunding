package com.atguigu.crowd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@Configuration
public class CrowdWebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 去权限的注册页
        String urlPath1 = "/auth/member/to/register/page.html";
        String viewName1 = "member-register";
        registry.addViewController(urlPath1).setViewName(viewName1);
        // 去权限的登录页
        String urlPath2 = "/auth/member/to/login/page.html";
        String viewName2 = "member-login";
        registry.addViewController(urlPath2).setViewName(viewName2);
        // 去权限的中心页
        String urlPath3 = "/auth/member/to/center/page.html";
        String viewName3 = "member-center";
        registry.addViewController(urlPath3).setViewName(viewName3);
        // 去项目的我的众筹页
        String urlPath4 = "/auth/member/to/my/crowd/page.html";
        String viewName4 = "member-my-crowd";
        registry.addViewController(urlPath4).setViewName(viewName4);
    }
}
