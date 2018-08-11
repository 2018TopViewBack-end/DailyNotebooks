# Spring MVC前后端数据交互总结

### 控制器 

作为控制器，大体的作用是作为V端的数据接收并且交给M层去处理，然后负责管理V的跳转。SpringMVC的作用不外乎就是如此，主要分为：接收表单或者请求的值，定义过滤器，跳转页面；其实就是servlet的替代品。 

- append 

  Spring MVC在Web应用中扮演V的角色，负责处理HTTP请求并返回相应的资源，它在用的时候要配置一个核心的Dispatcher负责检查资源，请求过来的时候会查找是否有相应的Handler，有就会把请求交Controller，一般使用注解来配置暴露给用户端进行调用。 

```java
@RequestMapping(value="/JSON",produces="text/html;charset=UTF-8")
```

​	value就是对外暴露的接口地址。

 	说到控制器，我们会想到熟悉的Struts2，它可以轻松的调用Request和Response来操作请求响应，如果要在Spring MVC里调用请求响应的话，则需要附带在方法参数上。 

```java
//Request Response
@RequestMapping(value="/ReqAndRep",produces="text/html;charset=UTF-8")
public void reqAndrep(HttpServletRequest request,HttpServletResponse response){
}
```

​	这样就可以控制这个Http请求并且往响应体里写东西了。 

### 传值方式

前后端的数据无非是：**变量，对象，数组，JSON**；他们有可能会有迭代的行为，把基本的搞懂就不怕他们的二次型了。 

springmvc最方便的一点就是可以通过注释方式来定义它的url。 

```java
@Controller
public class formMVC {
    @RequestMapping("/hello")
    public void login(){
}
```

​	如上面这种方式，在项目名下跟着hello就能访问这个方法了，相较struts2的xml配置加大了开发效率，并且								是以方法为级别的开发。 

​	接收表单数据只需要在方法的参数加入相应的字段，对应表单input的name属性，因为是通过反射技术实现的所以字段要完全相同。

### 变量 

```java
 @RequestMapping("/login")
    public String login(String username,String password){
        System.out.println(username+" "+password);
        return "form.jsp";
    }
```

​	变量一般是在表单中指定的name对应的形参，也可以在形参中使用别名。 

```java
 //传入一般参数
    @RequestMapping(value="/Normal",produces="text/html;charset=UTF-8")
    public String normal(HttpServletRequest request,@RequestParam("username") String username,@RequestParam("password") String password){
        System.out.println("username:"+username+" password:"+password);
        request.setAttribute("info", "Normal server recv:"+username+" "+password);
        return "testPage.jsp";
    }
```

​	前端数据页面，写一个Form，input对应别名就好。 

```html
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>测试数据交互</title>
</head>
<body>
    <form action="/Shop/Array" method="post">
        <table>
            <tr>
                <td>用户名:</td>
                <td><input name="username" size="15"></td>
            </tr>
            <tr>
                <td>密码:</td>
                <td><input name="password" size="15"></td>
            </tr>
            <tr>
                <td><button type="submit">提交</button></td>
                <td><button type="reset">重置</button></td>
            </tr>
        </table>
    </form>
    <span>服务器信息：${info}</span>
</body>
</html>
```

​	 如上面这种方式，表单提交之后就会获得值。跳转方式就是使用返回的字符串，springmvc的DispatcherServlet会跳转到字符串的页面。你也可以配置它的前缀后缀。在它的配置文件中配置下面属性，就是在这个return的字符串的前面和后面加入你配置的前缀后缀。比如你return一个index.jsp，配置了前缀就可能是demo/index.jsp这样的demo包里面的index页面。 

```xml
<!-- configure the InternalResourceViewResolver -->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver" 
            id="internalResourceViewResolver">
        <!-- 前缀 -->
        <property name="prefix" value="" />
        <!-- 后缀 -->
        <property name="suffix" value="" />
    </bean>
```

### 对象 	

​	传递对象这回事，说实在的和Struts2没什么区别，Struts2使用ModelDriven或者直接在控制器里新建一个对象，提交Http请求的时候name需要写成：**对象名.字段名**，这样的形式。但是Spring MVC只要你和字段名（类属性名）相同就行了。

