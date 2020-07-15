## 阅读Spring发展
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






















