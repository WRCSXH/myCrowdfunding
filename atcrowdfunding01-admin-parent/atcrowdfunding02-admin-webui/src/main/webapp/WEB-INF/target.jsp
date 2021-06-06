<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>
    <title>结果页面</title>
</head>
<body>
    <h1>结果页面</h1>
    ${adminList}
</body>
</html>
