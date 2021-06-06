package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.entity.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * 考虑到 User 对象中仅仅包含账号和密码，为了能够获取到原始的 Admin 对象，专门创建
 * 这个类对 User 类进行扩展
 */
public class SecurityAdmin extends User {

    // 原始的Admin对象，包含Admin对象的全部属性
    private Admin originalAdmin;

    public SecurityAdmin(
            // 传入原始的Admin对象
            Admin originalAdmin,
            // 传入封装角色和权限信息的集合
            Collection<? extends GrantedAuthority> authorities) {
        // 调用父类构造器
        super(originalAdmin.getLoginAcct(),originalAdmin.getUserPswd(), authorities);
        // 给本类中的originalAdmin属性赋值
        this.originalAdmin = originalAdmin;
        // 本身SpringSecurity是会自动把User对象中的密码部分擦除
        // 再手动将原始Admin对象中的密码擦除
        // 擦除密码是在不影响登录认证的情况下，避免密码泄露，增加系统安全性。
        this.originalAdmin.setUserPswd(null);
    }

    // 对外提供获取原始Admin对象的方法
    public Admin getOriginalAdmin() {
        return originalAdmin;
    }
}
