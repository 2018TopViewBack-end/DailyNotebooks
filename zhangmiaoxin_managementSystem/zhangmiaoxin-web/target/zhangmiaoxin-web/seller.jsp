<%@ page import="com.zhangmiaoxin.www.po.Food" %>
<%@ page import="java.util.List" %>
<%@ page import="com.zhangmiaoxin.www.po.Store" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>我的店铺</title>
    <script type="text/javascript">
        function newFood() {
            document.getElementById("addDish").style.display='block';
        }
        function newFoodCheck() {
            var dishName=document.getElementsByName("dishName")[0].value;
            var disPrice=document.getElementsByName("dishPrice")[0].value;
            var dishDes=document.getElementsByName("dishDes")[0].value;
            if(dishName.length<1){
                alert("菜品名称不能为空");
                return false;
            }
            if(disPrice.length<1){
                alert("菜品价格不能为空");
                return false;
            }
            if(parseFloat(disPrice)<0){
                alert("菜品价格必须为正数");
                return false;
            }
            if(dishDes.length<1){
                dishDes="";
            }else if(dishDes.length>200){
                alert("菜品描述字数最大限制为200");
                return false;
            }
            return true;
        }
        function cancelAddFood() {
            document.getElementById("addDish").style.display='none';
        }
        function editFoodCheck() {
            var dishName=document.getElementsByName("newName")[0].value;
            var disPrice=document.getElementsByName("newPrice")[0].value;
            var dishDes=document.getElementsByName("newDes")[0].value;
            if(dishName.length<1){
                alert("菜品名称不能为空");
                return false;
            }
            if(disPrice.length<1){
                alert("菜品价格不能为空");
                return false;
            }
            if(parseFloat(disPrice)<0){
                alert("菜品价格必须为正数");
                return false;
            }
            if(dishDes.length<1){
                dishDes="";
            }else if(dishDes.length>200){
                alert("菜品描述字数最大限制为200");
                return false;
            }
            document.getElementById("addDish").style.display='none';
            return true;
        }
        function editFood(i) {
            document.getElementById("food"+i).style.display='none';
            document.getElementById("editFood"+i).style.display='block';
        }
        function cancelEditFood(i) {
            document.getElementById("food"+i).style.display='block';
            document.getElementById("editFood"+i).style.display='none';
        }
        function clearNoNum(obj){
            obj.value = obj.value.replace(/[^\d.]/g,"");  //清除“数字”和“.”以外的字符
            obj.value = obj.value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的
            obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
            obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3');//只能输入两个小数
            if(obj.value.indexOf(".")< 0 && obj.value !=""){//以上已经过滤，此处控制的是如果没有小数点，首位不能为类似于 01、02的金额
                obj.value= parseFloat(obj.value);
            }
        }
        function offFood(i) {

        }
    </script>
