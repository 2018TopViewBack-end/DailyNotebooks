# 

# SpringAOP基础

### 1、AOP的概述

#### 1.1、AOP到底是什么？

AOP是Aspect Oriented Programing 的简称，“面向切面编程”。

#### 1.2、AOP术语

1. 连接点（Joinpoint）：特定点是程序执行的某个特定位置，如累开始初始化前、类初始化后、类的某个方法调用前/调用后、方法抛出异常后。一个类或一段程序代码拥有一些具有边界性质的特定点，这些代码中的特定点就被称为“连接点”。
2. 切点（Pointcut）：每个程序类都拥有多个连接点，如一个拥有两个方法的类，这两个方法都是连接点，即连接点是程序类中客观存在的事务。
3. 增强（Advice）：增强是织入目标类连接点上的一段程序代码。
4. 目标对象（Target）：增强逻辑的织入目标类。
5. 引介（Introduction）：引介是一种特殊的增强，它为类添加一些属性和方法。
6. 织入（Weaving）：织入是将增强添加到目标类的具体连接点上的过程。
7. 代理（Proxy）：一个类被AOP织入增强后，就产生了一个结果类，它是融合了原类和增强逻辑的代理类。
8. 切面（Aspect）：切面由切点和增强（引介）组成，它既包括横切逻辑的定义，也包括连接点的定义。

AOP的工作重心在于如何将增强应用于目标对象的连接点上。这里包括两项工作：第一，如何通过切点和增强定位到连接点上；第二，如何在增强中编写切面的代码。

### 2、基础知识

SpringAOP使用了两种代理机制：一种是基于JDK的动态代理；另一种是基于CGLIB的动态代理。

#### 2.1、带有横切逻辑的实例

代码清单7-2 ForumService：包含性能监视横切代码

```java
package com.smart.proxy;
/**
 * @author Pan梓涵
 */
public class ForumServiceImpl implements ForumService {
    @Override
    public void removeTopic(int topicId) {
        //开始对该方法进行性能监测
        PerformanceMonitor.begin("com.smart.proxy.ForumServiceImpl.removeTopic");
        System.out.println("模拟删除Topic记录：" + topicId);
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //结束对该方法的性能监测
        PerformanceMonitor.end();
    }

    @Override
    public void removeForum(int forumId) {
        //开始对改方法的性能监测
        PerformanceMonitor.begin("com.smart.proxy.ForumServiceImpl.removeForum");
        System.out.println("模拟删除Forum记录：" + forumId);
        try {
            Thread.sleep(40);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        PerformanceMonitor.end();
    }
}
```

代码清单PerformanceMonitor

```java
package com.smart.proxy;
/**
 * @author Pan梓涵
 */
public class PerformanceMonitor {

    //通过一个ThreadLocal保存于调用线程相关的性能监测信息
    private static ThreadLocal<MethodPerformance> performanceRecord = new ThreadLocal<>();

    /**
     * 启动对某一目标方法的性能监测
     * @param method 方法名字
     */
    public static void begin(String method) {
        System.out.println("begin monitor...");
        MethodPerformance methodPerformance = new MethodPerformance(method);
        performanceRecord.set(methodPerformance);
    }

    public static void end() {
        System.out.println("end monitor");
        MethodPerformance methodPerformance = performanceRecord.get();
        //打印出方法性能监测的结果信息
        methodPerformance.printPerformance();
    }
}
```

ThreadLocal是将非线程安全类改造为线程安全类的"法宝"。

代码清单 MethodPerformance

```java
package com.smart.proxy;

/**
 * @author Pan梓涵
 */
public class MethodPerformance {

    private long begin;
    private long end;
    private String serviceMethod;

    public MethodPerformance(String serviceMethod) {
        this.serviceMethod = serviceMethod;
        //记录目标方法开始执行点的系统时间
        this.begin = System.currentTimeMillis();
    }

    public void printPerformance() {
        //获取目标类方法执行完成后的系统时间，进而计算出目标类方法的执行时间
        end = System.currentTimeMillis();
        long elspse = end - begin;
        //报告目标类方法的执行时间
        System.out.println(serviceMethod + "花费" + elspse + "毫秒");
    }
}
```

通过下面的代码测试拥有性能监视恩能力的ForumServiceImpl业务方法

```java

package com.smart.proxy;

public class TestForumService {
    public static void main(String[] args) {
            ForumService forumService = new ForumServiceImpl();
        forumService.removeForum(10);
        forumService.removeTopic(1012);
    }
}

```

得到以下的输出信息：

```
begin monitor...
模拟删除Forum记录：10
end monitor
com.smart.proxy.ForumServiceImpl.removeForum花费42毫秒

begin monitor...
模拟删除Topic记录：1012
end monitor
com.smart.proxy.ForumServiceImpl.removeTopic花费21毫秒

```

#### 2.2、JDK动态代理

JDK主要代理涉及java.lang.reflect包中的两个类：Proxy和InvocationHandler。

其中，InvocationHandler是一个接口，可以通过实现该接口定义横切逻辑，并通过反射机制调用目标类的代码，动态地将横切逻辑和业务逻辑编织在一起。

而Proxy利用InvocationHandler动态创建一个符合某一接口的实例，生成目标类的代理对象。

代码清单，ForumServiceImpl：移除性能监测的代码

```java
package com.smart.proxy;
/**
 * @author Pan梓涵
 */
public class ForumServiceImpl implements ForumService {

    @Override
    public void removeTopic(int topicId) {
        System.out.println("模拟删除Topic记录：" + topicId);
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeForum(int forumId) {
        System.out.println("模拟删除Forum记录：" + forumId);
        try {
            Thread.sleep(40);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

```

从业务类中移除性能监测横切代码后，必须为其找到一个安身之所，InvocationHandler就是横切代码的“安家乐园”。将性能监视横切代码安置在PerformanceHandler中。

代码清单 PerformanceHandler

```java
package com.smart.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Pan梓涵
 */
public class PerformanceHandler implements InvocationHandler {

    private Object target;

    public void setTarget(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        PerformanceMonitor.begin(target.getClass().getName() + "." + method.getName());
        //通过反射方法调用业务类的目标方法
        Object object = method.invoke(target, args);
        PerformanceMonitor.end();
        return object;
    }
}
```

说明：

	首先实现了InvocationHandler接口，该接口定义了一个`invoke(Object proxy, Method method, Object[] args)`方法，其中，proxy是最终生成的代理实例，一般不会用到；method是被代理目标实例的某个具体方法，通过它可以发起目标实例方法的反射调用；args是被代理实例某个方法的入参，在方法反射调用时使用。
	
	其次，在set函数提供setTarget（Object target）传入希望被代理的目标对象，在InvocationHandler接口方法invoke（）里，将实例传递给method.invoke（）方法，并调用目标实例的方法。

代码清单：ForumServiceTest：创建代理实例

```java
package com.smart.proxy;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

/**
 * @author Pan梓涵
 */
public class ForumServiceTest {

    @Test
    public void proxy() {
        //1、希望被代理的目标业务类
        ForumService target = new ForumServiceImpl();

        //2、将目标业务类和横切代码编织到一起
        PerformanceHandler performanceHandler = new PerformanceHandler();
        performanceHandler.setTarget(target);

        //3、根据编织了目标业务类逻辑和性能监测横切逻辑的InvocationHandler实例创建代理实例
        ForumService proxy = (ForumService) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                performanceHandler
        );

        //4、调用代理对象
        proxy.removeForum(12);
        System.out.println();
        proxy.removeTopic(1012);
    }
}
```

