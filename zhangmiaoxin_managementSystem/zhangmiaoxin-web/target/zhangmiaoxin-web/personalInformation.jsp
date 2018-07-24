<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>个人信息</title>
    <script type="text/javascript">
        function editPersonalInformation() {
            document.getElementById("editPassword").style.display='none';
            document.getElementById("informationTable").style.display='none';

            var name=document.getElementsByName("reName")[0];
            var tel=document.getElementsByName("reTel")[0];
            name.value=document.getElementById("name").innerHTML;
            tel.value=document.getElementById("tel").innerHTML;

            document.getElementById("editDiv").style.display='block';
        }
        function savePersonalInformation() {
            var newName=document.getElementsByName("reName")[0].value;
            var newTel=document.getElementsByName("reTel")[0].value;

            if(newName.length<1){
                alert("昵称不能为空");
                return false;
            }
            if(newTel.length<1){
                alert("绑定电话不能为空");
                return false;
            }
            document.getElementById("editDiv").style.display='none';
            return true;
        }
        function cancelEdit() {
            document.getElementsByName("reName")[0].value="";
            document.getElementsByName("reTel")[0].value="";
        }
        function editPassword() {
            document.getElementsByName("oldPassword")[0].value="";

            document.getElementById("editDiv").style.display='none';
            document.getElementById("informationTable").style.display='none';
            document.getElementById("editPassword").style.display='block';
        }
        function updatePassword() {
            var oldPassword=document.getElementsByName("oldPassword")[0].value;
            var newPassword=document.getElementsByName("newPassword")[0].value;
            var reNewPassword=document.getElementsByName("reNewPassword")[0].value;

            if(oldPassword.length<1){
                alert("原密码不能为空");
                return false;
            }
            if(newPassword.length<1){
                alert("新密码不能为空");
                return false;
            }
            if(newPassword==reNewPassword){
                if(newPassword.length>5 && newPassword.length<17){
                    return true;
                }else {
                    alert("新密码的长度必须在6到16之间");
                    return false;
                }
            }else {
                alert("新密码与确认新密码不一致，请重新输入");
                return false;
            }
        }
        function cancelUpdatePassword() {
            document.getElementsByName("oldPassword")[0].value="";
            document.getElementsByName("newPassword")[0].value="";
            document.getElementsByName("reNewPassword")[0].value="";

            document.getElementById("informationTable").style.display='block';
            document.getElementById("editPassword").style.display='none';
        }
    </script>
</head>
<body>
    <%@ include file="menuBar.jsp" %>
    <h1 align="center" style="color: blue">个人信息</h1>
    <div align="center">
        <input type="button" value="编辑个人信息" onclick="editPersonalInformation()">&nbsp;&nbsp;&nbsp;
        <%
            if(user.getRoleId()==1){
        %>
        <input type="button" value="修改密码" onclick="editPassword()">&nbsp;&nbsp;&nbsp;
        <%
            }
        %>
        <input type="button" value="返回点餐" onclick="window.location.href='scanStore'">
    </div>
    <br><br><br>
    <table id="informationTable" align="center">
        <tr>
            <th>头像：</th>
            <th id="picture"><img src="pics/<%= user.getPic() %>" width="100px"></th>
        </tr>
        <tr>
            <th>账号：</th>
            <th id="username"><%= user.getUsername() %></th>
        </tr>
        <tr>
            <th>昵称：</th>
            <th id="name"><%= user.getName() %></th>
        </tr>
        <tr>
            <th>绑定电话：</th>
            <th id="tel"><%= user.getTel()%></th>
        </tr>
        <tr>
            <th>身份：</th>
            <th id="role"><%= user.getRole() %></th>
        </tr>
    </table>

    <div id="editDiv" style="display: none" align="center">
        <form action="User" onsubmit="return savePersonalInformation()" method="post" enctype="multipart/form-data">
            头像：&nbsp;<img src="pics/<%= user.getPic() %>" width="100px">&nbsp;&nbsp;&nbsp;
            <input type="file" accept="image/*"  value="更改头像" name="pic" ><br><br>
            昵称：<input type="text" name="reName"><br><br>
            绑定电话：<input type="text" name="reTel"><br><br>
            <input type="submit" value="保存" >&nbsp;&nbsp;&nbsp;
            <input type="button" value="取消" onclick="cancelEdit()">
        </form>
    </div>

    <div id="editPassword" style="display: none" align="center">
        <form action="UpdatePassword" onsubmit="return updatePassword()" method="post">
            原  密  码：<input type="password" name="oldPassword"><br><br>
            新  密  码：<input type="password" name="newPassword"><br><br>
            确认新密码：<input type="password" name="reNewPassword"><br><br>
            <input type="submit" value="保存" >&nbsp;&nbsp;&nbsp;
            <input type="button" value="取消" onclick="cancelUpdatePassword()">
        </form>
    </div>

    <%--看是否修改过个人信息，弹窗--%>
    <%
        if(request.getAttribute("updateMessage")!=null){
    %>
    <script type="text/javascript">
        alert("<%= (String) request.getAttribute("updateMessage") %>")
    </script>
    <%
        }
    %>

    <%--看是否修改过密码，弹窗--%>
    <%
        String result=(String) request.getAttribute("passwordMessage");
        if(result!=null){
    %>
    <script type="text/javascript">
        alert("<%=  result %>")
    </script>
    <%
            if (result.equals("原密码错误，请重新输入")){
    %>
    <script type="text/javascript">
        document.getElementById("informationTable").style.display='none';
        document.getElementById("editPassword").style.display='block';

        document.getElementsByName("oldPassword")[0].value="";
        document.getElementsByName("newPassword")[0].value="";
        document.getElementsByName("reNewPassword")[0].value="";
    </script>
    <%
            }
        }
    %>
</body>
</html>
