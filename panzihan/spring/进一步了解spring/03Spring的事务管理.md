# Spring的事务管理

## 1、数据库事务

### 1.1、何为数据库事务

	数据库事务有严格的定义，它必须同时满足4个条件：原子性（Atomic）、一致性（Consistency）、隔离性（Isolation）和持久性（Durabiliy），简称为ACID。

- 原子性（Atomicity）：事务是一个原子操作，由一系列动作组成。事务的原子性确保动作要么全部完成，要么完全不起作用。
- 一致性（Consistency）：一旦事务完成（不管成功还是失败），系统必须确保它所建模的业务处于一致的状态，而不会是部分完成部分失败。在现实中的数据不应该被破坏。
- 隔离性（Isolation）：可能有许多事务会同时处理相同的数据，因此每个事务都应该与其他事务隔离开来，防止数据损坏。
- 持久性（Durability）：一旦事务完成，无论发生什么系统错误，它的结果都不应该受到影响，这样就能从任何系统崩溃中恢复过来。通常情况下，事务的结果被写到持久化存储器中。

### 1.2、数据并发问题

	一个数据库可能有多个访问客户端，这些客户端都可用并发的方式访问数据库。数据库中的相同数据可能同时被多个是事务访问，如果没有采取必要的隔离措施，就会导致各种并发的问题，破坏数据的完整性。这些问题可以归结为5类，包括3类数据读取问题（脏读、不可重复读和幻象读）及2类数据更新问题（第一类丢失更新和第二类丢失更新）。

1. 脏读：A事务读取B事务尚未提交更改数据，并在这个数据的基础上进行操作。
2. 不可重复读：指A事务读取了B事务已经提交的更改数据。
3. 幻象读：A事务读取B事务提交的新增数据，这时A事务将出现幻象读的问题。

幻象读和不可重复读是两个容易混淆的概念，前者是指读到了其他已经提交事务的新增数据，而后者是指读到了已经提交事务的更改数据（更改或者删除）。为了避免这两种情况，采取的策略是不同的，防止读到更改数据，只需对操作的数据添加行级锁，阻止操作中的数据发生变化；而防止读到新增数据，则往往需要添加表级锁——将整张表锁定。

4. 第一类丢失更新：A事务撤销的时候，把已经提交的B事务的更改数据覆盖了。
5. 第二类丢失更新：A事务覆盖B事务已经提交的数据。

### 1.3、数据库锁机制

按锁定的对象不同，一般可分为行级锁和表级锁。前者对整张表进行锁定，而后者对表中的特定行进行锁定。从并发事务锁定的关系上看，可以分了共享锁定和独占锁定。共享锁定会防止独占锁定，但允许其他共享锁定。而独占锁定既防止其他的独占锁定，也防止其他的共享锁定。为了更改数据，数据库必须在进行更改的行上施加行独占锁定，INSERT、UPDATE、DELETE和SELECT FOR UPDATE语句都会隐式采用必要的行锁定。

1. 行共享锁定：一般通过SELECT FOR UPDATE语句会隐式获得行共享锁定，在Oracle中用户也可以通过LOCK TABLE IN ROW SHARE MODE语句显示获得行共享锁定。行共享锁定并不防止对数据行进行更改操作，但是可以防止其他会话获取独占性数据表锁定。允许进行多个并发的行共享和行独占锁定，还允许进行数据表的共享或者采用共享行独占锁定。
2. 行独占锁定：通过一条INSERT、UPDATE或DELETE语句隐式获取，或者通过一条LOCK TABLE IN ROW EXCLUSIVE MODE语句显示获取。这种锁定可以防止其他会话获取一个共享锁定、共享行独占锁定或独占锁定。
3. 表共享锁定：通过LOCK TABLE IN SHARE MODE语句显示获得。这种锁定可防止其他会话获取行独占锁定（INSERT、UPDATE或DELETE），或者防止其他表共享行独占锁定或表独占锁定，但他允许在表中拥有多个行共享和表共享锁定。该锁定可以让会话具有对表事务级一致性访问，因为其他会话在用户提交或者回滚该事务并释放对改表的锁定之前不能更改这张被锁定的表。
4. 表共享行独占锁定：通过LOCK TABLE IN SHARE ROW EXCLUSIVE MODE语句显示获得。这种锁定可以防止其他会话获取一个表共享、行独占或者表独占锁定，但允许其他行共享锁定。这种锁定类似于表共享锁定，只是一次只能对一张表放置一个表共享行独占锁定。如果A会话拥有该锁定，则B会话可以执行SELECT FOR UPDATE操作；但如果B会话试图更新选择的行，则需要等待。
5. 表独占锁定：通过LOCK TABLE IN EXCLUSIVE MODE语句显示获得。这种锁定可以防止其他会话对改表的任何其他锁定。

