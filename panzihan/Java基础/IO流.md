# I/O系统

### 1、File类

它既能代表一个特定文件的名称，又能代表一个目录下的一组文件的名称。

#### 1.1、目录列表器

| 返回值      | 描述                                                         |
| ----------- | ------------------------------------------------------------ |
| ` String[]` | `list()`             返回一个字符串数组，这些字符串指定此抽象路径名表示的目录中的文件和目录。 |

```java
public class DirList {
    public static void main(String[] args) {
        File path = new File(".");
        String[] list;
        if (args.length == 0) {
            list = path.list();
        } else {
            list = path.list(new DirFilter(args[0]));
        }
        Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);
        for (String dirItem : list) {
            System.out.println(dirItem);
        }
    }
}

class DirFilter implements FilenameFilter {//实现了FilenameFilter接口，重写accept方法
    private Pattern pattern;
    public DirFilter(String regex) {
        pattern = Pattern.compile(regex);
    }
    @Override
    public boolean accept(File dir, String name) {
        return pattern.matcher(name).matches();
    }
}
```

DirFilter这个类存在的唯一原因就是将accept（）方法提供list（）方法使用，使list（）方法可以回调accept（）。

使用匿名内部类改写

```java
public class DirList2 {
    //传递给filter（）的参数必须是final，这在匿名内部类是必须的，这样它才能使用来之该类范围之外的东西
    public static FilenameFilter filter(final String regex) {
        //创建一个匿名内部类
        return new FilenameFilter() {
            private Pattern pattern = Pattern.compile(regex);
            @Override
            public boolean accept(File dir, String name) {
                return pattern.matcher(name).matches();
            }
        };
    }

    public static void main(String[] args) {
        File path = new File(".");
        String[] list;
        if (args.length == 0) {
            list = path.list();
        } else {
            list = path.list(filter(args[0]));
        }
        Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);
        for (String dirItem : list) {
            System.out.println(dirItem);
        }
    }
}
```

进一步改进，定义一个作为list（）参数的匿名内部类，这样一来程序会变得更小。

```java
public class DirList3 {
    public static void main(String[] args) {
        File path = new File(".");
        String[] list;
        if (args.length == 0) {
            list = path.list();
        } else {
            list = path.list(new FilenameFilter() {
                private Pattern pattern = Pattern.compile(args[0]);
                @Override
                public boolean accept(File dir, String name) {
                    return pattern.matcher(name).matches();
                }
            });
        }
        Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);
        for (String dirItem : list) {
            System.out.println(dirItem);
        }
    }
}
```

既然匿名内部类直接使用args[0]，那么传递给main（）方法的参数现在就是final的。

这个例子展示了匿名内部类怎样通过创建特定的、一次性的类来解决问题。此方法的一个优点就是将解决特定问题的代码隔离、聚拢于一点。而另一方面，这种方法不易阅读，因此要谨慎使用。