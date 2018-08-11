# Bug归纳

## Mapped Statements collection does not contain value for解决

* 1.mybatis的映射文件的命令空间与接口的全限定名不一致;
* 2有可能mybatis的映射文件名字与接口的类名字不一致;
* 3.还有一种情况就是接口声明的方法在映射文件里面没有；
* 4.mapper包中的mapper.xml没有编译到targger中， maven的配置文件可能有问题，即没有配置build的resources 。
* 5.mapper.xml中有重复的id



##  javaweb中路径问题

* 1.对于 idea 来说，servlet 的访问路径比较简单，**直接在 localhost:8080 后面加上我们自己配置的值即可** ,无需加上工程名



## Sql语句查询

* 1.成员基本属性(String也是)变量名不要和表的名字一样



## org.apache.ibatis.executor.ExecutorException

* Cause: org.apache.ibatis.executor.ExecutorException: No constructor found in www.jisheng.po.Customer matching [java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String]The error may exist in CustomerDAO.xml
* 解决方案：创建无参构造器



## org.apache.ibatis.binding.BindingException:

* org.apache.ibatis.binding.BindingException: Type interface www.jisheng.dao.FoodDAO is not known to the MapperRegistry.
* 解决方案：在MyBatis配置文件中添加该Mapper的路径



## Annotation processing is not supported for module cycles 

* Error:java: Annotation processing is not supported for module cycles.Please ensure that all modules from cycle [OrderSystem-util,OrderSystem-service,OrderSystem-dao] are excluded from annotation processing
* 依赖循环



## java.lang.ClassNotFoundException: org.apache.jsp.WEB_002dINF.views.login_jsp

* 可能是out文件夹中无加载jar包



## Exception starting filter OpenSessionInViewFilter的解决方法

* 解决方案1：重新设置路径：点击该模块的Open Module settings ,然后对web重新设置路径
* 解决方案2：重新设置tomcat：Edit Configurations，新建tomcat并进行部署



## 使用外部属性文件连接异常问题

* **`<property name="username" value="${username}" />`** 这一行因为此时${username}的值并不是jdbc.properties文件中的username值，而是JVM系统环境变量的username。spring容器在管理PropertySource时，不光读取自己写的properties文件，spring也会把JVM system properties和JVM system env properties都读取到容器中，所以请不要使用和JVM properties相同的key。 

* 正确文件名格式：

  * ```
    jdbc.driver=com.mysql.jdbc.Driver
    jdbc.url=jdbc:mysql://localhost:3306/foodsystem?useUnicode=true&characterEncoding=gbk
    jdbc.username=root
    jdbc.password=root
    ```




## Cannot resolve method setAttribute(java.lang.String, java.lang.String)的解决问题

* 原因:缺少了`servlet-api.jar`和`jsp-api.jar`这两个包。 
* 解决方案：**这两个jar包是是tomcat自带的，因此在Project Structure中，选择Library–>Application Server Libraries–>增加tomcat中的这两个包就行了 



## url-pattern中 / 和 /* 的区别

* < url-pattern > `/*`  < url-pattern > 中`/*`能匹配所有请求URL，会匹配到*.jsp，会出现返回jsp视图时再次进入spring的DispatcherServlet 类，导致找不到对应的controller所以报404错。
* 当映射规则为 /* 时，最后返回xx.jsp也经过DispatcherServlet，它又会去找相对应的处理器，这也是控制台打印noHandlerFound，也就导致了404错误，页面更别想看到。当改成 / 后，servlet不会匹配到.jsp的URI，当然就能正常返回页面了
* 注意：配置HiddenHttpMethodFilter时<url-pattern>要为**" / * "**,只有这样才能过滤到.jsp文件。但是，配置DispatcherServlet时却不需要，因为其无需得到.jsp文件的映射

## 在读取磁盘文件时遇到%20的解决方案

* ```java
  String genCfg = "/mbgConfiguration.xml";
  String img_path = GenMain.class.getResource(genCfg).getPath();
  img_path = URLDecoder.decode(img_path,"utf-8");
  File configFile = new File(String.valueOf(new FileDataSource(img_path).getFile()));
  ```

 ## NoSuchMethodError解决

* 原因：JVM的**全盘委托机制** 使重复的两个包造成冲突
* eg：commons-loang2.x.jar和commons-loang4.x.jar都位于类路径中，当代码用到了commons-lang4.x的方法，而这个方法在commons-lang2.x中不存在，JVM加载器碰巧又从commons-lang 2.x.jar中加载类，运行时就会抛出该错误
* 解决方案：在maven中对该依赖进行scope限制，或用execution来排除依赖

## resources中的建包问题

* resouces中要用"/"来分包

## 端口占用的解决办法

- cd c:/WINDOWS/system32
- taskkill /f /pid java.exe






