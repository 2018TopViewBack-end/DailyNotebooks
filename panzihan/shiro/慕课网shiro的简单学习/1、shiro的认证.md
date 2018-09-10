# 认证

在shiro中，用户需要提供principals （身份）和credentials（证明）给shiro，从而应用能验证用户身份：

**principals**：身份，即主体的标识属性，可以是任何东西，如用户名、邮箱等，唯一即可。一个主体可以有多个principals，但只有一个Primary principals，一般是用户名/密码/手机号。

**credentials**：证明/凭证，即只有主体知道的安全值，如密码/数字证书等。

最常见的principals和credentials组合就是用户名/密码了。接下来先进行一个基本的身份认证。

## 1、环境准备

```xml
<dependencies>
    <dependency>
        <groupId>org.apache.shiro</groupId>
        <artifactId>shiro-core</artifactId>
        <version>1.4.0</version>
    </dependency>

    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.12</version>
    </dependency>
</dependencies>
```

## 2、流程

```java
package com.pzh.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

public class AuthenticationTest {

    SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();

    @Before
    public void addUser() {
        simpleAccountRealm.addAccount("Mark", "123456");
    }

    @Test
    public void testAuthentication() {
        //1、构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        //2、设置认证的realm
        defaultSecurityManager.setRealm(simpleAccountRealm);

        //3、主体提交认证数据
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("Mark", "123456");

        //4、登录
        subject.login(token);
        //5、判断是否登录成功
        System.out.println("isAuthenticated:" + subject.isAuthenticated());
        //6、退出
        subject.logout();
        //7、判断是否已经退出
        System.out.println("isAuthenticated:" + subject.isAuthenticated());

    }

}
```

如果身份验证失败请捕获AuthenticationException或其子类，下面列举一些常见的异常：

```java
//身份验证异常
public class AuthenticationException extends ShiroException{}

//禁用的帐号
public class DisabledAccountException extends AccountException {}

//锁定的帐号
public class LockedAccountException extends DisabledAccountException {}

//错误的帐号
public class UnknownAccountException extends AccountException {}

//错误的凭证
public class IncorrectCredentialsException extends CredentialsException {}

//过期的批证
public class ExpiredCredentialsException extends CredentialsException {}

//登录失败次数过多
public class ExcessiveAttemptsException extends AccountException {}
```