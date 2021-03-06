## 阅读[《SpringMVC 学习指南》](http://152.136.139.89/book/SpringMVC.pdf)

*2020-07-09 Dyinfalse*

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

XML
``` xml
<beans xmlns="...">
	<bean name="product" class="com.app.beans.Product" />
</beans>
```

java
``` java
ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
Student student = (Student) context.getBean("student3", Student.class);
```

- 实例化XML时的钩子函数，Factory和 Destroy
> factory-method: 指定一个工厂类，生成类的时候做一定的加工，有静态，动态的区别
> destroy-method: 实例被销毁之前调用这个方法

- 实例化时传递参数

``` xml
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

本书介绍了一个基于MVC模型2架构的产品表单的例子，其中包含

> 一个Product类，包含产品相关字段
>
> 一个ProductForm类，封装HTML表单字段 因为要做成业务与代码的区分,虽然ProductForm和Product字段差不多,但是要区分开
>
> 一个ControllerServlet类，作为控制器
>
> 一个SaveProductAction类，作为action类
>
> 两个JSP文件作为view，存放在WEB-INF文件夹内，外部无法直接访问。
>
> 后续扩展ControllerServlet解耦, 路径分发(DispatcherServlet)以及两个针对action的Controll, InputProductController(表单输入)和SaveProductController(表单提交)
>
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

``` xml
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

> SpringMVC 2.5 之前开发一个Controller的方式是实现`org.springframework.web.servlet.mvc.Controller`接口, 并且重写`handleRequest`方法, 下面是该方法的签名

``` java

ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)

```
- 一个Controller例子

[可以查看](https://github.com/Dyinfalse/JavaLearn/blob/master/comservletweb/src/main/java/com/servlet/web/controller/Home.java)

- View Resolver

使用XML可以配置在访问JSP文件的公共路径, 一般的配置方法如下

``` xml
<!-- springmvc-config.xml -->
<beans xmlns="..." xmlns:xsi="...">
	<bean id=""viewResolver class="org.springframework.web.servlet.view.InternalResourceViewResolver">
		<property name="prefix" value="WEB-INF/jsp/" />
		<property name="suffix" value=".jsp">
	</bean>
</beans>
```

上述配置表示当在 Controller 中的 hadnleRequest 方法返回 ModelAndView 的时候,指定JSP文件的时候,可以省去前缀路径和文件后缀,写法如下

``` java
import org.springframework.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
// some import

class Home implements Controller {
	public ModelAndView handleRequest (HttpRequestServlet request, HttpResponseServlet response){
		// some th ...
		return new ModelAndView("home"); // -> 代替 return new ModelAndView("WEB-INF/jsp/home.jsp");
	}
}
```

---
### 第四章 基于注解的控制器

- SpringMVC的注解优点

> 一个控制器可以处理多个请求, 这就允许了将多个相关动作写在一个控制器内部, 减少了类的数量

> 请求映射不在需要专门的配置文件, 使用`RequestMapping`注解可以对一个方法进行请求处理

- 配置注解

> 需要在springmvc的配置文件中增加`<compontent-scan />`元素, 还需要修改`beans`元素的属性,如下

``` xml
<!-- beans 需要增加一个xmlns:context属性, 链接是固定的 -->
<beans
	...
	xmlns:context="http://www.springframework.org/schema/context"
>
	<!-- 配置你的controller类包位置 -->
	<context:compontent-scan base-package="com.example.controller">
	<!-- 同时你也可以指定service -->
	<context:compontent-scan base-package="com.example.service">
</beans>
```
这些配置告诉spring, 我需要在`com.example.controller`和`com.example.service`这两个包下使用注解

- 使用Controller注解

下面是一个例子

``` java
package com.example.controller;

import org.springframework.stereotype.Controller;

@Controller
public class CustomerController {
	
}
```

- RequestMapping 注解类型

> 按照之前的例子, 我们有了controller之后还需要为这个controller的每个动作开发一个处理响应的方法,需要告诉spring方法和动作之间的映射(Mapping), `@RequestMapping`注解就是用来干这个的
>
> 一个采用`@RequestMapping`注解的方法将成为一个请求处理方法, 并由调度程序在接受到对应的URL请求时调用

我们给CustromerController增加一个响应方法

``` java
package com.example.controller;

import org.springframework.stereotype.Controller;

@Controller
public class CustomerController {
	
	@RequestMapping(value="/custromer_input")
	public String custromerInput () {
		// do some th
		return "custromerInputForm";
	}
}
```

> 使用RequestMapping注解的value属性将URI映射到方法, 在上面的例子中, 我门将`customer_input`映射到`custromerInput`方法, 我们可以使用下面这个URL访问到custromerInput方法
>
> http://localhost:8080/custromer_input

- RequestMapping设置请求方法

> 除了value属性以外, 我们还可以传递一个method属性, 用来指定什么样的HTTP方法可以被映射到当前的响应方法









- jsp提交表单，如果要使用jsp提供的表单组件，需要在jsp文件开头加上`<%@ taglib prefix="from" uri="http://www.springframework.org/tags/form" %>`，组件属性详细见5.2(p69-p78)
 1. 主要相关的属性:
 2. `commandName` form标签的属性，用来指明该表单映射的对象(之前在Controller里使用`ModelAndView.addObject()`方法传入jsp的对象)
 3. `path` 单个表单标签的属性，指明当前标签对应的是commandName对象上的哪一个字段。
