# SpringBoot Note 1

1. SpringBoot 优点
    1. 创建独立spring应用
    2. 内嵌web服务器，直接启动
    3. starter依赖，简化配置
    4. 无代码生成，无需编写xml
    5. 自动配置spring及一些第三方应用
    6. 生产级别应用监控
2. HelloWorld
    1. 通过spring initializer初始化spring boot项目，勾选spring web
        * spring boot 2.7 => java 1.8
        * spring boot 3 => java 17
    2. 添加主应用类  
        ```java
        package com.example.demo;

        import org.springframework.boot.SpringApplication;
        import org.springframework.boot.autoconfigure.SpringBootApplication;
        
        @SpringBootApplication
        public class DemoSpring2Application {
                public static void main(String[] args) {
                    SpringApplication.run(DemoSpring2Application.class, args);
                }
        }
        ```
    3. 添加一个controller类处理`\hello`
        ```java
        package com.example.demo.controller;

        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RestController;

        @RestController
        public class HelloController {
            @RequestMapping("hello")
            public String handle() {
                return "Hello, SpringBoot 2.7!";
            }
        }
        ```  
3. 依赖管理
   1. 父项目中约定了几乎所有常用的依赖版本号，避免了手动引入造成的冲突
        ```xml
        <parent>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-parent</artifactId>
            <version>2.7.7</version>
            <relativePath/> <!-- lookup parent from repository -->
        </parent>
        ```
   2. starter启动器
   3. 修改依赖的版本号（以mysql驱动为例子）
      * 默认是8.0+ 
        ```xml
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        ```
      * 更改版本
        ```xml
        <properties>
            <mysql.version>5.1.43</mysql.version>
        </properties>
        ```
4. 自动配置
   1. 自动配置依赖：如tomcat，springMVC
   2. 自动配置web常用功能：如字符串编码问题
   3. 扫描默认包结构：主应用所在包及其子包
   4. 各种默认配置值，在`application.properties`下更改
   5. 按需加载starter
5. 容器注解
   1. @Configuration
      spring的注解配置bean
        1. 配置类里面使用@Bean标注在方法上给容器注册组件，默认也是单实例的
        2. 配置类本身也是组件
        3. proxyBeanMethods：代理bean的方法
            Full(proxyBeanMethods = true)、【保证每个@Bean方法被调用多少次返回的组件都是单实例的】
            Lite(proxyBeanMethods = false)【每个@Bean方法被调用多少次返回的组件都是新创建的】
            组件依赖必须使用Full模式默认。其他默认是否Lite模式
   2. @Bean/@Component/@Controller/@Service/@Repository
   3. @Import: 注解注入bean
   4. @Conditional：条件注入
   5. @ImportResource：引入bean.xml配置
   6. @ConfigurationProperties配置绑定
      * 从核心配置文件初始化bean
      * Car.java
      ```java
        package com.example.demo.bean;

        import lombok.Data;
        import org.springframework.boot.context.properties.ConfigurationProperties;

        @Data
        @ConfigurationProperties(prefix = "mycar")
        public class Car {
            private String brand;
            private Integer price;
        }
      ```
      * application.properties
      ```properties
      mycar.brand=byd
      mycar.price=100000
      ```
      * MyConfig.java加上
      ```java
      @EnableConfigurationProperties(Car.class)
      ```
   7. @AutoWired 自动注入