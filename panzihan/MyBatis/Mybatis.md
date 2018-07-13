# MyBatis

## 1、MyBatis介绍

MyBatis是支持普通SQL查询，存储过程和高级映射的优秀持久层框架。MyBatis消除了几乎所有的JDBC代码和参数的手工设置以及对结果集的检索封装。MyBatis可以使用简单的XML或注解用于配置和原始映射，将接口和Java的POJO（Plain Old Java Objects，普通的Java对象）映射成数据库中的记录

![](https://github.com/Rzihan/JavaStudy/blob/master/image/MyBatis/JDBC.png?raw=true)

## 2、MyBatis快速入门

### 2.1、添加jar包

```
[mybatis] mybatis-3.1.1.jar
[mysql驱动包] mysql-connector-java-5.1.7-bin.jar
```

### 2.2、建库+表

```sql
create database mybatis;
use mybatis;
CREATE TABLE users(id INT PRIMARY KEY AUTO_INCREMENT, NAME VARCHAR(20), age INT);
INSERT INTO users(NAME, age) VALUES('Tom', 12);
INSERT INTO users(NAME, age) VALUES('Jack', 11);
```

### 2.3、添加Mybatis的配置文件 conf.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!--work:工作模式;development:开发者模式-->
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC" />
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver" />
                <property name="url" value="jdbc:mysql://localhost:3306/mybatis" />
                <property name="username" value="root" />
                <property name="password" value="root" />
            </dataSource>
        </environment>
    </environments>
</configuration>
```

### 2.4、定义表所对应的实体类

```java
public class User {
    private int id;
    private String name;
    private int age;
    //get,set方法
}
```

### 2.5、定义操作users表的sql映射文件userMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace=" com.atguigu.mybatis_test.test1.userMapper">
    <select id="getUser" parameterType="int"
    	resultType="com.atguigu.mybatis_test.test1.User">
    	select * from users where id=#{id}
    </select>
</mapper>
```

### 2.6、在conf.xml文件中注册userMapper.xml文件

```xml
<mappers>
	<mapper resource="com/atguigu/mybatis_test/test1/userMapper.xml"/>
</mappers>
```

### 2.7、编写测试代码：执行定义的select语句

```java
public class Test {
    public static void main(String[] args) throws IOException {
        String resource = "/conf.xml";
        //加载mybatis的配置文件
        InputStream is = Test.class.getResourceAsStream(resource);
        //构建sqlSession工厂
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
        //创建能执行映射文件中的sql的sqlSession
        SqlSession session = sessionFactory.openSession();
        //映射sql的标识字符串
        String statement = "www.panzihan.po.User" + ".getUser";
        //执行查询返回一个唯一的user对象的sql
        User user = session.selectOne(statement, 1);
        System.out.println(user);
    }
}
```

## 3、操作users表的CRUD

### 3.1、XML的实现

#### 3.1.1、定义sql映射xml文件：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="www.panzihan.po.userMapper">
    <select id="getUser" parameterType="int" resultType="www.panzihan.po.User">
    	SELECT * FROM users WHERE id = #{id}
    </select>

    <insert id="addUser" parameterType="www.panzihan.po.User">
        INSERT INTO users(name,age) VALUES(#{name}, #{age})
    </insert>

    <update id="updateUser" parameterType="www.panzihan.po.User">
        UPDATE users SET name = #{name}, age = #{age} WHERE id = #{id}
    </update>

    <delete id="deleteUser" parameterType="int">
        DELETE FROM users WHERE id = #{id}
    </delete>

    <select id="selectAllUsers" resultType="www.panzihan.po.User">
        SELECT * FROM users
    </select>
</mapper>
```

#### 3.1.2、在config.xml注册这个映射文件

```xml
<mappers>
    <mapper resource="www\panzihan\po\userMapper.xml"/>
</mappers>
```

#### 3.1.3、代码演示

```java
package www.panzihan.Util;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import www.panzihan.po.Test;

import java.io.InputStream;

public class MyBatisUtil {

    public static SqlSessionFactory getSessionFactory() {
        String resource = "/conf.xml";
        //加载mybatis的配置文件
        InputStream is = Test.class.getResourceAsStream(resource);
        //构建sqlSession工厂
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
        return sessionFactory;
    }
}

```

```java
package www.panzihan.po;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import www.panzihan.Util.MyBatisUtil;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        SqlSessionFactory sessionFactory = MyBatisUtil.getSessionFactory();
        SqlSession session = sessionFactory.openSession();

        //查找方法
//        String statement = "www.panzihan.po.userMapper.getUser";
//        User user = session.selectOne(statement, 2);
//        System.out.println(user);

        //增加方法
//        String statement = "www.panzihan.po.userMapper.addUser";
//        int result = session.insert(statement, new User(-1, "li", 12));
//        System.out.println(result);

        //更新方法
//        String statement = "www.panzihan.po.userMapper.updateUser";
//        int result = session.update(statement, new User(3, "panzihan", 20));
//        System.out.println(result);

        //删除方法
//        String statement = "www.panzihan.po.userMapper.deleteUser";
//        int result = session.delete(statement, 1);
//        System.out.println(result);

        //查找所有方法
        String statement = "www.panzihan.po.userMapper.selectAllUsers";
        List<User> list = session.selectList(statement);
        System.out.println(list);

        session.commit();
        session.close();
    }
}

