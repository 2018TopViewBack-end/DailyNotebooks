<%@ page import="java.util.List" %>
<%@ page import="com.zhangmiaoxin.www.po.Receiver" %>
<%@ page import="com.zhangmiaoxin.www.po.Food" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style>
        #addressList{
            width: 500px;
            height: auto;
            border: 2px #33bb62 solid;
            z-index: 2000;
            margin: 10px auto;
        }
        #selectRow{
            width: 350px;
            height: 50px;
            z-index: 2000;
            margin: 10px auto;
            display: none;
        }
    </style>

    <title>支付</title>
    <script type="text/javascript">
        function selectAddress() {
            var addressList=document.getElementById("addressList");
            addressList.style.display='block';
        }
        function hideDiv(i,id) {
            var addressList=document.getElementById("addressList");
            var selectAddressButton=document.getElementById("selectAddressButton");
            var selectRow=document.getElementById("selectRow");

            addressList.style.display='none';
            selectAddressButton.style.display='none';

            var addressTable=document.getElementById("addressTable");
            var tr=addressTable.getElementsByTagName("tr")[i+1];
            var ths=tr.getElementsByTagName("th");

            document.getElementById("selectReceiver").value=id;
            document.getElementById("selectName").innerHTML=ths[0].innerHTML;
            document.getElementById("selectTel").innerHTML=ths[1].innerHTML;
            document.getElementById("selectAddress").innerHTML=ths[2].innerHTML;
            selectRow.style.display='block';

            document.getElementById("orderDiv").style.display='block';
        }
        function addAddress() {
            document.getElementById("addressList").style.display='none';

            var editFormTable=document.getElementById("editFormTable");
            editFormTable.style.display='block';
        }
        function editAddress(i,id) {
            var addressTable=document.getElementById("addressTable");
            var tr=addressTable.getElementsByTagName("tr")[i+1];
            var ths=tr.getElementsByTagName("th");

            document.getElementById("editName").value=ths[0].innerHTML;
            document.getElementById("editTel").value=ths[1].innerHTML;
            document.getElementById("editAddress").value=ths[2].innerHTML;

            document.getElementById("addressList").style.display='none';
            document.getElementById("chooseAddress").style.display='none';

            var editFormTable=document.getElementById("editFormTable");
            editFormTable.style.display='block';

            var receiverId=document.getElementById("receiverId");
            receiverId.value=id;
        }
        function saveAddress() {
            var name=document.getElementById("editName");
            var tel=document.getElementById("editTel");
            var address=document.getElementById("editAddress");

            if(name.value.length<1){
                alert("收货人姓名不能为空");
                return false;
            }
            if(tel.value.length<1){
                alert("电话不能为空");
                return false;
            }
            if(address.value.length<1){
                alert("收货地址不能为空");
                return false;
            }
            document.getElementById("editFormTable").style.display='none';
            return true;
        }
        function cancelEdit() {
            document.getElementById("editName").value="";
            document.getElementById("editTel").value="";
            document.getElementById("editAddress").value="";
            document.getElementById("receiverId").value=null;

            document.getElementById("editFormTable").style.display='none';
            document.getElementById("addressList").style.display='block';
        }
        function reSelectReceiver() {
            document.getElementById("selectRow").style.display='none';
            document.getElementById("addressList").style.display='block';

            document.getElementById("orderDiv").style.display='none';
        }
        function addFoodNum() {
            var foodNum = parseInt(document.getElementById("foodNum").value);
            if (foodNum != 99) {
                document.getElementById("foodNum").value = String(foodNum + 1);
                smallCount(foodNum+1);
            }
        }
        function minusFoodNum() {
            var foodNum = parseInt(document.getElementById("foodNum").value);
            if (foodNum != 1) {
                document.getElementById("foodNum").value=String(foodNum-1);
                smallCount(foodNum-1);
            }
        }
        function smallCount(foodNum) {
            var smallCount = document.getElementById("smallCount");
            var foodPrice=parseFloat(document.getElementsByName("foodPrice")[0].value);
            smallCount.innerHTML = parseFloat(foodNum*foodPrice);
        }
        function confirmOrder() {
            var receiverId=parseInt(document.getElementById("selectReceiver").value);
            var receiverName=document.getElementById("selectName").innerHTML;
            var receiverTel=document.getElementById("selectTel").innerHTML;
            var receiverAddress=document.getElementById("selectAddress").innerHTML;
            var message=document.getElementById("message").value;
            var price=parseFloat(document.getElementById("smallCount").innerHTML);
            var storeId=parseInt(document.getElementsByName("storeId")[0].value);
            var foodId=parseInt(document.getElementsByName("foodId")[0].value);
            var foodName=document.getElementsByName("foodName")[0].value;
            var foodPrice=parseFloat(document.getElementsByName("foodPrice")[0].value);
            var number=parseInt(document.getElementById("foodNum").value);
            var storeName=document.getElementById("storeName").innerHTML;

            window.location.href="Order?receiverId="+receiverId+"&receiverName="+receiverName+"&receiverTel="+receiverTel+
                "&receiverAddress="+receiverAddress+"&message="+message+"&price="+price+"&storeId="+storeId+
            "&foodId="+foodId+"&number="+number+"&storeName="+storeName+"&foodName="+foodName+"&foodPrice="+foodPrice;
        }
    </script>
