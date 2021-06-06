package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressVO implements Serializable {
    private Integer id; // 地址 id
    private String receiveName; // 收件人姓名
    private String phoneNum;    // 联系电话
    private String address; // 收件地址
    private Integer memberId;   // 发起人 id
}