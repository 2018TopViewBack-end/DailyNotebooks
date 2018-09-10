# shiro的授权

## 1、IniRealm

##### 1.1、配置文件

```ini
[users]
Mark=123456,admin

[roles]
admin=user:delete,user:find
```

##### 1.2、测试代码

```java
package com.pzh.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

import java.util.Arrays;

public class IniRealmTest {

    @Test
    public void testIniRealm() {
        //1、构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();

        //2、创建iniRealm
        IniRealm iniRealm = new IniRealm("classpath:users.ini");

        //3、将iniRealm设置到SecurityManager里面
        defaultSecurityManager.setRealm(iniRealm);

        //4、主体提交认证数据
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("Mark", "123456");
        subject.login(token);

        System.out.println("是否认证成功:" + subject.isAuthenticated());
        System.out.println("是否具有admin角色:" + subject.hasRole("admin"));
        System.out.println("是否具有user:delete权限:" + subject.isPermitted("user:delete"));
        System.out.println("是否具有user:update权限:" + subject.isPermitted("user:update"));
        System.out.println("是否具有user:delete和user:find权限" + subject.isPermittedAll("user:delete","user:find"));
    }
}
```

##### 1.3、运行结果

```
是否认证成功:true
是否具有admin角色:true
是否具有user:delete权限:true
是否具有user:update权限:false
是否具有user:delete和user:find权限true

```

## 2、JdbcRealm

##### 2.1、数据库

```sql
CREATE DATABASE /*!32312 IF NOT EXISTS*/`shiro_test` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `shiro_test`;

/*Table structure for table `permissions` */

DROP TABLE IF EXISTS `permissions`;

CREATE TABLE `permissions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) DEFAULT '',
  `url` varchar(100) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `permissions` */

insert  into `permissions`(`id`,`name`,`url`) values (1,'添加用户','user/add'),(2,'删除用户','user/delete'),(3,'查找用户','user/find'),(4,'更新用户','user/update');

/*Table structure for table `roles` */

DROP TABLE IF EXISTS `roles`;

CREATE TABLE `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(64) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `roles` */

insert  into `roles`(`id`,`role_name`) values (1,'user'),(2,'admin');

/*Table structure for table `roles_and_permissions` */

DROP TABLE IF EXISTS `roles_and_permissions`;

CREATE TABLE `roles_and_permissions` (
  `role_id` int(11) DEFAULT NULL,
  `permission_id` int(11) DEFAULT NULL,
  KEY `roles_and_permissions_roles_id_fk` (`role_id`),
  KEY `roles_and_permissions_permissions_id_fk` (`permission_id`),
  CONSTRAINT `roles_and_permissions_permissions_id_fk` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `roles_and_permissions_roles_id_fk` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `roles_and_permissions` */

insert  into `roles_and_permissions`(`role_id`,`permission_id`) values (1,3),(2,1),(2,2),(2,3),(2,4);

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) DEFAULT '',
  `password` varchar(64) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `users` */

insert  into `users`(`id`,`username`,`password`) values (1,'John','123456');

/*Table structure for table `users_and_roles` */

DROP TABLE IF EXISTS `users_and_roles`;

CREATE TABLE `users_and_roles` (
  `user_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  KEY `users_and_roles_users_id_fk` (`user_id`),
  KEY `users_and_roles_roles_id_fk` (`role_id`),
  CONSTRAINT `users_and_roles_roles_id_fk` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `users_and_roles_users_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `users_and_roles` */

insert  into `users_and_roles`(`user_id`,`role_id`) values (1,2),(1,1);
```

##### 2.2、测试代码

