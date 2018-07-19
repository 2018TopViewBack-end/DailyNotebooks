package com.zhangmiaoxin.www.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class C3P0Util {
    private static DataSource ds=null;
    static{
        ds = new ComboPooledDataSource("mysql");//这是mysql数据库
    }

    /**
     * 获得数据库连接
     * @return   Connection
     */
    public synchronized static Connection getConnection(){
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 数据库关闭操作
     * @param con
     * @param pstmt
     * @param rs
     */
    public synchronized static void close(Connection con, PreparedStatement pstmt, ResultSet rs){
        try {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
