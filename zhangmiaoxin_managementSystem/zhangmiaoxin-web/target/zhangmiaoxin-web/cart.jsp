<%@ page import="java.util.List" %>
<%@ page import="com.zhangmiaoxin.www.po.Order" %>
<%@ page import="com.zhangmiaoxin.www.po.Store" %>
<%@ page import="com.zhangmiaoxin.www.po.OrderItem" %>
<%@ page import="com.zhangmiaoxin.www.po.Food" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script type="text/javascript">
        function addFoodNum(i,j) {
            var foodNum = parseInt(document.getElementById(""+i).value);
            if (foodNum != 99) {
                document.getElementById(""+i).value = String(foodNum + 1);
                var num=document.getElementById(""+i).value;
                smallCount(foodNum+1,i,j);
                window.location.href="EditOrderItem?storeId="+j+"&foodId="+i+"&num="+num;
            }
        }
        function minusFoodNum(i,j) {
            var foodNum = parseInt(document.getElementById(""+i).value);
            if (foodNum != 1) {
                document.getElementById(""+i).value=String(foodNum-1);
                var num=document.getElementById(""+i).value;
                smallCount(foodNum-1,i,j);
                window.location.href="EditOrderItem?storeId="+j+"&foodId="+i+"&num="+num;
            }
        }
        function smallCount(foodNum,i,j) {
            var smallCount = document.getElementById("smallCount"+i);
            var foodPrice=parseFloat(document.getElementById("price"+i).innerHTML);
            smallCount.innerHTML = parseFloat(foodNum*foodPrice);


            var itemPrice=document.getElementsByName("smallCount"+j);
            var orderPrice=0.0;
            for(var k=0; k<itemPrice.length; k++){
                orderPrice+=parseFloat(itemPrice[k].innerHTML);
            }
            document.getElementById("orderPrice"+j).innerHTML="总计："+orderPrice+"元";
        }
    </script>
    <title>我的购物车</title>
</head>
<body>
<%@ include file="menuBar.jsp" %>
    <%
        List<Order> cartOrderList=(List<Order>) session.getAttribute("cart");
        if(cartOrderList==null||cartOrderList.size()<1){
    %>
    <div align="center">
    <h3 align="center" style="color: lightcoral">您的购物车里还没有东西，先去逛逛店铺吧</h3>
    <input type="button" value="去逛逛" onclick="window.location.href='scanStore'">
    </div>
    <%
        }
        for (Order order:cartOrderList) {
            Store store=order.getStore();
    %>
    <div align="center">
        <input type="hidden" value="<%=store.getId()%>">
        商家名称:&nbsp;&nbsp;&nbsp;<span style="color:darkblue"><%=store.getName()%></span>
        <br><br>
    </div>
    <table align="center">
        <tr>
            <th>菜品参考图</th>
            <th>菜品名称</th>
            <th>单价</th>
            <th>数量</th>
            <th>小计</th>
            <th></th>
        </tr>
        <%
            List<OrderItem> orderItemList=order.getOrderItemList();
            double orderPrice=0.0;
            for (OrderItem orderItem:orderItemList) {
                Food food=orderItem.getFood();
        %>
        <tr>
            <th><img src="pics/<%=food.getPic()%>" width="100px"></th>
            <th><%=food.getName()%></th>
            <th id="price<%=food.getId()%>"><%=food.getPrice()%></th>
            <th><input type="button" value="-" onclick="minusFoodNum(<%=food.getId()%>,<%=store.getId()%>)">
                <input type="text" id="<%=food.getId()%>" value="<%=orderItem.getNumber()%>">
                <input type="button" value="+" onclick="addFoodNum(<%=food.getId()%>,<%=store.getId()%>)">
            </th>
            <th id="smallCount<%=food.getId()%>"
                name="smallCount<%=store.getId()%>"><%= orderItem.getNumber()*food.getPrice() %></th>
            <th><input type="button" value="删除" onclick="window.location.href='DeleteOrderItem?' +
                    'storeId=<%=store.getId()%>&foodId=<%=food.getId()%>'"></th>
        </tr>
        <%
                orderPrice+=orderItem.getNumber()*food.getPrice();
            }
        %>
    </table>
    <div align="center">
        <label id="orderPrice<%=store.getId()%>">总计：<%=orderPrice%>元</label>
        <input type="button" value="确认订单" onclick="window.location.href='Cart?storeId=<%=store.getId()%>&'">
        <br><br><br><br>
    </div>
    <%
        }
    %>

    <script type="text/javascript">
        <%
            String orderMessage=(String)request.getAttribute("Order");
            if(orderMessage!=null){
        %>
            alert("<%=orderMessage%>");
        <%
            request.removeAttribute("Order");
            }
        %>
    </script>
</body>
</html>
