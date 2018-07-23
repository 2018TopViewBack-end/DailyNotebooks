# [Maven]使用Maven配置Jstl出现异常：The absolute uri: http://java.sun.com/jsp/jstl/core cannot

* 也就是出现如下错误: 

```java
Servlet.service() for servlet jsp threw exception 
org.apache.jasper.JasperException: The absolute uri: http://java.sun.com/jsp/jstl/core cannot be resolved in either web.xml or the jar files deployed with this application 
at org.apache.jasper.compiler.DefaultErrorHandler.jspError(DefaultErrorHandler.java:56) 
at org.apache.jasper.compiler.ErrorDispatcher.dispatch(ErrorDispatcher.java:410)…
```

* 我的JSP页面是这样导入的 

```jsp
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
```

* 我的maven是这样配置的: 

```xml
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>jstl</artifactId>
    <version>1.2</version>
    <scope>compile</scope>
</dependency>
```

* 访问页面出现500错误。  

### 解决方法

- 可能是你的Tomcat缺少JSTL包，所以，你只需要在项目的lib下导入JSTL对应版本的包就可以解决该异常了。或者在Tomcat的lib下导入JSTL对应版本的包就可以了。 
- maven的本地仓库中可以找到jstl的jar包 