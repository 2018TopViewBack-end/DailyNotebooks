## 一、Bean的生命周期

1. 实例化（当我们的程序加载beans.xml文件），把我们的bean（前提是scope=singleton）实例化到内存中。
	默认调用无参数的构造方法
	```java
	public UserService(){
        System.out.println("调用无参的构造方法");
    }
	```
2. 调用set方法设置属性。
	```java
	public void setName(String name) {
        System.out.println("调用setName");
        this.name = name;
    }
	```
3. 如果你实现了bean名字关注接口（BeanNameAware）则可以通过setBeanName获取id号。

    ```java
    public void setBeanName(String name) {
		System.out.println("setBeanName " + name);
	}
    ```

4. 如果你实现了 bean 工厂关注接口（BeanFactoryAware）则可以获取BeanFactory。

   ```java
   public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
   		System.out.println("setBeanFactory " + beanFactory.getClass().getName());
   }
   ```

5. 如果你实现了 ApplicationContextAware 接口，则调用方法

    ```java
    // 该方法传递ApplicationContext
    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		System.out.println("setApplicationContext " + applicationContext.getClass().getName());
	}
    ```

6. 如果bean和一个后置处理器关联了，则会自动调用

    ```java
   	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessBeforeInitialization : " + bean.getClass().getName() + " : " + beanName);
        return bean;
    }
    ```

7. 如果你实现了InitializingBean接口，则会自动调用 afterPropertiesSet（）方法

   ```java
   public void afterPropertiesSet() throws Exception {}
   ```

8. 如果自己在<bean init-method="init"/>则可以在bean定义自己的初始化方法。

   ```java
   public void myInit() {
        System.out.println("调用自己的init方法");
   }
   ```

9. 如果bean和一个后置处理器关联，则会自动去调用 after。

   ```java
   public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessBeforeInitialization : " + bean.getClass().getName() + " : " + beanName);
        return bean;
    }
   ```

10. 使用我们的bean。

11. 容器关闭。

12. 可以通过实现DisposableBean接口来调用方法 destory。

13. 可以在<bean destory-method = "fun"> 调用定制的销毁方法。

### 小结：

我们实际开发中往往没有用到这么多的过程，常见的是：

1 -> 2 ->6 ->10 ->9 ->11

## 二、装配bean

### 1、基本装配

在spring容器内拼凑bean叫做装配。装配bean的时候，需要告诉容器哪些bean以及容器如何使用依赖注入将它们配合在一起。

### 2、配置bean的细节

1. 对bean的最基本的配置包括bean的id和它的全称类名。
2. 基本装配scope
   1. prototype、singleton、request、session、global-session。
   2. spring中的bean缺省情况下是单例模式，始终返回一个实例。
3. 使用原型bean会对性能产生影响，尽量不要设置为prototype，除非有必要。
4. spring实例化bean或销毁bean时，有时需要做一些处理工作，因此spring可以在创建和拆卸bean的时候调用bean的两个生命周期方法。

### 3、如何给集合注入值

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="
        http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd
        http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd">

    <bean id="department" class="com.test3.Department">
        <property name="strings">
            <list>
                <value>小明</value>
                <value>小李</value>
            </list>
        </property>
        <property name="empList">
            <list>
                <ref bean="emp1"/>
                <ref bean="emp2"/>
            </list>
        </property>
        <property name="empSet">
            <set>
                <ref bean="emp1"/>
                <ref bean="emp1"/>
                <ref bean="emp2"/>
            </set>
        </property>
        <property name="emploeeMap">
            <map>
                <entry key="1" value-ref="emp1"/>
                <entry key="2" value-ref="emp2"/>
            </map>
        </property>
    </bean>

    <bean id="emp1" class="com.test3.Emploee">
        <property name="id" value="3"/>
        <property name="name" value="张三"/>
    </bean>

    <bean id="emp2" class="com.test3.Emploee">
        <property name="id" value="4"/>
        <property name="name" value="李四"/>
    </bean>

</beans>
```

### 4、继承配置	

```xml
 <bean id="parent" class="com.test3.parent">
     <property name="id" value="1"/>
     <property name="name" value="parent"/>
</bean>

<bean id="son" parent="parent" class="com.test3.Son">
	<property name="age" value="2"/>
</bean>
```

### 5、自动装配  

| Mode        | Explanation                                                  |
| ----------- | ------------------------------------------------------------ |
| no          | (Defoault)No autowiring.Bean references must be defined a ref element.Changing the default setting is not recommended for large depleoyments,because specifying collaborators explicitly gives greats control and clarity.To some extent, it documents the structure of a system. |
| byName      | Autowiring by property name.Spring looks for a bean with the same name as the property that needs to be autowired.For example, if a bean definition is set to autowire by name,and it contains a master property(that is, it has a setMaster() method),Spring loos for a bean dinition name master,and uses it to set the property |
| byType      | Allows a property to be autowired if exactly one bean of the property type exists in the container.If more than one exists,la fatal exception is thrown,which indicates that you may not use byType autowiring for that bean.If there are no matching beans, nothing happens; the propertyis not set |
| constructor | Analogous to byType, but applies to constructor arguments. If there is not exactly one bean ofthe constructor argument type in the container, a fatal error is raised |

### 6、启用注解

```xml
<!--启用注解annotation-->
<context:annotation-config/>
```

