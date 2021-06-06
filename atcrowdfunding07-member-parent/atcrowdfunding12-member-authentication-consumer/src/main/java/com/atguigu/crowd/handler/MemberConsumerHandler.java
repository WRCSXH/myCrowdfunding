package com.atguigu.crowd.handler;

import com.atguigu.crowd.api.MysqlRemoteService;
import com.atguigu.crowd.api.RedisRemoteService;
import com.atguigu.crowd.config.ShortMessageProperties;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import com.atguigu.crowd.entity.vo.MemberLoginVO;
import com.atguigu.crowd.entity.vo.MemberVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Controller
public class MemberConsumerHandler {

    @Autowired
    private ShortMessageProperties shortMessageProperties;

    @Autowired
    private RedisRemoteService redisRemoteService;

    @Autowired
    private MysqlRemoteService mysqlRemoteService;

    @RequestMapping("/auth/member/logout")
    public String logout(HttpSession session){
        // 销毁 session 中的用户信息
        session.invalidate();
        return "redirect:http://localhost/";
    }

    @RequestMapping("/auth/member/login")
    public String login(@RequestParam("loginacct") String loginacct,
                        @RequestParam("userpswd") String userpswd,
                        ModelMap modelMap,
                        HttpSession session
                        ){
        // 从 db 中查找账号
        ResultEntity<MemberPO> memberPOByLoginAcctRemote = mysqlRemoteService.getMemberPOByLoginAcctRemote(loginacct);
        // 判断查找动作是否成功
        if(ResultEntity.FAILED.equals(memberPOByLoginAcctRemote.getResult())){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,memberPOByLoginAcctRemote.getMessage());
            return "member-login";
        }
        // 判断账号是否存在
        if(memberPOByLoginAcctRemote.getData() == null){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_LOGIN_FAILED_ACCT_NOT_EXIST);
            return "member-login";
        }
        // 判断密码是否正确
        MemberPO memberPO = memberPOByLoginAcctRemote.getData();
        String userpswdDB = memberPO.getUserpswd();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean matches = passwordEncoder.matches(userpswd, userpswdDB);
        if(!matches){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_LOGIN_FAILED_ACCT_PASSWORD_ERROR);
            return "member-login";
        }
        // 将用户信息存入 session
        MemberLoginVO memberLoginVO = new MemberLoginVO(memberPO.getId(),memberPO.getUsername(),memberPO.getEmail());
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER,memberLoginVO);
        /*
        描述问题
            http://localhost:4000
            http://localhost:80
            是两个不同网站，浏览器工作时不会使用相同的Cookie
        解决问题
            以后重定向的地址都按照通过 Zuul 访问的方式写地址。
        redirect:http://localhost//auth/member/to/center/page
         */
        return "redirect:http://localhost/auth/member/to/center/page.html";
    }

    @RequestMapping("/auth/member/register")
    public String register(MemberVO memberVO, ModelMap modelMap){
        // 获取表单验证码和 redis 验证码
        String formCode = memberVO.getCode();
        String redisCodeKey = CrowdConstant.REDIS_CODE_PREFIX + memberVO.getMobile();
        ResultEntity<String> redisStringValue = redisRemoteService.getRedisStringValueByKeyRemote(redisCodeKey);
        if(ResultEntity.FAILED.equals(redisStringValue.getResult())){
            // 查询操作无效
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,redisStringValue.getMessage());
            return "member-register";
        }
        String redisCode = redisStringValue.getData();
        if(redisCode == null){
            // 获取 redis 验证码失败
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_REGISTER_CODE_IS_NULL);
            return "member-register";
        }
        // 判断表单验证码和redis验证码是否一致
        if(!Objects.equals(formCode,redisCode)){
            // 不一致
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_REGISTER_CODE_IS_INCORRECT);
            return "member-register";
        }
        // 一致
        // 删除redis验证码，如果删除失败就要记录日志，再手动删除
        // redisRemoteService.removeRedisKeyRemote(redisCodeKey);
        // 加密密码
        String userpswdBeforeEncode = memberVO.getUserpswd();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String userPswdAfterEncode = bCryptPasswordEncoder.encode(userpswdBeforeEncode);
        memberVO.setUserpswd(userPswdAfterEncode);
        // 创建一个 MemberPO
        MemberPO memberPO = new MemberPO();
        // 将 MemberVO 的属性复制给 MemberPO
        BeanUtils.copyProperties(memberVO,memberPO);
        System.out.println(memberPO);
        // 保存用户信息
        ResultEntity<String> saveMemberPO = mysqlRemoteService.saveMemberPORemote(memberPO);
        // 判断保存用户信息是否成功
        if(ResultEntity.FAILED.equals(saveMemberPO.getResult())){
            // 保存失败返回提示消息
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,saveMemberPO.getMessage());
            return "member-register";
        }

        // 避免注册成功之后刷新再执行注册操作，重定向到登录页面
        return "redirect:http://localhost/auth/member/to/login/page.html";
    }

    @ResponseBody
    @RequestMapping("/auth/member/get/code")
    public ResultEntity<String> getCode(@RequestParam("mobile") String mobile){
        // 生成验证码
        ResultEntity<String> getCodeResultEntity = CrowdUtil.sendShortMessage(
                shortMessageProperties.getHost(),
                shortMessageProperties.getPath(),
                shortMessageProperties.getMethod(),
                shortMessageProperties.getAppCode(),
                mobile,
                shortMessageProperties.getSmsSignId(),
                shortMessageProperties.getTemplateId()
        );
        // 判断验证码是否生成成功
        if(ResultEntity.SUCCESS.equals(getCodeResultEntity.getResult())){
            // 成功
            // 从上一步中得到验证码
            String code = getCodeResultEntity.getData();
            // 设置生成的验证码在 redis 中的 key 名
            String codeKey = CrowdConstant.REDIS_CODE_PREFIX + mobile;
            // 将验证码存入 redis
            ResultEntity<String> saveCodeResultEntity = redisRemoteService.setRedisKeyValueRemoteWithTimeout(codeKey, code, 15, TimeUnit.MINUTES);
            // 判断验证码是否保存成功
            if(ResultEntity.SUCCESS.equals(saveCodeResultEntity.getResult())){
                // 成功
                return ResultEntity.successWithoutData();
            } else {
                // 失败，返回保存验证码失败的错误原因
                return saveCodeResultEntity;
            }
        }
        // 失败，返回获取验证码失败的错误原因
        return getCodeResultEntity;
    }
}
