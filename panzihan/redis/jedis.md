# Jedis

## 1、Jedis入门

### 1.1、配置

```xml
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
    <version>2.9.0</version>
</dependency>

<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
    <version>2.6.0</version>
</dependency>

<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
</dependency>
```

### 1.2、第一个单实例

```java
@Test
/**
 * 单实例的测试
 */
public void demo1() {
    //1、设置IP地址和端口号
    Jedis jedis = new Jedis("127.0.0.1", 6379);
    //2、保存数据
    jedis.set("name", "imooc");
    //3、获取数据
    String name = jedis.get("name");
    System.out.println(name);
    //4、释放资源
    jedis.close();
}
```

### 1.3、使用连接池的方式

```java
@Test
/**
 * 使用连接池的方式来连接
 */
public void demo2() {
    //获取连接池的配置对象
    JedisPoolConfig config = new JedisPoolConfig();
    //设置最大的连接数
    config.setMaxTotal(30);
    //设置最大空闲连接数
    config.setMaxIdle(10);

    //获取连接池
    JedisPool jedisPool = new JedisPool(config, "127.0.0.1", 6379);
    //获取核心对象
    Jedis jedis = null;
    try {
        //通过连接池获取连接
        jedis = jedisPool.getResource();
        //设置数据
        jedis.set("name", "zhangsan");
        //获取数据
        String name = jedis.get("name");
        System.out.println(name);
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        if (jedis != null) {
            jedis.close();
        }
        jedisPool.close();
    }
}
```

## 2、String的基本操作

```java
private static final String HOST = "localhost";
private static final int PORT = 6379;
private static final int DEFAULT_MAX_TOTAL = 30;
private static final int DEFAULT_MAX_IDLE = 10;
private JedisPool jedisPool;

@Before
public void initJedisPool() {
    //获取连接池的配置对象
    JedisPoolConfig config = new JedisPoolConfig();
    //设置最大的连接数
    config.setMaxTotal(DEFAULT_MAX_TOTAL);
    //设置最大空闲连接数
    config.setMaxIdle(DEFAULT_MAX_IDLE);
    //获取连接池
    jedisPool = new JedisPool(config, HOST, PORT);
}

@Test
public void testRedisString() {
    try (Jedis jedis = jedisPool.getResource()) {
        //赋值set(key,value)
        jedis.set("company", "imooc");

        //取值get(key)
        System.out.println(jedis.get("company"));

        //先获取值再设置值getSet(key, value)
        System.out.println(jedis.getSet("company", "baidu"));
        System.out.println(jedis.get("company"));

        //删除del(key)
        jedis.set("person", "jack");
        System.out.println(jedis.get("person"));
        jedis.del("person");
        System.out.println(jedis.get("person"));

        //数值增减(如果值不能转为Integer,将抛出异常JedisDataException)
        jedis.incr("num");
        System.out.println(jedis.get("num"));
        jedis.incr("num");
        System.out.println(jedis.get("num"));
        //jedis.incr("company");将抛出异常
        jedis.decr("num");
        System.out.println(jedis.get("num"));

        //扩展命令
        //incr、decr
        jedis.incrBy("num", 5);
        System.out.println(jedis.get("num"));
        jedis.append("num","5");
        System.out.println(jedis.get("num"));

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        jedisPool.close();
    }
}
```

## 3、Hash的基本操作

