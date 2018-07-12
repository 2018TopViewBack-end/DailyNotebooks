1. src 目录下放conf.xml 配置信息

   ```
   <?xml version="1.0" encoding="UTF-8"?>
   <!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">
   <configuration>
   
   	<properties resource="db.properties"/>
   	
   	<!-- 配置实体类的别名 -->
   	<typeAliases>
   		<!-- <typeAlias type="com.atguigu.day03_mybaits.test1.User" alias="_User"/> -->
   		<package name="com.atguigu.day03_mybaits.bean"/>
   	</typeAliases>
   <!-- 
   	development : 开发模式
   	work : 工作模式
    -->
   	<environments default="development">
   		<environment id="development">
   			<transactionManager type="JDBC" />
   			<dataSource type="POOLED">  <!-- 设置为池化 -->
   				<property name="driver" value="com.mysql.jdbc.Driver" />
   				<property name="url" value="jdbc:mysql://localhost:3306/testmybatis/" />
   				<property name="username" value="root" />
   				<property name="password" value="759486" />
   			</dataSource>
   		</environment>
   	</environments>
   	
   	<mappers>
   		<mapper resource="com/atguigu/day03_mybaits/test1/userMapper.xml"/>
   		<mapper resource="com/atguigu/day03_mybaits/test2/userMapper.xml"/>
   		<mapper class="com.atguigu.day03_mybaits.test3.UserMapper"/>
   		...
   	</mappers>
   </configuration>
   
   ```

   

2. 定义操作表的sql映射文件xxMapper.xml

   ```
   <?xml version="1.0" encoding="UTF-8" ?>
   <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
   "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
   
   <mapper namespace="com.atguigu.day03_mybaits.test1.userMapper">
   
   	 <select id="getUser" parameterType="int" resultType="com.atguigu.day03_mybaits.bean.User">
   	 	select * from users where id=#{id}
   	 	<!-- select语句 -->
   	 </select>
   	 
   </mapper>
   ```



3.在conf.xml中注册xxmMapper.xml文件

- conf.xml应放在包的同级目录下！

```
<mapper resource="com/atguigu/day03_mybaits/test1/userMapper.xml"/>
```

4.构建SqlSessionFactory

```
String resource = "conf.xml";
InputStream is = Test.class.getClassLoder().getResourceAsStream(resource); //构建输入流
SqlSessionFactory factory = new SqlSessionBuilder().build(is); //把流输入factory 
SqlSession session = factory.openSession();//相当于queryRunner

String statement = "..."; //...为mapper中的namespace+id
User user = seession.selectOne(stament, 2); //获取user对象
```

## CRUD操作

### 添加：

```
<insert id="addUser" parameterType="cn.medwin.test.User">
	insert into user(name. age) values(#{name}, #{age});
	<!-- 用到了反射 -->
</insert>
```

### 删除：

```
<delete id="deleteUser" paremeterType=“int”>
	delete from users where id = #{id};
	<!-- {}内内容可随意写，但一般写属性名-->
</delete>
```

### 更新:

```
<update id="updateUser" parameterType="cn.medwin.test.User">
	update user set name = #{name}, age = #{age} where id = #{id};
</update>
```

### 查询：

```
<select id="getAllUsers" resultType="">
	select * from users
</select>
```

