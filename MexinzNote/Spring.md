# Spring

## IOC-inversion of control 控制反转

- 对象由程序本身创建，变为了程序接收对象

  eg：原来Service中会new一个dao对象，当service中需要更换dao对象时（如数据库发生改变），许多地方都要发生改动。而在service中添加一个setdao()方法，将dao对象交给客户端来决定，dao应声明为一个接口，set进去的是实现它的类，这样就可以实现程序接收对象

- 程序员主要精力集中于业务实现

- 实现了service和dao的解耦工作。service层和dao层实现了分离，没有直接依赖关系

- 如果dao的实现发生改变，应用程序本身不发生改变

- 对象由spring来创建、管理、装配

### 配置bean.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">  
 
   <!-- id是bean的标识符 要唯一  如果没有配置id，name默认标识符 
   		如果配置id，又配置了name 那么name是别名
   		name可以设置多个别名 分隔符可以 是 空格 逗号 分号
   		class是bean的全限定名=包名+类名
   		如果不配置 id,和name 那么可以根据applicationContext.getBean(Class) 获取对象
             									（只有一个这类对象的情况）
   -->
   <import resource="config/spring/entity.xml"/>
    <!-- bean就是java对象，由spring来创建和管理，scope取决了该对象是单例还是每用一次就创建一个新的或是别的方式 -->  
   <bean name="xxx" class="xxx.xxx.xxx.xx" scope="xx">
       <!--property为对象的属性，name/ref为属性名，必须与set方法后的那个单词一致，并把首字母改为小写，value为属性值-->
       <!--对象属性是spring容器来设置的，具有value属性值的是常规属性，而对象引用则是用ref属性，值为被引用对象的id属性-->
    	<property name="xxx" value="xxx"/>
    </bean>
