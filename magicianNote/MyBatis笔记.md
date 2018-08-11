# MyBatis笔记

---

[TOC]

---

### 0.随记

* ```xml
  <configuration>
      <typeAliases>
        <!--
        通过package, 可以直接指定package的名字， mybatis会自动扫描你指定包下面的javabean,
        并且默认设置一个别名，默认的名字为： javabean 的首字母小写的非限定类名来作为它的别名。
        也可在javabean 加上注解@Alias 来自定义别名， 例如： @Alias(user) 
        <package name="com.dy.entity"/>
         -->
        <typeAlias alias="UserEntity" type="com.dy.entity.User"/>
    </typeAliases>
    ......
  </configuration>
  ```

* 在mybatis中，**映射文件中的namespace是用于绑定Dao接口的，即面向接口编程。** 

* MyBatis 运用了动态代理技术使接口能运行起来

* SqlSession接口有两个实现类:DefaultSqlSession和SqlSessionManager

* 一定要记得**`sqlSession.commit();//提交事务`**

* ```java
  //一定要记得:
  finally{
      //在finally语句中确保资源被顺利关闭
      if(sqlSession != null){
          sqlSession.close();
      }
  }
  ```

* 一定要记得:MyBatis中Mapper.xml里的sql语句的参数是用**"#{  }"**来表示的

* **`<resource>`**标签中要用"/"来分隔(但文件名和文件类型中间仍要用"."隔开)，而其他的用"."来分隔

* MyBatis 需要POJO的无参数的构造函数

* 如果想要通过sql语句获得List，需要在接口中定义返回值为List的方法

* ### sessionFactory.openSession(true)可以使sqlSession自动提交

* ### **`<mappers>`**节点下没办法同时使用**`<mapper>`**和`<package>`节点的问题，编译器一直报错，最后查询dtd约束才发现，配置文件要求先使用**`<mapper>`**节点，再使用**`<package>`**节点，顺序混乱就会报错 


### 生命周期

* SqlSessionFactoryBuilder存在目的只是创建SqlSessionFactory,因此无需长期存在
* SqlSessionFactory相当于一个对数据库的连接池,它占据着数据库的连接资源，因此它的生命周期等同于MyBatis的应用周期。由于其很耗资源，因此一般将其作为一个单例来应用
* SqlSession相当于一个数据库连接(Connection对象),它可用来执行多条SQL,然后通过commit,rollback等方法，提交或者回滚事务。所以它应该存活在一个业务请求中，处理完后，应该关闭这条连接，让它归还给SqlSessionFactory，应该要用try...catch...finally...语句来保证其正确关闭。
* Mapper是一个接口，由SqlSession所创建，它代表一个请求中的业务处理，，完成后，就应该被废弃。其生命周期最多和SqlSession保持一致

### 自定义别名

* ```xml
     <typeAliases>
        <!--
        通过package, 可以直接指定package的名字， mybatis会自动扫描你指定包下面的javabean,
        并且默认设置一个别名，默认的名字为： javabean 的首字母小写的非限定类名来作为它的别名。
        也可在javabean 加上注解@Alias 来自定义别名， 例如： @Alias(user) 
        <package name="com.dy.entity"/>
         -->
        <typeAlias alias="UserEntity" type="com.dy.entity.User"/>
     </typeAliases>
     ```
* 注意：

   MyBatis 会扫描这个包里的类，将其第一个字母变为小写作为其别名，比如类Role的别名会变为role。使用这样的规则，有时候会出现重名，那么就会出现异常，这个时候可以使用MyBatis提供的注解@Alias(" ")进行区分，并写在该类的上方


###  实现 getOrderById(id) 的查询：

* 方式一: 通过在 sql 语句中定义别名

   ```xml
   <select id="selectOrder" parameterType="int" resultType="_Order">
   select order_id id, order_no orderNo,order_price price from orders where order_id=#{id}
   </select>
   ```

* 方式二: 通过`<resultMap>`

  ```xml
   <select id="selectOrderResultMap" parameterType="int" resultMap="orderResultMap">
      select * from orders where order_id=#{id}
   </select>
   <resultMap type="_Order" id="orderResultMap">
  	<id property="id" column="order_id"/>
    	<result property="orderNo" column="order_no"/>
     	<result property="price" column="order_price"/>
  </resultMap>
  ```

### MyBatis缓存机制