```java
 //传入对象
    @RequestMapping(value="/Object",produces="text/html;charset=UTF-8")
    public String objTransaction(HttpServletRequest request,@ModelAttribute Demo d){
        System.out.println("username:"+d.getUsername()+" password:"+d.getPassword());
        request.setAttribute("info", "objTransaction server recv:"+d.getUsername()+" "+d.getPassword());
        return "testPage.jsp";
    } 
```

​	前台仍然是上个页面，改一些Action就行。 

​	另外，Spring MVC可以使用bean来接收参数，因为是反射技术，所以属性字段依然要保持完全一样。 

```java
public class user {
    private String username;
    private String password;
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
}
```



```java
@RequestMapping(value="/Model",method=RequestMethod.POST)
    public String loginModel(user u){
        System.out.println(u.getUsername()+" "+u.getPassword());
        return "form.jsp";
    }　　
```

### **数组** 

　表单提交肯定会有批量提交的需求，批量提交虽然不一定经常能见到，但是学习一定要学全。批量提交就是name共用一个name，一般都是框架给你处理好拿到手上的，所以Spring MVC在方法形参中写List就可以接收批量提交同个变量了。 

```java
//传入数组
    @RequestMapping(value="/Array",produces="text/html;charset=UTF-8")
    public String arrayTransaction(HttpServletRequest request,@RequestParam("username") String[] usernames){
        if(usernames != null)
        {
            for(int i=0;i<usernames.length;i++)
                System.out.println("username:"+usernames[i]);
        }
        request.setAttribute("info", "arrayTransaction server recv");
        return "testPage.jsp";
    }
```

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>测试数据交互</title>
</head>
<body>
    <form action="/Shop/Array" method="post">
        <table>
            <tr>
                <td>用户名:</td>
                <td><input name="username" size="15"></td>
            </tr>
            <tr>
                <td>用户名:</td>
                <td><input name="username" size="15"></td>
            </tr>
            <tr>
                <td><button type="submit">提交</button></td>
                <td><button type="reset">重置</button></td>
            </tr>
        </table>
    </form>
    <span>服务器信息：${info}</span>
</body>
</html>
```

### **JSON** 

​	Spring MVC提供了自动序列化JSON数据的注解，在前端提供JSON数据的时候，可以十分方便的转换。JSON为何如此重要，因为在Restful风格的架构中，传递数据使用JSON差不多可以算是一种标准了，即使是微服务架构，每个服务之间也普遍见到设计成Restful风格的接口，设计Restful风格Spring MVC有着天然的支持，只需要使用它的注解就好了，而返回的字符串可以自己构造。 

```java
package Common.Controller;
import org.springframework.stereotype.Controller;

import net.sf.json.JSONObject;
/**
 * 接口输出JSON形式
 * restful
 * {"status":"0|1|2","info":message,"result":result}
 * status：0.失败；1.成功；2.（未定）；
 * info：返回服务器信息
 * result：返回查询结果
 * @author ctk
 *
 */
@Controller
public class OutputStringController {
    //返回成功信息
    public String success(String info){
        JSONObject result = new JSONObject();
        result.put("status", 1);
        if(info == null)
            result.put("info", "");
        else
            result.put("info", info);
        result.put("result", "");
        return result.toString();
    }
    //返回失败信息
    public String failure(String info){
        JSONObject result = new JSONObject();
        result.put("status",0);
        if(info == null)
            result.put("info", "");
        else
            result.put("info", info);
        result.put("result", "");
        return result.toString();
    }
    //返回供前端使用的result-成功
    public String resultSuccess(String info,String resultStr){
        JSONObject result = new JSONObject();
        result.put("status",1);
        if(info == null)
            result.put("info", "");
        else
            result.put("info", info);
        if(resultStr == null)
            result.put("result", "");
        else
            result.put("result", resultStr);
        return result.toString();
    }
    //返回供前端使用的result-失败
    public String resultFailure(String info,String resultStr){
        JSONObject result = new JSONObject();
        result.put("status",0);
        if(info == null)
            result.put("info", "");
        else
            result.put("info", info);
        if(resultStr == null)
            result.put("result", "");
        else
            result.put("result", resultStr);
        return result.toString();
    }
}
```

​	定义了顶层类之后，编写接口就继承。接口风格如下。  

```java
//restful接口
    @RequestMapping(value = "/start/{name}", produces = "text/html;charset=UTF-8")
    @ResponseBody 
    public String start2(@PathVariable("name") String name1) {
        System.out.println(name1);
        return "start";
    }
