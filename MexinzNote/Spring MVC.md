# Spring MVC

## 注解

### **@RequestMapping**

- 用于修饰方法/类，表示访问的路径。

- 类定义处：提供初步的请求映射信息，相对于WEB应用的根目录。方法处：提供进一步的细分映射信息，相对于类定义处的URL。若类定义处未标注@RequestMapping，则方法处标记的URL是相对于WEB应用的根目录。

1. 它除了可以使用请求URL映射请求，还可以使用请求方法、请求参数及请求头映射请求
2. @RequestMapping  的value、method、params和heads分别表示请求URL、请求方法、请求参数及请求头。**他们之间是与的关系**，联合使用多个条件可以使请求映射更加精确化
3. params和headers支持简单的表达式，下面给出params的几个例子，headers与其类似
   - param1：表示请求必须包含名为param1的请求参数
   - !param1：表示请求不能包含名为param1的请求参数
   - param1 != value1：表示请求包含名为param1的请求参数，但其值不能为value1
   - {"param1=value1","param2"}：请求必须包含名为param1和param2的两个请求参数，且param1参数的值必须为value1
4. 支持Ant风格的URL，Ant风格资源地址又支持3种匹配符
   - ?： 匹配文件名中的一个字符
   - *：匹配文件名中的任意字符  
   - ** **：匹配多层路径    即/User/ ****/createUser 匹配/user/createUser、/user/aaa/bbb/createUser等URL

### **@PathVariable**

- 可以映射URL中的占位符到目标方法的参数中，占位符的名称必须与该注解中相同

```java
/**（Rest风格的URL）
     * @PathVariable 可以映射URL中的占位符到目标方法的参数中，占位符的名称必须与该注解中相同
     * @param id
     * @return
     */
    @RequestMapping(value = "/testPathVariable/{id}")
    public String testPathVariable(@PathVariable("id") Integer id) {
        System.out.println("传参成功："+id);

        return "success";
    }
```

### **@RequestParam**

- 映射请求参数

```java
/**
     * @RequestParam 来映射请求参数
     * value 值即为请求参数的参数名
     * required 指定该参数是否必须要有，默认为true，即一定要有
     * defaultValue 请求参数的默认值
     * @param username
     * @param age
     */
    @RequestMapping(value = "/testRequestParam")
    public String testRequestParam(@RequestParam(value = "username") String username,
                                   @RequestParam(value = "age", required = false, defaultValue = "0") Integer age){

        System.out.println("testRequestParam, username:"+username+",age:"+ age);
        return SUCCESS;
    }
```

### @RequestHeader

- 映射请求头信息。用法同@RequestParam，但是不常用

```java
@RequestMapping(value = "/testRequestHeader")
    public String testRequestHeader(@RequestHeader(value = "Accept-Encoding")String header) {
        System.out.println("testRequestHeader："+header);
        return SUCCESS;
    }
```

### @CookieValue

-  映射一个Cookie值，可以打开控制台查看，用法同 @RequestParam，不常用

```java
/**
     * @CookieValue 映射一个Cookie值，用法同 @RequestParam
     * @param sessionId
     * @return
     */
    @RequestMapping(value = "/testCookieValue")
    public String testCookieValue(@CookieValue("JSESSIONID") String  sessionId) {
        System.out.println("testCookieValue : "+ sessionId);
        return SUCCESS;
    }
```

### @SessionAttributes 

- 该注解用于指定一些属性放在session域中（默认是request）。
- 除了可以通过属性名指定需要放到会话中的属性外（实际使用的是value属性值），还可以通过模型属性的**对象类型**（class类型）指定哪些模型属性需要放到会话中（实际上使用的是types属性值）
- **该注解只能放在类的上面，而不能放在方法上，且Map及ModelAndView都适用**