上面的代码完成了业务类代码和横切代码的编织工作并生成了代理实例。在2处让PerformanceHandler将性能监测横切逻辑编织到ForumService实例中，然后在3处，通过Proxy的newProxyInstance（）静态方法为编制了业务类逻辑和性能监测逻辑的handler创建一个符合ForumService接口的代理实例。该方法的第一个入参为类加载器，第二个入参为创建代理实例所需实现的一组接口；第三个入参是整合了业务逻辑和横切逻辑的编织器对象。

运行以上代码，输出如下信息：

```
begin monitor...
模拟删除Forum记录：12
end monitor
com.smart.proxy.ForumServiceImpl.removeForum花费41毫秒

begin monitor...
模拟删除Topic记录：1012
end monitor
com.smart.proxy.ForumServiceImpl.removeTopic花费20毫秒
```

#### 2.3、CGLib动态代理

使用JDK创建代理有一个限制，即它只能为接口创建代理实例。

CGLib采用底层的字节码技术，可以为一个类创建子类，在子类中采用方法拦截的技术拦截所有父类方法的调用并顺势织入横切逻辑。

代码清单 CglibProxy：

```java
package com.smart.proxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibProxy implements MethodInterceptor {

    private Enhancer enhancer = new Enhancer();

    public Object getProxy(Class clazz) {
        //1、设置需要创建子类的类
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        //2、通过字节码技术动态创建子类实例
        return enhancer.create();
    }

    /**
     * 拦截父类所有方法的调用
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        PerformanceMonitor.begin(obj.getClass().getName() + "." + method.getName());
        //调用代理类的方法
        Object result = methodProxy.invokeSuper(obj, args);
        PerformanceMonitor.end();
        return result;
    }
}
```

在上面的代码中，用户可以通过getProxy（Class clazz）方法为一个创建动态代理对象，该代理对象通过扩展clazz实现代理。在这个代理对象中，织入性能监视的横切逻辑。`intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) `是CGLib定义的Interceptor接口方法，它拦截所有目标类方法的调用。其中obj表示目标类实例；method表示目标类方法的反射对象；args为方法的动态入参；methodProxy为代理类实例。

代码清单：ForumServiceTest：测试CGLib创建的代理类：

```
@Test
public void test() {
    CglibProxy proxy = new CglibProxy();
    //通过动态生成子类的方式创建代理类
    ForumServiceImpl forumService = (ForumServiceImpl) proxy.getProxy(ForumServiceImpl.class);
    forumService.removeForum(10);
    forumService.removeTopic(1013);
}
```

输出信息：

```
begin monitor...
模拟删除Forum记录：10
end monitor
com.smart.proxy.ForumServiceImpl$$EnhancerByCGLIB$$e1416aa5.removeForum花费60毫秒

begin monitor...
模拟删除Topic记录：1013
end monitor
com.smart.proxy.ForumServiceImpl$$EnhancerByCGLIB$$e1416aa5.removeTopic花费21毫秒
```

观察输出信息，除了发现了两个业务方法中都织入了性能监测的逻辑外，还发现代理类的名字变成了`com.smart.proxy.ForumServiceImpl$$EnhancerByCGLIB$$e1416aa5`这个特殊的类就是CGLib为ForumServiceImpl动态创建的子类。

值得一提的是，由于CGLib采用动态创建子类的方式生存代理对象，所以不能对目标类中的final或private方法进行代理。

#### 2.4、代理知识小结

SpringAOP的底层就是通过使用JDK或CGLib动态代理技术为目标bean织入横切逻辑的。

虽然提供PerformanceHandler或CglibProxy实现了性能监视横切逻辑的动态织入，但这种实现方式存在3个明显的需要改进的地方。

1. 目标类的所有方法都添加了性能监视的横切逻辑，而有时整并不是我们所期望的，我们可能只希望对业务类中的某些特定的方法添加横切逻辑。
2. 通过硬编码的方式指定了织入横切逻辑的织入点，即在目标类业务方法的开始和结束前织入代码。
3. 手工编写代理实例的创建过程，在为不同类创建代理时，需要分别编写相应的创建代码，无法做到通用。

SpringAOP通过Point（切点）指定在哪些类的哪些方法上织入横切逻辑，通过Advice（增强）描述横切逻辑和方法的具体织入点（方法前、方法后、方法的两端）。此外，Spring通过Advisor（切面）将Pointcut和Advice组装起来。有了Advisor的信息，Spring就可以利用JDK或CGLib动态代理技术采用统一的方式为目标Bean创建织入切面的代理对象。

### 3、创建增强类

#### 3.1、增强类型

1. 前置增强：org.springframework.aop.BeforeAdvice代表前置增强。因为Spring只支持方法级的增强，所以MethodBeforeAdvice是目前可用的前置增强，表示在目标方法执行前实施增强，而BeforeAdvice是为了将来版本扩展需要定义的。
2. 后置增强：org.springframework.aop.AfterReturningAdvice代表后置增强，表示在目标方法执行后实施增强。
3. 环绕增强：org.springframework.aop.MethodInterceptor代表环绕增强，表示在目标方法执行前后实施增强。
4. 异常抛出增强：org.springframework.aop.ThrowsAdvice代表抛出异常增强，表示在目标方法抛出异常后实施增强。
5. 引介增强：org.springframework.aop.IntroductionInterceptor代表引介增强，表示在目标类中添加一些的新的方法和属性。

这些接口都有一些方法，通过实现这些接口方法，并在接口方法中定义横切逻辑，就可以将它们织入目标类方法的相应链接点的位置。

#### 3.2、前置增强

##### 3.2.1、保证使用礼貌用语的实例

代码清单Waiter

```java
package com.smart.advice;

public interface Waiter {

    void greetTo(String name);

    void serveTo(String name);
}
```

代码清单NaiveWaiter

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

NaiveWaiter只是简单地向顾客打招呼，默不作声地走到顾客前，直接提供服务，下面对NaiveWaiter的服务行为进行规范，让他们在打招呼前和提供服务之前，必须先对顾客使用礼貌用语。

代码清单 GreetingBeforeAdvice

```java
package com.smart.advice;
import org.springframework.aop.MethodBeforeAdvice;
import java.lang.reflect.Method;
public class GreetingBeforeAdvice implements MethodBeforeAdvice {

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        //在目标类方法执行前调用
        String clientName = (String) args[0];
        System.out.println("How are you! Mr." + clientName + ".");
    }
}
```

BeforeAdvice是前置增强的接口，方法前置增强的MethodBeforeAdvice接口是其子类。MethodBeforeAdvice接口仅定义了唯一的方法：`before(Method method, Object[] args, Object target)` ，其中，method为目标类的方法；args为目标类方法的入参；target为目标类实例。

测试代码：BeforeAdviceTest：

```java
package com.smart.advice;

import org.junit.jupiter.api.Test;
import org.springframework.aop.BeforeAdvice;
import org.springframework.aop.framework.ProxyFactory;

public class BeforeTest {

    @Test
    public void before() {
        Waiter target = new NaiveWaiter();
        BeforeAdvice advice = new GreetingBeforeAdvice();

        //1、Spring提供代理工厂
        ProxyFactory proxyFactory = new ProxyFactory();

        //2、设置代理目标类
        proxyFactory.setTarget(target);

        //3、为代理目标添加增强
        proxyFactory.addAdvice(advice);

        //4、生成代理类实例
        Waiter proxy = (Waiter) proxyFactory.getProxy();
        proxy.greetTo("John");
        proxy.serveTo("Tom");
    }
}
```

