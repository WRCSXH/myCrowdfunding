package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.AdminExample;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.util.CrowdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AdminServiceImpl implements AdminService {

    // 装配BCryptPasswordEncoder
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AdminMapper adminMapper;

    private Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);
    @Override
    public void save(Admin admin) {
        // 密码加密
        String userPswd = admin.getUserPswd();
        // 使用BCryptPasswordEncoder 在保存Admin 时加密
        userPswd = passwordEncoder.encode(userPswd);
        // userPswd = CrowdUtil.md5(userPswd);
        admin.setUserPswd(userPswd);
        // 生成创建时间
        admin.setCreateTime(CrowdUtil.getSysTime());
        // 插入admin
        try{
            adminMapper.insert(admin);
        } catch (Exception e){
            logger.info("异常全类名："+e.getClass().getName());
            // 账号重复
            if (e instanceof DuplicateKeyException){
                throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
            // 其它问题
            throw e;
        }
    }

    @Override
    public List<Admin> getAll() {
        List<Admin> adminList = adminMapper.selectByExample(new AdminExample());
        return adminList;
    }

    @Override
    public Admin getAdminByLoginAcct(String loginAcct, String userPswd) {
        // 1、根据账号查询admin对象

        // 这一段是mybatis的内容，有时间再补吧
        // 1）创建AdminExample对象
        AdminExample adminExample = new AdminExample();
        // 2）创建Criteria对象
        AdminExample.Criteria criteria = adminExample.createCriteria();
        // 3）在Criteria对象中封装查询条件
        criteria.andLoginAcctEqualTo(loginAcct);

        // 4）调用AdminMapper的方法执行查询
        List<Admin> adminList = adminMapper.selectByExample(adminExample);
        // 2、如果adminList为空
        if (adminList == null||adminList.size() == 0){
            // 抛出登录失败异常
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED_ACCT_NOT_EXIST);
        }
        // 如果admin重复
        if (adminList.size() > 1){
            // 抛出一个运行时异常，表示账号不唯一
            throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
        }
        // 从adminList中取出admin
        Admin admin = adminList.get(0);
        // 如果admin为null
        if (admin == null){
            // 抛出登录失败异常
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED_ACCT_NOT_EXIST);
        }
        // 3、如果admin不为null，把数据库密码和表单密码进行比较
        // 1）使用MD5将表单密码的明文加密成密文
        String userPswdForm = CrowdUtil.md5(userPswd);
        // 2）从admin中得到数据库密码
        String userPswdDB = admin.getUserPswd();
        // 3）比较密码是否一致
        if (!userPswdForm.equals(userPswdDB)){
            // 密码不一致，抛出异常
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED_ACCT_PASSWORD_ERROR);
        }
        // 密码一致，返回admin
        return admin;
    }

    @Override
    public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {
        // 调用PageHelper的静态方法开启分页功能
        // 这里充分体现了PageHelper的非入侵式设计，原本的查询代码不需要修改
        PageHelper.startPage(pageNum, pageSize);
        // 执行查询
        List<Admin> adminList = adminMapper.selectByKeyword(keyword);
        // 将查询结果封装到PageInfo对象中
        PageInfo<Admin> pageInfo = new PageInfo<>(adminList);
        return pageInfo;
    }

    @Override
    public void remove(Integer adminId) {
        adminMapper.deleteByPrimaryKey(adminId);
    }

    @Override
    public Admin getAdminByAdminId(Integer adminId) {
        return adminMapper.selectByPrimaryKey(adminId);
    }

    @Override
    public void update(Admin admin) {
        // 有选择的更新，null值字段不更新
        adminMapper.updateByPrimaryKeySelective(admin);
    }

    @Override
    public void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList) {
        // 先根据adminId删除关联关系表中的旧数据
        adminMapper.deleteOldRelationship(adminId);
        // 再根据adminId和roleIdList添加关联关系表中的新数据
        if (roleIdList != null && roleIdList.size() > 0){
            adminMapper.insertNewRelationship(adminId,roleIdList);
        }
    }

    @Override
    public Admin getAdminByLoginAcct(String username) {
        AdminExample example = new AdminExample();
        AdminExample.Criteria criteria = example.createCriteria();
        criteria.andLoginAcctEqualTo(username);
        List<Admin> list = adminMapper.selectByExample(example);
        Admin admin = list.get(0);
        return admin;
    }
}