```java
/**
 * 在控制器类上@SessionAttributes({"user"}),
 * 就会把map里面的这个User即放在session域中也放在request 域中
 * 里面value属性可以放一个字符串数组，即可以放多个map的键值
 * 也可以通过types属性放class类型
 * @param map
 * @return
 */
@RequestMapping("/testSessionAttributes")
public String testSessionAttributes(Map<String,Object> map) {
    User user  = new User("Tom","123455","123123@qq.com",10);
    map.put("user",user);
    map.put("school","GDUT");
    map.put("number",100);	//这个session域就取不出来，因为没配
    return SUCCESS;
}

//类上的注解
@SessionAttributes(value = {"user"},types = {String.class})
```

### @ModelAttribute

- 由该注解标记的方法，会在**每个目标方法执行之前被Spring MVC调用**

- 运行流程：

  1. 执行@ModelAttribute注解修饰的方法：从数据库中取出对象，把对象放入到了Map中，键为user
  2. Spring MVC 从Map中取出User对象，并把表单的请求参数赋给该User对象的对应属性
  3. Spring MVC 把上述对象传入目标方法的参数

  注意：在@ModelAttribute修饰的方法中，放入到Map时的**键**需要和目标方法**入参类型的名称**（即字符串，并把该入参类型名称的第一个字母改为小写）一致。

- **重要：**

  1. 有@ModelAttribute标记的方法，会在每个目标方法执行之前被Spring MVC调用
  2. @ModelAttribute注解也可以来修饰目标方法POJO类型的入参，其value属性值有如下的作用：
     - Spring MVC会使用value属性值在 implicitModel 中查找对应的对象，若存在则会直接传入到目标方法的如参中
     - Spring MVC 会以value(注解的属性)为key，POJO类的对象为value(map的value)，存入request中

```jsp
<!--模拟修改操作
1. 原始数据为 1，Tom，123456，tom@qq.com，12
2. 密码不能被修改
3. 表单回显，模拟操作直接在表单填写对应的属性值
-->
<form method="post" action="/springmvc/testModelAttribute">
    <input name="id" type="hidden" value="1">
    username:<input type="text" name="username" value="Tom">
    <br>
    email:<input type="text" name="email" value="tom@qq.com">
    <br>
    age:<input type="text" name="age" value="12">
    <br>
    <input type="submit" value="Submit">
</form>
```

```java
//这个例子是user修改信息，密码不能直接暴露在(hidden也不行)jsp页面上，所以使用这个注解先从数据库中查询到原来的User信息，只显示除了password的信息在页面上供用户修改，而不使用@ModelAttribute的话，直接读取页面信息，password是null的
@RequestMapping("/testModelAttribute")
//下面User参数前的xxx代表下面的getUser方法存进去的user对象的key，一致才可以取出下面存进去的那个user并更改想要更改的信息，不一致的话会通过反射获取一个新的对象
//如果不使用@ModelAttribute注解则默认的key为这个参数POJO类名且首字母小写
public String testModelAttribute(@ModelAttribute("xxx") User user) {
    System.out.println("修改："+ user);
    return "success";
}

@ModelAttribute
public void getUser(@RequestParam(value = "id",required = false) Integer id, Map<String,Object> map) {
    System.out.println("modelAttribute");
    if(id != null) {
        //模拟数据库中获取对象
        User user = new User(id,"Tom","123456","tom@qq.com",12);
        System.out.println("从数据库中获取一个对象：" + user);

        map.put("user",user);
    }
}
```

- 下面划红线的地方的解释：如果目标方法的参数的key值使用了@ModelAttribute指定，然后implicitModel中不存在该key对应的对象，而且在类上@SessionAttributes注解的value值包含了该key值，如果存在会传入到目标方法的入参中，不存在会抛异常

![image](E:\2018summer copy\MexinzNote\微信截图_20180728203817.png)



## Handler方法参数

### POJO

- Spring MVC会按请求的参数名和POJO类的属性名进行自动匹配 (前提是两者完全相同)，自动为该对象填充属性值。同时支持级联属性（即对象里还有对象）

