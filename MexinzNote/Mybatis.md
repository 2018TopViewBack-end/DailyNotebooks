# Mybatis

## 简介

- Mybatis是支持**普通SQL查询，存储过程和高级映射**的优秀持久层框架。其消除了几乎所有的JDBC代码和参数的手工设置以及对结果集的检索封装。Mybatis可以使用**简单的XML或注解**用于配置和原始映射（放SQL语句），将接口和Java的POJO映射成数据库中的记录。
- JDBC-->dbutils(自动封装成对象，不需要操作Statement和ResultSet)-->Mybatis-->Hibernate(自动生成SQL语句)

## conf配置文件

- 在下载mybatis的文件夹中有官方说明的pdf文档，里面可以找到conf.xml配置文件的样例

- 在comf.xml配置文件中，**<environments>**标签中的default属性有两种，位于**<environments>**标签下面的**<environment>**标签中的id属性必须和**<environments>**标签中的default属性**相同**

  1. **development**：开发模式
  2. **work**：工作模式（项目发布后要改成这个）

- **每个实体类的映射文件/接口类注解**需要在conf.xml文件中注册，即相当于conf.xml管理所有的映射文件，格式如下：

  ```xml
  <mappers>
      <!--xml映射文件-->
  	<mapper resource="该实体类映射文件的完整包路径，用/代替."/>
      
      <!--接口类注解-->
      <mapper class="该接口类的完整包路径，用.分开"/>
  </mappers>
  ```

## 操作数据表的增删改查的两种方式

### SQL映射文件POJO类+Mapper.xml（实体类的配置文件）

- **<mapper>**标签中的namespace属性，相当于是这个SQL映射文件的Id值，唯一表示着这个XML文件，因此每个SQL映射文件的namespace值都应该是唯一的，不可重复。习惯命名为**完整包名+该xml文件的文件名，去掉xml后缀**，从而确保每个文件的namespace属性唯一。

- 该xml文件中对应增删改查的4个标签

  ```xml
  <!--增-->
  <insert id="xxx" parameterType="该对象类的包路径">
  <!--删-->
  <delete id="xxx" parameterType="int"(此处是通过id来删)>
  <!--改-->
  <update id="xxx"  parameterType="int"(此处是通过id来改) resultType="该对象类的完整包路径">
  <!--查-->
  <select id="xxx" resultType="该对象类的完整包路径">
  ```

  - 其中的id为操作时statement用来从该xml文件中确定具体的操作语句，**statement格式：该xml文件的namespace + .id**

  - parameterType为执行该sql语句时的条件参数类型，如通过id查询某用户，则paremeterType为int

    **注意：当参数类型为某数据表的实体类对象时，如update和add语句，则参数格式必须为 数据表字段=#{该对象的相应属性名}**

  - resultType为执行该sql语句返回的结果类型，如查询User表中的某一个User，则resultType为该User类的完整包路径

### 注解

- 创建一个接口类，习惯命名为POJO类+Mapper，规定相应的返回类型，注解类型为以下4种，**括号中sql语句和Mapper.xml文件中的规则相同**

  1. @Insert("SQL语句")
  2. @Delete("SQL语句")
  3. @Update("SQL语句")
  4. @Select("SQL语句")

  

## SqlSession

- **session**是执行SQL语句的对象
- **session**执行完后默认为手动提交（即调用SqlSessionFactory实例对象时用不带参数的openSession() 方法），需要多一行代码session.commit()
- 设置成自动提交则调用重载的方法：获取Session对象时调用SqlSessionFactory实例对象的openSession(true) 方法

## 优化

### properties文件存放连接数据库的配置

- 文件名：**db.properties**，放在src目录下，和conf.xml文件同级目录，格式如下：

  ```properties
  # 连接数据库的四个信息
  driver=com.mysql.jdbc.Driver
  url=jdbc:mysql://localhost:3306/...
  name=xxx
  password=xxxx
  ```

