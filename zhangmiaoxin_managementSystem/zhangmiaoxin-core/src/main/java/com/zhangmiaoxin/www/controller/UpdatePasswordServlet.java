package com.zhangmiaoxin.www.controller;

import com.zhangmiaoxin.www.po.User;
import com.zhangmiaoxin.www.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class UpdatePasswordServlet extends HttpServlet {
    private UserService us = new UserService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String oldPassword=request.getParameter("oldPassword");
        User oldUser=(User)request.getSession().getAttribute("user");
        if(!oldPassword.equals(oldUser.getPassword())){
            request.setAttribute("passwordMessage","原密码错误，请重新输入");
            request.getRequestDispatcher("personalInformation.jsp").forward(request,response);
            return;
        }
        String newPassword=request.getParameter("newPassword");
        String reNewPassword=request.getParameter("reNewPassword");
        if(newPassword.equals(reNewPassword)&&newPassword.length()>5&&newPassword.length()<17){
            User user=new User(oldUser.getId(),newPassword);
            int result=us.updatePasswordService(user);
            if(result==1){
                oldUser.setPassword(newPassword);
                request.getSession().setAttribute("user",oldUser);

                request.setAttribute("passwordMessage","密码修改成功");
                request.getRequestDispatcher("personalInformation.jsp").forward(request,response);
            }else {
                request.setAttribute("passwordMessage","很抱歉，密码修改失败，请重试");
                request.getRequestDispatcher("personalInformation.jsp").forward(request,response);
            }
        }else {
            request.setAttribute("passwordMessage","两次输入新密码不一致，请重新输入");
            request.getRequestDispatcher("personalInformation.jsp").forward(request,response);
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
