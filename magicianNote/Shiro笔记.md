# Shiro笔记

[TOC]

---

## 参考资料

* [Shiro教程](http://wiki.jikexueyuan.com/project/shiro/overview.html)

## 随记

* 在使用JdbcRealm时，如果需要验证权限，则需要在JdbcRealm实例中打开权限可见开关

  * ```java
    JdbcRealm jdbcRealm = new JdbcRealm();
    jdbcRealm.setDataSource(dataSource);
    //重要
    jdbcRealm.setPermissionsLookupEnabled(true);
    ```

  * ```xml
    <!--在Shiro集成Spring中，一定要记得在DispatcherServlet中书写该代码-->
    <mvc:resources location="/" mapping="/*" />
    ```

  * 

## 介绍

* Shiro可以进行认证、授权、加密、会话管理、与 Web 集成、缓存等 操作 

#### Shiro中的基本功能点 

* ![img](http://wiki.jikexueyuan.com/project/shiro/images/1.png) 

* 图解如下：

  * **Authentication**：身份认证 / 登录，验证用户是不是拥有相应的身份；

    **Authorization**：授权，即权限验证，验证某个已认证的用户是否拥有某个权限；即判断用户是否能做事情，常见的如：验证某个用户是否拥有某个角色。或者细粒度的验证某个用户对某个资源是否具有某个权限；

    **Session Manager**：会话管理，即用户登录后就是一次会话，在没有退出之前，它的所有信息都在会话中；会话可以是普通 JavaSE 环境的，也可以是如 Web 环境的；

    **Cryptography**：加密，保护数据的安全性，如密码加密存储到数据库，而不是明文存储；

    **Web Support**：Web 支持，可以非常容易的集成到 Web 环境；

    **Caching**：缓存，比如用户登录后，其用户信息、拥有的角色 / 权限不必每次去查，这样可以提高效率；

    **Concurrency**：shiro 支持多线程应用的并发验证，即如在一个线程中开启另一个线程，能把权限自动传播过去；

    **Testing**：提供测试支持；

    **Run As**：允许一个用户假装为另一个用户（如果他们允许）的身份进行访问；

    **Remember Me**：记住我，这个是非常常见的功能，即一次登录后，下次再来的话不用登录了。

    **记住一点，Shiro 不会去维护用户、维护权限；这些需要我们自己去设计 / 提供；然后通过相应的接口注入给 Shiro 即可。**

#### 从应用程序角度的来观察如何使用 Shiro 完成工作 

  * ![img](http://wiki.jikexueyuan.com/project/shiro/images/2.png) 
  * 可以看到：应用代码直接交互的对象是 Subject，也就是说 Shiro 的对外 API 核心就是 Subject；其每个 API 的含义：
    * **Subject**：主体，代表了当前 “用户”，这个用户不一定是一个具体的人，与当前应用交互的任何东西都是 Subject，如网络爬虫，机器人等；即一个抽象概念；所有 Subject 都绑定到 SecurityManager，与 Subject 的所有交互都会委托给 SecurityManager；可以把 Subject 认为是一个门面；SecurityManager 才是实际的执行者；
    * **SecurityManager**：安全管理器；即所有与安全有关的操作都会与 SecurityManager 交互；且它管理着所有 Subject；可以看出它是 Shiro 的核心，它负责与后边介绍的其他组件进行交互，如果学习过 SpringMVC，你可以把它看成 DispatcherServlet 前端控制器；
    * **Realm**：域，Shiro 从 Realm 获取安全数据（如用户、角色、权限），就是说 SecurityManager 要验证用户身份，那么它需要从 Realm 获取相应的用户进行比较以确定用户身份是否合法；也需要从 Realm 得到用户相应的角色 / 权限进行验证用户是否能进行操作；可以把 Realm 看成 DataSource，即安全数据源。
  * 应用代码通过 Subject 来进行认证和授权，而 Subject 又委托给 SecurityManager；
  * 我们需要给 Shiro 的 SecurityManager 注入 Realm，从而让 SecurityManager 能得到合法的用户及其权限进行判断。

#### Shiro的组件

* ![img](http://wiki.jikexueyuan.com/project/shiro/images/3.png) 

* **Subject**：主体，可以看到主体可以是任何可以与应用交互的 “用户”；

  **SecurityManager**：相当于 SpringMVC 中的 DispatcherServlet 或者 Struts2 中的 FilterDispatcher；是 Shiro 的心脏；所有具体的交互都通过 SecurityManager 进行控制；它管理着所有 Subject、且负责进行认证和授权、及会话、缓存的管理。

  **Authenticator**：认证器，负责主体认证的，这是一个扩展点，如果用户觉得 Shiro 默认的不好，可以自定义实现；其需要认证策略（Authentication Strategy），即什么情况下算用户认证通过了；

  **Authrizer**：授权器，或者访问控制器，用来决定主体是否有权限进行相应的操作；即控制着用户能访问应用中的哪些功能；

  **Realm**：可以有 1 个或多个 Realm，可以认为是安全实体数据源，即用于获取安全实体的；可以是 JDBC 实现，也可以是 LDAP 实现，或者内存实现等等；由用户提供；注意：Shiro 不知道你的用户 / 权限存储在哪及以何种格式存储；所以我们一般在应用中都需要实现自己的 Realm；

  **SessionManager**：如果写过 Servlet 就应该知道 Session 的概念，Session 呢需要有人去管理它的生命周期，这个组件就是 SessionManager；而 Shiro 并不仅仅可以用在 Web 环境，也可以用在如普通的 JavaSE 环境、EJB 等环境；所有呢，Shiro 就抽象了一个自己的 Session 来管理主体与应用之间交互的数据；这样的话，比如我们在 Web 环境用，刚开始是一台 Web 服务器；接着又上了台 EJB 服务器；这时想把两台服务器的会话数据放到一个地方，这个时候就可以实现自己的分布式会话（如把数据放到 Memcached 服务器）；

  **SessionDAO**：DAO 大家都用过，数据访问对象，用于会话的 CRUD，比如我们想把 Session 保存到数据库，那么可以实现自己的 SessionDAO，通过如 JDBC 写到数据库；比如想把 Session 放到 Memcached 中，可以实现自己的 Memcached SessionDAO；另外 SessionDAO 中可以使用 Cache 进行缓存，以提高性能；

  **CacheManager**：缓存控制器，来管理如用户、角色、权限等的缓存的；因为这些数据基本上很少去改变，放到缓存中后可以提高访问的性能

  **Cryptography**：密码模块，Shiro 提高了一些常见的加密组件用于如密码加密 / 解密的。

## 身份验证

* **身份验证的步骤** ：
  * 收集用户身份 / 凭证，即如用户名 / 密码；
  * 调用 Subject.login 进行登录，如果失败将得到相应的 AuthenticationException 异常，根据异常提示用户错误信息；否则登录成功；
  * 最后调用 Subject.logout 进行退出操作。

## 授权

#### 介绍

* 授权，也叫访问控制，即在应用中控制谁能访问哪些资源（如访问页面/编辑数据/页面操作等）。

####  关键对象

  * **主体**
    主体，即访问应用的用户，在 Shiro 中使用 Subject 代表该用户。用户只有授权后才允许访问相应的资源。

    **资源**
    在应用中用户可以访问的任何东西，比如访问 JSP 页面、查看/编辑某些数据、访问某个业务方法、打印文本等等都是资源。用户只要授权后才能访问。

    **权限**
    安全策略中的原子授权单位，通过权限我们可以表示在应用中用户有没有操作某个资源的权力。即权限表示在应用中用户能不能访问某个资源，如： 访问用户列表页面
    查看/新增/修改/删除用户数据（即很多时候都是 CRUD（增查改删）式权限控制）
    打印文档等等。。。

    如上可以看出，权限代表了用户有没有操作某个资源的权利，即反映在某个资源上的操作允不允许，不反映谁去执行这个操作。所以后续还需要把权限赋予给用户，即定义哪个用户允许在某个资源上做什么操作（权限），Shiro 不会去做这件事情，而是由实现人员提供。

    Shiro 支持粗粒度权限（如用户模块的所有权限）和细粒度权限（操作某个用户的权限，即实例级别的），后续部分介绍。

    **角色**
    角色代表了操作集合，可以理解为权限的集合，一般情况下我们会赋予用户角色而不是权限，即这样用户可以拥有一组权限，赋予权限时比较方便。典型的如：项目经理、技术总监、CTO、开发工程师等都是角色，不同的角色拥有一组不同的权限。

    **隐式角色**：
    即直接通过角色来验证用户有没有操作权限，如在应用中 CTO、技术总监、开发工程师可以使用打印机，假设某天不允许开发工程师使用打印机，此时需要从应用中删除相应代码；再如在应用中 CTO、技术总监可以查看用户、查看权限；突然有一天不允许技术总监查看用户、查看权限了，需要在相关代码中把技术总监角色从判断逻辑中删除掉；即粒度是以角色为单位进行访问控制的，粒度较粗；如果进行修改可能造成多处代码修改。

    **显示角色**：
    在程序中通过权限控制谁能访问某个资源，角色聚合一组权限集合；这样假设哪个角色不能访问某个资源，只需要从角色代表的权限集合中移除即可；无须修改多处代码；即粒度是以资源/实例为单位的；粒度较细。

#### 常用方法

* Shiro 提供了 hasRole/hasRole 用于判断用户是否拥有某个角色/某些权限；但是没有提供如 hashAnyRole 用于判断是否有某些权限中的某一个。 
* Shiro 提供的 checkRole/checkRoles 和 hasRole/hasAllRoles 不同的地方是它在判断为假的情况下会抛出 UnauthorizedException 异常。 
* Shiro 提供了 isPermitted 和 isPermittedAll 用于判断用户是否拥有某个权限或所有权限，也没有提供如 isPermittedAny 用于判断拥有某一个权限的接口。 但是失败的情况下会抛出 UnauthorizedException 异常。 
* 



#### 组件介绍

* Authorizer 的职责是进行授权（访问控制），是 Shiro API 中授权核心的入口点，其提供了相应的角色/权限判断接口
* ModularRealmAuthorizer 用于多 Realm 时的授权匹配 
* PermissionResolver 用于解析权限字符串到 Permission 实例 
* RolePermissionResolver 用于根据角色解析相应的权限集合。 
* 切记：**在ini 配置中，设置 securityManager 的 realms 一定要放到最后**，因为在调用 SecurityManager.setRealms 时会将 realms 设置给 authorizer，并为各个 Realm 设置 permissionResolver 和 rolePermissionResolver。另外，**不能使用 IniSecurityManagerFactory 创建的 IniRealm**，因为其初始化顺序的问题可能造成后续的初始化 Permission 造成影响。 

#### **定义 BitAndWildPermissionResolver 及 BitPermission** 

* BitPermission 用于实现位移方式的权限，如规则是：
  * 权限字符串格式：+ 资源字符串 + 权限位 + 实例 ID；以 + 开头中间通过 + 分割；权限：0 表示所有权限；1 新增（二进制：0001）、2 修改（二进制：0010）、4 删除（二进制：0100）、8 查看（二进制：1000）；如 +user+10 表示对资源 user 拥有修改 / 查看权限。

## JdbcRealm

#### 自定义sql语句

* ```java
  JdbcRealm jdbcRealm = new JdbcRealm();
  jdbcRealm.setDataSource(dataSource);
  jdbcRealm.setPermissionsLookupEnabled(true);
  String sql = "select password from test_user where user_name=?";
  jdbcRealm.setAuthenticationQuery(sql);
  ```

#### 使用JdbcReealm的注意事项：

* 查看权限时，要记得打开开关  **jdbcRealm.setPermissionsLookupEnabled(true);**

## 自定义Realm

#### 步骤

* 1.继承AuthorizingRealm，并重写doGetAuthorizationInfo和doGetAuthenticationInfo方法
* 2.在doGetAuthenticationInfo方法中通过token.getPrincipal获得userName来从数据库中获得password
* 3.将用户信息放入SimpleAuthenticationInfo实例中，便于验证用户信息
* 4.在doGetAuthorizationInfo方法中获取角色数据，便于权限与角色的检验

## Shiro加密

#### 暂无

## Shiro集成Spring

#### 步骤

* 在web.xml中配置过滤器

  * ```xml
    <filter>
        <filter-name>shiroFilter</filter-name>
        <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
    </filter>
    
    <filter-mapping>
        <filter-name>shiroFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
    ```

* 在spring-mvc.xml中进行配置

  * ```xml
    <context:component-scan base-package="com.zzz.shiro.controller"></context:component-scan>
    <mvc:annotation-driven/>
    <!--将静态文件排除掉,防止出现404-->
    <mvc:resources location="/" mapping="/*" />
    ```

* 在spring.xml中进行配置

  * ```xml
    <bean id="shiroFilter" class="org.apache.shiro.spring.web.ShiroFilterFactoryBean">
        <property name="securityManager" ref="securityManager"/>
        <property name="loginUrl" value="login.html"/>
        <property name="unauthorizedUrl" value="403.html"/>
        <!--过滤器链,有序-->
        <property name="filterChainDefinitions">
            <value>
                <!--不需要验证即可访问-->
                /login.html = anon
                /subLogin = anon
                <!--经过认证之后才可以访问相应的路径-->
                /* = authc
            </value>
        </property>
    </bean>
    
    <!--创建SecurityManager对象-->
    <bean class="org.apache.shiro.web.mgt.DefaultWebSecurityManager" id="securityManager">
        <property name="realm" ref="realm"/>
    </bean>
    
    <bean class="com.zzz.shiro.realm.CustomRealm" id="realm">
        <property name="credentialsMatcher" ref="credentialsMatcher"/>
    </bean>
    
    <bean class="org.apache.shiro.authc.credential.HashedCredentialsMatcher" id="credentialsMatcher">
        <property name="hashAlgorithmName" value="md5"/>
        <property name="hashIterations" value="1"/>
    </bean>
    ```

* 在UserController中设置字符编码格式

* ```java
  @RequestMapping(value = "/subLogin", method = RequestMethod.POST,
                  produces = "application/json;charset=utf-8")
  
  ```

#### 从数据库获取信息

* 在spring-dao.xml中配置数据源

* **在spring.xml中将dao配置文件和mapper进行配置或注入**

  * ```xml
    <import resource="spring-dao.xml"/>
    <context:component-scan base-package="com.zzz.shiro"/>
    ```


## 自定义过滤器

#### 步骤

* 新建filter并继承AuthorizationFilter

* ```java
  //重写该方法
  @Override
  protected boolean isAccessAllowed(ServletRequest req,
        ServletResponse resp, Object object) throws Exception {
      
      //获取主体
      Subject subject = getSubject(req, resp);
      
      //将object对象强转为角色数组
      String[] roles = (String[]) object;
      if (roles == null || roles.length == 0) {
          return true;
      }
      for (String role : roles) {
          //可以通过subject.hasRole(role)来获取该主体所拥有的角色
          if (subject.hasRole(role)) {
              return true;
          }
      }
      return false;
  }
  ```

* 在spring.xml中配置filter

* ```xml
  <property name="filters">
      <map>
          <entry key="rolesOr" value-ref="rolesOrFilter" />
      </map>
  </property>
  
  <!-- 自定义filter -->
  <bean id="rolesOrFilter" class="com.zzz.shiro.filter.RolesOrFilter" />
  ```

* 可以在过滤器链中使用

* ```xml
  <bean id="shiroFilter" class="org.apache.shiro.spring.web.ShiroFilterFactoryBean">
      <property name="securityManager" ref="securityManager"/>
      <property name="loginUrl" value="login.html"/>
      <property name="unauthorizedUrl" value="403.html"/>
      <!--过滤器链,有序-->
      <property name="filterChainDefinitions">
          <value>
              <!--不需要验证即可访问-->
              /login.html = anon
              /testRole = roles["admin","admin1"]
              /testRole1 = rolesOr["admin","admin1"]
              /subLogin = anon
              <!--经过认证之后才可以访问相应的路径-->
              /* = authc
          </value>
      </property>
      <property name="filters">
          <map>
              <entry key="rolesOr" value-ref="rolesOrFilter" />
          </map>
      </property>
  </bean>
  ```

* 