package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 项目信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectVO implements Serializable {
    private List<Integer> typeIdList; // 分类id 集合
    private List<Integer> tagIdList; // 标签id 集合
    private String projectName; // 项目名称
    private String projectDescription; // 项目描述
    private Long money; // 计划筹集的金额
    private Integer day; // 众筹持续的天数
    private String createdate; // 创建项目的日期
    private String headerPicturePath; // 头图的路径
    private List<String> detailPicturePathList; // 详情图片的路径
    private MemberLaunchInfoVO memberLaunchInfoVO; // 发起人信息
    private List<ReturnVO> returnVOList; // 回报信息集合
    private MemberConfirmInfoVO memberConfirmInfoVO; // 发起人确认信息
}