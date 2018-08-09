#  String MVC

## 随记

* 通过在WEB-INF中配置HiddenHttpMethodFilter来将post变为delete或put

* form标签中要有modelAttribute值，而且form中path名必须为表中对应的值，因为form标签会被用来进行表单回显的

* ${pageContext.request.contextPath}用来获取绝对路径

* 用**`<mvc:default-servlet-handler/>`**来解决静态资源的问题

* 记得要在DispatcherServlet中配置对应的包的位置

  * ```xml
    <context:component-scan base-package="com.zzz.www.demo1,com.zzz.www.demo2"/>
    ```

  * 

## 使用 @RequestMapping 映射请求

*  Spring MVC 使用 @RequestMapping 注解为控制器指定可 以处理哪些 URL 请求 
*  在控制器的类定义及方法定义处都可标注 @RequestMapping 
*  类定义处：提供初步的请求映射信息。相对于 WEB 应用的根目录 
*  方法处：提供进一步的细分映射信息。相对于类定义处的 URL。若 类定义处未标注 @RequestMapping，则方法处标记的 URL 相对于 WEB 应用的根目录 • DispatcherServlet 截获请求后，就通过控制器上 @RequestMapping 提供的映射信息确定请求所对应的处理 方法。
*  @RequestMapping 除了可以使用请求 URL 映射请求外， 还可以使用请求方法、请求参数及请求头映射请求
*  @RequestMapping 的 value、method、params 及 heads 分别表示请求 URL、请求方法、请求参数及请求头的映射条件，他们之间是与的关系，联合使用多个条件可让请求映射 更加精确化。 
## 基本配置 

* 配置 DispatcherServlet：DispatcherServlet 默认加载 /WEB-INF/.xml 的 Spring 配置文件, 启动 WEB 层 的 Spring 容器。可以通过 contextConfigLocation 初始化参数自定义配置文件的位置和名称 
* • 配置视图解析器：视图名称解析器：将视图逻辑 名解析为: /WEB-INF/pages/.jsp 

## @PathVariable映射URL绑定的占位符

* 通过 @PathVariable 可以将 URL 中占位符参数绑定到控制器处理方法的入参中：URL 中的 {xxx} 占位符可以通过 @PathVariable("xxx") 绑定到操作方法的 入参中

* eg：

  * ```java
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id){
        UserDao.delete(id);
        return "redirect:/user/list.action";
    }
    ```

![1532527427492](E:\Program Files\notebook\photos\1532527427492.png)

## 重要注解

### 1.@RequestParam

* ```java
  (@RequestParam(value="username", required=true, defaultValue="zhang") String username)
  //其中required为是否一定需要该值
  ```

### 2.@SessionAttributes: 

* 将模型中的某个属性暂存到 HttpSession 中，以便多个请求之间可以共享这个属性 

* 除了可以通过属性名指定需要放到会话中的属性外(实际上使用的是value属性值),

* 还可以通过模型属性的对象类型指定哪些模型属性需要放到会话中(实际上使用的是types属性值)

* 注意：该注解只能放在类上面，而且可以放多个值，因为其属性都为数组

  * ```java
    @SessionAttributes(value={"currentUser","saveTime"},types={User.class,Date.class})
    ```

### 3.@ModelAttribute: 

* Spring MVC 在调用目标处理方法前，会先逐个调用在方法级上标注了 @ModelAttribute 的方法。 在方法的入参前使用 @ModelAttribute 注解： 
  * 可以从隐含对象中获取隐含的模型数据中获取对象，再将请求参数绑定到对象中，再传入入参 
  * 将方法入参对象添加到模型中 
  * ![1532782369771](E:\Program Files\notebook\photos\1532782369771.png)
* ![1532593079141](E:\Program Files\notebook\photos\1532593079141.png)
* ![1532591913704](E:\Program Files\notebook\photos\1532591913704.png)
* ![1532592941322](E:\Program Files\notebook\photos\1532592941322.png)

