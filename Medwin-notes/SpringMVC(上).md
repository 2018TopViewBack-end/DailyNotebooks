# SpringMVC

## 概述

## 基本步骤

1. 新建project，步骤为：普通maven-添加web模块-spring依赖

2. 导包

3. 在web.xml中加入DespatcherServlet

4. 新建springmvc.xml并加入包扫描--context:component-scan

5. 在控制器加上@Controller注解，方法用@RequestMapping("/name")映射请求

6. 配置视图解析器：把方法返回值解析为实际的物理视图

   ```xml
   <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver" >
       	<!--前缀-->
           <property name="prefix" value="/WEB-INF/views/" />
       	<!--后缀-->
           <property name="suffix" value=".jsp" />
       </bean>
   ```

   对于**InternalResourceViewResolver**解析器，会做如下解析：

   “**前缀+返回值+后缀**” 得到实际的物理视图(即/WEB-INF/views/xxx.jsp)，然后做转发操作到该页面

   遇到的bug详见bugs.md

- 实际也可以不通过contextConfigLocation配置smvc的配置文件，而使用默认配置文件：/WEB-INF/<servlet-name>-servlet.xml

----



## @RequestMapping

### 使用

为控制器指定可以处理哪些url请求

- 也可加在Controller上，此时定位到方法的url前要加上类的@RequestMapping

的value

### 映射关系

@RequestMapping的**value、method**、params、heads分别表示请求URl、请求方法、请求参数及请求头的映射条件 

method用法：@RequestMapping(value="/test" ,method=**RequestMethod.POST**)

- ### @PathVariable映射URL绑定的占位符

**重要性：引入对RESTful风格url支持**

它可以映射url中的占位符到目标方法的参数中

```java
@RequestMapping("/testPathVariable/{id}")
public String test(@PathVariable("id") Integer id){
    System.out.println("result:"+ id);
	return SUCCESS;    
}
//效果：若跳转到/testPathVariable/xxx，则会输出result：xxx
```

- ### HiddenHttpMethodFilter可将post请求转为put或delete

在web.xml中配置此filter过滤所有请求，使四种请求对应增删查改，符合REST

在发送post时携带一个post时携带一个name="_method"的隐藏域，值为DELETE或PUT

```java
/**
	 * 如何发送 PUT 请求和 DELETE 请求呢 ? 1. 需要配置 HiddenHttpMethodFilter 2. 需要发送 POST 请求
	 * 3. 需要在发送 POST 请求时携带一个 name="_method" 的隐藏域, 值为 DELETE 或 PUT
	 * 
	 * 在 SpringMVC 的目标方法中如何得到 id 呢? 使用 @PathVariable 注解
	 */
	@RequestMapping(value = "/testRest/{id}", method = RequestMethod.PUT)
	public String testRestPut(@PathVariable Integer id) {
		System.out.println("testRest Put: " + id);
		return SUCCESS;
	}
	@RequestMapping(value = "/testRest/{id}", method = RequestMethod.DELETE)
	public String testRestDelete(@PathVariable Integer id) {
		System.out.println("testRest Delete: " + id);
		return SUCCESS;
	}
```



- ### @RequestParam

在方法中获取**请求参数** （/test?username="medwin"&age=18）

```java
@RequestMapping("/test")
public String test(@RequestParam(value="username") String userName,@RequestParam(value="age") Integer age){
    System.out.println("name:"+ username+"age:"+age);
	return SUCCESS;    
}
//若在value后加上 required=false，则不加参数也可以，否则会报错
```



- ### @RequestHeader

  设置请求头。只需了解

- ### @CookieValue

  获取某个cookie的值。了解

- ### 使用pojo作为请求参数

  **在表单中输入pojo的属性值并提交，可以在方法中获取到对象**

  ```java
  @RequestMapping("/testPojo")
  public String testPojo(User user){
      System.out.println(user);
  	return SUCCESS;    
  }
  ```

- ### 使用Servlet 原生API作为入参

  可作为参数的有：

  - HttpServletRequest
  - HttpServletResponse
  - session
  - In/OutputStream
  - ...

- ### 处理模型数据：使用Map作为入参传到页面(常用)

  ```java
  /**
  	 * 目标方法可以添加 Map 类型(实际上也可以是 Model 类型或 ModelMap 类型)的参数. 
  	 * @param map
  	 * @return
  	 */
  	@RequestMapping("/testMap")
  	public String testMap(Map<String, Object> map){
  		System.out.println(map.getClass().getName()); 
  		map.put("names", Arrays.asList("Tom", "Jerry", "Mike"));
  		return SUCCESS;
  	}
  //页面获取map：
  ${requestScope.names}
  ```

  