```

### 3.2、注解的实现

#### 3.2.1、定义sql映射的接口

```java
package www.panzihan.po;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface userMapper2 {

    @Insert("INSERT INTO users(name,age) VALUES(#{name},#{age})")
    public int addUser(User user);

    @Delete("DELETE FROM users WHERE id = #{id}")
    public int deleteUserById(int id);

    @Update("UPDATE users SET name = #{name}, age = #{age} WHERE id = #{id}")
    public int updateUser(User user);

    @Select("SELECT * FROM users WHERE id = #{id}")
    public User getUserById(int id);

    @Select("SELECT * FROM users")
    public List<User> getAllUsers();
}

```

#### 3.2.2、在 config 中注册这个映射接口 

```xml
<mappers>
        <mapper class="www.panzihan.po.userMapper2"/>
</mappers>
```

## 4、几个可以优化的地方

### 4.1、连接数据库的配置单独放在一个properties文件中

```xml
## db.properties
<properties resource="db.properties"/>
<property name="driver" value="${driver}" />
<property name="url" value="${url}" />
<property name="username" value="${username}" />
<property name="password" value="${password}" />
```

### 4.2、为实体类定义别名，简化sql映射xml文件中的引用

```xml
<!--配置实体类的别名-->
<typeAliases>
	<typeAlias type="www.panzihan.po.User" alias="User"/>
</typeAliases>
```

## 5、解决字段名与实体类属性名不相同的冲突

### 5.1、通过sql语句中定义别名

```xml
<result id = "selectOrder" parameterType = "int" resultType = "_Order">
    SELECT order_id id, order_no orderNo, order_price price FROM orders WHERE order_id = #{id}
</result>
```

### 5.2、通过<resultMap>

```xml
<select id="selectOrderResultMap" parameterType="int" resultMap="orderResultMap">
    SELECT * FROM orders WHERE order_id = #{id}
</select>

<resultMap type="_Order" id="orderResultMap">
	<id property="id" column="order_id"/>
    <result property="orderNo" column="order_no"/>
    <result property="price" column="order_price"/>
</resultMap>
```

## 6、实现关联表查询

### 6.1、一对一关联

#### 1）提出需求

根据班级id查询班级信息（带老师信息）

#### 2）创建表和数据

```sql
CREATE TABLE teacher(
    t_id INT PRIMARY KEY AUTO_INCREMENT,
    t_name VARCHAR(20)
);
CREATE TABLE class(
    c_id INT PRIMARY KEY AUTO_INCREMENT,
    c_name VARCHAR(20),
    teacher_id INT
);
ALTER TABLE class ADD CONSTRAINT fk_teacher_id FOREIGN KEY (teacher_id) REFERENCES
teacher(t_id);
INSERT INTO teacher(t_name) VALUES('LS1');
INSERT INTO teacher(t_name) VALUES('LS2');
INSERT INTO class(c_name, teacher_id) VALUES('bj_a', 1);
INSERT INTO class(c_name, teacher_id) VALUES('bj_b', 2);

```

#### 3）定义实体类

```java
public class Teacher {
    private int id;
    private String name;
}