### 4.数据格式化注解
* @DateTimeFormat(pattern="yyyy-MM-dd")和@NumberFormat(pattern="#,###,###.#")

  * 记得要使用以下代码

  * ```xml
    <mvc:annotation-driven></mvc:annotation-driven>
    ```


### 5.@ResponseBody

* @Responsebody 注解表示该方法的返回的结果直接写入 HTTP 响应正文（ResponseBody）中，一般在异步获取数据时使用，通常是在使用 @RequestMapping 后，返回值通常解析为跳转路径，加上 @Responsebody 后返回结果不会被解析为跳转路径，而是直接写入HTTP 响应正文中。

### 6.@RequestBody

*  @RequestBody 注解则是将 HTTP 请求正文插入方法中，使用适合的 HttpMessageConverter 将请求体写入某个对象。  

## 重定向或转发

* 在controller的方法中返回String时，前缀加上"redirect:"或"forward"，即可发生重定向或转发



## 调用DELETE和PUT方法

* ```xml
  <!--配置HiddenHttpMethodFilter: 把POST请求转为DELETE,PUT请求-->
  <filter>
       <filter-name>HiddenHttpMethodFilter</filter-name>
       <filter-class>org.springframework.web.filter.HiddenHttpMethodFilter</filter-class>
  </filter>
  <filter-mapping>
       <filter-name>HiddenHttpMethodFilter</filter-name>
       <url-pattern>/*</url-pattern>
  </filter-mapping>
  ```

  ```xml
  <!--在表单中要写以下代码,才能将POST请求转化为想要的请求-->
  <input type="hidden" name="_method" value="PUT">
  ```

  

* ```java
  @RequestMapping(value="/emp", method=RequestMethod.DELETE)
  @RequestMapping(value="/emp", method=RequestMethod.PUT)
  ```

* 

## 配置流程

### 1.先在WEB-INF中配置DispatcherServlet的基本配置

* ```xml
  <servlet>
  	<servlet-name>springDispatcherServlet</servlet-name>
      <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
      <!--设置配置文件的路径-->
      <init-param>
      	<param-name>contextConfigLocation</param-name>
      	<param-value>classpath:springmvc.xml</param-vlaue>
      </init-param>
      <!--使Dispatcher在服务器启动时就初始化-->
      <load-on-startup>1</load-on-startup>
  </servlet>
  <!--Servlet拦截配置-->
  <servlet-mapping>
  	<servlet-name>springDispatcherServlet</servlet-name>
  	<url-pattern>/</url-pattern>
  </servlet-mapping>
  ```

### 2.然后在dispatcherServlet中进行配置

* ```xml
  <!--配制自动扫描的包-->
  <context:component-scan base-package="com.atguigu.springmvc"></context:component-scan>
  <!--配置视图解析器-->
  <bean class="org.springframework.web.servlet.view.InternalResourceViewResource">
  	<property name="prefix" value="/WEB-INF/views/"></property>    
      <property name="suffix" value=".jsp"></property>
  </bean>
  ```

### 3.如有需要，要配置能调用DELETE和PUT方法的过滤器

### 4.如有需要，请进行以下配置

* ```xml
  <!-- 静态资源(js、image等)的访问 -->
  <mvc:default-servlet-handler/>    
  <!-- 开启注解 -->
  <mvc:annotation-driven/>
  <!--在Shiro集成Spring中，一定要记得在DispatcherServlet中书写该代码,将静态文件排除掉,防止出现404-->
  <mvc:resources location="/" mapping="/*" />
  <!--ViewResolver 视图解析器-->
  <!--用于支持Servlet、JSP视图解析-->
  <bean id="jspViewResolver" 			class="org.springframework.web.servlet.view.InternalResourceViewResolver">
      <property name="viewClass" value="org.springframework.web.servlet.view.JstlView"/>
      <property name="prefix" value="/WEB-INF/views/"/>
      <property name="suffix" value=".jsp"/>
  </bean>
  ```

## form标签

