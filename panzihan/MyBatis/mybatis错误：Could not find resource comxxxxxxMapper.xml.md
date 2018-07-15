# mybatis错误——java.io.IOException: Could not find resource com/xxx/xxxMapper.xml

#### 1.在学习Mybatis的时候，参考网上的教程进行简单demo的搭建，配置的没有问题，然后出现了下面的错误！ 

![](https://github.com/Rzihan/JavaStudy/blob/master/image/MyBatis/bug1.png?raw=true)

#### 2、最终通过上网查找找到了三种解决方案.

在说解决方案之前，先申明我的环境！我会用的开发工具是IDEA ，项目构建使用Maven！网上一些教程使用的Eclipse开发工具，项目是普通的java web项目，所以开发工具和构建项目不同就会存在一些出入（坑）！ 

**我项目的目录和xxxMapper.xml的位置如下图** 

![](https://github.com/Rzihan/JavaStudy/blob/master/image/MyBatis/bug2.png?raw=true)



原因：IDEA是不会编译src的java目录的xml文件，所以在Mybatis的配置文件中找不到xml文件！（也有可能是Maven构建项目的问题，网上教程很多项目是普通的Java web项目，所以可以放到src下面也能读取到） 

#### **解决方案1：** 

```
不将xml放到src目录下面，将xxxMapper.xml放到Maven构建的resource目录下面！
```

#### **解决方案2：** 

```xml
<build>
        <resources>
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.xml</include>
                </includes>
            </resource>
        </resources>
    </build>
```

#### **解决方案3** :

```
我测试时候只有 mapper resource 这种方式加载不到资源，其他的url class和package都可以，如果想解决问题的话，可以不使用resource这种方式！
```





