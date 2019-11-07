# 阅读[《SpringMVC 学习指南》](http://152.136.139.89/center/java%E4%B9%A6/SpringMVC.pdf)写的一些练习例子

## 目录

### 第一章 Spring 框架

- 代码主要是使用 Spring 提供的 ClassPathXmlApplicationContext 类实现控制反转
- 使用XML配置Bean 用来实例化类
- 实例化XML时的钩子函数，Factory和 Destroy
- XMl之前的引用，属性引入，实例化参数引入

[例子](https://github.com/Dyinfalse/JavaLean/tree/master/springIocDemo)

---
### 第二章 至 第四章

- 主要介绍模式2，以及MVC概念
- 举了一个简单的产品表单例子
- DispatcherServlet 分发请求动作
- 一个SpringMVC应用配置方法
- 主要介绍了三种实现SpringMVC的方式以及配置方法
 1. 继承Controller类，使用doGet，doPost方法响应接口 *特点是：一个类的doGet或doPost方法响应一个请求（代码不包含该例子）*
 2. 实现Controller接口，以及handleRequest方法响应接口，*特点是：一个类的handRequest响应一个请求，在SpringMVC配置XML文件中用bean指定请求地址与类的对应关系*
 3. 使用@Controller注解和@RequestMapping注解实现，*特点是：一个类的每个带有@RequestMapping注解的方法响应一个请求，配置包路径，自动扫描注解*
- @Autowired 依赖注入（member例子）
- 配合@Autowired 的 @Service 注解
- 主要介绍了如何重定向，以及为什么要重定向
- 重定向如何传递参数
 1. 使用`RedirectAttributes.addFlashAttribute`增加一个Flash参数
 2. 在被定向的方法内，使用`RequestContextUtils.getInputFlashMap`从`HttpServletRequest`上获取增加的参数
- 请求时携带参数路径参数和请求体参数
 1. 路径参数 -> 首先在方法上配置`@RequestMapping(value = {"/getParams/{pathParam}"}` 然后在方法参数上使用`@PathVariable String pathParam`注入
 2. 请求体参数 -> 直接在方法参数上使用`@RequestParam(value = "httpParam", defaultValue = "default HttpParam", required = false) String httpParam` 进行配置，并注入到方法内

[例子](https://github.com/Dyinfalse/JavaLean/tree/master/comservletweb)

---
### 第五章

- jsp提交表单，如果要使用jsp提供的表单组件，需要在jsp文件开头加上`<%@ taglib prefix="from" uri="http://www.springframework.org/tags/form" %>`，组件属性详细见5.2(p69-p78)
 1. 主要相关的属性:
 2. `commandName` form标签的属性，用来指明该表单映射的对象(之前在Controller里使用`ModelAndView.addObject()`方法传入jsp的对象)
 3. `path` 单个表单标签的属性，指明当前标签对应的是commandName对象上的哪一个字段