*  映射语句文件中的所有 select 语句将会被缓存。
*  映射语句文件中的所有 insert，update 和 delete 语句会刷新缓存。
*  缓存会使用 Least Recently Used（LRU，最近最少使用的）算法来收回。
*  缓存会根据指定的时间间隔来刷新。
*  缓存会存储 1024 个对象
  <cache
  eviction="FIFO" //回收策略为先进先出
  flushInterval="60000" //自动刷新时间 60s
  size="512" //最多缓存 512 个引用对象
  readOnly="true"/> //只读

### 传入多个参数

* 使用注解

  * ```java
    //定义接口
    public List<Role> findRolesByAnnotation (@Param("roleName") String rolename,@Param("note") String note);
    ```

  * ```xml
    <select id = "findRolesByAnnotation" resultType="role">
    	select id, role_name roleName, note from t_role where role_name like concat('%',#{roleName},'%')
    </select>
    <!-- 注意，此时不需要给出parameterType属性，让MyBatis自动探索便可以了-->
    ```

  * 

* 通过Java Bean

  * ```java
    public class RoleParam{
        private String roleName;
        private String note;
        /**setter and getter**/
    }
    ```

  * ```xml
    <select id = "findRolesByBean" 
      parameterType="com.learn.ssm.chapter5.param.RoleParams"
        resultType="role">
    	select id, role_name roleName, note from t_role 
    	where role_name like concat('%',#{roleName},'%')
        and note like concat('%',${note},'%')
    </select>
    ```

### 插入语句中的主键回填

* 背景：由于MySQL中的表格无需自己填写主键id，而是自增生成，所以我们不知道该id的值。但，有时我们还需要继续使用这个主键

```xml
<insert id="insertRole" parameterType="role"
    useGeneratedKeys="true" keyProperty="id">
    insert into t_role(role_name,note) values(#{roleName},#{note})
</insert>
<!-- 其中，useGeneratedKeys代表采用JDBC的Statement对象的getGeneratedKeys方法返回主键，而keyProperty则代表将用哪个POJO的属性去匹配这个主键，这里是id，如果是复合主键，要把每个名称用(,)隔开 -->
```

* 因此，主键回填主要依赖**userGeneratedKeys** 和**keyProperty** 两个属性
### sql元素

* 作用：可以定义一条SQL语句的一部分，方便后面的SQL引用它

* ```xml
  <sql id="roleCols">
  	id,role_name,note
  </sql>
  <select id="getRole" parameterType="long" resultMap="roleMap">
  	select <include refid="roleCols"/> from t_role where id=${id}
  </select>
  ```

### 一对一关联

* ```xml
  <!--
  方式一： 嵌套结果：使用嵌套结果映射来处理重复的联合结果的子集
  封装联表查询的数据(去除重复的数据)
  select * from class c, teacher t where c.teacher_id=t.t_id and c.c_id=1
  -->
  <select id="getClass" parameterType="int" resultMap="ClassResultMap">
  select * from class c, teacher t where c.teacher_id=t.t_id and c.c_id=#{id}
  </select>
  <resultMap type="_Classes" id="ClassResultMap">
  <id property="id" column="c_id"/>
  <result property="name" column="c_name"/>
  <association property="teacher" javaType="_Teacher">
  <id property="id" column="t_id"/>
  <result property="name" column="t_name"/>
  </association>
  </resultMap>
  <!--
  方式二： 嵌套查询：通过执行另外一个 SQL 映射语句来返回预期的复杂类型
  SELECT * FROM class WHERE c_id=1;
  SELECT * FROM teacher WHERE t_id=1 //1 是上一个查询得到的 teacher_id 的值
  -->
  <select id="getClass2" parameterType="int" resultMap="ClassResultMap2">
  select * from class where c_id=#{id}
  </select>
  <resultMap type="_Classes" id="ClassResultMap2">
  <id property="id" column="c_id"/>
  <result property="name" column="c_name"/>
  <association property="teacher" column="teacher_id" select="getTeacher">
  </association>
  </resultMap>
  <select id="getTeacher" parameterType="int" resultType="_Teacher">
  SELECT t_id id, t_name name FROM teacher WHERE t_id=#{id}
  </select>
  ```

* ```xml
  <!--
  	association 用于一对一的关联查询的
  	property 对象属性的名称
  	javaType 对象属性的类型
  	column 所对应的外键字段名称
  	select 使用另一个查询封装的结果
  -->
  ```

### 一对多关联

