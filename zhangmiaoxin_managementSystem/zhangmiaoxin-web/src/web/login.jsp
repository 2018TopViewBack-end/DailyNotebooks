<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>饿了就快点登录吧</title>
      <script type="text/javascript">
          function validate() {
              var username=document.getElementsByName("username")[0];
              var password=document.getElementsByName("password")[0];

              if(username.value.length<1){
                  alert("账号不能为空");
                  return false;
              }
              if(password.value.length<1){
                  alert("密码不能为空");
                  return false;
              }
              return true;
          }
      </script>
  </head>
  <body>
  <h1 align="center" style="color: darkblue">饿了就快点登录吧</h1>
    <table align="center">
      <tr><td>
          <form method="post" action="Login" onsubmit="return validate()" accept-charset="UTF-8">
            账 号 ：<input type="text" name="username"><br><br>
            密 码 ：<input type="password" name="password"><br><br>
            身 份 ：<select name="role">
                      <option value="1">普通用户</option>
                      <option value="2">商家</option>
                      <option value="3">管理员</option>
                  </select><br><br>
            &nbsp;&nbsp;&nbsp;<input type="submit" value="登录">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="reset" value="重置"><br><br>&nbsp;
            <span style="color: deepskyblue">没有账号?</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="button" value="注册" onclick="window.location.href='register.jsp'"><br><br>&nbsp;
            <span style="color: deepskyblue">忘记密码?</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="button" value="找回">
          </form>
      </td></tr>
    </table>

      <%--账号或密码错误的弹窗--%>
      <%
          String error=(String) request.getAttribute("error");
          if(error!=null){
      %>
      <script type="text/javascript">
          alert("<%=error%>>");
      <% } %>
      </script>

      <%--注册成功后的登录提示弹窗--%>
      <%
          String registerSuccess=(String) request.getSession().getAttribute("registerSuccess");
          if(registerSuccess!=null){
      %>
      <script type="text/javascript">
          alert("<%= registerSuccess %>>");
      </script>
      <%
          session.removeAttribute("registerSuccess");}
      %>
  </body>
</html>