</beans>
```

- 此时对应的测试类代码

  ```java
  //解析beans.xml文件，生成管理相应的bean对象
  ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
  //hello为beans.xml文件中配置的对应Hello POJO类的name/id属性
  Hello hello = (Hello) context.getBean("hello");
  //show为Hello对象中的一个方法
  hello.show();
  ```

- 上述过程叫做**控制反转**（依赖注入）：传统的应用程序的对象创建是由程序本身控制的。使用spring后，是由spring来创建对象。从而实现程序的解耦，**需要更改的时候不需要再去对源码进行修改，只需要修改配置文件即可**。

  （正转指程序来创建对象，反转指程序本身不去创建对象，变为被动接收对象）

- **控制反转**：以前对象是由程序本身来创建，使用spring后，程序变为被动接收spring创建好的对象

- **依赖注入**：POJO类**依赖**于它本身的属性，而现在它的属性，通过POJO类本身的set方法，由spring进行赋值，因此又被称为依赖注入

## 最底层的BeanFactory接口

- getBean() 获取配置给Spring IOC容器的Bean 参数类型是字符串/Class类型
- isSingleton() 判断是否单例，是则返回true，默认为创建单例，即返回true
- isPrototype() 和上面的相反，返回true则意味着当你从IOC容器中获取bean就生成一个新的实例
- getAliases() 获取别名的方法，参数name，返回String[ ]

## 使用IOC创建对象的3种方式（依赖注入）

- 通过无参的构造方法来创建

  ```xml
  <bean id="user" class="xxx.xxx.xxx.User">
  	<property name="name" value="张三"/>
  </bean>
  ```

  

- 通过有参的构造方法来创建(Bean.xml文件的配置有三种)

  1. 根据参数的下标来设置

  ```xml
  <bean id="user" class="xxx.xxx.xxx.User">
      <!--index指构造方法参数下标，下标从0开始-->
  	<constructor-arg index="0" value="李四"/>
  </bean>
  ```

  2. 根据参数的名称来设置

  ```xml
  <bean id="user" class="xxx.xxx.xxx.User">
      <!--name属性的值与对应POJO类的属性要一致-->
  	<constructor-arg name="name" value="李四"/>
  </bean>
  ```

  3. 根据参数类型来设置

  ```xml
  <bean id="user" class="xxx.xxx.xxx.User">
      <!--如果该POJO类中多个属性值类型相同，则会按照顺序进行赋值-->
  	<constructor-arg type="java.lang.String" value="李四"/>
  </bean>
  ```

- 通过工厂方法来创建对象

  1. 静态工厂

  - 示例：对应的工厂类代码

    ```java
    public class UserFactory{
        //必须是static
        public static User newInstance(String name){
            return new User(name);
        }
    }
    ```

  - bean.xml配置文件代码

    ```xml
    <!--此处的class写工厂类的完整包名+类名，factory-method属性写生成该POJO类对象的静态方法名称-->
    <bean id="user" class="xxx.xxx.xxx.UserFactory" factory-method="newInstance">
    	<constructor-arg index="0" value="王五"/>
    </bean>
    ```

  

  2. 动态工厂

  - 示例：对应的工厂类代码

    ```java
    public class UserDynamicFactory{
        //和静态工厂相比，没有static
        public User newInstance(String name){
            return new User(name);
        }
    }
    ```

  - bean.xml配置文件代码

    ```xml
    <bean id="userFactory" class="xxx.xxx.xxx.UserDynamicFactory"/>
    <!--下面的factory-bean必须与上面的bean的id属性的值相同-->
    <bean id="user" factory-bean="userFactory" factory-method="newInstance">
    	<constructor-arg index="0" value="王五"/>
    </bean>
    ```

## spring配置文件

- 设置别名：

  ```xml
  <!--设置别名，这样就可以在java代码中的getBean方法中使用别名，本例为可以使用user1来获取user对象-->
  <alias name="user" alias="user1"/>
  ```

- bean的配置：

  ```xml
  <!--id是bean的标识符，必须唯一 如果没有配置id，则name为默认标识符。如果配置了id，又配置了name，那么name是别名。通过name属性可以设置多个别名，分隔符可以是空格/,/; class是bean的全限定名=包名+类名。如果不配置id和name，那么可以根据applicationContext.getBean(Class)获取对象-->
  <bean id="h1" name="hello h2,h3;h4" class="xxx.xxx.xxx.Hello">
  	<property name="name" value="张三"/>
  </bean>
  ```

- 注：如果使用applicationContext.getBean(Class)获取对象，则在bean.xml配置文件中必须只能存在一个这个POJO类的**<bean>**，否则会报错，因为spring不能确定要获取的是哪一个对象

- 团队协作通过import来实现

  ```xml
  <!--引入xml文件，这样在java代码中也可以使用该xml文件中的配置，如bean中 的一些对象配置-->
  <import resource="xxx/xxx/xxx.xml"/>
  ```

## 依赖注入 Dependency Injection

### xml注入

1. - 依赖：指bean对象依赖于容器，bean对象的依赖资源
   - 注入：指bean对象依赖的资源由容器来设置和装配

2.    spring注入：

   - 构造器注入：见上面的ioc创建对象

   - **setter注入**：要求被注入的属性必须有set方法，set方法的方法名由set+属性首字母大写。如果属性是boolean，则没有get方法，默认生成的方法是is方法。

     1. 常量注入

        ```xml
        <bean id="student" class="xx.xxx.xx.Student">
        	<property name="name" value="张三丰"/>
        </bean>
        ```

     2. bean注入（对象注入）

        ```xml
        <bean id="addr" class="xx.xx.xx.Address">
        	<property name="address" value="工一"/>
        </bean>
        <!--这里的addr是一个地址对象，每个学生中包含一个地址对象-->
        <bean id="student" class="xx.xxx.xx.Student">
        	<property name="name" value="张三丰"/>
            <!--这个对象引用的name必须与上面的id相同-->
            <property name="addr" ref="addr"/>
        </bean>
        
        ```

     3. 数组注入

        ```xml
        <property name="books">
        	<array>
            	<value>Head first Java</value>
                <value>Core Java</value>
                <value>Effective Java</value>
            </array>
        </property>
        ```

     4. list注入

        ```xml
        <!--这里的list中存放的是String-->
        <property name="hobbies">
            <list>
            	<value>羽毛球</value>
                <value>乒乓球</value>
                <value>jazz</value>
            </list>
        </property>
        ```

     5. map注入

        ```xml
        <!--这里注入的map是<String,String>类型的-->
        <property name="cards">
        	<map>
                <!--有两种方式，上下两种的配置效果是一样的-->
            	<entry key="中国银行" value="12345"/>
                <entry>
                	<key><value>建设银行</value></key>
                    <value>1233454</value>
                </entry>
            </map>
        </property>
        ```

     6. set注入

        ```xml
        <property name="games">
        	<set>
            	<value>lol</value>
                <value>吃鸡</value>
                <value>炉石</value>
            </set>
        </property>
        ```

     7. null注入

        ```xml
        <property name="wife"><null/></property>
        ```

     8. properties注入

        ```xml
        <property name="info">
        	<props>
            	<prop key="学号">20180101</prop>
                <prop key="sex">男</prop>
                <prop key="name">小明</prop>
            </props>
        </property>
        ```

     9. p命名空间注入

        **需要在头文件中加入xmins:p="http://www.springframework.org/schema/p"**

        ```xml
        <!--这里的User有两个成员变量，分别是name和age，这样注入的效果和使用property标签注入是一样的，p代表的就是property，每个属性依旧要设置set方法才可以成功注入-->
        <bean id="user" class="xxx.xx.xxx.User" p:name="Shawn" p:age="19"/>
        ```

     10. c命名空间注入

         **需要在头文件中加入xmins:c="http://www.springframework.org/schema/c"**

         ```xml
         <!--这里指定的构造器初始化了name和age属性，c代表constructor,这种注入要求POJO类中一定要有对应参数的构造方法-->
         <bean id="u1" class="xxx.xx.xxx.User" c:name="Taylor" c:age="26"/>
         ```

### 注解注入

1. @Component("xxx") 标注在类上，相当于xml中bean的id

2. @Value("xxx") 标注在成员变量上，为值的注入

3. @ComponentScan 标注在需要使用到@Component标注的类的类上，默认是扫描当前包的路径

   它还有两个配置项，一个是basePackages（一个Java包数组，根据其配置扫描对应的包和子包）,一个是basePackageClasses（配置多个类，根据这些类所在的包，为包和子包扫描装配对应位置的bean）

- 但是这样的类Spring IoC不知道要去哪里扫描对象，需要另外一个Config类告诉它

  ```java
  package com.xxx.xxx.....//这个包名必须和相应的POJO类保持一致，或者使用下面注解的配置项告诉编译器要扫描的包名，不建议使用包数组，因为编译器没有任何提示
      
  @ComponentScan 
  //这个注解代表扫描，默认是当前包的路径，POJO的包名和它保持一致才能扫描，否则是没有的
  public class PojoConfig{
      //这个类很简单，几乎没有逻辑
  }
  ```

- 测试类

  ```java
  public static void main(String[] agrs){
      ApplicationContext context = new AnnotationConfigApplicationContext(PojoConfig.class);
      Role role = context.getBean(Role.class);
  }
  ```

- @Autowired自动装配 一般用于对象引用，也允许用来配置方法。可以用setter和构造方法上

- @Primary 当成员变量有一个是接口时，而它又对应着多个实现类，这个时候使用Autowired自动装配会失败，因为Spring IOC不知道把哪个对象装配进来（它是按类型装配对象的），因此，在该接口的实现类上使用@Primary注解，表示**优先使用该类注入**

- @Qualifier 在上述所说的接口成员变量上加上这个注解，使用方法为@Qualifier("xxx")，xxx为该实现类的别名（即该实现类的@Component括号中的名字），这时IoC容器会使用名称的方式注入而不是类型，从而知道将哪一个实现类装配进来

- @AutoWired和@Qualifier同样可以用在构造方法的参数上（通常是对象引用参数）

- @Bean 注解到方法上（不能注解在类上），并且将方法返回的对象作为Spring的Bean（因为在引用第三方的jar包时，往往没有这些包的源码，无法为这些包的类加入@Compenent注解，让他们变成开发环境的Bean），它也可以使用@AutoWired和@Qualifier装配到别的Bean中。

  @Bean 还有四个配置项：

  1. name：一个字符串数组，允许配置多个BeanName
  2. autowire：标志是否一个引用的Bean对象，默认值是Autowire.NO
  3. initMethod ：自定义初始化方法（可以用注解实现生命周期中的一些自定义的初始化方法）
  4. destroyMethod：自定义销毁方法（可以用注解实现生命周期中的一些自定义的销毁方法）

- @ImportResource({"classPath:xxxxx.xml"，...}) 配置的内容是一个数组，也就是可以引入多个xml文件，通过这种方法**在注解体系中引入xml文件中配置的bean**

- @Import({xxx.class,xxx.class.....}) 导入多个配置类（有时候所有配置都放一个类里会混乱）

- @Configuration标注在类上，相当于把该类作为spring的xml配置文件中的`<beans>`，作用为：配置spring容器(应用上下文) 

- @Profile（“xxx”） 可以定义两个不同的数据库连接池，xxx以该数据库的用途来取名，如dev表示用于开发，test表示用于测试，下面是以xml文件的配置方法

  ```xml
  <beans profile="test"> 	<!--用于开发则profile="dev"-->
  	<bean id="dataSource" class="xxx..">
       	<property name="driverClassName" value="com.mysql.jdbc.Driver" />
          	....
      </bean>
  </beans>
  ```

  在测试类上使用@ActiveProfile("xxx") 用于指定加载哪一个Profile

## bean作用域

- 每个**<bean>**标签中都有一个scope属性，这就是bean的作用域，分为以下6种
  1. singleton：**单例**，也是默认的作用域。和正常的单例不太一样，它是通过一个计数器去记录的。整个容器中只有一个对象实例，每次访问的都是同一个对象
  2. prototype：**原型**。每次获取bean都产生一个新的对象（在整合struts2和spring时，需要将action设为scope="prototype"）
  3. request：**请求**。每次请求时创建一个新的对象 在web应用中使用
  4. session：**会话**。在一个会话中spring只创建一个实例（指的是http那个session，不是mybatis那个）在web应用中使用 
  5. globalSession：只在portlet下有用，表示是application 只适用于web应用 
  6. application：在整个应用范围中只有一个对象

- 使用@Scope(ConfigurableBeanFactory.xxx) 配置在类上，表示创建该类实例时的作用域--------单例/原型

  @Scope(value = WebApplicationContext.xxx, proxyMode = ScopedProxyMode.INTERFACES) -------请求/会话

## bean自动装配

- 在**<bean>**中有autowire这个属性，可以使用该属性设置该bean中的属性进行自动装配，**从而简化spring配置文件**，不用自己在xml文件中写**<property>**进行装配（只能用于对象引用的成员变量的装配，因为对象引用的成员变量会在bean.xml文件中有自己的bean）
  1. autowire="**byName**" 根据名称（set方法名后面那个），在bean.xml文件中去查找相应的bean（id值和刚刚set方法名相同），如果有则装配上
  2. autowire="**byType**" 根据类型进行自动装配，不用管bean的id，**但是该类型的bean只能有一个**（容易忽略，不建议使用byType）
  3. autowire="**constructor**" 当通过构造器实例化bean时，使用byType的方式装配构造方法
  4. autowire="**no**" 不使用自动装配
- 如果觉得在每个**<bean>**中写这个autowire属性麻烦，可以在bean.xml文件的上面的**<beans>**全局设置**default-autowire**
- 不推荐使用自动装配，而使用**annnotation**（注解）

## 静态代理

### 角色

- 抽象角色：一般使用接口或者抽象类来实现
- 真实角色：被代理的角色（如房东）
- 代理角色：代理真实角色（如房屋中介），在代理真实角色后，会做一些附属的操作
- 客户：使用代理角色来进行一些操作

### 代码实现

- 示例：房东 中介 租房者 三者的关系

- 抽象角色 Rent.java

  ```java
  public interface Rent {
      void rent();
  }
  ```

- 真实角色 Host.java

  ```java
  public class Host implements Rent{
      public void rent() {
          System.out.println("rent house!");
      }
  }
  ```

- 代理角色 Proxy.java

  ```java
  public class Proxy implements Rent{
      private Host host;
      
      //租房
      public void rent() {
          seeHouse();
          host.rent();
          fare();
      }
      //看房
      private void seeHouse() {
          System.out.println("take the customer to see the house");
      }
      //收中介费
      private void fare() {
          System.out.println("ask the customer to pay the fare");
      }
  
      public Proxy() {
      }
      public Proxy(Host host) {
          this.host = host;
      }
      public void setHost(Host host) {
          this.host = host;
      }
  }
  ```

- 客户角色 Client.java

  ```java
  public class Client {
      public static void main(String[] args) {
          Host host = new Host();
          Proxy proxy = new Proxy(host);
          proxy.rent();
      }
  }
  ```

### 使用静态代理的优点

- 使得真实角色处理的业务更加纯粹，不再去关注一些公共的事情
- 公共的业务由代理来完成，实现了业务的分工
- **公共业务发生修改或扩展时，变得更加集中和方便。**不使用静态代理，一旦公共的业务发生改变，这样真实角色的每个方法都要一个个去改。而使用静态代理后，只需要在proxy的公共业务方法中修改就可以了。

### 使用静态代理的缺点

- 类多了。一个Service类就要写一个代理类，工作量变大。开发效率降低 

---

## 动态代理	

### 和静态代理的相同点

- 动态代理和静态代理的角色是一样的

### 和静态代理的不同点

- 动态代理的代理类是动态生成的，而静态代理的代理类是我们自己写死的

### 分类

1. 基于接口的动态代理：jdk动态代理
2. 基于类的动态代理：cglib

- 现在一般使用 **javassist** 来生成动态代理

  ps：javassist是jboss的一个子项目，主要的优点为简单，快速。直接使用java编码的形式，而不需要了解虚拟机指令，就能动态改变类的结构 ，或者动态生成类

### jdk动态代理

- Proxy类：提供用于创建动态代理类和实例的静态方法，它还是由这些方法创建的所有动态代理类的父类。（位于java.lang.reflect包中）

  - 创建静态代理类的静态方法，返回一个指定接口的代理类实例，该接口可以将方法调用指派到指定的调用处理程序

    **static Object newProxyInstance (ClassLoader loader, Class<?>[ ] interfaces, InvocationHandler h)**

    - loader 定义代理类的类加载器
    - interfaces 代理类要实现的接口列表
    - h 指派方法调用的调用处理程序

- InvocationHandler接口：是**代理类实例**的*调用处理程序* 实现的接口。每个代理实例都具有一个关联的调用处理程序。对代理实例调用方法时，将对方法调用进行编码并将其指派到它的调用处理程序的 invoke 方法

- 动态代理具有静态代理的所有优点，并避开了静态代理的缺点

- **一个动态代理一般代理某一类业务，一个动态代理可以代理多个类**

- 代码实现：

  ```java
  public class ProxyInovationHandler implements InvocationHandler {
      private Object target; //目标对象--真实对象
  
      public void setTarget(Object target) {
          this.target = target;
      }
      public Object getProxy() {
          return Proxy.newProxyInstance(this.getClass().getClassLoader(),target.getClass().getInterfaces(),this);
      }
      /**
       * @param proxy 代理类
       * @param method 代理类的调用处理程序的方法对象
       * @param args
       * @return
       * @throws Throwable
       */
      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
          log(method.getName());
          Object result = method.invoke(target,args);
          return result;
      }
  
      private void log(String methodName) {
          System.out.println("执行"+methodName+"方法");
      }
  }
  ```

- Client：

  ```java
  public class Client {
      public static void main(String[] args) {
          UserService userService = new UserServiceImpl();
          ProxyInovationHandler handler = new ProxyInovationHandler();
          handler.setTarget(userService);
          UserService proxy = (UserService) handler.getProxy();
          proxy.add();
      }
  }
  ```

## AOP面向切面编程 

- aspect oriented programming 面向切面编程

- aop在spring中作用：

  1. 提供声明式服务（声明式事务）
  2. 允许用户实现自定义切面

- 在不改变原有代码的情况下，增加新的功能

- 传统编程：JSP-->Action-->service-->dao 是纵向的编程

  AOP编程模式：不改变原有代码就可以拓展公共业务（通过代理来实现），是横向切入的，因此是横向的编程	

- 好处：

  1. 使得真实角色处理的业务更加纯粹，不再去关注一些公共的事情
  2. 公共的业务由代理来完成，实现业务的分工
  3. 公共业务发生拓展时变得更加集中和方便

- 名词解释：

  - **关注点 Joinpoint** ：增加的某个公共业务。如日志、安全、权限、缓存、事务、异常处理等。一个关注点可以横切多个对象（一般是同一类的，如xxxService）
  - **切面 Aspect**：一个关注点的模块化。将一个关注点单独写成一个类封装起来
  - **通知 Advice**：在切面的某个特定连接点上执行的动作
  - **织入 Weaving**：把切面连接到其他的应用程序类型或者对象上，并创建一个被通知的对象

### 使用spring实现aop

#### 	通过springAPI实现

- 将目标的公共业务写成一个类，而原本的业务类不需要改动，这个业务类就是上面的**关注点**的内容

- 示例：下面的api是spring给我们提供的，直接用就好

  ```java
  //Log.java 前置通知
  public class Log implements MethodBeforeAdvice {
      /**
       * @param method 要执行的目标对象的方法（被调用的方法对象）
       * @param args 被调用的方法的参数
       * @param target 目标对象（被代理的）
       * @throws Throwable
       */
      @Override
      public void before(Method method, Object[] args, Object target) throws Throwable {
          System.out.println(target.getClass().getName()+"的"+method.getName()+"方法被执行");
      }
  }
  ```

  ```java
  //AfterLog 后置通知
  public class AfterLog implements AfterReturningAdvice {
      /**
       * 目标方法执行后执行的通知
       * @param returnValue 返回值
       * @param method 被调用的方法对象
       * @param args 被调用的方法对象的参数
       * @param target 目标对象（即具有这个方法的被代理对象）
       * @throws Throwable
       */
      @Override
      public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
          System.out.println(target.getClass().getName()+"的"+method.getName()+"方法被执行 "+"返回值是："+returnValue);
      }
  }
  ```

  ```java
  //目标类，不是下面这个接口，是实现了这个接口的一个类
  //在spring中可以不写这个接口，直接写类就可以
  //如果是直接使用jdk的动态代理，则一定要写这个接口
  public interface UserService {
      void add();
      void update(int a);
      void delete();
      void select();
  }
  ```

  ```xml
  <!--spring的配置文件-->
  <?xml version="1.0" encoding="UTF-8" ?>
  <beans xmlns="http://www.springframework.org/schema/beans"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns:aop="http://www.springframework.org/schema/aop"
         xsi:schemaLocation="http://www.springframework.org/schema/beans
         http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
         http://www.springframework.org/schema/aop
         http://www.springframework.org/schema/aop/spring-aop-3.0.xsd">
  
          <bean id="userService" class="com.practice.service.UserServiceImpl"/>
          <bean id="log" class="com.practice.aop.Log"/>
          <bean id="AfterLog" class="com.practice.aop.AfterLog"/>
          <aop:config>
              <!--第一个星号表示所有返回值，第二个星号表示该类下的所有方法，括号中的两个点表示所有参数都适用 -->
              <aop:pointcut id="pointcut" expression="execution(* com.practice.service.UserServiceImpl.*(..))"/>
              <aop:advisor advice-ref="log" pointcut-ref="pointcut"/>
              <aop:advisor advice-ref="AfterLog" pointcut-ref="pointcut"/>
          </aop:config>
  </beans>
  ```

  ```java
  //测试类
  public static void main(String[] args) {
          ApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml");
          UserService userService = (UserService) ac.getBean("userService");
          userService.update(2);
      }
  ```

  

- 写好bean.xml配置文件就可以

  **具体实现代码见E盘spring项目**

- spring的底层是通过动态代理去实现的



#### 	自定义类实现

- 将同一类的公共业务写在一个类中，这个类里面包含这类公共业务的许多方法。

  ```java
  //Log.java 公共的日志业务
  public class Log {
      public void before() {
          System.out.println("-----方法执行前-----");
      }
  
      public void after() {
          System.out.println("-----方法执行后-----");
      }
  }
  ```

- 领域业务

  ```java
  //目标类
  public class UserServiceImpl implements UserService {
      @Override
      public void add() {
          System.out.println("-----添加用户数据-----");
      }
  
      @Override
      public int delete() {
          System.out.println("-----删除用户数据-----");
          return 1;
      }
  }
  ```

- bean.xml配置文件

  ```xml
  <bean id="userService" class="com.practice.aop2.UserServiceImpl"/>
  <bean id="log" class="com.practice.aop2.Log"/>
  <aop:config>
      <!--这个ref属性表和上面的id相同，表示公共业务就是上面配置的那个类里面的方法	-->
      <aop:aspect ref="log">
          <!--执行execution里面的所有方法都会调用这个公共业务-->
          <aop:pointcut id="pointcut" expression="execution(* com.practice.aop2.*.*(..))"/>
          <!--下面两个method为我们在Log公共业务类中自己定义的方法，pointcut-ref和上面pointcut的id相同，表示这些公共业务的切入点是上面pointcut指定的地方（expression），执行这些地方的方法时就会在前面插入下面before中指定的方法，after同理-->
          <aop:before method="before" pointcut-ref="pointcut"/>
          <aop:after method="after" pointcut-ref="pointcut"/>
      </aop:aspect>
  </aop:config>
  </beans>#### 
  ```

  

#### 通过注解实现

- 示例：Log.java 公共的日志业务

```java
//aspect表示它是一个切面
//当有多个切面时，使用@Order(number),表示切面的切入顺序，先进后出
@Aspect
public class Log {
    