- 下面是一个表单的例子：

  ```jsp
  <form action="/springmvc/testPojo" method="post">
          username:<input type="text" name="username"/><br>
          password:<input type="password" name="password"><br>
          email:<input type="text" name="email"><br>
          age:<input type="text" name="age"><br>
          <!--级联属性，User对象中包着一个Address对象，下面两个是Address对象的属性，注意name的书写-->
          province:<input type="text" name="address.province"><br>
          city:<input type="text" name="address.city"><br>
          <input type="submit" value="Submit">
      </form>
  ```

  对应的java代码：

  ```java
  //测试类 对应上面表单的action
  @RequestMapping("/testPojo")
  public String testPojo(User user) {
      System.out.println("testPojo: " + user);
      return SUCCESS;
  }
  
  //属性名和表单中的name保持完全一致
  public class User {
      private String username;
      private String password;
      private String email;
      private int age;
      private Address address;
      
      /* getter and setter */
  }
  
  //属性名和表单中的name保持完全一致
  public class Address {
      private String province;
      private String city;
      
      /* getter and setter */
  }
  ```

### Servlet原生API

-  可以使用Servlet的原生API作为目标方法的参数，具体支持以下类型：HttpServletRequest、HttpServletResponse、HttpSession、java.security.Principal、Locale、InputStream。OutputStream、Reader、Writer

  ```java
  /**
       * 可以使用Servlet的原生API作为目标方法的参数
       * @param request
       * @param response
       * @return
       */
  @RequestMapping("/testServletAPI")
  public String testServletAPI(HttpServletRequest request, HttpServletResponse response) {
      System.out.println("testServletAPI, "+request+", "+response);
      return SUCCESS;
  }
  ```

## 处理模型数据

### **ModelAndView**

- 控制器处理方法的返回值如果为ModelAndView，则其**既包含视图信息，也包含模型数据信息**

  ```java
  /** 
       * SpringMVC会把ModelAndView的 model中数据放 到 request域对象中
       * @return
       */
  @RequestMapping("/testModelAndView")
  public ModelAndView testModelAndView() {
      String viewName = "success";	//到success.jsp
      ModelAndView modelAndView = new ModelAndView(viewName);
  
      //添加模型数据到ModelAndView中
      modelAndView.addObject("time",new Date());
      return modelAndView;
  }
  
  //success.jsp
  time:${requestScope.time}
  ```

### Map及Model

- 目标方法的入参可以加Map或Model类型的参数

  ```java
  /**
       * 目标方法可以添加 Map类型的参数，实际上也可以是Model类型或ModelMap类型的参数
       * @param map
       * @return
       */
  @RequestMapping("/testMap")
  public String testMap(Map<String,Object> map) {
      map.put("names",Arrays.asList("Tom","Jerry","Mike"));
      return “success;
  }
  
  //success.jsp（目标页面） names和上面map的键相同
  names:${requestScope.names}
  ```

## 视图解析

- 无论目标方法返回的是String、ModelAndView、ModelMap 还是 View，Spring MVC都会将其转化成一个ModelAndView对象，它包含了逻辑名和模型对象的视图
- Spring MVC借助视图解析器（**ViewResolver**）得到最终的视图对象（**View**），再调用view的render()方法得到最终的响应结果。最终的视图是以JSP，也可能是其他表现形式的视图
- 视图是无状态的（每个请求都会创建一个新的视图对象），因此不会有线程安全的问题。
- 默认用的是 InternalResourceView 和 InternalResourceViewResolver

### 流程

- 目标方法返回值 --->ModelAndView ---(ViewResolver视图解析器解析)--->View(视图对象) ---(调用render()方法渲染视图)---->得到最终的视图

## JSTL

- 若项目中使用了JSTL，则Spring MVC会自动把视图由 InternalResourceView 转为 JstlView

## i18n国际化文件

- 若使用JSTL的 fmt 标签则需要在Spring MVC的配置文件中配置国际化资源文件 i18n.properties 。不同国家的使用下划线，如美国则 i18n_en_US.properties，中国则 i18n_zh_CN.properties，**然后会根据浏览器设置的国家和语言来自动适配**

  ![image](E:\2018summer copy\MexinzNote\微信截图_20180728223043.png)

  

