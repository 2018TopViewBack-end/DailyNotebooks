#                                             BUG！

1，错误：**org.apache.ibatis.binding.BindingException: Invalid bound statement (not found): org.fkjava.mybatis.dao.EmployeeMapper.findById**

 错误的原因是：项目是maven项目，请你在编译后，到接口所在目录看一看，很有可能是没有生产对应的xml文件，因为maven默认是不编译的，因此，你需要在你的pom.xml的<build></build>里面，加这么一段： 

1. <resources>  

2. ​    <resource>  

3. ​        <directory>src/main/java</directory>  

4. ​        <includes>  

5. ​            <include>**/*.xml</include>  

6. ​        </includes>  

7. ​        <filtering>true</filtering>  

8. ​    </resource>  

9. </resources>  

   ![1531920085957](C:\Users\i\AppData\Local\Temp\1531920085957.png)