    //如果不想写很多次execution这个正则表达式，可以定义一个空方法，上面用@Pointcut("execution(...)") 后面的@Before等等就可以直接@Before("空方法名()")
    
    //使用注解也要告诉它切入点，即括号里面的位置
    //如果需要给通知传参，则在下面的excution这一串字符串后+"&&args(name1,name2,...)",然后在通知的方法参数中对应声明 类型+参数名，参数名和name1，name2对应
    @Before("execution(* com.practice.aop3.*.*(..))")
    public void before() {
        System.out.println("-----方法执行前-----");
    }

    @After("execution(* com.practice.aop3.*.*(..))")
    public void after() {
        System.out.println("-----方法执行后-----");
    }

    //Around注解表示在这里执行目标方法并写一些环绕目标方法的操作
    @Around("execution(* com.practice.aop3.*.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //在before前执行
        System.out.println("环绕前");
        System.out.println("方法签名为："+ joinPoint.getSignature());
        //执行目标方法
        Object o = joinPoint.proceed();
        //在after前执行
        System.out.println("环绕后");

        return o;
    }
    
}
```

- bean.xml配置文件

```xml
<!-- 要加上aop命名空间-->
<bean id="userService" class="com.practice.aop3.UserServiceImpl"/>
<bean id="log" class="com.practice.aop3.Log"/>
<aop:aspectj-autoproxy/>
<!--表示开启AOP代理自动配置-->

