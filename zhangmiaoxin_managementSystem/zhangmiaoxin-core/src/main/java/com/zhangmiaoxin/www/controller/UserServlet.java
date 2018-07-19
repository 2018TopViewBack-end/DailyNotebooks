package com.zhangmiaoxin.www.controller;

import com.zhangmiaoxin.www.po.User;
import com.zhangmiaoxin.www.service.UserService;
import com.zhangmiaoxin.www.util.Pic;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class UserServlet extends HttpServlet {
    private UserService us = new UserService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String,String> paramMap=new HashMap<>();
        User newUser=(User)request.getSession().getAttribute("user");
        paramMap.put("reName","");
        paramMap.put("reTel","");
        paramMap.put("pic",((User)request.getSession().getAttribute("user")).getPic());

        paramMap= Pic.uploadPic(request,paramMap);
        for (Map.Entry<String,String> entry: paramMap.entrySet()) {
            switch (entry.getKey()){
                case "reName":
                    newUser.setName(entry.getValue());
                    break;
                case "reTel":
                    newUser.setTel(entry.getValue());
                    break;
                case "pic":
                    newUser.setPic(entry.getValue());
                    break;
            }
        }

        User reUser=new User(newUser.getId(),newUser.getTel(),newUser.getPic(),newUser.getName());
        if(us.updateInformationService(reUser)==1){
            request.getSession().setAttribute("user",newUser);
            request.setAttribute("updateMessage","信息更改成功");
        }else {
            request.setAttribute("updateMessage","信息更改失败");
        }
        request.getRequestDispatcher("personalInformation.jsp").forward(request,response);

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