- 在conf.xml文件中导入db.properties文件：

  ```xml
  <configuration>
  	<properties resources="db.properties"/>
      ...
      <property name="driver" value="${driver}"/>
      <property name="url" value="${url}"/>
      <property name="username" value="${username}"/>
      <property name="password" value="${password}"/>
  </configuration>
  ```

### 配置实体类的别名

- 原因：sql映射文件中的引用，如parameterType等属性需要输入**完整包名**，太过复杂，于是为该POJO类在conf.xml中定义一个别名，简化引用，格式有两种：

  1. ```xml
     <typeAliases>
     	<typeAlias type="该实体类的完整包路径" alias="想定义的别名">
     </typealias>
     ```

  2. ```xml
     <!--通过这种方式定义的别名直接为该实体类的名称，推荐使用这种定义别名的方式，因为在做项目时实体类都是定义在同一个包中，因此只用写一条配置代码即可-->
     <typeAliases>
     	<package name="该实体类的完整包路径，去掉该实体类的名称"/>
     </typeAliases>
     ```



### log4j

- 其实打印语句写不写配置都会执行，只是写了才会显示
- 步骤：
  1. 添加log4j的jar包
  2. 在src目录下添加log4j.xml配置文件
- log4j 日志输出的7个级别：
  1. ALL level: 是最低等级的,用于打开所有日志记录. 
  2. DEBUG Level: 指出细粒度信息事件对调试应用程序是非常有帮助的,就是输出debug的信息. 
  3. INFO level: 表明消息在粗粒度级别上突出强调应用程序的运行过程,就是输出提示信息. 
  4. WARN level: 表明会出现潜在错误的情形,就是显示警告信息. 
  5. ERROR level: 指出虽然发生错误事件,但仍然不影响系统的继续运行.就是显示错误信息. 
  6. FATAL level: 指出每个严重的错误事件将会导致应用程序的退出. 
  7. OFF level: 是最高等级的,用于关闭所有日志记录. 

## 数据表字段和实体类属性不一致冲突

- 处理方法有两种

  1. 在配置sql语句时，将数据表中字段名命一个别名，别名的名称必须与实体类中的属性名相同

  2. 为查询的结果（对象）设置一个resultMap

     ```xml
     <select id="getOrder" parameterType="double" resultMap="下面的id">
             sql语句
     </select>
     <resultMap id="get实体类名Map" type="实体类名">
         <!--封装了一些映射关系，解决了数据表中字段名和实体类的属性名不一致的问题-->
         <!--id是专门针对主键的-->
         <id property="属性名" column="字段名"/>
         <!--result是针对除了主键以外的其他字段的 -->
         <result property="属性名" column="字段名"/>
         <result property="属性名" column="字段名"/>
     </resultMap>
     ```

     ​	

## 多表查询

### 一对一

#### 	联表查询（一次，通过外键）	

- 在大的那个实体类的映射文件添加如下代码，此处以班级包括老师为例

  ```xml
  <select id="get+类名" parameterType="" resultMap="下面resultMap的id">
  	SQL语句
  </select>
  <resultMap id="get类名Map" type="类名（设了别名）完整包名+类名">
      <id property="主键对应的实体类属性名" column="主键名"/>
      <result property="类属性名" column="字段名"/>
      ···（更多属性）
      <!--这里是大对象中的小对象，如班级对象中包含班主任对象-->
      <association property="属性名" javaType="类名（设了别名）/完整包名+类名">
      	<id property="主键对应的实体类属性名" column="主键名"/>
      	<result property="类属性名" column="字段名"/>
      ···（更多属性）
      </association>
  </resultMap>
  ```



#### 	查询两次

- 在大的那个实体类的映射文件添加如下代码，此处以**班级包括老师**为例

  ```xml
  <!--方式二：查两次-->
  <select id="getClass2" parameterType="int" resultMap="getClass2Map">
      sql语句
  </select>
  
  <select id="getTeacher" parameterType="int" resultType="Teacher">
      sql语句
  </select>
  
  <resultMap id="getClass2Map" type="Classes">
      <id property="id" column="c_id"/>
      <result property="name" column="c_name"/>
      <!--下面的select和上面的teacher的查询语句id值相同，column为第一次查询class时查到的teacher_id字段，作为teacher表的查询条件-->
      <association property="teacher" select="getTeacher" column="teacher_id">
          <id property="id" column="t_id"/>
          <result property="name" column="t_name"/>
      </association>
  </resultMap>
  ```


