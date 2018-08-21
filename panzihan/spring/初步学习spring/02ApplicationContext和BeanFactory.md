

# DI讲解

## 1、理解反向控制（IOC）

1. 依赖注入（di）：比IOC更好的名字。获得依赖对象的方式反转了。
2. IOC应用：ioc或者说di，还可以得到解耦的目的。
3. 小例子：
   1. 创建一个接口 ChangeLetter；
   2. 两个类实现接口；
   3. 把对象配置到spring中；
   4. 使用。

```java
public interface ValidateUser {
    void check(String name, String password);
}

public class checkUser1 implements ValidateUser {
    public void check(String name, String password) {
        System.out.println("我去xml文件验证了");
        System.out.println(name + ":" + password);
    }
}

public class checkUser2 implements ValidateUser {
    public void check(String name, String password) {
        System.out.println("我去数据库验证了");
        System.out.println(name + ":" + password);
    }
}
```

配置文件：

```xml
<!--<bean id="validateUser" class="com.test1.checkUser1"/>-->
<bean id="validateUser" class="com.test1.checkUser2"/>
```

测试方法：

```java
public class ValidateUserTest {
    @Test
    public void check() {
        ApplicationContext applicationContext = 
            new ClassPathXmlApplicationContext("com/test1/beans.xml");
        ((ValidateUser)applicationContext.getBean("validateUser"))
        .check("name", "password");
    }
}
```



* 通过上面的案例，我们可以初步体会到di配合接口编程，的确可以减少（web层）和业务层的耦合度。