package com.zhangmiaoxin.www.controller;

import com.zhangmiaoxin.www.po.User;
import com.zhangmiaoxin.www.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class RegisterServlet extends HttpServlet {
    private UserService us = new UserService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user=check(request,response);
        if(user!=null) {
            int result = us.register(user);
            if (result == -5) {
                request.setAttribute("error", "很抱歉，该用户名已经存在，请更换");
                request.getRequestDispatcher("register.jsp").forward(request, response);
            } else if (result == 1) {
                request.setAttribute("success","恭喜您，注册成功，点击确定为您跳转到登录界面");
                request.getRequestDispatcher("register.jsp").forward(request,response);
            } else {
                request.setAttribute("error", "很抱歉，注册失败,请重新注册");
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("register.jsp");    //不允许用户用get方法直接访问Servlet
    }

    protected User check(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        List<String> errorList=new ArrayList<>();
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        String rePassword=request.getParameter("re_password");
        String tel=request.getParameter("tel");
        String name=request.getParameter("name");

        if(null == username || "".equals(username))
        {
            errorList.add("用户名不能为空！");
        }
        if(null == password || "".equals(password))
        {
            errorList.add("密码不能为空！");
        }
        if(null==rePassword||"".equals(rePassword)){
            errorList.add("重复密码不能为空");
        }
        if(null==tel||"".equals(tel)){
            errorList.add("电话不能为空");
        }
        if(null==name||"".equals(name)){
            errorList.add("昵称不能为空");
        }
        if(null!=password&&(password.length()<6||password.length()>16)){
            errorList.add("密码长度必须在6到16之间");
        }
        if(null!=username&&(username.length()<6||username.length()>16)){
            errorList.add("账号长度必须在6到16之间");
        }
        if(!password.equals(rePassword)){
            errorList.add("密码与确认密码不一致");
        }

        if(errorList.isEmpty()){
            User user=new User(username,password,tel,name);
            return user;
        }else {
            request.setAttribute("errorList",errorList);
            request.getRequestDispatcher("registerFailure.jsp").forward(request, response);
            return null;
        }
    }
}
