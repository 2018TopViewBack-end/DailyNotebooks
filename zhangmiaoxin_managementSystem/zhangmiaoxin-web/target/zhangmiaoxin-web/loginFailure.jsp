<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录失败</title>
</head>
<body>
    错误信息如下：<br><br><br>

    <% List<String> list = (List<String>) request.getAttribute("errorList"); %>
    <ol>
        <%
    for(String str : list)
    {
    %>
        <li style="color: red"><%= str %></li>
    <%} %>
    </ol>
    <br><br>
    <a href="login.jsp" style="color: darkblue">点击此处重新登录</a>
</body>
</html>
