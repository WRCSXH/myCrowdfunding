<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>
    <title>测试ssm环境</title>
    <script type="text/javascript" src="jquery/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="layer/layer.js"></script>
    <script type="text/javascript">
        $(function(){

            $("#btn5").click(function () {
                layer.msg("layer的弹框！");
            });

            $("#loginBtn").click(function(){
                $.ajax({
                    url:"test/login.json",
                    type:"post",
                    data:{
                        "name":$("#name").val(),
                        "password":$("#password").val(),
                    },
                    dataType:"json",
                    success:function(result){
                        if(result.result == "SUCCESS"){
                            alert(result.data);
                        } else if(result.result == "FAILED"){
                            alert(result.message);
                        }
                    },
                    error:function(xhr){
                        // 只有发生错误时才会执行error回调函数，比如前端期望的数据类型（json）与后端返回的数据类型（text）不一致时，
                        // 但是由于后端使用了ResultEntity之后，后端返回的数据类型一定是json，而前端期望的数据类型也统一为json，
                        // 所以不可能存在数据类型不一致的问题导致该回调函数的执行，所以在这里这个回调函数是没有用的
                        alert(xhr.responseText);
                    }
                });
            })

            $("#btn4").click(function(){
                // 准备复杂对象的json数据
                var json = {
                    "stuId":"001",
                    "stuName":"张三",
                    "address":{
                        "province":"广东",
                        "city":"深圳",
                        "street":"后瑞"
                    },
                    "subjectList":[
                        {
                            "subjectName":"JavaSE",
                            "subjectGrade":100
                        },
                        {
                            "subjectName":"SSM",
                            "subjectGrade":99
                        },
                    ],
                    "map":{
                        "k1":"v1",
                        "k2":"v2"
                    }
                }

                // 将json数据转换成json格式的字符串
                var jsonStr = JSON.stringify(json);

                // 发送ajax请求
                $.ajax({
                    url:"send/compose/object.json",
                    type:"post",
                    contentType:"application/json;charset=utf-8",
                    data:jsonStr,
                    dataType:"json",
                    success:function(result){
                        if(result.result == "SUCCESS"){
                            console.log(result);
                        } else if(result.result == "FAILED"){
                            alert(result.message);
                        }
                    },
                    error:function(xhr){
                        alert(xhr.responseText);
                    }
                });
            })

            $("#btn3").click(function(){
                // json数组
                var jsonArray = [5,8,12];
                // 在浏览器的控制台上打印json数组的长度 = 3
                console.log(jsonArray.length);
                // 将json数组转换称json格式的字符串："['5','8','12']"
                var jsonArrayStr = JSON.stringify(jsonArray);
                // 在浏览器的控制台上打印json数组字符串的长度 = 8
                console.log(jsonArrayStr.length);
                $.ajax({
                    url:"send/array/three.html",
                    type:"post", // 只能用post请求，因为只有请求体才能将json数组的字符串转换成json数组
                    data:jsonArrayStr,
                    // "data":"name=zs&age=10",
                    dataType:"text",
                    /*
                        注意：
                        当ajax请求的contentType属性设置为 application/json;charset=utf-8，
                        并且请求参数为json格式的字符串时，称作json请求参数，

                        当不设置ajax请求的contentType属性，
                        并且请求参数是单一的值（如字符串、普通js数组等，即"name=zs&age=10"，{"name":"zs","age":10}，"array":[5,8,12]），
                        称作普通请求参数

                        如果ajax请求的contentType属性没有设置为json，那么json格式的字符串就是普通的字符串，也就是普通请求参数
                        一般地，只有在发送复杂json对象（如请求参数是json数组或json对象）作为请求参数时，才会使用json请求参数，
                        {"name":"zs","age":10}和"name=zs&age=10"等价，是ajax请求中常见的两种普通请求参数

                        get请求的无论是普通请求参数还是json请求参数，都在请求头中，叫做 Query String Parameters，
                        post请求的普通请求参数，在请求体中，叫做 Form Data，
                        post请求的json请求参数，在请求体中，叫做 Request Payload，

                        Query String Parameters 是 普通请求参数 时有效，
                        Form Data 是 普通请求参数 时有效，
                        Request Payload 只有是 json请求参数 时才有效

                        综上所述：
                        发送简单参数，post请求和get请求都可以，但是不能设置 contentType 的属性为 application/json;charset=utf-8；
                        发送复杂参数，需执行以下几个步骤：
                            1、将该参数转换成json格式的字符串：JSON.stringify(param)
                            2、设置 contentType 为 application/json;charset=utf-8
                            3、使用post请求方式
                    */
                    contentType:"application/json;charset=utf-8",
                    success:function (result) {
                        alert(result);
                    },
                    error:function (xhr) {
                        alert(xhr.responseText);
                    }
                });
            });

            $("#btn2").click(function(){
                $.ajax({
                    url:"send/array/two.html",
                    type:"post",
                    data:{
                        "array[0]":5,
                        "array[1]":8,
                        "array[2]":12
                    },
                    dataType:"text",
                    success:function (result) {
                        alert(result);
                    },
                    error:function (xhr) {
                        alert(xhr.responseText);
                    }
                });
            });

            $("#btn1").click(function(){
                $.ajax({
                    url:"send/array/one.html",
                    type:"post",
                    data:{
                        "array":[5,8,12]
                    },
                    dataType:"text",
                    success:function (result) {
                        alert(result);
                    },
                    error:function (xhr) {
                        alert(xhr.responseText);
                    }
                });
            });

            $("#btn0").click(function(){
                $.ajax({
                    url:"send/array/zero.html",
                    type:"post",
                    data:{
                        "num1":5,
                        "num2":8,
                        "num3":12,
                    },
                    dataType:"text",
                    success:function (result) {
                        alert(result);
                    },
                    error:function (xhr) {
                        alert(xhr.responseText);
                    }
                });
            });
        });
    </script>
</head>
<body>
    <!--测试ssm环境-->
    <a href="test/testSsm.html">测试ssm环境</a><br><br>

    <button id="btn0">send array [5,8,12] zero</button><br><br>

    <button id="btn1">send array [5,8,12] one</button><br><br>

    <button id="btn2">send array [5,8,12] two</button><br><br>

    <button id="btn3">send array [5,8,12] three</button><br><br>

    <button id="btn4">send compose object</button><br><br>

    <button id="btn5">点我弹框</button><br><br>

    <h1>用户登录表</h1>
    <table border="1">
        <tr>
            <td>用户名：</td>
            <td><input type="text" id="name"/></td>
        </tr>
        <tr>
            <td>密码：</td>
            <td><input type="text" id="password"/></td>
        </tr>
        <tr>
            <td colspan="2" align="center"><input type="button" id="loginBtn" value="登录"/></td>
        </tr>
    </table>
</body>
</html>
