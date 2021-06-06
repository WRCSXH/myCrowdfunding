package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单中的项目信息（包含回报信息）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderProjectReturnVO implements Serializable {
    private Integer id; // 本类的主键 id
    private String projectName; // 项目名称
    private String launchName;  // 发起人名称
    private String returnContent;   // 回报内容
    private Integer returnCount;    // 回报数量
    private Integer supportPrice;   // 支持单价
    private Integer freight;    // 运费，0-包邮
    private Integer orderId;    // 对应的订单 id
    private Integer signalPurchase; // 是否限购，0-不限购，1-限购
    private Integer purchase;   // 限购数量
}