- ### @RequestMapping支持Ant风格通配符url

  ？-- 匹配文件名中一个字符，* -- 任意字符，** --多层路径

  如@RequestMapping("test/*/abc")

----



## 处理模型数据

###  ModelAndView类

```java
/**
	 * 目标方法的返回值可以是 ModelAndView 类型。 
	 * 其中可以包含视图和模型信息
	 * SpringMVC 会把 ModelAndView 的 model 中数据放入到 request 域对象中，传到页面里
	 * @return
	 */
	@RequestMapping("/testModelAndView")
	public ModelAndView testModelAndView(){
		String viewName = SUCCESS;
		ModelAndView modelAndView = new ModelAndView(viewName);
		
		//添加模型数据到 ModelAndView 中.
		modelAndView.addObject("time", new Date());
		
		return modelAndView;
	}

//页面中，用以下语句即可获取
${requestScope.time}
```



### @SessionAttritubes

```java
/**
	 * @SessionAttributes 除了可以通过属性名指定需要放到会话中的属性外(实际上使用的是 value 属性值),
	 * 还可以通过模型属性的对象类型指定哪些模型属性需要放到会话中(实际上使用的是 types 属性值)
	 * 
	 * 注意: 该注解只能放在类的上面. 而不能修饰方法.
     用法：@SessionAttributes(value={"user"}, types={String.class})
     就可以在session中获取了
	 */
	@RequestMapping("/testSessionAttributes")
	public String testSessionAttributes(Map<String, Object> map){
		User user = new User("Tom", "123456", "tom@atguigu.com", 15);
		map.put("user", user);
		map.put("school", "atguigu");
		return SUCCESS;
	}
```



### @ModelAttritube（重要）

应用场景：表的某个字段不能被修改。实现原理：从数据库取出所有字段，但只更新要修改的字段，再放回去

1. 有 @ModelAttribute 标记的方法, **会在任何目标方法执行之前被 SpringMVC 调用**！
2. @ModelAttribute 注解也可以来修饰目标方法 POJO 类型的入参, 其 value 属性值有如下的作用:
    * 1). SpringMVC 会使用 value 属性值在 implicitModel 中查找对应的对象, 若存在则会直接传入到目标方法的入参中.
    * 2). SpringMVC 会以 value 为 key, POJO 类型的对象为 value, 存入到 request 中. 

```java
	@ModelAttribute
	public void getUser(@RequestParam(value="id",required=false) Integer id, 
			Map<String, Object> map){
		System.out.println("modelAttribute method");
		if(id != null){
			//模拟从数据库中获取对象并放在map中
			User user = new User(1, "Tom", "123456", "tom@atguigu.com", 12);
			System.out.println("从数据库中获取一个对象: " + user);
			
			map.put("user", user); //注意！此处的key必须是User的小写
		}
	}

	@RequestMapping("/testModelAttribute")
	public String testModelAttribute(User user){
		System.out.println("修改: " + user);
		return SUCCESS;
	} //若在此方法的User user前加@ModelAttritube("abc")，则上面那个方法的user键可改为abc
```

**运行流程:**
	 * 1. 执行 @ModelAttribute 注解修饰的方法: 从数据库中取出对象, 把对象放入到了 Map 中. 键为: user
	 * 2. SpringMVC 从 Map 中取出 User 对象, 并把表单的请求参数赋给该 User 对象的对应属性.
	 * 3. SpringMVC 把上述对象传入目标方法的参数. 
	 * 
	 * 注意: 在 @ModelAttribute 修饰的方法中, **放入到 Map 时的键需要和目标方法入参类型的第一个字母小写的字符串一致!**
	 * 
	 * **SpringMVC 确定目标方法 POJO 类型入参的过程**
	 * 1. 确定一个 key:
	 	* 1). 若目标方法的 POJO 类型的参数木有使用 @ModelAttribute 作为修饰, 则 key 为 POJO 类名第一个字母的小写
		 * 2). 若使用了  @ModelAttribute 来修饰, 则 key 为 @ModelAttribute 注解的 value 属性值. 
	 * 2. 在 implicitModel 中查找 key 对应的对象, 若存在, 则作为入参传入
		 * 1). 若在 @ModelAttribute 标记的方法中在 Map 中保存过, 且 key 和 1 确定的 key 一致, 则会获取到. 
	 * 3. 若 implicitModel 中不存在 key 对应的对象, 则检查当前的 Handler 是否使用 @SessionAttributes 注解修饰, 
	 * 若使用了该注解, 且 @SessionAttributes 注解的 value 属性值中包含了 key, 则会从 HttpSession 中来获取 key 所
	 * 对应的 value 值, 若存在则直接传入到目标方法的入参中. 若不存在则将抛出异常. 
	 * 4. 若 Handler 没有标识 @SessionAttributes 注解或 @SessionAttributes 注解的 value 值中不包含 key, 则
	 * 会通过反射来创建 POJO 类型的参数, 传入为目标方法的参数

  *  5. SpringMVC 会把 key 和 POJO 类型的对象保存到 implicitModel 中, 进而会保存到 request 中. 

        

