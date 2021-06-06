package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.service.api.AuthService;
import com.atguigu.crowd.service.api.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CrowdUserDetailsService implements UserDetailsService {
    @Autowired
    private AdminService adminService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AuthService authService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据账号名称查询Admin对象（此时用户已经成功登录）
        Admin admin = adminService.getAdminByLoginAcct(username);
        // 获取adminId
        Integer adminId = admin.getId();
        // 根据adminId查询已分配的角色信息
        List<Role> roleList = roleService.getAssignedRoleList(adminId);
        // 根据adminId查询已分配的权限信息
        List<String> authNameList = authService.getAssignedAuthNameByAdminId(adminId);
        // 创建集合对象存储角色和权限信息
        List<GrantedAuthority> authorities = new ArrayList<>();
        // 遍历roleList存入角色信息
        for (Role role:roleList){
            // 注意，不要忘了加前缀：ROLE_
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getName()));
        }
        // 遍历authNameList存入权限信息
        for (String authName:authNameList){
            authorities.add(new SimpleGrantedAuthority(authName));
        }
        // 封装SecurityAdmin对象并返回
        return new SecurityAdmin(admin,authorities);
    }
}
