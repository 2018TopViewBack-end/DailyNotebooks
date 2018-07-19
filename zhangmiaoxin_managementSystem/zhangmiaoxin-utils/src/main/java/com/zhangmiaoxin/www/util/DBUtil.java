package com.zhangmiaoxin.www.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {

    private static Connection con;
    private static PreparedStatement pstmt;
    private static ResultSet rs;

    private DBUtil() {

    }

    /**
     * 查询全部字段数据并返回结果集
     * @param sql
     * @return
     */
    public static ResultSet executeQuery(String sql) {
        con = C3P0Util.getConnection();
        if (con == null) {
            System.out.println("获取数据库连接失败，无法进行操作");
            return null;
        }
        try {
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * 查询指定字段的数据并返回结果集
     * @param sql
     * @param obj
     * @return
     */
    public static ResultSet executeQuery(String sql, Object[] obj) {
        con = C3P0Util.getConnection();
        if (con == null) {
            System.out.println("获取数据库连接失败，无法进行操作");
            return null;
        }
        try {
            pstmt = con.prepareStatement(sql);
            for (int i = 0; i < obj.length; i++) {
                pstmt.setObject(i+1, obj[i]);
            }
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * 对数据进行增删改操作
     * @param sql
     * @param obj 操作的字段
     * @return 成功操作记录数
     */
    public static int executeUpdate(String sql, Object[] obj) {
        int result = -1;
            con = C3P0Util.getConnection();
            if (con == null) {
                System.out.println("获取数据库连接失败，无法进行操作");
                return result;
            }
        try {
            pstmt = con.prepareStatement(sql);
            for (int i = 0; i < obj.length; i++) {
                pstmt.setObject(i + 1, obj[i]);
            }
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close();
        }
        return result;
    }

    /**
     * 将连接归还到连接池
     */
    public static void close() {
        C3P0Util.close(con,pstmt,rs);
    }
}
