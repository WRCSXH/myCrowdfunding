package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 项目详情信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailProjectVO {
    private Integer projectId; // 项目 id
    private String projectName; // 项目名称
    private String projectDescription; // 项目描述
    private Integer followerCount; // 项目关注者人数
    private String headerPicturePath; // 项目头图路径
    List<String> detailPicturePathList; // 项目详情图路径
    private Integer status; // 项目状态，0-即将开始，1-众筹中，2-众筹成功，3-众筹失败
    private String statusText; // 项目状态文本
    private Long money; // 目标金额
    private Long day; // 众筹总天数
    private Integer percentage; // 筹集进度
    private Long supportMoney; // 已筹资金
    private String deployDate; // 项目审核通过当天的日期
    private Long lastDays; // 项目剩余天数
    private Integer supporterCount; // 项目支持者人数
    private List<DetailReturnVO> detailReturnVOList; // 项目回报详情
}
