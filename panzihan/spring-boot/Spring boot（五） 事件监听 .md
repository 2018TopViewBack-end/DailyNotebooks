# Spring boot（五） 事件监听

spring boot在启动过程中增加事件监听机制，为用户功能拓展提供极大的便利。

## 1、支持的事件类型四种

```
ApplicationStartedEvent

ApplicationEnvironmentPreparedEvent

ApplicationPreparedEvent

ApplicationFailedEvent
```

## 2、实现监听步骤：

1. 监听类实现**ApplicationListener**接口 

2. 将监听类添加到`SpringApplication`实例

## 3、ApplicationStartedEvent

`ApplicationStartedEvent`：spring boot启动开始时执行的事件

创建对应的监听类 `MyApplicationStartedEventListener.java`

```java
package com.pzh.springboot.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

/**
 * spring boot 启动监听类
 * @author Pan梓涵
 */
public class MyApplicationStartedEventListener
        implements ApplicationListener<ApplicationStartedEvent> {

    private Logger logger = LoggerFactory.getLogger(MyApplicationStartedEventListener.class);

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        logger.info("==" + getClass().getName() + "==");
    }
}
```

```java
package com.pzh.springboot;

import com.pzh.springboot.listener.MyApplicationStartedEventListener;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author Pan梓涵
 */
@SpringBootApplication
@MapperScan("com.pzh.springboot.mapper")
@EnableCaching
public class Application {

   public static void main(String[] args) {
      SpringApplication springApplication = new SpringApplication(Application.class);
      springApplication.addListeners(new MyApplicationStartedEventListener());
      springApplication.run(args);
   }
}
```

## 4、ApplicationEnvironmentPreparedEvent

`ApplicationEnvironmentPreparedEvent`：spring boot 对应**Enviroment**已经准备完毕，但此时上下文context还没有创建

```java
package com.pzh.springboot.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

/**
 * spring boot 环境事件见监听
 * @author Pan梓涵
 */
public class MyApplicationEnvironmentPreparedEventListener
    implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private Logger logger = LoggerFactory.getLogger(MyApplicationEnvironmentPreparedEventListener.class);

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        logger.info("============" + getClass().getName() + "===============");
    }

}
```

```java
@SpringBootApplication
@MapperScan("com.pzh.springboot.mapper")
@EnableCaching
public class Application {

   public static void main(String[] args) {
      SpringApplication springApplication = new SpringApplication(Application.class);
      springApplication.addListeners(new MyApplicationStartedEventListener());
      springApplication.addListeners(new MyApplicationEnvironmentPreparedEventListener());
      springApplication.run(args);
   }
}
```

在该监听中获取到`ConfigurableEnvironment`后可以对配置信息做操作，例如：修改默认的配置信息，增加额外的配置信息等等~~~

## 5、ApplicationPreparedEvent

`ApplicationPreparedEvent`:spring boot上下文context创建完成，但此时spring中的bean是没有完全加载完成的。

```java
package com.pzh.springboot.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;

/**
 * 上下文创建完成后执行的事件监听器
 * @author Pan梓涵
 */
public class MyApplicationPreparedEventListener
    implements ApplicationListener<ApplicationPreparedEvent> {

    private Logger logger = LoggerFactory.getLogger(MyApplicationPreparedEventListener.class);

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
        logger.info("********************************");
        logger.info(getClass().getName());
        logger.info("********************************");
    }
}
```

```java
package com.pzh.springboot;

import com.pzh.springboot.listener.MyApplicationEnvironmentPreparedEventListener;
import com.pzh.springboot.listener.MyApplicationPreparedEventListener;
import com.pzh.springboot.listener.MyApplicationStartedEventListener;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author Pan梓涵
 */
@SpringBootApplication
@MapperScan("com.pzh.springboot.mapper")
@EnableCaching
public class Application {

   public static void main(String[] args) {
      SpringApplication springApplication = new SpringApplication(Application.class);
      springApplication.addListeners(new MyApplicationStartedEventListener());
      springApplication.addListeners(new MyApplicationEnvironmentPreparedEventListener());
      springApplication.addListeners(new MyApplicationPreparedEventListener());
      springApplication.run(args);
   }
}
```

在获取完上下文后，可以将上下文传递出去做一些额外的操作。

## 6、ApplicationFailedEvent

`ApplicationFailedEvent`:spring boot启动异常时执行事件

```java
package com.pzh.springboot.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;

/**
 * spring boot启动异常时执行事件
 * @author Pan梓涵
 */
public class MyApplicationFailedEventListener
    implements ApplicationListener<ApplicationFailedEvent> {

    private Logger logger = LoggerFactory.getLogger(MyApplicationFailedEventListener.class);

    @Override
    public void onApplicationEvent(ApplicationFailedEvent event) {
        logger.info("------------------------");
        logger.info(getClass().getName());
        logger.info("------------------------");
    }

}
```

```java
package com.pzh.springboot;

import com.pzh.springboot.listener.MyApplicationEnvironmentPreparedEventListener;
import com.pzh.springboot.listener.MyApplicationFailedEventListener;
import com.pzh.springboot.listener.MyApplicationPreparedEventListener;
import com.pzh.springboot.listener.MyApplicationStartedEventListener;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author Pan梓涵
 */
@SpringBootApplication
@MapperScan("com.pzh.springboot.mapper")
@EnableCaching
public class Application {

   public static void main(String[] args) {
      SpringApplication springApplication = new SpringApplication(Application.class);
      springApplication.addListeners(new MyApplicationStartedEventListener());
      springApplication.addListeners(new MyApplicationEnvironmentPreparedEventListener());
      springApplication.addListeners(new MyApplicationPreparedEventListener());
      springApplication.addListeners(new MyApplicationFailedEventListener());
      springApplication.run(args);
   }
}
```

