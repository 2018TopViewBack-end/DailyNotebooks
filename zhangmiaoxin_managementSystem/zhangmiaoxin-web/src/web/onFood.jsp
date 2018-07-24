<%@ page import="java.util.List" %>
<%@ page import="com.zhangmiaoxin.www.po.Food" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.zhangmiaoxin.www.po.Store" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script type="text/javascript">
        function refuseOn(i) {
            document.getElementById("refuseOn"+i).style.display='block';
        }
        function cancelRefuse(i) {
            document.getElementById("refuseOn"+i).style.display='none';
        }
        function inspect(i) {
            var reason=document.getElementsByName("reason"+i)[0].value;
            if(reason.length<1){
                alert("审核不通过的理由不能为空");
            }else if(reason.length>50){
                alert("审核不通过的理由最大长度为50字");
            }
        }
    </script>
    <title>待审核的菜品</title>
</head>
<body>
<%@ include file="menuBar.jsp" %>
    <%
        String noFood=(String) request.getAttribute("noFood");
        if(noFood!=null){
    %>
    <div align="center">
        <h3 style="color: dodgerblue"><%= noFood %></h3>
        <input type="button" value="点击此处返回点餐主界面" onclick="window.location.href='scanStore'">
    </div>
    <%
        }else {
            List<List<Food>> storeOnFoodList= (List<List<Food>>) request.getAttribute("storeOnFoodList");
            List<Store> storeList=(List<Store>)request.getAttribute("storeList");
            for (int i = 0; i < storeList.size(); i++) {
                Store store=storeList.get(i);
                List<Food> foodList=storeOnFoodList.get(i);
    %>
    <div id="<%=store.getId()%>" align="center">
        <img src="pics/<%=store.getPic()%>" width="50px">
        <h5><%=store.getName()%></h5>
        商家电话:&nbsp;<%=store.getTel()%>&nbsp;&nbsp;&nbsp;地址:&nbsp;<%=store.getAddress()%><br>
        商家分类:&nbsp;<%=store.getCategory()%>&nbsp;&nbsp;&nbsp;销量:&nbsp;<%=store.getSales()%>
    </div><br>
    <table id="onFood<%=store.getId()%>" align="center">
        <tr>
            <th>菜品编号</th>
            <th>菜品参考图</th>
            <th>菜品名称</th>
            <th>菜品定价</th>
            <th>菜品描述</th>
            <th></th>
            <th></th>
            <th></th>
        </tr>

    <%
            for (Food food:foodList) {
    %>
        <tr>
            <th><%=food.getId()%></th>
            <th><img src="pics/<%=food.getPic()%>" width="75px"></th>
            <th><%=food.getName()%></th>
            <th><%=food.getPrice()%></th>
            <th><%=food.getDescription()%></th>
            <th><input type="button" value="审核通过" onclick="window.location.href='AllowOnFood?foodId=<%=food.getId()%>'"></th>
            <th><input type="button" value="审核不通过" onclick="refuseOn(<%=food.getId()%>)"></th>
            <th id="refuseOn<%=food.getId()%>" style="display: none">
                <form action="RefuseOnFood?foodId=<%=food.getId()%>" onsubmit="inspect(<%=food.getId()%>)">
                    <textarea rows="5" cols="20" name="reason<%=food.getId()%>"
                              placeholder="请在此输入审核不通过的原因（必填,50字以内），以便商家整改" ></textarea><br>
                    <input type="submit" value="提交">&nbsp;&nbsp;&nbsp;
                    <input type="reset" value="重置">&nbsp;&nbsp;&nbsp;
                    <input type="button" value="取消" onclick="cancelRefuse(<%=food.getId()%>)">
                </form>
            </th>
        </tr>

    <%
            }
    %>
    </table>
    <%
            }
    %>

    <%
        }
    %>
    <%--审核消息弹窗--%>
    <%
        String message=(String)request.getAttribute("message");
        if(message!=null){
    %>
        <script type="text/javascript">
            alert("<%=message%>");
        </script>
    <%
        }
    %>
</body>
</html>