输出结果：

```
How are you! Mr.John.
greet to John...
How are you! Mr.Tom.
serve to Tom...
```

##### 3.2.2、解剖ProxyFactory

Spring定义了org.springframework.aop.framework.AopProxy接口，并提供了两个final类型的实现类。

其中，Cglib2AopProxy使用了CGLib动态代理技术创建代理，而JdkDynamicAopProxy使用JDK动态代理技术创建代理。如果通过ProxyFactory的setInterfaces（Class[] interfaces）方法指定目标接口进行代理，则ProxyFactory使用JdkDynamicAopProxy；如果针对类的代理，则使用Cglib2AopProxy。此外，还可以通过ProxyFactory的setOptimize（true）方法让ProxyFactory启动优化代理方式，这样针对接口的代理也会使用Cglib2AopProxy。值得注意的是，在使用CGLib动态代理技术时，必须引入CGLib类库。

```java
//指定对接口进行代理的话,将使用JDK动态代理技术
proxyFactory.setInterfaces(target.getClass().getInterfaces());
        
//启用优化代理优化的话，则还将使用CGLib2AopProxy代理
proxyFactory.setOptimize(true);
```

ProxyFactory通过addAdvice（Advice）方法添加一个增强，用户可以使用该方法添加多个增强。多个增强形成一个增强链，它们的调用顺序和添加顺序一致，可以通过addAdvice（int，Advice）方法将增强添加到增强链的具体位置（第一个位置为0）。

##### 3.2.3、在Spring中配置

```xml
<bean id="greetingBeforeAdvice" class="com.smart.advice.GreetingBeforeAdvice"/>
<bean id="target" class="com.smart.advice.NaiveWaiter"/>
<bean id="waiter" class="org.springframework.aop.framework.ProxyFactoryBean"
p:optimize="true"
p:interceptorNames="greetingBeforeAdvice"
p:target-ref="target"/>
```

ProxyFactoryBean是FactoryBean接口的实现类。ProxyFactoryBean负责为其他Bean创建代理是I里，它的内部使用ProxyFactory来完成这项工作。

1. target：代理的目标对象。
2. proxyInterfaces：代理所要实现的接口，可以是多个接口。该属性还有一个别名属性interfaces。
3. interceptorNames：需要织入目标对象的Bean列表，采用Bean的名称指定。这些Bean必须要实现了org.springframework.aop.Advisor或org.aopalliance.intercept.MethodInterceptor的Bean，配置中的顺序对应调用的顺序。
4. singleton：返回的代理是否是单实例，默认为单实例。
5. optimize：当设置为true时，强制使用CGLib动态代理。对于singleton的代理，我们推荐使用CGLib；对于其他作用域类型的代理，最好使用JDK动态代理。原因是虽然CGLib创建代理时速度慢，但其创建出的代理对象运行效率较高；而使用JDK创建代理的表现正好相反。
6. proxyTargetClass：是否对类进行代理（而不是对接口进行代理）。当设置为true时，使用CGLib动态代理。

#### 3.3、后置增强

```java
public class GreetingAfterAdvice implements AfterReturningAdvice {
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        //在目标类方法之后调用
        System.out.println("Please enjoy yourself!");
    }
}
```

通过实现AfterReturningAdvice来定义后置增强的逻辑，AfterReturningAdvice接口也仅定义了唯一的方法

`afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable`

其中，returnValue为目标实例方法返回的结果；method为目标类的方法；args为目标类的方法入参数；target为目标类实例。

在spring.xml配置文件中配置，代码清单 spring-advice.xml

```xml
<bean id="greetingBeforeAdvice" class="com.smart.advice.GreetingBeforeAdvice"/>
<bean id="greetingAfterAdvice" class="com.smart.advice.GreetingAfterAdvice"/>
<bean id="target" class="com.smart.advice.NaiveWaiter"/>
<bean id="waiter" class="org.springframework.aop.framework.ProxyFactoryBean"
      p:optimize="true"
      p:interceptorNames="greetingBeforeAdvice,greetingAfterAdvice"
      p:target-ref="target"/>
```

#### 3.4、环绕增强

```java
package com.smart.advice;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class GreetingInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        //目标方法的入参
        Object[] args = invocation.getArguments();
        String clientName = (String) args[0];
        //在方法执行前调用
        System.out.println("How are you! Mr." + clientName + ".");
        //通过方法机制调用目标方法
        Object object = invocation.proceed();
        //在方法执行后调用
        System.out.println("Please enjoy yourself");
        return object;
    }
}
```

Spring直接使用AOP联盟所定义的MethodInterceptor作为环绕增强的接口。该接口有唯一的接口方法`invoke(MethodInvocation invocation) throws Throwable`，MethodInvocation不但封装了目标方法及其入参数组，还封装了目标方法所在的实例对象，通过MethodInvocation的getArguments（）方法可以获取目标方法的入参数组，通过proceed（）方法反射调用目标实例相应的方法。

#### 3.5、异常抛出增强

异常抛出增强最适合的场景是事务管理，当参与事务的某个DAO发生异常的时候，事务管理器就必须回滚事务。

```java
package com.smart.advice;
import org.springframework.aop.ThrowsAdvice;
import java.lang.reflect.Method;

public class TransactionManager implements ThrowsAdvice {
    //定义增强逻辑
    public void afterThrowing(Method method, Object[] args, Object target, Exception e) throws Throwable {
        System.out.println("-----------");
        System.out.println("method：" + method.getName());
        System.out.println("抛出异常：" + e.getMessage());
        System.out.println("成功回滚事务");
    }
}
```

ThrowAdvice异常抛出增强接口没有定义任何方法，它是一个标签接口，在运行时Spring使用反射机制进行判断，必须采用以下签名形式定义抛出的增强方法：

`void afterThrowing(Method method, Object[] args, Object target, Exception e) throws Throwable`

方法名必须为afterThrowing，方法入参规定如下：前三个入参Method method、Object[] args、Object target是可选的（3个入参要么提供，要么不提供），而最后一个入参是Throwable或其子类。

可以在同一个异常抛出增强中定义多个afterThrowing（）方法，当目标类方法抛出异常时，Spring会自动选用最匹配的增强方法。

#### 3.6、引介增强

引介增强是一种比较特俗的增强类型，它不是在目标方法周围织入增强，而是为目标类创建新的方法和属性，所以引介增强的连接点是类级别的，而非方法级别的。通过引介增强，可以为目标类添加一个接口的实现，即原来目标类未实现某个接口，通过引介增强可以未某个目标类创建实现某接口的代理。

Spring定义了引介增强接口IntroductionInterceptor，该接口没有定义任何方法，Spring为该接口提供了 DelegatingIntroductionInterceptor实现类。一般情况下，通过扩展该实现类定义自己的引介增强类。

回到本章前面的性能监视的例子，我们对所有的业务类都织入了性能监视的增强。由于性能监视会影响业务系统的性能，所以是否启用性能监视应该是可控的，即维护人员可以手工打开或关闭性能检测的功能。但原来的例子只简单地添加了运行性能监视逻辑，为提供任何控制的功能，现在可以通过引介增强来实现这一诱人的功能。

定义一个用于表示目标类是否支持性能监视的接口，代码清单：Monitorable

```java
package com.smart.advice;
public interface Monitorable {
    void setMonitorActive(boolean active);
}
```

该接口仅包括一个setMonitorActive（boolean active）方法，我们期望通过该接口方法控制业务类性能监视功能的激活和关闭状态。

