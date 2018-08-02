# SpringMVC

## @RequestBody

作用（**两个绑定**）： 

​      i) 该注解用于读取Request请求的**body部分数据**，使用系统默认配置的**HttpMessageConverter**进行解析，然后把相应的数据绑定到要返回的对象上；

​      ii) 再把HttpMessageConverter返回的对象数据**绑定到 controller中方法的参数**上。

## @ResponseBody

该注解用于将Controller的方法返回的对象，通过适当的HttpMessageConverter转换为指定格式后，写入到Response对象的body数据区。 与之对应的是@RequestBody（修饰目标方法入参）但不需成对出现（人话版：发一个请求，spring返回一个Json对象或Json数组等）

适用情形：返回的数据不是html标签的页面，而是其他某种格式的数据时（如json、xml等）使用 

范例：

```java
	@ResponseBody
	@RequestMapping("/testJson")
	public Collection<Employee> testJson(){
		return employeeDao.getAll();
	}
```

js：

```xml
<script type="text/javascript" src="scripts/jquery-1.9.1.min.js"></script>
<script type="text/javascript">
	$(function(){
		$("#testJson").click(function(){
			var url = this.href;
			var args = {};
			$.post(url, args, function(data){
				for(var i = 0; i < data.length; i++){
					var id = data[i].id;
					var lastName = data[i].lastName;
					
					alert(id + ": " + lastName);
				}
			});
			return false;
		});
	})
</script>
  
    <!--点此链接进入js-->
<a href="testJson" id="testJson">Test Json</a>

```

实现原理：HttpMessageConverter ，其负责将请求信息转换为一个对象，将对象输出为响应信息

![HttpMeaasgeConverter.png](https://github.com/Medw1nnn/repo-pics/blob/master/HttpMeaasgeConverter.png?raw=true) 

----

## 国际化

三个要求：

1. 在页面上能够根据浏览器语言设置的情况对**文本**（如name、description等）、**时间**、**数值**等进行本地化处理
	. 可以在bean中获取国际化资源文件Locale对应的消息 	
3. 可以通过超链接切换Locale，而不再依赖于浏览器的语言设置情况

三个要求的对应解决方法：

1. 使用JSTL的 **fmt 标签**
2. 在bean中注入 ResourceBundleMessageSource 的实例，使用其对应的getMessage方法即可
3. 配置LocalResolver 和 LocaleChangeIntercepter

----

## 文件上传

通过**MultipartResolver**实现，spring实现了CommonsMultipartResolver实现类

需导包：commons-fileupload、commons-io ，在context中配置**CommonsMultipartResolver**

通过表单上传，handler参数中写MultipartFile

HttpMessageConverter不能用于文件上传但能用于文件下载

思考：多个文件上传

----

## 拦截器

用来对请求进行拦截处理，可通过自定义拦截器实现特定的功能。自定义拦截器必须实现HandlerIntercepter接口 

需在springmvc.xml中配置 用<bean>.用<mvc:intercepter>可配置拦截器作用（不作用）的路径

 **preHandle方法**：若返回值为true，则会继续调用后续的拦截器和目标方法，false则不会。可以用于权限控制、日志、事务等（多个顺序执行）

**postHandle方法**：调用目标方法之后，渲染视图之前被调用。可以对请求域的属性或视图做出修改（多个倒序执行，after方法同）

**afterCompletion方法**：在渲染视图之后被调用。释放资源 

拦截器处理流程：（first为全局，second为单个）

![æ¦æªå¨.png](https://github.com/Medw1nnn/repo-pics/blob/master/%E6%8B%A6%E6%88%AA%E5%99%A8.png?raw=true) 

理解：若secondIntercepter返回false，则执行流程为first.prehandle->second.preHandle->first.afterCompletion.中间的都不执行，当然也不通过dispatcherServlet。

视频少了第51集

------

## 异常处理

### ExceptionHandlerExceptionResolver

主要处理handler中用@ExceptionHandler标注的方法

优先级问题：会根据异常的最近继承关系找到继承深度最浅的那个

### ResponseStatusExceptionResolver

是**HandleExceptionResolver**的实现类，它使用@ResponseStatus注解把Exception映射为HTTP状态码 （该注解可放在类或方法上，可自定状态码，返回错误页面） 

### DefaultHandlerExceptionResolver

也是**HandleExceptionResolver**的实现类，用来处理特定异常（参考源码）

### SimpleMappingExceptionResolver

可在xml中配置，设置出现异常转向的页面。且可在传参数页面上打印Exception

----

## Spring整合SpringMVC

是否需要在web.xml中配置启动springIOC容器的ContextLoaderListener

需要：通常类似数据源、事务、整合其他框架都是放在spring的配置文件中而不是放在springmvc.xml，分多个配置文件用import

- 若spring IOC容器和 springMVC 的IOC容器扫描部分有重合，则有的bean会被创建两次

  解决：

  1. 使两个容器扫描的包没有重合的部分，但若是多模块会比较麻烦

  2. 在**context：component-scan**加上use-default-filters=“false”并使用 **exclude-filter和include-filter子节点**规定扫描哪些**注解**（**注意不是handler**）

     在一个中include则在另一个中exclude 

- 不需要:...

### 两个IOC容器关系

springMVC的IOC容器中的bean可以引用springIOC容器中的bean ，**反之则不行**



- WebUtil、BeanUtil工具类