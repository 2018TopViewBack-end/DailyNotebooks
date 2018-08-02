# Spring

## IoC容器

ioc容器最低要求是实现BeanFactory接口而非ApplicationContext

通过对bean生命周期的学习，就可以知道如何在初始化和销毁时加入自定义的方法

bean初始化时，有些步骤在一定条件下才会执行，如果不注意这些就会发现明明实现了一些接口但是方法并没有执行（详见bean的生命周期）

## 依赖注入

### 构造器注入

参数多时较为复杂

### setter注入

最主流的方式，灵活且可读性高。spring通过反射调用无参构造函数，同时通过对应的setter注入配置的值

### 接口注入

接口注入是从别的地方注入的方式，如在web中数据源通过tomcat配置，这时可以用JNDI的形式通过接口将bean注入到ioc容器中

## Bean装配

**优先原则**：隐式bean的发现机制和自动装配>java接口和类中用注解实现配置>xml配置 （**约定优于配置**）

简单属性值的注入可以用@Value("...")

### **用注解时ioc容器的实现类：AnnotationConfigApplicationContext()**

使用：加一个config类在bean同一个包下 （见SSM Page237）

```java
@ComponentScan(...)
public class Config{
    //空
}
```

然后用

```java
AnnotationConfigApplicationContext(Config.class)
```



- context需要close？

- @AutoWired根据类型的实质？

- 在构造方法中使用@Autowired：

  ```java
  public RoleController (@Autowired RoleService roleService){
      this.roleService = roleService;
  }
  ```

### 使用@Bean装配bean：

引入第三方jar时没有源码，无法加入@Component注解，可以用@Bean**注解到方法上**（不能注解在类上），将方法**返回的对象**作为spring的bean

**但引入第三方包或服务尽量使用XML方式**，减少理解

- @ImportSource

### 使用@Profile，以支持在不同开发环境下进行切换的需求（需要自行激活）

如配置两个分别供开发人员和测试人员使用的dataSource，使用@Profile分别标注为@Profile("dev")和@Profile("test")

也可使用xml

### 加载properties文件

使用@PropertySource。配置项：name、value等

### 条件化装配bean

类需要实现接口Condition

## Spring AOP

### 使用@AspectJ注解驱动切面

最常用。需导入spring-aspects包！

## Spring+Mybatis

### MapperScannerConfigurer  P325

## 事务

- **声明式事务的底层原理 ---- AOP**

常用的事务管理器是DataSourceTransactionManager，它的最顶层是PlatformTransactionManager

需要在xml中配置事务管理器：

```xml
<bean id="transactionManager" class="org.springframnwork.jdbc.datasource
                                     .DataSourceTransactionManager">
	<property name="dataSource" ref="dataSource"/>
</bean>
```

**配置事务的主流方式：@Transaction注解**：

需在**配置类**中实现TransactionManagementConfigurer的annotationDrivenTransactionManager方法，spring会将其返回值作为程序中的事务管理器，同时要在此类上加上@EnableTransactionManagement注解

