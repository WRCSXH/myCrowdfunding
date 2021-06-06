package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.vo.AddressVO;
import com.atguigu.crowd.entity.vo.OrderProjectReturnVO;
import com.atguigu.crowd.entity.vo.OrderVO;

import java.util.List;

public interface OrderService {
    OrderProjectReturnVO getOrderProjectReturnVO(Integer projectId, Integer returnId);

    List<AddressVO> getAddressVOList(Integer memberId);

    void saveNewAddress(AddressVO addressVO);

    void saveOrderVO(OrderVO orderVO);
}