<!--如果使用注解配置，则在类上@EnableAspectJAutoProxy-->
```

![image](E:\2018summer copy\MexinzNote\微信图片_20180726215240.png)

![image](E:\2018summer copy\MexinzNote\微信截图_20180727105633.png)

### Spring aop的理解

- Spring aop就是将公共的业务（如日志、安全、缓存、事务、异常处理等）和领域业务相结合。当执行领域业务时将会把公共业务加进来。实现**公共业务的重复利用**，使**领域业务更加纯粹**。程序员只需要专注于领域业务。其本质还是**动态代理**

### 异常通知

- 它有很多种写法，afterThrowing方法中前面为可变参数，必须要有的是后面的Exception参数，只要是实现了Throwble接口的异常都可以（可以自定义异常去实现 throwable 接口）

  ```java
  public class ExceptionLog implements ThrowsAdvice {
      //这个接口没有必须要实现哪些方法，像serializable接口一样，是一个标志性的接口
  
      //Exceptin参数前还可以有很多别的参数，也可以没有
      public void afterThrowing(Method m ,Exception ex) throws Throwable {
          //code
      }
  }
  ```

  

## Spring和Mybatis整合

1. 第一种方式
   - bean4.xml的配置文件（Spring的配置文件）

```xml
<!--配置数据源，mybatis的配置文件就可以不用配了-->
<bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
    <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
    <property name="url" value="jdbc:mysql://localhost:3306/mybatis01?useSSL=false"/>
    <property name="username" value="root"/>
    <property name="password" value="justinsgf5203!"/>
