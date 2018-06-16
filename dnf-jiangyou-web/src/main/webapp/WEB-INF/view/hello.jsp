<%--
  Created by IntelliJ IDEA.
  User: MACHUNHUI
  Date: 2018/3/8
  Time: 9:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>SendMessage</title>
    <script type="text/javascript">
        function CheckText()
        {
            alert("SendMessage Over");
        }
    </script>
</head>
<body>
    <h1>登录 SUCCESS !</h1>
    <form method="get" action="/login3">
        <input type="submit" value="SEND" onclick="return CheckText()"/>
    </form>
</body>
</html>
