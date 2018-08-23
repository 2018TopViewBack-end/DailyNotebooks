# 基于@AspectJ的AOP

### 1、Java5.0注解知识快速进阶

#### 1.1、一个简单的注解类

```java
package com.pzh.aspectj.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) //（一）声明注解类的保留期限
@Target(ElementType.METHOD) //（二）声明可以使用该注解的目标类型
public @interface NeedTest { //（三）定义注解
    boolean value() default true; //（四）声明注解成员
}
```

Java新语法规定使用@interface修饰符定义注解类，如（三）所示。一个注解可以拥有多个成员，成员声明和接口方法声明类似，这里仅定义了一个成员，如（四）所示。成员声明有以下几点限制：

1. 成员以无入参、无抛出异常的方式声明。
2. 可以通过defalut为成员指定一个默认值。
3. 成员类型是受限的，合法类型包括原始类型及其封装类、String、Class、enums、注解类型，以及上述类型的数组类型。

@Retention

```java
//注解信息仅保留在目标类代码的源码文件中，但对应的字节码文件将不再保留。
SOURCE：
//注解信息将进入目标类代码的字节码文件中，但类加载器加载字节码文件时不会将注解加载到JVM中，即运行其不能获取注解信息
CLASS,
//注解信息在目标类加载到JVM依然保留，在运行期可以通过反射机制读取类中的注解信息。
RUNTIME
```

@Target

```java
//类、接口、注解类、Enum声明处，相应的注解称为类型注解
TYPE,
//类成员变量或常量处，相应的注解称为域值注解。
FIELD,
//方法声明处，相应的注解称为方法注解
METHOD,
//参数声明处，相应的注解称为参数注解
PARAMETER,
//构造函数声明处，相应的注解称为构造函数注解
CONSTRUCTOR,
//局部变量声明处，相应的注解称为局部变量注解
LOCAL_VARIABLE,
//注解类声明处，相应的注解称为注解类注解
ANNOTATION_TYPE,
//包声明处，相应的注解称为包注解
PACKAGE,
```

如果注解只有一个成员，则成员名必须取名为value（）。

#### 1.2、使用注解

```java
package com.pzh.aspectj.anno;

public class ForumService {
    
    @NeedTest()
    public void deleteForum(int forumId) {
        System.out.println("删除论坛模块：" + forumId);
    }
    
    @NeedTest(false)
    public void deleteTopic(int postId) {
        System.out.println("删除论坛主题：" + postId);
    }
}
```

#### 1.3、访问注解

下面通过反射来访问注解：

```java
package com.pzh.aspectj.anno;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class ToolTest {

    @Test
    public void tool() {

        //(一)得到ForumService对应的Class对象
        Class clazz = ForumService.class;

        //(二)得到ForumService对应的Method数组
        Method[] methods = clazz.getDeclaredMethods();
        System.out.println(methods.length);
        for (Method method : methods) {

            //(三)获取方法上所标注的注解对象
            NeedTest needTest = method.getAnnotation(NeedTest.class);
            if(needTest != null) {
                if (needTest.value()) {
                    System.out.println(method.getName() + "()需要测试");
                }else {
                    System.out.println(method.getName() + "()不需要测试");
                }
            }
        }
    }
}
```

在（三）处通方法的反射对象，获取方法上所标注的NeedTest注解对象，接着就可以访问注解对象的成员。

运行上面代码，输出下面的信息：

```
2
deleteForum()需要测试
deleteTopic()不需要测试
```

### 2、着手使用@AspectJ

	Spring在处理@Aspect注解表达式时，需哟啊将Spring的asm模块添加到类路径中。asm是轻量级的字节码处理框架，因为Java的反射机制无法获取入参名，Spring就利用asm处理@AspectJ中所描述的方法入参名。
	
	此外，Spring采用AspectJ提供的@AspectJ注解类库及相应的解析类库，需要在pom.xml文件中添加aspectj.weaver和aspectj.tools类包的依赖。

#### 2.1、一个简答的例子

@AspectJ采用不同的方式对AOP进行描述，依旧使用NaiveWaiter的例子进行讲解。

