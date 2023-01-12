package com.example.demo.config;

import com.example.demo.bean.Car;
import com.example.demo.bean.Pet;
import com.example.demo.bean.User;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 1、配置类里面使用@Bean标注在方法上给容器注册组件，默认也是单实例的
 * 2、配置类本身也是组件
 * 3、proxyBeanMethods：代理bean的方法
 *      Full(proxyBeanMethods = true)、【保证每个@Bean方法被调用多少次返回的组件都是单实例的】
 *      Lite(proxyBeanMethods = false)【每个@Bean方法被调用多少次返回的组件都是新创建的】
 *      组件依赖必须使用Full模式默认。其他默认是否Lite模式
 */
@Configuration(proxyBeanMethods = true)
@Import({User.class, Pet.class})
// open configure property & import car
@EnableConfigurationProperties(Car.class)
public class MyConfig {
    @ConditionalOnBean(name = "tom")
    @Bean
    public User user1(){
        User user=new User(18,"zhangsan",tomcat1());
        return user;
    }

//    @Bean("tom")
    public Pet tomcat1(){
        return new Pet("tomcat");
    }
}
