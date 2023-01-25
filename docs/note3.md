# SpringBoot Note 3：Springboot中的SpringMVC与web开发
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
6. 后台管理登录demo：Srpingboot+SpringMVC+thymeleaf
   ```java
    package com.example.admin.controller;

    import com.example.admin.bean.User;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.util.StreamUtils;
    import org.springframework.util.StringUtils;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PostMapping;

    import javax.servlet.http.HttpSession;

    @Controller
    public class IndexController {
        @GetMapping(value = {"/", "/login"})
        public String loginPage() {
            return "login";
        }

        @PostMapping("/login")
        public String login(User user, HttpSession session, Model model) {
            // redirection avoid form submit again
            if (StringUtils.hasLength(user.getUserName()) &&
                    user.getPassword().equals("123456")) {
                session.setAttribute("loginUser", user);
                return "redirect:/main.html";
            } else {
                model.addAttribute("msg", "username or password error.");
                return "login";
            }
        }

        @GetMapping("/main.html")
        public String mainPage(HttpSession session, Model model) {
            Object user = session.getAttribute("loginUser");
            if (user != null) {
                return "main";
            } else {
                model.addAttribute("msg", "please login first");
                return "login";
            }
        }
    }
   ``` 
   * SpringMVC中使用model在前后端传数据
   * SpringMVC中每个controller方法都可以获取session,request,response对象，用法与servlet中相同，demo使用session检查登录状态
   * demo使用thymeleaf模板语言，实际使用前后端分离的项目较多
   * 使用重定向解决表单重复提交的问题
7. 拦截器
   1. 继承`HandlerInterceptor`定义拦截器
      ```java
      public class LoginInterceptor implements HandlerInterceptor {
      @Override
      public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
          HttpSession session = request.getSession();
          Object loginUser = session.getAttribute("loginUser");
          if (loginUser != null) {
              return true;
          } else{
              session.setAttribute("msg","please login first");
              response.sendRedirect("/");
              return false;
          }
      }

      @Override
      public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
          HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
      }

      @Override
      public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
          HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
      }
      }
      ``` 
   2. 添加一个配置类`@Configuration`，继承`WebMvcConfigurer`  
      ```java
      @Configuration
      public class WebConfig implements WebMvcConfigurer {
          @Override
          public void addInterceptors(InterceptorRegistry registry) {
              registry.addInterceptor(new LoginInterceptor())
                      .addPathPatterns("/**")
                      .excludePathPatterns("/", "/login","/css/**","/fonts/**","/images/**","/js/**");
          }
      }
      ``` 
   3. 在`addInterceptors`方法中，定义拦截和放行路径，注意需要放行静态资源
8. 文件上传：使用SpringMVC封装好的`MultipartFile`
   ```java
    @Data
    public class ImgBean {
        String email;
        String username;
        MultipartFile headerImg;
        MultipartFile[] photos;
    }
   ```
   ```java
    @PostMapping("/upload")
    public String upload(ImgBean imgBean) throws IOException {
        MultipartFile headerImg = imgBean.getHeaderImg();
        MultipartFile[] photos = imgBean.getPhotos();
        // save single file
        if (!headerImg.isEmpty()) {
            headerImg.transferTo(new File("D:\\" + headerImg.getOriginalFilename()));
        }
        for (MultipartFile photo : photos) {
            if (!photo.isEmpty()) {
                photo.transferTo(new File("D:\\group\\" + photo.getOriginalFilename()));
            }
        }
        return "main";
    }
   ``` 
   * 配置单个文件和单次请求大小限制
   ```yaml
    spring.servlet.multipart.max-file-size=10MB
    spring.servlet.multipart.max-request-size=100MB
   ``` 
9.  错误处理 
    1.  默认错误处理
        1.  对于机器端，响应报错的json数据
        2.  对于浏览器端，响应一个whitelabel错误视图，在templates/error/下自定义`4xx.html`，`5xx.html`可以对应4**,5**的http报错代码
    2.  自定义：实现`ErrorController`