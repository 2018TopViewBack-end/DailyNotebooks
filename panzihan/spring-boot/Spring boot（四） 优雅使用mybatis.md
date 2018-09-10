# Spring boot（四）优雅使用mybatis

### 1、mybatis-spring-boot-starter

官方说明：`MyBatis Spring-Boot-Starter will help you use MyBatis with Spring Boot`
其实就是myBatis看spring boot这么火热也开发出一套解决方案来凑凑热闹,但这一凑确实解决了很多问题，使用起来确实顺畅了许多。mybatis-spring-boot-starter主要有两种解决方案，一种是使用注解解决一切问题，一种是简化后的老传统。

当然任何模式都需要首先引入mybatis-spring-boot-starter的pom文件

```xml
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>1.3.2</version>
</dependency>
```

好了下来分别介绍两种开发模式

### 2、无配置文件注解版

#### 2.1、添加相关maven文件

```xml
<dependencies>
   <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter</artifactId>
   </dependency>

   <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
   </dependency>

   <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
   </dependency>

   <dependency>
      <groupId>org.mybatis.spring.boot</groupId>
      <artifactId>mybatis-spring-boot-starter</artifactId>
      <version>1.3.2</version>
   </dependency>

   <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
   </dependency>

   <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <version>1.16.10</version>
   </dependency>
</dependencies>
```

#### 2.2、application.properties配置

```properties
mybatis.type-aliases-package=com.pzh.springboot.po

spring.datasource.driverClassName = com.mysql.jdbc.Driver
spring.datasource.url = jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8
spring.datasource.username = root
spring.datasource.password = 123456
```

springboot会自动加载spring.datasource.*相关配置，数据源就会自动注入到sqlSessionFactory中，sqlSessionFactory会自动注入到Mapper中，对了你一切都不用管了，直接拿起来使用就行了。

在启动类中添加对mapper包扫描`@MapperScan`

```java
@SpringBootApplication
@MapperScan("com.pzh.springboot.mapper")
public class Application {

   public static void main(String[] args) {
      SpringApplication.run(Application.class, args);
   }
}
```

或者直接在Mapper类上面添加注解`@Mapper`,建议使用上面那种，不然每个mapper加个注解也挺麻烦的

#### 2.3、开发Mapper

```java
public interface UserMapper {

    @Delete("DROP table IF EXISTS users")
    void dropTable();

    @Insert("CREATE TABLE IF NOT EXISTS " +
            "users(" +
            "   id INT UNSIGNED AUTO_INCREMENT," +
            "   name VARCHAR(100) NOT NULL," +
            "   age INT NOT NULL," +
            "   PRIMARY KEY(id)" +
            ")ENGINE=InnoDB DEFAULT CHARSET=utf8")
    void createTable();

    @Insert("INSERT INTO users(name,age) values(#{name},#{age})")
    void insert(User user);

    @Select("SELECT * FROM users")
    List<User> findAll();
}
```

- @Select 是查询类的注解，所有的查询均使用这个
- @Result 修饰返回的结果集，关联实体类属性和数据库字段一一对应，如果实体类属性和数据库属性名保持一致，就不需要这个属性来修饰。
- @Insert 插入数据库使用，直接传入实体类会自动解析属性到对应的值
- @Update 负责修改，也可以直接传入对象
- @delete 负责删除

**注意，使用#符号和$符号的不同：**

```java
// This example creates a prepared statement, something like select * from teacher where name = ?;
@Select("Select * from teacher where name = #{name}")
Teacher selectTeachForGivenName(@Param("name") String name);

// This example creates n inlined statement, something like select * from teacher where name = 'someName';
@Select("Select * from teacher where name = '${name}'")
Teacher selectTeachForGivenName(@Param("name") String name);
```

#### 2.4、使用

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void test() {
        userMapper.dropTable();
        userMapper.createTable();

        userMapper.insert(new User("周杰伦",40));
        userMapper.insert(new User("林志颖",42));
        userMapper.insert(new User("李宇春", 30));

        List<User> users = userMapper.findAll();
        System.out.println(users);
        Assert.assertEquals(3, users.size());
    }

}
```

### 3、极简xml版本

极简xml版本保持映射文件的老传统，优化主要体现在不需要实现dao的是实现层，系统会自动根据方法名在映射文件中找对应的sql.

#### 3.1、添加相关的maven文件

```xml
<dependencies>
   <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter</artifactId>
   </dependency>

   <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
   </dependency>

   <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
   </dependency>

   <dependency>
      <groupId>org.mybatis.spring.boot</groupId>
      <artifactId>mybatis-spring-boot-starter</artifactId>
      <version>1.3.2</version>
   </dependency>

   <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
   </dependency>

   <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <version>1.16.10</version>
   </dependency>