### 1.4、事务隔离级别

| 隔离级别        | 脏读   | 不可重复读 | 幻象读 | 第一类丢失更新 | 第二类丢失更新 |
| --------------- | ------ | ---------- | ------ | -------------- | -------------- |
| READ UNCOMMITED | 允许   | 允许       | 允许   | 不允许         | 允许           |
| READ COMMITED   | 不允许 | 允许       | 允许   | 不允许         | 允许           |
| REPEATABLE READ | 不允许 | 不允许     | 允许   | 不允许         | 不允许         |
| SERIALIZABLE    | 不允许 | 不允许     | 不允许 | 不允许         | 不允许         |

### 1.5、JDBC对事务的支持

Connection默认情况下是自动提交的，即每条执行的sql语句都对应一个事务。

```java
Connection connection;
try {
    //获取数据连接
    connection = DriverManager.getConnection("");
    //关闭自动提交机制
    connection.setAutoCommit(false);
    //设置事务的隔离级别
    connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
    //...
    //提交事务
    connection.commit();        
} catch (SQLException e) {
    //...
    //回滚事务    
} finally {
    //...
}
```

JDBC还允许设置事务保存点，回滚到特定的保存点。

```java
Savepoint savePoint1 = connection.setSavepoint("savePoint1");
connection.rollback(savePoint1);
```

## 2、ThreadLocal基础知识

### 2.1、ThreadLocal是什么

ThreadLocal是一个保存线程本地化对象的容器。当运行与多线程环境的某个对象使用ThreadLocal维护变量的时候，ThreadLocal为每个使用该变量的线程分配一个独立的变量副本。所以每个线程都可以独立改变自己的变量副本，而不会影响其他线程所对应的副本。从线程的角度看，这个变量就像线程专有的本地变量。

### 2.2、ThreadLocal的接口方法

```java
public interface ThreadLocal {
	void set(Object value);//设置当前线程的线程局部变量的值。
    public Object get();//返回当前线程所对应的线程局部变量。
    public void remove();//将当前线程的局部变量删除。
    protected Object initialValue();//返回该线程局部变量的初始值。
}
```

ThreadLocal是如何做到为每个线程维护一份独立的变量副本呢？其实现的思路很简单：在ThreadLocal类中有一个Map，用于存储每个线程的变量副本，Map中元素的键为线程对象，值为对应线程的变量副本。下面提供一个简单的实现版本：

```java
package com.smart.simple;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SimpleThreadLocal {

    private Map<Object, Object> valueMap = Collections.synchronizedMap(new HashMap<>());

    public void set(Object newValue) {
        //键为线程对象,值为本线程的变量副本
        valueMap.put(Thread.currentThread(), newValue);
    }

    public Object get() {
        Thread thread = Thread.currentThread();
        //返回本线程对应的变量
        Object object = valueMap.get(thread);
        if (object == null && !valueMap.containsKey(thread)) {
            object = initialValue();
            valueMap.put(thread, object);
        }
        return object;
    }

    public void remove() {
        valueMap.remove(Thread.currentThread());
    }

    public Object initialValue() {
        return null;
    }

}
```

### 2.3、一个ThreadLoca实例