```java
package com.smart.advice;

public class NaiveWaiter implements Waiter {

    @Override
    public void greetTo(String name) {
        System.out.println("greet to " + name + "...");
    }

    @Override
    public void serveTo(String name) {
        System.out.println("serve to " + name + "...");
    }
}
```

下面是用@AspectJ注解定义一个切面：

```java
package com.pzh.aspectj.anno;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect //(一)通过该注解将PreGreetingAspect标识为一个切面
public class PreGreetingAspect {

    @Before("execution(* greetTo(..))") //(二)定义切点和增强类型
    public void beforeGreeting() {  //(三)添加增强逻辑
        System.out.println("How are you");
    }
}
```

	首先，在PreGreetingAspect类定义处标注了@Aspect注解，这样，第三方处理程序就可以通过类是否拥有@Aspect注解判断其是否为一个切面。
	
	其次，在beforeGreeting（）方法定义处标注了@Before注解，并为该注解提供了成员值`"execution(* greetTo(..))"`。（二）提供了两个信息：@Before注解表示该增强是前置增强，而成员值是一个@AspectJ切点表达式。它的意思是：在目标类的greetTo（）方法上织入增强，greetTo（）方法可以带任意的入参合任意的返回值。
	
	最后，在（三）的beforeGreeting（）方法是增强所使用的横切逻辑，该横切逻辑在目标方法前调用。

下面通过AspectJProxyFactory为NaiveWaiter生成织入PreGreetingAspect切面的代理。

```java
package com.pzh.aspectj.anno;

import com.smart.advice.NaiveWaiter;
import com.smart.advice.Waiter;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

public class AspectJProxyTest {

    @Test
    public void proxy() {
        Waiter target = new NaiveWaiter();
        AspectJProxyFactory factory = new AspectJProxyFactory();
        //(一)设置目标类对象
        factory.setTarget(target);
        //(二)添加切面类
        factory.addAspect(PreGreetingAspect.class);
        //(三)生成织入切面逻辑的代理对象
        Waiter proxy = factory.getProxy();
        proxy.greetTo("John");
        proxy.serveTo("John");
    }
}
```

#### 2.2、如何通过配置使用@AspectJ切面

```xml
<!--目标bean-->
<bean id="waiter" class="com.smart.advice.NaiveWaiter"/>
<!--使用了@AspectJ注解的切面类-->
<bean class="com.pzh.aspectj.anno.PreGreetingAspect"/>
<!--自动代理创建器，自动将@AspectJ注解切面类织入目标Bean中-->
<bean class="org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator"/>
```

如果使用基于Schema的aop命名空间配置，那么事情就更简单了。

```xml
<!--目标bean-->
<bean id="waiter" class="com.smart.advice.NaiveWaiter"/>
<!--使用了@AspectJ注解的切面类-->
<bean class="com.pzh.aspectj.anno.PreGreetingAspect"/>
<!--自动代理创建器，自动将@AspectJ注解切面类织入目标Bean中-->
<aop:aspectj-autoproxy/>
```

使用aop进行配置，Spring在内部依旧采用AnnotationAwareAspectJAutoProxyCreator进行自动代理的创建工作，但具体的实现细节已经被`<aop:aspectj-autoproxy>`隐藏起来。

`<aop:aspectj-autoproxy/>`有一个proxy-target-class属性，默认值为false，表示使用JDK动态代理技术织入增强；当配置为`<aop:aspectj-autoproxy proxy-target-class="true"/>`时，表示使用CGLib动态代理技术织入增强。不过即使proxy-target-class设置为false，如果目标类没有声明接口，则Spring将自动使用CGLib动态代理。

### 3、@AspectJ语法基础

#### 3.1、切点表达式函数

##### 3.1.1、方法切点函数

| 函数          | 入参           | 说明                                     | 示例                                                         |
| ------------- | -------------- | ---------------------------------------- | ------------------------------------------------------------ |
| execution()   | 方法匹配字符串 | 满足某一匹配模式的的所有目标类方法连接点 | `execution(* com.yc.service.*.*(..))`在配置service层的事务管理时常用，定位于任意返回类型（第一个`”*”`) ；在com.yc.service包下的所有类（第二个`”*”`)下的所有方法（第三个`”*”`),且这个方法的入参为任意类型、数量（体现在 “(..)“） |
| @annotation() | 方法注解类名   | 标注了特定注解的目标方法连接点上         | `@anntation(com.yc.controller.needRecord)`，定位于controller层中任何添加@needRecord的方法，这可以方便地对控制层中某些方法被调用（如某人某时间登陆、进入后台管理界面）添加日志记录。 |

