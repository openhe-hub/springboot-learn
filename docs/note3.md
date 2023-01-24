# SpringBoot Note 3
1. SpringMVC 自动配置
    * 内容协商视图解析器和BeanName视图解析器
    * 静态资源（包括webjars）
    * 自动注册 Converter，GenericConverter，Formatter 
    * 支持 HttpMessageConverters （后来配合内容协商理解原理）
    * 自动注册 MessageCodesResolver （国际化用）
    * 静态index.html 页支持
    * 自定义 Favicon  
    * 自动使用 ConfigurableWebBindingInitializer ，（DataBinder负责将请求数据绑定到JavaBean上）
2. 静态资源
   1. 默认路径：`/static` or `/public` or `/resources` or `/META-INF/resources`
   2. 默认静态映射url：`/**`
   3. 同一url，先交给controller处理，再给静态资源处理器，最后响应404
   4. 配置静态映射：
      ```yaml
      spring:
        mvc:
          static-path-pattern: /res/**
      ```  
3. 欢迎页
   1. 在静态资源下加入`index.html`，访问直接跳转欢迎页
4. 自定义Favicon
   1. 在静态资源下加入`favicon.ico`
5. SpringMVC处理请求
   1. 获取路径变量:`@PathVariable`
      ```java
        @GetMapping("/car/{id}/{username}")
        public Map<String, Object> getCar(@PathVariable("id") Integer id,
                                        @PathVariable("username") String username,
                                        @PathVariable Map<String, String> paramsMap,
                                        @RequestHeader("User-Agent") String userAgent,
                                        @RequestHeaderMap<String, String> headerMap) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("username", username);
            map.put("map", paramsMap);
            map.put("user-agent", userAgent);
            map.put("header", headerMap);
            return map;
        }
      ``` 
   2. 获取请求头: `@RequestHeader`
   3. get请求与获取query string: `@GetMapping`,`@RequestParam`
      ```java
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
      ``` 
   4. 获取cookie: `@CookieValue`
      ```java
        @GetMapping("/cookie")
        public Map<String, Object> getCookie(@CookieValue("_ga") String _ga,
                                            @CookieValue("_ga") Cookie cookie) {
            Map<String, Object> map = new HashMap<>();
            map.put("_ga", _ga);
            System.out.println(cookie.getName() + cookie.getValue());
            return map;
        }
      ``` 
   5. post请求与获取请求体:`@PostMapping`,`@ResponseBody`
      ```java
        @PostMapping("/user")
        public Map<String, Object> postUser(@RequestBody User user) {
            Map<String, Object> map = new HashMap<>();
            System.out.println(user.toString());
            map.put("user", user);
            return map;
        }
      ``` 
      ```java
        package com.example.web.bean;

        import lombok.Data;

        @Data
        public class User {
            private String username;
            private Integer[] scores;
        }
      ```
      ```json
        {
            "username":"openhe",
            "scores":[1,2,3]
        }
      ```
      * 注意json的key和对象属性必须一一对应，若不对应，可以使用`@JsonAlias`或`@JsonProperty`进行配置
   6. 