```java
package com.smart.simple;

public class SequenceNumber {

    /**
     * 通过匿名内部类覆盖ThreadLocal的initialValue方法,指定初始值
     */
    private static ThreadLocal<Integer> seqNum = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    /**
     * 获取下一个系列值
     */
    public int getNextNum() {
        seqNum.set(seqNum.get() + 1);
        return seqNum.get();
    }

    private static class TestClient extends Thread {
        private SequenceNumber sn;
        public TestClient(SequenceNumber sn) {
            this.sn = sn;
        }
        @Override
        public void run() {
            for (int i = 0; i < 3; i++) {
                System.out.println("Thread[" + Thread.currentThread().getName() + "] sn[" + sn.getNextNum() + "]");
            }
        }
    }

    public static void main(String[] args) {
        SequenceNumber sn = new SequenceNumber();
        TestClient testClient1 = new TestClient(sn);
        TestClient testClient2 = new TestClient(sn);
        TestClient testClient3 = new TestClient(sn);
        testClient1.start();
        testClient2.start();
        testClient3.start();
    }
}
```

输出结果：

```
Thread[Thread-0] sn[1]
Thread[Thread-1] sn[1]
Thread[Thread-2] sn[1]
Thread[Thread-2] sn[2]
Thread[Thread-2] sn[3]
Thread[Thread-0] sn[2]
Thread[Thread-1] sn[2]
Thread[Thread-1] sn[3]
Thread[Thread-0] sn[3]
```

考查输出的结果信息，发现每个线程所产生的序号虽然都共享同一个SequenceNumber实例，但他们并没有相互干扰，而是各自产生独立的序列号，这是因为通过ThreadLocal为每个线程提供了单独了副本。

### 2.4、与Thread同步机制的比较

在同步机制中，通过对象的锁机制保证同一时间只有一个线程访问变量。这时该变量是多个线程共享的，使用同步机制要求程序缜密的分析什么时候对变量进行读/写，什么时候需要锁定某个对象，什么时候释放对象锁等繁杂的问题，程序设计和编写难度相对较大。

而ThreadLocal从另一个角度来解决多线程的并发访问。ThreadLocal为每个线程提供了一个独立的变量副本，从而隔离了多个线程对访问数据的冲突。因为每个线程都拥有自己的变量副本，因而也就没必要对该变量进行同步。ThreadLocal提供了线程安全的对象封装，在编写多线程代码时，可以把不安全的变量封装进ThreadLocal。

概括而言，对于多线程资源共享的问题，同步机制采用了“以时间换空间”的方式：访问串行化，对象共享化；而ThreadLocal采用了“以空间换时间”的方式：访问并行化，对象独享化。前者提供一份变量，让不同的线程排队访问；而后者为每个线程都提供了一份变量，因此可以同时访问而互不影响。

## 3、Spring对事务管理的支持

### 3.1、事务管理关键抽象

1、TransactionDefinition用于描述事务的隔离级别、超时时间、是否为只读事务和事务的传播规则。

```java
public interface TransactionDefinition {
//事务隔离：当前事务和其他事务的隔离程度
	int ISOLATION_DEFAULT = -1;
	int ISOLATION_READ_UNCOMMITTED = Connection.TRANSACTION_READ_UNCOMMITTED;
	int ISOLATION_READ_COMMITTED = Connection.TRANSACTION_READ_COMMITTED;
	int ISOLATION_REPEATABLE_READ = Connection.TRANSACTION_REPEATABLE_READ;
	int ISOLATION_SERIALIZABLE = Connection.TRANSACTION_SERIALIZABLE;
//事务传播：通常一个事务中执行的所有代码都会运行与同一个事务的上下文中。但Spring也提供几种可选的事务传播类型
    int PROPAGATION_REQUIRED = 0;
	int PROPAGATION_SUPPORTS = 1;
	int PROPAGATION_MANDATORY = 2;
	int PROPAGATION_REQUIRES_NEW = 3;
	int PROPAGATION_NOT_SUPPORTED = 4;
	int PROPAGATION_NEVER = 5;
	int PROPAGATION_NESTED = 6;
//事务超时
	int TIMEOUT_DEFAULT = -1;
//只读状态
    boolean isReadOnly();
}
```

2、TransactionStatus代表一个事务的具体运行状态。事务管理器可以通过该接口获取事务运行期的状态信息，也可以通过该接口间接地回滚事务。该接口继承与SavepointManager接口。

```java
public interface SavepointManager {
    /**
     * 创建一个保存点对象，以便在后面可以利用rollbackToSavepoint(Object savepoint)回滚到特定的
     * 保存点上，也可以通过releaseSavepoint(Object savepoint)释放一个已经不用的保存点
     */
    Object createSavepoint() throws TransactionException;
	//将事务回滚到特定的保存点上，被回滚的保存点自动释放
    void rollbackToSavepoint(Object savepoint) throws TransactionException;
	//释放一个保存点。如果事务提交，则所有的保存点都会被释放，无需手工清除
    void releaseSavepoint(Object savepoint) throws TransactionException;
}
```

