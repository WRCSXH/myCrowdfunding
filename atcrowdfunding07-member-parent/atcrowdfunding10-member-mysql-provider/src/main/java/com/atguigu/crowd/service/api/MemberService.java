package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.po.MemberPO;
import org.springframework.web.bind.annotation.RequestParam;

public interface MemberService {
    MemberPO getMemberPOByLoginAcct(@RequestParam("loginacct") String loginacct);
    void saveMemberPO(MemberPO memberPO);
}
