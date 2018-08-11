**一，mybatics的简介：**MyBatis是一个优秀的持久层框架，它对jdbc的操作数据库的过程进行封装，使开发者只需要关注 SQL 本身，而不需要花费精力去处理例如注册驱动、创建connection、创建statement、手动设置参数、结果集检索等jdbc繁杂的过程代码。
Mybatis通过xml或注解的方式将要执行的各种statement（statement、preparedStatemnt、CallableStatement）配置起来，并通过java对象和statement中的sql进行映射生成最终执行的sql语句，最后由mybatis框架执行sql并将结果映射成java对象并返回。

每个基于 MyBatis 的应用都是以一个 SqlSessionFactory 的实例为中心的。SqlSessionFactory 的实例可以通过  SqlSessionFactoryBuilder 获得。而 SqlSessionFactoryBuilder 则可以从 XML 配置文件或一个预先定制的  Configuration 的实例构建出 SqlSessionFactory 的实例。

从 XML 文件中构建 SqlSessionFactory  的实例非常简单，建议使用类路径下的资源文件进行配置。但是也可以使用任意的输入流(InputStream)实例，包括字符串形式的文件路径或者 file:// 的  URL 形式的文件路径来配置。MyBatis 包含一个名叫 Resources 的工具类，它包含一些实用方法，可使从 classpath  或其他位置加载资源文件更加容易。

如下：String resource = "org/mybatis/example/mybatis-config.xml";InputStream inputStream = Resources.getResourceAsStream(resource);SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

**注意点：**

- **尽管可以配置多个环境，每个 SqlSessionFactory 实例只能选择其一。**
- **每个数据库对应一个 SqlSessionFactory 实例** 
- **默认的环境和环境 ID 是一目了然的。随你怎么命名，只要保证默认环境要匹配其中一个环境ID。**
- useGeneratedKeys="true" ， keyProperty="对应的主键的对象"，**用了这个useGenerateKeys="true"后，不用再在写一句关于select的SQL（降低数据库的压力）而直接能狗得到刚刚插进去的那个主键，但是——这个属性只能用在主键能自增长的数据库里面比如MySQL可以用，但是oracle就不能用了！**
- **mybatics事务是手动提交的，session.commit(),session.close();**

**二，xml的配置：看文档！**看文档！

- ​	一定要注意文件的路径和类名的正确。     

**三，xml映射文件：**

​     1.select：

`**<select id="selectPerson" parameterType="int" resultType="hashmap">**` 

 SELECT * FROM PERSON WHERE ID = #{id}

</select>

​      2,resultType和resultMap的区别：前者时自动映射，后者是手动映射，两者只能选择其中一个。

​      3，\#{}和${}在预编译中的处理是不一样的。#{}在预处理时，会把参数部门用一个占位符？代替，而${}直接以字符串代替。#{}的参数替换发生在DBMS（ 数据库管理系统）中，而${}则发生在动态解析过程中。${}方式会引发sql注入的问题，同时也会影响sql语句的预编译，所以从安全性和性能的角度出发，能使用#{}的情况下就不要使用${}。

   4,手动映射（**注意与自动映射的区别**）：

   ![1531730688175](C:\Users\i\AppData\Local\Temp\1531730688175.png)

 	

![ 1531750437372](C:\Users\i\AppData\Local\Temp\1531750437372.png)

5，<mappers></mappers>的配置

![1531733624231](C:\Users\i\AppData\Local\Temp\1531733624231.png)

​         **6.@param的讲解**

![1531741573097](C:\Users\i\AppData\Local\Temp\1531741573097.png)

**四，注解开发（注意与映射的区别，此方法不用配置文件）**

![1531732128054](C:\Users\i\AppData\Local\Temp\1531732128054.png)

**四，动态代理：****     

​          Mapper接口开发方法只需要程序员编写Mapper接口（相当于Dao接口），由Mybatis框架根据接口定义创建接口的动态代理对象，代理对象的方法体同上边Dao接口实现类方法。

