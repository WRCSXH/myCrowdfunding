package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.mapper.MemberPOMapper;
import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.po.MemberPOExample;
import com.atguigu.crowd.service.api.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 在类上使用@Transactional(readOnly = true)针对查询操作设置事务属性
@Transactional(readOnly = true)
@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberPOMapper memberPOMapper;
    @Override
    public MemberPO getMemberPOByLoginAcct(String loginacct) {
        // 1.创建Example 对象
        MemberPOExample example = new MemberPOExample();
        // 2.创建Criteria 对象
        MemberPOExample.Criteria criteria = example.createCriteria();
        // 3.封装查询条件
        criteria.andLoginacctEqualTo(loginacct);
        // 4.执行查询
        List<MemberPO> list = memberPOMapper.selectByExample(example);
        // 5.获取结果
        if (list == null || list.size() == 0){
            return null;
        }
        return list.get(0);
    }

    // 针对这个保存操作设置事务属性，无论何种情况都创建新事务
    // 默认非只读，设置所有异常回滚
    @Transactional(readOnly = false,propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    @Override
    public void saveMemberPO(MemberPO memberPO) {
        // 有选择的插入，没有传的值自动补为 null
        memberPOMapper.insertSelective(memberPO);
    }
}
