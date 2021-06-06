package com.atguigu.crowd.mvc.interceptor;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.exception.AccessForbiddenException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录拦截器
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    // 继承抽象类HandlerInterceptorAdapter可以降低实现接口HandlerInterceptor的难度
    // 因为只需要在登录之前做拦截检查，所以只需要重写preHandle()方法
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1、通过request获取session
        HttpSession session = request.getSession();
        // 2、尝试从session中获取admin
        Admin admin = (Admin) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN);
        // 3、判断admin是否为空
        if (admin == null){
            // 如果admin为空，抛出异常
            throw new AccessForbiddenException(CrowdConstant.MESSAGE_ACCESS_FORBIDDEN);
        }
        // 如果admin不为空，返回true，放行
        return true;
    }
}