```java
package com.smart.advice;

import com.smart.proxy.PerformanceMonitor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.DelegatingIntroductionInterceptor;

public class ControllablePerformanceMonitor extends DelegatingIntroductionInterceptor
    implements Monitorable{

    private ThreadLocal<Boolean> MonitorStatusMap = new ThreadLocal<>();

    @Override
    public void setMonitorActive(boolean active) {
        MonitorStatusMap.set(active);
    }

    /**
     * 拦截方法
     */
    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        Object object ;

        //对于支持性能监视的可控代理，通过判断其状态决定是否开启性能监测功能
        if(MonitorStatusMap.get() != null && MonitorStatusMap.get()) {
            PerformanceMonitor.begin(mi.getClass().getName() + "." + mi.getMethod().getName());
            object = super.invoke(mi);
            PerformanceMonitor.end();
        } else {
            object = super.invoke(mi);
        }
        return object;
    }
}
```

ControllablePerformanceMonitor在扩展DelegatingIntroductionInterceptor的同时，还必须实现Monitorable接口，提供方法的实现。定义了一个ThreadLocal类型的变量，用于保存性能监视开关状态。之所以使用ThreadLocal变量，是以为者控制状态使代理类变成了非线程安全的实例，为了解决单实例线程安全的问题，通过ThreadLocal让每个线程单独使用一个状态。

覆盖了父类中的invoke（）方法，该方法用于拦截目标类方法的调用，根据监视开关的状态有条件地对目标类实例方法进行性能监视。

代码清单：配置引介增强

```xml
<bean id="forumServiceTarget" class="com.smart.advice.ForumService"/>
<bean id="pmonitor" class="com.smart.advice.ControllablePerformanceMonitor"/>
<bean id="forumService" class="org.springframework.aop.framework.ProxyFactoryBean"
      p:interfaces="com.smart.advice.Monitorable"
      p:target-ref="forumServiceTarget"
      p:interceptorNames="pmonitor"
      p:proxyTargetClass="true"/>
```

代码清单：IntroduceTest：测试引介增强

```java
package com.smart.advice;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.SQLException;

public class IntroduceTest {

    @Test
    public void introduce() throws SQLException {
        String configPath = "com/smart/advice/spring-advice.xml";
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(configPath);

        ForumService forumService = applicationContext.getBean("forumService", ForumService.class);

        //默认情况下,未开启性能监测
        forumService.updateForum();
        forumService.removeForum(1);

        //开启性能监测
        Monitorable monitorable = (Monitorable) forumService;
        monitorable.setMonitorActive(true);

        //在性能监视功能开启的情况下，再次调用业务方法
        System.out.println("------------");
        forumService.updateForum();
        System.out.println("------------");
        forumService.removeForum(1);
    }
}
```

### 4、创建切面

Spring通过org.springframework.aop.Pointcut接口描述切点，Pointcut由ClassFilter和MethodMatcher构成，它通过ClassFilter定位到某些特定的类上面，通过MethodMatcher定位到某些特定的方法上面，这样Pointcut就拥有了描述某些类的某些特定方法的能力。

* ClassFilter只定义了一个方法matcher（Class clazz），其参数代表一个被检测类，该方法判别被检测的类是否匹配过滤条件。

Spring支持两种方法匹配器：静态方法匹配器和动态方法匹配器。所谓静态方法匹配器，仅对方法名签名（包括方法名和入参类型和顺序）进行匹配；而动态方法匹配器会在运行期检查方法入参的值。静态匹配仅会判别一次，而动态匹配因为每次调用方法的入参都可能不一样，所以每次调用方法都必须判别，因此，动态匹配对性能的影响和电脑很大。一般情况下，动态匹配不常使用。方法匹配器的类型由isRuntime（）方法的返回值决定，返回false表示是静态方法匹配器，返回true是动态方法匹配器。

#### 4.1、切点类型

Spring提供了6种切点：

1.静态方法切点：org.springframework.aop.support.StaticMethodMatcherPointcut

　　StaticMethodMatcherPointcut是静态方法切点的抽象基类，默认情况下匹配所有的类。StaticMethodMatcherPointcut有两个重要的子类：NameMethodMatcherPointcut和AbstractRegexMethodPoint。前者提供简单的字符串匹配方法签名，后者使用正则表达式匹配方法签名。

2.动态方法切点：org.springframework.aop.support.DynamicMethodMatcherPointcut

　　DynamicMethodMatcherPointcut是动态方法切点的抽象基类，默认情况下它匹配所有的类。DynamicMethodMatcherPointcut已过时！！使用DefaultPointcutAdvisor和DynamicMethodPointcut动态方法匹配器代替。

3.注解切点

4.表达式切点

5.流程切点

6.复合切点

#### 4.2、切面类型

Spring使用org.springframework.aop.Advisor接口表示切面的概念。

一个切面同时包含横切代码和连接点信息。切面分为三类：一般切面、切点切面、引介切面。

1. 一般切面：Advisor

　　它仅包含一个Advice，Advice包含了横切代码和连接点的信息，所以Advice本身就是一个简单的切面，只不过它代表的是所有目标类的所有方法。由于这个横切面过于宽泛，所以一把不会直接使用。

2. 切点切面：PointcutAdvisor

　　包含Advice和Pointcut两个类。我们可以通过类、方法名以及方法方位等信息灵活定义切面的连接点，提供更具适用性的切面。

3. 引介切面：IntroductionAdvisor

引介切面是对应引介增强的特殊的切面，它应用于类层面之上，所以引介切点适用ClassFilter进行定义。

#### 4.3、静态普通方法名匹配切面

StaticMethodMatcherPointcutAdvisor代表一个静态方法匹配切面，它通过StaticMethodMatcherPointcut来定义切点，并通过类过滤和方法名来匹配所定义的切点。

代码清单：Waiter

```java
package com.pzh.advisor;
public class Waiter {
    public void greetTo(String name) {
        System.out.println("waiter greet to " + name + "...");
    }
    public void serveTo(String name) {
        System.out.println("waiter serve to " + name + "...");
    }
}
```

代码清单：Seller

```java
package com.pzh.advisor;
public class Seller {
    public void greetTo(String name) {
        System.out.println("seller greet to " + name + "...");
    }
}
```

Seller拥有一个和Waiter相同名称的方法greetTo（）。现在，我们希望通过StaticMethodMatcherPointcutAdvisor定义一个切面，在Waiter#greetTo（）方法调用前织入一个增强，即连接点为Waiter#greetTo（）方法调用前的位置。具体的切面类的实现如代码清单所示。

代码清单：GreetingAdvisor

```java
package com.pzh.advisor;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import java.lang.reflect.Method;

public class GreetingAdvisor extends StaticMethodMatcherPointcutAdvisor {
    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        //切点方法匹配规则：方法名为greetTo
        return "greetTo".equals(method.getName());
    }

    @Override
    public ClassFilter getClassFilter() {
        //切点类匹配规则：为Waiter的类或者子类
        return new ClassFilter() {
            @Override
            public boolean matches(Class<?> clazz) {
                return Waiter.class.isAssignableFrom(clazz);
            }
        };
    }
}
```

StaticMethodMatcherPointcutAdvisor抽象类唯一需要定义的是matches（）方法。在默认情况下，该切面匹配所有的类，这里通过覆盖getClassFilter（）方法，让它仅匹配Waiter类及其子类。

代码清单：GreetingBeforeAdvice 一个前置增强

```java
package com.pzh.advisor;
import org.springframework.aop.MethodBeforeAdvice;
import java.lang.reflect.Method;
public class GreetingBeforeAdvice implements MethodBeforeAdvice {

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println(target.getClass().getName() + "." + method.getName());
        String clientName = (String) args[0];
        System.out.println("How are you! Mr." + clientName + ".");
    }
}
```

