package com.zhangmiaoxin.www.dao;

import com.zhangmiaoxin.www.po.User;

public interface UserMapper {
    /**
     * 用户的登录，若正确则返回一个用户对象
     * @param user
     * @return User
     */
    User getUser(User user);

    /**
     * 获取用户的角色
     * @param roleId
     * @return String roleName
     */
    String getRole(int roleId);

    /**
     * 用户修改自己的个人信息（昵称，电话，图片）
     * @param user
     * @return int result
     */
    int updateInformation(User user);

    /**
     * 用户修改自己的密码
     * @param user
     * @return int result
     */
    int updatePassword(User user);

    /**
     * 管理员批准普通用户申请成为商家
     * @param userId
     * @return int result
     */
    int applySeller(int userId);

    /**
     * 管理员审核待上架店铺时用到的申请用户信息
     * @param userId
     * @return User
     */
    User onStoreUser(int userId);

    /**
     * 注册前先检查用户名是否已经被注册
     * @param username
     * @return 用户名id
     */
    Integer checkRegister(String username);

    /**
     * 用户注册
     * @param user
     * @return 是否注册成功
     */
    int register(User user);
}
