## 阅读《SpringBoot开发实战》总结
### 第一部分 SpringBoot框架基础
#### 第一章 SpringBoot简介

##### **EJB到Spring**

Spring框架是由Rod Johnson在2001年开始开发的一个开源框架，把依赖注入和面向切面编程发扬光大，在Spring之前，实现*J2EE的方式大多采用EJB，EJB的基础是RMI(Remote Method Invocation, 远程方法调用)，EJB规范定义了EJB组件在何时如何与它们的容器进行交互作用，容器负责提供公用的服务，比如目录服务，事务管理，安全性，资源缓冲池以及容错性。但是EJB并不是实现J2EE的唯一方式，大多数应用都不需要采用这么重的解决方案，所以EJB显得十分臃肿。
    
对中小型应用来说，不需要采用分布式的解决方案，Spring就是为了解决这个问题而诞生的。

Spring是一个轻量级控制反转(IOC)和面向切面编程(AOP)的容器框架，它的主要功能是使用基本的Java Bean代替EJB，并提供更多的企业应用功能。

Spring框架的核心功能简单概括为：结耦依赖(DI)，系统模块化(AOP)

Spring容器以Bean的方式来组织和管理Java应用中的各个组件及其组件之间的关系。

Spring使用BeanFactory来产生和管理Bean，它是工厂模式的实现。

Spring框架主要用于与其他技术(例如Struts，Hibernate，MyBatis等)进行整合，将应用程序中的Bean组件实现低耦合关联。

##### **Spring框架核心模块**

核心容器模块提供了Spring框架的基本功能，包括Core，Beans，Context，EL。

AOP，Aspects模块提供了符合AOP Alliance规范的面向切面的编程实现，因为继承了AspectJ，因此比SpringASP功能更强大

数据访问继承模块包括JDBC，ORM，OXM，JMS和事务(Transactions)。

Web/Remoting模块包含Web，Web-Servlet，Web-Struts，Web-Porlet模块

Test模块支持Junit和TestNG测试框架

##### **Spring Boot**

Spring Boot由Pivotal团队提供的全新框架，为了简化Spring应用的初始搭建以及开发过程，遵循着"约定优于配置(*COC)"，致力于帮助开发人员快速开发应用。

使用Spring Boot我们可以体验到下面这些特性：
- 使用Spring Initializr在几秒内创建Spring应用。
- 构建任何东西————REST API，WebSocket，Web，流媒体，任务等。
- 简化了安全(Security)权限开发。
- 丰富的SQL和NoSQL支持。
- 嵌入式运行时支持————Tomcat，Jetty，Undertow。
- 开发人员的生产力工具，例如实时重载(reload)和自动启动(restart)
- 开箱即用的模块化依赖。
- 供生产环境直接使用的特性，如追踪，度量，健康状态的监控。
- 丰富的IDE支持：Spring Tool Suite，InteLLiJ IDEA，NetBeans。

##### **Spring Boot核心模块**

- spring-boot核心工程。
- starters，是Spring Boot的启动服务工程。
- autoconfigure是Spring Boot实现自动配置的核心工程。
- actuator提供Spring Boot应用的外围支撑性功能
- tools提供Spring Boot开发者的常用工具集。
- cli是Spring命令交互工具，可用于Spring的快速搭建，如果你不喜欢Maven可以使用CLI来开发运行Spring应用。

**本章名词解释**

J2EE
> J2EE，也称为Java EE，全称：Java 2 Platform Enterprise Edition，一种企业级开发分布式应用程序的开发规范是，其中定义了动态Web页面(Servlet和JSP)，商业组件(EJB)，异步消息传输机制(JMS)，名称和目录定位服务(JNDI)，数据库访问(JDBC)，子系统的连接器(JCA)，和安全服务等。

COC
> COC，全称：Convention Over Configuration，中文：约定优于配置，按约定编程，是一种软件设计范式，旨在减少软件开发人员需要做决定的数量，获得简单的好处，而又不失灵活性