</dependencies>
```

#### 3.2、application.properties配置

```properties
mybatis.type-aliases-package=com.pzh.springboot.po
mybatis.config-locations=classpath:mybatis/mybatis-config.xml
mybatis.mapper-locations=classpath:mybatis/mapper/*.xml

spring.datasource.driverClassName = com.mysql.jdbc.Driver
spring.datasource.url = jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=true
spring.datasource.username = root
spring.datasource.password = 123456
```

#### 3.3、mybatis-config.xml 配置

```xml
<configuration>
    <typeAliases>
        <typeAlias alias="Integer" type="java.lang.Integer" />
        <typeAlias alias="Long" type="java.lang.Long" />
        <typeAlias alias="HashMap" type="java.util.HashMap" />
        <typeAlias alias="LinkedHashMap" type="java.util.LinkedHashMap" />
        <typeAlias alias="ArrayList" type="java.util.ArrayList" />
        <typeAlias alias="LinkedList" type="java.util.LinkedList" />
    </typeAliases>
</configuration>
```

这里也可以添加一些mybatis基础的配置

#### 3.4、Student的映射文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.pzh.springboot.mapper.StudentMapper" >

    <resultMap id="BaseResultMap" type="com.pzh.springboot.po.Student" >
        <id column="id" property="id"/>
        <result column="name" property="name" jdbcType="VARCHAR" />
        <result column="email" property="email" jdbcType="VARCHAR" />
    </resultMap>

    <select id="getAll"
            resultMap="BaseResultMap"  >
        SELECT
          id,name,email
        FROM
            students
    </select>

    <select id="getOne"
            parameterType="int"
            resultMap="BaseResultMap" >
        SELECT
            id,name,email
        FROM
            students
        WHERE
            id = #{id}
    </select>

    <insert id="insert"
            parameterType="com.pzh.springboot.po.Student"
            useGeneratedKeys="true"
            keyProperty="id"
            keyColumn="id">
        INSERT INTO
        students
            (name, email)
        VALUES
            (#{name}, #{email})
    </insert>
    
    <update id="update"
            parameterType="com.pzh.springboot.po.Student" >
        UPDATE
            students
        SET
          <if test="name != null">name = #{name},</if>
          <if test="email != null">email = #{email},</if>
        WHERE
          id = #{id}
    </update>

    <delete id="delete"
            parameterType="int" >
        DELETE FROM
          students
        WHERE
          id =#{id}
    </delete>
</mapper>
```

#### 3.5、编写dao层的代码

```java
package com.pzh.springboot.mapper;

import com.pzh.springboot.po.Student;
import java.util.List;

public interface StudentMapper {

    Student getOne(int id);

    List<Student> getAll();

    int insert(Student student);

    int update(Student student);

    int delete(int id);

}
```

#### 3.6、使用

```java
package com.pzh.springboot.mapper;

import com.pzh.springboot.ApplicationTests;
import com.pzh.springboot.po.Student;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class StudentMapperTest extends ApplicationTests {

    @Autowired
    private StudentMapper studentMapper;

    @Test
    public void getOne() {
        Student student = studentMapper.getOne(1);
        System.out.println(student);
        assertNotNull(student);
    }

    @Test
    public void getAll() {
        List<Student> students = studentMapper.getAll();
        System.out.println(students);
        assertNotNull(students);
    }

    @Test
    public void insert() {
        String name = UUID.randomUUID().toString();
        String email = UUID.randomUUID().toString();
        Student student = new Student(name, email);
        int result = studentMapper.insert(student);
        assertEquals(1, result);
    }

    @Test
    public void update() {
        String name = UUID.randomUUID().toString();
        String email = UUID.randomUUID().toString();
        Student student = new Student(name, email);
        student.setId(1);
        int result = studentMapper.update(student);
        assertEquals(1, result);
    }

    @Test
    public void delete() {
        String name = UUID.randomUUID().toString();
        String email = UUID.randomUUID().toString();
        Student student = new Student(name, email);
        studentMapper.insert(student);
        int result = studentMapper.delete(student.getId());
        assertEquals(1, result);
    }
}
```

