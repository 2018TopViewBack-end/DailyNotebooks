package com.zhangmiaoxin.www.controller;

import com.zhangmiaoxin.www.po.Receiver;
import com.zhangmiaoxin.www.po.User;
import com.zhangmiaoxin.www.service.ReceiverService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class ReceiverServlet extends HttpServlet {
    private ReceiverService rs = new ReceiverService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer receiverId = null;
        try {
            receiverId = Integer.parseInt(request.getParameter("receiverId"));
        } catch (NumberFormatException e) {

        }
        String name = request.getParameter("editName");
        String tel = request.getParameter("editTel");
        String address = request.getParameter("editAddress");

        if(name==null||name==""||tel==null||tel==""||address==""||address==null){
            request.setAttribute("nouNull","收货信息中任一项都不能为空");
            request.getRequestDispatcher("Pay").forward(request,response);
        }

        if (receiverId != null) {
            Receiver receiver = new Receiver(receiverId, name, tel, address);

            String editResult;
            if (rs.updateReceiverService(receiver) == 1) {
                editResult = "收件信息更改成功";
            } else {
                editResult = "收件信息更改失败";
            }
            request.setAttribute("editResult", editResult);
            request.getRequestDispatcher("Pay").forward(request, response);
        }else {
            int userId=((User)request.getSession().getAttribute("user")).getId();
            Receiver receiver=new Receiver(name,tel,address,userId);

            String addResult;
            if (rs.addReceiverService(receiver) == 1) {
                addResult = "新增收货信息成功 ";
            } else {
                addResult = "新增收货信息失败 ";
            }
            request.setAttribute("addResult", addResult);
            request.getRequestDispatcher("Pay").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
