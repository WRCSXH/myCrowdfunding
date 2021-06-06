package com.atguigu.spring;

import com.atguigu.spring.entity.Employee;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringTest {
    public static void main(String[] args) {
        // 以前使用new ClassPathXmlApplicationContext("")的方式加载xml配置文件
        // 现在基于注解配置类创建ioc容器
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MyAnnotationConfiguration.class);
        // 从ioc容器中取得bean
        Employee employee = applicationContext.getBean(Employee.class);
        System.out.println(employee);
    }
}
