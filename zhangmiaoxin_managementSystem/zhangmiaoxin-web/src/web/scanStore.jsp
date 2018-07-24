<%@ page import="java.util.List" %>
<%@ page import="com.zhangmiaoxin.www.po.Store" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>附近的店</title>
</head>
<body>
    <%@ include file="menuBar.jsp" %>
    <h1 align="center" style="color: lightcoral">看看附近有啥好店</h1>
    <table align="center" width="80%">
        <tr>
            <th>店面图片</th>
            <th>店铺名称</th>
            <th>电话</th>
            <th>地址</th>
            <th>经营类别</th>
            <th>月销量</th>
        </tr>
        <%
            List<Store> storeList=(List<Store>) request.getAttribute("allStoreList");
            if(storeList!=null){
                for (Store store:storeList) {
        %>
            <tr>
                <th><img src="pics/<%= store.getPic() %>" width="150px"></th>
                <th><%= store.getName() %></th>
                <th><%= store.getTel() %></th>
                <th><%= store.getAddress() %></th>
                <th><%= store.getCategory() %></th>
                <th><%= store.getSales() %></th>
                <th><input type="button" value="进入店铺"
                           onclick="document.location.href='Food?storeId=<%= store.getId()%>&storeName=<%=store.getName()%>'"></th>
            </tr>
        <%
                }
            }
        %>
    </table>

    <%--申请开店成功弹窗，跳转到这个界面--%>
    <%
        String message=(String)request.getAttribute("message");
        if(message!=null){
    %>
    <script type="text/javascript">
        alert("<%=message%>");
        document.getElementById("applyStore").style.display='none';
    </script>
    <%
        }
    %>

    <%--加入购物车失败弹窗--%>
    <%
        String error=(String)request.getAttribute("error");
        if(error!=null){
    %>
    <script type="text/javascript">
        alert("<%=error%>");
    </script>
    <%
        }
    %>
</body>
</html>