- 在使用到上述properties文件的jsp页面进行如下操作

  1. 配置国际化资源文件，在dispatcher-servlet.xml中

     ```xml
     <!--配置国际化资源文件-->
     <bean id="messageSource" class="org.springframework.context.support.ResourceBundleMessageSource">
         <property name="basename" value="i18n"/>
     </bean>
     ```

  2. <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

  3. 使用这种格式来使用properties文件中配置的东西：

     ```jsp
     <!--引用了该文件中的username和password属性-->
     <fmt:message key="i18n.username"/>
     <fmt:message key="i18n.password"/>
     ```

## 不经过handler的页面

- 配置直接转发的页面，不需要经过handler的方法（即直接响应通过Spring MVC 渲染的画面），在dispatcher-servlet.xml中配置

  ```xml
  <!--controller为访问的路径，view-name为相应的JSP页面的名称-->
  <!--记得配上mvc:annotation-driven标签，之前需要经过handler的方法的那些请求也可以正常使用-->
  <mvc:view-controller path="controller" view-name="success"/>
  ```

## 自定义视图

- 在 dispatcher-servlet.xml 文件中，配置 BeanNameViewResolver 解析器：

  ```xml
  <!--配置视图 BeanNameViewResolver 解析器: 使用视图的名字来解析视图-->
  <!--通过order属性来定义视图解析器的优先级，order值越小优先级越高。默认的视图解析器的order值是MAXInteger-->
  <bean class="org.springframework.web.servlet.view.BeanNameViewResolver">
      <property name="order" value="100"/>
  </bean>
  ```

- 新建一个HelloView类，实现View接口

  ```java
  //加上这个注解表示扫描这个类
  @Component
  public class HelloView implements View {
      @Override
      public String getContentType() {
          return "text/html";
      }
      
      @Override
      public void render(Map<String, ?> map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
          //页面显示的内容
          httpServletResponse.getWriter().print("hello view, time：" + new Date());
      }
  }
  ```

- controller 内的代码

  ```java
  @RequestMapping("/testView")
      public String testView() {
          System.out.println("testView");
          //上面那个视图类的名字，小写首字母
          return "helloView";
      }
  ```

  

## 重定向

- 如果返回的字符串带 forward: 或 redirect: 前缀时，Spring MVC 会对他们进行特殊处理：将forward:和redirect:当成指示符，**其后的字符串作为URL来处理**
  - redirect:success.jsp 会完成一个到 success.jsp 的重定向操作
  - forward:success.jsp 会完成一个到 success.jsp 的转发操作

## 表单标签

- 使用form标签的原因：
  1. 可以更快速地开发出表单页面（一些属性例如选择框不需要循环）
  2. 可以更方便地进行表单值的回显
- path属性对应html表单标签的name属性
- 表单标签若要正常显示，必须在请求域中有一个bean，bean的属性需要与表单中的字段一一对应，即为modelAttribute属性。默认为command
- modelAttribute的属性和表单中元素的path属性是对应的，modelAttribute没有的属性，path不能有
- 在一些需要循环的标签中（如radioButtons，select），items可以是一个List、String[]或Map；itemValue可以是集合bean中的一个属性值；itemLabel:指定radio的label值；delimiter指定radioButtons即单选框之间的分隔符

## tips

- ![image](E:\2018summer copy\MexinzNote\微信图片_20180730101252.png)

## 数据类型转换、格式化、校验

- 倘若表单中需要收集用户的生日，用户POJO类中生日是用Date类型储存的，而表单中输入的类型却是字符串，因此这里涉及到数据类型转换的问题

- 时间类型：@DateTimeFormat，可以对Date、Calendar、java.long.Long时间类型进行标注

  1. 在POJO类的属性上加上@DateTimeFormat注解，括号中的格式即为输入的字符串的格式，只有这种类型的字符串才可以被成功转换，否则会报错

     ```java
     @DateTimeFormat(pattern="yyyy-MM-dd")
     private Date birth;
     ```

  2. jsp页面中代码，没啥特别的，一个普通的字符串输入

     ```java
     Birth:<form:input path="birth"/>
     ```

  3. **在springmvc的xml配置文件中添加**：

     ```xml
     <mvc:annotation-driven/>
     ```

  ![image](E:\2018summer copy\MexinzNote\微信截图_20180730152919.png)

