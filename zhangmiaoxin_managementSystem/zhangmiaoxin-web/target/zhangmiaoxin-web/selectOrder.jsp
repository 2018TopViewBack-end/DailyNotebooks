<%@ page import="java.util.List" %>
<%@ page import="com.zhangmiaoxin.www.po.Order" %>
<%@ page import="com.zhangmiaoxin.www.po.OrderItem" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>我的订单</title>
    <script type="text/javascript">
        function showDetail(i) {
            var detailDiv=document.getElementById("detail"+i);
            var messageDiv=document.getElementById("message"+i);
            detailDiv.style.display='block';
            messageDiv.style.display='none';
        }
        function hideDetail(i) {
            var detailDiv=document.getElementById("detail"+i);
            var messageDiv=document.getElementById("message"+i);
            detailDiv.style.display='none';
            messageDiv.style.display='block';
        }
    </script>
</head>
<body>
    <%@ include file="menuBar.jsp" %>
    <input type="button" value="返回点餐" onclick="window.location.href='scanStore'">
    <%
        String tips=(String) request.getAttribute("noOrder");
        if(tips!=null){
    %>
        <h1 align="center"><%= tips %></h1><br><br>
        <input type="button" value="去逛逛" onclick="window.location.href='scanStore'">

    <%
        }else {
            List<Order> orderList=(List<Order>) request.getAttribute("orderList");
            int i=0;
            for (Order order:orderList) {
    %>
    <div align="center" id="message<%=i%>">
        <table>
            <tr>
                <th>商家：</th>
                <th><img src="pics/<%= order.getStore().getPic() %>" width="100px"></th>
                <th><%= order.getStore().getName()%></th>
                <th></th>
            </tr>
            <%
                List<OrderItem> orderItemList=order.getOrderItemList();
                for (OrderItem orderItem:orderItemList){
            %>
            <tr>
                <th>商品名称：</th>
                <th><%= orderItem.getFood().getName() %></th>
                &nbsp;&nbsp;&nbsp;
                <th>数量：</th>
                <th><%= orderItem.getNumber() %></th>
            </tr>
            <%
                }
            %>
        </table><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <span>总价：<%= order.getPrice() %></span><br>
        <input type="button" value="查看订单详情" onclick="showDetail(<%=i%>)">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <%
            if(!order.getStatus().equals("已评价")){
        %>
        <input type="button" value="评价" onclick="">
        <%
            }else {
        %>
        <input type="button" value="查看评价" onclick="">
        <%
            }
        %>
        <br><br><br>
    </div>
    <div align="center" id="detail<%=i%>" style="display: none">
        <span><img src="pics/<%= order.getStore().getPic() %>" width="50px"><%=order.getStore().getName()%></span>
        <br><br>
        <table>
            <tr>
                <th>商品图片</th>
                <th>商品名称</th>
                <th>单价</th>
                <th>数量</th>
            </tr>
        <%
            List<OrderItem> orderItemList2=order.getOrderItemList();
            for (OrderItem orderItem:orderItemList2){
        %>
        <tr>
            <th><img src="pics/<%=orderItem.getFood().getPic()%>" width="75px"></th>
            <th><%= orderItem.getFood().getName() %></th>
            <th><%= orderItem.getFood().getPrice() %></th>
            <th><%= orderItem.getNumber() %></th>
        </tr>
        <%
            }
        %>
        </table><br><br>
        <span>配送地址：</span><br>
        &nbsp;&nbsp;&nbsp;<span><%= order.getReceiver().getName() %></span>
        &nbsp;&nbsp;&nbsp;<span><%= order.getReceiver().getTel() %></span>
        &nbsp;&nbsp;&nbsp;<span><%= order.getReceiver().getAddress() %></span>
        <span>合计：<%= order.getPrice() %></span><br><br>
        <span>订 单 号：<%= order.getId() %></span><br><br>
        <span>订单备注：<%= order.getMessage() %></span><br><br>
        <span>订单状态：<%= order.getStatus() %></span><br><br>
        <span>订单时间：<%= order.getDate() %></span><br><br>
        <%
            if(!order.getStatus().equals("已评价")){
        %>
        <input type="button" value="评价" onclick="">
        <%
        }else {
        %>
        <input type="button" value="查看评价" onclick="">
        <%
            }
        %>
        <br><br>
        <input type="button" value="收起详情" onclick="hideDetail(<%=i%>)">
        <br><br><br><br><br>
    </div>
    <%
                i++;
            }
        }
    %>
</body>
</html>