public class Classes {
    private int id;
    private String name;
    private Teacher teacher;
}
```

#### 4）定义sql映射文件ClassMapper.xml

* ```sql
  方式一（嵌套结果）：
  使用嵌套结果映射来处理重复的联合结果的子集 
  封装联表查询的数据(去除重复的数据)
  select * from class c, teacher t where c.teacher_id=t.t_id
  ```

```xml
<select id="getAllClasses" resultMap="ClassResultMap">
    SELECT * FROM class c, teacher t WHERE c.teacher_id=t.t_id
</select>

<resultMap id="ClassResultMap" type="www.panzihan.po.Classes">
    <id property="id" column="c_id"/>
    <result property="name" column="c_name"/>
    <association property="teacher" column="teacher_id" javaType="www.panzihan.po.Teacher">
        <id property="id" column="t_id"/>
        <result property="name" column="t_name"/>
    </association>
</resultMap>
```

* 测试

```java
@Test
public void testGetAllClasses() {
    SqlSessionFactory sessionFactory = MyBatisUtil.getSessionFactory();
    SqlSession session = sessionFactory.openSession();
    String statement = "www.panzihan.po.ClassMapper" + ".getAllClasses";
    List<Classes> list = session.selectList(statement);
    System.out.println(list);
    session.close();
}
```

* ```sql
  方式二：嵌套查询：通过执行另外一个 SQL 映射语句来返回预期的复杂类型
  SELECT * FROM class WHERE c_id=1;
  SELECT * FROM teacher WHERE t_id=1 //1 是上一个查询得到的 teacher_id 的值
  ```

```xml
<select id="getAllClasses2" resultMap="ClassResultMap2">
    SELECT * FROM class
</select>

<select id="getTeacher" parameterType="int" resultType="www.panzihan.po.Teacher">
    SELECT t_id id, t_name name FROM teacher WHERE t_id = #{id}
</select>

<resultMap id="ClassResultMap2" type="www.panzihan.po.Classes">
    <id property="id" column="c_id"/>
    <result property="name" column="c_name"/>
    <association property="teacher" select="getTeacher"
                 column="teacher_id" javaType="www.panzihan.po.Teacher"/>
</resultMap>
```

* 测试

```java
@Test
public void testGetAllClasses2() {
    SqlSessionFactory sessionFactory = MyBatisUtil.getSessionFactory();
    SqlSession session = sessionFactory.openSession();
    String statement = "www.panzihan.po.ClassMapper" + ".getAllClasses2";
    List<Classes> list = session.selectList(statement);
    System.out.println(list);
    session.close();
}
```

#### 5）小结

1. association：用于一对一的关联查询的
2. property：对象属性的名称
3. javaType：对象属性的类型
4. column：所对应的外键字段名称
5. select：使用另一个查询封装的结果

### 6.2、一对多关联

#### 1）提出需求

根据classId查询对应的班级信息，包括学生，老师

#### 2）创建表和数据

```sql
CREATE TABLE student(
    s_id INT PRIMARY KEY AUTO_INCREMENT,
    s_name VARCHAR(20),
    class_id INT
);
INSERT INTO student(s_name, class_id) VALUES('xs_A', 1);
INSERT INTO student(s_name, class_id) VALUES('xs_B', 1);
INSERT INTO student(s_name, class_id) VALUES('xs_C', 1);
INSERT INTO student(s_name, class_id) VALUES('xs_D', 2);
INSERT INTO student(s_name, class_id) VALUES('xs_E', 2);
INSERT INTO student(s_name, class_id) VALUES('xs_F', 2);
```

#### 3）定义实体类

```java
public class Student {
    private int id;
    private String name;
}
public class Classes {
    private int id;
    private String name;
    private Teacher teacher;
    private List<Student> student;
}
```

#### 4）定义sql映射文件ClassMapper.xml

##### 方式一：

```sql
方式一: 嵌套结果: 使用嵌套结果映射来处理重复的联合结果的子集
SELECT * FROM class c, teacher t,student s WHERE c.teacher_id=t.t_id AND c.C_id=s.class_id AND c.c_id=1
```

xml文件的配置：

```xml
<select id="getAllClasses3" resultMap="ClassResultMap3">
    SELECT * FROM class c, teacher t,student s WHERE c.teacher_id=t.t_id AND c.C_id=s.class_id
