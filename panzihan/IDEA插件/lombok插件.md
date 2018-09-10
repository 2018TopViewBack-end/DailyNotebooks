# lombok插件

### 1、安装lombok插件

### 2、添加lombok的maven的pom.xml依赖

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.16.10</version>
</dependency>
```

### 3、测试

```java
/**
 * @author Pan梓涵
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class User {
    private int id;
    private String name;
    private int age;
}
```

生成的实体类

```java
public class User {
    private int id;
    private String name;
    private int age;

    public User() {
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String toString() {
        return "User(id=" + this.getId() + ", name=" + this.getName() + ", age=" + this.getAge() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof User)) {
            return false;
        } else {
            User other = (User)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getId() != other.getId()) {
                return false;
            } else {
                Object this$name = this.getName();
                Object other$name = other.getName();
                if (this$name == null) {
                    if (other$name == null) {
                        return this.getAge() == other.getAge();
                    }
                } else if (this$name.equals(other$name)) {
                    return this.getAge() == other.getAge();
                }

                return false;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof User;
    }

    public int hashCode() {
        int PRIME = true;
        int result = 1;
        int result = result * 59 + this.getId();
        Object $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        result = result * 59 + this.getAge();
        return result;
    }
}
```

### 4、注解

##### 4.1、@Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode 注解

##### 4.2、@Data注解 

相当于 @Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode的合集 

##### 4.3、@log注解

```java
//效果
private static final Logger log = Logger.getLogger(User.class.getName());
```

##### 4.4、@Builder

构造Builder模式的结构。通过内部类Builder()进行构建对象。

##### 4.5、@Value

与@Data相对应的@Value， 两个annotation的主要区别就是如果变量不加@NonFinal ，@Value会给所有的弄成final的。当然如果是final的话，就没有set方法了。 

##### 4.6、@Synchronized

同步方法