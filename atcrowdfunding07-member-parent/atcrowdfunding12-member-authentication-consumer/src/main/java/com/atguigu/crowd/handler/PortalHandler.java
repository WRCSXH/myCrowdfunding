package com.atguigu.crowd.handler;

import com.atguigu.crowd.api.MysqlRemoteService;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.vo.DetailProjectVO;
import com.atguigu.crowd.entity.vo.PortalTypeVO;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class PortalHandler {
    @Autowired
    private MysqlRemoteService mysqlRemoteService;

    @RequestMapping("/")
    public String getPortalTypeVOList(ModelMap modelMap){
        try {
            ResultEntity<List<PortalTypeVO>> portalTypeVOListResultEntity = mysqlRemoteService.getPortalTypeVOListRemote();
            String result = portalTypeVOListResultEntity.getResult();
            if (ResultEntity.SUCCESS.equals(result)){
                List<PortalTypeVO> portalTypeVOList = portalTypeVOListResultEntity.getData();
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_PORTAL_DATA,portalTypeVOList);
                return "portal";
            } else {
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_PORTAL_DATA_MISSING);
                return "portal";
            }
        } catch (Exception e){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,e.getMessage());
            return "portal";
        }
    }
}