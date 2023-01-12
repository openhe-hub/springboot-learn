package com.example.demo.controller;

import com.example.demo.bean.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    Car car;

    @RequestMapping("hello")
    public String handle() {
        return "Hello, SpringBoot 2.7!"+"你好";
    }

    @RequestMapping("car")
    public Car car(){
        return car;
    }
}