</select>

<resultMap id="ClassResultMap3" type="www.panzihan.po.Classes">
    <id property="id" column="c_id"/>
    <result property="name" column="c_name"/>
    <association property="teacher" column="teacher_id" javaType="www.panzihan.po.Teacher">
        <id property="id" column="t_id"/>
        <result property="name" column="t_name"/>
    </association>
    <collection property="list" ofType="www.panzihan.po.Student">
        <id property="id" column="s_id"/>
        <result property="name" column="s_name"/>
    </collection>
</resultMap>
```

测试：

```java
@Test
public void testGetAllClasses3() {
    SqlSessionFactory sessionFactory = MyBatisUtil.getSessionFactory();
    SqlSession session = sessionFactory.openSession();
    String statement = "www.panzihan.po.ClassMapper" + ".getAllClasses3";
    List<Classes> list = session.selectList(statement);
    System.out.println(list);
    session.close();
}
```

##### 方式二：

```sql
方式二：嵌套查询：通过执行另外一个 SQL 映射语句来返回预期的复杂类型
SELECT * FROM class WHERE c_id=1;
SELECT * FROM teacher WHERE t_id=1 //1 是上一个查询得到的 teacher_id 的值
SELECT * FROM student WHERE class_id=1 //1 是第一个查询得到的 c_id 字段的值
```

xml文件的配置：

```xml
<select id="getClass4" parameterType="int" resultMap="ClassResultMap4">
	select * from class where c_id=#{id}
</select>

<resultMap type="_Classes" id="ClassResultMap4">
    <id property="id" column="c_id"/>
    <result property="name" column="c_name"/>
    <association property="teacher" column="teacher_id" javaType="_Teacher"
    select="getTeacher2"/>
    <collection property="students" ofType="_Student" column="c_id" select="getStudent"/>	
</resultMap>

<select id="getTeacher2" parameterType="int" resultType="_Teacher">
	SELECT t_id id, t_name name FROM teacher WHERE t_id=#{id}
</select>

<select id="getStudent" parameterType="int" resultType="_Student">
	SELECT s_id id, s_name name FROM student WHERE class_id=#{id}
</select>
```

测试：

```java
@Test
public void testGetAllClasses4() {
    SqlSessionFactory sessionFactory = MyBatisUtil.getSessionFactory();
    SqlSession session = sessionFactory.openSession();
    String statement = "www.panzihan.po.ClassMapper" + ".getAllClasses4";
    List<Classes> list = session.selectList(statement);
    System.out.println(list);
    session.close();
}
```

#### 5）小结

1. collection：做一对多关联查询的。
2. ofType：指定集合中元素对象的类型。

## 7、动态sql与模糊查询

### 7.1、提出需求：

实现多条件查询用户（姓名模糊匹配，年龄在指定的最小值到最大值之间）

### 7.2、准备数据表和数据：

```sql
create table d_user(
    id int primary key auto_increment,
    name varchar(10),
    age int(3)
);
insert into d_user(name,age) values('Tom',12);
insert into d_user(name,age) values('Bob',13); 
insert into d_user(name,age) values('Jack',18);
```



### 7.3、ConditionUser（查询条件实体类）：

```java
private String name;
private int minAge;
private int maxAge;
```

### 7.4、User（表实体类）:

```java
private int id;
private String name;
private int age;
```



### 7.5、userMapper.xml（映射文件）

```xml
<mapper namespace="www.panzihan.mybatis.userMapper">

    <select id="getUser" parameterType="www.panzihan.mybatis.ConditionUser"
        resultType="www.panzihan.mybatis.User">
        SELECT * FROM d_user WHERE age BETWEEN #{minAge} AND #{maxAge}
        <if test='name != "%null%"'> AND name like #{name}</if>
    </select>

