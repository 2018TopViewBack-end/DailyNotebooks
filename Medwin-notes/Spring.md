

##  Spring简介

spring是一个轻量级**容器框架**，它可以管理web层、业务层、dao层、持久层，用于**配置bean并维护bean之间关系**

### bean

spring中一个重要概念，可以是**java中任何一种对象**，如javabean/service/action/dao/数据源

Spring中由BeanFactory构建，它是ApplicationContext的父类

### IOC

- inverse of control **控制反转**-->依赖注入

一种思想。对象由**spring**创建、管理和装配，应用程序不用管具体实现（被动地接收对象），可达到**解耦**作用。service层不用管dao层具体实现，完全分离。如果dao的实现发生改变，应用程序本身不改变

在HiSpringIoc例子中，HiSpringIoc对象由Spring容器创建而不是程序本身，对象属性由Spring容器设置(配置文件中)，这个过程就叫控制反转

### DI

dependency injection 依赖注入

即容器设置属性值的过程

### AOP

面向切面编程

### 事务

jdbc等

## Spring开发步骤

1. 引入maven的**spring依赖**

2. 创建核心文件applicationContext.xml，一般放在src下

   在该文件中**引入xsd**文件，其中**bean元素**的作用是，当spring加载的时候，会自动地**创建bean对象**并放入内存（反射机制）

3. 得到spring的applicationContext对象（容器对象）

   ```java
   Applicationcontext ac = new ClassPathXmlApplicationContext(“aplicationcontext.xml);
   Instance newInstance = ac.getBean("beanId");
    //只需一个对象时，beanId可直接写为类.class，不用强转
   ```

   

4. 

##  IOC创建对象的方式

1. 通过无参构造方法

2. 通过有参构造方法 并用<constructor-arg>

3. 通过两种工厂方法：静态工厂、动态工厂（区别为工厂方法是否为静态）

   **静态**：<bean>中有factory-method参数，指定工厂类中创建新对象的方法

   **动态**：在产品<bean>中加factory-bean参数

## 配置文件

