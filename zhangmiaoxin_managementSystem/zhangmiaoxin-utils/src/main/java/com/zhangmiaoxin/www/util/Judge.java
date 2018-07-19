package com.zhangmiaoxin.www.util;


public class Judge {
    /**
     * 判断数据库操作是否成功
     * @param result
     * @return
     */
    public static boolean judgeUse(int result){
        if (result>0){
            return true;
        }else {
            return false;
        }
    }
}
