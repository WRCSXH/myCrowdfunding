package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 回报详情信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailReturnVO {
    private Integer returnId; // 回报 id
    private Integer supportMoney; // 单笔支持金额
    private Integer signalPurchase; // 是否限制单笔购买数量，0 表示不限购，1 表示限购
    private Integer purchase; // 如果单笔限购，那么具体的限购数量
    private Integer freight; // 运费，0-包邮
    private Integer returnDate; // 众筹结束后返还回报物品天数
    private String content; // 回报内容介绍
}