代码清单：配置切面：静态方法匹配切面：

```xml
<bean id="waiterTarget" class="com.pzh.advisor.Waiter"/>
<bean id="sellerTarget" class="com.pzh.advisor.Seller"/>
<bean id="greetingBeforeAdvice" class="com.pzh.advisor.GreetingBeforeAdvice"/>
<!--向切面注入一个前置增强-->
<bean id="greetingAdvisor" class="com.pzh.advisor.GreetingAdvisor"
      p:advice-ref="greetingBeforeAdvice"/>
<!--通过一个父<bean>定义公共的配置信息-->
<bean id="parent" abstract="true"
      class="org.springframework.aop.framework.ProxyFactoryBean"
      p:interceptorNames="greetingAdvisor"
      p:proxyTargetClass="true"/>

<bean id="waiter" parent="parent" p:target-ref="waiterTarget"/>
<bean id="seller" parent="parent" p:target-ref="sellerTarget"/>
```

StaticMethodMatcherPointcutAdvisor除了具有advice属性外，还可以定义另外两个属性。

1. ClassFilter：类匹配过滤器，在GreetingAdvisor中用编码的方式设定了classFilter。
2. order：切面织入时的顺序，该属性用于定义Ordered接口表示的顺序。

代码清单：测试代码

```java
@Test
public void testStaticMethodMatcherPointcutAdvisor() {
    String configPath = "com/pzh/advisor/beans.xml";
    ApplicationContext applicationContext = new ClassPathXmlApplicationContext(configPath);
    Waiter waiter = applicationContext.getBean("waiter", Waiter.class);
    Seller seller = applicationContext.getBean("seller", Seller.class);
    waiter.greetTo("John");
    waiter.serveTo("John");
    seller.greetTo("John");
}
```

输出信息：

```
com.pzh.advisor.Waiter.greetTo
How are you! Mr.John.
waiter greet to John...
waiter serve to John...
seller greet to John...
```

#### 4.4、静态正则表达式匹配切面

在StaticMethodMatcherPointcutAdvisor中，仅能通过方法名定义切点，这种描述方式不够灵活。假设目标类中由多个方法，且它们满足一定的命名规范，使用正则表达式进行匹配就要灵活多了。RegexpMethodPointcutAdvisor是正则表达式方法匹配的切面实现类，该类已经是功能齐备的实现类，一般情况下无需扩展该类。

##### 4.4.1、具体实例

```xml
<bean id="regexpAdvisor" class="org.springframework.aop.support.RegexpMethodPointcutAdvisor"
      p:advice-ref="greetingBeforeAdvice">
    <property name="patterns">
        <list>
            <value>.*greet.*</value>
        </list>
    </property>
</bean>
```

在其中定义了一个匹配模式串“.*greet.*”，该模式串匹配Waiter.greetTo（）方法。值得注意的是，匹配模式串匹配的是目标类方法的全限定名，即带类名的方法名。

除了例子使用的patterns和advice属性外，还由另外两个属性，分别介绍如下。

1. pattern：如果只有一个匹配模式串，则可以使用该属性进行配置。patterns属性用于定义多个匹配模式串，这些匹配模式串之间是“或”的关系。
2. order：切面在织入时对应的顺序。

##### 4.4.2、正则表达式语法

| 符号   | 说明                                                         | 实例                                                         |
| ------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| .      | 匹配除换行符外的所有当个字符                                 | .n匹配nay,an apple is on the tree中的an和on，但不匹配nay     |
| *      | 匹配*前面的字符0次或n次                                      | bo*匹配A ghost booooed中的boooo或A bird warbled中的b，但不匹配Agoat g runted中的任何字符。 |
| +      | 匹配+前面的字符1次或n次。等价于{1，}                         | a+匹配candy中的a和caaaaaaandy.中的所有a                      |
| ^      | 表示匹配的字符必须在最前边                                   | ^A不匹配anA中的A，但匹配AnA.中最前面的A                      |
| $      | 与^类似，匹配最末的字符                                      | t$不匹配eater中的t，但匹配eat中的t                           |
| ？     | 匹配？前面的字符0次或1次                                     | e？le？匹配angel中的el和angle中的el                          |
| x\|y   | 匹配x或者y                                                   | green\|red 匹配 green applie中green或者red apple中的red      |
| [xyz]  | 一张字符列表，匹配列表中的任一字符。可以通过连字符“-”指出一个字符字符的范围 | [abc]和[a-c]一样。它们匹配brisket中的b及ache中的a和c         |
| {n}    | 这里的n是一个正整数。匹配前面n个字符                         | a{2}不匹配candy中的a，但匹配caandy中的两个a                  |
| {n，}  | 这里的n是一个正整数。匹配至少n个前面的字符                   | a{2，}不匹配candy中的a，但匹配caandy中的所有a和caaaaaaaandy中的所有a |
| {n，m} | 这里的n和m都是正整数。匹配至少n个，最多m个前面的字符         | a{1，3}不匹配candy中的a，但匹配candy中的a和caandy中的前面两个a和caaaaandy中前面的3个a。注意：即使caaaaandy中有很多个a，但只匹配前面的3个a，即aaa |
| \      | 将下一个字符标记为一个特殊的字符                             | 例如，n匹配字符n。\n匹配一个换行符。语法中的特殊字符需要通过转义符表示， |

| 转义字符                                                     |
| ------------------------------------------------------------ |
| \d 匹配一个数字字符。等价于`[0-9]`                           |
| \D 匹配一个非数字字符。等价于`[^0-9]`                        |
| \f 匹配一个换页符。等价于`\x0c和\cL`                         |
| \n 匹配一个换行符。等价于`\x0a和\cJ`                         |
| \r 匹配一个回车符。等价于`\x0d和\cM`                         |
| \s 匹配任何空白字符，包括空格、制表符、换页符等。等价于`[\f\n\r\t\v]` |
| \S 匹配任何非空白字符。等价于`[^\f\n\r\t\v]`                 |
| \t 匹配一个制表符。等价于`\x09和\cI`                         |
| \v 匹配一个垂直制表符。等价于`\x0b和\c`                      |
| \w 匹配包括下划线的任何单词字符。等价于`[A-Za-z0-9]`         |
| \W 匹配任何非单词字符。等价于`[^A-Za-z0-9]`                  |

#### 4.5、动态切面

DynamicMethodMatcherPointcut是一个抽象类，它将isRuntime（）标识为final且返回true，这样其子类就一定是一个动态切点。该抽象类默认匹配所有的类和方法，因此需要通过扩展该类编写符合要求的动态切点。

代码清单：GreetingDynamicPointcut

```java
package com.pzh.advisor;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.DynamicMethodMatcherPointcut;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class GreetingDynamicPointcut extends DynamicMethodMatcherPointcut {

    private static List<String> specialClientList = new ArrayList<>();

    static {
        specialClientList.add("John");
        specialClientList.add("Tom");
    }

    /**
     * 对类进行静态切点的检查
     */
    @Override
    public ClassFilter getClassFilter() {
        return new ClassFilter() {
            @Override
            public boolean matches(Class<?> clazz) {
                System.out.println("调用getClassFilter（）对" + clazz.getName() + "做静态检查.");
                return Waiter.class.isAssignableFrom(clazz);
            }
        };
    }

    /**
     * 对方法进行静态切点检查
     */
    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        System.out.println("调用matches（method，targetClass）"
                + targetClass.getName()
                + "."
                + method.getName()
                + "做静态检查."
        );
        return "greetTo".equals(method.getName());
    }

    /**
     * 对方法进行动态切点检查
     */
    @Override
    public boolean matches(Method method, Class<?> targetClass, Object... args) {
        System.out.println("调用matches（method，targetClass，args）"
                + targetClass.getName()
                + "."
                + method.getName()
                + "做动态检查."
                );
        String clientName = (String) args[0];
        return specialClientList.contains(clientName);
    }
}
```