##### 3.1.2、 方法入参切点函数

| 函数    | 入参         | 说明                                     | 示例                                                         |
| ------- | ------------ | ---------------------------------------- | ------------------------------------------------------------ |
| args()  | 类名         | 定位于入参为特定类型的的方法             | 如args(com.yc.model.User，com.yc.model.Article),我们要定位于所有以User,Article为入参的方法，需要注意的是，类型的个数、顺序必须都一一对应） |
| @args() | 类型注解类名 | 定位于被特定注解的类作为方法入参的连接点 | @args(com.yc.annotation.MyAnnotation)。MyAnnotation为自定义注解，标注在目标对象方法入参上，被标注的目标都会被匹配。，如方法public myMethod(@MyAnnotation String args); |

##### 3.1.3、 目标类切点函数

| 函数      | 入参         | 说明                                   | 示例                                                         |
| --------- | ------------ | -------------------------------------- | ------------------------------------------------------------ |
| within()  | 类名匹配串   | 定位于特定作用于下的所有连接点         | within(com.yc.service.*ServiceImpl)，可以通过此注解为特定包下的所有以ServiceImpl名字结尾的类里面的所有方法添加事务控制。 |
| target()  | 类名         | 定位于指定类及其子类                   | target(com.yc.service.IUserService),则可定位到IUserService接口和它的实现类如UserServiceImpl |
| @within() | 类型注解类名 | 定位与标注了特定注解的类及其实现类     | @within(com.yc.controller.needRecord),比如我们可以在BaseController中标注@needRecord，则所有继承了BaseController的UserController、ArticleController等等都会被定位 |
| @target() | 类型注解类名 | 定位于标注了特定注解的目标类里所有方法 | @target(com.yc.controller.needRecord)，则可以在controller层中，为我们需要日志记录的类标注@needRecord。 |

##### 3.1.4、代理类切点函数

主要为this()，大多数情况使用方法与target()相同，区别在通过引介增强引入新接口方法时，新的接口方法同样会被this()定位，但target()则不会。 

#### 3.2、不同的增强类型

##### 3.2.1、Before

```java
public @interface Before {
	//用于定义切点
    String value();
    
    /**
     * 由于无法提供Java反射机制获取方法入参名，所以如果在Java编译时为启用调试信息，或者需要
     * 在运行是解析切点，就必须通过这个成员指定注解所标注增强方法的参数名（注意名字必须完全相
     * 同），多个参数名用逗号隔开。
    */
    String argNames() default "";
}
```

##### 3.2.2、AfterReturning

```java
public @interface AfterReturning {
	//定义切点
    String value() default "";

    /**
     * 表示切点的信息。如果显示指定pointcut的值，那么它将赋给value的设置值，可以将pointcut看作
     * value的同义词
     */
    String pointcut() default "";

    //将目标对象方法返回值绑定给增强的方法
    String returning() default "";
	
    //如前所述
    String argNames() default "";
}
```

##### 3.2.3、Around

```java
public @interface Around {
	//定义切点
    String value();
    
	//如前所述    
    String argNames() default "";

}
```

##### 3.2.4、AfterThrowing

```java
public @interface AfterThrowing {
	//定义切点
    String value() default "";

    /**
     * 表示切点的信息。如果显示指定pointcut的值，那么它将赋给value的设置值，可以将pointcut看作
     * value的同义词
     */
    String pointcut() default "";

    //将抛出异常绑定到增强方法中
    String throwing() default "";

    //如前所述
    String argNames() default "";
}
```

##### 3.2.5、After

```java
/**
 * Final增强，不管是抛出异常还是正常退出，该增强都会得到执行
 */
public @interface After {

    //定义切点
    String value();

    //如前所述
    String argNames() default "";
}
```

##### 3.2.6、DeclareParents

