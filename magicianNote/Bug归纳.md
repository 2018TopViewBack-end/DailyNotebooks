# Bug归纳

## Mapped Statements collection does not contain value for解决

* 1.mybatis的映射文件的命令空间与接口的全限定名不一致;
* 2有可能mybatis的映射文件名字与接口的类名字不一致;
* 3.还有一种情况就是接口声明的方法在映射文件里面没有；
* 4.mapper包中的mapper.xml没有编译到targger中， maven的配置文件可能有问题，即没有配置build的resources 。
* 5.mapper.xml中有重复的id

##  javaweb中路径问题

* 1.对于 idea 来说，servlet 的访问路径比较简单，**直接在 localhost:8080 后面加上我们自己配置的值即可** ,无需加上工程名

## Sql语句查询

* 1.成员基本属性(String也是)变量名不要和表明一样

## org.apache.ibatis.executor.ExecutorException

* Cause: org.apache.ibatis.executor.ExecutorException: No constructor found in www.jisheng.po.Customer matching [java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String]The error may exist in CustomerDAO.xml

* 解决方案：创建无参构造器

## org.apache.ibatis.binding.BindingException:

* org.apache.ibatis.binding.BindingException: Type interface www.jisheng.dao.FoodDAO is not known to the MapperRegistry.
* 解决方案：在MyBatis配置文件中添加该Mapper的路径

## Annotation processing is not supported for module cycles 

* Error:java: Annotation processing is not supported for module cycles.Please ensure that all modules from cycle [OrderSystem-util,OrderSystem-service,OrderSystem-dao] are excluded from annotation processing
* 依赖循环

## java.lang.ClassNotFoundException: org.apache.jsp.WEB_002dINF.views.login_jsp

* 可能是out文件夹中无加载jar包

## Exception starting filter OpenSessionInViewFilter的解决方法

* 解决方案1：重新设置路径：点击该模块的Open Module settings ,然后对web重新设置路径
* 解决方案2：重新设置tomcat：Edit Configurations，新建tomcat并进行部署