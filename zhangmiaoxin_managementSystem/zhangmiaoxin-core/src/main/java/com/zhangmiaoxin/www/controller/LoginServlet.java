package com.zhangmiaoxin.www.controller;

import com.zhangmiaoxin.www.po.Order;
import com.zhangmiaoxin.www.po.Store;
import com.zhangmiaoxin.www.po.User;
import com.zhangmiaoxin.www.service.StoreService;
import com.zhangmiaoxin.www.service.UserService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class LoginServlet extends javax.servlet.http.HttpServlet {
    private UserService us = new UserService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        List<String> errorList = new ArrayList<>();

        //进行身份的检验
        Integer roleNum = null;
        try {
            roleNum = Integer.parseInt(request.getParameter("role"));
        } catch (NumberFormatException e) {
            errorList.add("身份必须选择");
        }

        if (null == username || "".equals(username)) {
            errorList.add("用户名不能为空！");
        }
        if (null == password || "".equals(password)) {
            errorList.add("密码不能为空！");
        }

        //避免下面的空指针异常
        if(roleNum==null){
            errorList.add("身份必须选择");
        }
        if (errorList.isEmpty()) {
            String role = us.getRoleService(roleNum);
            if (role != null) {
                User user = new User(username, password, roleNum);
                if ((user = us.loginCheck(user)) != null) {   //检验用户账户密码是否正确
                    user.setRole(role);
                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);
                    session.setAttribute("cart",new ArrayList<Order>());
                    //如果是商家，就在session中放入自己的店铺
                    if(roleNum==2){
                        Store myStore=new StoreService().selectMyStore(user.getId());
                        session.setAttribute("myStore",myStore);
                    }
                    response.sendRedirect("scanStore");
                } else {
                    request.setAttribute("error", "账号或密码错误");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
            }
        } else {
                request.setAttribute("errorList", errorList);
                request.getRequestDispatcher("loginFailure.jsp").forward(request, response);
            }
        }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }
}