- 倘若表单中需要收集用户的薪资，用户POJO类中薪资是用Double类型储存的，而表单中输入的类型却是字符串，薪资经常是用逗号把每3位分开，因此这里涉及到数据类型转换的问题

- 小数类型：@NumberFormat

  1. 在POJO类的属性上加上@NumberFormat注解，括号中的格式即为输入的字符串的格式，只有这种类型的字符串才可以被成功转换，否则会报错

     ```java
     @NumberFormat(pattern="#,###,###.#")
     private Float salary;
     ```

  2. 3.同上

     ![image](E:\2018summer copy\MexinzNote\微信截图_20180730153454.png)

### 数据校验

- 简介：

  ![image](E:\2018summer copy\MexinzNote\微信截图_20180730155942.png)

![image](E:\2018summer copy\MexinzNote\微信截图_20180730160021.png)

- **操作顺序**：

  1. 使用JSR 303 验证标准
  2. 加入 hibernate validator 验证框架 (跟hibernate没啥关系) 的jar包
  3. 在Spring MVC 配置文件中添加 **<mvc:annotation-driven>**
  4. 需要在bean的属性上添加对应的注解
  5. 在目标方法bean类型的前面添加 **@Valid**

  6. 注意：@NotEmpty 只能用于对String 、 Collection 或 array 字段的注解， 其他的 就不行 

- **打印出错消息**：（控制台）

  ```java
  //handler方法中添加BindingResult参数，出错的信息会存储在这个对象中
  //result为一个BindingResult类的对象
  if (result.getErrorCount() > 0) {
              System.out.println("出错啦！！");
              for (FieldError error : result.getFieldErrors()) {
                  System.out.println(error.getField()+" : "+error.getDefaultMessage());
              }
  }
  ```

  注意：**需要校验的Bean对象和其绑定结果对象或错误对象成对出现时，他们之间不允许声明其他的入参**

  在方法中他们是挨着的：![image](E:\2018summer copy\MexinzNote\微信截图_20180730164059.png)

- **在页面中显示错误消息**：

  ```xml
  <!--星号表示显示所有的错误消息，也可以指定属性提醒，编译器会提示-->
  <form:errors path="*"></form:errors>
  ```

- 错误消息定制及国际化

  1. 在springmvc配置文件中，加入配置和先前的一样

  2. **自定义错误信息的规则**：

     ![image](E:\2018summer copy\MexinzNote\微信截图_20180730192703.png)

     ![image](E:\2018summer copy\MexinzNote\微信截图_20180730191212.png)

     

  3. 示例：用的是最上面那张图的第一种，注解.首字母小写类名.属性。POJO类上对应属性记得加上注解

     ```properties
     NotEmpty.employee.lastName=^^LastName不能为空
     Email.employee.email=Email格式不对
     Past.employee.birth=Birth不能是一个将来的时间
     
     typeMismatch.employee.birth=Birth不是一个日期
     ```

     ![image](E:\2018summer copy\MexinzNote\微信截图_20180730192610.png)



## json

### 返回json示例

1. 导入3个jar包

2. 目标方法直接返回你需要的东西，如对应的对象或集合

   ```java
   //注意加上@ResponseBody
   @ResponseBody
   @RequestMapping("/testJson")
   public Collection<Employee> testJson() {
       return employeeDao.getAll();
   }
   ```

### 原理

- 接口：HttpMessageConverter**<T>**

- 用处：负责将请求信息转换为一个对象（类型为T），将对象（类型为T）输出为响应信息

- 工作原理：

  ![image](E:\2018summer copy\MexinzNote\微信截图_20180730210615.png)

- 解释：

  - HttpInputMessage中只有一个方法，InputStream getBody()，将请求信息转化为输入流
  - HttpOutputMessage中只有一个方法，OutputStream getBody()，将响应信息转化为输出流

### 两个注解

- @ResponseBody  修饰目标方法，将响应信息转化为方法声明的返回类型，直接返回给客户端

