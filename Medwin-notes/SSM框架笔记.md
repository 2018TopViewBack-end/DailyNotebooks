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