TransactionStatus扩展了SavepointManager接口

```java
public interface TransactionStatus extends SavepointManager, Flushable {
	//判断当前事务是否是一个新的事务，如果返回false，则表示当前事务是一个已经存在的事务，或者当前操作未运行在事务环境中
    boolean isNewTransaction();
	//判断当前事务是否在内部创建了一个保存点，该保存点是为了支持Spring的嵌套事务创建的
    boolean hasSavepoint();
	//将当前事务设置为rollback-only。通过该标识通知事务管理器只能将事务回滚，事务管理器将通过显示调用回滚命令或抛出异常的方式来回滚事务
    void setRollbackOnly();
	//判断当前事务是否已经被标识为rollback-only
    boolean isRollbackOnly();
	//判断当前事务是否已经结束（已经提交或者回滚）
    boolean isCompleted();
}
```

3、PlatformTransactionManger

```java
public interface PlatformTransactionManager {
    //该方法根据事务定义信息从事务环境中返回一个已存在的事务，或者创建一个新的事务，并用
    //TransactionStatus描述这个事务的状态
    	TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException;
	//根据事务的状态提交事务。如果事务状态已经被标识为rollback-only，则该方法将执行一个回滚事务
 	void commit(TransactionStatus status) throws TransactionException;
	//将事务回滚。当commit()方法抛出异常的时候，rollback（）方法会被隐式调用
    void rollback(TransactionStatus status) throws TransactionException;
}
```

### 3.2、spring事务管理器的实现类

1、SpringJDBC和Mybaits

代码清单 基于数据源的事务管理器：

```xml
<!--配置一个数据源-->
<bean id="dataSource"
      class="org.apache.commons.dbcp.BasicDataSource"
      destroy-method="close"
      p:driverClassName="${jdbc.driverClassName}"
      p:url="${jdbc.url}"
      p:username="${jdbc.username}"
      p:password="${jdbc.password}"/>
<!--基于数据源的事务管理器-->
<bean id="txManager"
          class="org.springframework.jdbc.datasource.DataSourceTransactionManager"
          p:dataSource-ref="dataSource"/><!--引用数据源-->
```

### 3.3、事务同步管理器

TransactionSynchronizationManager

```java
public abstract class TransactionSynchronizationManager {
	//用于保存每个事务线程对应的Connection或Session等类型的资源
    private static final ThreadLocal<Map<Object, Object>> resources =
         new NamedThreadLocal<Map<Object, Object>>("Transactional resources");

    //用于保存每个事务线程对应事务的名称
   	private static final ThreadLocal<String> currentTransactionName =
         new NamedThreadLocal<String>("Current transaction name");

    //用于保存每个事务线程对应事务的read-only状态
   	private static final ThreadLocal<Boolean> currentTransactionReadOnly =
         new NamedThreadLocal<Boolean>("Current transaction read-only status");

    //用于保存每个事务线程对应事务的隔离级别
   	private static final ThreadLocal<Integer> currentTransactionIsolationLevel =
         new NamedThreadLocal<Integer>("Current transaction isolation level");
	
    //用于保存每个事务线程对应事务的激活状态
   	private static final ThreadLocal<Boolean> actualTransactionActive =
         new NamedThreadLocal<Boolean>("Actual transaction active");
}
```

TransactionSynchronizationManager将DAO、Service中影响线程安全的所有“状态”统一抽取到该类中，并用ThreadLocal进行替换，从此DAO（必须是基于模板类或资源获取工具类创建的DAO）和Service（必须采用Spring事务管理机制）摘掉了非线程安全的帽子，完成了脱胎换骨的身份转变。

### 3.4、事务传播行为

Spring在TransactionDefinition接口中规定了7种类型的事务传播行为。