```java
private static final String HOST = "localhost";
private static final int PORT = 6379;
private static final int DEFAULT_MAX_TOTAL = 30;
private static final int DEFAULT_MAX_IDLE = 10;
private JedisPool jedisPool;

@Before
public void initJedisPool() {
    //获取连接池的配置对象
    JedisPoolConfig config = new JedisPoolConfig();
    //设置最大的连接数
    config.setMaxTotal(DEFAULT_MAX_TOTAL);
    //设置最大空闲连接数
    config.setMaxIdle(DEFAULT_MAX_IDLE);
    //获取连接池
    jedisPool = new JedisPool(config, HOST, PORT);
}

@Test
public void testRedisHash() {
    try (Jedis jedis = jedisPool.getResource()) {
        //设置值hset(key, field, value)
        jedis.hset("myHash", "username", "jack");
        jedis.hset("myHash", "age", "18");
        System.out.println(jedis.hget("myHash", "username"));
        System.out.println(jedis.hget("myHash", "age"));

        /*
        一次设置多个字段
        hmset(final String key, final Map<String, String> hash)
        获取值
        hmget(final String key, final String... fields)
        获取全部值
        hgetAll(final String key)
        */
        Map<String, String> userMap = new HashMap<>(2);
        userMap.put("username", "张三");
        userMap.put("age", "20");
        jedis.hmset("myHash3", userMap);
        System.out.println(jedis.hmget("myHash3", "username", "age"));
        System.out.println(jedis.hgetAll("myHash3"));

        /*
        删除
        hdel(final String key, final String... fields)
        del(String key)
        */
        jedis.hdel("myHash3", "username", "age");
        System.out.println(jedis.hgetAll("myHash3"));
        jedis.del("myHash3");
        System.out.println(jedis.hgetAll("myHash3"));

        //判断key是否存在
        System.out.println(jedis.exists("asdih"));

        //获取hash的长度
        System.out.println(jedis.hlen("myHash"));

        //获取hash里面的字段名
        System.out.println(jedis.hkeys("myHash"));

        //获取hash里面的字段值
        System.out.println(jedis.hvals("myHash"));
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        jedisPool.close();
    }
}

```

## 4、List的基本操作

```java
private static final String HOST = "localhost";
private static final int PORT = 6379;
private static final int DEFAULT_MAX_TOTAL = 30;
private static final int DEFAULT_MAX_IDLE = 10;
private JedisPool jedisPool;

@Before
public void initJedisPool() {
    //获取连接池的配置对象
    JedisPoolConfig config = new JedisPoolConfig();
    //设置最大的连接数
    config.setMaxTotal(DEFAULT_MAX_TOTAL);
    //设置最大空闲连接数
    config.setMaxIdle(DEFAULT_MAX_IDLE);
    //获取连接池
    jedisPool = new JedisPool(config, HOST, PORT);
}
@Test
public void testRedisList() {
    try (Jedis jedis = jedisPool.getResource()) {
        /*
        两端添加
        public Long lpush(final String key, final String... strings);
        public Long rpush(final String key, final String... strings);
        */
        jedis.lpush("mylist", "a", "b", "c");
        jedis.lpush("mylist", "1", "2", "3");
        jedis.rpush("mylist2", "a", "b", "c");
        jedis.rpush("mylist2", "1", "2", "3");

        /*
        查看列表
        public List<String> lrange(final String key, final long start, final long end)
        */
        System.out.println(jedis.lrange("mylist", 0, 5));
        System.out.println(jedis.lrange("mylist", 0, -1));
        System.out.println(jedis.lrange("mylist", 0, -2));

        /*
        两端弹出
        public String lpop(final String key);
        public String rpop(final String key);
        */
        System.out.println(jedis.lpop("mylist"));
        System.out.println(jedis.lrange("mylist",0, -1));
        System.out.println(jedis.rpop("mylist2"));
        System.out.println(jedis.lrange("mylist2",0, -1));

        //获取list中元素的个数
        System.out.println(jedis.llen("mylist2"));

        /*
        删除list中元素
        public Long lrem(final String key, final long count, final String value);
        插入元素
        public Long linsert(final String key, final LIST_POSITION where,
        final String pivot,final String value);
        
        弹出元素加入到别的集合
        public String brpoplpush(String source, String destination, int timeout);
        */
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        jedisPool.close();
    }
}
```

## 5、set的基本操作

