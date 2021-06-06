package com.atguigu.crowd.api;

import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.vo.*;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("atguigu-crowd-mysql")
public interface MysqlRemoteService {
    @RequestMapping("/get/memberpo/by/loginacct/remote")
    ResultEntity<MemberPO> getMemberPOByLoginAcctRemote(@RequestParam("loginacct") String loginacct);

    @RequestMapping("/save/memberpo/remote")
    ResultEntity<String> saveMemberPORemote(@RequestBody MemberPO memberPO);

    @RequestMapping("/save/projectvo/remote")
    ResultEntity<String> saveProjectVORemote(@RequestBody ProjectVO projectVO,@RequestParam("memberid")Integer memberid);

    @RequestMapping("/get/portal/type/vo/list/remote")
    ResultEntity<List<PortalTypeVO>> getPortalTypeVOListRemote();

    @RequestMapping("/get/detail/projectvo/remote/{projectId}")
    ResultEntity<DetailProjectVO> getDetailProjectVORemote(@PathVariable("projectId") Integer projectId);

    @RequestMapping("/get/return/confirm/info/{projectId}/{returnId}")
    ResultEntity<OrderProjectReturnVO> getReturnConfirmInfoRemote(@PathVariable("projectId") Integer projectId,
                                                                  @PathVariable("returnId") Integer returnId);
    @RequestMapping("/get/address/vo/list/{memberId}")
    ResultEntity<List<AddressVO>> getAddressVOListRemote(@PathVariable("memberId") Integer memberId);


    @RequestMapping("/save/new/address")
    ResultEntity<String> saveNewAddressRemote(@RequestBody AddressVO addressVO);

    @RequestMapping("/save/ordervo/remote")
    ResultEntity<String> saveOrderVORemote(@RequestBody OrderVO orderVO);
}
