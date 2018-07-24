package com.zhangmiaoxin.www.util;

public class Constants {
    //JDBC
    public static final String JDBC_URL="jdbc:mysql://localhost:3306/topview2";    //数据库地址
    public static final String JDBC_USERNAME="root";    //用户名
    public static final String JDBC_PASSWORD="justinsgf5203!";      //密码
    public static final String JDBC_DRIVER="com.mysql.jdbc.Driver";     // 驱动名称

    //上传文件存储目录
    public static final String UPLOAD_DIRECTORY="pics";
    //上传配置
    public static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  //3MB
    public static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; //40MB
    public static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; //50MB

    public static final String DEFAULT_PIC="20180523231548.jpg";
}
