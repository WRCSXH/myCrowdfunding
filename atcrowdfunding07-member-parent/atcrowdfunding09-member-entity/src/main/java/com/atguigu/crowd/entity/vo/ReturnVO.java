package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 回报信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnVO implements Serializable {
    private Integer type; // 回报类型：0-实物回报， 1-虚拟物品回报
    private Integer supportmoney; // 支持金额
    private String content; // 回报内容介绍
    private Integer count; // 总回报数量，0 为不限制
    private Integer signalpurchase; // 是否限制单笔购买数量，0-不限购，1-限购
    private Integer purchase; // 如果单笔限购，那么具体的限购数量
    private Integer freight; // 运费，“0”为包邮
    private Integer invoice; // 是否开发票，0-不开发票， 1-开发票
    private Integer returndate; // 众筹结束后几天内回报物品
    private String describPicPath; // 回报说明的图片路径
}