</head>
<body>
<%@ include file="menuBar.jsp" %>
    <%
        Store myStore=(Store)request.getAttribute("myStore");
    %>
    <h1 align="center" style="color: orange"><%= myStore.getName() %></h1>
    <%
        String noFood=(String)request.getAttribute("noFood");
        if(noFood!=null){
    %>
    <div align="center">
        <h3 style="color: red"><%= noFood %></h3>
    </div>
    <%
        }
    %>
    <div align="center" style="display: none" id="addDish">
        <h4 style="color: orange">添加新菜品</h4>
        <form action="AddFood" onsubmit="return newFoodCheck()" method="post" enctype="multipart/form-data">
            <input type="hidden" name="storeId" value="<%=myStore.getId()%>">
            菜品名称：<input type="text" name="dishName"><br><br>
            菜品图片：<input type="file" accept="image/*" name="dishPic" width="100px">
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;若不上传则采用系统默认图<br><br>
            菜品单价：<input type="text" name="dishPrice" onkeyup="clearNoNum(this)"><br><br>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;菜品单价必须为正数，小数点后最多为两位数字
            菜品描述：<textarea name="dishDes" id="dishDes" cols="30" rows="10"></textarea>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;菜品描述字数最大限制为200<br><br>
            <input type="submit" value="提交">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="reset" value="重置">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="button" value="取消" onclick="cancelAddFood()">
        </form>
    </div>

    <div align="center">
        <input type="button" value="添加新菜品" onclick="newFood()">&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="button" value="查看店铺订单" onclick="window.location.href='SellerOrder'">
        <br><br>
    </div>

    <%
        List<Food> foodList=(List<Food>) request.getAttribute("foodList");
        if(foodList!=null){
        %>

    <table align="center" width="70%">
        <tr>
            <th><b>参考图</b></th>
            <th><b>名称</b></th>
            <th><b>价格</b></th>
            <th><b>销量</b></th>
            <th><b>库存</b></th>
            <th><b>是否上架</b></th>
            <th><b>菜品详情</b></th>
        </tr>
    <%
            for (Food food:foodList) {
    %>
        <tr id="food<%=food.getId()%>">
            <%
                System.out.println(food.getPic());
            %>
            <th><img src="pics/<%= food.getPic() %>" width="150px"></th>
            <th><%= food.getName() %></th>
            <th ><%= food.getPrice() %></th>
            <%
                if(food.isUsable()){
            %>
            <th><%= food.getSales() %></th>
            <th><%= food.getStock() %></th>
            <th id="on<%=food.getId()%>">已上架</th>
            <%
                }else {
            %>
            <th></th>
            <th></th>
            <th id="on<%=food.getId()%>">未上架</th>
            <%
                }
            %>
            <th><%= food.getDescription() %></th>
            <th><input type="button" value="编辑菜品信息" onclick="editFood(<%=food.getId()%>)"></th>
            <th><input type="button" value="下架" onclick="" id="off<%=food.getId()%>"></th>
        </tr>
    <div align="center" style="display: none" id="editFood<%=food.getId()%>">
        <form action="EditFood" onsubmit="return editFoodCheck()"  method="post" enctype="multipart/form-data">
           <input type="hidden" name="oldPic" value="<%=food.getPic()%>">
            <input type="hidden" name="foodId" value="<%=food.getId()%>">
            <input type="hidden" name="usable" value="<%=food.isUsable()%>">
            菜品名称：<input type="text" name="newName" value="<%=food.getName()%>"><br><br>
            菜品图片：<img src="pics/<%= food.getPic() %>" width="150px">
            <input type="file" accept="image/*" name="pic" value="更改图片">
            &nbsp;&nbsp;若不上传则采用系统默认图<br><br>
            菜品单价：<input type="text" name="newPrice" onkeyup="clearNoNum(this)" value="<%= food.getPrice() %>">
            &nbsp;&nbsp;菜品单价必须为正数，小数点后最多为两位数字<br><br>
           <%
                if(food.isUsable()){
            %>
            菜品库存：<input type="text" name="newStock" value="<%= food.getStock()%>"><br><br>
            <%
                }
            %>
            菜品描述：<textarea name="newDes" id="newDes" cols="30" rows="10"><%=food.getDescription()%></textarea>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;菜品描述字数最大限制为200<br><br>
            <input type="submit" value="提交">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="reset" value="重置">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="button" value="取消" onclick="cancelEditFood(<%= food.getId() %>)">
        </form>
    </div>
    <%
            }
    %>
    </table>
    <%
        }
    %>

    <%--编辑失败弹窗--%>
    <%
        String editError=(String) request.getAttribute("editError");
        if(editError!=null){
            int id=Integer.parseInt(request.getParameter("foodId"));
    %>
    <script type="text/javascript">
        alert("<%= editError %>")
        document.getElementById("editFood"+<%=id%>).style.display='block';
    </script>
    <%
        }
    %>

    <%--增加失败弹窗--%>
    <%
        String addError=(String) request.getAttribute("addError");
        if(addError!=null){
    %>
    <script type="text/javascript">
        alert("<%= addError %>")
        document.getElementById("addDish").style.display='block';
    </script>
    <%
        }
    %>

    <%--消息弹窗--%>
    <%
        String message=(String) request.getAttribute("message");
        if(message!=null){
    %>
    <script type="text/javascript">
        alert("<%= message %>")
    </script>
    <%
        }
    %>
</body>
</html>