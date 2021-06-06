package com.atguigu.crowd.handler;

import com.atguigu.crowd.api.MysqlRemoteService;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.vo.AddressVO;
import com.atguigu.crowd.entity.vo.MemberLoginVO;
import com.atguigu.crowd.entity.vo.OrderProjectReturnVO;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jws.WebParam;
import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
public class OrderConsumerHandler {

    @Autowired
    private MysqlRemoteService mysqlRemoteService;

    @RequestMapping("/get/return/confirm/info/{projectId}/{returnId}")
    public String getReturnConfirmInfo(@PathVariable("projectId") Integer projectId,
                                       @PathVariable("returnId") Integer returnId,
                                       ModelMap modelMap,
                                       HttpSession session){
        try {
            ResultEntity<OrderProjectReturnVO> returnConfirmInfoResultEntity = mysqlRemoteService.getReturnConfirmInfoRemote(projectId,returnId);
            String result = returnConfirmInfoResultEntity.getResult();
            if (ResultEntity.SUCCESS.equals(result)){
                OrderProjectReturnVO orderProjectReturnVO = returnConfirmInfoResultEntity.getData();
                session.setAttribute(CrowdConstant.ATTR_NAME_RETURN_CONFIRM_INFO,orderProjectReturnVO);
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_RETURN_CONFIRM_INFO,orderProjectReturnVO);
                return "order-confirm-return";
            } else {
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_RETURN_CONFIRM_INFO_MISSING);
                return "order-confirm-return";
            }
        } catch (Exception e) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,e.getMessage());
            return "order-confirm-return";
        }
    }

    @RequestMapping("/get/order/confirm/info/{returnCount}")
    public String getOrderConfirmInfo(@PathVariable("returnCount") Integer returnCount,
                                      ModelMap modelMap,
                                      HttpSession session){
        try {
            // 1.合并回报数
            OrderProjectReturnVO orderProjectReturnVO = (OrderProjectReturnVO) session.getAttribute(CrowdConstant.ATTR_NAME_RETURN_CONFIRM_INFO);
            orderProjectReturnVO.setReturnCount(returnCount);
            // 2.更新 session
            session.setAttribute(CrowdConstant.ATTR_NAME_RETURN_CONFIRM_INFO,orderProjectReturnVO);
            // 3.获取 memberId
            MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
            Integer memberId = memberLoginVO.getId();
            // 4.根据 memberId 查找 addressVOList
            ResultEntity<List<AddressVO>> addressVOListResultEntity = mysqlRemoteService.getAddressVOListRemote(memberId);
            // 5.判断是否获取成功
            String result = addressVOListResultEntity.getResult();
            if (ResultEntity.SUCCESS.equals(result)){
                List<AddressVO> addressVOList = addressVOListResultEntity.getData();
                // 6.存入 session 域
                session.setAttribute(CrowdConstant.ATTR_NAME_ORDER_CONFIRM_INFO,addressVOList);
                // 7.存入 modelMap
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_ORDER_CONFIRM_INFO,addressVOList);
                return "order-confirm-order";
            } else {
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_ORDER_CONFIRM_INFO_MISSING);
                return "order-confirm-order";
            }
        } catch (Exception e){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,e.getMessage());
            return "order-confirm-order";
        }
    }

    @RequestMapping("/save/new/address")
    public String saveNewAddress(AddressVO addressVO, HttpSession session,ModelMap modelMap){
        Integer returnCount = 0;
        try {
            MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
            Integer memberId = memberLoginVO.getId();
            addressVO.setMemberId(memberId);
            ResultEntity<String> saveNewAddressResultEntity = mysqlRemoteService.saveNewAddressRemote(addressVO);
            String result = saveNewAddressResultEntity.getResult();
            OrderProjectReturnVO orderProjectReturnVO = (OrderProjectReturnVO) session.getAttribute(CrowdConstant.ATTR_NAME_RETURN_CONFIRM_INFO);
            returnCount = orderProjectReturnVO.getReturnCount();
            if (ResultEntity.SUCCESS.equals(result)) {
                return "redirect:http://localhost/order/get/order/confirm/info/"+returnCount;
            } else {
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_SAVE_NEW_ADDRESS_FAILED);
                return "redirect:http://localhost/order/get/order/confirm/info/"+returnCount;
            }
        } catch (Exception e) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,e.getMessage());
            return "redirect:http://localhost/order/get/order/confirm/info/"+returnCount;
        }
    }
}
