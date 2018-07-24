# Maven

[TOC]



---

### 参考文献

[Maven之依赖配置-关于依赖传递、依赖范围和类加载器](https://my.oschina.net/mzdbxqh/blog/847313#h2_8)



---

### n.随记

* jar包和war包所存在的原因是，为了项目的部署(Deployment)和发布，通常把项目打包，通常在打包部署的时候，会在里面加上部署的相关信息。这个打包实际上就是**把代码和依赖的东西压缩在一起**，变成后缀名为.jar和.war的文件，就是我们说的jar包和war包。但是这个“压缩包”可以被编译器直接使用，把war包放在tomcat目录的webapp下，tomcat服务器在启动的时候可以直接使用这个war包。通常tomcat的做法是解压，编译里面的代码，所以当文件很多的时候，tomcat的启动会很慢。 

* jar包是java打的包，war包可以理解为javaweb打的包，这样会比较好记。jar包中只是用java来写的项目打包来的，里面只有编译后的class和一些部署文件。而war包里面的东西就全了，包括写的代码编译成的class文件，依赖的包，配置文件，所有的网站页面，包括html，jsp等等。一个war包可以理解为是一个web项目，里面是项目的所有东西。 

* 什么时候使用jar包或war包？当你的项目在没有完全完成的时候，不适合使用war文件，因为你的类会由于调试之类的经常改，这样来回删除、创建war文件很不方便，来回修改，来回打包，最好是你的项目已经完成了，不做修改的时候，那就打个war包吧，这个时候一个war文件就相当于一个web应用程序；而jar文件就是把类和一些相关的资源封装到一个包中，便于程序中引用。 

* **`<dependencyManagement>`**为依赖管理

* **`<execution>`**将插件绑定在某个阶段

* 继承的绝对路径是pom的文件

* 聚合是模块(module)的位置

* ```xml
  <!--由于Maven中jdk默认版本为1.5,所以一般需要在pom.xml中加入该插件来将jdk版本改为1.8-->
  <build>
      <plugins>
          <plugin>
              <groupId>org.apache.maven.plugins</groupId>
              <artifactId>maven-compiler-plugin</artifactId>
              <configuration>
                  <source>1.8</source>
                  <target>1.8</target>
              </configuration>
          </plugin>
      </plugins>
  </build>
  ```

* 

* site![1531298117965](https://github.com/630231047/notebook/blob/master/1531298117965.png?raw=true)

* 插件的安装方法

* ![1531298309002](https://github.com/630231047/notebook/blob/master/1531298309002.png?raw=true)

---

### 依赖的范围

* ### 依赖的使用场景

  * 开发时
  * 编译时
  * 运行时
  * 测试编译时
  * 测试运行时

* 对应这些使用场景，maven设计了scope（依赖范围），把依赖的范围分为compile、provided、runtime、test、system和import六种。注意，只要配置了第三方依赖，无论是哪一种scope，IDEA都会把它列入External Libaraies进行索引，以用作在我们开发时弹出相应的代码补全提示等用途。 

* ### compile

  * 当一个第三方依赖在上述五种场景都要用到时，即选用compile这种范围。这也是scope的默认值。在打包war时，该类依赖会被一起打包。

* ### provided

  * IDEA、Javac、maven-compile-plugin的本质都是Java程序，其运行需依赖于JVM实例，自然也就加载了上文所说的那些基础类包和扩展类包。在自己的程序中引入、使用这些类包时是不需要配置依赖的。但是，对于只在运行时才由容器提供的那些类包，例如tomcat的servlet API，是这几个IDE/工具所不了解的。
  * 因此，如果你要使用如servlet API进行开发，就必须配置相应的依赖，并把scope配置为provided。意思是，我开发时请帮我索引以实现代码补全提示，编译时请加载这些包以校验我的代码正确性，但是，**不要打包**，因为容器会提供这些包。
  * 更具体地说，如果我依赖了servlet API并且没有配置scope，那么我的war包里边就会含有servlet-api.jar。按照tomcat的类加载顺序，启动时已经由Common ClassLoader加载了一个servlet-api，运行时如需加载servlet-api，请求会发往Webapp ClassLoaderwar，而该加载器会先到我的webapp里找到并发现我包里的servlet-api，结果就是一个jvm里面出现了两个相同的包以及包里相同或版本有差异的类，导致报错（官方文档提到webapp ClassLoader会刻意忽略掉webapp里面的servlet-api包，但实测发现仍会报错）。
  * * 个人理解：
    * ​    IDEA、Javac、maven-compile-plugin自身不需要配置依赖，因此外面的依赖与自身不会产生冲突，但，Tomcat容器自带了依赖，容易与外面的依赖产生冲突，因此用provided来避免产生冲突

* ###  runtime

  * 为了解耦，我们经常是面向接口编程而不是面向具体实现类编程，最终打包的时候，就分成了接口和实现两种包。在开发和编译时，我们调用一个方法，IDEA和Javac并不要求知道具体的实现类，只要我们引用的依赖能告诉它们接口就行。这样一来，只要把scope配置为runtime，就可以不参与编译，**但是会被打包，因为测试时和运行时都要加载该依赖**。
  * 举个栗子来说明这种scope的应用场景：
    * 开发一个ORM框架，连接数据库部分全部面向JDBC接口编程，然后通过Spring来注入具体的实现类。这时候，项目的pom.xml就可以同时配置Mysql、Oracle、Sqlserver的实现类依赖，scope设置为runtime，并配置为optional（可选依赖）。然后运行的时候，只需要修改Spring的配置文件，改变注入的实现，就可以分别连接不同类型的数据库。
  * 其他项目引用该依赖时，因为orm/pom.xml里JDBC实现类都配置成了optional，故项目不会传递这些实现类的依赖，此时要根据具体环境手动配置一个实现类。例如我们给Mybatis配置的Mysql连接包。

* ###  test

  * test依赖就简单了，就是Junit一类的测试框架准备的，用在test类开发、test类编译（测试编译）和test类运行（测试运行）三种场景。
  * 依赖范围会影响依赖的传递，例如范围为test的依赖并不会被传递。详见maven官方文档。

* 简洁版

  * **compile**：**默认的依赖范围**，**编译测试运行都有效**

  * **provided**：在**编译**和**测试**时有效

    eg：servlet-api，因为它在tomcat等web服务器已经存在了，如果再打包会冲突

  * **runtime**：在**测试**和**运行**时有效

    eg：mysql-connector，jdbc驱动

  * **test**：只在**测试**时有效

    eg：JUnit

  * system：有效范围和provided相同 但与本机系统相关联，可移植性差

  * import：导入的范围。它只使用在dependencyManagement中，表示从其他的pom中导入dependency的配置

----

### 依赖的传递性

* 在maven里，依赖是会传染的。传染有两种方式，一种是Sub Module对Parent Module的**继承依赖**，另一种就是**依赖传递**(其规则是：A依赖B，B依赖C，那么A依赖C)。 

* 当依赖级别相同的时候，相同的jar，我们是哪个先被依赖就依赖那个模块的jar，前提是，此jar我们只依赖它“依赖层数”最短的jar。 这就是为什么会使用common-logging的1.1.1版本。  

------

### 配路径

* 本地仓库
* ![1531298018202](https://github.com/630231047/notebook/blob/master/1531298018202.png?raw=true)
* 镜像仓库
* ![1531297947281](https://github.com/630231047/notebook/blob/master/1531297947281.png?raw=true)

---

### 依赖冲突

* 既然有传递依赖，就不可避免的会出现下图这种情况： 

* ![img](https://static.oschina.net/uploads/space/2017/0311/000933_f4iR_1032568.png) 

* 如图，因为传递依赖机制，项目study-A既依赖于项目study-D:1.0版本，又依赖于项目study-D:2.0版本，造成依赖冲突。解决这个问题有两个办法，一个是**通过手工排除其中一个依赖**；另一个就是**由maven自行通过算法去选择**。

  * 算法有两种：

    - 选择传递依赖 - 同样是传递依赖，maven优先选传递路径短的那个。
      如上图中的D:2.0，它离A只差一个节点；而D:1.0离A差两个节点。
    - 选择直接依赖 - maven优先选配置在前面的那个（老版本没有这个特性）。
      例如同一个pom里面约定了两个study-F，先约定的是2.0，后约定的是1.0，maven选2.0那个。

  *  依赖排除可以用**`<exclusion>`**

     * ```xml
       <exclusions>
            <!-- Spring官方建议不要用这个 -->
            <exclusion>
            	<groupId>commons-logging</groupId>
            	<artifactId>commons-logging</artifactId>
            </exclusion>
       </exclusions>
       ```

     * 

---

### 目录骨架

- src

  - main
    - java
      - package
  - test
    - java
      - package
  - resources

* pom.xml

- target(调用mvn:compile后出现)，里面是编译好的.class等等
- ps：resources文件夹中放资源文件

-----------------

### 常用命令

* mvn -v 查看maven版本

* compile 编译
* test 测试
* package 打包
* clean 删除target
* install 安装jar包到本地仓库中

### 创建目录的两种方式

* 1. archetype:generate 按照提示进行选择
  2. archetype:generate -DgroupId=组织名 ，公司网址的反写+项目名  

  -DartifactId=项目名-模块名  -Dversion =版本号  -Dpackage =代码所在的包名

---

### 聚合和继承

   * **继承和聚合可以在同一个pom.xml文件中完成**，该文件位于所有模块的根目录下
   * 继承的路径最后是一个pom.xml文件，聚合是模块的位置
* 聚合

	- 当项目模块化时，要运行项目不可能将每个模块都编译一次（模块一多效率很低），因此在模块的根目录下**添加一个pom.xml文件**，通过这个文件来集中编译各个模块中的项目。**这个pom.xml文件不用写任何内容，它的用途就是聚合操作**

	- 在这个pom.xml文件中加入

  ```xml
  <!--用于聚合运行多个模块的项目，可以一次编译-->
  <modules>
      <module>模块相对于该pom.xml文件的路径</module>
      <module>同上</module>
      .....（个数为需要聚合的模块数量）
  </modules>    
  ```

	- 要针对**整个**项目作**编译测试运行**等操作时，就可以直接对pom.xml文件做操作即可，操作顺序按照pom.xml中**<module>**的顺序

* 继承

	- 当**多个模块中的pom.xml文件中的配置有大量重复**时，让所有的项目模块继承于一个根类（一个空项目），在根类里配置公有的东西

	- 用法：在子模块项目中添加如下代码：

  ```xml
  <!--parent标签通常用于子模块中，表现对父模块pom的继承-->
  <parent>
  	<groupId>xxxx</groupId>
      <artifactId>根模块的名称</artifactId>
      <version>xxxx</version>
      <relativePath>根模块的名称/pom.xml
      </relativePath>
  </parent>
  ```

	- 依赖也可以继承，**这样在子模块中就不用再写版本信息和作用域等公有的东西了（不用<version>等**），只要写**groupId**和**artifactId**：在父模块的pom.xml文件中添加如下代码：

  ```xml
  <!--依赖的管理，这个标签里的不会被运行，也就是不会引入实际的依赖，该标签主要用于定义在父模块中，供子模块继承使用-->
  <dependencyManagement>
  	<dependencies>
          <dependency>
          	<groupId>xxxx</groupId>
          	<artifactId>xxxx</artifactId>
              <version>xxxx</version>
              <scope>xxxx</scope>
          </dependency>
          ....
      </dependencies>
  </dependencyManagement>
  ```

### pom.xml

- groupId--项目全称 如cn.medwin.test

- artifactId--模块名称

- version--版本 1.0.0-SNAPSHOT (快照版本，alpha 项目组内部测试版本，beta 公测版，Release(RC)释放版本，GA 最终版本)

  xxx0.0.1-SNAPSHOT-->xxx0.0.1-Release-->xxx1.0.1-SNAPSHOT		

  ​				-->xxx0.1.1-SS分支-->xxx0.1.1-Release-->两个分支合并为xxx1.0.1-Release

- propertise参数

- 其他maven内置属性

### 范围为compile 和provided的区别

* 对于scope=compile的情况（默认scope),也就是说这个项目在编译，测试，运行阶段都需要这个artifact对应的jar包在classpath中。

  而对于scope=provided的情况，则可以认为这个provided是目标容器已经provide这个artifact。换句话说，它只影响到编译，测试阶段。在编译测试阶段，我们需要这个artifact对应的jar包在classpath中，而在运行阶段，假定目标的容器（比如我们这里的liferay容器）已经提供了这个jar包，所以无需我们这个artifact对应的jar包了。

* 做一个实验就可以很容易发现，当我们用maven install生成最终的构件包ProjectABC.war后，在其下的WEB-INF/lib中，会包含我们被标注为scope=compile的构件的jar包，而不会包含我们被标注为scope=provided的构件的jar包。这也避免了此类构件当部署到目标容器后产生包依赖冲突。

> 综上：可知compile在编译，测试和运行阶段都存在，而provided只影响编译，测试阶段

### packaging 的含义

* “打包“这个词听起来比较土，比较正式的说法应该是**"构建项目软件包"**，具体说就是将项目中的各种文件，比如源代码、编译生成的字节码、配置文件、文档，按照规范的格式生成归档，最常见的当然就是JAR包和WAR包了。 
* 任何一个Maven项目都需要定义POM元素packaging（如果不写则默认值为jar）。顾名思义，该元素决定了项目的打包方式。实际的情形中，如果你不声明该元素，Maven会帮你生成一个JAR包；如果你定义该元素的值为war，那你会得到一个WAR包；如果定义其值为POM（比如是一个父模块），那什么包都不会生成。除此之外，Maven默认还支持一些其他的流行打包格式，例如ejb3和ear。你不需要了解具体的打包细节，你所需要做的就是告诉Maven，”我是个什么类型的项目“，这就是约定优于配置的力量。 

### pom属性(以下参考[慕课网视频](https://www.imooc.com/video/8645/0))

* ![1531298639475](https://github.com/630231047/notebook/blob/master/1531298639475.png?raw=true)

![1531298681189](https://github.com/630231047/notebook/blob/master/1531298681189.png?raw=true)

![1531298861706](https://github.com/630231047/notebook/blob/master/1531298861706.png?raw=true)

![1531298950992](E:\Program Files\notebook\1531298950992.png)

![1531299003791](https://github.com/630231047/notebook/blob/master/1531299003791.png?raw=true)

```xml
<!--将插件绑定在某个阶段-->
<executions>
	<execution> 
		 <id></id>
		 <phase></phase>
		 <goals><goal></goal></goals>
	</execution>
</executions>
```

---

### 依赖与插件的区别 

* 解释1
  * 依赖的话就相当于你用c写代码时候引用的库文件。你之所以引用这些库是因为你需要它里面的函数，需要通过这些函数构建自己的代码。所以最终你调用的库函数成为了你代码的一部分。
  *  插件呢，相当于你用word写文档时候最上面工具栏里面的工具，比如你可以通过‘插入图片’工具往word里面插入一张喜欢的图片。可是插件与你的文本本身不发生任何关系。
  *  回到maven，依赖中被你调用过的函数会与你的代码一起进行编译。对于插件来说呢，比如有些插件是帮助你进行编译工作的，你不用手动写javac一个个去编译。插件就相当于小程序(其实是脚本)。

  

* 解释2

  * 说的简单一点：插件是一种工具，例如compile插件是用来编译代码的工具，mybatis插件是用来自动生成数据库dao和mapper的工具。而依赖则是项目工程在编译过程中需要依赖的二方及三方包。在你的工程中可以不需要mybatis插件，自己去实现sql的crud，但如果工程里需要三方包，则必须要用dependency引入。

* 记住：

  * 插件里面也可以加依赖
---



### 插件的使用方法

* **`configuration`**可以用来配置参数
* ![1531308712068](https://github.com/630231047/notebook/blob/master/1531308712068.png?raw=true)



### 依赖冲突的解决方案

当出现了冲突的时候，比如系统出现了**`NoSuchMethodError`**，[LinkageError](http://www.daniel-journey.com/archives/1122) 很有可能是你系统中出现了依赖冲突。出现冲突以后，可以按以下的步骤执行：

* 1.确定出了问题的jar包名称。通常可以在eclipse中查找冲突的类有在哪些依赖包里面出现了。并确定实际要使用的是哪个包，冲突的包有哪些。
* 2.通过mvn dependency:tree  >  tree.txt 导出全部的依赖。
* 3.在导出的依赖文件中，查找问题相关的jar。确定这些jar是如何被依赖进来的，是直接依赖的还是通过传递依赖引入的。
* 4.找到相互冲突的并需要排除的依赖的顶级依赖,并分析冲突的原因，冲突的原因可能是以下几种：
  * 同一个jar包但groupId, artifactId不同，这种冲突只能通过设定依赖的**`<exclusions> `**来进行排除
  * 需要的版本jar包依赖路径较长，这种冲突可以把想要版本的依赖直接什么在依赖中，这样路径就最短了优先级最高。
* 5.最后可以通过打包mvn install 来确认打出来的war包中是否有被排除的依赖。

 





​    

​     

​     

​     

​     

​     

   

   