- <bean> : **id** 唯一/**name** 可有多个/**class**--bean的全限定名(包名＋类名)，若没有配置id，name默认为标识符

  property属性的ref(引用对象)值要与setter的名字相同

- <constructor-arg> : 有参方法构造对象。

  **index**="0" 指调用第一个构造方法，**value**传值。

  或用**name**指定构造器名、**type**指定参数类型

- <alias> 为bean设置**别名**，也可直接在bean的name中设置多个值（用逗号或空格隔开均可）

- <import> 团队协作开发，导入多个配置

## 依赖注入 DI

依赖：指bean对象的创建依赖于容器。

注入：bean对象的依赖资源(ref/value)由容器来设置和装配

### spring注入：

#### 	构造器注入

​	见ioc创建对象

#### 	setter注入（重要）

​	要求被注入属性必须有set方法。方法名为set+属性首字母大写 

​	常量、bean、数组等对应bean的属性类型

​	a) **常量注入**

```xml
<bean id="student" class="cn.medwin.Student">
    <property name="name" value="new student"/>
</bean>
```

​	b)**bean注入**（bean中有bean）

```xml
<bean id="student" class="cn.medwin.Student">
    <property name="name" value="new student"/>
    <property name="addr" ref="addr"/>
    <!-- 引入studetn的属性addr类 -->
</bean>
```

​	c)**数组注入**

```xml
<property name="books">
	<array>
    	<value>《易经》</value>
        <value>《不易经》</value>
    </array>
</property>
```

​	d)**List注入**

```xml
<property name="hobbies">
	<list>
    	<value>骑行</value>
        <value>开飞机</value>
    </list>
</property>
```

​	e)**Map注入**

```xml
<property name="cards">
	<map>
    	<entry key="中国银行" value="654898976432"/>
    </map>
</property>
```

​	d)**null注入**

```xml
<property name="wife">
    <null/>
</property>
```

​	e)**Properties注入**

```xml
<property>
    <props>
    	<prop key="num">3117000001</prop>
        <prop key="sex">female</prop>
        <prop key="name">Mary</prop>
    </props>
</property>
```

 	f)**p命名空间 p-namespace**注入

​		和c注入需要加xmlns

```xml
<bean id="user" class=""cn.medwin.user" p:name="xxx" p:age="xxx">
```

​	g)**c注入** : 构造方法注入。要求有对应参数的构造方法

 ... 

## bean的作用域

scope：

- singleton--**单例**  只产生一个对象 (**默认**)
- prototype--**原型**  每次获取bean都产生新的对象
- request  每次请求时创建一个新的对象
- session  会话范围内使用同一个对象
- global session  只在portlet下有用，表示是application 
- application  在整个ServletContext范围内(和prototype的区别？)

## bean自动装配

（不推荐使用，而使用annotation）

简化spring配置

- **byName根据名称**（setter方法名称对应的名字）查找相应的bean
- **byType根据类型** (不要使用)   不用管属性bean的id，但同一种类型的bean只能有一个
- **constructor构造器**--当通过构造器实例化bean时使用byType方式装配构造方法



可在每个bean中配也可在文件头加上:default-autowire="byName"

##  代理

### 静态代理

角色分析：

​	抽象角色：一般使用接口或抽象类来实现

​	真实角色：被代理的角色

​	代理角色：代理真实角色并做一些附属操作

​	客户：使用代理角色来进行一些操作

- 类比房主、中介、房客

#### 优点

使真实角色处理的业务更加纯粹，公共的业务来代理来完成--实现了业务的分工

公共业务扩展变得更加集中和方便哦🙂

#### 缺点

多了代理类，工作量变大，开发效率降低

### 动态代理

- 代理类动态生成，可以代理多个类

- 一个动态代理一般代理某一类业务

  发展：基于接口(jdk动态代理)-->基于类-->javassist生成动态代理（now）

#### jdk动态代理 （只能代理接口）

​	Proxy类和InvocationHandler接口

```java
//代理类
public class ProxyInovationHandler implements InvocationHandler{
    private Rent rent;
    /*
      proxy是代理类
      method是代理类的调用处理程序的方法对象
    */
    public void setRent(Rent rent){
        this.rent = rent;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = method.invoke(rent, args);
        return result;
    }
    
    /*
      生成代理类
    */
    public Object getProxy(){
        return
  Proxy.newProxyInstance(this.getClass().getClassLoader(),rent.getClass().getInterface(),this);
    }
}

//客户类
	...{
	Host host = new Host();
	ProxyInovationHandler pih = new ProxyInovationHandler();
	pih.setRent(host);
	Rent proxy = (Rent)pih.getProxy();//生成代理类
	proxy.rent();
 }
