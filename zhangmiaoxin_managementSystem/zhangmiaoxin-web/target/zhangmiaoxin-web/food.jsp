<%@ page import="java.util.List" %>
<%@ page import="com.zhangmiaoxin.www.po.Food" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%
        String storeName=(String)request.getAttribute("storeName");
        if(storeName!=null){
    %>
    <title><%=storeName%></title>
    <%
        }else {
    %>
    <title>该店铺不存在</title>
    <%
        }
    %>
</head>
<body>
    <%@ include file="menuBar.jsp" %>
    <%
        String notExist=(String)request.getAttribute("null");
        if(notExist!=null){
    %>
    <div align="center">
        <h3 align="center" style="color: red"><%=notExist%></h3>
        <input type="button" value="返回浏览店铺" onclick="document.location.href='scanStore'">
    </div>
    <%
        }else {
    %>
    <h1 align="center" style="color: orange"><%= storeName%></h1>
    <table align="center" width="80%">
        <tr>
            <th><b>参考图</b></th>
            <th><b>名称</b></th>
            <th><b>价格</b></th>
            <th><b>销量</b></th>
            <th><b>库存</b></th>
        </tr>
        <%
            List<Food> foodList=(List<Food>) request.getAttribute("foodList");
            if(foodList!=null){
                for (Food food : foodList) {
        %>
        <tr>
            <th><img src="pics/<%= food.getPic() %>" width="100px"></th>
            <th><%= food.getName() %>
            </th>
            <th><%= food.getPrice() %>
            </th>
            <th><%= food.getSales() %>
            </th>
            <th><%= food.getStock() %>
            </th>
            <th><input type="button" value="查看详情" onclick="alert('<%= food.getDescription() %>')"></th>
            <th><input type="button" value="我要购买" onclick="document.location.href='Pay?' +
                    'foodName=<%=food.getName()%>&foodPrice=<%=food.getPrice()%>&foodId=<%= food.getId() %>'+
                    '&storeId=<%=food.getStoreId()%>&storeName=<%= storeName%>&payWay=direct'">
            </th>
            <th><input type="button" value="加入购物车" onclick="window.location.href='AddCart?'+
                    'storeId=<%=food.getStoreId()%>&storeName=<%=storeName%>&foodId=<%= food.getId() %>&'+
                    'foodName=<%=food.getName()%>&foodPrice=<%=food.getPrice()%>&pic=<%=food.getPic()%>'"></th>
        </tr>
        <%
            }
        %>
    </table>
        <%
            }else {
        %>
        <div align="center">
            <h3 align="center" style="color: coral">啊哦，这个店铺还没有可以购买的菜品，返回去别的店铺看看吧</h3>
            <input type="button" value="返回浏览店铺" onclick="document.location.href='scanStore'">
        </div>
        <%
                }
            }
        %>

    <%--加入购物车成功弹窗--%>
    <%
        String success=(String)request.getAttribute("success");
        if(success!=null){
    %>
    <script type="text/javascript">
        alert("<%=success%>");
    </script>
    <%
        request.removeAttribute("success");
        }
    %>
</body>
</html>
