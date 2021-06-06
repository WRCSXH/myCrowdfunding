package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 前台系统的用户信息
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberVO {
    private String loginacct; // 账号
    private String userpswd; // 密码
    private String username; // 用户名
    private String email; // 邮箱
    private String mobile; // 手机号
    private String code; // 验证码
}
