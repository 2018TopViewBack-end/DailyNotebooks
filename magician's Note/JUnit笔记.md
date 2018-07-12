

# JUnit

[TOC]



---

### 什么是Junit

* **JUnit是一个Java语言的单元测试框架。 **
* **Junit 测试也是程序员测试，即所谓的白盒测试，它需要程序员知道被测试的代码如何完成功能，以及完成什么样的功能 **
* **`Assert.assertEquals(result, ``1``);`**

###  1.Junit 的几种类似于 @Test 的注解 

* **1.@Test: 测试方法**

  * **a)(expected=XXException.class)如果程序的异常和XXException.class一样，则测试通过**　　　　
  * **b)(timeout=100)如果程序的执行能在100毫秒之内完成，则测试通过**
* **2.@Ignore: 被忽略的测试方法：加上之后，暂时不运行此段代码**
* **3.@Before: 每一个测试方法之前运行**
* **4.@After: 每一个测试方法之后运行**
* **5.@BeforeClass: 方法必须必须要是静态方法（static 声明），所有测试开始之前运行，注意区分before，是所有测试方法**
* **6.@AfterClass: 方法必须要是静态方法（static 声明），所有测试结束之后运行，注意区分 @After**
* **7、@Test(excepted=XX.class) 在运行时忽略某个异常。**
* **8、@Test(timeout=毫秒) 允许程序运行的时间。**
* **9、@Ignore 所修饰的方法被测试器忽略。**
* **10、RunWith 可以修改测试运行器 org.junit.runner.Runner**
---
eg:

```java
public class JunitTest {
    public JunitTest() {
        System.out.println("构造函数");
    }
 
    @BeforeClass
    public static void beforeClass(){
        System.out.println("@BeforeClass");
    }
     
    @Before
    public void befor(){
        System.out.println("@Before");
    }
     
    @Test
    public void test(){
        System.out.println("@Test");
    }
     
    @Ignore
    public void ignore(){
        System.out.println("@Ignore");
    }
     
    @After
    public void after(){
        System.out.println("@After");
    }
     
    @AfterClass
    public static void afterClass(){
        System.out.println("@AfterClass");
    }
}
结果为:
@BeforeClass
构造函数
@Before
@Test
@After
@AfterClass
```



### 2.编写测试类的原则

* **①测试方法上必须使用@Test进行修饰**
* **②测试方法必须使用public void 进行修饰，不能带任何的参数**
* **③新建一个源代码目录来存放我们的测试代码，即将测试代码和项目业务代码分开**
* **④测试类所在的包名应该和被测试类所在的包名保持一致**
* **⑤测试单元中的每个方法必须可以独立测试，测试方法间不能有任何的依赖**
* **⑥测试类使用Test作为类名的后缀（不是必须）**
* **⑦测试方法使用test作为方法名的前缀（不是必须）**