```java
package com.pzh.shiro;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class JdbcRealmTest {

    /**
     * 数据源
     */
    private DruidDataSource dataSource = new DruidDataSource();

    /**
     * sql查询语句:用户名和密码是否匹配
     */
    private static final String AUTHENTICATION_QUERY = "SELECT password FROM users WHERE username = ?";

    /**
     * sql查询语句:为认证成功的用户赋予角色
     */
    private static final String USER_ROLES_QUERY =
            "SELECT "
            + " roles.role_name "
            + " FROM "
            + " users_and_roles "
            + " INNER JOIN roles ON (users_and_roles.role_id = roles.id) "
            + " INNER JOIN users ON (users_and_roles.user_id = users.id) "
            + " WHERE "
            + " users.username = ?";

    /**
     * sql查询语句:授权
     */
    private static final String PERMISSIONS_QUERY =
            "SELECT "
            + " permissions.url "
            + " FROM "
            + " roles_and_permissions "
            + " INNER JOIN roles ON (roles_and_permissions.role_id = roles.id) "
            + " INNER JOIN permissions ON (roles_and_permissions.permission_id = permissions.id) "
            + " WHERE "
            + " roles.role_name = ? ";


    {
        //初始化数据源
        dataSource.setUrl("jdbc:mysql://localhost:3306/shiro_test");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
    }

    @Test
    public void testJdbcRealm() {
        //构建JdbcRealm
        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(dataSource);
        jdbcRealm.setAuthenticationQuery(AUTHENTICATION_QUERY);
        jdbcRealm.setUserRolesQuery(USER_ROLES_QUERY);
        //开启授权
        jdbcRealm.setPermissionsLookupEnabled(true);
        jdbcRealm.setPermissionsQuery(PERMISSIONS_QUERY);

        //构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);
        SecurityUtils.setSecurityManager(defaultSecurityManager);

        //提交认证数据
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("John", "123456");
        subject.login(token);

        //判断角色
        subject.checkRole("admin");
        subject.checkRole("user");
        subject.checkRoles("admin", "user");

        //判断权限
        subject.checkPermission("user/add");
        subject.checkPermission("user/update");
        subject.checkPermission("user/delete");
        subject.checkPermission("user/find");
        subject.checkPermissions("user/find","user/update","user/delete","user/add");
    }
}
```

## 3、自定义Realm

##### 1、MyRealm代码

```java
package com.pzh.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MyRealm extends AuthorizingRealm {

    private Map<String, String> users = new HashMap<>();

    {
        users.put("Tom", "123456");
        super.setName("myRealm");
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //获取用户名
        String username = (String) principals.getPrimaryPrincipal();
        //模拟获取角色数据和权限数据
        Set<String> roles = getRolesByUsername(username);
        Set<String> permissions = getPermissionsByUsername(username);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(roles);
        authorizationInfo.setStringPermissions(permissions);
        return authorizationInfo;
    }

    /**
     * 模拟从数据库中获取权限数据
     */
    private Set<String> getPermissionsByUsername(String username) {
        Set<String> permissions = new HashSet<>();
        permissions.add("user:find");
        permissions.add("user:update");
        return permissions;
    }

    /**
     * 模拟从数据库中获取角色数据
     */
    private Set<String> getRolesByUsername(String username) {
        Set<String> roles = new HashSet<>();
        roles.add("admin");
        roles.add("user");
        return roles;
    }

    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        //获取用户名
        String username = (String) token.getPrincipal();
        //模拟从数据库中获取批证
        String password = getPasswordByUsername(username);
        //如果为空,直接返回null
        if (password == null) {
            return null;
        }
        //到这里,表示认证成功
        return new SimpleAuthenticationInfo(username, password, getName());
    }

    /**
     * 模拟数据库获取认证数据
     */
    private String getPasswordByUsername(String username) {
        return users.get(username);
    }
}
```

2、测试



##### 2、测试代码

```java
package com.pzh.shiro;

import com.pzh.shiro.realm.MyRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Assert;
import org.junit.Test;

public class MyRealmTest {

    @Test
    public void testMyRealm() {
        //构建自定义的realm
        MyRealm myRealm = new MyRealm();

        //构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(myRealm);
        SecurityUtils.setSecurityManager(defaultSecurityManager);

        //主体提交认证数据
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("Tom", "123456");
        subject.login(token);

        System.out.println("是否认证成功：" + subject.isAuthenticated());
        subject.checkRole("admin");
        subject.checkRole("user");
        subject.checkRoles("admin", "user");

        subject.checkPermission("user:update");
        subject.checkPermission("user:find");
        subject.checkPermissions("user:update", "user:find");
        Assert.assertFalse(subject.isPermitted("user:add"));
    }

}
```

## 4、加密：

```java
HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
matcher.setHashAlgorithmName("md5");//设置算法名称
matcher.setHashIterations(1);//设置加密次数
myRealm.setCredentialsMatcher(matcher);
```

```java
@Override
protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

    //获取用户名
    String username = (String) token.getPrincipal();
    //模拟从数据库中获取批证
    String password = getPasswordByUsername(username);
    //如果为空,直接返回null
    if (password == null) {
        return null;
    }
    //到这里,表示认证成功
    SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username, password, getName());
    authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("Tom"));//设置加的盐
    return authenticationInfo;
}
```

在reaml中设置一个`HashedCredentialsMatcher`,同时在返回认证信息的地方设置加的盐即可。

