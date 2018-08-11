#  AOP编程

### 1、概念

aop（aspect oriented programming）面向切面编程，是对所有对象或者是一类对象编程，核心是（在不增加代码的基础上，还增加新功能）。

### 2、编程说明

1. 定义接口；
2. 编写对象（被代理对象=目标对象）；
3. 编写通知（前置通知目标调用前调用）；
4. 在beans.xml文件配置；
5. 配置被代理对象=目标对象；
6. 配置通知；
7. 配置代理对象 是ProxyFactoryBean的对象实例
   1. <!--代理接口集-->；
   2. 织入通知；
   3. 配置被代理对象；
   * 后面还后置通知，环绕通知，异常通知，引入通知

### 3、xml配置

```xml
<bean id="nativeWaiter" class="com.test6.NativeWaiter"/>
<bean id="greetingBeforeAdvice" class="com.test6.GreetingBeforeAdvice"/>
<bean id="proxyFactoryBean" class="org.springframework.aop.framework.ProxyFactoryBean">
    
    <property name="proxyInterfaces">
        <list>
            <value>com.test6.Waiter</value>
            <value>com.test6.Waiter2</value>
        </list>
    </property>
    
    <property name="interceptorNames">
    	<list>
        	<value>greetingBeforeAdvice</value>
        </list>
    </property>
    
    <property name="target" ref bean="nativeWaiter"/>
    
</bean>
```

## Spring的通知（Advice）

Spring 提供了5种Advice类型：

* Interception Around：JointPoint前后调用
* Before：JointPoint前调用；
* AfterReturning：JointPoint后调用；
* Throw：JointPoint抛出异常时调用；
* Introduction：JointPoint调用完毕后调用；

### 1、Interception Around通知 

　Interception Around通知会在JoinPoint的前后执行。实现此通知的类需要实现接口MethodInterceptor，示例代码如下： 

```java
public class MyMethodInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("调用方法前");
        Object object = invocation.proceed();
        System.out.println("调用方法后");
        return object;
    }
}
```

### 2、Before通知 

只在JointPoint前执行，实现Before通知的类需要实现接口MethodBeforeAdvice，示例带入如下： 

```java
public class GreetingBeforeAdvice implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("****************");
        System.out.println("欢迎光临");
    }
}
```

### 3、After Returning通知 

只在JointPoint之后执行，实现After Returning通知的类需要实现接口AfterReturningAdvice，示例代码如下： 

```java
public class ByeAfterAdvice implements AfterReturningAdvice {
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println("再见");
    }
}
```

### 4、Throw通知 

只在JointPoint抛出异常时执行，实现Throw通知的类需要实现接口ThrowsAdvice,示例代码如下： 

```java
public class MyThrowsAdvice implements ThrowsAdvice {
    public void afterThrowing(Method method, Object[] objects, Object target, Exception e) {
        System.out.println("出大事了" + e.getMessage());
    }
}
```

### 5、Introduction通知 

### 注意点：

Spring的aop中，当你通过代理对象去实现aop的时候，获取的ProxyFactoryBean是声明类型？

答：返回的是一个代理对象，如果目标对象实现了接口，则Spring使用JDK动态代理技术，如果目标对象没有实现接口，则spring使用CGLIB技术。

