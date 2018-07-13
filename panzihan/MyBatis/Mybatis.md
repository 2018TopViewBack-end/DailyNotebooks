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
	<id property="id" column="order_id"></id>
    <result property="orderNo" column="order_no"></result>
    <result property="price" column="order_price"></result>
</resultMap>
```

