package com.zhangmiaoxin.www.service;


import com.zhangmiaoxin.www.dao.UserMapper;
import com.zhangmiaoxin.www.po.User;
import com.zhangmiaoxin.www.util.Judge;
import com.zhangmiaoxin.www.util.MybatisUtil;
import org.apache.ibatis.session.SqlSession;

public class UserService {

    /**
     * 用户的登录，若正确则返回一个用户对象
     * @param user
     * @return
     */
    public User loginCheck(User user) {
        SqlSession session = MybatisUtil.getSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        User newUser = userMapper.getUser(user);
        session.close();

        return newUser;
    }

    /**
     * 用户修改自己的密码
     * @param user
     * @return
     */
    public int updatePasswordService(User user){
        SqlSession session = MybatisUtil.getSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        int result = userMapper.updatePassword(user);
        session.close();

        return result;
    }

    /**
     * 用户修改自己的个人信息（昵称，电话，图片）
     * @param user
     * @return
     */
    public int updateInformationService(User user){
        SqlSession session = MybatisUtil.getSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        int result = userMapper.updateInformation(user);
        session.close();

        return result;
    }

    /**
     * 获取用户的角色
     * @param roleId
     * @return
     */
    public String getRoleService(int roleId){
        SqlSession session = MybatisUtil.getSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        String roleName = userMapper.getRole(roleId);
        session.close();

        return roleName;
    }

    /**
     * 管理员批准普通用户申请成为商家服务
     * @param userId
     * @return
     */
    public boolean applySellerService(int userId){
        SqlSession session = MybatisUtil.getSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        int result=userMapper.applySeller(userId);
        session.close();

        return Judge.judgeUse(result);
    }

    /**
     * 管理员审核待上架店铺时用到的申请用户信息服务
     * @param userId
     * @return
     */
    public User onStoreUserService(int userId){
        SqlSession session = MybatisUtil.getSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        User user = userMapper.onStoreUser(userId);
        session.close();

        return user;
    }

    /**
     * 注册
     * @param user
     * @return 注册成功则返回1
     */
    public int register(User user) {
        SqlSession session = MybatisUtil.getSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        Integer userId = userMapper.checkRegister(user.getUsername());
        if(userId == null || userId <= 0){
            int result = userMapper.register(user);
            session.close();
            return result;
        } else {
            session.close();
            return -5;
        }
    }

}
