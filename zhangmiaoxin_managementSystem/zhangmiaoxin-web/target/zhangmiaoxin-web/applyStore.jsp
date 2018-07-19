<%@ page import="java.util.List" %>
<%@ page import="com.zhangmiaoxin.www.po.Category" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script type="text/javascript">
        function inspect() {
            var storeName=document.getElementsByName("storeName")[0].value;
            var storeTel=document.getElementsByName("storeTel")[0].value;
            var address=document.getElementsByName("address")[0].value;
            if(storeName.length<1){
                alert("店铺名称不能为空");
            }else if(storeName.length>15){
                alert("店铺名称必须在十五字内");
            }

            if(storeTel.length<1){
                alert("店铺电话不能为空");
            }else if(storeTel.length!=11){
                alert("店铺电话必须为11位手机号");
            }
            if(address.length<1){
                alert("店铺地址不能为空");
            }
        }
    </script>
    <title>开店申请</title>
</head>
<body>
<%@ include file="menuBar.jsp" %>
    <div align="center">
        <h1 style="color: red">开店申请</h1>
        <h3 style="color: blue">注意，若申请提交成功后不要再重复提交，管理员只能看到第一次提交的申请，谢谢合作</h3>
        <form action="ApplyStore" onsubmit="inspect()" method="post" enctype="multipart/form-data">
            店铺图片：<input type="file" accept="image/*" value="上传店铺图片" name="pic"><br><br>
            店铺名称：<input type="text" name="storeName" placeholder="不能为空，十五字以内"><br><br>
            店铺电话：<input type="text" name="storeTel" placeholder="不能为空，必须为11位手机号"
                        onkeyup="value=value.replace(/[^\d]/g,'')"><br><br>
            店铺地址：<textarea rows="5" cols="20" name="address" placeholder="不能为空"></textarea><br><br>
            店铺分类：<select name="categoryId">
                    <%
                        List<Category> categoryList=(List<Category>) request.getAttribute("categoryList");
                        for (Category category: categoryList) {
                    %>
                        <option value="<%=category.getId()%>"><%=category.getName()%></option>
                    <%
                        }
                    %>
                    </select><br><br>
            <input type="submit" value="提交申请">&nbsp;&nbsp;&nbsp;
            <input type="reset" value="重置">
        </form>
    </div>

   <%--申请失败弹窗，一般是店铺分类出问题--%>
    <%
        String message=(String)request.getAttribute("message");
        if(message!=null){
    %>
    <script type="text/javascript">
        alert("<%=message%>");
    </script>
    <%
            request.removeAttribute("message");
        }
    %>
</body>
</html>
