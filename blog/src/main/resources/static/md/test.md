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
	<!-- 同时你也可以指定Service -->
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
import org.springframework.web.bind.annotation.RequestMapping;

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
> `http://localhost:8080/custromer_input`

- RequestMapping设置请求方法

> 除了value属性以外, 我们还可以传递一个method属性, 用来指定什么样的HTTP方法可以被映射到当前的响应方法

``` java
@RequestMapping(value="/custromer_input", method=RequestMethod.POST)
// 或者如果需要允许多个
@RequestMapping(value="/custromer_input", method={RequestMethod.POST, RequestMethod.PUT})
```

> 如果没有指定`method`那么将允许任意HTTP方法

- RequestMapping 在类上使用

我们改写一下之前的CustomerController

``` java
package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/customer")
public class CustomerController {
	
	@RequestMapping(value="/custromer_input", method=RequestMethod.POST)
	public String custromerInput () {
		
		// do some th

		return "custromerInputForm";
	}
}
```

> 由于控制器类的映射使用"/customer", 而customerInputForm方法映射是"/customer_input"
> 所以要访问这个方法, URL应该是这样
>
> `http://localhost:8080/customer/customer_input`

- 编写请求处理方法

每个请求方法都会有不同类型的参数和不同类型的返回结果, 如果在请求方法中需要HttpSession对象, 则可以添加一个HttpSession作为参数, spring会将对象正确的传递给方法

``` java

@RequestMapping("/uri")
public String CustomerInfo(HttpSession session){
	...
	session.addAttribute(key, value);
	...
}
```

- 可以在请求中出现的参数类型和返回类型
	- javax.servlet.ServletRequest or javax.servlet.http.HttpServletRequest
	- javax.servlet.ServletReponse or javax.servlet.http.HttpServletReponse
	- javax.servlet.http.HttpSession
	- org.springframework.web.context.request.WebRequest
	- org.springframework.web.context.request.NativeWebRequest
	- java.util.Locale
	- java.io.InputStream or java.io.Reader
	- java.io.OutputStream of java.io.Writer
	- java.security.Principal
	- HttpEntity<?>
	- java.util.Map / org.springframework.ui.Model
	- org.springframework.ui.ModelMap
	- org.springframework.web.servlet.mvc.support.RedirectAttribures
	- org,springframework.validation.Errors
	- org.springframework.validation.BindingResult
	- 命令或表单对象
	- org.springframework.web.bind.support.SessionStatus
	- org.springframework.web.util.UriComponentsBuilder
	- 带有@PathVariable, @MatrixVariable 注解对象
	- @RequestParam, @RequestHeader, @RequestBody, @RequestPart

其中特别重要的是org.springframework.ui.Model类型, 这不是一个Servlet API类型, 而是一个包含Map 的SpringMVC类型, 每次请求处理方法的时候, SpringMVC都会创建Model对象, 并将其Map注入到各种对象

- 可以在请求中返回的类型
	- ModelAndView
	- Model
	- Map包含模型的属性
	- View
	- 代表逻辑视图的String(JSP, HTML文件名称)
	- void
	- 提供对Servlet的访问, 以响应HTTP头部和内容HttpEntity或者 ResponseEntity对象
	- Callable
	- DeferredResult
	- 其他任意类型, Spring将其视为输出给View的对象模型

- 一个基于注解的应用