![](https://github.com/Medw1nnn/repo-pics/blob/master/%E4%BA%8B%E5%8A%A1.png?raw=true)

@Transactional有几个比较重要的配置项：

1. value或transactionManager--定义事务管理器
2. isolation--隔离级别（重要）
3. propagation--传播行为（重要）

### 多事务产生并发问题

其中最主要的就是丢失更新，由此引出隔离级别

### 隔离级别

可以在不同程度下减少丢失更新

首先，丢失更新分为第一类丢失更新（已被数据库克服）和第二类丢失更新

#### SQL的隔离级别定义（级别越高性能越低）：P344

1. 脏读 （dirty read）-- 事务一可以读取事务二未提交的事务
2. 读/写提交（read commit）-- 一个事务只能读取另一个事务已经提交的数据
3. 可重复读（repeatable read）-- 克服读写提交产生的不可重复读问题，但会产生幻读
4. 序列化（Serializable）-- 克服幻读

大部分场景下，企业会选择读写提交的方式，但需要克服读写一致性问题

### 传播行为

指方法间调用事务策略的问题，如一个方法调用另一个方法时，是否需要创建新事务

**传播行为共有七种**，默认为REQUIRED，REQUIRES_NEW 和 NESTED(嵌套事务) 也比较重要

### @Transaction自调用失效问题

因为实现原理是aop->动态代理，自己调用自己时并不存在代理对象的调用，所以不会产生AOP为我们设置@Transaction配置的参数，从而产生自调用注解失效问题



- 在log4j配置文件中加入 log4j.logger.org.springframework=DEBUG,使得Spring在运行中会输出对应的日志

----

# SpringMVC

## 运行流程

- SpringMVC框架是围绕DispatcherServlet工作的，springmvc会根据配置信息得到URI和handler之间的映射关系
- springmvc还会给处理器加入拦截器，这样就构成一个处理器和拦截器的执行链，并根据上下文初始化视图解析器等内容，当处理器返回时就可以根据视图解析器定位视图然后将模型数据渲染到视图中来响应用户请求
- 当请求到来时，dispatcherServlet首先通过请求和handlerMapping配置，找到对应的处理器(handler)，开始运行执行链，而运行处理器需要有对应的环境，因此就有了一个处理器的适配器（handlerAdapter）
- 当处理器返回模型和视图给dispatcherServlet后，它就会把对应的视图信息传递给视图解析器（ViewResolver），这一步会决定是否采用逻辑视图

![dspringMVCè¿è¡æµç¨.png](https://github.com/Medw1nnn/repo-pics/blob/master/springMVC%E8%BF%90%E8%A1%8C%E6%B5%81%E7%A8%8B.png?raw=true) 

## 配置文件解析

ContextLoaderListener实现了ServletContextListener接口

有开启事务的需要在springmvc.xml加上

```xml
<tx:annontation-driven transaction-manager:transactionManager"/>
```

- default-servlet-handler和annotation-driven一定要一起加

## SpringMVC初始化

SpringMVC需要初始化IoC容器和DispatcherServlet请求两个context，其中DispatcherServlet请求上下文是Spring Ioc上下文的扩展

大部分情况下使用ContextLoaderListener进行DispatcherServlet初始化

当IoC容器没有对应初始化时，DispatcherServlet会尝试去初始化它，最后调度onFresh方法，此方法将初始化springMVC的各个组件

onFresh方法唯一调用的initStrategies方法解析：

![DispatcherServletçonFreshæ¹æ³(1).png](https://github.com/Medw1nnn/repo-pics/blob/master/DispatcherServlet%E7%9A%84onFresh%E6%96%B9%E6%B3%95(1).png?raw=true) 

![DispatcherServletçonFreshæ¹æ³(2).png](https://github.com/Medw1nnn/repo-pics/blob/master/DispatcherServlet%E7%9A%84onFresh%E6%96%B9%E6%B3%95(2).png?raw=true) 

启动期间DispatcherServlet会加载这些配置的组件进行初始化，因此不需要很多手动配置

- 可不用web.xml文件，只使用注解方式初始化，需有一个类继承A b s t r a c t A n n o t a t i o n C o n f i g D i s p a t c h e r S e r v l e t l n i t i a l i z e r，然后实现它的方法

## 开发流程详解

## HandlerMapping

定义了请求到处理器间的映射，获取处理器调用链

## HandlerExecutionChain

处理器调用链，包含处理器(handler)和对应的拦截器

## HandlerAdapter

数据类型转换、检验、调用HttpMessageConverter等

### 关于拦截器：

springmvc解析完Controller中的**requestMapping**的配置，再结合所配置的拦截器，组成**多个拦截器和一个控制器**的形式，存放到一个**HandlerMapping**中。当请求来到服务器，首先通过请求信息找到对应的HandlerMapping，进而找到对应的拦截器和控制器然后运行

当xml中配置了annotation-driven或使用java配置使用注解@EnableWebMvc时，系统会初始化ConversionServiceExposingIntercepter，它的作用是根据各种注解完成相应的功能

spring提供公共拦截器HandlerIntercepeterAdapter，每个拦截器都要继承它

### 控制器开发

分为三步：

1. 获取请求参数
2. 处理业务逻辑
3. 绑定模型和视图

----

- 参数尽量不要使用Servlet容器中的API（如session、request等），因为这样的控制器会依赖于Servlet容器

- @RequestParam 获取参数就相当于 request.getParameter，它会进行对类型的转换。默认情况下不能为空，但可以使用required和defaultValue进行配置

- 请求会有很多参数传递方法，如Json、URI路径传递参数或文件流等

- @SessionAttritube("name") 获取参数就相当于 session.getParameter

- handler方法return ModelAndView 把信息传递到页面。需mv.setName("jspName")

- 因前端使用ajax，所以后台往往需要返回json给前端使用

  ```java
  @RequestMapping(value = "/ getRole", method=RequestMethod.  GET)
  public  ModelAndView  getRole(@RequestParam ("id")  Long  id)  (
      Role  role=  roleService.getRole(id) ;
      ModelAndView  mv  = new  ModelAndView();
      mv.addObject ("role", role);
  	//指定视图类型
  	mv.setView(new MappingJakcson2JsonView());
  	return mv;
  }
  ```

  注：**@RequestBody**更为简单和广泛使用、

- 某些应用需要传递的是JSON，比如查询用户的时候，需要**分页**，可能用户信息比较多，那么查询参数可能多达1 0个，为了易于控制，往往**将客户查询参数组装成另一个JSON数据集**，而把**分页参数作为普通参数**传递，进而把数据传递给后台。见P400

- 区分：@RequestParam("name")和@PathVariable("name")。@RequestParam（org.springframework.web.bind.annotation.RequestParam）用于将指定的请求参数赋值给方法中的形参。 

  @PathVariable是获取通过URL传递的参数. 且@PathVariable允许参数为空

- JQuery传递json数据。p405。使用@RequestBody接受参数
- 通过表单序列化（jq中操作）也能将表单数据转换为字符串传递给后台。因为一些隐藏表单需要一定的计算，所以也需要在用户点击提交后通过序列化去提交表单 

### 重定向传递参数

可通过redirect：，或返回ModelAndView实现重定向 （参数中加ModelAndView，此对象springMVC会自定初始化）

```java
mv.setName( "redirect:./showRoleJsonInfo.do");
return mv;
```

- 在URL重定向过程中，并不能有效传递对象，因为重定向的参数是以字符串传递的。

  springMVC提供了一个方法--flash属性。此时需要提供的模型就是一个RedirectAttribute（写在参数里）

  然后用

  ```java
  redirectAttribute.addFlashAttritube("role“, role）；
  return “redirect: ./showRole.do”;
  ```

###保存并获取属性参数

三个注解标记参数：

1. @RequsetAttritube 在页面中用setAttritube可以在方法中get到，默认不能为空(少用)
2. @SessionAttritube 
3. @SessionAttritubes，可以给它配置一个字符串数组，这个数组对应的是数据模型对应的键值对，然后将这些键值对保存到session对象中 （只能对类标注）
   - 没有@RequsetAttritubes 是因为springMVC更希望你使用他所提供的数据模型

### 验证器验证表单

需导入四个包：classmate、jboss-logging、hibernate-validator、validation-api

**使用JSR303注解验证输入内容**

![JSR303æ³¨è§£.png](https://github.com/Medw1nnn/repo-pics/blob/master/JSR303%E6%B3%A8%E8%A7%A3.png?raw=true) 

使用方式：在po属性中加入验证注解，在handler目标方法参数po加上@Valid
示例见p427

### Validator接口

实现表单的复杂验证，可用@InitBinder将实现类与控制器绑定在一起

## 数据模型

ModelAndView有一个类型为**ModelMap**（LinkedHashMap的子类）的属性model，spring还创建了ExtendedModelMap类，它实现了数据模型定义的Model接口，在此基础上派生了关于数据绑定的类--BindAwareModelMap 。P433

实际使用时，handler方法可以使用**ModelMap、Model、ModelAndView**, 前两个可相互转换

## 视图和视图解析器

**视图解析器**：对逻辑视图进一步解析，以定位真实视图。

**视图**：把控制器查询回来的数据模型进行渲染，以显示给请求者查看