对于COC，文中举了一个很好的例子，就是在Java的知名对象关系映射（ORM）框架Hibernate中，将类及其属性映射到数据库上，在XML文件中进行配置，而其中大部分配置都能通过约定得到，如将类映射到对应的数据库表，将类属性一一映射到表上的字段，在后续的版本中抛弃了XML配置文件，而是使用Java类属性使用驼峰式命名对应数据库中下划线命名这个恰当的约定，大大简化配置。而对于不符合这些约定的特殊情形，就使用java注解来标注说明。

例如，Spring通过使用约定好的注解来标注Spring应用中的各层类：

- `@Component` 标注一个一个普通的Spring Bean类。
- `@Controller` 标注一个控制器组件类。
- `@Service` 标注一个业务逻辑组件类。
- `@Repository` 标注一个DAO组件类。

由此可见，Java的成功，Spring的成功，XML的成功，Maven的成功，都有其必然性，因为它们的设计理念都包含一个很简单但很深刻的道理，那就是"通用"。为什么通用？因为遵循约定。

#### 第二章 快速开始HelloWorld

##### **创建SpringBoot项目**

如何创建项目，这个在文中配有大量图片说明，这里不再赘述，如果还不知道如何创建项目，可以上网查询，教程非常多，我们直接从创建了一个简单的RESTful WEB HTTP Service之后，开始讲解SpringBoot使用中的代码。

##### **SpringBoot应用注解@SpringBootApplication**

SpringBoot应用的一个标志性写法就是在应用的入口类上添加`@SpringBootApplication`注解，我们来看一下这个注解的定义

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excluderFilters = {
    @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
    @Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class)
})
public @interface SpringBootApplication { ... }
```

`@SpringBootApplication`实际上封装了三个注解：
- `@SpringBootConfiguration` 配置类注解。
- `@EnableAutoConfiguration` 启用自动配置注解。
- `@ComponentScan` 组件扫描注解。

##### **SpringBoot配置类注解**

`@SpringBootConfiguration`与`@Component`注解实际上是一样的，为什么这么说？因为`@SpringBootConfiguration`其实是SpringBoot包装的`@Configuration`注解：

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
public @interface SpringBootConfiguration { }
```

而`@Configuration`注解使用的又是`@Component`注解：

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Configuration { }
```

`@Component`注解的功能是吧普通的POJO实例话到Spring容器中，相当于配置文件中的`<bean id="" class="" />`。

在类上添加`@Configuration`注解，表示这个类代表一个Spring配置文件，与原来的XML配置是等效的，只不过现在Java类加上一个`@Configuration`注解进行配置，这种方式与XML相比可以称的上是极简风格了，大大提高了可读性。

Spring容器可以扫描出任何我们添加了`@Component`注解类，Bean的注册逻辑在`ClassPathScanningCandidateComponentProvider`类中的`registerDefaultFilters`方法里。

##### **启动自动配置注解**

`@EnableAutoConfiguration`注解是SpringBoot最核心的注解，首先看下定义:

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Import(AutoConfiguationImportSelector.class)
class @interface EnableAutoConfiguration { ... }
```

其中导入配置类注解`@Import`标识导入`@Configuration`标注的配置类，`@Import`用来整合所有在`@Configuration`注解中定义的Bean配置，这和我们把多个XML配置文件导入到单个文件的场景相似，`@Import`注解实现了相同的功能。

使用`@EnableAutoConfiguration`注解可以启用Spring应用程序上下文的自动配置，SpringBoot会尝试猜测你可能需要的Bean配置，自动配置类通常是根据你在类路径中你定义的Bean来推断可能需要怎样的配置。

例如，如果在你的类路径中又`tomcat-embedded.jar`这个类库，那么SpringBoot会根据此信息来判断你可能需要一个`TomcatServletWebServiceFactory` 配置（除非你已经定义了你自己的`ServletWebServiceFactory` Bean）,当然我们也可以通过exclude或者excludeName变量的值来手动排除你不想要的自动配置。

SpringBoot的默认扫描路径是入口类所在的根包，以及其子包，通常，SpringBoot自动配置Bean是根据Conditional Bean（条件Bean）中的注解信息来推断的，例如`@ConditionalOnClass`，`@ConditionalOnMissingBean`注解， 其他详细内容在后续的解读中奉上。


















