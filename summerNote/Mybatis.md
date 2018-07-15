# Mybatis

### IDEA搭建第一个mybatis工程

* 创建一个maven项目

* 配置pom.xml

  ```
   <!-- mybatis核心包 -->
           <dependency>
              <groupId>org.mybatis</groupId>
              <artifactId>mybatis</artifactId>
              <version>3.3.0</version>
          </dependency>
          <!-- mysql驱动包 -->
          <dependency>
              <groupId>mysql</groupId>
              <artifactId>mysql-connector-java</artifactId>
              <version>5.1.29</version>
          </dependency>
          <!-- junit测试包 -->
          <dependency>
              <groupId>junit</groupId>
              <artifactId>junit</artifactId>
              <version>4.11</version>
              <scope>test</scope>
          </dependency>
          <!-- 日志文件管理包 -->
          <dependency>
              <groupId>log4j</groupId>
              <artifactId>log4j</artifactId>
              <version>1.2.17</version>
          </dependency>
          <dependency>
              <groupId>org.slf4j</groupId>
              <artifactId>slf4j-api</artifactId>
              <version>1.7.12</version>
          </dependency>
          <dependency>
              <groupId>org.slf4j</groupId>
              <artifactId>slf4j-log4j12</artifactId>
              <version>1.7.12</version>
          </dependency>
      </dependencies>
  ```

* 在src/java下新建一个包com.lql.pojo,然后在该包下创建一个java文件，文件名User.

* 在src/main/resources下创建一个文件夹mapper，在该文件夹下创建一个UserMapper.xml文件

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.hgc.pojo.User">
    <select id="findById" parameterType="int" resultType="com.hgc.pojo.User">
        SELECT * FROM User WHERE id=#{id}
    </select>
</mapper>
```

* 在src/main/resources下创建mysql.properties文件,代码如下 

```
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost/mybatis 
jdbc.username=root 
jdbc.password=My159357@sql 
```

* 在src/main/resources下创建mybatis-config.xml文件,代码如下

  ```
  <?xml version="1.0" encoding="UTF-8"?>
  <!DOCTYPE configuration
          PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
          "http://mybatis.org/dtd/mybatis-3-config.dtd">
  <configuration>
      <properties resource="mysql.properties"/>
      <settings>
          <!--全局性设置懒加载。如果设为‘false’，则所有相关联的都会被初始化加载,默认值为false-->
          <setting name="lazyLoadingEnabled" value="true"/>
          <!--当设置为‘true’的时候，懒加载的对象可能被任何懒属性全部加载。否则，每个属性都按需加载。默认值为true-->
          <setting name="aggressiveLazyLoading" value="false"/>
      </settings>
      <typeAliases>
          <!-- 其实就是将bean的替换成一个短的名字-->
          <typeAlias type="com.hgc.pojo.User" alias="User"/>
      </typeAliases>
      <!--对事务的管理和连接池的配置-->
      <environments default="development">
          <environment id="development">
              <transactionManager type="JDBC"></transactionManager>
              <dataSource type="POOLED"><!--POOLED：使用Mybatis自带的数据库连接池来管理数据库连接-->
                  <property name="driver" value="${jdbc.driver}"/>
                  <property name="url" value="${jdbc.url}"/>
                  <property name="username" value="${jdbc.username}"/>
                  <property name="password" value="${jdbc.password}"/>
              </dataSource>
          </environment>
      </environments>
      <!--mapping文件路径配置-->
      <mappers>
          <mapper resource="mapper/UserMapper.xml"/>
      </mappers>
  
  </configuration>
  ```

* 建立数据库

  ```
  CREATE TABLE `User` ( 
  `id` int(11) NOT NULL AUTO_INCREMENT, 
  `name` varchar(255) DEFAULT NULL, 
  `age` int(11) DEFAULT NULL, PRIMARY KEY (`id`) ) 
  ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8; 
  INSERT INTO `User` VALUES (1, 'test', 18); 
  INSERT INTO `User` VALUES (2, '张三', 25); 
  ```

* 在src/main/java下创建一个包com.hgc.test，在该包下创建UserTest.java文件，代码如下 

```
public class UserTest {
	public static void main(String[] args) { 
		String resource ="mybatis-config.xml"; 
		Reader reader = null; 
		try { 
			reader = Resources.getResourceAsReader(resource);
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader); 			SqlSession session = sqlSessionFactory.openSession(); 
			User user = session.selectOne("findById",2); 
			session.commit(); 
			System.out.println(user.getName()); 
		}catch (IOException e){
        	e.printStackTrace(); 	        	
       	} 
    } 
} 
```

* 运行
* [项目源码](https://github.com/Charlie12138/EndlessGit/tree/master/protectProject/Test)