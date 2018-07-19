<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>新用户注册</title>
    <script type="text/javascript">
        function passwordCheck() {
            var username=document.getElementsByName("username")[0];
            var password=document.getElementsByName("password")[0];
            var rePassword=document.getElementsByName("re_password")[0];
            var tel=document.getElementsByName("tel")[0];
            var name=document.getElementsByName("name")[0];

            if(username.value.length==0){
                alert("账号不能为空");
                return false;
            }
            if(password.value.length==0){
                alert("密码不能为空");
                return false;
            }
            if(rePassword.value.length==0){
                alert("重复密码不能为空");
                return false;
            }
            if(tel.value.length==0){
                alert("电话号码不能为空");
                return false;
            }
            if(name.value.length==0){
                alert("昵称不能为空");
                return false;
            }
            if(username.value.length<6 ||username.value.length>16){
                alert("账号长度必须在6到16之间");
                return false;
            }
            if(password.value==rePassword.value){
                if(password.value.length>5 && password.value.length<17){
                    return true;
                }else{
                    alert("密码长度必须在6到16之间");
                    return false;
                }
            }else {
                alert("密码与确认密码不一致，请重新输入");
                return false;
            }
        }
    </script>
</head>
<body>
    <h1 align="center" style="color: red">新用户注册</h1>
    <table align="center">
        <tr><td>
            <p style="color: black">以下皆为必填项,其中用户名与密码长度必须在6到16之间</p>
            <form method="post" action="../Register" onsubmit="return passwordCheck()" accept-charset="UTF-8">
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                账   号：<input type="text" name="username"><br><br>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                密   码：<input type="password" name="password"><br><br>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                确认密码：<input type="password" name="re_password"><br><br>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                电话号码：<input type="text" name="tel"><br><br>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                昵   称：<input type="text" name="name"><br><br>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="submit" value="提交">
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="reset" value="重置">
            </form>
        </td></tr>
    </table>

    <%--用户名重复或发生未知错误--%>
    <%
        String error=(String) request.getAttribute("error");
        if(error!=null){
    %>
    <script type="text/javascript">
        alert("<%=error%>>");
        <% } %>
    </script>

    <%--注册成功，跳转回登录界面--%>
    <%
        String success=(String) request.getAttribute("success");
        if(success!=null){
        session.setAttribute("registerSuccess",success);
        response.sendRedirect("login.jsp");
        }
    %>
</body>
</html>
