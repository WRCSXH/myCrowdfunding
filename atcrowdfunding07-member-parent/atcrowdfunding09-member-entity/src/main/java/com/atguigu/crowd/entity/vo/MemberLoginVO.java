package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 前台系统登录成功后的用户信息
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberLoginVO implements Serializable {
    private Integer id; // 自增主键
    private String username; // 用户名
    private String email; // 邮箱
}
