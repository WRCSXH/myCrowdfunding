package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 首页分类信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortalTypeVO {
    private Integer id; // 分类 id
    private String name; // 分类名称
    private String remark; // 分类介绍
    private List<PortalProjectVO> portalProjectVOList; // 分类中的具体项目 list
}
