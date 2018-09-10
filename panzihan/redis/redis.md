# Redis

## 1、Redis的安装

http://www.runoob.com/redis/redis-install.html

菜鸟教程的安装方法。

### 启动：

打开一个 **cmd** 窗口 使用cd命令切换目录到 **C:\redis** 运行 **redis-server.exe redis.windows.conf** 。

这时候另启一个cmd窗口，原来的不要关闭，不然就无法访问服务端了。

切换到redis目录下运行 **redis-cli.exe -h 127.0.0.1 -p 6379** 。

## 2、Redis 配置

Redis 的配置文件位于 Redis 安装目录下，文件名为 redis.conf。

你可以通过 **CONFIG** 命令查看或设置配置项。

### Redis CONFIG 命令格式：

```r
redis 127.0.0.1:6379> CONFIG GET CONFIG_SETTING_NAME
```

你可以通过修改 redis.conf 文件或使用 **CONFIG set** 命令来修改配置。

### **CONFIG SET** 命令格式：

```
redis 127.0.0.1:6379> CONFIG SET CONFIG_SETTING_NAME NEW_CONFIG_VALUE
```

### 参数说明

redis.conf 配置项说明如下：

1. Redis默认不是以守护进程的方式运行，可以通过该配置项修改，使用yes启用守护进程

    **daemonize no**

2. 当Redis以守护进程方式运行时，Redis默认会把pid写入/var/run/redis.pid文件，可以通过pidfile指定

    **pidfile /var/run/redis.pid**

3. 指定Redis监听端口，默认端口为6379，作者在自己的一篇博文中解释了为什么选用6379作为默认端口，因为6379在手机按键上MERZ对应的号码，而MERZ取自意大利歌女Alessia Merz的名字

    **port 6379**

4. 绑定的主机地址

    **bind 127.0.0.1**

5. 当客户端闲置多长时间后关闭连接，如果指定为0，表示关闭该功能

    **timeout 300**

6. 指定日志记录级别，Redis总共支持四个级别：debug、verbose、notice、warning，默认为verbose

    **loglevel verbose**

7. 日志记录方式，默认为标准输出，如果配置Redis为守护进程方式运行，而这里又配置为日志记录方式为标准输出，则日志将会发送给/dev/null

    **logfile stdout**

8. 设置数据库的数量，默认数据库为0，可以使用SELECT <dbid>命令在连接上指定数据库id

    **databases 16**

9. 指定在多长时间内，有多少次更新操作，就将数据同步到数据文件，可以多个条件配合

    **save <seconds> <changes>**

    Redis默认配置文件中提供了三个条件：

    **save 900 1**

    **save 300 10**

    **save 60 10000**

    分别表示900秒（15分钟）内有1个更改，300秒（5分钟）内有10个更改以及60秒内有10000个更改。

10. 指定存储至本地数据库时是否压缩数据，默认为yes，Redis采用LZF压缩，如果为了节省CPU时间，可以关闭该选项，但会导致数据库文件变的巨大

    **rdbcompression yes**

11. 指定本地数据库文件名，默认值为dump.rdb

    **dbfilename dump.rdb**