```java
/**
 * 引介增强，相当于IntroductionInterceptor。DeclareParents注解类拥有两个成员
 */
public @interface DeclareParents {

    //定义切点
    String value();

    //默认的接口实现类
    Class defaultImpl() default DeclareParents.class;
}
```

#### 3.3、引介增强用法

```java
package com.pzh.aspectj.anno;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareParents;

@Aspect
public class EnableSellerAspect {

    /**
     * 要实现的目标接口
     */
    @DeclareParents(value = "com.smart.advice.NaiveWaiter",
    defaultImpl = SmartSeller.class)
    public Seller seller;

}
```

在EnableSellerAspect切面中，通过@DeclareParents为NaiveWaiter添加了一个需要实现的Seller接口，并指定其默认实现类为SmartSeller，然后通过切面技术将SmartSeller融合到NaiveWaiter中，这样NaiveWaiter就实现了Seller接口。

配置文件中的配置：

```xml
<aop:aspectj-autoproxy/>
<bean id="waiter" class="com.smart.advice.NaiveWaiter"/>
<bean class="com.pzh.aspectj.anno.PreGreetingAspect"/>
<bean class="com.pzh.aspectj.anno.EnableSellerAspect"/>
```

测试代码：

```java
@Test
public void testDeclaredParents() {
    String configPath = "com/pzh/aspectj/anno/beans.xml";
    ApplicationContext applicationContext = new ClassPathXmlApplicationContext(configPath);
    Waiter waiter = (Waiter) applicationContext.getBean("waiter");
    waiter.greetTo("John");
    Seller seller = (Seller) waiter;
    seller.sell("food");
}
```

输出信息：

```
How are you
greet to John...
成功卖出food
```

### 4、切点函数详解

#### 4.1、Annotation

@annotation表示标注了某个注解的所有方法。通过一个实例说明@annotation的用法。

```java
package com.pzh.aspectj.anno;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class TestAspect {

    @AfterReturning("@annotation(com.pzh.aspectj.anno.NeedTest)")
    public void needTestFun() {
        System.out.println("needTestFun() executed!");
    }
}
```

标注了@NeedTest注解的NaughtyWaiter

```java
package com.pzh.aspectj.anno;
import com.smart.advice.Waiter;
public class NaughtyWaiter implements Waiter {
    @Override
    @NeedTest
    public void greetTo(String name) {
        System.out.println("NaughtyWaiter:greet to " + name + "..");
    }

    @Override
    public void serveTo(String name) {}
}
```

配置spring：

```xml
<aop:aspectj-autoproxy/>
<bean id="naiveWaiter" class="com.smart.advice.NaiveWaiter"/>
<bean id="naughtyWaiter" class="com.pzh.aspectj.anno.NaughtyWaiter"/>
<bean class="com.pzh.aspectj.anno.TestAspect"/>
```

测试代码：

```java
public class AspectJProxyTest {

    private ApplicationContext applicationContext;

    @BeforeEach
    public void initApplication() {
        String configPath = "com/pzh/aspectj/anno/beans.xml";
        applicationContext = new ClassPathXmlApplicationContext(configPath);
    }

    @Test
    public void testAnnotation() {
        Waiter naiveWaiter = applicationContext.getBean("naiveWaiter", Waiter.class);
        Waiter naughtyWaiter = applicationContext.getBean("naughtyWaiter", Waiter.class);
        naiveWaiter.greetTo("John");
        naughtyWaiter.greetTo("Tom");
    }
}
```

输出结果：

```
greet to John...
NaughtyWaiter:greet to Tom..
needTestFun() executed!
```

#### 4.2、execution（）

execution（）是最常用的切点函数，其语法如下：

`execution(<修饰符模式>？ <返回类是模式><方法名模式>(<参数模式>) <异常模式>？)`

除了返回类型模式、方法名模式和参数模式外，其他项都是可选的。

#### 4.3、args（）和@args（）

args（）函数的入参是类名，而@args（）函数的入参必须是注解类的类名。

1. args（）

   该函数接收一个类名，表示目标类方法入参对象是指定类（包含子类）时，切点匹配，如下面例子

   `args(com.smart.Waiter)`。

2. @args（）

   该函数接手一个注解类的类名，当方法的运行时入参对象标注了指定的注解时，匹配切点。