- @RequestBody 修饰目标方法的入参，将请求信息转化为方法的入参类型，这样在方法内部可以使用（想正常入参一样）

- 上面两者可以单独出现，看需要用到哪个

- HttpEntity**<T>**和ResponseEntity**<T>**，可以替代上面的两个注释。使用ResponseEntity**<T>**作为返回类型，HttpEntity**<T>**作为入参类型。比起注解，他们的好处是具有**额外的灵活性来定义任意HTTP响应头 **。使用这个构造方法：

  ```java
  //方便设置响应头和响应码
  ResponseEntity(T body, MultiValueMap<String,String> headers, HttpStatus statusCode) 
  ```

- 可以用 ResponseEntity<byte[]> 做文件下载

![image](E:\2018summer copy\MexinzNote\notePic\微信截图_20180730212131.png)



## 将POST转为DELETE

- 先将 jquery-1.9.1.min.js 文件添加到web目录下 ![image](E:\2018summer copy\MexinzNote\notePic\微信截图_20180731092700.png)

### 静态资源

- 上面的文件是静态资源文件，需要进行特殊处理，否则会出错

  ![image](E:\2018summer copy\MexinzNote\微信截图_20180730232625.png)

  ![image](E:\2018summer copy\MexinzNote\微信截图_20180730231739.png)

- 还要记得在SpringMVC的配置文件中加上  <<mvc:annotation-driven/>>，不然之前那些经过映射的请求（@RequestMapping）的都会失效（404）

- JSP文件配置：

  ```jsp
  <!--删除链接-->
  <td><a class="delete" href="/delete/${emp.id}">Delete</a></td>
  
  <!--将POST请求转为DELETE请求-->
  <form action="" method="post">
          <input type="hidden" name="_method" value="DELETE">
  </form>
  
  <!--jquery部分-->
  <script type="text/javascript" src="scripts/jquery-1.9.1.min.js"></script>
  <script type="text/javascript">
      $(function () {
          $(".delete").click(function () {
              var href = $(this).attr("href");
              $("form").attr("action",href).submit();
              return false;
          });
      })
  </script>
  ```

- handler函数代码：

  ```java
  @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
  public String delete(@PathVariable("id") Integer id) {
      employeeDao.delete(id);   //就是从集合中remove
      //调到另一个handler函数，获取删除后的新的用户列表
      return "redirect:/emps";
  }
  @RequestMapping("/emps")
  public String list(Map<String,Object> map) {
      map.put("employeeDao",employeeDao.getAll());
      return "list";
  }
  ```

## 国际化

1. 在页面上能够根据浏览器的语言设置情况对文本(不是内容)，时间，数值进行本地化处理

   解决：使用 JSTL  的 fmt 标签，可参照上面 i18n 国际化文件的操作

2. 可以在 bean 中获取国际化资源文件 Locale 对应的消息

   解决：在 bean 中注入 ResourceBundleMessageSource  的示例，使用其对应的 getMessage 方法即可

3. 可以通过超链接切换 Locale，而不再依赖于浏览器的语言设置情况

   解决：配置 LocalResolver 和 LocaleChangeInterpretor

## 文件上传

- 接口：MultipartResolver

- 使用上述接口的实现类：CommonsMultpartResolver 实现文件上传功能

- Spring mvc默认没有装配上述接口，需要在上下文手动配置

  ```xml
  <!--配置MultipartResolver-->
  <bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
      <property name="defaultEncoding" value="UTF-8"/>
      <property name="maxUploadSize" value="1024000"/>
  </bean>
  ```

- 示例：一个表单上传文件

  ```jsp
  <form action="/testFileUpload" method="post" enctype="multipart/form-data">
      File:<input type="file" name="file">
      Desc:<input type="text" name="desc">
      <input type="submit" value="Submit">
  </form>
  ```

  对应的handler

  ```java
  @RequestMapping("/testFileUpload")
  public String testFileUpload(@RequestParam("desc") String desc, @RequestParam("file") MultipartFile file) throws IOException {
      System.out.println("desc : " + desc);
      System.out.println("originalFileName : " + file.getOriginalFilename());
      System.out.println("inputStream : " + file.getInputStream());
      
      return xxx;
  }
  
  /* 下面是控制台跑出来的结果 上传了一个名为abcddd.txt的文件 
  desc : example file
  originalFileName : abcddd.txt
  inputStream : java.io.ByteArrayInputStream@e7bf291
  */
  ```

