# File类

### 1、File类的字段

```java
//与系统相关的默认每次分隔符
public static final char separatorChar = fs.getSeparator();

//与系统相关的默认名称-分隔符字符
public static final String separator = "" + separatorChar;

//与系统相关的路径分隔符
public static final char pathSeparatorChar = fs.getPathSeparator();

//与系统相关的路径分隔符字符，为方便起见，表示为字符串
public static final String pathSeparator = "" + pathSeparatorChar;
```

各个平台之间的路径分隔符是不一样的。

　　1. 对于UNIX平台，绝对路径名的前缀始终为`"/"` 。 相对路径名没有前缀。 表示根目录的抽象路径名具有前缀`"/"`和空名称序列。

　　2. 对于Microsoft Windows平台，包含驱动器说明符的路径名的前缀由后面跟着`":"`的驱动器号组成，如果路径名是绝对的，则可能后跟`"\\"` 。 UNC路径名的前缀为`"\\\\"` ; 主机名和共享名称是名称序列中的前两个名称没有有指定驱动器的相对路径名没有前缀。

　　那么为了屏蔽各个平台之间的分隔符差异，我们在构造 File 类的时候，就可以使用上述 Java 为我们提供的字段。

```java
System.out.println(File.pathSeparator);//输出 ;
System.out.println(File.separator);// 输出 \
```

### 2、File类的构造方法

```java
//通过将给定的路径名字字符串转换为抽象路径名来创建新的File实例
public File(String pathname) {}

//从父路径名字符串和子路径名字符串创建新的File实例
public File(String parent, String child) {}

//从父抽象路径名和子路径名字符串创建新的File实例
public File(File parent, String child) {}

//通过将给定的file：uri转换为抽象路径名来创建新的File实例
public File(URI uri) {}
```

如何使用上述构造方法，请看如下例子：

```java
@Test
public void testFile1() {
    //不适用Java提供的分隔符字段，注意：这样写只能在windows平台有效
    File f1 = new File("D:\\IO\\a.txt");
    //使用Java提供的分隔符
    File f2 = new File("D:" + File.separator + "IO" + File.separator + "a.txt");
    System.out.println(f1);//输出 D:\IO\a.txt
    System.out.println(f2);//输出 D:\IO\a.txt

    //从父抽象路径名和子路径名字符串创建新的 File实例。
    File f3 = new File("D:");
    File f4 = new File(f3, "IO");
    System.out.println(f4);//输出 D:\IO

    //通过将给定的路径名字符串转换为抽象路径名来创建新的 File实例。
    File f5 = new File("D:" + File.separator + "IO" + File.separator + "a.txt");
    System.out.println(f5);//输出 D:\IO\a.txt

    //从父路径名字符串和子路径名字符串创建新的 File实例。
    File f6 = new File("D:","IO\\a.txt");
    System.out.println(f6);//输出 D:\IO\a.txt
}
```

### 3、File类的常用方法

#### 3.1、创建方法

```java
//不存在返回true，存在返回false
public boolean createNewFile() throws IOException {}

//创建目录，如果上一级目录不存在，则会创建失败
public boolean mkdir() {}

//创建多级目录，如果上一级目录不存在也会自动创建
public boolean mkdirs() {}
```

#### 3.2、删除方法

```java
//删除文件或目录，如果表示目录，则目录下必须为空才能删除
public boolean delete() {}

//文件使用完成后删除
public void deleteOnExit() {}
```

#### 3.3、判断方法

```java
public boolean canRead() {} //判断文件是否可读
public boolean canWrite() {} //判断文件是否可写
public boolean exists() {} //判断文件或目录是否已经存在
public boolean isDirectory() {} //判断此路径是否为一个目录
public boolean isFile() {}	//判断是否为一个文件
public boolean isHidden() {} //判断是否为隐藏文件
public boolean isAbsolute() {} //判断是否是绝对路径，文件不存在也能判断
```

#### 3.4、获取方法

```java
public String getName() //获取此路径表示的文件或目录名称
public String getPath() //将此路径名转换为路径名字符串
public String getAbsolutePath() //返回此抽象路径名的绝对形式
public String getParent()//如果没有父目录返回null
public long lastModified()//获取最后一次修改的时间
public long length() //返回由此抽象路径名表示的文件的长度。
public boolean renameTo(File f) //重命名由此抽象路径名表示的文件。
public File[] liseRoots()//获取机器盘符
public String[] list()  //返回一个字符串数组，命名由此抽象路径名表示的目录中的文件和目录。
public String[] list(FilenameFilter filter) //返回一个字符串数组，命名由此抽象路径名表示的目录中满足指定过滤器的文件和目录。
```

### 4、测试

```java
@Test
public void testFile2() throws IOException {
    File file = new File("D:/IO/a.txt");
    Assert.assertTrue(file.createNewFile());
    System.out.println("是不是一个目录路径：" + file.isDirectory());
    System.out.println("是不是一个文件路径：" + file.isFile());

    System.out.println("-------");
    System.out.println(file.getName());
    System.out.println(file.getParent());
    System.out.println(file.getPath());
}

public static void getFileList(File file) {
    File[] files = file.listFiles();
    for (File f : files) {
        System.out.println(f);
        if (f.isDirectory()) {
            getFileList(f);
        }
    }
}

@Test
public void testGetFileList() {
    File file = new File("D:\\Java");
    getFileList(file);
}

```

