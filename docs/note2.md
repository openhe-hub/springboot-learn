# SpringBoot Note 2：各类开发工具与yaml配置
1. dev-tools：热部署插件(严格意义上自动重启)
   依赖
   ```xml
    <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <optional>true</optional>
    </dependency>
   ```
   idea内ctrl+F9刷新
2. Spring Initializer：脚手架工具
3. yaml配置语言
   * 大小写敏感
   * 使用缩进表示层级关系
   * ‘#’表示注解
   * '',""表示字符串，会被转义/不转义
   * 数据类型
     * 字面量：date,boolean,string,number,null
        ```yaml
        k: v
        ```
     * 对象
       * 行内写法
       ```yaml
       k: {k1: v1,k2: v2}
       ``` 
       * 多行写法
       ```yaml
       k: 
           k1: v1
           k2: v2
       ``` 
     * 数组
        * 行内写法
         ```yaml
         k: [v1,v2,v3]
         ``` 
         * 多行写法
         ```yaml
         k: 
            - v1
            - v2
         ``` 
4. 在SpringBoot中使用yaml作为配置（推荐）
   * 在resources/下新建application.yml，与application.properties同时生效
   * Person.java
   ```java
    package com.example.demo.bean;

    import lombok.Data;
    import org.springframework.boot.context.properties.ConfigurationProperties;
    import org.springframework.stereotype.Component;

    import java.util.Date;
    import java.util.List;
    import java.util.Map;
    import java.util.Set;

    @Data
    @Component
    @ConfigurationProperties(prefix = "person")
    public class Person {
        private String userName;
        private Boolean boss;
        private Date birth;
        private Integer age;
        private Pet pet;
        private String[] interests;
        private List<String> animal;
        private Map<String, Object> score;
        private Set<Double> salarys;
        private Map<String, List<Pet>> allPets;
    }
   ``` 
   * Pet.java
   ```java
    package com.example.demo.bean;

    import lombok.Data;
    import org.springframework.boot.context.properties.ConfigurationProperties;
    import org.springframework.stereotype.Component;

    @Data
    @Component
    @ConfigurationProperties(prefix = "pet")
    public class Pet {
        private String name;
        private Double weight;
    }
   ``` 
   * TestController.java
   ```java
    package com.example.demo.controller;

    import com.example.demo.bean.Person;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;

    @RestController
    public class TestController {
        @Autowired
        Person person;

        @RequestMapping("/person")
        public Person getPerson(){
            return person;
        }
    }
   ``` 
   * application.yml
   ```yaml
    person:
    # 单引号转义，双引号不转义
    # 单引号将\n作为字符串输出，双引号不转义
    # someVar => some-var
    user-name: 'openhe\n'
    boss: true
    birth: 2004/04/08
    age: 18
    interests: [coding, music]
    animal:
        - cat
        - dog
    score: {math:100,English:80}
    salarys: [999,888]
    pet:
        name: tomcat
        weight: 10
    all-pets:
        sick:
        - {name: aaa, weight: 10}
        - {name: bbb, weight: 10}
        health:
        - {name: ccc, weight: 10}
        - {name: ddd, weight: 10}
   ``` 
5. Slf4j日志系统：Springboot已经集成，类上加注解@Slf4j即可自动注入log对象   