#### 4.4、within（）

通过类匹配模式串声明切点，within（）函数定义的连接点是针对目标类而言的，而非中对运行期对象而言的。

语法：`within(<类匹配模式>)`。

#### 4.5、@within（）和@target（）

@target（M）匹配任意标注了@M的目标类，而@within（M）匹配了标注了@M的类及其子孙类。

#### 4.6、target（）和this（）

target（）切点函数通过判断目标类是否按类型匹配指定类来决定连接点是否匹配，而this（）函数则通过判断代理类是否按类型匹配指定类来决定是否和切点匹配。

1. target（M）表示如果目标类按类型匹配于M，则目标的所有方法都匹配切点。下面通过一些例子理解target（M）的匹配规则。

   `target(com.smart.Waiter)`:NaiveWaiter、NaughtyWaiter及CuteNaiveWaiter的所有方法都匹配切点，包括那些未在Waiter接口中定义的方法。

2. this（）

   一般情况下，使用this（）和target（）来匹配定义切点，二者是等效的。

### 5、@Aspectj进阶

#### 5.1、切点的复合运算

```java
//与运算
@After("within(com.pzh.*) && execution(* greetTo(..))")
public void greeToFun() {
    System.out.println("--greeToFun() executed!--");
}

//非与运算
@Before("!target(com.smart.aspectj.NativeWaiter) && execution(* serveTo(..))")
public void notServeInNaiveWaiter() {
    System.out.println("--notServeInNaiveWaiter() executed!--");
}

//或运算
@AfterReturning("target(com.pzh.advisor.Waiter) || target(com.pzh.advisor.Seller)")
public void waiterOrSeller() {
    System.out.println("--waiterOrSeller() executed!--");
}
```

#### 5.2、命名切点

```java
/**
 * 通过注解方法inPackage()对该切点进行命名，方法可视域修饰符为 private
 * 表明该命名切点只能在本切面类中使用
 */
@Pointcut("within(com.pzh.*)")
private void inPackage() {}

/**
 * 通过注解方法greetTo()对该切点进行命名，方法可视域修饰符为protected
 * 表明该命名切点可以在当前包中的切面类，子切面类中使用
 */
@Pointcut("execution(* greetTo(..))")
protected void greetTo() {}

/**
 * 引用命名切点定义的切点，本切点也是命名切点，它对应的可视域为public
 */
@Pointcut("inPackage() AND greetTo()")
public void inPkgGreetTo() {}
```

命名切点仅利用方法名及访问权限修饰符信息，所以在习惯上方法的返回类型为void，并且方法体为空。

命名切点的引用示例：

```java
@Before("com.pzh.aspectj.advanced.TestNamePointcut.greetTo()")
public void pkgGreetTo() {
    System.out.println("--pkgGreetTo() executed!--");
}

@Before("!target(com.smart.aspectj.NativeWaiter) " +
        "&& com.pzh.aspectj.advanced.TestNamePointcut.inPkgGreetTo()")
public void pkgGreetToNotNaiveWaiter() {
    System.out.println("--pkgGreetToNotNaiveWaiter() executed!--");
}
```

#### 5.3、增强织入的顺序

一个连接点可以同时匹配多个切点，切点对应的增强在连接点上的织入顺序是怎么样的呢？

1. 如果增强在同一个切面类中声明，则依照增强在切面类中定义的顺序进行织入。
2. 如果增强位于不同的切面类中，且这些切面类都实现了org.springframework.core.Ordered接口，则由接口方法的顺序号决定（顺序号小的先织入）。
3. 如果增强位于不同的切面类中，且这些切面类中没有实现org.springframework.core.Ordered接口，则织入的顺序是不确定的。

#### 5.4、访问连接点的信息

##### 5.4.1、JoinPoint

```java
public interface JoinPoint {
	//获取连接点方法运行时的入参列表
	Object[] getArgs();
	//获取连接点的方法签名对象
	Signature getSignature();
    //获取连接点所在的目标对象
    Object getTarget();
    //获取代理对象本身
    Object getThis();
}
```

##### 5.4.2、ProceedingJoinPoint

