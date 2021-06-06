package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 首页项目信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortalProjectVO {
    private Integer projectId; // 项目id
    private String headerPicturePath; // 头图路径
    private String projectName; // 项目名称
    private Long money; // 支持金额
    private Integer percentage; // 项目进度
    private String deployDate; // 发布日期
    private Integer supporter; // 支持人数
}