* ```html
  <form:form action="${pageContext.request.contextPath}/emp" method="POST"
                 modelAttribute="employee">
  </form:form>
  <!--在该标签中一定要写modelAttribute,且它的值要与controller对应方法中的map的key值相同-->
  ```

* 

* • form:radiobuttons:单选框组标签,用于构造多个单选框 
  * – items:可以是一个 List、String[] 或 Map 
  * – itemValue:指定 radio 的 value 值。可以是集合中 bean 的一个属性值 
  * – itemLabel:指定 radio 的 label 值 
  * – delimiter:多个单选框可以通过 delimiter 指定分隔符 

* ```html
  <!--下拉框-->
  <form:select path="department.id" items="${departments}"
       itemLabel="departmentName" itemValue="id"/>
  
  ```

将get请求转化为post请求

* ```xml
      <script type="text/javascript" src="scripts/jquery-1.9.1.min.js"></script>
      <script type="text/javascript">
          $(function () {
              $(".delete").click(function () {
                  var href = $(this).attr("href");
                  $("form").attr("action",href).submit();
                  return false;
              })
          })
      </script>
  ```

* ```xml
  <!--注意:需要在该链接中写class="delete" -->
  <a class="delete" href="/emp/${emp.id}">Delete</a>
  ```


## 自定义类型转化器和数据的格式化

### 1.自定义类型转化器代码

* ```xml
  <mvc:annotation-driven conversion-service="conversionService"></mvc:annotation-driven>
  <!-- 配置 ConversionService -->
  <bean id="conversionService"
        class="org.springframework.format.support.FormattingConversionServiceFactoryBean">
      <property name="converters">
          <set>
              <ref bean="employeeConverter"/>
          </set>
      </property>
  </bean>
  ```

  ```java
  @Component
  public class EmployeeConverter implements Converter<String, Employee> {
  
     @Override
     public Employee convert(String source) {
        if(source != null){
           String [] vals = source.split("-");
           //GG-gg@atguigu.com-0-105
           if(vals != null && vals.length == 4){
              String lastName = vals[0];
              String email = vals[1];
              Integer gender = Integer.parseInt(vals[2]);
              Department department = new Department();
              department.setId(Integer.parseInt(vals[3]));
              
              Employee employee = new Employee(null, lastName, email, gender, department);
              System.out.println(source + "--convert--" + employee);
              return employee;
           }
        }
        return null;
     }
  
  }
  ```

### 2.数据格式化步骤：

* ```xml
  <mvc:annotation-driven/>
  ```

* ```java
  @DateTimeFormat(partten="yyyy-mm-dd")
  ```

## JSON

* 1.先导入三个jar包

  * jackson-annotations
  * jackson-core
  * jackson-databind

* 2.写入以下代码(可省略)

  ```xml
      <script type="text/javascript" src="scripts/jquery-1.9.1.min.js"/>
      <script type="text/javascript">
          $(function () {
              $("#testJson").click(function(){
                  var url = this.href;
                  var args = {};
                  $.post(url,args,function (data) {
                      for(var i=0;i<data.length;i++){
                          var id = data[i].id;
                          var lastName = data[i].lastName;
                          alert(id + ":" + lastName);
                      }
                  })
                  return false;
              })
          })
      </script>
  ```

* 3.在目标方法中直接返回需要的集合

* 4.在目标方法中加注解@ResponseBody

## HttpMessageConverter**<T>**

* 作用:负责将请求信息转换为一个对象(类型为T)并绑定到处理方法的入参中，将对象(类型为T)输出为响应信息
* ![1533217411088](E:\Program Files\notebook\photos\1533217411088.png)
* 使用@RequestBody / @ResponseBody对处理方法进行标注
* 使用RequestEntity<T> / ResponseEntity<T>作为处理方法的入参或返回值

## 文件上传

* 利用CommonsMultipartResolver来实现MultipartResolver接口 

* 导入io和commons两个jar包

* 用参数MultipartFile来得到文件上传的信息

