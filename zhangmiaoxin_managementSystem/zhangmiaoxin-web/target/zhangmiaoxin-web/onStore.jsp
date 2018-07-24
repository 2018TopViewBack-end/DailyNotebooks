<%@ page import="java.util.List" %>
<%@ page import="com.zhangmiaoxin.www.po.Store" %>
<%@ page import="com.zhangmiaoxin.www.po.User" %>
<%@ page import="java.util.Iterator" %>
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
    <title>待审核的店铺</title>
</head>
<body>
    <%@ include file="menuBar.jsp" %>
    <%
        String noStore=(String) request.getAttribute("noStore");
        String error=(String) request.getAttribute("error");
        if(noStore!=null){
    %>
    <div align="center">
        <h3 style="color: dodgerblue"><%= noStore %></h3>
        <input type="button" value="点击此处返回点餐主界面" onclick="window.location.href='scanStore'">
    </div>
    <%
        }else if(error!=null){
    %>
    <div align="center">
        <h3 style="color: dodgerblue"><%= error %></h3>
        <input type="button" value="点击此处返回点餐主界面" onclick="window.location.href='scanStore'">
    </div>
    <%
        } else{
            List<Store> storeList=(List<Store>)request.getAttribute("storeList");
            List<User> userList=(List<User>) request.getAttribute("userList");
    %>
    <h1 align="center" style="color: coral">待审核的店铺</h1>
    <table  align="center">
        <tr>
            <th>店铺id</th>
            <th>店铺图片</th>
            <th>店铺名称</th>
            <th>店铺电话</th>
            <th>店铺分类</th>
            <th>申请用户id</th>
            <th>申请用户账号</th>
            <th>申请用户电话</th>
            <th>申请用户昵称</th>
            <th></th>
            <th></th>
            <th></th>
        </tr>
    <%
        Iterator<Store> storeIterator=storeList.iterator();
        Iterator<User> userIterator=userList.iterator();
        while (storeIterator.hasNext()&&userIterator.hasNext()) {
            Store store=storeIterator.next();
            User owner=userIterator.next();
    %>
    <tr>
        <th><%=store.getId()%></th>
        <th><img src="pics/<%=store.getPic()%>" width="150px"></th>
        <th><%=store.getName()%></th>
        <th><%=store.getTel()%></th>
        <th><%=store.getCategory()%></th>
        <th><%=owner.getId()%></th>
        <th><%=owner.getUsername()%></th>
        <th><%=owner.getTel()%></th>
        <th><%=owner.getName()%></th>
        <th><input type="button" value="审核通过"
                   onclick="window.location.href='AllowOnStore?storeId=<%=store.getId()%>&userId=<%=owner.getId()%>'"></th>
        <th><input type="button" value="审核不通过" onclick="refuseOn(<%=store.getId()%>)"></th>
        <th id="refuseOn<%=store.getId()%>" style="display: none">
            <form action="" onsubmit="inspect(<%=store.getId()%>)">
                    <textarea rows="5" cols="20" name="reason<%=store.getId()%>"
                              placeholder="请在此输入审核不通过的原因（必填,50字以内），以便商家整改" ></textarea><br>
                <input type="submit" value="提交">&nbsp;&nbsp;&nbsp;
                <input type="reset" value="重置">&nbsp;&nbsp;&nbsp;
                <input type="button" value="取消" onclick="cancelRefuse(<%=store.getId()%>)">
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
