# MyBatis笔记

---

[TOC]

---

### 0.随记

* ```xml
  <span style="font-size:14px;"><configuration>
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
    
  </configuration></span>
  ```

* 在mybatis中，**映射文件中的namespace是用于绑定Dao接口的，即面向接口编程。** 

* MyBatis 运用了动态代理技术使接口能运行起来

* SqlSession接口有两个实现类:DefaultSqlSession和SqlSessionManager

* 一定要记得**`sqlSession.commit();//提交事务`**

* 一定要记得:

* ```java
  finally{
      //在finally语句中确保资源被顺利关闭
      if(sqlSession != null){
          sqlSession.close();
      }
  }
  ```

* **`<resource>`**标签中要用"/"来分隔(但文件名和文件类型中间仍要用"."隔开)，而其他的用"."来分隔

* MyBatis 需要POJO的无参数的构造函数

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



