package com.example.web.controller;

import com.example.web.bean.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;

@RestController
public class WebController1 {
    @GetMapping("/car/{id}/{username}")
    public Map<String, Object> getCar(@PathVariable("id") Integer id,
                                      @PathVariable("username") String username,
                                      @PathVariable Map<String, String> paramsMap,
                                      @RequestHeader("User-Agent") String userAgent,
                                      @RequestHeader Map<String, String> headerMap) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("username", username);
        map.put("map", paramsMap);
        map.put("user-agent", userAgent);
        map.put("header", headerMap);
        return map;
    }

    @GetMapping("/user")
    public Map<String, Object> getUser(@RequestParam("id") Integer id,
                                       @RequestParam("username") String username,
                                       @RequestParam Map<String, String> paramsMap) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("username", username);
        map.put("params", paramsMap);
        return map;
    }

    @GetMapping("/cookie")
    public Map<String, Object> getCookie(@CookieValue("_ga") String _ga,
                                         @CookieValue("_ga") Cookie cookie) {
        Map<String, Object> map = new HashMap<>();
        map.put("_ga", _ga);
        System.out.println(cookie.getName() + cookie.getValue());
        return map;
    }

    @PostMapping("/user")
    public Map<String, Object> postUser(@RequestBody User user) {
        Map<String, Object> map = new HashMap<>();
        System.out.println(user.toString());
        map.put("user", user);
        return map;
    }
}
