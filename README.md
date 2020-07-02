# 阅读[《SpringMVC 学习指南》](http://152.136.139.89/book/SpringMVC.pdf)写的一些练习例子

## 目录

### 第一章 Spring 框架

- 什么是Spring？
> 是一个开源的企业级开发框架

- 什么是SpringMVC？
> Spring的一个子框架

- 什么是控制反转（IoC）？
> 将创建类实例的控制权移交给Spring容器，叫做控制反转

- (1.2)如何使用Spring的控制反转？
> Spring提供两种方式配置Spring控制反转：XML文件和注解，还需要一个ApplicationContext对象作为控制反转容器，ApplicationContext接口有两个实现 ClassPathXmlApplicationContext和FileSystemXmlApplicationContext，本书主要使用的是ClassPathXmlApplicationContext

- 使用XML配置Bean 用来实例化类
> 

- 使用 Spring 提供的 ClassPathXmlApplicationContext 类实现控制反转

```xml
<beans xmlns="...">
	<bean name="product" class="com.app.beans.Product" />
</beans>
```

``` java
ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
Student student = (Student) context.getBean("student3", Student.class);
```

- 实例化XML时的钩子函数，Factory和 Destroy
> factory-method: 指定一个工厂类，生成类的时候做一定的加工，有静态，动态的区别
> destroy-method: 实例被销毁之前调用这个方法

- 实例化时传递参数

```xml
<beans xmlns="...">
	<bean name="product" class="com.app.beans.Product">
		<constructor-arg name="name"  value="Bob"/>
		<constructor-arg name="age"  value="18"/>
		...
	</bean>
</beans>
```

- XML配置类的依赖关系
> Spring提供两种配置依赖关系的元素
> 使用`<property ref="目标类" />`指定setter来设置值，前提是接受依赖的类必须有一个setter方法
> 使用`<constructor-arg ref="目标类" />`调用构造方法设置值，在构造方法中需要接受并赋值给指定字段

[例子](https://github.com/Dyinfalse/JavaLean/tree/master/springIocDemo)

---
### 第二章 模型2和SpringMVC模式

- 模型1和模型2

> 模型1是页面中心，简单的jsp应用，路由大多由文件系统处理，因此修改一个jsp文件名称，需要修改其他页面的链接，所以维护起来比较麻烦
> 模型2是居于模型-视图-控制器（MVC）的模式，几乎所有现代web框架都是模型2的实现，在SpringMVC中一般使用`POJO(Plain Old Java Object)`作为模型，在实践中会使用一个JavaBean，配合Action支持持久化，`Servlet`或者`Filter`来作为控制器，`JSP`作为视图，一个提供了Action的对象称为action对象，一个action对象可支持多个action（一个action可以理解为一个动作，一个响应方式，根据请求地址，访问不同的action，做出不同的响应）

- 模型2之Servlet控制器

> 本书介绍了一个基于MVC模型2架构的产品表单的例子，其中包含
> - 一个Product类，包含产品相关字段
> - 一个ProductForm类，封装HTML表单字段 因为要做成业务与代码的区分,虽然ProductForm和Product字段差不多,但是要区分开
> - 一个ControllerServlet类，作为控制器
> - 一个SaveProductAction类，作为action类
> - 两个JSP文件作为view，存放在WEB-INF文件夹内，外部无法直接访问。
> 后续扩展ControllerServlet解耦, 路径分发(DispatcherServlet)以及两个针对action的Controll, InputProductController(表单输入)和SaveProductController(表单提交)
> 增加一个ProductValidator作为表单验证类, 返回一个ArrayList,存贮ProductFrom类每个字段的验证结果

需要强调, ControllerServlet继承自HttpServlet类, 通过`HttpServletRequest`和`HttpServletResponse`来描述映射(uri与action), 并且作出返回(P17), 这个例子只到了Controller层, 实际应用场景中还需要若干Service类用来处理后端的复杂逻辑, 需要DAO类来访问数据库

---
### 第三章 SpringMVC模式

- SpringMVC的好处


对比了MVC模式2和SpringMVC在Dispatcher Servlet的区别, 以下是使用SpringMVC的好处

> SpringMVC提供一个Dispatcher Servlet, 无需开发
>
> SpringMVC基于XML配置文件, 可以编辑, 不需要重新编译
>
> SpringMVC实例化控制器, 并根据用户输入构造bean
>
> SpringMVC自动绑定用户输入, 并正确的转换数据类型
>
> SpringMVC可以校验用户输入, 支持编程方式声明, 并且内置常用校验
>
> SpringMVC基于Spring, 可以使用其他Spring的功能
>
> SpringMVC支持国际化和本地化, 根据用户所在区域显示不同语言
>
> SpringMVC支持多种视图技术, 常用的JSP, Velocity和 FreeMarker

- SpringMVC的DispatcherServlet

> 要使用SpringMVC提供的DispatcherServlet, 需要一个`srevletName-servlet.xml`作为配置, 其中可以使用如下代码引入配置

```xml
	<servlet>
		...
		<init-param>
			<param-name>contextConfigLocation</param-name>
			<param-value>WEB-INF/config/simple-config.xml</param-value>
		</init-param>

		<!-- 下面的元素表示在程序启动时, 装载servlet -->
		<load-on-startup>1</load-on-startup>
	</servlet>
```

> `<load-on-startup>1</load-on-startup>`下面的元素表示在程序启动时, 装载servlet, 并且调用init方法, 如果不存在, 则在该servlet的第一个请求是装载

- Controller 接口




---
### 第五章

- jsp提交表单，如果要使用jsp提供的表单组件，需要在jsp文件开头加上`<%@ taglib prefix="from" uri="http://www.springframework.org/tags/form" %>`，组件属性详细见5.2(p69-p78)
 1. 主要相关的属性:
 2. `commandName` form标签的属性，用来指明该表单映射的对象(之前在Controller里使用`ModelAndView.addObject()`方法传入jsp的对象)
 3. `path` 单个表单标签的属性，指明当前标签对应的是commandName对象上的哪一个字段
