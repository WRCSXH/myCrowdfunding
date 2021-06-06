package com.atguigu.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SecurityTest {
    public static void main(String[] args) {
        // 创建BCryptPasswordEncoder对象
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 准备明文字符串
        String rawPassword = "123123";
        // 测试带盐值的加密
        String encode = passwordEncoder.encode(rawPassword);
        System.out.println(encode);
        // $2a$10$hLa02sb5SXATQcEc/rGecuDOhaMuyYXJoo8jsVgMPJyyxG5Wd8D02
        // $2a$10$QrEjDbObVwXrhXXMWIBUoOfhc5ghAT2E5N7KqhHjT2J.KwQVn7ZTS
        // $2a$10$szT5RYztXvELNkwFvpnYDOtnIdwdvejG1lIAgHa1QWeIYuBih/bpW
    }
}

class EncodeTest {
    public static void main(String[] args) {
        // 创建BCryptPasswordEncoder对象
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 准备明文字符串
        String rawPassword = "123123";
        // 准备密文字符串
        String encodedPassword = "$2a$10$hLa02sb5SXATQcEc/rGecuDOhaMuyYXJoo8jsVgMPJyyxG5Wd8D02";
        // 比较明文和带盐值加密的密文
        boolean matchesResult = passwordEncoder.matches(rawPassword,encodedPassword);
        System.out.println(matchesResult == true?"一致":"不一致");
    }
}
