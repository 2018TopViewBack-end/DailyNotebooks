# spring的快速入门案例

### 1、spring是说明？

* struts是web框架（jsp/action/actionfrom）
* hibenate是orm框架，处于持久层
* spring是容器框架，用于配置bean，并维护bean之间关系的框架

### 2、spring中有一个非常重要的概念：bean

​	bean是java中的任何一种对象（javabean / service / action / 数据源 /dao.） 

* ioc：控制反转 inverse of control。
* di：dependency injection 依赖注入。

### 3、快速入门

* 开发一个spring项目

1. 引入spring的开发包。（最小配置 spring.jar 该包把常用的 jar 都包括了，还要写日志包 common-logging.jar）
2. 创建 spring 的一个核心文件 applicationContext.xml  【hibenate 有核心 hibernate.cfg.xml  structs 核心文件 struts-config.xml】

* 该文件一般放在src目录下，该文件中引入xsd文件，可以从给出的案例中拷贝一份即可。

3. 小例子

```java
public class UserService {
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void sayHello() {
        System.out.println("hello " + name);
    }
}


// 测试方法
@Test
public void testSayHello() {
    // 传统方法
    /*
       UserService userService = new UserService();
       userService.setName("panzihan");
       userService.sayHello();
    */
    // Spring 的方法
    ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
    UserService userService = (UserService) applicationContext.getBean("userService");
    userService.sayHello();
}
```

* applicationContext.xml文件的配置

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

    <!-- 在容器文件中配置bean(service/dao/domain/action/数据源) -->
    <!-- bean元素的作用是,当我们的spring框架加载时候,spring就会自动创建一个bean对象,并放在ApplicationContext-->
    <!--
        UserService userService = new UserService();
        userService.setName("panzihan");
    -->
    <bean id="userService" class="com.panzihan.UserService">
        <!--这里就体现注入的概念-->
        <property name="name">
            <value>张三</value>
        </property>
    </bean>
</beans>
```

