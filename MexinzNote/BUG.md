# BUG记录

## 2018.7.13

- mybatis第4集视频中，提到**当数据表中字段和实体类中属性名不一致时**，会无法映射正确，导致操作失败。但我尝试后发现，即使不做任何处理，依旧可以正常运行（在有正确的重写构造方法的情况下），如果没重写构造方法（默认调用无参的），则一定要对该冲突进行处理
- 在运用mybatis框架写**联表查询**时，一直报找不到类构造器，原因：**类中没有写默认的构造方法**，很多框架都大量使用反射，如spring。**这些框架在调用反射的类后会默认调用默认的构造器**

## 2018.7.14

- （未解决）在mapper文件中通过**<cache/>**标签开启了二级缓存，且相应的POJO类也实现了Serializable接口，但二级缓存依旧无法使用

## 2018.7.17

- 当数据表的名字与POJO类中的属性名相同时，在mapper.xml文件中可能会导致错误

## 2018.7.19

- 由于mybatis的一级缓存，当在select语句中嵌套foreach循环时，相同的查询条件只会查询一次，导致查询到的结果集变少（参数的list中有重复的）

## 2018.7.29

- 自定义视图的时候，把试图类（实现了View接口），放在了view包，然后 dispatcher-servlet.xml 没有加多一个包扫描，导致默认跳转到 jsp 页面404

## 2018.8.1

- 在Spring + JUnit4进行测试时遇到：java.lang.ExceptionInInitializerError。原因是spring支持的JUnit测试版本最低要求是4.12，而我的是4.11