- MultipartFile 接口可以获取上传的文件的许多信息，如文件名，大小，输入流，byte数组等，以下是它的方法

  ```java
  public interface MultipartFile extends InputStreamSource {
      String getName();
      String getOriginalFilename();
      String getContentType();
      boolean isEmpty();
      long getSize();
      byte[] getBytes() throws IOException;
      InputStream getInputStream() throws IOException;
      void transferTo(File var1) throws IOException, IllegalStateException;
  }
  ```

 ## 拦截器

### 自定义拦截器

-  自定义的拦截器必须实现 **HandlerInterceptor** 接口

- 在spring mvc 配置文件中添加自定义拦截器的配置

  ```xml
  <!--配置自定义的拦截器-->
  <mvc:interceptors>
      <!--下面这个是我这个例子自定义拦截器的全限定名-->
      <bean class="com.SpringMVC2.www.interceptor.FirstInterceptor"/>
  </mvc:interceptors>
  ```

- 关于拦截器的三个方法的执行时间和作用：

  ````java
  public class FirstInterceptor implements HandlerInterceptor {
      /**
       * 在目标方法之前被调用
       * 若返回值为true，则继续调用后续的拦截器和目标方法
       * 若返回值为false，则不会再调用后续的拦截器和目标方法
       * 可以用于做 权限、日志、事务等
       */
      @Override
      public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
          return true;
      }
  
      /**
       * 在调用目标方法之后，渲染视图之前被调用
       * 可以对请求域中的属性或视图（该方法传入了ModelAndView参数）作出修改
       */
      @Override
      public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
      }
  
      /**
       * 在渲染视图之后被调用
       * 可以用来释放资源
       */
      @Override
      public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
      }
  }
  ````

  ![image](E:\2018summer copy\MexinzNote\notePic\微信截图_20180731141042.png)

- 拦截器可以指定拦截哪些handler和不拦截哪些handler

  ```xml
  <!--配置自定义的拦截器-->
  <mvc:interceptors>
      <mvc:interceptor>
          <!--配置拦截器作用的路径-->
          <mvc:mapping path="/*/*"/>
          <!--配置拦截器不作用的路径-->        
          <mvc:exclude-mapping path="/emps"/>
          <!--拦截器类的全限定名-->
          <bean class="com.SpringMVC2.www.interceptor.SecondInterceptor"/>
      </mvc:interceptor>
  </mvc:interceptors>
  ```

- 拦截器的**执行顺序**：

  1. preHandle方法与配置文件中的拦截器的配置顺序一致
  2. postHandle与afterCompletion与配置文件中的拦截器的配置顺序相反

  如果后面的拦截器的preHandle返回了false，则先前的拦截器只执行自己的afterCompletion方法。即只要perHandle方法返回了true的拦截器，就一定要执行afterCompletion方法

  ![image](E:\2018summer copy\MexinzNote\notePic\微信截图_20180731150914.png)



## SpringMVC运行流程

![image](E:\2018summer copy\MexinzNote\notePic\微信截图_20180731151933.png)



## SpringMVC 整合 Spring

1. 在 web.xm 中配置启动Spring IOC容器的Listener

	 注意：若Spring 的IOC容器和SpringMVC 的IOC容器扫描的包有重合的部分，就会导致有的Bean会被创建两次	

2. 使用 exclute-filter 和 include-filter 子节点来规定只能扫描或不能扫描的注解，其中的 expression 属性写注解所属的注解类的全限定名

   示例：

   ![image](E:\2018summer copy\MexinzNote\notePic\微信截图_20180731155501.png)

3. SpringMVC 的IOC容器中的 bean 可以来引用 Spring IOC 容器中的bean ，反之则不行 。如：Controller层引用Service层