```

​	回归正题，要传递JSON，它的过程是前台等待用户输入，用户输入完毕之后把数据构造成JSON Data，然后访问接口。前台的代码我是这样写。 

```java
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
 <script src="../js/jquery/jquery.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSON数据提交</title>
</head>
<body>
<table>
    <tr>
        <td>用户名:</td>
        <td><input id="username"></td>
    </tr>
        <tr>
        <td>密码:</td>
        <td><input id="password"></td>
    </tr>
        <tr>
        <td>姓名:</td>
        <td><input id="name"></td>
    </tr>
        <tr>
        <td>邮箱:</td>
        <td><input id="email"></td>
    </tr>
</table>
<button id="submitForm">提交</button>

<script type="text/javascript">
    $(document).ready(function(){
        $('#submitForm').click(function(){
            var formData = new Object;
            formData.username = $('#username').val();
            formData.password = $('#password').val();
            formData.name = $('#name').val();
            formData.email = $('#email').val();
            var JsonData = JSON.stringify(formData); 
            console.log(JsonData);
            $.ajax({
                type : "POST",
                url : "/Shop/JSON",
                dataType : 'text',  
                contentType: "application/json",
                data : JsonData,
                success : function(data) {
                   alert(data);
                }
            });
        });        
    });
</script>
</body>
</html>
```

​	注意dataType如果使用json那服务器返回什么都会往error走，我初步理解它为返回数据类型。

　　Spring MVC解包需要jackson这个东西，笔者踩过了坑，没加jar包一直访问返回415，不支持此类媒介。笔者使用的是2.5版本的，Spring是4.0的。

​	将其导入到工程中，然后配置applicationContext.xml，就是Spring的配置文件。  

```xml
 <!--json转换器-->
    <bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter">
        <property name="messageConverters">
            <list >
                <ref bean="mappingJacksonHttpMessageConverter" />
            </list>
        </property>
    </bean>
    <bean id="mappingJacksonHttpMessageConverter"
        class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
        <property name="supportedMediaTypes">
            <list>
                <value>text/html;charset=UTF-8</value>
            </list>
        </property>
    </bean>
```

 	然后后台接口如下。

```java
//传入JSON
    @RequestMapping(value="/JSON",produces="text/html;charset=UTF-8")
    @ResponseBody 
    public String jsonTransaction(HttpServletRequest request,@RequestBody Demo ds){
        System.out.println("username:"+ds.getUsername()+" password:"+ds.getPassword()+" name:"+ds.getName()+" email:"+ds.getEmail());
        request.setAttribute("info", "JSON server recv");
        return "success";
    } 
```

​	最后，前端发过来的数据是经过json包装的，依然可以在后端使用bean来接收。 

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="jquery.min.js"></script>  
<title>登录表单</title>
</head>
<script type="text/javascript">  
    $(document).ready(function(){  
        $("#button_submit").click(function(){  
            //序列化表单元素，返回json数据  
            var params = $("#userForm").serializeArray();  
            console.log(params);
            //也可以把表单之外的元素按照name value的格式存进来  
            //params.push({name:"hello",value:"man"});  
            $.ajax({
                type:"post",
                url:"Model",
                data:params
            });
        });  
    });  
</script>  
<body>
    <form id="userForm">
        <input name="username" type="text"/>
        <br/>
        <input name="password" type="password"/>
        <br/>
    </form>
        <button id="button_submit">提交</button>
        <button type="reset" >重置</button>
</body>
</html>
```