</mapper>
```

### 7.6、UserTest（测试）

```java
@Test
public void testGetUser() {
    SqlSessionFactory sessionFactory = MyBatisUtil.getSessionFactory();
    SqlSession session = sessionFactory.openSession();
    String statement = "www.panzihan.mybatis.userMapper" + ".getUser";
    String name = "o";
    ConditionUser conditionUser = new ConditionUser( "%" + name + "%", 18, 13);
    List<User> users = session.selectList(statement, conditionUser);
    System.out.println(users);
    session.close();
}
```

## 8、调用存储过程

### 8.1、提出需求：

查询得到男性或女性的数量，如果传入的是0就女性否则是男性。

### 8.2、准备数据库表的存储过程

```sql
create table p_user(
    id int primary key auto_increment,
    name varchar(10),
    sex char(2)
);
insert into p_user(name,sex) values('A',"男");
insert into p_user(name,sex) values('B',"女");
insert into p_user(name,sex) values('C',"男"); 

DELIMITER $
CREATE PROCEDURE mybatis.ges_user_count(IN sex_id INT, OUT user_count INT)
BEGIN
IF sex_id=0 THEN
SELECT COUNT(*) FROM mybatis.p_user WHERE p_user.sex='女' INTO user_count;
ELSE
SELECT COUNT(*) FROM mybatis.p_user WHERE p_user.sex='男' INTO user_count;
END IF;
END
$
DELIMITER ;

#调用存储过程
SET @user_count = 0;
CALL mybatis.ges_user_count(1, @user_count);
SELECT @user_count;

```

### 8.3、创建表的实体类

```java
public class User {
    private String id;
    private String name;
    private String sex;
}	
```

### 8.4、userMapper.xml

```xml
<mapper namespace="www.panzihan.test2.userMapper">
    <select id="getUserCount" parameterMap="getUserCountMap" statementType="CALLABLE">
        CALL mybatis.ges_user_count(?,?)
    </select>
    <parameterMap id="getUserCountMap" type="java.util.Map">
        <parameter property="sexid" mode="IN" jdbcType="INTEGER"/>
        <parameter property="usercount" mode="OUT" jdbcType="INTEGER"/>
    </parameterMap>
</mapper>
```

### 8.5、测试调用

```java
@Test
public void testGetCount() {
    SqlSessionFactory sessionFactory = MyBatisUtil.getSessionFactory();//自己写的获取会话工厂的方法
    SqlSession session = sessionFactory.openSession();
    String statement = "www.panzihan.test2.userMapper" + ".getUserCount";
    Map<String, Integer> parameterMap = new HashMap<String, Integer>();
    parameterMap.put("sexid", 1);
    parameterMap.put("usercount", -1);
    session.selectOne(statement, parameterMap);
    Integer integer = parameterMap.get("usercount");
    System.out.println(integer);
    session.close();
}

```

## 9、MyBatis缓存

### 9.1、理解MyBatis缓存

正如大多数持久层框架一样，MyBatis同样提供了一级缓存和二级缓存的支持；

1. 一级缓存：基于PerpetualCache的HashMap本地缓存，器存储作用域为Session，当Session flush或close之后，该Session中的所有Cache就将清空。
2. 二级缓存与一级缓存其机制相同，默认也是采用PerpetualCache，HashMap存储，不同在于其存储作用域为Mapper（Namespace），并且可自定义存储源，如Ehcache。
3. 对于缓存数据更新机制，当某一个作用域（一级缓存Session/二级缓存Namespaces）的进行了C/U/D操作后，默认该作用域下所有select中的缓存将被clear。

### 9.2、Mybatis一级缓存

1. 一级缓存: 也就 Session 级的缓存(默认开启) 。
2. 必须是同一个 Session,如果 session 对象已经 close()过了就不可能用了 。
3. 查询条件是一样的 。
4. 没有执行过 session.clearCache()清理缓存。 
5. 没有执行过增删改的操作(这些操作都会清理缓存)。

### 9.3、Mybatis二级缓存

```xml
<cache><cache/>
```

### 9.4、补充说明

1. 映射语句文件中的所有select语句会将被缓存
2. 映射语句文件中的所有insert，update和delete语句会刷新缓存。
3. 缓存会使用Least Recently Used(LRU，最近最少使用的)算法来收回。
4. 缓存在根据指定的时间间隔来刷新。
5. 缓存会存储1024个对象

```xml
<cache 
 	eviction="FIFO" //回收策略为先进先出
    flushInterval="60000" //自动刷新时间60s
    size="512" //最多缓存512个引用对象
    readOnly="true" //只读>
</cache>
```