### 一对多

	#### 	联表查询

- 当查询一个班级和其中的学生时，班级和学生是一对多的关系。以下为示例代码

  ```xml
  <select id="getClass" parameterType="int" resultMap="getClassMap">
          select * from class c, student s where c.c_id=s.class_id and c.c_id=#{id}
  </select>
      
  <resultMap id="getClassMap" type="Classes">
      <id property="id" column="c_id"/>
      <result property="name" column="c_name"/>
      <!--下面为学生list，property为class类中对应的属性，ofType代表该集合中对象的类型，此处对象的类型为Student-->
      <collection property="studentList" ofType="Student">
          <id property="id" column="s_id"/>
          <result property="name" column="s_name"/>
      </collection>
  </resultMap>
  ```



#### 查询两次

- 此处是班级，包含一个老师和一个学生List，以下为示例代码

  ```xml
  <select id="getClass2" parameterType="int" resultMap="getClass2Map">
      select * from class where c_id=#{id}
  </select>
  <select id="getTeacher" resultType="Teacher">
      select t_id id, t_name name from teacher where t_id=#{id}
  </select>
  <select id="getStudent" resultType="Student">
      select s_id id, s_name name from student where class_id=#{id}
  </select>
  
  <resultMap id="getClass2Map" type="Classes">
      <id property="id" column="c_id"/>
      <result property="name" column="c_name"/>
      <association property="teacher" column="teacher_id" select="getTeacher"/>
      <collection property="studentList" column="c_id" select="getStudent"/>
  </resultMap>
  ```



## 模糊查询

- 示例：通过名字的模糊查询和年龄范围查找用户，以下是xml映射文件代码：

  ```xml
  <!--此处为查找条件创建一个实体类对象，此处conditionUser的三个实体类属性为模糊姓名，最大年龄和最小年龄-->
  <select id="xxx" resultType="User" parameterType="ConditionUser">
          select * from t_user where name like #{name} and age between #{minAge} and #{maxAge}
  </select>
  ```

- 以下是java测试类中代码：

  ```java
  //此处为测试类代码，查找名字中含有o字母和年龄位于13-18之间的用户
  String name="o";
  ConditionUser parameter = new ConditionUser("%"+name+"%", 13, 18);
  //statement为xml映射文件的namespace +相应select的id
  List<User> userList = session.selectList(statement,parameter);
  ```



## 动态sql

- **注意**：上面的代码没有检测传入的name属性是否为空，需要在xml映射文件中添加如下代码，通过if标签进行检测：

  ```xml
  <select id="xxxx" resultType="User" parameterType="ConditionUser">
      select * from t_user where
      <!--这里记得要判断-->
      <if test="name != '%null%'">
          name like #{name} and
      </if>
      age between #{minAge} and #{maxAge}
  </select> 
  ```

## mybatis缓存

- **一级缓存**：默认开启。基于PerpetualCache和HashMap的本地缓存，其存储作用域为Session。清除一级缓存有三种方式

  1. 执行session.clearCache()
  2. session进行了CUD操作(刷新缓存)
  3. 调用session.close()，原session已经被关闭，新的session和原来的不是同一个session对象，缓存区域自然不同

- **二级缓存**：默认关闭，通过在xml映射文件中添加**<cache>来开启。**缓存机制与一级缓存相同。不同在于其存储作用域为**Mapper(Namespace)**，即整个映射文件中，并且可**自定义存储源**，如Ehcache

- **缓存数据更新机制**：当某一个作用域（一级缓存Session / 二级缓存Namespaces）的进行了

  C(增)/U(改)/D(删)操作后，**默认该作用域下的所有select中的缓存将被clear**，原因是数据库中的数据很大可能已经被改变，该缓存已经无法被复用