<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="/WEB-INF/include-head.jsp" %>
<link rel="stylesheet" href="css/pagination.css">
<script type="text/javascript" src="jquery/jquery.pagination.js"></script>
<script type="text/javascript">
    $(function () {
       // 调用声明函数
       initPagination();
    });
    // 声明一个函数删除单条用户记录
    function deleteAdmin(adminId) {
        layer.confirm("您确定要删除本条记录吗？",{btn:["确定","取消"],title:"提示"},function () {
            if (adminId == "${sessionScope.admin.id}"){
                layer.msg("禁止删除当前登录的管理员！");
            } else {
                window.location.href = "admin/remove/"+adminId+"/${requestScope.pageInfo.pageNum}/${param.keyword}.html";
            }
        },function () {
            layer.msg("已取消！");
        });
    }
    // 声明一个函数初始化Pagination
    function initPagination() {
        // 获取分页数据中的总记录数
        var totalRecord = ${requestScope.pageInfo.total};
        // 声明Pagination设置属性的json对象
        var properties = {
            num_edge_entries: 3, // 边缘页数
            num_display_entries: 5, // 主体页数
            callback:pageSelectCallback, // 用户点击翻页按钮之后，执行翻页操作的回调函数
            // 当前页，pageNum从1开始表示第一页，而current_page从0开始表示第一页，所以减1
            current_page: ${requestScope.pageInfo.pageNum - 1},
            prev_text: "上一页", // 上一页按钮上显示的文字
            next_text: "下一页", // 下一页按钮上显示的文字
            items_per_page:${requestScope.pageInfo.pageSize}, // 每页显示的记录条数
        }
        // 调用div的jQuery对象的pagination()方法生成分页导航条
        $("#Pagination").pagination(totalRecord,properties);
        // 翻页过程中执行的回调函数，点击上一页、下一页或数字页码都会触发翻页动作，从而导致当前函数被调用
        function pageSelectCallback(pageIndex,jQuery) {
            // pageIndex表示当前页的页码，默认从0开始表示第一页，所以比pageNum小1
            var pageNum = pageIndex + 1;
            // 跳转页面实现翻页，翻页时保持关键词查询，param用于获取浏览器地址栏上的请求参数
            window.location.href = "admin/get/page.html?pageNum="+pageNum+"&keyword=${param.keyword}";
            // 取消超链接的默认行为
            return false;
        }
    }
</script>
<body>
<%@ include file="/WEB-INF/include-nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@ include file="/WEB-INF/include-sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                </div>
                <div class="panel-body">
                    <form action="admin/get/page.html" method="post" class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input name="keyword" value="${param.keyword}" class="form-control has-success" type="text" placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button type="submit" class="btn btn-warning"><i class="glyphicon glyphicon-search"></i> 查询</button>
                    </form>
                    <button type="button" class="btn btn-danger" style="float:right;margin-left:10px;"><i class=" glyphicon glyphicon-remove"></i> 删除</button>
                    <button type="button" class="btn btn-primary" style="float:right;" onclick="window.location.href='admin/to/add/page.html'"><i class="glyphicon glyphicon-plus"></i> 新增</button>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr>
                                <th width="30">#</th>
                                <th width="30"><input type="checkbox"></th>
                                <th>账号</th>
                                <th>名称</th>
                                <th>邮箱地址</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <!--PageInfo类中有一个属性list，用于获取封装到PageInfo对象中的List集合-->
                            <c:if test="${empty requestScope.pageInfo.list}">
                                <tr>
                                    <td colspan="6" align="center">抱歉，没有查询到您要的数据！</td>
                                </tr>
                            </c:if>
                            <c:if test="${!empty requestScope.pageInfo.list}">
                                <c:forEach items="${requestScope.pageInfo.list}" var="admin" varStatus="myStatus">
                                    <tr>
                                        <td>${myStatus.count}</td>
                                        <td><input type="checkbox"></td>
                                        <td>${admin.loginAcct}</td>
                                        <td>${admin.userName}</td>
                                        <td>${admin.email}</td>
                                        <td>
                                            <%--<button type="button" class="btn btn-success btn-xs"><i class=" glyphicon glyphicon-check"></i></button>--%>
                                            <%--<button type="button" class="btn btn-primary btn-xs"><i class=" glyphicon glyphicon-pencil"></i></button>--%>
                                            <a href="assign/to/assign/role/page.html?adminId=${admin.id}&pageNum=${requestScope.pageInfo.pageNum}&keyword=${param.keyword}" class="btn btn-success btn-xs"><i class=" glyphicon glyphicon-check"></i></a>
                                            <a href="admin/to/edit/page.html?adminId=${admin.id}&pageNum=${requestScope.pageInfo.pageNum}&keyword=${param.keyword}" class="btn btn-primary btn-xs"><i class=" glyphicon glyphicon-pencil"></i></a>
                                            <button type="button" class="btn btn-danger btn-xs" onclick="deleteAdmin(${admin.id});"><i class=" glyphicon glyphicon-remove"></i></button>
                                            <%--<a href="admin/remove/${admin.id}/${requestScope.pageInfo.pageNum}/${param.keyword}.html" class="btn btn-danger btn-xs"><i class=" glyphicon glyphicon-remove"></i></a>--%>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="6" align="center">
                                    <!--使用Pagination要求的div格式代替原有的分页列表-->
                                    <div id="Pagination" class="pagination"><!--这里显示分页--></div>
                                </td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>