12. 指定本地数据库存放目录

    **dir ./**

13. 设置当本机为slav服务时，设置master服务的IP地址及端口，在Redis启动时，它会自动从master进行数据同步

    **slaveof <masterip> <masterport>**

14. 当master服务设置了密码保护时，slav服务连接master的密码

    **masterauth <master-password>**

15. 设置Redis连接密码，如果配置了连接密码，客户端在连接Redis时需要通过AUTH <password>命令提供密码，默认关闭

    **requirepass foobared**

16. 设置同一时间最大客户端连接数，默认无限制，Redis可以同时打开的客户端连接数为Redis进程可以打开的最大文件描述符数，如果设置 maxclients 0，表示不作限制。当客户端连接数到达限制时，Redis会关闭新的连接并向客户端返回max number of clients reached错误信息

    **maxclients 128**

17. 指定Redis最大内存限制，Redis在启动时会把数据加载到内存中，达到最大内存后，Redis会先尝试清除已到期或即将到期的Key，当此方法处理 后，仍然到达最大内存设置，将无法再进行写入操作，但仍然可以进行读取操作。Redis新的vm机制，会把Key存放内存，Value会存放在swap区

    **maxmemory <bytes>**

18. 指定是否在每次更新操作后进行日志记录，Redis在默认情况下是异步的把数据写入磁盘，如果不开启，可能会在断电时导致一段时间内的数据丢失。因为 redis本身同步数据文件是按上面save条件来同步的，所以有的数据会在一段时间内只存在于内存中。默认为no

    **appendonly no**

19. 指定更新日志文件名，默认为appendonly.aof

     **appendfilename appendonly.aof**

20. 指定更新日志条件，共有3个可选值： 
        **no**：表示等操作系统进行数据缓存同步到磁盘（快） 
        **always**：表示每次更新操作后手动调用fsync()将数据写到磁盘（慢，安全） 
        **everysec**：表示每秒同步一次（折衷，默认值）

    **appendfsync everysec**

21. 指定是否启用虚拟内存机制，默认值为no，简单的介绍一下，VM机制将数据分页存放，由Redis将访问量较少的页即冷数据swap到磁盘上，访问多的页面由磁盘自动换出到内存中（在后面的文章我会仔细分析Redis的VM机制）

     **vm-enabled no**

22. 虚拟内存文件路径，默认值为/tmp/redis.swap，不可多个Redis实例共享

     **vm-swap-file /tmp/redis.swap**

23. 将所有大于vm-max-memory的数据存入虚拟内存,无论vm-max-memory设置多小,所有索引数据都是内存存储的(Redis的索引数据 就是keys),也就是说,当vm-max-memory设置为0的时候,其实是所有value都存在于磁盘。默认值为0

     **vm-max-memory 0**

24. Redis swap文件分成了很多的page，一个对象可以保存在多个page上面，但一个page上不能被多个对象共享，vm-page-size是要根据存储的 数据大小来设定的，作者建议如果存储很多小对象，page大小最好设置为32或者64bytes；如果存储很大大对象，则可以使用更大的page，如果不 确定，就使用默认值

     **vm-page-size 32**

25. 设置swap文件中的page数量，由于页表（一种表示页面空闲或使用的bitmap）是在放在内存中的，，在磁盘上每8个pages将消耗1byte的内存。

     **vm-pages 134217728**

26. 设置访问swap文件的线程数,最好不要超过机器的核数,如果设置为0,那么所有对swap文件的操作都是串行的，可能会造成比较长时间的延迟。默认值为4

     **vm-max-threads 4**

27. 设置在向客户端应答时，是否把较小的包合并为一个包发送，默认为开启

    **glueoutputbuf yes**

28. 指定在超过一定的数量或者最大的元素超过某一临界值时，采用一种特殊的哈希算法

    **hash-max-zipmap-entries 64**

    **hash-max-zipmap-value 512**

29. 指定是否激活重置哈希，默认为开启（后面在介绍Redis的哈希算法时具体介绍）

    **activerehashing yes**

30. 指定包含其它的配置文件，可以在同一主机上多个Redis实例之间使用同一份配置文件，而同时各个实例又拥有自己的特定配置文件

    **include /path/to/local.conf**

## 3、Redis 数据类型

Redis支持五种数据类型：string（字符串），hash（哈希），list（列表），set（集合）及zset(sorted set：有序集合)。

### String（字符串）

string 是 redis 最基本的类型，你可以理解成与 Memcached 一模一样的类型，一个 key 对应一个 value。

string 类型是二进制安全的。意思是 redis 的 string 可以包含任何数据。比如jpg图片或者序列化的对象。

string 类型是 Redis 最基本的数据类型，string 类型的值最大能存储 512MB。

实例:

```cmd
127.0.0.1:6379> set name "topview"
OK
127.0.0.1:6379> get name
"topview"
```

**注意：**一个键最大能存储512MB。

### Hash（哈希）

Redis hash 是一个键值(key=>value)对集合。

Redis hash 是一个 string 类型的 field 和 value 的映射表，hash 特别适合用于存储对象。





实例:

```cmd
127.0.0.1:6379> hmset user id 123 name Tom
OK
127.0.0.1:6379> hget user id
"123"
127.0.0.1:6379> hget user name
"Tom"
```

实例中我们使用了 Redis **HMSET, HGET** 命令，**HMSET** 设置了两个 field=>value 对, HGET 获取对应 **field** 对应的 **value**。

### List（列表）

Redis 列表是简单的字符串列表，按照插入顺序排序。你可以添加一个元素到列表的头部（左边）或者尾部（右边）。

实例:

```cmd
127.0.0.1:6379> lpush users user1
(integer) 1
127.0.0.1:6379> lpush users user2
(integer) 2
127.0.0.1:6379> lpush users user3
(integer) 3
127.0.0.1:6379> lrange users 0 10
1) "user3"
2) "user2"
3) "user1"
```

列表最多可存储 (2^32 - 1) 元素 (4294967295, 每个列表可存储40多亿)。

### Set（集合）

Redis的Set是string类型的无序集合。

集合是通过哈希表实现的，所以添加，删除，查找的复杂度都是O(1)。

添加一个 string 元素到 key 对应的 set 集合中，成功返回1，如果元素已经在集合中返回 0，如果 key 对应的 set 不存在则返回错误。

实例：

```cmd
127.0.0.1:6379> sadd myset user1
(integer) 1
127.0.0.1:6379> sadd myset user2
(integer) 1
127.0.0.1:6379> sadd myset user3
(integer) 1
127.0.0.1:6379> sadd myset user3
(integer) 0
127.0.0.1:6379> smembers myset
1) "user3"
2) "user2"
3) "user1"
4) "redis"
```

注意：以上实例中user3被添加了两次，但根据集合内元素的唯一性，第二次插入的元素将被忽略。

### zset(sorted set：有序集合)

Redis zset 和 set 一样也是string类型元素的集合,且不允许重复的成员。

不同的是每个元素都会关联一个double类型的分数。redis正是通过分数来为集合中的成员进行从小到大的排序。

zset的成员是唯一的,但分数(score)却可以重复。

* zadd 命令：

添加元素到集合，元素在集合中存在则更新对应score

```cmd
zadd key score member 
```

实例：

```cmd
127.0.0.1:6379> zadd zset 0 redis
(integer) 1
127.0.0.1:6379> zadd zset 0 mongodb
(integer) 1
127.0.0.1:6379> zadd zset 0 rabitmq
(integer) 1
127.0.0.1:6379> zadd zset 0 rabitmq
(integer) 0
127.0.0.1:6379> zrangebyscore zset 0 1000
1) "mongodb"
2) "rabitmq"
3) "redis"
```

## 4、Redis 命令

Redis 命令用于在 redis 服务上执行操作。

要在 redis 服务上执行命令需要一个 redis 客户端。Redis 客户端在我们之前下载的的 redis 的安装包中。

### 1、Redis 客户端的基本语法

```cmd
$ redis-cli
```

实例：以下实例讲解了如何启动 redis 客户端：

```cmd
$redis-cli
redis 127.0.0.1:6379>
redis 127.0.0.1:6379> PING
PONG
```

在以上实例中我们连接到本地的 redis 服务并执行 **PING** 命令，该命令用于检测 redis 服务是否启动。

### 2、在远程服务上执行命令

如果需要在远程 redis 服务上执行命令，同样我们使用的也是 **redis-cli** 命令。

语法：

```cmd
$ redis-cli -h host -p port -a password
```

实例：

以下实例演示了如何连接到主机为 127.0.0.1，端口为 6379 ，密码为 mypass 的 redis 服务上。

```cmd
$redis-cli -h 127.0.0.1 -p 6379 -a "mypass"
redis 127.0.0.1:6379>
redis 127.0.0.1:6379> PING
PONG
```

## 5、Redis 键(key)

Redis 键命令用于管理 redis 的键。

Redis 键命令的基本语法如下：

```cmd
redis 127.0.0.1:6379> COMMAND KEY_NAME
```

实例：

```cmd
127.0.0.1:6379> set runoobkey redis
OK
127.0.0.1:6379> del runoobkey
(integer) 1
```

在以上实例中 **DEL** 是一个命令， **runoobkey** 是一个键。 如果键被删除成功，命令执行后输出 **(integer) 1**，否则将输出 **(integer) 0**

### Redis key

| 序号 | 命令                                 | 描述1                                                        |
| ---- | ------------------------------------ | ------------------------------------------------------------ |
| 1    | DEL key                              | 该命令用于在key存在时删除key。                               |
| 2    | DUMP key                             | 序列化给定key，并返回被序列化的值。                          |
| 3    | EXISTS key                           | 检查给定的key是否存在                                        |
| 4    | EXPIRE key  seconds                  | 为给定key设置过期时间                                        |
| 5    | EXPIREAT key timestamp               | EXPIREAT 的作用和 EXPIRE 类似，都用于为 key 设置过期时间。 不同在于 EXPIREAT 命令接受的时间参数是 UNIX 时间戳(unix timestamp)。 |
| 6    | PEXPIRE key milliseconds             | 设置key的过期时间以毫秒计                                    |
| 7    | PEXPIREAT key milliseconds-timestamp | 设置 key 过期时间的时间戳(unix timestamp) 以毫秒计           |
| 8    | KEYS pattern                         | 查找所有符合给定模式（pattern）的key                         |
| 9    | MOVE key db                          | 将当前数据库的key移动到给定的数据库                          |
| 10   | PERSIST key                          | 移除key的过期时间，key将持久保持                             |
| 11   | PTTL key                             | 以毫秒为单位返回key的剩余的过期时间                          |
| 12   | TTL key                              | 以秒为单位，返回给定key的剩余生存时间                        |
| 13   | RANDOMKEY                            | 从当前数据库中随机返回一个key                                |
| 14   | RENAME key newkey                    | 修改key的名称                                                |
| 15   | RENAMENX key newkey                  | 仅当 newkey 不存在时，将 key 改名为 newkey 。                |
| 16   | TYPE key                             | 返回key所存储的值的类型                                      |

## 6、Redis 字符串(String)

Redis 字符串数据类型的相关命令用于管理 redis 字符串值，基本语法如下：

### 语法

```cmd
redis 127.0.0.1:6379> COMMAND KEY_NAME
```

### 实例

```
redis 127.0.0.1:6379> SET runoobkey redis
OK
redis 127.0.0.1:6379> GET runoobkey
"redis"
```

在以上实例中我们使用了 **SET** 和 **GET** 命令，键为 **runoobkey**。

### Redis 字符串命令

下表列出了常用的 redis 字符串命令：

| 序号 | 命令及描述                                                   |
| ---- | ------------------------------------------------------------ |
| 1    | [SET key value](http://www.runoob.com/redis/strings-set.html)  设置指定 key 的值 |
| 2    | [GET key](http://www.runoob.com/redis/strings-get.html)  获取指定 key 的值。 |
| 3    | [GETRANGE key start end](http://www.runoob.com/redis/strings-getrange.html)  返回 key 中字符串值的子字符 |
| 4    | [GETSET key value](http://www.runoob.com/redis/strings-getset.html) 将给定 key 的值设为 value ，并返回 key 的旧值(old value)。 |
| 5    | [GETBIT key offset](http://www.runoob.com/redis/strings-getbit.html) 对 key 所储存的字符串值，获取指定偏移量上的位(bit)。 |
| 6    | [MGET key1 [key2..\]](http://www.runoob.com/redis/strings-mget.html) 获取所有(一个或多个)给定 key 的值。 |
| 7    | [SETBIT key offset value](http://www.runoob.com/redis/strings-setbit.html) 对 key 所储存的字符串值，设置或清除指定偏移量上的位(bit)。 |
| 8    | [SETEX key seconds value](http://www.runoob.com/redis/strings-setex.html) 将值 value 关联到 key ，并将 key 的过期时间设为 seconds (以秒为单位)。 |
| 9    | [SETNX key value](http://www.runoob.com/redis/strings-setnx.html) 只有在 key 不存在时设置 key 的值。 |
| 10   | [SETRANGE key offset value](http://www.runoob.com/redis/strings-setrange.html) 用 value 参数覆写给定 key 所储存的字符串值，从偏移量 offset 开始。 |
| 11   | [STRLEN key](http://www.runoob.com/redis/strings-strlen.html) 返回 key 所储存的字符串值的长度。 |
| 12   | [MSET key value [key value ...\]](http://www.runoob.com/redis/strings-mset.html) 同时设置一个或多个 key-value 对。 |
| 13   | [MSETNX key value [key value ...\]](http://www.runoob.com/redis/strings-msetnx.html)  同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在。 |
| 14   | [PSETEX key milliseconds value](http://www.runoob.com/redis/strings-psetex.html) 这个命令和 SETEX 命令相似，但它以毫秒为单位设置 key 的生存时间，而不是像 SETEX 命令那样，以秒为单位。 |
| 15   | [INCR key](http://www.runoob.com/redis/strings-incr.html) 将 key 中储存的数字值增一。 |
| 16   | [INCRBY key increment](http://www.runoob.com/redis/strings-incrby.html) 将 key 所储存的值加上给定的增量值（increment） 。 |
| 17   | [INCRBYFLOAT key increment](http://www.runoob.com/redis/strings-incrbyfloat.html) 将 key 所储存的值加上给定的浮点增量值（increment） 。 |
| 18   | [DECR key](http://www.runoob.com/redis/strings-decr.html) 将 key 中储存的数字值减一。 |
| 19   | [DECRBY key decrement](http://www.runoob.com/redis/strings-decrby.html) key 所储存的值减去给定的减量值（decrement） 。 |
| 20   | [APPEND key value](http://www.runoob.com/redis/strings-append.html) 如果 key 已经存在并且是一个字符串， APPEND 命令将指定的 value 追加到该 key 原来值（value）的末尾。 |

## 7、Redis 哈希(Hash)

Redis hash 是一个string类型的field和value的映射表，hash特别适合用于存储对象。

Redis 中每个 hash 可以存储 232 - 1 键值对（40多亿）。

### 实例

```cmd
127.0.0.1:6379>  HMSET runoobkey name "redis tutorial" description "redis basic commands for caching" likes 20 visitors 23000
OK
127.0.0.1:6379>  HGETALL runoobkey
1) "name"
2) "redis tutorial"
3) "description"
4) "redis basic commands for caching"
5) "likes"
6) "20"
7) "visitors"
8) "23000"
```

在以上实例中，我们设置了 redis 的一些描述信息(name, description, likes, visitors) 到哈希表的 **runoobkey** 中。

### Redis hash 命令

| 序号 | 命令                                          | 描述                                                  |
| ---- | --------------------------------------------- | ----------------------------------------------------- |
| 1    | HDEL key field [field]                        | 删除一个或多个哈希表字段                              |
| 2    | HEXISTS key field                             | 查看哈希表key中，指定的字段是否存在                   |
| 3    | HGET key field                                | 获取存储在哈希表中指定字段的值                        |
| 4    | HGETALL key                                   | 获取在哈希表中指定key的所有字段和值                   |
| 5    | HINCRBY key field increment                   | 为哈希表中key的指定字段的整数加上增量 increment 。    |
| 6    | HINCREBFLOAT key field increment              | 为哈希表key中的指定字段的浮点数加上增量increment      |
| 7    | HKEYS key                                     | 获取所有哈希表中的字段                                |
| 8    | HLEN key                                      | 获取哈希表中字段的数量                                |
| 9    | HMGET key field [field]                       | 获取所有给定字段的值                                  |
| 10   | HMSET key field1 value1 [field2 value2]       | 同时将多个 field-value (域-值)对设置到哈希表 key 中。 |
| 11   | HSET key field value                          | 将哈希表key中的字段field的值设为value                 |
| 12   | HSETNX key field value                        | 只有在字段 field 不存在时，设置哈希表字段的值。       |
| 13   | HVALS key                                     | 获取哈希表中所有值                                    |
| 14   | HSCAN key cursor [MATCH pattern][COUNT count] | 迭代哈希表中的键值对。                                |

## 8、Redis 列表(List)

Redis列表是简单的字符串列表，按照插入顺序排序。你可以添加一个元素到列表的头部（左边）或者尾部（右边）

一个列表最多可以包含 2^32 - 1 个元素 (4294967295, 每个列表超过40亿个元素)。

### 实例

```cmd
redis 127.0.0.1:6379> LPUSH runoobkey redis
(integer) 1
redis 127.0.0.1:6379> LPUSH runoobkey mongodb
(integer) 2
redis 127.0.0.1:6379> LPUSH runoobkey mysql
(integer) 3
redis 127.0.0.1:6379> LRANGE runoobkey 0 10

1) "mysql"
2) "mongodb"
3) "redis"
```

在以上实例中我们使用了 **LPUSH** 将三个值插入了名为 **runoobkey** 的列表当中。

### Redis 列表命令

| 序号 | 命令                                  | 描述                                                         |
| ---- | ------------------------------------- | ------------------------------------------------------------ |
| 1    | BLPOP key1 [key2] timeout             | 移出并获取列表的第一个元素，如果列表没有元素会阻塞列表直到等待时间超时或发现可弹出元素为止。 |
| 2    | BRPOP key [key2] timeout              | 移出并获取列表的最后一个元素，如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止 |
| 3    | BRPOPLPUSH source destination timeout | 从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它； 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。 |
| 4    | LINDEX key index                      | 通过索引获取列表中的元素                                     |
| 5    | LINSERT key BEFORE\|AFTER pivot value | 在列表的元素前或者后插入元素                                 |
| 6    | LLEN key                              | 获取列表长度                                                 |
| 7    | LPOP key                              | 移出并获取列表的第一个元素                                   |
| 8    | LPUSH key value1 [value2]             | 将一个或多个值插入到列表头部                                 |
| 9    | LPUSHX key value                      | 将一个值插入到已存在的列表头部                               |
| 10   | LRANGE key start stop                 | 获取列表指定范围内的元素                                     |
| 11   | LREM key count value                  | 移除列表元素                                                 |
| 12   | LSET key index value                  | 通过索引设置列表元素的值                                     |
| 13   | LTRIM key start stop                  | 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。 |
| 14   | RPOP key                              | 移除并获取列表最后一个元素                                   |
| 15   | RPOPLPUSH source destination          | 移除列表的最后一个元素，并将该元素添加到另一个列表并返回     |
| 16   | RPUSH key value1 [value2]             | 在列表中添加一个或多个值                                     |
| 17   | RPUSHX key value                      | 为已存在的列表添加值                                         |