GreetingDynamicPointcut类既有用于静态切点检查的方法，又有用于动态切点检查的方法。由于动态切点检查会对性能造成很大的影响，所以应当尽量避免在运行时每次都对目标类各个方法进行动态检查。Spring采用这样的机制：在创建代理时，对目标类的每个连接点使用静态切点检查，如过仅通过静态切点检查就可以知道连接点不匹配，则在运行时就不再进行动态检查；如果静态切点检查是匹配的，则在运行时菜进行动态切点检查。

在动态切点类中进行静态切点检查的方法可以避免不必要的动态切点的检查操作，从而极大的提高运行效率。

在GreetingDynamicPointcut类中，通过boolean matches(Method method, Class<?> targetClass, Object... args)定义了动态切点检查的方法，只对目标方法为greetTo（clientName）且clientName为特殊客户的方法启用增强，通过specialClientList模拟特殊的客户名单。

代码清单：动态切面的配置

```xml
<bean id="waiterTarget" class="com.pzh.advisor.Waiter"/>
<bean id="dynamicAdvisor"
      class="org.springframework.aop.support.DefaultPointcutAdvisor">
    <property name="pointcut">
        <bean class="com.pzh.advisor.GreetingDynamicPointcut"/>
    </property>
    <property name="advice">
        <bean class="com.pzh.advisor.GreetingBeforeAdvice"/>
    </property>
</bean>

<bean id="waiter2" class="org.springframework.aop.framework.ProxyFactoryBean"
      p:interceptorNames="dynamicAdvisor"
      p:target-ref="waiterTarget"
      p:proxyTargetClass="true"/>

```

动态切面的配置和静态切面的配置没有什么区别。使用DefaultPointcutAdvisor定义切面，使用内部bean的方式注入动态切点GreetingDynamicPointcut，增强依旧使用前面定义的GreetingBeforeAdvice。此外，DefaultPointcutAdvisor还有一个order属性，用于定义切面的织入顺序。

代码清单：动态切面的测试代码

```java
@Test
public void testDynamic() {
    String configPath = "com/pzh/advisor/beans.xml";
    ApplicationContext applicationContext = new ClassPathXmlApplicationContext(configPath);
    Waiter waiter = applicationContext.getBean("waiter2", Waiter.class);
    waiter.greetTo("Peter");
    waiter.serveTo("Peter");
    System.out.println("---------------");
    waiter.greetTo("Tom");
    waiter.serveTo("Tom");
}
```

运行代码，输出以下信息：

```java
//以下8行输出信息反映了在织入切面前Spring对目标类中所有方法进行静态检查
调用getClassFilter（）对com.pzh.advisor.Waiter做静态检查.
调用matches（method，targetClass）com.pzh.advisor.Waiter.serveTo做静态检查.
调用getClassFilter（）对com.pzh.advisor.Waiter做静态检查.
调用matches（method，targetClass）com.pzh.advisor.Waiter.greetTo做静态检查.
调用getClassFilter（）对com.pzh.advisor.Waiter做静态检查.
调用matches（method，targetClass）com.pzh.advisor.Waiter.toString做静态检查.
调用getClassFilter（）对com.pzh.advisor.Waiter做静态检查.
调用matches（method，targetClass）com.pzh.advisor.Waiter.clone做静态检查.

//对应waiter.greetTo("Peter"):第一次调用serveTo（）方法时，执行静态、动态切点检查
调用getClassFilter（）对com.pzh.advisor.Waiter做静态检查.
调用matches（method，targetClass）com.pzh.advisor.Waiter.greetTo做静态检查.
调用matches（method，targetClass，args）com.pzh.advisor.Waiter.greetTo做动态检查.
waiter greet to Peter...

//对应waiter.serveTo("Peter"):第一次调用serveTo（）方法时，执行静态切点检查
调用getClassFilter（）对com.pzh.advisor.Waiter做静态检查.
调用matches（method，targetClass）com.pzh.advisor.Waiter.serveTo做静态检查.
waiter serve to Peter...

---------------
//对应waiter.greetTo("Tom"):第二次调用greetTo（）方法时，只进行动态切点检查
调用matches（method，targetClass，args）com.pzh.advisor.Waiter.greetTo做动态检查.
com.pzh.advisor.Waiter.greetTo
How are you! Mr.Tom.
waiter greet to Tom...

//对应waiter.serveTo("Tom"):第二次调用serveTo（）方法时，不再执行静态切点检查
waiter serve to Tom...
```

通过以上的输出信息，对照DynamicMethodMatcherPointcut切点类，可以很容易发现，Spring会在创建代理织入切面时，对目标类中的所有方法进行静态切点检查；在生成织入切面的代理对象后，第一次调用代理类的每一个方法都会进行一次静态切点检查，如果本次检查就能从候选者列表中将该方法派出，则以后对该方法的调用就不再执行静态切点检查；对于那些在静态切点检查时匹配的方法，在后续调用该方法时，将执行动态切点检查。

如果将GreetingDynamicPointcut类的getClassFilter（）和matcher（Method method，Class clazz）方法注释掉，重新测试代码，将得到以下的输出信息。

```sql
调用matches（method，targetClass，args）com.pzh.advisor.Waiter.greetTo做动态检查.
waiter greet to Peter...
调用matches（method，targetClass，args）com.pzh.advisor.Waiter.serveTo做动态检查.
waiter serve to Peter...
---------------
调用matches（method，targetClass，args）com.pzh.advisor.Waiter.greetTo做动态检查.
com.pzh.advisor.Waiter.greetTo
How are you! Mr.Tom.
waiter greet to Tom...
调用matches（method，targetClass，args）com.pzh.advisor.Waiter.serveTo做动态检查.
com.pzh.advisor.Waiter.serveTo
How are you! Mr.Tom.
waiter serve to Tom...
```

可以发现，每次调用代理对象的任何一个方法，都会执行动态切点检查，这将导致很大的性能问题。所以，在定义动态切点时，切勿忘记同时覆盖getClassFilter（）和matcher（Method method，Class clazz）方法，通过静态切点检查排除大部分方法。

#### 4.6、流程切面

Spring的流程切面有DefaultPointcutAdvisor和ControlFlowPointcut实现。流程切点代表有某个方法直接或间接发起调用的其他方法。来看下面的实例，假设通过一个WaiterDelegate类代理Waiter所有方法。

代码清单：WaiterDelegate

```java
package com.pzh.advisor;
public class WaiterDelegate {
    private Waiter waiter;
    public void setWaiter(Waiter waiter) {
        this.waiter = waiter;
    }
    public void service(String clientName) {
        waiter.greetTo(clientName);
        waiter.serveTo(clientName);
    }
}
```

如果希望所有有WaiterDelegate#service（）方法发起调用的其他方法都织入GreetingBeforeAdvice增强，就必须使用流程切面来完成目标。

代码清单：配置控制流程切面

