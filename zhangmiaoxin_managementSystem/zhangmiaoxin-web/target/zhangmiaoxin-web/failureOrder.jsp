<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>下单失败</title>
</head>
<body>
    <%@ include file="menuBar.jsp" %>
    <h1 align="center" style="color: red">非常抱歉，下单失败</h1>
    <br><br><br>
    <div align="center">
        或者您再试试：<br>
        &nbsp;&nbsp;&nbsp;<input type="button" value="看看还有啥好吃的" onclick="document.location.href='scanStore'">
        还是：<br>
        &nbsp;&nbsp;&nbsp;<input type="button" value="退出系统" onclick="window.location.href='Logout'">
    </div>
</body>
</html>