| 事务传播行为类型          | 说明                                                         |
| ------------------------- | ------------------------------------------------------------ |
| PROPAGATION_REQUIRED      | 如果当前没有事务，则新建一个事务；如果已经存在一个事务，则加入到这个事务中。这是最常见的选择。 |
| PROPAGATION_SUPPORTS      | 支持当前事务。如果当前没有事务，则以非事务方式执行。         |
| PROPAGATION_MANDATORY     | 使用当前事务。如果当前没有事务，则抛出异常。                 |
| PROPAGATION_REQUIRES_NEW  | 新建事务。如果当前存在事务，则把当前事务挂起。               |
| PROPAGATION_NOT_SUPPORTED | 以非事务方式执行操作。如果当前存在事务，则把当前事务挂起。   |
| PROPAGATION_NEVER         | 以非事务方式执行。如果当前存在事务，则抛出异常。             |
| PROPAGATION_NESTED        | 如果当前存在事务，则在嵌套事务内执行；如果当前没有事务，则执行与PROPAGATION_REQUIRED类似的操作。 |

## 4、编程式的事务管理

TransactionTemplate和那些持久化模板类一样是线程安全的，因此，可以在多个业务类中共享TransactionTemplate实例进行事务管理。

```java
public class TransactionTemplate extends DefaultTransactionDefinition
		implements TransactionOperations, InitializingBean {
	//设置事务管理器
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	//在TransactionCallback回调接口定义需要以事务方式组织的数据访问逻辑
    public <T> T execute(TransactionCallback<T> action) throws TransactionException {
	//todo
	}
}
```

TransactionCallback接口只用一个方法。如果操作不会返回接口可以使用子接口TransactionCallbackWithoutResult。

```java
public interface TransactionCallback<T> {
    T doInTransaction(TransactionStatus status);
}
```

```java
public abstract class TransactionCallbackWithoutResult implements TransactionCallback<Object> {
    public TransactionCallbackWithoutResult() {
    }

    public final Object doInTransaction(TransactionStatus status) {
        this.doInTransactionWithoutResult(status);
        return null;
    }

    protected abstract void doInTransactionWithoutResult(TransactionStatus status);
}
```

代码清单，采用编程式的事务管理：

```java
@Service
public class ForumService1 {

    public ForumDao forumDao;
    public TransactionTemplate template;

    @Autowired
    public void setTemplate(TransactionTemplate template) {
        this.template = template;
    }

    public void addForum(final Forum forum) {
        template.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                //需要在事务环境中执行的代码
                forumDao.addForum(forum);
            }
        });
    }
}
```

由于Spring事务管理基于TranactionSynchronizationManager进行工作，所以，如果在回调接口方法中需要显示访问底层数据连接，则必须通过资源获取工具类得到线程绑定的数据连接。这是Spring事务管理的底层协议，不容违反。如果ForumDao是基于Spring提供的模板类构建的，由于模板类已经在内部使用了资源获取工具类获取数据连接，所以用户就不必关心底层数据连接的获取问题。

## 5、使用XML配置声明式事务

### 5.1、一个将被实施事务增强的服务接口

```java
@Service
public class BbtForum {

public ForumDao forumDao;

    public TopicDao topicDao;
    public PostDao postDao;

    public void addTopic(Topic topic) throws Exception {
        topicDao.addTopic(topic);
        postDao.addPost(topic.getPost());
    }

    public Forum getForum(int forumId) {
        return forumDao.getForum(forumId);
    }

    public void updateForum(Forum forum) {
        forumDao.updateForum(forum);
    }

    public int getForumNum() {
        return forumDao.getForumNum();
    }

}
```

### 5.2、使用原始的TransactionProxyFactoryBean

1. 声明式事务配置。

```xml
<!--引入Dao和DateSource的配置文件-->
<import resource="classpath:applicationContext-dao.xml"/>

<!--声明事务管理器-->
<bean id="txManager" 
      class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource"/>
</bean>
    
<!--需要实施事务增强的目标业务bean-->
<bean id="bbtForumTarget"
      class="com.smart.service.BbtForum"
      p:forumDao-ref="forumDao"
      p:topicDao-ref="topicDao"
      p:postDao-ref="postDao"/>

<!--使用事务代理工厂类为目标业务bean提供事务增强-->
<bean id="bbtForum" 
      class="org.springframework.transaction.interceptor.TransactionProxyFactoryBean"
      p:transactionManager-ref="txManager"
      p:target-ref="bbtForumTarget">
    <property name="transactionAttributes">
        <props>
            <prop key="get*">PROPAGATION_REQUIRED,readOnly</prop>
            <prop key="*">PROPAGATION_REQUIRED</prop>
        </props>
    </property>
</bean>
```

