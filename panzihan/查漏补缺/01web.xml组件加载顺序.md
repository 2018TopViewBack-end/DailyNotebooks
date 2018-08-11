## web.xml 组件加载顺序

在部署项目组件的过程中，了解Tomcat加载组件顺序很有必要。

经过查阅和Debug发现，web.xml组件加载顺序为：context-param -> listener -> filter ->servlet （同类则按编写顺序执行）

web.xml常用组件解析：

```xml
<web-app>
<display-name></display-name>
WEB应用的名字 
  
<description></description> 
WEB应用的描述    

<context-param></context-param> 
context-param元素声明应用范围内的初始化参数

 <!-- 指定spring配置文件位置 -->    
<context-param>    
  <param-name>contextConfigLocation</param-name>    
  <param-value>    
<!--加载多个spring配置文件 -->    
/WEB-INF/applicationContext.xml, /WEB-INF/action-servlet.xml    
  </param-value>    
</context-param>    

<filter></filter> 
过滤器将一个名字与一个实现javax.servlet.Filter接口的类相关联    

<filter-mapping></filter-mapping> 
一旦命名了一个过滤器，就要利用filter-mapping元素把它
与一个或多个servlet或JSP页面相关联
   
<listener></listener> 
事件监听程序在建立、修改和删除会话或servlet环境时得到通知。
Listener元素指出事件监听程序类。    

如Log4j这个广泛使用的监听

<!-- 定义SPRING监听器，加载spring -->   
<listener>    
  <listenerclass>
    org.springframework.web.context.ContextLoaderListener
  </listener-class>    
</listener>  

<servlet></servlet> 
在向servlet或JSP页面制定初始化参数或定制URL时，
必须首先命名servlet或JSP页面。
Servlet元素就是用来完成此项任务的。
   
<servlet-mapping></servlet-mapping> 
服务器一般为servlet提供一个缺省的URL：http://host/webAppPrefix/servlet/ServletName。
但是，常常会更改这个URL，
以便servlet可以访问初始化参数或更容易地处理相对URL。
在更改缺省URL时，使用servlet-mapping元素  
    
<welcome-file-list></welcome-file-list> 
指示服务器在收到引用一个目录名而不是文件名的URL时，
使用哪个文件(其实就是欢迎界面或者说入口界面一般为index.*)   

<error-page></error-page>
在返回特定HTTP状态代码时，或者特定类型的异常被抛出时，
能够制定将要显示的页面。    

<taglib></taglib> 
对标记库描述符文件（Tag Libraryu Descriptor file）指定别名。
此功能使你能够更改TLD文件的位置，   
而不用编辑使用这些文件的JSP页面。
   
<resource-env-ref></resource-env-ref>
声明与资源相关的一个管理对象。 
  
<resource-ref></resource-ref> 
声明一个资源工厂使用的外部资源。    

<security-constraint></security-constraint> 
制定应该保护的URL。它与login-config元素联合使用 
  
<login-config></login-config> 
指定服务器应该怎样给试图访问受保护页面的用户授权。
它与sercurity-constraint元素联合使用。    

<security-role></security-role>
给出安全角色的一个列表，这些角色将出现在servlet元素内的
security-role-ref元素的role-name子元素中。
分别地声明角色可使高级IDE处理安全信息更为容易

<env-entry></env-entry>
声明Web应用的环境项
 
</web-app>
```

#### 1、`<context-param> `

1. 启动一个WEB项目的时候,容器(如:Tomcat)会去读它的配置文件web.xml.读两个节点: 

   `<listener></listener> 和 <context-param></context-param> `。

2. 紧接着,容器创建一个ServletContext(上下文),这个WEB项目所有部分都将共享这个上下文。

3. 容器将`<context-param></context-param>`转化为键值对,并交给ServletContext。

4. 容器创建`<listener></listener>`中的类实例,即创建监听。

5. 在监听中会有contextInitialized(ServletContextEvent args)初始化方法,在这个方法中获得

   ```java
   ServletContext = ServletContextEvent.getServletContext(); 
   context-param的值 = ServletContext.getInitParameter("context-param的键"); 
   ```

6.  得到这个context-param的值之后,你就可以做一些操作了.注意,这个时候你的WEB项目还没有完全启动完成.这个动作会比所有的Servlet都要早. 换句话说,这个时候,你对<context-param>中的键值做的操作,将在你的WEB项目完全启动之前被执行。

7. 举例.你可能想在项目启动之前就打开数据库. 那么这里就可以在<context-param>中设置数据库的连接方式,在监听类中初始化数据库的连接. 

   ```xml
   <!-- 加载spring的配置文件 -->
   <context-param>
       <param-name>contextConfigLocation</param-name>
       <param-value>
           /WEB-INF/applicationContext.xml,
           /WEB-INF/action-servlet.xml,
           /WEB-INF/jason-servlet.xml
       </param-value>
   </context-param>
   <listener>
       <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
   </listener>
   ```

   