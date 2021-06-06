package com.atguigu.crowd.constant;

import java.util.HashSet;
import java.util.Set;

/**
 * 不需要登录就能访问的资源
 */
public class AccessPassResources {
    // 请求
    public static final Set<String> REQUEST_SET = new HashSet<>();
    static {
        REQUEST_SET.add("/");
        REQUEST_SET.add("/auth/member/to/register/page.html");
        REQUEST_SET.add("/auth/member/get/code");
        REQUEST_SET.add("/auth/member/register");
        REQUEST_SET.add("/auth/member/to/login/page.html");
        REQUEST_SET.add("/auth/member/login");
        REQUEST_SET.add("/auth/member/logout");
    }
    // 静态资源
    public static final Set<String> STATIC_RESOURCE_SET = new HashSet<>();
    static {
        STATIC_RESOURCE_SET.add("bootstrap");
        STATIC_RESOURCE_SET.add("css");
        STATIC_RESOURCE_SET.add("fonts");
        STATIC_RESOURCE_SET.add("img");
        STATIC_RESOURCE_SET.add("jquery");
        STATIC_RESOURCE_SET.add("layer");
        STATIC_RESOURCE_SET.add("script");
        STATIC_RESOURCE_SET.add("ztree");
    }

    /**
     * 判断当前某个 ServletPath 是否对应某个静态资源
     * @param servletPath
     * @return 返回 true 是静态资源，返回 false 不是静态资源
     */
    public static boolean judgeCurrentServletPathWhetherStaticResource(String servletPath){
        // 排除字符串无效的情况
        if (servletPath == null || servletPath.length() == 0){
            return false;
        }
        // 按照“/”拆分 ServletPath 字符串，获取第一个有效字符串
        String[] split = servletPath.split("/");
        // 由于 ServletPath 的格式为 /aaa/bbb/ccc，所以 split 数组的第一个元素是空字符串，所以下标为1的元素才是有效的
        String firstItem = split[1];
        // 看这个元素是否包含在代表静态资源的 set 集合中
        if (AccessPassResources.STATIC_RESOURCE_SET.contains(firstItem)){
            return true;
        }
        return false;
    }
}