```xml
<bean id="waiterTarget" class="com.pzh.advisor.Waiter"/>
<bean id="greetingBeforeAdvice" class="com.pzh.advisor.GreetingBeforeAdvice"/>
<bean id="controlFlowPointcut" class="org.springframework.aop.support.ControlFlowPointcut">
    <constructor-arg type="java.lang.Class" value="com.pzh.advisor.WaiterDelegate"/>
    <constructor-arg type="java.lang.String" value="service"/>
</bean>

<bean id="controlFlowAdvisor" class="org.springframework.aop.support.DefaultPointcutAdvisor"
      p:pointcut-ref="controlFlowPointcut"
      p:advice-ref="greetingBeforeAdvice"/>

<bean id="waiter3" class="org.springframework.aop.framework.ProxyFactoryBean"
      p:interceptorNames="controlFlowAdvisor"
      p:target-ref="waiterTarget"
      p:proxyTargetClass="true"/>
```

ControlFlowPointcut有两个构造函数，分别是ControlFlowPointcut（Class clazz）和ControlFlowPointcut（Class clazz，String methodName）。第一个构造函数指定一个类作为流程切点；而第二个构造函数指定一个类和某一个方法作为流程切点。

在这里，指定com.smart.advisor.WaiterDelegate#service（）方法作为切点，表示所有通过该方法直接或间接发起的调用匹配切点。

测试代码：

```java
@Test
public void testControlFlowAdvisor() {
    String configPath = "com/pzh/advisor/beans.xml";
    ApplicationContext applicationContext = new ClassPathXmlApplicationContext(configPath);
    WaiterDelegate waiterDelegate = new WaiterDelegate();
    Waiter waiter = applicationContext.getBean("waiter3", Waiter.class);
    waiter.serveTo("Peter");
    waiter.greetTo("Peter");

    waiterDelegate.setWaiter(waiter);
    waiterDelegate.service("Peter");
}
```

运行上面的代码，输出下面的信息：

```java
waiter serve to Peter...	//对应 waiter.serveTo("Peter");
waiter greet to Peter...	//对应 waiter.greetTo("Peter");
//对应waiterDelegate.service("Peter");
com.pzh.advisor.Waiter.greetTo
How are you! Mr.Peter.
waiter greet to Peter...
com.pzh.advisor.Waiter.serveTo
How are you! Mr.Peter.
waiter serve to Peter...
```

流程切面和动态切面从某种程度上说可以算是一类切面，因为二者都需要在运行期判断动态的环境。对于流程切面来说，代理对象在每次调用目标类方法时，都需要判断方法调用堆栈中是否有满足流程切点要求的方法。因此和动态切面一样，流程切面对性能的影响也很大。

#### 4.7、复合切点切面

假设我们希望由WaiterDelegate#service（）发起调用且被调用的方法是Waiter#greetTo（）时才织入增强，这切点就是复合切点，因为它由两个单独的切点共同确定。

Spring提供的ComposablePointcut把两个切点组合起来，通过切点的复合运算表示。ComposablePointcut可以将多个切点以并集或交集的方式组合起来，提供了切点之间的复合运算的功能。

```java
//构造一个匹配所有类所有方法的复合切点
public ComposablePointcut() {
    this.classFilter = ClassFilter.TRUE;
    this.methodMatcher = MethodMatcher.TRUE;
}

//构造一个匹配特定类所有方法的复合切点
public ComposablePointcut(ClassFilter classFilter) {
    Assert.notNull(classFilter, "ClassFilter must not be null");
    this.classFilter = classFilter;
    this.methodMatcher = MethodMatcher.TRUE;
}

//构造一个匹配所有类的特定方法的复合切点
public ComposablePointcut(MethodMatcher methodMatcher) {
    Assert.notNull(methodMatcher, "MethodMatcher must not be null");
    this.classFilter = ClassFilter.TRUE;
    this.methodMatcher = methodMatcher;
}

//构造一个匹配特定类的特定方法的复合切点
public ComposablePointcut(ClassFilter classFilter, MethodMatcher methodMatcher) {
    Assert.notNull(classFilter, "ClassFilter must not be null");
    Assert.notNull(methodMatcher, "MethodMatcher must not be null");
    this.classFilter = classFilter;
    this.methodMatcher = methodMatcher;
}

//ComposablePointcut提供了3个交集运算的方法
//将复合切点和一个ClassFilter对象进行交集运算，得到一个结果复合切点
public ComposablePointcut intersection(ClassFilter other) {
    this.classFilter = ClassFilters.intersection(this.classFilter, other);
    return this;
}

//将复合切点和一个MethodMatcher对象进行交集运算，得到一个结果复合切点
public ComposablePointcut intersection(MethodMatcher other) {
    this.methodMatcher = MethodMatchers.intersection(this.methodMatcher, other);
    return this;
}

//将复合切点和一个切点对象进行交集运算，得到一个结果复合切点
public ComposablePointcut intersection(Pointcut other) {
    this.classFilter = ClassFilters.intersection(this.classFilter, other.getClassFilter());
    this.methodMatcher = MethodMatchers.intersection(this.methodMatcher, other.getMethodMatcher());
    return this;
}

//ComposablePointcut提供了两个并集运算的方法
//将复合切点和一个ClassFilter对象进行并集运算，得到一个结果复合切点
public ComposablePointcut union(ClassFilter other) {
    this.classFilter = ClassFilters.union(this.classFilter, other);
    return this;
}

//将复合切点和一个MethodMatcher对象进行并集运算，得到一个结果复合切点
public ComposablePointcut union(MethodMatcher other) {
    this.methodMatcher = MethodMatchers.union(this.methodMatcher, other);
    return this;
}
```

ComposablePointcut没有提供直接对两个切点进行交并集运算的方法，如果需要对两个切点进行交并集运算，可以使用Spring提供的org.springframework.aop.support.Pointcuts工具类，该工具类中由两个非常好用的静态方法。

```java
//对两个切点进行并集运算，返回一个结果切点，该切点即ComposablePointcut对象的实例
public static Pointcut union(Pointcut pc1, Pointcut pc2) {
   return new ComposablePointcut(pc1).union(pc2);
}

//对两个切点进行交集运算，返回一个结果切点，该切点即ComposablePointcut对象的实例
public static Pointcut intersection(Pointcut pc1, Pointcut pc2) {
		return new ComposablePointcut(pc1).intersection(pc2);
	}
```

下面通过ComposablePointcut创建一个流程切点和方法名切点的相交切点。

代码清单：GreetingComposablePointcut

```java
package com.pzh.advisor;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.ControlFlowPointcut;
import org.springframework.aop.support.NameMatchMethodPointcut;

public class GreetingComposablePointcut {

    public Pointcut getIntersectionPointcut() {
        //创建一个复合切点
        ComposablePointcut composablePointcut = new ComposablePointcut();

        //创建一个流程切点
        Pointcut pointcut1 = new ControlFlowPointcut(WaiterDelegate.class, "service");

        //创建一个方法名字切点
        NameMatchMethodPointcut pointcut2 = new NameMatchMethodPointcut();
        pointcut2.setMappedName("greetTo");
		
        //将两个交点进行交集运算
        return composablePointcut.intersection(pointcut1).intersection((Pointcut) pointcut2);
    }
}
```

代码清单：配置复合切点切面：