* ```xml
  <!--配置MultipartResolver-->
  <bean id = "multipartResolver"
        class = "org.springframework.web.multipart.commons.CommonsMultipartResolver">
  	<property name = "defaultEncoding" value = "UTF-8"></property>
      <property name = "maxUploadSize" value="102400"></property>
  </bean>
  ```

* ```xml
  <form action = "testFileUpload" method="POST" enctype = "multipart/form-data">
  	File: <input type = "file" name = "file"/>
  	Desc: <input type = "text" name = "desc"/>
      <input type = "submit" value = "Submit"/>
  </form>
  ```

* ![1533216814108](E:\Program Files\notebook\photos\1533216814108.png)

## 文件下载

* ```java
  @RequestMapping("/testResponseEntity")
  	public ResponseEntity<byte[]> testResponseEntity(HttpSession session) throws IOException{
  		byte [] body = null;
  		ServletContext servletContext = session.getServletContext();
  		InputStream in = servletContext.getResourceAsStream("/files/abc.txt");
  		body = new byte[in.available()];
  		in.read(body);
  
  		HttpHeaders headers = new HttpHeaders();
  		headers.add("Content-Disposition", "attachment;filename=abc.txt");
  
  		HttpStatus statusCode = HttpStatus.OK;
  
  		ResponseEntity<byte[]> response = new ResponseEntity<>(body, headers, statusCode);
  		return response;
  	}
  
  ```

* 

## 数据检验

* 一.如何校验
  * 1.使用JSR303验证标准

  * 2.加入Hibernate validator 验证框架的jar包

  * 3.在SpringMVC配置文件中添加**`<mvc:annotation-driven/>`**

  * 4.需要在bean的属性上添加对应的注解  eg:@Email  @Past  @NotEmpty

  * 5.在目标方法bean类型的前面添加**@Vaild**注解

* 二.验证出错转向到哪一个页面

  * 注意：需校验的Bean对象和其绑定结果的对象或错误对象是成对出现的，它们之间不允许声明其他的入参

* 三.错误消息的显示

  * ```xml
    <!--在jsp中的对应属性位置写该代码-->
    <form:errors path="lastName"></form:errors>
    ```

  * ```java
        @RequestMapping(value = "/emp", method = RequestMethod.POST)
        public String save(@Valid Employee employee, BindingResult result, Map<String, Object> map) {
            System.out.println("save" + employee);
            if (result.getErrorCount() > 0) {
                System.out.println("出错啦");
                for (FieldError error : result.getFieldErrors()) {
                    System.out.println(error.getField() + " : " + error.getDefaultMessage());
                }
                //若验证出错, 则转向定制的页面
                map.put("departments", departmentDao.getDepartments());
                return "input";
            }
            employeeDao.save(employee);
            return "redirect:/emps";
        }
    ```

## 自定义拦截器

* 先实现HandlerInterceptor接口

* 2.在SpringMVC配置文件中配置拦截器

* ```xml
  <!--配置自定义的拦截器-->
  <bean class="com.zzz.www.demo2.Interceptor.FirstInterceptor"></bean>
  <!-- 配置LocaleChanceInterceptor-->
  <bean class="org.springframework.web.servlet.i18n.LocaleChangeInterceptor"></bean>
  <!--配置拦截器(不)作用的路径-->
  <mvc:interceptor>
  	<mvc:mapping path="/emps"/>
      <bean class="com.zzz.www.demo2.Interceptor.FirstInterceptor"/>
  </mvc:interceptor>
  
  ```

## 使用Spring和SpringMVC进行整合

* 问题一：若Spring的IOC容器和SpringMVC的IOC容器扫描的包有重合的部分，就会导致有的bean会被创建两次。
* 解决：
  * 1.使用Spring的IOC容器扫描的包和SpringMVC的IOC容器扫描的包没有重合的部分
  * 2.使用exclude-filter和include-filter子节点来规定只能扫描的注解 
* 问题二：SpringMVC的IOC容器中的bean可以来引用SpringIOC容器中的bean，反之则不行


