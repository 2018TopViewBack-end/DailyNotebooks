## 基本配置

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

### 基于xml（更优）：

### 添加：

```
<insert id="addUser" parameterType="cn.medwin.test.User">
	insert into user(name. age) values(#{name}, #{age})
	<!-- 用到了反射 -->
</insert>
```

```
int insert = session.insert(statement，new User（..）);//执行操作并用int保存被影响行数
```



### 删除：

```
<delete id="deleteUser" paremeterType=“int”>
	delete from users where id = #{id}
	<!-- {}内内容可随意写，但一般写属性名-->
</delete>
```

### 更新:

```
<update id="updateUser" parameterType="cn.medwin.test.User">
	update user set name = #{name}, age = #{age} where id = #{id}
</update>
```

### 查询：

```
<select id="getAllUsers" resultType="cn.medwin.test.User">
	select * from users
</select>
```

- session默认手动提交，因此在insert操作后需要加上,表中才会插入

  ```
  session.commit();
  
  session.close();//记得关闭资源
  ```

  要自动添加，将factory.openSession()参数改为true

- 为什么查询单个时不自动提交也没commit会得到结果？

### 基于注解：

需要有xxxMapper接口

```
public interface UserMapper(){

	@insert("此处为xml法中<>之间语句，如下")
    int add(User user);
    
    @delete(delete from users where id = #{id})
    int deleteById(int id);
    
    ...
    int update(User user);
    
    User getById(int id);
    
    List<User> getAll();
}
```

```
test中：

UserMapper mapper = session.getMapper(UserMapper.class);
//得到产生的动态对象

int add = mapper.add(new User(id, name, age))
```

同样需要在conf.xml注册，写法：

```

<mapper class="com.atguigu.day03_mybaits.test3.UserMapper"/>
```



## 配置文件中的别名以及mapper中的namespace

我们可以通过在主配置文件中配置**别名**，就不再需要指定完整的包名了 

别名的基本用法： 

```
<configuration>  
    <typeAliases>  
      <typeAlias type="com.domain.Student" alias="Student"/>  
  </typeAliases>  
  ......  
</configuration>  
```

但是如果每一个实体类都这样配置还是有点麻烦这时我们可以**直接指定package的名字**， mybatis会自动扫描指定包下面的javabean，并且默认设置一个别名，默认的名字为： javabean 的首字母小写的非限定类名来作为它的别名（其实别名是不分大小写的）。

也可在javabean 加上注解@Alias 来自定义别名， 例如： @Alias(student) 

```
<typeAliases>  
    <package name="com.domain"/>  
</typeAliases>  
```

这样，在Mapper中我们就不用每次配置都写类的全名了，但是**有一个例外，那就是namespace**。 

### namespace属性

在MyBatis中，Mapper中的namespace用于绑定Dao接口的，即面向接口编程。

它的好处在于当使用了namespace之后就可以不用写接口实现类，业务逻辑会直接通过这个绑定寻找到相对应的SQL语句进行对应的数据处理

```
student = (Student) session.selectOne("com.domain.Student.selectById", new Integer(10));  
```

```
<mapper namespace="com.domain.StudentMapper">    
  
    <select id="selectById" parameterType="int" resultType="student">    
       select * from student where id=#{id}    
    </select>  
       
</mapper>    
```

## 可优化的地方

### 将数据库配置单独放在一个properties文件中，如db.properties

此时conf.xml加上

```
<properties resource="db.properties">
```

原dataSource中内容改为

```
<property name="driver" value="${driverClassName}" />
<property name="url" value="${url}" />
<property name="username" value="${username}" />
<property name="password" value="${password}" />
```

### 为类名取别名 

见上