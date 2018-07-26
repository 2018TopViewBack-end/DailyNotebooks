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

