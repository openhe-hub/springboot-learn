package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// @RestController = @Controller + @ResponseBody
@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String handle(){
        return "Hello, SpringBoot3 !";
    }
}
