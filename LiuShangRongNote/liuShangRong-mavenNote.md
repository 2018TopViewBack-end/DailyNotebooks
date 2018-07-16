**一，maven常用命令介绍**



MVN clean清理（删除target目录下编译内容）；mvn clean编译项目mvn compile，执行mvn compile命令会在根目录生成target文件  ；打包发布mvn package；仅打包Web页面文件mvn war:exploded；打包时跳过测试mvn package -Dmaven.test.skip=ture；执行mvn install命令 ，maven通过install将本地工程打包成jar包，放入到本地仓库中，再通过pom.xml配置依赖引入到当前工程 ，以便在本地其他项目中使用该包  ；

**二，Junit测试：**

​        1.@Test: 测试方法

　　　　a)(expected=XXException.class)如果程序的异常和XXException.class一样，则测试通过　　　　b)(timeout=100)如果程序的执行能在100毫秒之内完成，则测试通过

　　2.@Ignore: 被忽略的测试方法：加上之后，暂时不运行此段代码

　　3.@Before: 每一个测试方法之前运行

　　4.@After: 每一个测试方法之后运行

　　5.@BeforeClass: 方法必须必须要是静态方法（static 声明），所有测开始之前运行，注意区分before，是所有测试方法

　　6.@AfterClass: 方法必须要是静态方法（static 声明），所有测试结束之后运行，注意区分 @After。

​        注意：编写测试类的原则：　

　　  ①测试方法上必须使用@Test进行修饰

​        ②测试方法必须使用public void 进行修饰，不能带任何的参数

​        ③新建一个源代码目录来存放我们的测试代码，即将测试代码和项目业务代码分开

​        ④测试类所在的包名应该和被测试类所在的包名保持一致

​        ⑤测试单元中的每个方法必须可以独立测试，测试方法间不能有任何的依赖

​        ⑥测试类使用Test作为类名的后缀（不是必须）

​        ⑦测试方法使用test作为方法名的前缀（不是必须）

**三，maven内置的隐藏变量：**

​      

- ${basedir} 项目根目录
- ${project.build.directory} 构建目录，缺省为target
- ${project.build.outputDirectory} 构建过程输出目录，缺省为target/classes
- ${project.build.finalName} 产出物名称，缺省为${project.artifactId}-${project.version}
- ${project.packaging} 打包类型，缺省为jar
- ${project.xxx} 当前pom文件的任意节点的内



**四，scope的分类**

1.compile：是默认值 他表示被依赖项目需要参与当前项目的编译，还有后续的测试，运行周期也参与其中，是一个比较强的依赖。打包的时候通常需要包含进去

2.test：依赖项目仅仅参与测试相关的工作，包括测试代码的编译和执行，不会被打包，例如：junit

3.runtime：表示被依赖项目无需参与项目的编译，不过后期的测试和运行周期需要其参与。与compile相比，跳过了编译而已。例如JDBC驱动，适用运行和测试阶段

4.provided：打包的时候可以不用包进去，别的设施会提供。事实上该依赖理论上可以参与编译，测试，运行等周期。相当于compile，但是打包阶段做了exclude操作

5.system：从参与度来说，和provided相同，不过被依赖项不会从maven仓库下载，而是从本地文件系统拿。需要添加systemPath的属性来定义路径；

五，依赖关系

​     1，间接依赖：A一>B,B一>C,则A一>C;

​     2,同等级依赖，那个先写就依赖那个；如a依赖b1又依赖于b2；

​     3，不同等级依赖：A一>B一>C1（两个等级），A一>C2（一个等级），则A一>C2;

​      **4,test依赖不会传递；**

​      5，排除依赖：exclusion 

**六， 继承和聚合**：

​	1.聚合的路径是模块的位置，继承的绝对路径是pom的文件的位置；

​	2，继承POM的用法：面向对象设计中，程序员可以通过一种类的父子结构，在父类中声明一些字段和方法供子类继承，这样可以做到“一处声明、多处使用”，类似的我们需要创建POM的父子结构，然后在父POM中声明一些配置，供子POM继承。（父类是所有子类依赖的集合（不重复的依赖））。

​	3,对于聚合模块来说，其打包方式必须为pom，否则无法构建; 

**七，maven的生命周期：**

​        

| validate                | 验证项目是正确的，所有必要的信息都是可用的。                 |
| ----------------------- | ------------------------------------------------------------ |
| initialize              | 初始化构建状态，例如设置属性或创建目录。                     |
| generate-sources        | 生成包含在编译中的任何源代码。                               |
| process-sources         | 处理源代码，例如过滤任何值。                                 |
| generate-resources      | 生成包含在包中的资源。                                       |
| process-resources       | 将资源复制并处理到目标目录中，准备打包。                     |
| compile                 | 编译项目的源代码。                                           |
| process-classes         | 从编译后生成生成的文件，例如在Java类上执行字节码增强。       |
| generate-test-sources   | 生成包含在编译中的任何测试源代码。                           |
| process-test-sources    | 处理测试源代码，例如过滤任何值。                             |
| generate-test-resources | 为测试创建资源。                                             |
| process-test-resources  | 将资源复制并处理到测试目标目录中。                           |
| test-compile            | 将测试源代码编译到测试目标目录                               |
| process-test-classes    | 从测试编译后post-process生成文件，例如在Java类上执行字节码增强。对于Maven 2.0.5和以上。 |
| test                    | 使用合适的单元测试框架运行测试。这些测试不应该要求打包或部署代码。 |
| prepare-package         | 在实际包装前执行必要的准备工作。这通常会导致包的一个未打包的、经过处理的版本。(Maven 2.1及以上) |
| package                 | 使用已编译的代码，并将其打包成可部署格式，例如JAR。          |
| pre-integration-test    | 执行集成测试之前需要执行的操作。这可能涉及到设置所需的环境等问题。 |
| integration-test        | 在需要集成测试的环境中，处理并部署包。                       |
| post-integration-test   | 执行集成测试后所需要的操作。这可能包括清理环境。             |
| verify                  | 运行任何检查以验证包是否有效，并满足质量标准。               |
| install                 | 将该包安装到本地存储库中，作为本地其他项目的依赖项。         |
| deploy                  |                                                              |

**八，插件：**

​      1，jar-no-fork与jar的区别 ，jar，在执行goal之前，执行generate-sources阶段，也就是说，如果，jar绑定的目标在generate-sources之后(比如verify)的话，generate-sources会执行两遍。 jar-no-fork，没有其余动作，在绑定的phase执行。 

​     2，插件的配置格式：

​		

<plugin>  
    <groupId>org.apache.maven.plugins</groupId>  
    <artifactId>maven-resources-plugin</artifactId>  
    <version>2.4.3</version>  
    <executions>  
        <execution>  
            <phase>compile</phase>(绑定生命周期)  

​        </execution>  
    </executions>  
    <configuration>  

​        （配置相关参数）
<encoding>${project.build.sourceEncoding}</encoding>           </configuration>  

</plugin>  

​     3，插件也可以有依赖。

**九，测试**：

​	1.只有如下格式才会进行测试：*/Test.java；**/*Test.java；**/*TestCase.java。

​	2，跳过测试：*<configuration> <skipTests>*true</skipTests

**</configuration>** 

​	3，包含与排除 ：Include，exclude，配置是标明对应的Java类。

​	4，动态制定要运行的测试：mvn test -Dtest = RadomGeneratorTest （参数）

​       5，测试覆盖率：$mvn cobertura:cobertura（不太懂）。