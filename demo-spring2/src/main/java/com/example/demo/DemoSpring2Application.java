package com.example.demo;

import com.example.demo.bean.Pet;
import com.example.demo.bean.User;
import com.example.demo.config.MyConfig;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DemoSpring2Application {
    public static void main(String[] args) {
        // return IOC container
        ConfigurableApplicationContext context=SpringApplication.run(DemoSpring2Application.class,args);
        // get beans
        String[] names=context.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println(name);
        }
        // get beans from container
        Pet tomcat1=context.getBean("tom",Pet.class);
        Pet tomcat2=context.getBean("tom",Pet.class);
        System.out.println("check if equal 1="+(tomcat1==tomcat2));
        // configure class is also a bean
        MyConfig config=context.getBean(MyConfig.class);
        System.out.println(config);
        // the use of proxy bean method
        User user1=context.getBean("user1",User.class);
        Pet tom=context.getBean("tom",Pet.class);
        System.out.println("check if equal 2="+(user1.getPet()==tom));
    }
}