```



## AOP -- 面向切面编程

​	aspect oriented programming--面向切面编程。本质是动态代理

### 在spring中作用

- 提供声明式服务（声明式事务）
- 允许用户实现自定义切面
- 有利于在不改变原有代码情况下，增加新的功能（通过代理），使领域业务更加纯粹

### 优点

使真实角色处理的业务更加纯粹，公共的业务来代理来完成--实现了业务的分工

公共业务扩展变得更加集中和方便哦🙂（即代理优点）

### 几个概念

**关注点：** 关注的业务，如日志、安全、缓存、事务、异常等

**切面 aspect：**一个关注点的模块化（封装业务类）

**连接点 joinpoint：**程序执行中某个特定的点。一个连接点总是表示一个方法的执行（连接公共业务和领域业务）

**通知 advice：**在切面的某个特定的连接点上执行的动作。（目标方法周围的操作）

- 注意其中的ThrowsAdvice异常通知接口

**织入：**...

### 使用spring实现AOP的三种方式

1. **通过springAPI实现**  代理类和接口均可。

    spring的aop就是把动态代理封装了，不用编写者自己写动态代理，只需配置

    **需要在xml头文件 中导入新的命名空间 添加xsd**


   ```xml
   http://www.springframework.org/schema/aop
            http://www.springframework.org/schema/aop/spring-aop.xsd 
   <!-- 在service中的add方法加入日志功能 --> 
   <!-- 把方法名或包名改成"*"表示对所有方法、类应用(方法的括号保留) -->
   <!-- 括号中加".."表示应用到所有方法(所有类型的参数) -->
   	<bean id="userService" class="cn.medwin.service..."/>
   	<bean id="log" class="cn.medwin.log.Log"/>
       <aop:config>
           <!-- *号表示所有返回值 -->
           <aop:pointcut expression="execution(* 					   cn.medwin.service.UserServiceImpl.add())" id="pointcut1"/>
           <aop:advisor advice-ref="log" pointcut-ref="poingtcut1">
       </aop:config>
   ```

   ```java
   
   //log类 前置通知
   
   public class Log implements MethodBeforeAdvice {
   
       /**
        *
        * @param method 被调用的切入点的方法对象
        * @param objects 被调用方法的参数
        * @param o target 被调用方法的目标对象（被
        * @throws Throwable 
        */
       @Override
       public void before(Method method, Object[] objects, Object o) throws Throwable {
           System.out.println(o.getClass().getName()+"的"+method.getName()+"方法被执行");
       }
   }
   ```

   ```java
   
   //后置通知 需实现AfterReturningAdvice接口
   
   public class Log implements AfterReturningAdvice {
       /**
        *
        * @param o 返回值
        * @param method 被调用的方法对象
        * @param objects 被调用的方法的参数
        * @param o1 被调用的方法的目标对象
        * @throws Throwable
        */
       @Override
       public void afterReturning(Object o, Method method, Object[] objects, Object o1) throws Throwable {}
   }
   ```

2. **通过自定义类实现AOP**

   ```xml
       <bean id="userService" class="cn.medwin.service..."/>
       <bean id="log" class="cn.medwin.log.Log"/>
       <aop:config>
           <aop:aspect ref="log">
               <aop:pointcut expression="execution(* cn.medwin.service.*.*(..))" id="pointcut"/>
               <aop:before method="before" poingt-ref="pointcut"/>
               <aop:after method="after" poingt-ref="pointcut"/>
           </aop:aspect>
       </aop:config>
   <!-- Log类中有before和after方法 -->
   ```

3. #### 通过注解实现--autoproxy

   ```java
   
   // 在第二种方法的基础上，在Log类加上@Aspect注解，在方法上加上@Before、@after等 
   
   @Aspect
   public class Log {
       
   	@Before("execution(* cn.sxt.service.impl.*.*(..))")
   	public void before(){
   		System.out.println("-----方法执行前-----");
   	}
       
   	@After("execution(* cn.sxt.service.impl.*.*(..))")
   	public void after(){
   		System.out.println("-----方法执行后-----");
   	}
       
   	@Around("execution(* cn.sxt.service.impl.*.*(..))")
   	public Object aroud(ProceedingJoinPoint jp) throws Throwable{
   		System.out.println("环绕前");
   		System.out.println("签名："+jp.getSignature());//目标方法的签名
   		 Object result = jp.proceed();//执行目标方法并取得返回值
   		System.out.println("环绕后");
   		return result;
   	}
   }
   //执行结果：
   环绕前
   签名：int cn.medwin.service.UserService.delete()
   -----方法执行前-----    
   -----删除用户数据---
   环绕后
   -----方法执行后-----
   ```

   xml中aop直接改为：

   ```xml
   
   <aop:aspectj-autoproxy/>
   ```

   

## Spring整合MyBatis

1. 导入mybatis-spring包

2. 编写配置文件

3. 实现。多种方式

   video17

```xml
<!-- 1.不用Mapper动态代理的方式 -->
<!-- 配置数据源 -->
	<bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
		<property name="driverClassName" value="com.mysql.jdbc.Driver"/>
		<property name="url" value="jdbc:mysql://localhost:3306/test"/>
		<property name="username" value="root"/>
		<property name="password" value="root"/>
	</bean>
	
	<!-- 配置sqlSessionFactory -->
	<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
		<property name="dataSource" ref="dataSource"/>
		<property name="configLocation" value="classpath:conf.xml"/>
	</bean>
	<bean id="sqlSessionTemplate" class="org.mybatis.spring.SqlSessionTemplate">
		<constructor-arg index="0" ref="sqlSessionFactory"/>
	</bean>