```java
public interface ProceedingJoinPoint extends JoinPoint {
    //通过反射执行目标对象的连接点处的方法
    public Object proceed() throws Throwable;
    //通过反射执行目标对象连接点处的方法，不过使用新的入参代替原来的入参
    public Object proceed(Object[] args) throws Throwable;

}
```

代码清单：TestAspect：访问连接点对象

```java
@Around("execution(* greetTo(..)) && target(com.smart.aspectj.NativeWaiter)")//环绕增强
public void joinPointAccess(ProceedingJoinPoint pjp) throws Throwable {//声明连接点的入参
    System.out.println("--------joinPointAccess--------");
    //访问连接点的信息
    System.out.println("args[0]:" + pjp.getArgs()[0]);
    System.out.println("signature:" + pjp.getSignature().getName());
    System.out.println("target:" + pjp.getTarget().getClass().getName());
    System.out.println("this:" + pjp.getThis().getClass().getName());
    //通过连接点执行目标对象的方法
    pjp.proceed();
    System.out.println("--------joinPointAccess--------");
}
```

测试方法：

```java
package com.pzh.aspectj.advanced;

import com.smart.advisor.Waiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

    private ApplicationContext applicationContext;

    @BeforeEach
    public void initApplicationContext() {
        String configPath = "com/pzh/aspectj/advanced/beans.xml";
        applicationContext = new ClassPathXmlApplicationContext(configPath);
    }

    @Test
    public void testJoinPointAccess() {
        Waiter waiter = applicationContext.getBean("waiter", Waiter.class);
        waiter.greetTo("John");
    }

}
```

输出信息：

```
--------joinPointAccess--------
args[0]:John
signature:greetTo
target:com.smart.aspectj.NativeWaiter
this:com.smart.aspectj.NativeWaiter$$EnhancerBySpringCGLIB$$7590dcb9
NativeWaiter:greet to John...
--------joinPointAccess--------
```

#### 5.5、绑定连接点信息到入参

```java
/**
 * 绑定连接点参数，首先，args(name)根据增强方法入参找到name对应的类型，得到真正的切点表达式：
 * target(com.smart.aspectj.NaiveWaiter) && args(String);
 * 其次，在该增强方法织入目标连接点时，增强方法可以通过name访问到连接点的入参
 */
@Before("target(com.smart.aspectj.NativeWaiter) && args(name)")
public void bindJoinPointParams(String name) {
    System.out.println("----bindJoinPointParams----");
    System.out.println("name:" + name);
    System.out.println("----bindJoinPointParams----");
}
```

和args（）一样，其他可以绑定连接点参数的切点函数（@args（）和target（）等），当指定参数名时，就同时具有匹配切点和绑定参数双重功能。

#### 5.6、绑定代理对象

```java
/**
 * 通过入参招入waiter对应的类型为Waiter，因而切点表达式为this(Waiter)。
 * 当增强织入目标连接点的时候，增强方法通过waiter入参绑定目标对象
 */
@Before("this(waiter)")
public void bindProxyObj(Waiter waiter) {
    System.out.println("----bindProxyObj----");
    System.out.println(waiter.getClass().getName());
    System.out.println("----bindProxyObj----");
}
```

#### 5.7、绑定类注解对象

```java
/**
 * 所有标注了@NeedTest都方法都将被匹配到
 */
@Before("@annotation(needTest)")
public void bindTypeAnnoObject(NeedTest needTest) {
    System.out.println("----bindTypeAnnoObject----");
    System.out.println(needTest.getClass().getName());
    System.out.println("----bindTypeAnnoObject----");
}
```

使用CGLib代理时，其类的注解对象也会被代理。

#### 5.8、绑定返回值

```java
@AfterReturning(value = "target(com.pzh.advisor.Seller)", returning = "retVal")
public void bindReturnValue(int retVal) {
    System.out.println("----bindReturnValue----");
    System.out.println("returnValue:" + retVal);
    System.out.println("----bindReturnValue----");
}
```

#### 5.9、绑定抛出的异常

```java
@AfterThrowing(value = "target(com.pzh.advisor.Seller)", throwing = "iae")
public void bindException(IllegalArgumentException iae) {
    System.out.println(iae.getMessage());
}
```

