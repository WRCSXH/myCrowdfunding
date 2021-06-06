<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="/WEB-INF/include-head.jsp" %>
<script type="text/javascript">
    $(function () {
        // 点击往右按钮，将未分配角色框中选中的option剪切到已分配角色框中
        $("#toRightBtn").click(function () {
            $("select:eq(0)>option:selected").appendTo($("select:eq(1)"));
        });
        // 点击往左按钮，将已分配角色框中选中的option剪切到未分配角色框中
        $("#toLeftBtn").click(function () {
            $("select:eq(1)>option:selected").appendTo($("select:eq(0)"));
        });
        /*
        关于jQuery选择器
            select是标签选择器
            :eq(0)表示选择页面上的第一个
            :eq(1)表示选择页面上的第二个
            >表示选中子元素
            :selected表示选择被选中的元素
            A.appendTo(B)可以将选中的A剪切到B中
         */

        //在提交表单前把已分配角色框中的全部option选中
        $("#assignRoleBtn").click(function () {
            $("select:eq(1)>option").prop("selected","selected");
        })
    });
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
                <li class="active">分配角色</li>
            </ol>
            <div class="panel panel-default">
                <div class="panel-body">
                    <form role="form" action="assign/do/admin/assign/role.html" method="post" class="form-inline">
                        <input type="hidden" name="adminId" value="${param.adminId}"/>
                        <input type="hidden" name="pageNum" value="${param.pageNum}"/>
                        <input type="hidden" name="keyword" value="${param.keyword}"/>
                        <div class="form-group">
                            <label>未分配角色列表</label><br>
                            <select class="form-control" multiple="multiple" size="10" style="width:100px;overflow-y:auto;">
                                <c:forEach items="${requestScope.unAssignedRoleList}" var="role">
                                    <option value="${role.id}">${role.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <ul>
                                <li id="toRightBtn" class="btn btn-default glyphicon glyphicon-chevron-right"></li>
                                <br>
                                <li id="toLeftBtn" class="btn btn-default glyphicon glyphicon-chevron-left" style="margin-top:20px;"></li>
                            </ul>
                        </div>
                        <div class="form-group" style="margin-left:40px;">
                            <label>已分配角色列表</label><br>
                            <select name="roleIdList" class="form-control" multiple="multiple" size="10" style="width:100px;overflow-y:auto;">
                                <c:forEach items="${requestScope.assignedRoleList}" var="role">
                                    <option value="${role.id}">${role.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <button type="submit" id="assignRoleBtn" class="btn btn-success"><i class="glyphicon glyphicon-plus"></i> 提交</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>