```java
@Test
public void testRedisSet() {
    try (Jedis jedis = jedisPool.getResource()) {

        //添加
        jedis.sadd("myset", "a", "b", "c");
        jedis.sadd("myset", "a");
        jedis.sadd("myset", "1", "2", "3");

        //删除某个元素
        jedis.srem("myset", "1", "2");

        //查看元素
        System.out.println(jedis.smembers("myset"));

        //判断有没有指定元素在set中
        System.out.println(jedis.sismember("myset", "a"));
        System.out.println(jedis.sismember("myset", "z"));

        //差集运算
        jedis.sadd("mya1", "a", "b", "c");
        jedis.sadd("myb1", "a", "c", "1", "2");
        System.out.println("差集运算:" + jedis.sdiff("mya1", "myb1"));

        //交集运算
        jedis.sadd("mya2", "a", "b", "c");
        jedis.sadd("myb2", "a", "c", "1", "2");
        System.out.println("交集运算:" + jedis.sinter("mya2", "myb2"));

        //并集运算
        jedis.sadd("mya3", "a", "b", "c");
        jedis.sadd("myb3", "a", "c", "1", "2");
        System.out.println("并集运算:" + jedis.sunion("mya3", "myb3"));

        //查看数量
        System.out.println("查看数量:" + jedis.scard("mya3"));

        //随机返回set中的成员
        System.out.println("随机成员:" + jedis.srandmember("mya3"));

        //将差集存到新的集合
        jedis.sdiffstore("my1", "mya1", "myb1");
        System.out.println(jedis.smembers("my1"));

        //交集并集同理

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        jedisPool.close();
    }
}
```

## 6、zortedSet的基本操作

```java
@Test
public void testRedisSortedSet() {
    try (Jedis jedis = jedisPool.getResource()) {
        //添加元素
        Map<String, Double> sortMap = new HashMap<>(3);
        sortMap.put("张三", 70.00);
        sortMap.put("李四", 80.00);
        sortMap.put("王五", 90.00);
        jedis.zadd("mysort", sortMap);

        jedis.zadd("mysort", 100.00, "张三");
        jedis.zadd("mysort", 60.00, "Tom");

        //获取元素
        System.out.println(jedis.zscore("mysort", "张三"));
        System.out.println(jedis.zcard("mysort"));

        //删除元素
        jedis.zrem("mysort", "Tom", "王五");
        System.out.println(jedis.zcard("mysort"));

        //范围查找
        jedis.zadd("mysort", 85, "Jack");
        jedis.zadd("mysort", 95, "rose");
        System.out.println(jedis.zrange("mysort", 0, -1));
        System.out.println((jedis.zrevrange("mysort", 0, -1)));
        System.out.println(jedis.zrangeWithScores("mysort", 0, -1));
        System.out.println(jedis.zrevrangeWithScores("mysort", 0, -1));

        //删除元素
        jedis.zremrangeByScore("mysort", 0, 4);
        sortMap.clear();
        sortMap.put("张三", 80.00);
        sortMap.put("李三", 90.00);
        sortMap.put("李四", 100.00);
        jedis.zadd("mysort", sortMap);
        jedis.zremrangeByScore("mysort", 80.00, 100.00);
        System.out.println(jedis.zrange("mysort", 0, -1));

        //扩展命令
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        jedisPool.close();
    }
}
```

## 7、keys的通用操作

```java
@Test
public void testRedisKeys() {
    try (Jedis jedis = jedisPool.getResource()) {
        //获取当前所有的key
        Set<String> keys = jedis.keys("*");
        System.out.println(keys);
        System.out.println(jedis.keys("my?"));
        System.out.println(jedis.keys("my*"));

        //删除key
        System.out.println(jedis.del("mya1", "mya2", "mya3"));

        //判断是否存在
        System.out.println(jedis.exists("mya1"));

        //重命名key
        System.out.println(jedis.rename("user2", "myuser1"));

        //设置过期时间
        System.out.println(jedis.expire("myuser1", 1000));

        //查看所剩的时间
        System.out.println(jedis.ttl("myuser1"));

        //查看key的类型
        System.out.println(jedis.type("myuser1"));
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        jedisPool.close();
    }
}
```

## 8、redis的特性

```java
@Test
public void testDatabase() {
    try (Jedis jedis = jedisPool.getResource()) {
        //默认的数据库是0
        System.out.println(jedis.keys("*"));
        //选择数据库
        jedis.select(1);
        System.out.println(jedis.keys("*"));

        //选择数据库
        jedis.select(0);
        //将myhash从0号数据库移动到1号数据库
        jedis.move("myhash", 1);
        jedis.select(1);
        System.out.println(jedis.keys("*"));

        jedis.set("username", "张三");
        System.out.println(jedis.get("username"));
        //事务

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        jedisPool.close();
    }

}
```

## 9、redis的持久化

### 9.1、RDB持久化

### 9.2、AOF持久化