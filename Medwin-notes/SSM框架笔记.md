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

### 事务

### 使用@AspectJ注解驱动切面

最常用。需导入spring-aspects包！

#### 