</head>
<body>
    <%@ include file="menuBar.jsp" %>
    <h1 align="center" style="color: red">支付</h1>
    <div align="center" id="chooseAddress">
        <span style="margin: 0 auto">您的收货地址：</span>
        <input type="button" align="center" value="选择收货地址" id="selectAddressButton" onclick="selectAddress()">
    </div>

    <%--先隐藏这个选择地址的框，点击按钮后再显示--%>
    <div id="addressList" style="display: none" align="center">
        <table align="center" id="addressTable">
            <tr>
                <th><b>收货人姓名</b></th>
                <th><b>电话</b></th>
                <th><b>地址</b></th>
            </tr>
            <%
                List<Receiver> receiverList=(List<Receiver>) request.getAttribute("receiverList");
                if(receiverList!=null) {
                    for (int i=0;i<receiverList.size();i++) {
                        Receiver receiver=receiverList.get(i);
            %>
            <tr>
                <th><%= receiver.getName() %></th>
                <th><%= receiver.getTel() %></th>
                <th><%= receiver.getAddress() %></th>
                <th><input type="button" value="选择" onclick="hideDiv(<%= i %>,<%=receiver.getId()%>)"></th>
                <th><input type="button" value="编辑" onclick="editAddress(<%= i %>,<%=receiver.getId()%>)"></th>
            </tr>
            <%
                    }
                }
            %>
        </table>
        <br>
        <input type="button" value="添加收货地址" align="center" onclick="addAddress()">
    </div>

    <%--这是选择后的地址表格--%>
    <table id="selectRow">
        <tr>
            <th><b>收货人姓名</b></th>
            <th><b>电话</b></th>
            <th><b>收货地址</b></th>
        </tr>
        <tr>
            <input type="hidden" id="selectReceiver">
            <th id="selectName"></th>
            <th id="selectTel"></th>
            <th id="selectAddress"></th>
            <th><input type="button" id="reSelectReceiver" value="重新选择" onclick="reSelectReceiver()"></th>
        </tr>
    </table>

    <%--编辑和新增地址弹出框--%>
    <div align="center" id="editFormTable" style="display: none">
        <form id="editForm" action="Receiver" onsubmit="return saveAddress()" method="post">
            <input type="hidden"  id="receiverId" name="receiverId">
            收货人姓名：<input type="text" id="editName" name="editName"><br>
            电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;话：
            <input type="text" id="editTel" name="editTel"><br>
            地&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;址：
            <input type="text" id="editAddress" name="editAddress"><br>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="button" value="取消" onclick="cancelEdit()">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="submit" value="保存">
        </form>
    </div>

    <%--选择完收货地址后显示的订单块--%>
    <div id="orderDiv" align="center" style="display: none">
        <br><br><br><br><br>
        <table id="orderTable">
            <tr>
                <th id="storeName">商家名称：<%=request.getAttribute("storeName")%></th>
            </tr>
            <tr>
                <th>商品名称</th>
                <th>单价</th>
                <th>数量</th>
                <th>小计</th>
            </tr>
            <%
                List<Food> foodList=(List<Food>) request.getAttribute("foodList");
                if (foodList!=null){
                    for (Food food:foodList) {
            %>
            <input type="hidden" name="storeId" value="<%= food.getStoreId()%>">
            <input type="hidden" name="foodId" value="<%= food.getId() %>">
            <input type="hidden" name="foodName" value="<%= food.getName() %>">
            <input type="hidden" name="foodPrice" value="<%= food.getPrice() %>">
            <tr>
                <th ><%= food.getName() %></th>
                <th ><%= food.getPrice() %></th>
                <th><input type="button" value="-" onclick="minusFoodNum()">
                    <input type="text" id="foodNum" value="1">
                    <input type="button" value="+" onclick="addFoodNum()">
                </th>
                <th id="smallCount"><%= food.getPrice() %></th>
            </tr>
            <%
                    }
                }
            %>
        </table>
        <br>
        给商家的备注：<input type="text" id="message" value="">
        <br><br>
        <input type="button" value="确认订单" onclick="confirmOrder()">
    </div>

    <%--修改或新增收货信息后的提示弹窗,并重新跳转到PayServlet进行修改后收货信息的查询--%>
    <%  String editResult=(String)request.getAttribute("editResult");
        String addResult=(String)request.getAttribute("addResult");

        if(editResult!=null){
    %>
    <script type="text/javascript">
        alert("<%= editResult %>");
        window.location.href='Pay';
    </script>
    <%
        }else if(addResult!=null){
    %>
    <script type="text/javascript">
        alert("<%= addResult %>");
        window.location.href='Pay';
    </script>
    <%
        }
    %>

    <%--当绕过前端检测想数据库插入收件人信息有任何一项为空的数据时，返回登录界面--%>
    <%
        String notNull=(String)request.getAttribute("notNull");
        if(notNull!=null){
    %>
    <script type="text/javascript">
        alert("<%= notNull %>");
        window.location.href='Login';
    </script>
    <%
        }
    %>
</body>
</html>