[可以查看](https://github.com/Dyinfalse/JavaLearn/blob/master/comservletweb/src/main/java/com/servlet/web/controller/Home.java)

- @Autowried 和 @Service

> 将依赖注入到SpringMVC容器里的最简单的办法是通过注解`@Autowrite`到字段或方法
>
> 为了能被作为依赖注入, 类必须要注明为`@Service`
>
> 还需要在配置文件中添加一个`<componte-scan/>`元素扫描基本依赖包(在@Controller的章节中有所提及)

*值得一提的是在开发service的时候会先创建一个接口(interface), 在类中注入的interface, @Autowried会自动为注入一个该接口的实现类, 如果有多个实现类, 就需要使用`@Resource(name="实现类名")`指定其中一个实现类, 这样做是为了方便依赖的扩展*

- 重定向和转发

> 转发比重定向快, 因为重定向需要经过客户端, 而转发没有, 但是如果需要重定向到外部网址, 无法使用转发

这是一个简单的重定向写法, 将这句代码加载响应方法的最后一行,就会被重定向到`/product_view/${productId}`页面

``` java
 return "redirect:/product_view/" + product.getId();
```

- Flash属性

重定向的一个不便地方在于无法轻松的给目标传值, 由于经过客户端所有Model中额一切都在重定向时丢失, 幸运的是, Spring 3.1中通过Flash属性提供了一种重定向传值的方法.

> 要使用Flash属性, 必须在Spring MVC配置文件中增加一个`<annotation-driven/>`元素, 然后在响应方法上添加一个参数类型`org.springframework.web.servlet.mvc.support.RedirectAttributes`

``` java

@RequestMapping("/product_save")
public String saveProduct (RedirectAttributes redirectAttributes){

	// ...
	// 给重定向增加参数
	redirectAttributes.addFlashAttribute("message", "This's a Flash attribute message");

	return "redirect:/product_view" + product.getId();
}

```

- 请求参数

> 请求参数是URL的一部分,用来携带数据发送给服务器, 类似这样`http://localhost:8080/product?productId=3`, 这是一个访问产品的URL, 包含请求参数`productId`, 它的值是3
>
> SpringMVC有一个简单的方式接受它, 使用`org.springframework.web.bind.annotation.RequestParam` 注解修饰参数

这段代码可以正确的接受到上面URL中携带来的productId参数

``` java

@RequestMapping("/product")
public String product (@RequestParam int productId) {

	return "product_view";
}
```

- 路径变量

> 与请求参数相似, 但是路径变量没有key的部分, 只是一个值, 类似这样`http://localhost:8080/product/3`, SpringMVC会尽量吧值转化为非String类型, 它的接受方式如下

``` java

@RequestMapping("/product/{id}")
public String product (@PathVariable Long id) {

	return "product";
}

```

- @ModelAttribute

> SpringMVC每次调用请求处理方法的时候, 都会穿件一个Model类型的实例, 如果要使用这个实例, 可以在请求方法上增加一个Model类型参数, 或者在方法中添加`@ModelAttribute`注解来访问Model实例

@ModelAttribute 可以用来注释方法或者方法参数, 当注释到参数时如下

``` java
@RequestMapping("/product")
public String product (@ModelAttribute("newOrder") Order order, Model model) {
	// 在这里使用Model时, Model会自动添加一个newOrder属性, 值就是order
	return "product"
}
```

如果没有指定newOrder, 那么会使用对象类型名称, 如下

``` java
@RequestMapping("/product")
public String product (@ModelAttribute Order order, Model model) {
	// 在这里使用Model时, Model会自动添加一个order属性, 值就是order
	return "product";
}
```

> 当@ModelAttribute注释到一个非请求处理方法时, 会在每次调用该控制器类的请求处理方法时被调用, 因此带有@ModelAttribute注解的方法会被频繁调用
> 
> SpringMVC在调用请求处理方法之前, 会调用带有@ModelAttribute注解的方法, 这个方法返回Model或者void类型, 如果返回一个对象, 则返回的对象会自动添加到请求处理方法的Model实例中

``` java
@ModelAttribute("productKey")
public Product addProduct(@RequestParam String productId) {
	return productService.get(productId);
}
```

> 有了这个方法, 那么在这个类中的所有请求处理方法的Model都会被提前添加一个product实例, key=productKey;
>
> 如果方法返回void, 那么方法必须接受一个Model类型参数, 并且自行添加属性到Model中 

``` java
@ModelAttribute
public void addProduct(@RequestParam String productId, Model model) {
	model.addAttribute("productKey", productService.get(productId))
}
```

---
### 第五章 数据绑定和表单标签库

- 数据绑定是什么?

> 数据绑定是将用户输入绑定到领域模型的一种特性

- 数据绑定有什么用?

> 有了数据绑定, 类型总是String的HTTP请求参数, 可以用于不同类型的对象属性, 数据绑定使得form bean(之前章节的ProductForm实例)变得多余

- Spring数据绑定的好处

> 不用创建form类, 也不用单独去处理类上的各种类型并且当输入验证失败的时候, 它会重新生成一个HTML, 手工编写的时候, 必须记住之前输入的值, 重新填充输入字段, 有了Spring数据绑定和表单标签库, 它们就会替你完成这些工作

- 表单标签库

> 表单标签库包含了可以用在JSP页面中渲染HTML元素的标签, 为了使用这些标签, 必须在JSP页面开头处声明这个taglib	 `<%taglib prefix="form" uri="http://www.springframework.org/tags/form" %>`

- 表单标签库的标签

| 标签	         | 描述                              |
|---------------|-----------------------------------|
| form          | 渲染一个表单元素                     |
| input         | 渲染一个<input type="text">元素     |
| password      | 渲染一个<input type="password">元素 |
| hidden        | 渲染一个<input type="hidden">元素   |
| textarea      | 渲染一个textarea元素                |
| checkbox      | 渲染一个<input type="checkbox">元素 |
| checkboxes    | 渲染多个<input type="checkbox">元素 |
| raadiobutton  | 渲染一个<input type="radio">元素    |
| raadiobuttons | 渲染多个<input type="radio">元素    |
| select        | 渲染一个select元素                  |
| option        | 渲染一个option元素                  |
| options       | 渲染多个option元素                  |
| errors        | 在span元素中渲染字段错误             |

- 表单标签的属性

> 为了更好的将表单标签渲染称HTML, 表单标签具备了很多属性, 并非HTML元素属性
>
> 其中最重要的是`commandName`属性(已被弃用, 使用`modelAttribute`替换), 因为它定义了模型属性的名称, 如果它存在, 则必须在返回包含该表单属兔的请求处理方法中添加对应的模型属性, 代码如下

``` html
<!-- bookAddForm.jsp -->
<form:form commandName="book" action="book_save" method="post">
	some HTML elements...
</form:form>
```

> 这里的form绑定了一个book实例, 因此在该请求处理方法中必须在Model上增加一个book实例, 否则会抛出异常

``` java
@RequestMapping("/bookAdd")
public String bookAdd () {
	some java ...
	model.addAttribute("book", new Book()); // 这是必须的
	return "bookAdd";
}
```

> 除此之外, 还是需要正常使用HTML的其他属性, 例如上面的form元素, 依然使用着action和method

- 其他标签 (P71-P78, 各种html标签很多, 这把几个重点属性说一下, 不一一列举了)

> 在form标签内部, 我们还需要很多内部的字段标签来对应绑定模型属性, 以input为例绑定方式如下

``` html
	<form:input id="productName" path="productName" cssClass="form-item"></form:input>
	<!-- 被渲染成下面 -->
	<input type="text" id="productName" class="form-item" />
```

还有一些其他属性


| 属性名称	      | 描述                                                          |
|---------------|---------------------------------------------------------------|
| path          | 要绑定的属性路径                                                 |
| cssClass      | 定义要应用到被渲染的元素的CSS类                                    |
| cssStyle      | 定义要应用到元素的CSS样式                                         |
| cssErrorClass | 定义要应用在元素的CSS类, 如果bound属性中有错误, 会覆盖cssClass        |
| htmlEscape    | 接受true和false, 表示是否对被渲染值进行HTML转码                     |
| showPassword  | 是否应该显示或遮挡密码, 默认false                                  |
| label         | 作为label用于被渲染复选框的值                                      |
| delimiter     | 定义两个input(批量渲染) 之间的分割符                                |
| element       | 给每个被渲染的input元素都定义一个HTML元素, 默认"span"                |
| items         | 用于生成input元素的对象的Collection, Map, Array                    |
| itemLabel     | item属性中定义的Collection, Map, Array中对象属性, 为input提供label  |
| itemValue     | item属性中定义的Collection, Map, Array中对象属性, 为input提供值     |
| errors        | 在span元素中渲染字段错误                                           |

- 一个数据绑定例子

