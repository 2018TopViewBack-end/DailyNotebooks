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