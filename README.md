# 阅读《SpringMVC 学习指南》写的一些练习例子

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
 1. 继承Controller类，使用doGet，doPost方法响应接口一个类的 *特点是*：doGet或doPost响应一个请求（代码不包含该例子）
 2. 实现Controller接口，以及handleRequest方法响应接口，*特点是*：一个类的handRequest响应一个请求，在SpringMVC配置XML文件中用bean指定请求地址与类的对应关系
 3. 使用@Controller注解和@RequestMapping注解实现，*特点是*：一个类的每个带有@RequestMapping注解的方法响应一个请求，配置包路径，自动扫描注解
- @Autowired 依赖注入（member例子）
- 配合@Autowired 的 @Service 注解

coming soon

[例子](https://github.com/Dyinfalse/JavaLean/tree/master/comservletweb)