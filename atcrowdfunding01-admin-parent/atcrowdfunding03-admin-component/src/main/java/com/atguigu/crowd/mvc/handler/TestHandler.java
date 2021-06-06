package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.ParamData;
import com.atguigu.crowd.entity.Student;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class TestHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AdminService adminService;

    // 测试用户登录
    @RequestMapping("/test/login.json")
    @ResponseBody
    public ResultEntity<Object> testLogin(String name,String password){
        if(!("zs".equals(name))) throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED_ACCT_NOT_EXIST);
        if(!("123".equals(password))) throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED_ACCT_PASSWORD_ERROR);
        return ResultEntity.successWithData(CrowdConstant.MESSAGE_LOGIN_SUCCESS);
    }

    @RequestMapping("/test/testSsm.html")
    // ModelMap对象主要用于传递控制方法处理数据到结果页面
    public String testSsm(ModelMap modelMap, HttpServletRequest request){
        boolean judgeResult = CrowdUtil.judgeRequestType(request);
        logger.info("judgeResult = "+judgeResult);
        List<Admin> adminList = adminService.getAll();
        modelMap.addAttribute("adminList",adminList);

        try{
            // 空指针异常
            String a = null;
            System.out.println(a.length());
        }
        catch (NullPointerException exception){
            throw new NullPointerException("空指针异常！");
        }

        return "target";
    }

    // 测试ResultEntity
    @RequestMapping("/send/compose/object.json")
    @ResponseBody
    public ResultEntity<Student> testReceiveComposeObjectAndResultEntity(@RequestBody Student student, HttpServletRequest request){
        boolean judgeResult = CrowdUtil.judgeRequestType(request);
        logger.info("judgeResult = "+judgeResult);
        logger.info("student = "+student);
        // 假设将查询到的Student对象封装到ResultEntity中返回
        ResultEntity<Student> resultEntity = ResultEntity.successWithData(student);

        // 除零异常
        System.out.println(10/0);

        return resultEntity;
    }

    @ResponseBody
    @RequestMapping("/send/array/three.html")
    public String testReceiveArrayThree(@RequestBody List<Integer> array){
        for (Integer num:array) {
            logger.info("num = "+num);
        }
        return "success";
    }

    @ResponseBody
    @RequestMapping("/send/array/two.html")
    public String testReceiveArrayTwo(ParamData paramData){
        List<Integer> array = paramData.getArray();
        for (Integer num:array) {
            logger.info("num = "+num);
        }
        return "success";
    }

    @ResponseBody
    @RequestMapping("/send/array/one.html")
    public String testReceiveArrayOne( @RequestParam("array[]") Integer[] array){
        for (Integer num:array) {
            logger.info("num = "+num);
        }
        return "success";
    }

    @ResponseBody
    @RequestMapping("/send/array/zero.html")
    public String testReceiveArrayZero(Integer num1,Integer num2,Integer num3){
            logger.info("num1 = "+num1);
            logger.info("num2 = "+num2);
            logger.info("num3 = "+num3);
        return "success";
    }

}
