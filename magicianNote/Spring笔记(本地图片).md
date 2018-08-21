# Spring

## Spring入门

## 随记

* "execution(* com.zzz.www.demo1.printRole(..)) "中**"*"后面要加个空格 **
* Spring在有接口时用jdk代理，在无接口时用cglib代理，因此，有无接口都能提供AOP功能
* 如果字面值包含特殊字符，可以使用<![CDATA[]]>包裹起来，如<![CDATA[<ShangHai^]]>
* 属性需要先初始化后才可以为级联属性赋值，否则会抛异常  
* 给级联属性赋值：**就是在配置文件中给属性的属性赋值。** 
* 继承SqlSessionDaoSupport 可以用上getSqlSession().setSelectList();简化代码
* ![1532420530104](E:\Program Files\notebook\photos\1532420530104.png)![1532420594421](E:\Program Files\notebook\photos\1532420594421.png)
* 配置sqlSessionFactory
* ![1532421517760](E:\Program Files\notebook\photos\1532421517760.png)
* bean中的单例和正常的单例不太一样
* ![1531996797088](E:\Program Files\notebook\photos\1531996797088.png)
* IOC控制反转，DI依赖注入
* ![1532003812699](E:\Program Files\notebook\photos\1532003812699.png)

![1532005140823](E:\Program Files\notebook\photos\1532005140823.png)

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

* ![1532178798304](E:\Program Files\notebook\photos\1532178798304.png)
* AOP的重要性
  * Spring AOP就是将公共的业务(如日志，安全等)和领域业务结合。当执行领域业务时将会把公共业务加起来。程序员注重领域业务，其本质还是动态代理
  * ![1532227854167](E:\Program Files\notebook\photos\1532227854167.png)

![1532227884099](E:\Program Files\notebook\photos\1532227884099.png)

![1532229473680](E:\Program Files\notebook\photos\1532229473680.png)

![1532231644593](E:\Program Files\notebook\photos\1532231644593.png)

![1532231787774](E:\Program Files\notebook\photos\1532231787774.png)

![1532316919502](E:\Program Files\notebook\photos\1532316919502.png)

![1532317106444](E:\Program Files\notebook\photos\1532317106444.png)

## 一些重要的注解

* @Configuration标注在类上，相当于把该类作为spring的xml配置文件中的`<beans>`，作用为：配置spring容器(应用上下文) 

* @Bean标注在方法上(返回某个实例的方法)，等价于spring的xml配置文件中的`<bean>`，作用为：注册bean对象。在其中可以为该bean设置名字，如无设名字，则名字为该方法名。

* @EnableAspectJAutoProxy代表启用AspectJ框架的自动代理，这个时候Spring才会生成动态代理对象，进而可以使用AOP

* @Autowired注解可用于为类的属性、构造器、方法进行注值。默认情况下，其依赖的对象必须存在（bean可用），如果需要改变这种默认方式，可以设置其required属性为false。 

* @Qualifier用在@Autowired下面，可以指定所需的bean的名字

* @Required用在set方法上，一旦用了这个注解，那么容器在初始化bean的时候必须要进行set，也就是说必须对这个值进行依赖注入。但是，使用它时需要在xml文件中加入**`<context:annotation-config />`**

* @RunWith @ContextConfiguration

  * Spring常用的 Bean对象 如Service Dao Action等等 在我们正常的项目运行中由于有Tomcat帮我们自动获得并初始化了这些Bean，所以我们不需要关系如何手动初始化他们。  但是在需要有测试类的时候，是没有tomcat帮我们初始化它们的，这时候如果是下面这样就抛出空指针异常，因为我们并没有得到一个实例化的Bean 
  * 注意：传统的spring容器的范围比JUnit小，但通过这两个注解可以使spring容器范围大于JUnit4范围

  ```java
  @RunWIth(SpringJunit4ClassRunner.class)
  @ContextConfiguration(locations = {"classpath:applicationContext.xml"}
  public  class MyTest{
  @Test
  public void runBy(){
  //.......
  }
  }
  ```

  * @ContextConfiguration 整合JUnit4测试时，使用注解引入多个配置文件

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
  6. :baby_chick: 重要:baby_chick: 如果这个Bean关联了BeanPostProcessor接口，将会调用postProcessBeforeInitialization(Object obj, String s)方法和postProcessAfterInitialization(Object obj, String s)方法，BeanPostProcessor经常被用作是Bean内容的更改，并且由于这个是在Bean初始化结束时调用After方法，也可用于内存或缓存技术，其还可以用来获取bean的名字，但要注意，返回值要为其第一个参数的值
  7. 如果这个Bean在Spring配置文件中配置了init-method属性会自动调用其配置的初始化方法
  8. 如果这个Bean关联了BeanPostProcessor接口，将会调用postAfterInitialization(Object obj, String s)方法

  注意：以上工作完成以后就可以用这个Bean了，那这个Bean是一个single的，所以一般情况下我们调用同一个ID的Bean会是在内容地址相同的实例

  9. 当Bean不再需要时，会经过清理阶段，如果Bean实现了DisposableBean接口，会调用其实现的destroy方法
  10. 最后，如果这个Bean的Spring配置中配置了destroy-method属性，会自动调用其配置的销毁方法

