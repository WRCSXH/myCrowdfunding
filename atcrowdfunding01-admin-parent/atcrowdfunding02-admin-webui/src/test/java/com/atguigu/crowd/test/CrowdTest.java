package com.atguigu.crowd.test;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.mapper.AdminMapper;

import com.atguigu.crowd.mapper.RoleMapper;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.util.CrowdUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.FrameworkServlet;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

// 指定Spring 给Junit 提供的运行器类
@RunWith(SpringJUnit4ClassRunner.class)
// 加载Spring 配置文件的注解
@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml","classpath:spring-persist-tx.xml"})
public class CrowdTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleMapper roleMapper;

    @Test
    public void testInsertRoles(){
        for (int i = 0; i < 235; i++) {
            roleMapper.insert(new Role(null,"role"+i));
        }
    }

    @Test
    public void textMd5(){
        String source = "123123"; // 4297F44B13955235245B2497399D7A93
        String encoded = CrowdUtil.md5(source);
        System.out.println(encoded); // 4297F44B13955235245B2497399D7A93
    }

    @Test
    public void testTx(){
        Admin admin = new Admin(null,"ww","123","王五","王五@qq.spring",null);
        adminService.save(admin);
    }
    @Test
    public void testLog(){
        // 1.获取Logger对象，这里传入的Class对象就是当前打印日志的类
        Logger logger = LoggerFactory.getLogger(CrowdTest.class);

        // 2.根据不同日志级别打印日志
        logger.debug("Hello I am Debug level!!!");
        logger.debug("Hello I am Debug level!!!");
        logger.debug("Hello I am Debug level!!!");

        logger.info("Info level!!!");
        logger.info("Info level!!!");
        logger.info("Info level!!!");

        logger.warn("Warn level!!!");
        logger.warn("Warn level!!!");
        logger.warn("Warn level!!!");

        logger.error("Error level!!!");
        logger.error("Error level!!!");
        logger.error("Error level!!!");
    }

    @Test
    public void testSaveAdmin(){
        Admin admin = new Admin(null,"tom","123123","汤姆","tom@qq.com",CrowdUtil.getSysTime());
        adminService.save(admin);
    }

    @Test
    public void testSaveAdmins(){
        for (int i=1;i<238;i++){
            Admin admin = new Admin(null,"loginAcct"+i,"userPswd"+i,"userName"+i,"email"+i,null);
            adminService.save(admin);
        }
    }
    @Test
    public void testInsertAdmin(){
        Admin admin = new Admin(null,"ls","123","李四","ls@qq.spring",null);
        int count = adminMapper.insert(admin);
        /*
            如果在实际开发中，所有想查看数值的地方都使用System.out的方式打印，会给项目上线运行带来问题
            System.out本质上是一个io操作，通常io操作是比较消耗性能的，如果项目中频繁地使用io操作，对性能影响会比较大，
            即使上线前专门花时间删除项目中的System.out代码，也很可能有遗漏，而且非常麻烦，
            而如果使用日志系统，那么通过日志的级别就可以批量地控制信息的打印。
         */
        // System.out.println("受影响的记录条数："+count);
        Logger logger = LoggerFactory.getLogger(CrowdTest.class);
        logger.info("受影响的记录条数："+count);
    }
    @Test
    public void testDataSource() throws SQLException {
        // 1.通过数据源对象获取数据源连接
        Connection connection = dataSource.getConnection();
        // 2.打印数据库连接
        System.out.println(connection);
    }
}
