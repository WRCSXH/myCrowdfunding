<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="keys" content="">
    <meta name="author" content="">
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/font-awesome.min.css">
    <link rel="stylesheet" href="css/login.css">
    <script type="text/javascript" src="jquery/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript">
        $(function () {
            $("button").click(function () {
                // 相当于浏览器中的后退按钮
                window.history.back();
            });
        });
    </script>
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <div><a class="navbar-brand" href="index.html" style="font-size:32px;">尚筹网-创意产品众筹平台</a></div>
        </div>
    </div>
</nav>

<div class="container">
    <h2 class="form-signin-heading" style="text-align: center;">
        <i class="glyphicon glyphicon-log-in"></i> 尚筹网系统错误消息：
    </h2>
    <!--
        requestScope相当于存入请求域对象的Map集合
        requestScope.exception相当于request.getAttribute("exception")
        requestScope.exception.message相当于request.getAttribute("exception").getMessage()
    -->
    <!--从请求域中取出Exception对象，默认名称是exception，再进一步访问message属性，就能够显示异常信息-->
    <!--AccessDeniedHandler不走异常映射，所以requestScope.exception是我们自己手动存入request域的一个普通错误消息-->
    <p style="font-size: 20px;color: #ff0000;text-align: center">${requestScope.crowdError}</p>
    <!--使用SpringSecurity显示错误消息-->
    <p style="font-size: 20px;color: #ff0000;text-align: center">${SPRING_SECURITY_LAST_EXCEPTION.message}</p>
    <button type="button" class="btn btn-lg btn-success btn-block" style="width: 160px;margin:30px auto 0px auto;">点我返回上一步</button>
</div>
</body>
</html>