</bean>

<!--配置sqlSessionFactory-->
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource"/>
    <!--conf.xml是mybatis的配置文件-->
    <property name="configLocation" value="conf.xml"/>
</bean>
<!--通过构造器注入-->
<bean id="sqlSessionTemplate" class="org.mybatis.spring.SqlSessionTemplate">
    <constructor-arg index="0" ref="sqlSessionFactory"/>
</bean>
<!--这个sqlsession是sqlSessionTemplate类的一个对象-->
<bean id="userDao" class="com.practice.daoimpl.UserDaoImpl">
    <property name="sqlSession" ref="sqlSessionTemplate"/>
</bean>
```
- mybatis的配置文件

  ```xml
  <configuration>
      <!--别名-->
      <typeAliases>
          <package name="com.practice.vo"/>
      </typeAliases>
      <!--这里的示例只有一个usermapper-->
       <mappers>
          <mapper resource="UserMapper.xml"/>
      </mappers>
  </configuration>
  ```

- dao接口

  ```java
  public interface UserMapper {
      List<User> listUser();
  }
  ```

- dao实现类

  ```java
  public class UserDaoImpl implements UserMapper {
      private SqlSessionTemplate sqlSession;
  
      @Override
      public List<User> listUser() {
          return sqlSession.getMapper(UserMapper.class).listUser();
      }
  
      public void setSqlSession(SqlSessionTemplate sqlSession) {
          this.sqlSession = sqlSession;
      }
  }
  ```

- 测试类

  ```java
   @Test
  public void listUser() {
      ApplicationContext context = new ClassPathXmlApplicationContext("bean4.xml");
      UserMapper userMapper = (UserMapper) context.getBean("userDao");
      //14是数据库中的数据条数
      Assert.assertEquals(14,userMapper.listUser().size());
  }
  ```

2. 第二种方式

   - bean4.xml的配置文件

   ```xml
   <!--配置数据源，mybatis的配置文件就可以不用配了-->
   <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
       <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
       <property name="url" value="jdbc:mysql://localhost:3306/mybatis01?useSSL=false"/>
       <property name="username" value="root"/>
       <property name="password" value="justinsgf5203!"/>
   </bean>
   
   <!--配置sqlSessionFactory-->
   <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
       <property name="dataSource" ref="dataSource"/>
       <!--conf.xml是mybatis的配置文件-->
       <property name="configLocation" value="conf.xml"/>
   </bean>
   
   <!--在spring配置文件中，不需要管理sqlSessionTemplate，在dao的实现中，需要继承SqlSessionDaoSupport，然后直接getSqlSession()来获取sqlSession对象-->
   <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
       <property name="dataSource" ref="dataSource"/>
       <property name="configLocation" value="conf.xml"/>
   </bean>
   <bean id="userDao" class="com.practice.daoimpl.UserDaoImpl">
       <property name="sqlSessionFactory" ref="sqlSessionFactory"/>
   </bean>
   ```

   - dao类

   ```java
   //记得要继承 SqlSessionDaoSupport
   public class UserDaoImpl extends SqlSessionDaoSupport implements UserMapper {
       @Override
       public List<User> listUser() {
           UserMapper mapper = getSqlSession().getMapper(UserMapper.class);
           return mapper.listUser();
       }
   }
   ```

   - 测试类不变

3. 第三种

   - bean.xml文件

     ```xml
     <!--配置sqlSessionFactory-->
     <!--这里变化的是下面的property，不再需要mybatis的配置文件，将所有的配置信息配置到spring的配置文件中，别忘记把Mapper.xml里面的别名改了，不然会找不到类-->
     <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
         <property name="dataSource" ref="dataSource"/>
         <property name="mapperLocations" value="UserMapper.xml"/>
     </bean>
     ```

     

## 声明式事务管理

- 简单来说就是把几次对数据库的操作绑定在一起(大的包小的)，只有全部执行成功了，才会修改数据库中的数据

- 比如银行转账，就需要事务管理

- 在bean.xml文件开头的**<beans>**中加入如下命名空间

  ```xml
  xmlns:tx="http://www.springframework.org/schema/tx"
  ```

  在xsi:schemaLocation中加入

  ```xml
   http://www.springframework.org/schema/tx
   http://www.springframework.org/schema/tx/spring-tx-3.0.xsd
  ```

  bean.xml文件，**主要看声明式事务配置部分**，还有不需要管理的sqlSessionFactory部分

  ```xml
  <!--配置数据源-->
  <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
      <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
      <property name="url" value="jdbc:mysql://localhost:3306/mybatis01?useSSL=false"/>
      <property name="username" value="root"/>
      <property name="password" value="justinsgf5203!"/>
  </bean>
  
  <!--配置sqlSessionFactory-->
  <!--在spring配置文件中，不需要管理sqlSessionTemplate，在dao的实现中，需要继承SqlSessionDaoSupport，然后直接getSqlSession()来获取sqlSession对象-->
  <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
      <property name="dataSource" ref="dataSource"/>
      <property name="configLocation" value="conf.xml"/>
  </bean>
  <bean id="userDao" class="com.practice.daoimpl.UserDaoImpl">
      <property name="sqlSessionFactory" ref="sqlSessionFactory"/>
  </bean>
  
  <!--声明式事务配置 开始-->
  <!--配置事务管理器-->
  <bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
      <property name="dataSource" ref="dataSource"/>
  </bean>
  <!--配置事务通知-->
  <tx:advice id="txAdvice" transaction-manager="txManager">
      <tx:attributes>
          <!--这里是配置那些方法使用什么样的事务，配置事务的传播特性-->
          <!--required表示每次使用该方法时，看看当前是否有事务，如果有就用当前事务  
                  没有则开启一个新的事务，增删改都是配置成这个（最常用的）-->
          <!--add加星号表示所有以add开头的方法都遵循这个配置，如果name只有一个星号表示所有方法都适用-->
          <tx:method name="add*" propagation="REQUIRED"/>
          <tx:method name="delete" propagation="REQUIRED"/>
          <tx:method name="list*" propagation="REQUIRED"/>
          <!--<tx:method name="*" propagation="REQUIRED"/>-->
          <!--把查询的操作配置为只读，就不会提交事务，如果使用这个方法对数据库中的数据进行修改，就会报错-->
          <tx:method name="get" read-only="true"/>
      </tx:attributes>
  </tx:advice>
  <aop:config>
      <!--正常的话execution下应该配service包，service包里的东西才是负责业务的，这里配dao只是因为没写service-->
      <aop:pointcut id="pointcut" expression="execution(* com.practice.daoimpl.*.*(..))"/>
      <aop:advisor advice-ref="txAdvice" pointcut-ref="pointcut"/>
  </aop:config>
  <!--声明式事务配置 结束-->
  ```

- 配置注解驱动 : 在上述配置文件 加入如下配置就可以使用＠Transactional 配置事务了

  ```xml
  < tx:annotation-driven transaction-manager= ” transactionManager” />
  ```

  

