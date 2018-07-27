# SpringMVC

:one:@RequestMapping可以修饰方法也可以修饰类 

* 类定义处：提供初步的请求映射信息。相对于WEB应用的根目录
* 方法处：提供进一步的细分映射信息，相对于类定义处的URL。若类定义处未标注@RequestMapping，则方法处标注的@RequestMapping相对于WEB应用的根目录。

:two:

```
@RequestMapping(value = "/testMethod", method = RequestMethod.POST)

method指定请求方法
```

:three:

```
@RequestMapping(value = "/testParamsAndHeaders", params = {"username", "age != 10"}, headers = 					{"Accept-Language=zh-CN,zh;q=0.9"})
params是参数
headers是报头？
```

:four:

```
@RequestMapping("/testPathVariable/{id}")
	public String testPathVariable(@PathVariable("id") Integer id) {
		System.out.println(id);
		return SUCCESS;
	}
@PathVariable可以映射URL中的占位符到目标方法的参数中
```

:five:

```
@RequestParam 来映射请求参数. 
value 值即请求参数的参数名 
required 该参数是否必须，默认为 true
defaultValue 请求参数的默认值
@RequestMapping("/testRequestParam")
	public String testRequestParam(@RequestParam(value = "username") String un, @RequestParam(value = "age", required = false, defaultValue = "0") Integer age){
		System.out.println("username:" + un + ", age:" + age);
		return SUCCESS;
	}
```

:six:

```
	@RequestMapping("/testPojo")
	public String testPojo(User user) {
		System.out.println(user);
		return SUCCESS;
	}
	SpringMVC会按请求参数名和POJO属性名进行自动匹配，自动为该对象填充属性值，支持级联属性，如user.username, user.address.city
	
```

:seven:

```
	@RequestMapping("/testServletAPI")
	public void testServletAPI(HttpServletRequest request, HttpServletResponse response, Writer 		writer) throws IOException {
		System.out.println("request:" + request + ", response:" + response);
		writer.write("hello world");
	}
  可以使用Servlet原生的API作为目标方法的参数

```

:eight:

```
	@RequestMapping("/testModelAndView")
	public ModelAndView testModelAndView() {
		String mn = SUCCESS;
		ModelAndView modelAndView = new ModelAndView(mn);
		modelAndView.addObject("time", new Date());
		return modelAndView;
	}
	目标方法的返回值可以是ModelAndView类型，其中可以包括试图和模型信息，SpringMVC会把ModelAndView的model中数据放到request域对象中。
```

:nine:

```
	@RequestMapping("/testMap")
	public String testMap(Map<String, Object> map) {
		System.out.println(map.getClass().getName());
		map.put("names", Arrays.asList("Tom", "Jerry", "Mike"));
		return SUCCESS;
	}
目标方法可以添加Map/ModelMap/Model类型的参数
```

:keycap_ten:

```
@SessionAttributes(value = {"user"}, types = String.class)
@RequestMapping("/springMvc")
@Controller
public class SpringMvcServlet {
	private static final String SUCCESS = "success";
	@RequestMapping("/testSessionAttribute")
	public String testSessionAttribute(Map<String, Object> map) {
		System.out.println(map.getClass().getName());
		map.put("user", new User("liqinglin", "123113"));
		map.put("hhh", "kkkkk");
		return SUCCESS;
	}
}

  	request user:${requestScope.user}
    <br>
    session user:${sessionScope.user}
    <br>
    request string:${requestScope.hhh}
    <br>
    session string:${sessionScope.hhh}
    
    除了可以通过属性名指定需要方法到会话中的属性外，还可以通过模型属性的对象类型指定哪些模型属性需要放到会话中
```

:one::one:

```
@ModelAttribute
	public void getUser(@RequestParam(value="id", required = false) Integer id, Map<String, Object> map){
		User user = new User(1, 12, "lql", "12121");
		System.out.println("从数据库获取对象"+user);
		map.put("user", user);

	}
    有@ModelAttribute标记的方法会在每个目标方法执行之前被SpringMVC调用
    
    /**
	 * 运行流程:
	 * 1. 执行 @ModelAttribute 注解修饰的方法: 从数据库中取出对象, 把对象放入到了 Map 中. 键为: user
	 * 2. SpringMVC 从 Map 中取出 User 对象, 并把表单的请求参数赋给该 User 对象的对应属性.
	 * 3. SpringMVC 把上述对象传入目标方法的参数. 
	 * 注意: 在 @ModelAttribute 修饰的方法中, 放入到 Map 时的键需要和目标方法入参类型的第一个字母小写		*的字符串一致!
	 */
    @RequestMapping("/testModelAttribute")
	public String testModelAttribute(User user) {
		System.out.println("修改后：" + user);
		return SUCCESS;
	}
```

 **SpringMVC 确定目标方法 POJO 类型入参的过程**

1.确定一个 key

​	1). 若目标方法的 POJO 类型的参数没有使用 @ModelAttribute 作为修饰, 则 key 为 POJO 类名第一个字母的				小写
	2). 若使用了  @ModelAttribute 来修饰, 则 key 为 @ModelAttribute 注解的 value 属性值.

2.在 implicitModel 中查找 key 对应的对象, 若存在, 则作为入参传入

​	 1). 若在 @ModelAttribute 标记的方法中在 Map 中保存过, 且 key 和 1 确定的 key 一致, 则会获取到. 

3.若 implicitModel 中不存在 key 对应的对象, 则检查当前的 Handler 是否使用 @SessionAttributes 注解修饰,  若使用了该注解, 且 @SessionAttributes 注解的 value 属性值中包含了 key, 则会从 HttpSession 中来获取 key 所对应的 value 值, 若存在则直接传入到目标方法的入参中. 若不存在则将抛出异常. 

4.若 Handler 没有标识 @SessionAttributes 注解或 @SessionAttributes 注解的 value 值中不包含 key, 则

 会通过反射来创建 POJO 类型的参数, 传入为目标方法的参数

5.SpringMVC 会把 key 和 POJO 类型的对象保存到 implicitModel 中, 进而会保存到 request 中. 

:one::two:

```
xxx-servlet.xml配置：
	<!-- 配置直接转发的页面 -->
	<!-- 可以直接相应转发的页面, 而无需再经过 Handler 的方法.  -->
	<mvc:view-controller path="/success" view-name="success"/>
	
	<!-- 在实际开发中通常都需配置 mvc:annotation-driven 标签 -->
	<mvc:annotation-driven></mvc:annotation-driven>

```

