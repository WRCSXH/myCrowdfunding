//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

// 采用解决方案二：改源码
/*
DelegatingFilterProxy查找IOC容器中所需bean的过程
    1、在初始化时查找IOC容器，找不到，放弃。
    2、第一次请求时再次查找。
    3、找到SpringMVC的IOC容器。
    4、从这个IOC容器中找到所需要的bean。
 */
/*
意外收获，发现了SpringSecurity的工作原理：
    在初始化时或第一次请求时创建了过滤器链，具体任务由具体过滤器来完成
[
Creating filter chain:
    org.springframework.security.web.util.matcher.AnyRequestMatcher@1,
    [
    org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter@23e736c9,
    org.springframework.security.web.context.SecurityContextPersistenceFilter@2c64af2d,
    org.springframework.security.web.header.HeaderWriterFilter@2964c970,
    org.springframework.security.web.csrf.CsrfFilter@73079ceb,
    org.springframework.security.web.authentication.logout.LogoutFilter@81cc373,
    org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter@332523da,
    org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter@6579d227,
    org.springframework.security.web.authentication.www.BasicAuthenticationFilter@2e837aae,
    org.springframework.security.web.savedrequest.RequestCacheAwareFilter@39242237,
    org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter@78c4d589,
    org.springframework.security.web.authentication.AnonymousAuthenticationFilter@197e7032,
    org.springframework.security.web.session.SessionManagementFilter@517a72c8,
    org.springframework.security.web.access.ExceptionTranslationFilter@3c354c6e,
    org.springframework.security.web.access.intercept.FilterSecurityInterceptor@6ff9ecea
    ]
]
 */
package org.springframework.web.filter;

import java.io.IOException;
import javax.servlet.*;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.FrameworkServlet;

public class DelegatingFilterProxy extends GenericFilterBean {
    private String contextAttribute;
    private WebApplicationContext webApplicationContext;
    private String targetBeanName;
    private boolean targetFilterLifecycle;
    private volatile Filter delegate;
    private final Object delegateMonitor;

    public DelegatingFilterProxy() {
        this.targetFilterLifecycle = false;
        this.delegateMonitor = new Object();
    }

    public DelegatingFilterProxy(Filter delegate) {
        this.targetFilterLifecycle = false;
        this.delegateMonitor = new Object();
        Assert.notNull(delegate, "Delegate Filter must not be null");
        this.delegate = delegate;
    }

    public DelegatingFilterProxy(String targetBeanName) {
        this(targetBeanName, (WebApplicationContext)null);
    }

    public DelegatingFilterProxy(String targetBeanName, WebApplicationContext wac) {
        this.targetFilterLifecycle = false;
        this.delegateMonitor = new Object();
        Assert.hasText(targetBeanName, "Target Filter bean name must not be null or empty");
        this.setTargetBeanName(targetBeanName);
        this.webApplicationContext = wac;
        if (wac != null) {
            this.setEnvironment(wac.getEnvironment());
        }

    }

    public void setContextAttribute(String contextAttribute) {
        this.contextAttribute = contextAttribute;
    }

    public String getContextAttribute() {
        return this.contextAttribute;
    }

    public void setTargetBeanName(String targetBeanName) {
        this.targetBeanName = targetBeanName;
    }

    protected String getTargetBeanName() {
        return this.targetBeanName;
    }

    public void setTargetFilterLifecycle(boolean targetFilterLifecycle) {
        this.targetFilterLifecycle = targetFilterLifecycle;
    }

    protected boolean isTargetFilterLifecycle() {
        return this.targetFilterLifecycle;
    }

    protected void initFilterBean() throws ServletException {
        synchronized(this.delegateMonitor) {
            if (this.delegate == null) {
                if (this.targetBeanName == null) {
                    this.targetBeanName = this.getFilterName();
                }

                // DelegatingFilterProxy初始化时直接跳过查找IOC容器的环节
                /*
                    WebApplicationContext wac = this.findWebApplicationContext();
                    if (wac != null) {
                        this.delegate = this.initDelegate(wac);
                    }
                */
            }

        }
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Filter delegateToUse = this.delegate;
        if (delegateToUse == null) {
            synchronized(this.delegateMonitor) {
                delegateToUse = this.delegate;
                if (delegateToUse == null) {
                    // 注释源码中查找IOC容器的代码
                    // WebApplicationContext wac = this.findWebApplicationContext();
                    // 按我们的需求重写
                    // 获取ServletContext对象（全局作用域对象）
                    ServletContext application = this.getServletContext();
                    // 拼接SpringMVC将IOC容器存入ServletContext域时IOC容器的属性名
                    String servletName = "springDispatcherServlet";
                    String attrName = FrameworkServlet.SERVLET_CONTEXT_PREFIX + servletName;
                    // 根据attrName从ServletContext域中获取IOC容器对象
                    WebApplicationContext wac = (WebApplicationContext) application.getAttribute(attrName);
                    if (wac == null) {
                        throw new IllegalStateException("No WebApplicationContext found: no ContextLoaderListener or DispatcherServlet registered?");
                    }

                    delegateToUse = this.initDelegate(wac);
                }

                this.delegate = delegateToUse;
            }
        }

        this.invokeDelegate(delegateToUse, request, response, filterChain);
    }

    public void destroy() {
        Filter delegateToUse = this.delegate;
        if (delegateToUse != null) {
            this.destroyDelegate(delegateToUse);
        }

    }

    protected WebApplicationContext findWebApplicationContext() {
        if (this.webApplicationContext != null) {
            if (this.webApplicationContext instanceof ConfigurableApplicationContext) {
                ConfigurableApplicationContext cac = (ConfigurableApplicationContext)this.webApplicationContext;
                if (!cac.isActive()) {
                    cac.refresh();
                }
            }

            return this.webApplicationContext;
        } else {
            String attrName = this.getContextAttribute();
            return attrName != null ? WebApplicationContextUtils.getWebApplicationContext(this.getServletContext(), attrName) : WebApplicationContextUtils.findWebApplicationContext(this.getServletContext());
        }
    }

    protected Filter initDelegate(WebApplicationContext wac) throws ServletException {
        Filter delegate = (Filter)wac.getBean(this.getTargetBeanName(), Filter.class);
        if (this.isTargetFilterLifecycle()) {
            delegate.init(this.getFilterConfig());
        }

        return delegate;
    }

    protected void invokeDelegate(Filter delegate, ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        delegate.doFilter(request, response, filterChain);
    }

    protected void destroyDelegate(Filter delegate) {
        if (this.isTargetFilterLifecycle()) {
            delegate.destroy();
        }

    }
}

