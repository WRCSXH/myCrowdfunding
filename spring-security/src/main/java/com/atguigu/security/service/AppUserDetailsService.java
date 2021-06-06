package com.atguigu.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据表单提交的用户名查询用户对象，并装配角色、权限等信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(
            // 表单提交的用户名
            String username) throws UsernameNotFoundException {
        // 使用sql语句根据用户名查询用户对象
        String sql = "select id,loginacct,userpswd,username,email,createtime from t_admin where loginacct=?";
        // 获取查询结果
        Map<String,Object> resultMap = jdbcTemplate.queryForMap(sql,username);
        // 获取用户名、密码数据
        String loginacct = resultMap.get("loginacct").toString();
        String userpswd = resultMap.get("userpswd").toString();
        // 给用户设置角色和权限，并封装到权限列表中
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        authorityList.add(new SimpleGrantedAuthority("UPDATE"));
        // 把loginacct、userpswd和authorityList封装到UserDetails的实现类User中
        return new User(loginacct,userpswd,authorityList);
    }
}
