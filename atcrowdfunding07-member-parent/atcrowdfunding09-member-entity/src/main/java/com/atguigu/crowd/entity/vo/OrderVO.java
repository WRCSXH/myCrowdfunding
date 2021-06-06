package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderVO implements Serializable {
    private Integer id; // 主键 id
    private String orderNum;    // 订单号
    private String payOrderNum; // 支付宝流水单号
    private Double orderAmount; // 订单金额
    private Integer invoice;    // 是否开发票，0-不开发票， 1-开发票
    private String invoiceTitle;    // 发票抬头
    private String orderRemark; // 订单备注
    private String addressId;   // 对应的收货地址 id
    private OrderProjectReturnVO orderProjectReturnVO; // 对应的 orderProjectReturnVO
}