![1532354212752](E:\Program Files\notebook\photos\1532354212752.png)

## bean配置的继承

* 使用bean的parent属性指定继承哪个bean的配置![1532358333134](E:\Program Files\notebook\photos\1532358333134.png)

## 导入外部文件

* •Spring 提供了一个 PropertyPlaceholderConfigurer 的 BeanFactory 后置处理器, 这个处理器允许用户将 Bean 配置的部分内容外移到属性文件中. 可以在 Bean 配置文件里使用形式为 ${var} 的变量, PropertyPlaceholderConfigurer 从属性文件里加载属性, 并使用这些属性来替换变量.

  •Spring 还允许在属性文件中使用 ${propName}，以实现属性之间的相互引用。

  ```xml
  <context:property-placeholder
           location="classpath:db.properties"/>
  ```

## SpEL

* ![1532395083246](E:\Program Files\notebook\photos\1532395083246.png)

## 自动装配

* 重要注解：@AutoWired  @Primary  @Qualifier  
* 自动装配可以将对象注入一个对象中。
* 自动装配的实现：
  * 首先要知道，bean的生命周期一开始是先实例化，相当于new一个对象，然后去寻找需要注入的资源，再进行配置。
  * 在寻找时，岂会查找对应的类型，将其注入进来，最终完成了依赖注入。
* 自动装配一般用在成员变量和构造函数的参数上
* 

## MyBatis与Spring Mapper接口方式的开发整合

* [参考文献](https://blog.csdn.net/kangguang/article/details/79350011)

* ```xml
  <!--在Spring的配置文件中 增加相应的Bean,从而获得Mapper-->
    <bean id = "customerMapper" class ="org.mybatis.spring.mapper.MapperFactoryBean">
          <property name="mapperInterface" value="com.kangxg.mapper.CustomerMapper"/>
          <property name="sqlSessionFactory" ref ="sqlSessionFactory"/>
    </bean>
  ```


## 配置MapperScannerConfigurer

* 没有必要在 Spring 的 XML 配置文件中注册所有的映射器。相反,你可以使用一个 MapperScannerConfigurer , 它 将 会 查 找 类 路 径 下 的 映 射 器 并 自 动 将 它 们 创 建 成 MapperFactoryBean。

  要创建 MapperScannerConfigurer,可以在 Spring 的配置中添加如下代码:

  ```xml
  <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
    <property name="basePackage" value="org.mybatis.spring.sample.mapper" />
    <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory" />
      <!--以上这句在只有一个数据源时可省略,注意是value而不是ref-->
    <property name="annotationClass" value="org.springframework.stereotype.Repository"/>
      <!--以上这句可以省略，但最好写上，并要求DAO中要有@Repository注解-->
  </bean>
  ```

* basePackage 属性是让你为映射器接口文件设置基本的包路径。 你可以使用分号或逗号 作为分隔符设置多于一个的包路径。每个映射器将会在指定的包路径中递归地被搜索到。

* 注 意 ,**没 有 必 要 去 指 定 SqlSessionFactory 或 SqlSessionTemplate**, 因 为 MapperScannerConfigurer 将会创建 MapperFactoryBean, 之后自动装配。但是, 如果你使用了一个 以上的 DataSource ,那 么自动 装配可 能会失效 。这种情况下 ,你可以使用 sqlSessionFactoryBeanName 或 sqlSessionTemplateBeanName 属性来设置正确的 bean 名 称来使用。这就是它如何来配置的,注意 bean 的名称是必须的,而不是 bean 的引用, 因此, value 属性在这里替代通常的 ref:

  ```xml
  <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory" />
  ```

* MapperScannerConfigurer 支 持 过 滤 由 指 定 的 创 建 接 口 或 注 解 创 建 映 射 器 。 annotationClass 属性指定了要寻找的注解名称。 markerInterface 属性指定了要寻找的父 接口。如果两者都被指定了,加入到接口中的映射器会匹配两种标准。默认情况下,这两个 属性都是 null,所以在基包中给定的所有接口可以作为映射器加载。 

