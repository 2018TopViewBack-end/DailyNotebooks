# Maven

[TOC]





---

### n.随记

* jar包和war包所存在的原因是，为了项目的部署和发布，通常把项目打包，通常在打包部署的时候，会在里面加上部署的相关信息。这个打包实际上就是**把代码和依赖的东西压缩在一起**，变成后缀名为.jar和.war的文件，就是我们说的jar包和war包。但是这个“压缩包”可以被编译器直接使用，把war包放在tomcat目录的webapp下，tomcat服务器在启动的时候可以直接使用这个war包。通常tomcat的做法是解压，编译里面的代码，所以当文件很多的时候，tomcat的启动会很慢。 
* jar包是java打的包，war包可以理解为javaweb打的包，这样会比较好记。jar包中只是用java来写的项目打包来的，里面只有编译后的class和一些部署文件。而war包里面的东西就全了，包括写的代码编译成的class文件，依赖的包，配置文件，所有的网站页面，包括html，jsp等等。一个war包可以理解为是一个web项目，里面是项目的所有东西。 
* 什么时候使用jar包或war包？当你的项目在没有完全完成的时候，不适合使用war文件，因为你的类会由于调试之类的经常改，这样来回删除、创建war文件很不方便，来回修改，来回打包，最好是你的项目已经完成了，不做修改的时候，那就打个war包吧，这个时候一个war文件就相当于一个web应用程序；而jar文件就是把类和一些相关的资源封装到一个包中，便于程序中引用。 
* **`<dependencyManagement>`**为依赖管理
* **`<execution>`**将插件绑定在某个阶段
* 继承的绝对路径是pom的文件
* 聚合是模块(module)的位置
* site![1531298117965](https://github.com/630231047/notebook/blob/master/1531298117965.png?raw=true)
* 插件的安装方法
* ![1531298309002](https://github.com/630231047/notebook/blob/master/1531298309002.png?raw=true)

---





### 1.依赖的范围

![1531208799535](C:\Users\63023\AppData\Local\Temp\1531208799535.png)



----

### 2.依赖的传递性

![1531210656573](C:\Users\63023\AppData\Local\Temp\1531210656573.png)

* 当依赖级别相同的时候，相同的jar，我们是哪个先被依赖就依赖那个模块的jar，前提是，此jar我们只依赖它“依赖层数”最短的jar。 这就是为什么会使用common-logging的1.1.1版本。  

------

### 3. 配路径

* 本地仓库

* ![1531298018202](https://github.com/630231047/notebook/blob/master/1531298018202.png?raw=true)
* 镜像仓库
* ![1531297947281](https://github.com/630231047/notebook/blob/master/1531297947281.png?raw=true)

---

### 4. 目录骨架

![1531271523736](https://github.com/630231047/notebook/blob/master/1531271523736.png?raw=true)

-----------------

### 5.常用命令

* mvn -v 查看maven版本

* compile 编译
* test 测试
* package 打包
* clean 删除target
* install 安装jar包到本地仓库中

### 6.创建目录的两种方式

* 1. archetype:generate 按照提示进行选择
  2. archetype:generate -DgroupId=组织名 ，公司网址的反写+项目名  

  -DartifactId=项目名-模块名  -Dversion =版本号  -Dpackage =代码所在的包名

  ---

### 7.范围为compile 和provided的区别

* 对于scope=compile的情况（默认scope),也就是说这个项目在编译，测试，运行阶段都需要这个artifact对应的jar包在classpath中。

  而对于scope=provided的情况，则可以认为这个provided是目标容器已经provide这个artifact。换句话说，它只影响到编译，测试阶段。在编译测试阶段，我们需要这个artifact对应的jar包在classpath中，而在运行阶段，假定目标的容器（比如我们这里的liferay容器）已经提供了这个jar包，所以无需我们这个artifact对应的jar包了。

* 做一个实验就可以很容易发现，当我们用maven install生成最终的构件包ProjectABC.war后，在其下的WEB-INF/lib中，会包含我们被标注为scope=compile的构件的jar包，而不会包含我们被标注为scope=provided的构件的jar包。这也避免了此类构件当部署到目标容器后产生包依赖冲突。

> 综上：可知compile在编译，测试和运行阶段都存在，而provided只影响编译，测试阶段

### 8.packaging 的含义

* “打包“这个词听起来比较土，比较正式的说法应该是**"构建项目软件包"**，具体说就是将项目中的各种文件，比如源代码、编译生成的字节码、配置文件、文档，按照规范的格式生成归档，最常见的当然就是JAR包和WAR包了。 
* 任何一个Maven项目都需要定义POM元素packaging（如果不写则默认值为jar）。顾名思义，该元素决定了项目的打包方式。实际的情形中，如果你不声明该元素，Maven会帮你生成一个JAR包；如果你定义该元素的值为war，那你会得到一个WAR包；如果定义其值为POM（比如是一个父模块），那什么包都不会生成。除此之外，Maven默认还支持一些其他的流行打包格式，例如ejb3和ear。你不需要了解具体的打包细节，你所需要做的就是告诉Maven，”我是个什么类型的项目“，这就是约定优于配置的力量。 

### 9.pom属性(以下参考慕课网视频https://www.imooc.com/video/8645/0)

* ![1531298639475](https://github.com/630231047/notebook/blob/master/1531298639475.png?raw=true)

![1531298681189](https://github.com/630231047/notebook/blob/master/1531298681189.png?raw=true)

![1531298861706](https://github.com/630231047/notebook/blob/master/1531298861706.png?raw=true)

![1531298950992](E:\Program Files\notebook\1531298950992.png)

![1531299003791](https://github.com/630231047/notebook/blob/master/1531299003791.png?raw=true)

```java
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

### 9.依赖与插件的区别 

* 解释1
  * 依赖的话就相当于你用c写代码时候引用的库文件。你之所以引用这些库是因为你需要它里面的函数，需要通过这些函数构建自己的代码。所以最终你调用的库函数成为了你代码的一部分。
  *  插件呢，相当于你用word写文档时候最上面工具栏里面的工具，比如你可以通过‘插入图片’工具往word里面插入一张喜欢的图片。可是插件与你的文本本身不发生任何关系。
  *  回到maven，依赖中被你调用过的函数会与你的代码一起进行编译。对于插件来说呢，比如有些插件是帮助你进行编译工作的，你不用手动写javac一个个去编译。插件就相当于小程序(其实是脚本)。

  

* 解释2

  * 说的简单一点：插件是一种工具，例如compile插件是用来编译代码的工具，mybatis插件是用来自动生成数据库dao和mapper的工具。而依赖则是项目工程在编译过程中需要依赖的二方及三方包。在你的工程中可以不需要mybatis插件，自己去实现sql的crud，但如果工程里需要三方包，则必须要用dependency引入。

* 记住：

  * 插件里面也可以加依赖
---



### 10.插件的使用方法

* **`configuration`**可以用来配置参数
* ![1531308712068](https://github.com/630231047/notebook/blob/master/1531308712068.png?raw=true)



### 11.依赖冲突的解决方案

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

   

   