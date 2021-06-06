<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="/WEB-INF/include-head.jsp" %>
<script type="text/javascript">
    $(function () {
        $("#adminSaveBtn").click(function () {
            if($.trim($("#loginAcct").val()) == ""){
                layer.msg("登录账号不能为空！");
            } else if ($.trim($("#userName").val()) == ""){
                layer.msg("用户昵称不能为空！");
            } else if ($.trim($("#userPswd").val()) == ""){
                layer.msg("用户密码不能为空！");
            } else if($.trim($("#email").val()) == ""){
                layer.msg("邮箱地址不能为空！");
            } else {
                $("#saveForm").submit();
            }
        })
    })
</script>
<body>
<%@ include file="/WEB-INF/include-nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@ include file="/WEB-INF/include-sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <ol class="breadcrumb">
                <li><a href="admin/to/main/page.html">首页</a></li>
                <li><a href="admin/get/page.html">数据列表</a></li>
                <li class="active">新增</li>
            </ol>
            <div class="panel panel-default">
                <div class="panel-heading">表单数据<div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal"><i class="glyphicon glyphicon-question-sign"></i></div></div>
                <div class="panel-body">
                    <form id="saveForm" action="admin/save.html" method="post" role="form">
                        <div class="form-group">
                            <label for="loginAcct">登录账号</label>
                            <input type="text" class="form-control" id="loginAcct" name="loginAcct" placeholder="请输入登录账号">
                            <p style="font-size: 12px;color: #ff0000">${requestScope.exception.message}</p>
                        </div>
                        <div class="form-group">
                            <label for="userName">用户昵称</label>
                            <input type="text" class="form-control" id="userName" name="userName" placeholder="请输入用户昵称">
                        </div>
                        <div class="form-group">
                            <label for="userPswd">用户密码</label>
                            <input type="text" class="form-control" id="userPswd" name="userPswd" placeholder="请输入用户密码">
                        </div>
                        <div class="form-group">
                            <label for="email">邮箱地址</label>
                            <input type="email" class="form-control" id="email" name="email" placeholder="请输入邮箱地址">
                            <p class="help-block label label-warning">请输入合法的邮箱地址, 格式为： xxxx@xxxx.com</p>
                        </div>
                        <button type="button" id="adminSaveBtn" class="btn btn-success"><i class="glyphicon glyphicon-plus"></i> 新增</button>
                        <button type="reset" class="btn btn-danger"><i class="glyphicon glyphicon-refresh"></i> 重置</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>