<!-- 若在sqlSessionFactory中配mapperLocations属性则可不用mybatis的conf.xml文件 -->
```

```xml
<!-- 2.使用Mapper动态代理的方式 -->
<!-- 2.1使用 spring-bean的方式显示注入 -->
<bean id="articleMapper" class="org.mybatis.spring.mapper.MapperFactoryBean">
    <property name="mapperInterface" value="com.kuanrf.gs.article.dao.ArticleMapper" />
    <property name="sqlSessionFactory" ref="sqlSessionFactory" />
</bean>

<!-- 2.2使用 MapperScannerConfigurer 更方便-->
<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
  <property name="basePackage" value="org.mybatis.spring.sample.mapper" />
</bean>
```

- 使用**MapperScannerConfigurer** , 它 将 会 查 找 类 路 径 下 的 映 射 器 并 自 动 将 它 们 创 建 成 MapperFactoryBean 

- **没 有 必 要** 去 指 定 SqlSessionFactory 或 SqlSessionTemplate , 因 为 MapperScannerConfigurer 将会创建 MapperFactoryBean,之后自动装配 

  见http://www.mybatis.org/spring/zh/mappers.html#MapperScannerConfigurer

- SqlSessionFactory等交给Spring管理可以保证是单例

  ### 3.用SqlSessionDaoSupport

  ​	在配置文件中不需要管理sqlSessionTemplate，在dao实现中需要继承SqlSessionDaoSupport

待完善

## 声明式事务(important)

配置事务

```xml
<!-- 声明式事务配置 开始 -->
	<!-- 配置事务管理器 -->
	<bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
	    <property name="dataSource" ref="dataSource"/>
	</bean>
	<!-- 配置事务通知 -->
	<tx:advice id="txAdvice" transaction-manager="txManager">
		<tx:attributes>
			<!-- 配置哪些方法使用什么样的事务,配置事务的传播特性 -->
			<tx:method name="add" propagation="REQUIRED"/>
			<tx:method name="insert" propagation="REQUIRED"/>
			<tx:method name="update" propagation="REQUIRED"/>
			<tx:method name="delete" propagation="REQUIRED"/>
			<tx:method name="remove*" propagation="REQUIRED"/>
			<tx:method name="get" read-only="true"/>
			<tx:method name="*" propagation="REQUIRED"/>
		</tx:attributes>
	</tx:advice>
	<aop:config>
		<aop:pointcut expression="execution(* cn.medwin.service.impl.*.*(..))" id="pointcut"/>
		<aop:advisor advice-ref="txAdvice" pointcut-ref="pointcut"/>
	</aop:config>
	<!-- 声明式事务配置 结束 -->
```

```
	sqlSession.insert("cn.sxt.vo.user.mapper.add", user);
	sqlSession.delete("cn.sxt.vo.user.mapper.remove",20);
//此时若delete失败，则insert也不执行
```

- 事务的传播特性

  1. PROPAGATION_REQUIRED: 如果存在一个事务，则支持当前事务。如果没有事务则开启

  2. PROPAGATION_SUPPORTS: 如果存在一个事务，则支持当前事务。如果没有事务，则非事务的执行

  3. PROPAGATION_MANDATORY: 如果存在一个事务，则支持当前事务。如果没有一个活动的事务，则抛出异常。

  4. PROPAGATION_REQUIRES_NEW: 总是开启一个新的事务。如果一个事务已经存在，则将这个存在的事务挂起。

  5. ......

     

  ## 使用注解开发

   

   

   

 