### @SessionAttritube引发的异常

**原因见上方第3点：**

-  若implicitModel 中不存在 key 对应的对象, 则检查当前的 Handler 是否使用 @SessionAttributes 注解修饰, 

  ​	 * 若使用了该注解, 且 @SessionAttributes 注解的 value 属性值中包含了 key, 则会从 HttpSession 中来获取 key 所

  ​	 * 对应的 value 值, 若存在则直接传入到目标方法的入参中. 若不存在则将抛出异常. 

解决方法：1. 在方法入参加上@ModelAttribute改名（少用）

​	       	  2.确保有 @ModelAttribute方法

----

## 视图与视图解析器

视图解析器：如InternalResourceViewResolver用来将逻辑视图转为物理视图

- 请求方法处理完成后，最终返回一个ModelAndView对象。**对于返回String、View或ModelMap等类型的处理方法，SpringMVC也会在内部将它们装配成一个ModelAndView对象**，它包含了逻辑名和模型对象的视图

- smvc借助视图解析器(ViewResolver)得到最终的视图对象(View)，最终的视图可以是jsp

  也可能是Excel等各种表现形式的视图

- View是一个**无状态**的接口，每一个请求都会创建新的对象，所以不存在线程安全问题



### JstlView

1. 导入jstl包

2. springmvc的配置文件 

   配置国际化资源文件 

   ```java
   <bean id="messageSource" class="org.springframework.context.support.ResourceBundleMessageSource">
   		<property name="basename" value="i18n"></property>
   	</bean>
   ```

3. 编写控制器

4. 编写国际化资源文件 i18n.properties等

5. 这样就可在浏览器通过修改语言显示不同语言内容



### mvc : view - controller 标签

 可直接转发到页面而不经过handler（如例中seccess页面此时可用/success跳转）

做法: 在servlet.xml中配置

```xml
<mvc : view - controller path="/success" view-name="success"/> 
```

但只这样配置会造成原来通过handler的链接失效，需配上

```xml
<mvc:annotation-driven/>
```



### 自定义视图

用来整合Excel等视图

```xml
<!-- 配置视图  BeanNameViewResolver 解析器: 使用视图的名字来解析视图 -->
	<!-- 通过 order 属性来定义视图解析器的优先级, order 值越小优先级越高 -->
	<bean class="org.springframework.web.servlet.view.BeanNameViewResolver">
		<property name="order" value="100"></property>
	</bean>
```

----

## 重定向

控制器返回字符串中如带 **forward：**或 **redirect：**前缀，springMVC会进行特殊处理。

即将**forward：**和**redirect：**当成指示符，其后的字符串作为URL来处理。

-- forward:success 会完成一个到success.jsp的**转发**操作

-- redirect:success 会完成一个到success.jsp的**重定向**操作

```java
return "redirect:/index.jsp";
```

----

## REATful 风格SpringMVC 增删查改

### 使用spring的表单标签 

#### 添加操作：

表单具体写法见视频范例2

可以将模型数据中的属性和html表单元素相绑定，以实现表单数据**更便捷编辑和表单值回显**

一般情况下，通过GET请求获取表单页面，通过POST请求提交表单页面，因此两个页面URL相同。（REST）

- **注意**！要在表单上通过**ModelAttritube**属性指定绑定的模型属性（modelAttritube="xxx"），若没有指定，则默认从request域对象中读取**command的表单bean**，如果该属性值也不存在，则报错

```java
	//提交新建的employee
	@RequestMapping(value="/emp", method=RequestMethod.POST)
	public String save(@Valid Employee employee, Errors result, 
			Map<String, Object> map){
		System.out.println("save: " + employee);
		
		if(result.getErrorCount() > 0){
			System.out.println("出错了!");
			for(FieldError error:result.getFieldErrors()){
				System.out.println(error.getField() + ":" + error.getDefaultMessage());
			}
			//若验证出错, 则转向定制的页面
			map.put("departments", departmentDao.getDepartments());
			return "input";
		}
		employeeDao.save(employee);
		return "redirect:/emps";
	}
	//显示表单页面
	@RequestMapping(value="/emp", method=RequestMethod.GET)
	public String input(Map<String, Object> map){
		map.put("departments", departmentDao.getDepartments());
		map.put("employee", new Employee()); //代替默认的command，new一个空的employee放到modelAttritube中
		return "input";
	}
```



#### 删除操作：

handler：

