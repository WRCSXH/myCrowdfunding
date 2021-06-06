package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 发起人发起信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberLaunchInfoVO implements Serializable {
    private String descriptionSimple;   // 简单介绍
    private String descriptionDetail;   // 详细介绍
    private String phoneNum;    // 联系电话
    private String serviceNum;  // 客服电话
}