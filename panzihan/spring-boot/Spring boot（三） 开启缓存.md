# Spring boot开启缓存

## 1、依赖包的引入

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
   <modelVersion>4.0.0</modelVersion>

   <groupId>com.pzh.springbootcache</groupId>
   <artifactId>cache-demo</artifactId>
   <version>0.0.1-SNAPSHOT</version>
   <packaging>jar</packaging>

   <name>cache-demo</name>
   <description>Demo project for Spring Boot</description>

   <parent>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-parent</artifactId>
      <version>2.0.4.RELEASE</version>
      <relativePath/> <!-- lookup parent from repository -->
   </parent>

   <properties>
      <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
      <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
      <java.version>1.8</java.version>
   </properties>

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
            <version>1.3.0</version>
        </dependency>

      <dependency>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-data-redis</artifactId>
      </dependency>

      <dependency>
         <groupId>mysql</groupId>
         <artifactId>mysql-connector-java</artifactId>
         <scope>runtime</scope>
      </dependency>

      <dependency>
         <groupId>com.alibaba</groupId>
         <artifactId>druid</artifactId>
         <version>1.0.29</version>
      </dependency>

   </dependencies>

   <build>
      <plugins>
         <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
         </plugin>
      </plugins>
   </build>

</project>
```

## 2、配置文件

```properties
#数据库的配置
spring.datasource.url=jdbc:mysql://localhost:3306/test?characterEncoding=utf8&useSSL=true
spring.datasource.username=root
spring.datasource.password=123456
spring.datasource.driver-class-name=com.mysql.jdbc.Driver

#连接池的配置
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
spring.datasource.initialSize=5
spring.datasource.minIdle=5
spring.datasource.maxActive=20
spring.datasource.maxWait=60000
spring.datasource.show-sql=true

#mybatis的配置
mybatis.mapper-locations=classpath*:mapper/*Mapper.xml
mybatis.type-aliases-package=com.cwh.springbootMybatis.entity

#redis的配置
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.password=
spring.redis.database=1
spring.redis.pool.max-active=20
spring.redis.pool.max-wait=-1
spring.redis.pool.max-idle=500
spring.redis.pool.min-idle=5
spring.redis.timeout=0

#日志的配置
logging.level.com.pzh.springbootcache.cachedemo=DEBUG

```

## 3、具体代码

### 3.1、StudentMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.pzh.springbootcache.cachedemo.mapper.StudentMapper">

    <insert id="addStudent"
            parameterType="Student"
            useGeneratedKeys="true"
            keyColumn="id"
            keyProperty="id">
        INSERT INTO
            students(name, email)
        VALUES
            (#{name},#{email})
    </insert>

    <delete id="deleteStudent"
            parameterType="int">
        DELETE FROM students
        WHERE id = #{id}
    </delete>

    <update id="updateStudent"
            parameterType="Student">
        UPDATE
            students
        SET
            name = #{name},
            email = #{email}
        WHERE
            id = #{id}
    </update>

    <select id="findStudent"
            parameterType="int"
            resultType="Student">
        SELECT
            *
        FROM
            students
        WHERE
            id = #{id}
    </select>

    <select id="findAllStudent"
            resultType="Student">
        SELECT
            *
        FROM
            students
    </select>

</mapper>
```

### 3.2、StudentMapper

```java
@CacheConfig(cacheNames = "students")
@Mapper
public interface StudentMapper {

    /**
     * 代表往缓存里添加值，key为参数Student的id属性，
     * 这样当我们add一个Student对象时，redis就会新增一个以id为key的Student对象
     * @param student 学生对象
     * @return 受影响的行数
     */
    @CachePut(key = "#p0.id")
    int addStudent(Student student);

    /**
     * 删除缓存
     * @param id 学生id
     * @return 受影响的行数
     */
    @CacheEvict(key = "#p0")
    int deleteStudent(int id);

    /**
     * 代表往缓存里修改值，key为参数Student的id属性，
     * 这样当我们update一个Student对象时，redis就会修改一个以id为key的Student对象，
     * 如果id为key的Student对象没有，redis则新增一个以id为key的Student对象
     * @param student 学生对象
     * @return 受影响的行数
     */
    @CachePut(key = "#p0.id")
    int updateStudent(Student student);

    /**
     * #p0代表第一个参数，也就是id,
     * 先从redis的student缓存对象里去查询key等于传过来的id的值。如果没有，就去查表
     * @param id 学生id
     * @return 返回查询结构
     */
    @Cacheable(key = "#p0")
    Student findStudent(int id);

    /**
     * 查询所有学生对象
     * @return 查询结果
     */
    List<Student> findAllStudent();
}
```

### 3.3、StudentService

```java
public interface StudentService {

    Student getStudent(int id);

    List<Student> getAllStudent();

    int add(Student student);

    int update(Student student);

    int delete(int id);
}
```

### 3.4、StudentServiceImpl

```java
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public Student getStudent(int id) {
        return studentMapper.findStudent(id);
    }

    @Override
    public List<Student> getAllStudent() {
        return studentMapper.findAllStudent();
    }

    @Override
    public int add(Student student) {
        return studentMapper.addStudent(student);
    }

    @Override
    public int update(Student student) {
        return studentMapper.updateStudent(student);
    }

    @Override
    public int delete(int id) {
        return studentMapper.deleteStudent(id);
    }
}
```

### 3.5、测试

```java
@SpringBootApplication
@EnableCaching
public class CacheDemoApplication {

   public static void main(String[] args) {
      SpringApplication.run(CacheDemoApplication.class, args);
   }
}
```

```java
public class StudentControllerTest extends CacheDemoApplicationTests {

    @Autowired
    private StudentService studentService;

    @Test
    public void testFindStudent() {
        Long start = System.currentTimeMillis();
        studentService.getStudent(1);
        Long end = System.currentTimeMillis();
        System.out.println("花费" + (end - start) + "毫秒");

        start = System.currentTimeMillis();
        for (int i = 0; i< 500; i++) {
            studentService.getStudent(1);
        }
        end = System.currentTimeMillis();
        System.out.println("花费" + (end - start) + "毫秒");
    }
}
```

### 

