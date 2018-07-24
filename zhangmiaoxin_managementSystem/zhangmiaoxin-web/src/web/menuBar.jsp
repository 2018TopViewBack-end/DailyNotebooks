<%@ page import="com.zhangmiaoxin.www.po.User" %>
<%@ page import="java.util.Calendar" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>MenuBar</title>
</head>
<body>
    <%
        User user=(User) session.getAttribute("user");
        int hour= Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String greet;
        if(hour>=18||hour<6){
            greet=",晚上好! 欢迎来到点不到外卖的外卖系统";
        }else if(hour>=6&&hour<12){
            greet=",早上好! 欢迎来到点不到外卖的外卖系统";
        }else {
            greet=",下午好! 欢迎来到点不到外卖的外卖系统";
        }
    %>
    <div align="left">
        <img src="pics/<%=user.getPic() %>" width="50px" >&nbsp;&nbsp;&nbsp;
        <span><%= "亲爱的"+user.getName() %></span>
        <span><%= greet %></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="button" value="查看个人信息" onclick="window.location.href='personalInformation.jsp'">
        &nbsp;&nbsp;&nbsp;
        <input type="button" value="查看所有订单" onclick="window.location.href='SelectOrder'">&nbsp;&nbsp;&nbsp;
        <input type="button" value="我的购物车" onclick="window.location.href='cart.jsp'">&nbsp;&nbsp;&nbsp;
        <input type="button" value="返回点餐" onclick="window.location.href='scanStore'">
        <%
            if(user.getRoleId()==1){
        %>
        <input id="applyStore" type="button" value="我要开店" onclick="document.location.href='GetCategory'">&nbsp;&nbsp;&nbsp;
        <%
            }else if(user.getRoleId()==2){
        %>
        <input type="button" value="进入我的店铺" onclick="document.location.href='MyStore'">&nbsp;&nbsp;&nbsp;
        <%
            }else {
        %>
        <input type="button" value="查看待审核的菜品" onclick="document.location.href='OnFood'">&nbsp;&nbsp;&nbsp;
        <input type="button" value="查看待审核的店铺" onclick="document.location.href='OnStore'">
        <%
            }
        %>
        <input type="button" value="退出登录" onclick="window.location.href='Logout'">
    </div>
</body>
</html>