​          Mapper接口开发需要遵循以下规范：
1、Mapper.xml文件中的namespace与mapper接口的类路径相同。
2、Mapper接口方法名和Mapper.xml中定义的每个statement的id相同 
3、Mapper接口方法的输入参数类型和mapper.xml中定义的每个sql 的parameterType的类型相同
4、Mapper接口方法的输出参数类型和mapper.xml中定义的每个sql的resultType的类型相同

**五，动态sql语句：**

 	1，当查询所传递的参数有多个时，适当的方法是先建一个domain类将其封装在传。（当要增加参数时，直接到domain类中增加该参数就行，适合动态的sql语句查询）  

​	2，if-where-set动态语句：

![1531742989709](C:\Users\i\AppData\Local\Temp\1531742989709.png)

​     ![1531743063245](C:\Users\i\AppData\Local\Temp\1531743063245.png)

![1531743988099](C:\Users\i\AppData\Local\Temp\1531743988099.png)

-  **可变长度数组：Integer...arrs;**

​      3,foreach查询例子：

![1531743620486](C:\Users\i\AppData\Local\Temp\1531743620486.png)

​    4，sql与include（多功能相同的sql语句，先抽出去再引进来）

**六，联合查询：**

​    1，![1531744445574](C:\Users\i\AppData\Local\Temp\1531744445574.png)

​    ![1531744724343](C:\Users\i\AppData\Local\Temp\1531744724343.png)

​    2，查找一个员工的个人信息和部门信息（以个人为中心，查与此人相关的信息one2one）

- 把部门（对象）一起封装到个人信息中              

![1531745957155](C:\Users\i\AppData\Local\Temp\1531745957155.png)

- 先把个人信息查出来（含有部门id），再联合部门id到部门的映射文件中查找所属部门的信息，查到自动封装返回
- ![1531746475428](C:\Users\i\AppData\Local\Temp\1531746475428.png)

![1531746731945](C:\Users\i\AppData\Local\Temp\1531746731945.png)

​     ![1531749201446](C:\Users\i\AppData\Local\Temp\1531749201446.png)

3，以部门为中心查找该部门的所有人员（one2many）

- 将该部门的人工信息用集合表示，并加到部门的domain中

  ![1531748949114](C:\Users\i\AppData\Local\Temp\1531748949114.png)

- ​    ![1531749147568](C:\Users\i\AppData\Local\Temp\1531749147568.png)

​     **注意：**上面的两种方法中，员工和部门已经互相关联了（就是在员工domain中加入了部门对象，在部门domain中加入了员工），在ToString时 ，防止出现死锁现象，只能一方打印所有变量。

4，多对多

5，多对一

**七，Mybatis的Dao向mapper传多个参数（三种解决方案）**：

​         1，Public User selectUser(String name,String area); 对应的Mapper.xml    <select id="selectUser" resultMap="BaseResultMap"  parameterType="java.lang.String">

​    select  *  from user_user_t   where user_name = #{0} and user_area={1}

</select>其中，#{0}代表接收的是dao层中的第一个参数，#{1}代表dao层中第二参数，更多参数一致往后加即可 。

​	2，Dao层的函数方法 ：Public User selectUser(Map paramMap); 

- 对应的Mapper.xml ：

<select id=" selectUser" resultMap="BaseResultMap">

select  *  from user_user_t   where user_name = #{userName，jdbcType=VARCHAR} and user_area=#{userArea,jdbcType=VARCHAR}

</select>

- Service层调用 ：

Private User xxxSelectUser(){

Map paramMap=new hashMap();

paramMap.put(“userName”,”对应具体的参数值”);

paramMap.put(“userArea”,”对应具体的参数值”);

User user=xxx. selectUser(paramMap);}（此方法不够直观，见到接口方法不能直接的知道要传的参数是什么 ）

   3，Dao层的函数方法 ：Public User selectUser(@param(“userName”)Stringname,@param(“userArea”)String area); 

- 对应的Mapper.xml :

  <select id=" selectUser" resultMap="BaseResultMap">

     select  *  from user_user_t   where user_name = #{userName，jdbcType=VARCHAR} and user_area=#{userArea,jdbcType=VARCHAR}

  </select>(推荐用此种方案 )

