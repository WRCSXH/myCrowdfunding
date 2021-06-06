package com.atguigu.crowd.filter;

import com.atguigu.crowd.constant.AccessPassResources;
import com.atguigu.crowd.constant.CrowdConstant;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class CrowdAccessFilter extends ZuulFilter {
    @Override
    public String filterType() {
        // 这里返回“pre”意思是在目标微服务前执行过滤
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        // 获取 RequestContext 对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        // 通过 RequestContext 对象获取当前请求对象（框架底层借助 ThreadLocal 从当前线程中获取事先绑定的 request 对象）
        HttpServletRequest request = requestContext.getRequest();
        // 获取当前请求的 ServletPath
        String servletPath = request.getServletPath();
        // 根据 ServletPath 判断当前请求是否是可直接访问的请求
        boolean contains = AccessPassResources.REQUEST_SET.contains(servletPath);
        if (contains){
            // false 表示应该放行
            return false;
        }
        // 判断当前请求访问的是否是静态资源
        // 工具方法返回 true ，表示是静态资源，应该放行，取反为 false 表示应该放行
        // 工具方法返回 false ，表示不是静态资源，应该拦截，取反为 true 表示应该拦截
        return !AccessPassResources.judgeCurrentServletPathWhetherStaticResource(servletPath);
    }

    @Override
    public Object run() throws ZuulException {
        // 获取 RequestContext 对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        // 获取当前 Request 对象
        HttpServletRequest request = requestContext.getRequest();
        // 获取当前 Session 对象
        HttpSession session = request.getSession();
        // 尝试从 Session 对象中获取已登录的用户
        Object loginMember = session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        // 判断 loginMember 是否为空
        if (loginMember == null){
            // 获取 Response 对象
            HttpServletResponse response = requestContext.getResponse();
            // 将提示信息存入 session 域
            session.setAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_ACCESS_FORBIDDEN);
            // 将请求重定向到 auth-consumer 工程中的 member-login 页面
            try {
                response.sendRedirect("http://localhost/auth/member/to/login/page.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