```java
@RequestMapping(value="/emp/{id}", method=RequestMethod.DELETE)
	public String delete(@PathVariable("id") Integer id){
		employeeDao.delete(id);
		return "redirect:/emps";
	}
```

需要借助**javascript**，才能用HiddenHttpMethodFilter**把get请求转为delete** (需加入处理静态资源的配置：default-servlet-handler，因其默认被springMVC拦截，同时要配annotation-driven)

```javascript
<script type="text/javascript" src="scripts/jquery-1.9.1.min.js"></script>
<script type="text/javascript">
	$(function(){
		$(".delete").click(function(){
			var href = $(this).attr("href");
			$("form").attr("action", href).submit();			
			return false;
		});
	})
</script>
```

- **default-servlet-handler** 将在 SpringMVC 上下文中定义一个 **DefaultServletHttpRequestHandler**,它会对进入 DispatcherServlet 的请求进行筛查, 如果发现是没有经过映射的请求, 就将该请求交由 WEB 应用服务器默认的 Servlet 处理. 如果不是静态资源的请求，才由 DispatcherServlet 继续处理

  一般 WEB 应用服务器默认的 Servlet 的名称都是 default.

```xml
<!--jsp中有此form才能使请求的转换有效，即把get转为post再转为delete！-->	
	<form action="" method="POST">
		<input type="hidden" name="_method" value="DELETE"/>
	</form>

<!--delete按钮-->
<a class="delete" href="emp/${emp.id}">Delete</a>
```

### 修改操作：

显示修改页面用**GET**，提交修改用**PUT**

在handler中，和添加操作无法用同一个方法（因需要@PathVariable）

```java
	@RequestMapping(value="/emp/{id}", method=RequestMethod.GET)
	public String input(@PathVariable("id") Integer id, Map<String, Object> map){
		map.put("employee", employeeDao.get(id));
		map.put("departments", departmentDao.getDepartments());
		return "input";
	}

	@RequestMapping(value="/emp", method=RequestMethod.PUT)
	public String update(Employee employee){
		employeeDao.save(employee);
		//和插入一样用save。通过是否有id判断操作
		return "redirect:/emps";
	}
	//不写ModelAttribute就会出错
	@ModelAttribute
	public void getEmployee(@RequestParam(value="id",required=false) Integer id,
			Map<String, Object> map){
		if(id != null){
            //id不空则为修改操作
			map.put("employee", employeeDao.get(id));
		}
	}
```

添加功能：**在修改时lastName属性不能显示**（不能修改）

```xml
		<c:if test="${employee.id == null }">
			<!-- path 属性对应 html 表单标签的 name 属性值 -->
			LastName: <form:input path="lastName"/>
			<form:errors path="lastName"></form:errors>
		</c:if>
		<c:if test="${employee.id != null }">
			<form:hidden path="id"/>
			<input type="hidden" name="_method" value="PUT"/>
			<%-- 转换为PUT请求。对于 _method 不能使用 form:hidden 标签, 因为 modelAttribute 对应的 bean 中没有 _method 这个属性 --%>
		</c:if>
```

注意，form：form的action应该用绝对路径，否则执行修改时会报错

```xml
<form:form action="${pageContext.request.contextPath }/emp" method="POST" 
		modelAttribute="employee">
```



### 数据绑定流程

表单输入数据类型转换、数据类型格式化、数据校验

![æ°æ®ç"å®æµç¨.png](https://github.com/Medw1nnn/repo-pics/blob/master/%E6%95%B0%E6%8D%AE%E7%BB%91%E5%AE%9A%E6%B5%81%E7%A8%8B.png?raw=true) 

可自定义类型转换器 配置ConversionService

### @InitBinder  对binder进行初始化

被标注的方法不能有返回值，参数通常为WebDataBinder

适用情形：用checkbox设置user的role，获取到的值数字，需要映射为role

### 数据格式化

以birthday、salary为例

```java
@DateTimeFormat(pattern="yyyy-MM-dd")
    private Date birth;

@NumberFormat(pattern="#,###,###.#")
    private Float salary;
```

###  JSR303数据校验 

jsr303是java的一个数据校验标准框架，需导入hibernate validator验证框架的jar包

在spring容器中定义LocalValidatorFactoryBean，在bean的属性上添加**@NotNull、@Max**等对应注解，在目标方法bean类型的签名添加**@Valid**实现校验。（见上文save方法）

适用情形：如需要用户输入的时间比当前系统时间早 

注意：需校验的bean对象和其绑定结果对象或错误对象是成对出现的，他们之间不允许声明其他的入参

### 错误消息

页面上显示所有错误消息

```xml
<form:errors path="*"></form:errors>
```

或将*改为对应字段显示对应错误消息

#### 错误消息的定制

在国际化资源文件i18n.properties中定制键值对并在spring.xml中配置

----