```xml
<bean id="waiterTarget" class="com.pzh.advisor.Waiter"/>
<bean id="sellerTarget" class="com.pzh.advisor.Seller"/>
<bean id="greetingBeforeAdvice" class="com.pzh.advisor.GreetingBeforeAdvice"/>
<bean id="greetingComposablePointcut" class="com.pzh.advisor.GreetingComposablePointcut"/>
<bean id="composableAdvisor"
      class="org.springframework.aop.support.DefaultPointcutAdvisor"
      p:pointcut="#{greetingComposablePointcut.intersectionPointcut}"
      p:advice-ref="greetingBeforeAdvice"/>

<bean id="waiter4" class="org.springframework.aop.framework.ProxyFactoryBean"
      p:interceptorNames="composableAdvisor"
      p:target-ref="waiterTarget"
      p:proxyTargetClass="true"/>
```

下面编写对应的测试代码：

```java
@Test
public void testComposablePointcut() {
    String configPath = "com/pzh/advisor/beans.xml";
    ApplicationContext applicationContext = new ClassPathXmlApplicationContext(configPath);
    Waiter waiter = applicationContext.getBean("waiter4", Waiter.class);
    WaiterDelegate waiterDelegate = new WaiterDelegate();
    waiterDelegate.setWaiter(waiter);

    waiter.greetTo("Peter");
    waiter.serveTo("Peter");

    waiterDelegate.service("Peter");
}
```

运行以上代码，输出以下信息：

```java
//对应waiter.greetTo("Peter");
waiter greet to Peter...	
//对应 waiter.serveTo("Peter");
waiter serve to Peter...	
//对应通过waiterDelegate.service("Peter")调用waiter.greetTo("Peter");	
com.pzh.advisor.Waiter.greetTo	
How are you! Mr.Peter.					
waiter greet to Peter...	
//通过waiterDelegate.service("Peter")调用waiter.serveTo("Peter");
waiter serve to Peter...
```

#### 4.8、引介切面

引介切面是引介增强的封装器，通过引介切面，可以很容易的为现有对象添加任何接口的实现。

![1534921824018](C:\Users\Pan梓涵\AppData\Local\Temp\1534921824018.png)

IntroductionAdvisor接口同时继承理了Advisor和IntroducitonInfo接口。IntroductionInfo接口描述了目标类需要实现的新接口。IntroductionAdvisor和PointcutAdvisor接口不同，它仅有一个类过滤器ClassFilter而没有methodMatcher，这是因为引介切面的切点是类级别的，而Pointcut切点是方法级别的。

IntroductionAdvisor有两个实现类，分别是DefaultIntroductionAdvisor和DeclareParentsAdvisor，前者是引介切面最常用的实现类，后者用于实现使用AspectJ语言的DeclareParent注解表示的引介切面。

DefaultIntroductionAdvisor拥有3个构造函数：

```java
//通过一个增强创建的引介切面，引介切面将为目标对象新增增强对象中所有接口的实现
public DefaultIntroductionAdvisor(Advice advice) {
   this(advice, (advice instanceof IntroductionInfo ? (IntroductionInfo) advice : null));
}

//通过一个增强和一个IntroductionInfo创建引介切面，目标对象需要实现哪些接口由introductionInfo对象的getInterfaces（）方法表示
public DefaultIntroductionAdvisor(Advice advice, IntroductionInfo introductionInfo) {
   Assert.notNull(advice, "Advice must not be null");
   this.advice = advice;
   if (introductionInfo != null) {
      Class<?>[] introducedInterfaces = introductionInfo.getInterfaces();
      if (introducedInterfaces.length == 0) {
         throw new IllegalArgumentException("IntroductionAdviceSupport implements no interfaces");
      }
      for (Class<?> ifc : introducedInterfaces) {
         addInterface(ifc);
      }
   }
}

//通过一个增强和一个指定的接口类创建引介切面，仅为目标对象新增clazz接口的实现
public DefaultIntroductionAdvisor(DynamicIntroductionAdvice advice, Class<?> intf) {
   Assert.notNull(advice, "Advice must not be null");
   this.advice = advice;
   addInterface(intf);
}
```

配置引介切面

```xml
<bean id="introductionAdvisor" class="org.springframework.aop.support.DefaultIntroductionAdvisor">
    <constructor-arg>
        <bean class="com.smart.advice.ControllablePerformanceMonitor"/>
    </constructor-arg>
</bean>

<bean id="forumServiceTarget" class="com.smart.advice.ForumService"/>
<bean id="forumService" class="org.springframework.aop.framework.ProxyFactoryBean"
      p:interceptorNames="introductionAdvisor"
      p:target-ref="forumServiceTarget"
      p:proxyTargetClass="true"/>
```

### 5、自动创建代理

在前面的所有例子中，都通过ProxyFactoryBean创建织入切面的代理，每个需要被代理的Bean都需要使用一个ProxyFactoryBean进行配置，虽然可以使用父子`<bean>`进行改造，但还是很麻烦。

幸运的是，Spring提供了自动代理机制，让容器自动生成代理，把开发人员从繁琐的配置工作中解放出来。在内部，Spring使用BeanPostProcessor自动完成这项工作。

#### 5.1、实现类介绍

1. 基于Bean配置名规则的自动代理创建器：允许为一组特定配置名的Bean自动创建代理实例的代理创建器，实现类为`BeanNameAutoProxyCreator`。
2. 基于Advisor匹配机制的自动代理创建器：它会对容器中所有的Advisor进行扫描，自动将这些切面应用到匹配的Bean中（为目标Bean创建代理实例，实现类为`DefaultAdvisorAutoProxyCreator`）。
3. 基于Bean中的AspectJ注解标签的自动代理创建器：为包含AspectJ注解的Bean自动创建代理实例，实现类为`AnnotationAwareAspectJAutoProxyCreator`。

#### 5.2、BeanNameAutoProxyCreator

代码清单，使用bean名进行自动代理：

```xml
<bean id="waiter" class="com.pzh.advisor.Waiter"/>
<bean id="seller" class="com.pzh.advisor.Seller"/>
<bean id="greetingBeforeAdvice" class="com.pzh.advisor.GreetingBeforeAdvice"/>

<bean class="org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator"
      p:interceptorNames="greetingBeforeAdvice"
      p:optimize="true">
    <property name="beanNames" value="waiter,seller"/>
</bean>
```

#### 5.3、DefaultAdvisorAutoProxyCreator

切面Advisor是切点和切面的复合体，Advisor本身已经包含了足够的信息，如横切逻辑和连接点。

DefaultAdvisorAutoProxyCreator能够扫描容器中的Advisor，并将Advisor自动织入匹配的目标bean中，即为匹配的目标bean自动创建代理。

代码清单：

```xml
<bean id="waiter" class="com.pzh.advisor.Waiter"/>
<bean id="seller" class="com.pzh.advisor.Seller"/>
<bean id="greetingBeforeAdvice" class="com.pzh.advisor.GreetingBeforeAdvice"/>
<bean id="regexpAdvisor" class="org.springframework.aop.support.RegexpMethodPointcutAdvisor"
      p:patterns=".*greet.*"
      p:advice-ref="greetingBeforeAdvice"/>
<bean class="org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator"/>
```

#### 5.4、AOP无法被增强疑难问题解析

大家在使用SpringAOP时，或多或少会碰到一些方法无法被增强的问题，有时同一个类里面的方法，有的可以被增强，有的无法被增强。要分析其原因，首先要从SpringAOP的实现机制入手。从上面的学习可以知道，AOP底层实现有两种方法：一种是基于JDK动态代理；另一种是基于CGLib动态代理。

在JDK动态代理中通过接口来实现方法的拦截，所以必须要确保要拦截的目标方法在接口中有定义，否则将无法实现拦截。

在CGLib动态代理中通过动态生成代理子类来实现方法拦截，所以必须确保要拦截的目标方法可被子类访问，也就是目标方法必须被定义为非final，则非私有方法。