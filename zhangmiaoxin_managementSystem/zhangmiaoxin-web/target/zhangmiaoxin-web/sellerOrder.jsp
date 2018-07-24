<%@ page import="java.util.List" %>
<%@ page import="com.zhangmiaoxin.www.po.Order" %>
<%@ page import="com.zhangmiaoxin.www.po.OrderItem" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.zhangmiaoxin.www.po.Store" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>我的店铺订单</title>
    <script type="text/javascript">
        function showNewOrder() {
            document.getElementById("newOrderDiv").style.display='block';
            document.getElementById("oldOrderDiv").style.display='none';
        }
        function showOldOrder() {
            document.getElementById("newOrderDiv").style.display='none';
            document.getElementById("oldOrderDiv").style.display='block';
        }
    </script>
</head>
<body>
    <%@ include file="menuBar.jsp" %>
    <input type="button" value="返回点餐" onclick="window.location.href='scanStore'">
    <%
        Store myStore=(Store)session.getAttribute("myStore");
        String tips=(String) request.getAttribute("storeNoOrder");
    %>
    <div align="center">
        <img src="pics/<%= myStore.getPic() %>" width="50px"><%=myStore.getName()%><br><br>
        待处理订单<input type="radio" name="orderCategory" checked onclick="showNewOrder()">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        已处理订单<input type="radio" name="orderCategory" onclick="showOldOrder()">
        <br><br><br>
    </div>
    <%
        if(tips!=null){
    %>
    <div align="center">
        <h1 align="center"><%= tips %></h1><br><br>
        <input type="button" value="去逛逛" onclick="window.location.href='scanStore'">
    </div>
    <%
    }
    else {
        List<List<Order>> orderList=(List<List<Order>>) request.getAttribute("storeOrderList");
        List<Order> newOrderList=new ArrayList<>();
        List<Order> oldOrderList=new ArrayList<>();
        if(orderList.get(0)!=null && orderList.get(0).size()!=0){
            newOrderList=orderList.get(0);
        }
        if(orderList.get(1)!=null &&orderList.get(1).size()!=0){
            oldOrderList=orderList.get(1);
        }
    %>
        <div align="center" id="newOrderDiv" style="display: block">
        <%
            for (Order order:newOrderList) {
        %>
            <span>订单号：<%=order.getId()%></span><br><br>
            <table>
                <tr>
                    <th>商品图片</th>
                    <th>商品名称</th>
                    <th>单价</th>
                    <th>数量</th>
                </tr>
                <%
                    List<OrderItem> orderItemList=order.getOrderItemList();
                    for (OrderItem orderItem:orderItemList){
                %>
                    <tr>
                        <th><img src="pics/<%= orderItem.getFood().getPic() %>" width="50px"></th>
                        <th><%= orderItem.getFood().getName() %></th>
                        <th><%= orderItem.getFood().getPrice() %></th>
                        <th><%= orderItem.getNumber() %></th>
                    </tr>
                <%
                    }
                %>
            </table>
            <br>
            <div align="center">
                总价：<%= order.getPrice() %><br>
                <span>配送地址：</span><br>
                <span><%= order.getReceiver().getName() %></span><br>
                <span><%= order.getReceiver().getTel() %></span><br>
                <span><%= order.getReceiver().getAddress() %></span><br>
                <span>订单备注：<%= order.getMessage() %></span><br><br>
                <span>订单时间：<%= order.getDate() %></span><br><br>
                <input type="button" value="接单" onclick="window.location.href='AcceptOrder?orderId=<%=order.getId()%>'">
                &nbsp;&nbsp;&nbsp;&nbsp;
                <input type="button" value="拒绝此订单">
            </div>
            <br>
        <%
                }
        %>
        </div>


        <div align="center" id="oldOrderDiv" style="display: none">
        <%
            for (Order order:oldOrderList) {
        %>
            <span>订单号：<%=order.getId()%></span><br><br>
            <table>
                <tr>
                    <th>商品图片</th>
                    <th>商品名称</th>
                    <th>单价</th>
                    <th>数量</th>
                </tr>
                <%
                    List<OrderItem> orderItemList=order.getOrderItemList();
                    for (OrderItem orderItem:orderItemList){
                %>
                <tr>
                    <th><img src="pics/<%= orderItem.getFood().getPic() %>" width="50px"></th>
                    <th><%= orderItem.getFood().getName() %></th>
                    <th><%= orderItem.getFood().getPrice() %></th>
                    <th><%= orderItem.getNumber() %></th>
                </tr>
                <%
                    }
                %>
            </table>
            <br>
            <div align="center">
                总价：<%= order.getPrice() %><br>
                <span>配送地址：</span><br>
                <span><%= order.getReceiver().getName() %></span><br>
                <span><%= order.getReceiver().getTel() %></span><br>
                <span><%= order.getReceiver().getAddress() %></span><br>
                <span>订单备注：<%= order.getMessage() %></span><br><br>
                <span>订单时间：<%= order.getDate() %></span><br><br>
            </div>
            <br>
            <%
                if(order.getStatus().equals("已评价")){
            %>
            <input type="button" value="查看评价" onclick="">
            <%
                }
            %>
            <br><br><br>

        <br><br>
        <%
            }
        %>
        </div>
    <%
        }
    %>

    <%--接单消息弹窗--%>
    <%
        String orderMessage=(String) request.getAttribute("orderMessage");
        if(orderMessage!=null){
    %>
    <script type="text/javascript">
        alert("<%= orderMessage %>")
    </script>
    <%
        }
    %>
</body>
</html>
