# Spring

## Spring入门

## 随记

* "execution(* com.zzz.www.demo1.printRole(..)) "中**"*"后面要加个空格 **
* Spring在有接口时用jdk代理，在无接口时用cglib代理，因此，有无接口都能提供AOP功能
* 如果字面值包含特殊字符，可以使用<![CDATA[]]>包裹起来，如<![CDATA[<ShangHai^]]>
* 属性需要先初始化后才可以为级联属性赋值，否则会抛异常  
* 给级联属性赋值：**就是在配置文件中给属性的属性赋值。** 
* bean中的单例和正常的单例不太一样
* ![1531996797088](https://github.com/630231047/notebook/blob/master/photos/1531996797088.png?raw=true)
* IOC控制反转，DI依赖注入
* ![1532003812699](https://github.com/630231047/notebook/blob/master/photos/1532003812699.png?raw=true)

![1532005140823](https://github.com/630231047/notebook/blob/master/photos/1532005140823.png?raw=true)

## 控制反转 

* 控制反转是一种通过描述(在java中可以是XML或者注解)并通过第三方去产生或获取特定对象的方式
* 依赖注入是IOC另一种表述方式，依赖容器把资源注入到需要的地方

### Bean的作用域（scope）：

* prototype原型 每次获取bean都产生一个新的对象
* singleton单例 整个容器中只有一个对象实例 (default)
* request 每次请求时创建一个新的对象
* session在会话的范围内获取一个对象（这里指的session是HTTPSession，而不是mybatis中的session）
* global session 只在portlet下有用，表示是application
* application 在应用范围中的一个对象

### Bean.xml配置

* 1.根据参数的下标来设置

* ```xml
  <bean id="user" class="cn.sxt.vo.User">
  <!-- index指构造方法 参数下标从0开始-->
  <constructor-arg index="0" value="李四"/>
  </bean>
  ```

  

* 根据参数名称来设置

  ```xml
  <bean id="user" class="cn.sxt.vo.User">
  <!-- name指参数名-->
  <constructor-arg name="name" value="李四"/>
  </bean>
  ```

  

* 根据参数类型来设置

* ```xml
  <bean id="user" class="cn.sxt.vo.User">
  <!-- type指参数类型-->
  <constructor-arg type="java.lang.String" valus="李四"/>
  </bean>
  ```

* 通过工厂方法来创建

  * 静态工厂（略）
  * 动态工厂（略）

### bean配置

```xml
<!-- id是bean的标识符 要唯一 如果没有配置id， name默认标识符
如果配置id，又配置了name 那么name是别名
name可以设置多个别名 分隔符可以是 空格，逗号和分号
class是bean的全限定名=包名+类名
如果不配置 id和name 那么可以根据
applicationContext.getBean(Class)获取对象
-->
<bean id="h1" name="hello h2,h3;h4" class="cn.sxt.bean.Hello">
<property name="name" value="张三"/>
</bean>
```

### 团队协作用import来实现

```xml
<import resource=""/>
<!-- resource中的路径用"/"分开-->
```

## 依赖注入--dependency injection

* 依赖：指bean对象依赖于容器。bean对象的依赖资源

* 注入：指bean对象依赖的资源由容器来设置和装配

* setter 注入 (切记，实体类中一定要有setter)：

* ```xml
  <bean id="student" class="com.duoduo.Student">
          <!-- 常量注入 -->
          <property name="name" value="constant test"/>
          <!-- Bean 注入 -->
          <property name="address" ref="address"/>
          <!-- 数组注入 -->
          <property name="books">
              <array>
                  <value>yuwen</value>
                  <value>shuxue</value>
                  <value>yingyu</value>
              </array>
          </property>
          <!-- List 注入 -->
          <property name="hobbies">
              <list>
                  <value>basketball</value>
                  <value>football</value>
              </list>
          </property>
  
          <!-- Map 注入 -->
          <property name="cards">
              <map>
                  <entry key="ICBC" value="1234567890"/>
                  <entry key="CMBC" value="12121212121"/>
              </map>
          </property>
  
          <!-- Set 注入 -->
          <property name="games">
              <set>
                  <value>dota</value>
                  <value>QQ game</value>
              </set>
          </property>
  
          <!-- Properties 注入 -->
          <property name="properties">
              <props>
                  <prop key="no">2012011111</prop>
                  <prop key="name">name</prop>
                  <prop key="aget">20</prop>
              </props>
          </property>
  
      </bean>
  
      <bean id="address" class="com.duoduo.Address">
          <property name="addressDetail" value="student address information"/>
      </bean> 
  ```


autowire自动装配：

* no不使用自动装配
* byName根据名称(set方法名来的)去查找相应的bean，如果有则装配上
* byType根据类型进行自动装配 不用管bean的id.但是同一种类型的bean只能有一个。建议慎用
* constructor 当通过构造器实例化bean时 适用byType的方式装配构造方法

## AOP

* 关注点：增加的某个业务。如日志，安全，缓存，事务等

* ![1532178798304](https://github.com/630231047/notebook/blob/master/photos/1532178798304.png?raw=true)
* AOP的重要性
  * Spring AOP就是将公共的业务(如日志，安全等)和领域业务结合。当执行领域业务时将会把公共业务加起来。程序员注重领域业务，其本质还是动态代理
  * ![1532227854167](https://github.com/630231047/notebook/blob/master/photos/1532227854167.png?raw=true)

![1532227884099](https://github.com/630231047/notebook/blob/master/photos/1532227884099.png?raw=true)

![1532229473680](https://github.com/630231047/notebook/blob/master/photos/1532229473680.png?raw=true)

![1532231644593](https://github.com/630231047/notebook/blob/master/photos/1532231644593.png?raw=true)

![1532231787774](https://github.com/630231047/notebook/blob/master/photos/1532231787774.png?raw=true)

![1532316919502](https://github.com/630231047/notebook/blob/master/photos/1532316919502.png?raw=true)

![1532317106444](https://github.com/630231047/notebook/blob/master/photos/1532317106444.png?raw=true)

## 一些重要的注解

* @Configuration标注在类上，相当于把该类作为spring的xml配置文件中的`<beans>`，作用为：配置spring容器(应用上下文) 

* @Bean标注在方法上(返回某个实例的方法)，等价于spring的xml配置文件中的`<bean>`，作用为：注册bean对象 

* @EnableAspectJAutoProxy代表启用AspectJ框架的自动代理，这个时候Spring才会生成动态代理对象，进而可以使用AOP

* @Autowired注解可用于为类的属性、构造器、方法进行注值。默认情况下，其依赖的对象必须存在（bean可用），如果需要改变这种默认方式，可以设置其required属性为false。 

* @Pointcut用来定义一个切点，避免重复书写路径

  * ```java
    //用法
    @Pointcut("execution(* com.zzz.www.demo1.RoleServiceImpl.printRole(...))")
    public void print(){
        
    }
    @Before("print()")
    public void before(){
        System.out.println("before...");
    }
    ```

* @ComponentScan(basePackageClasses = {Role.class, RoleServiceImpl.class})可以同时装配两个bean

## Spring Bean 的生命周期

* 1. 实例化一个Bean，也就是我们通常说的new
  2. 按照Spring上下文对实例化的Bean进行配置，也就是IOC注入
  3. 如果这个Bean实现了BeanNameAware接口，会调用它实现的setBeanName(String beanId)方法，此处传递的是Spring配置文件中Bean的ID
  4. 如果这个Bean实现了BeanFactoryAware接口，会调用它实现的setBeanFactory()，传递的是Spring工厂本身（可以用这个方法获取到其他Bean）
  5. 如果这个Bean实现了ApplicationContextAware接口，会调用setApplicationContext(ApplicationContext)方法，传入Spring上下文，该方式同样可以实现步骤4，但比4更好，以为ApplicationContext是BeanFactory的子接口，有更多的实现方法
  6. 如果这个Bean关联了BeanPostProcessor接口，将会调用postProcessBeforeInitialization(Object obj, String s)方法，BeanPostProcessor经常被用作是Bean内容的更改，并且由于这个是在Bean初始化结束时调用After方法，也可用于内存或缓存技术
  7. 如果这个Bean在Spring配置文件中配置了init-method属性会自动调用其配置的初始化方法
  8. 如果这个Bean关联了BeanPostProcessor接口，将会调用postAfterInitialization(Object obj, String s)方法

  注意：以上工作完成以后就可以用这个Bean了，那这个Bean是一个single的，所以一般情况下我们调用同一个ID的Bean会是在内容地址相同的实例

  9. 当Bean不再需要时，会经过清理阶段，如果Bean实现了DisposableBean接口，会调用其实现的destroy方法
  10. 最后，如果这个Bean的Spring配置中配置了destroy-method属性，会自动调用其配置的销毁方法

![1532354212752](https://github.com/630231047/notebook/blob/master/photos/1532354212752.png?raw=true)

## bean配置的继承

* 使用bean的parent属性指定继承哪个bean的配置![1532358333134](https://github.com/630231047/notebook/blob/master/photos/1532358333134.png?raw=true)

## 导入外部文件

* •Spring 提供了一个 PropertyPlaceholderConfigurer 的 BeanFactory 后置处理器, 这个处理器允许用户将 Bean 配置的部分内容外移到属性文件中. 可以在 Bean 配置文件里使用形式为 ${var} 的变量, PropertyPlaceholderConfigurer 从属性文件里加载属性, 并使用这些属性来替换变量.

  •Spring 还允许在属性文件中使用 ${propName}，以实现属性之间的相互引用。

* ![1532359125656](https://github.com/630231047/notebook/blob/master/photos/1532359125656.png?raw=true)

## SpEL

* ![1532395083246](https://github.com/630231047/notebook/blob/master/photos/1532395083246.png?raw=true)