* ```xml
  <!--
  方式一: 嵌套结果: 使用嵌套结果映射来处理重复的联合结果的子集
  SELECT * FROM class c, teacher t,student s WHERE c.teacher_id=t.t_id AND c.C_id=s.class_id AND c.c_id=1
  -->
  <select id="getClass3" parameterType="int" resultMap="ClassResultMap3">
  select * from class c, teacher t,student s where c.teacher_id=t.t_id and c.C_id=s.class_id and
  c.c_id=#{id}
  </select>
  <resultMap type="_Classes" id="ClassResultMap3">
  <id property="id" column="c_id"/>
  <result property="name" column="c_name"/>
  <association property="teacher" column="teacher_id" javaType="_Teacher">
  <id property="id" column="t_id"/>
  <result property="name" column="t_name"/>
  </association>
  <!-- ofType 指定 students 集合中的对象类型 -->
  <collection property="students" ofType="_Student">
  <id property="id" column="s_id"/>
  <result property="name" column="s_name"/>
  </collection>
  </resultMap>
  <!--
  方式二：嵌套查询：通过执行另外一个 SQL 映射语句来返回预期的复杂类型
  SELECT * FROM class WHERE c_id=1;
  SELECT * FROM teacher WHERE t_id=1 //1 是上一个查询得到的 teacher_id 的值
  SELECT * FROM student WHERE class_id=1 //1 是第一个查询得到的 c_id 字段的值
  -->
  <select id="getClass4" parameterType="int" resultMap="ClassResultMap4">
  select * from class where c_id=#{id}
  </select>
  <resultMap type="_Classes" id="ClassResultMap4">
  <id property="id" column="c_id"/>
  <result property="name" column="c_name"/>
  <association property="teacher" column="teacher_id" javaType="_Teacher"
  select="getTeacher2"></association>
  <collection property="students" ofType="_Student" column="c_id" select="getStudent"></collection>
  </resultMap>
  <select id="getTeacher2" parameterType="int" resultType="_Teacher">
  SELECT t_id id, t_name name FROM teacher WHERE t_id=#{id}
  </select>
  <select id="getStudent" parameterType="int" resultType="_Student">
  SELECT s_id id, s_name name FROM student WHERE class_id=#{id}
  </select>
  ```

* ```xml
  <!--
  	collection 做一对多关联查询的
  	ofType 指定集合中元素对象的类型
  -->
  ```

* 

### 模糊查询

* ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
  <mapper namespace="com.atguigu.day03_mybatis.test6.userMapper">
  <select id="getUser" parameterType="com.atguigu.day03_mybatis.test6.ConditionUser"
  resultType="com.atguigu.day03_mybatis.test6.User">
  select * from d_user where age>=#{minAge} and age&lt;=#{maxAge}
  <if test='name!="%null%"'>and name like #{name}</if>
      <!-- 其中#{name}里面是（"%"+realName+"%"）-->
  </select>
  </mapper>
  ```

* 

### 使用Mapper接口编程的规范

* Mapper 接口的名称和对应的Mapper.xml映射文件的名称一致
* Mapper.xml 文件中的namespace与Mapper接口的路径相同（即接口文件和映射文件需要放在同一个包中）
* Mapper接口中的方法名和Mapper.xml定义的每个执行语句的id相同
* Mapper接口中的方法的输入参数类型要和Mapper.xml中定义的每个sql的parameterType的类型相同
* Mapper接口方法的输出参数类型要和Mapper.xml中定义的每个sql的resultType的类型相同



### MyBatis分页插件

* ```xml
  <!--1.配置maven-->
  <dependency>
      <groupId>com.github.pagehelper</groupId>
      <artifactId>pagehelper</artifactId>
      <version>最新版本</version>
  </dependency>
  ```

* ```xml
  <!--2.在 MyBatis 配置 xml 中配置拦截器插件-->
  <!-- 
      plugins在配置文件中的位置必须符合要求，否则会报错，顺序如下:
      properties?, settings?, 
      typeAliases?, typeHandlers?, 
      objectFactory?,objectWrapperFactory?, 
      plugins?, 
      environments?, databaseIdProvider?, mappers?
  -->
  <plugins>
      <!-- com.github.pagehelper为PageHelper类所在包名 -->
      <plugin interceptor="com.github.pagehelper.PageInterceptor">
          <!-- 使用下面的方式配置参数，后面会有所有的参数介绍 -->
          <property name="param1" value="value1"/>
  	</plugin>
  </plugins>
  ```

* ```java
  //3.使用方法
  //第一种，RowBounds方式的调用
  List<Country> list = sqlSession.selectList("x.y.selectIf", null, new RowBounds(0, 10));
  //其中，如果需要将全部数据传出，则为第二个参数改为new RowBounds()
  //其中，0代表从第0条开始，总共10个
  ```

* 

