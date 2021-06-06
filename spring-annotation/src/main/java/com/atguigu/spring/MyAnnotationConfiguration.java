package com.atguigu.spring;

import com.atguigu.spring.entity.Employee;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * Spring基于注解开发
 */

// @Configuration表示当前类是一个配置类，相当于spring-context.xml这样的配置文件
@Configuration
public class MyAnnotationConfiguration {
    // @Bean注解相当于做了下面xml文件的配置，把方法的返回值放入ioc容器
    // <bean id="employee" class="com.atguigu.spring.entity.Employee"></bean>
    @Bean
    public Employee getEmployee() {
        return new Employee();
    }
}
