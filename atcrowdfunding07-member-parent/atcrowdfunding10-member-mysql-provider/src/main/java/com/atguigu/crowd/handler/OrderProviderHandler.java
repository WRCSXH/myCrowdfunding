package com.atguigu.crowd.handler;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.vo.AddressVO;
import com.atguigu.crowd.entity.vo.MemberLoginVO;
import com.atguigu.crowd.entity.vo.OrderProjectReturnVO;
import com.atguigu.crowd.entity.vo.OrderVO;
import com.atguigu.crowd.service.api.OrderService;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderProviderHandler {

    @Autowired
    private OrderService orderService;

    @RequestMapping("/get/return/confirm/info/{projectId}/{returnId}")
    public ResultEntity<OrderProjectReturnVO> getReturnConfirmInfoRemote(@PathVariable("projectId") Integer projectId,
                                                                         @PathVariable("returnId") Integer returnId){
        try {
            OrderProjectReturnVO orderProjectReturnVO = orderService.getOrderProjectReturnVO(projectId,returnId);
            return ResultEntity.successWithData(orderProjectReturnVO);
        } catch (Exception e) {
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/get/address/vo/list/{memberId}")
    public ResultEntity<List<AddressVO>> getAddressVOListRemote(@PathVariable("memberId") Integer memberId){
        try {
            List<AddressVO> addressVOList = orderService.getAddressVOList(memberId);
            return ResultEntity.successWithData(addressVOList);
        } catch (Exception e) {
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/save/new/address")
    public ResultEntity<String> saveNewAddress(@RequestBody AddressVO addressVO){
        try{
            orderService.saveNewAddress(addressVO);
            return ResultEntity.successWithoutData();
        } catch (Exception e){
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/save/ordervo/remote")
    public ResultEntity<String> saveOrderVORemote(@RequestBody OrderVO orderVO){
        try{
            orderService.saveOrderVO(orderVO);
            return ResultEntity.successWithoutData();
        } catch (Exception e){
            return ResultEntity.failed(e.getMessage());
        }
    }
}
