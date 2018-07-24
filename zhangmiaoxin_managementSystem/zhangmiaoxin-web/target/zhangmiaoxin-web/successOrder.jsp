<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>下单成功</title>
    <script type="text/javascript">
        function showOrder() {
            document.getElementById("orderDetail").style.display='block';
        }
    </script>
</head>
<body>
    <%@ include file="menuBar.jsp" %>
    <h1 align="center" style="color: red">恭喜您，下单成功</h1>
    <br><br><br>
    <div align="center">
        现在您要去：<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="button" value="看看还有啥好吃的" onclick="document.location.href='scanStore'">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="button" value="查看订单详情" onclick="showOrder()">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="button" value="退出系统" onclick="window.location.href='Logout'">
    </div>


    <div align="center" id="orderDetail" style="display: none">
        <br><br><br><br><br>
        <table id="orderTable">
            <tr><th>订单号：<%= request.getAttribute("orderId")%></th></tr>
            <%--这里已经包含商家名称四个字--%>
            <tr><th id="storeName"><%=request.getParameter("storeName")%></th></tr>
            <tr><th>商品名称: <%=request.getParameter("foodName")%></th></tr>
            <tr><th>单价: <%= request.getParameter("foodPrice") %></th></tr>
            <tr><th>数量: <%= request.getParameter("number") %></th></tr>
            <tr><th>小计: <%= request.getParameter("price") %></th></tr>
            <tr><th>总计：<%= request.getParameter("price") %></th></tr>
            <tr><th>收件人信息：</th></tr>
            <tr><th>&nbsp;&nbsp;&nbsp;收件人姓名：<%= request.getParameter("receiverName") %></th></tr>
            <tr><th>&nbsp;&nbsp;&nbsp;收件人电话：<%= request.getParameter("receiverTel") %></th></tr>
            <tr><th>&nbsp;&nbsp;&nbsp;收件人地址：<%= request.getParameter("receiverAddress") %></th></tr>
            <tr><th>订单时间：<%= request.getAttribute("orderDate")%></th></tr>
            <tr><th>订单状态：待接单</th></tr>
        </table>
        <br>
        给商家的备注：<%= request.getParameter("message")%>
        <br><br>
    </div>
</body>
</html>