2. 异常回滚/提交规则

`<prop>`内值为事务属性信息，其配置格式为

`PROPAGATION, ISOLATION, readOnly, -Exception, +Exception`

传播行为是唯一必须提供的配置项，当`<prop>`为空时，也不会发生配置异常，但对应的匹配方法不会应用事务，相当于没有配置。

隔离级别配置项是可选的，默认为ISOLATION_DEFAULT，表示使用数据库默认的隔离级别。

如果希望将匹配的方法设置为只读事务，则可添加readOnly配置项。

当事务运行过程中发生异常，事务可以被声明为回滚或继续提交。在默认情况下，当发生运行期异常，事务将被回滚；当发生检查期异常时，既不回滚也不提交，控制权交给外层调用。这种默认的回滚规则在大多数情况下是适用的，不过用户也可以通过配置显式指定回滚规则：指定带正号+或负号-的异常类名，当抛出负号型异常的时候，将触发回滚；当抛出正号型异常时，即使这个异常是检查型异常，事务也会提交。

### 5.3、基于aop/tx命名空间的配置

```xml
<import resource="classpath:applicationContext-dao.xml"/>
<bean id="transactionManager"
      class="org.springframework.jdbc.datasource.DataSourceTransactionManager"
      p:dataSource-ref="dataSource"/>

<aop:config>
    <!--通过aop定义事务增强切面-->
    <aop:pointcut id="serviceMethod"
                  expression="execution(* com.smart.service.*Forum.*(..))"/>
    <!--引用事务增强-->
    <aop:advisor pointcut-ref="serviceMethod"
                 advice-ref="txAdvice"/>
</aop:config>
<tx:advice id="txAdvice" transaction-manager="txManager">
    <tx:attributes>
        <tx:method name="get*" read-only="false"/>
        <tx:method name="add*" rollback-for="PessimisticLockingFailureException"/>
        <tx:method name="update*"/>
    </tx:attributes>
</tx:advice>
```

基于aop/tx配置声明式事务管理是实际应用中最常使用的事务管理方式，它的表达能力最强且使用最为灵活。

### 5.4、使用注解配置声明式事务

#### 5.4.1、使用@Transactional注解

```java
@Transactional
public class BbtForum {
    public ForumDao forumDao;

    public void addTopic(Topic topic) throws Exception {
        topicDao.addTopic(topic);
        postDao.addPost(topic.getPost());
    }
}
```

代码清单 applicationContext-anno.xml：使事务注解生效

```xml
<bean id="txManager"
      class="org.springframework.jdbc.datasource.DataSourceTransactionManager"
      p:dataSource-ref="dataSource"/>

<tx:annotation-driven transaction-manager="txManager" proxy-target-class="true"/>
```

#### 5.4.2、在何处标注@Transactioncl注解

@Transactional注解可以被应用于接口定义和接口方法、类定义和类的public方法上。

但Spring建议在业务实现类上使用@Transactional注解。当然也可以在业务接口上使用@Transactional注解，但是这样会留下一些容易被忽视的隐患。因为注解不能被继承，所有在业务接口标注@Transactional注解不会被业务实现类继承。如果通过以下配置启用子类代理

<tx:annotation-driven transaction-manager="txManager" proxy-target-class="true"/>

那么业务类不会被增强，照样工作在非事务环境下。

#### 5.4.3、在方法上使用注解

方法处的注解会覆盖类定义处的注解。如果有些方法需要使用特殊的事务属性，则可以在类注解的基础上提供方法注解。

#### 5.4.4、使用不同的事务管理器

```java
@Service
public class MultiForumService {
   private ForumDao forumDao;
   private TopicDao topicDao;
   private PostDao postDao;
   
// @Transactional("topic")
   @TopicTransactional
   public void addTopic(Topic topic) throws Exception {
       System.out.println("topic tx");
   }
   
// @Transactional("forum")
   @ForumTransactional
   public void updateForum(Forum forum) {
      System.out.println("forum tx");
   }
}
```

