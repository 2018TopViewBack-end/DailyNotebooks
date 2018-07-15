# JUnit4

## 规范

- 在使用JUnit框架对项目代码进行测试时，应该将测试代码单独放在一个文件夹test中，与src源代码目录同级，这样在项目开发完毕后删除测试代码，不会对项目源代码产生影响
- 测试类使用**Test**作为类名的**后缀**
- 测试方法使用**test**作为方法名的**前缀**
- 测试类的包要和被测试类的包保持一致 

## 使用方法

- 每个测试方法上必须使用**@Test**进行修饰
- 测试方法必须使用**public void**进行修饰，且该方法**必须是无参的**
- 测试单元中的每个方法必须可以独立测试，测试方法间不能有任何的依赖

## 注意事项

- 测试用例用来达到想要的预期效果，但对于逻辑错误无能为力

## 两种错误

1. **failure**：一般由单元测试所使用的断言方法判断失败所引起的。表示测试点发现了问题，即**程序输出的结果和我们预期的结果不一样**
2. **error**：由代码异常引起的。它可以产生于**代码本身的错误**，也可以是**被测试代码中的一个隐藏的bug**

## 注解

- **@BeforeClass**：其修饰的方法会在所有方法被调用前执行，且该方法为静态(statc)方法，所以当测试类被加载后就会运行它，而且在内存中只存在一份实例，比较适合**加载一些只需要加载一次的东西**（如配置文件）
- **@AfterClass**：其修饰的方法会在所有方法运行结束后执行，且该方法为静态(static)方法。**通常用来对资源进行清理**（如关闭数据库的连接）。
- **@Before**：在每个测试方法（用@Test标注的方法）**前**执行一次
- **@After**：在每个测试方法（用@Test标注的方法）**后**执行一次
- **@Test**：将一个普通的方法修饰成为一个测试方法，其还具有以下两个属性
  1. **expected** = xx.class ：可以用于捕获异常。如果没有捕获到这个异常会报错
  2. **timeout** = xx毫秒：限制运行这个方法的时间，如果超过这个时间会报错。通常用于**测试性能和检测是否存在死循环**
- **@Ignore**：其所修饰的测试方法会被测试运行器忽略，即不会被执行，该标签后可加括号，括号中填写字符串可在运行时输出，可以用于对该方法进行说明（为什么测试时不执行它）
- **@RunWith**：可以更改测试运行器。可以自己编写一个测试运行器类，这个类必须继承org.junit.runner.Runner。但是一般JUnit默认的测试运行器就可以完成大部分的测试工作

## 测试套件

### 用途

有时候一个项目中有许多个测试类，一个个去运行太麻烦，使用测试套件将想运行的测试类添加进去，一次运行即可运行添加的所有测试类

### 使用方法

- 写一个类作为测试套件的入口类，这个类必须是一个**空类**

- 在这个类上方使用**@RunWith**注解，更改测试运行器为**Suite.class**，即**@RunWith(Suite.class)**

- 将要测试的类的类对象作为数组传入到**Suite.SuiteClasses ( {} ) 。

  在这个类上方使用**@Suite.SuiteClasses**注解，即**Suite.SuiteClasses( {类名1.class, 类名2.class, 类名3.class .....} )**

## 参数化设置

### 用途

- 当我们需要对一个测试类测试多组数据时，每次测试都要修改数据十分麻烦，因此对测试的参数进行设置

### 使用方法

1. 更改该**测试类**的测试运行器为**@RunWith(Parameterized.class)**
2. 在该测试类中声明变量来存放**预期值**和**结果值**
3. 在该测试类中声明一个**返回值**为Collection的**公共静态方法**，并使用**@Parameters**进行修饰
4. 为测试类声明一个**带有参数的公共构造函数**，并在其中**为其声明变量赋值**

- 示例代码如下：

  ```java
  //此处例子为测试add方法，即测试用例为1+2=3,2+2=4
  @RunWith(Parameterized.class)
  public class ParameterTeat {
      private int expected;
      private int input1;
      private int input2;
  
      @Parameterized.Parameters
      public static Collection<Object[]> t() {
          return Arrays.asList(new Object[][] {
                  {3,1,2},
                  {4,2,2}
          });
      }
  
      public ParameterTeat(int expected, int input1, int input2) {
          this.expected = expected;
          this.input1 = input1;
          this.input2 = input2;
      }
  
      @Test
      public void testAdd() {
          Assert.assertEquals(expected, new Calculator1().add(input1, input2));
      }
  }
  
  //Calculator的add方法
  public int add(int a, int b){
          return a+b;
      }
  ```

  