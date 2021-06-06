<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="/WEB-INF/include-head.jsp" %>
<link rel="stylesheet" href="css/pagination.css">
<script type="text/javascript" src="jquery/jquery.pagination.js"></script>
<link rel="stylesheet" href="ztree/zTreeStyle.css"/>
<script type="text/javascript" src="ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="crowd/role.js"></script>
<script type="text/javascript">
    $(function (){
        // 为分页操作设置初始化数据
        window.pageNum = 1;
        window.pageSize = 5;
        window.keyword = "";
        // 调用执行分页的函数，显示分页效果
        generatePage();
        // 给查询按钮绑定单击事件
        $("#roleSearchBtn").click(function (){
            // 获取关键词数据赋值给对应的全局变量
            window.keyword = $("#keywordInput").val();
            // 调用分页函数刷新页面
            generatePage();
        });
        // 给关键字搜索框绑定敲回车事件
        $("#keywordInput").keydown(function (event) {
            if (event.keyCode == 13){
                window.keyword = $("#keywordInput").val();
                generatePage();
                // 取消浏览器中敲回车的默认行为
                return false;
            }
        });
        // 点击新增按钮打开添加角色的模态框
        $("#showRoleAddModal").click(function () {
            $("#roleAddModal").modal("show");
        });
        // 点击保存按钮添加角色
        $("#roleSaveBtn").click(function () {
            $.ajax({
                url:"role/save.json",
                type:"post",
                data:{
                    "name":$.trim($("#roleAddModal [name=roleName]").val())
                },
                dataType:"json",
                success:function (response) {
                    var result = response.result;
                    if (result == "SUCCESS"){
                        layer.msg("添加角色成功！");
                        window.pageNum = 999;
                        generatePage();
                    } else {
                        layer.msg("添加角色失败："+response.message);
                    }
                },
                error:function (response) {
                    layer.msg("响应状态码："+response.status+"，提示信息："+response.statusText);
                }
            });
            $("#roleAddModal [name=roleName]").val("");
            $("#roleAddModal").modal("hide");
        });
        // 点击铅笔按钮打开修改角色的模态框
        // 找到动态元素附着的静态元素，使用on()函数完成单击事件的绑定
        $("#rolePageBody").on("click",".pencilBtn",function () {
            // 角色名称回显
            /*
                this表示当前dom节点，即铅笔按钮
                .parent().prev()表示找到铅笔按钮的父节点的左节点，即显示角色名称的<td>标签
                然后再通过.text()获取这个标签的文本值
             */
            var roleName = $(this).parent().prev().text();
            $("#roleEditModal [name=roleName]").val(roleName);
            window.roleId = this.id;
            // 打开模态框
            $("#roleEditModal").modal("show");
        });
        // 点击更新按钮，修改角色
        $("#roleUpdateBtn").click(function () {
            $.ajax({
                url:"role/update.json",
                type:"post",
                data:{
                  "id":window.roleId,
                  "name":$.trim($("#roleEditModal [name=roleName]").val())
                },
                dataType:"json",
                success:function (response) {
                    var result = response.result;
                    if (result == "SUCCESS"){
                        layer.msg("修改角色成功！");
                        generatePage();
                    } else {
                        layer.msg("修改角色失败："+response.message);
                    }
                },
                error:function (response) {
                    layer.msg("响应状态码："+response.status+"，提示信息："+response.statusText);
                }
            });
            $("#roleEditModal").modal("hide");
        });
        // 点击单条删除按钮，打开确认删除的模态框，使用on()方法绑定单击事件
        $("#rolePageBody").on("click",".removeBtn",function () {
            // 准备roleArray数组
            var roleArray = [];
            roleArray.push({
               roleId:this.id,
               roleName:$(this).parent().prev().text()
            });
            // 调用封装好的展示确认删除模态框的函数
            showRoleConfirmModal(roleArray);
        });
        // 全选和全不选
        $("#summaryBox").click(function () {
            $(".itemBox").prop("checked",this.checked);
        });
        // 反向操作
        $("#rolePageBody").on("click",".itemBox",function () {
            var checkedBoxCount = $(".itemBox:checked").length;
            var totalBoxCount = $(".itemBox").length;
            $("#summaryBox").prop("checked",checkedBoxCount == totalBoxCount);
        });
        // 点击批量删除按钮，打开确认删除的模态框
        $("#roleBatchRemoveBtn").click(function () {
            // 准备roleArray数组
            var roleArray = [];
            // 遍历被选中的dom数组
            $(".itemBox:checked").each(function () {
                roleArray.push({
                    roleId:this.id,
                    roleName:$(this).parent().next().text()
                });
            })
            if (roleArray.length == 0){
                layer.msg("请至少选择一条记录！");
                return;
            }
            // 调用封装好的展示确认删除模态框的函数
            showRoleConfirmModal(roleArray);
        });
        // 点击确认删除按钮，删除角色
        $("#roleConfirmBtn").click(function () {
            // 将数组转换成json字符串
            var roleIdArray = JSON.stringify(window.roleIdArray);
            $.ajax({
                url:"role/remove/by/role/id/array.json",
                type:"post",
                data:roleIdArray,
                dataType: "json",
                contentType:"application/json;charset=UTF-8",
                success:function (response) {
                    var result = response.result;
                    if (result == "SUCCESS"){
                        layer.msg("删除角色成功！");
                        generatePage();
                    } else {
                        layer.msg("删除角色失败："+response.message);
                    }
                },
                error:function (response) {
                    layer.msg("响应状态码："+response.status+"，提示信息："+response.statusText);
                }
            });
            $("#roleConfirmModal").modal("hide");
        });
        // 给分配权限的按钮绑定单击事件
        $("#rolePageBody").on("click",".checkBtn",function () {
            window.roleId = this.id;
            // 打开模态框
            $("#roleAssignAuthModal").modal("show");
            // 在模态框中加载Auth的树形结构
            fillAuthTree();
        });
        // 给分配权限模态框中的确认分配按钮绑定单击事件
        $("#roleAssignAuthBtn").click(function () {
            // 收集树形结构中被勾选的权限节点
            // 声明一个数组存储它们的id
            var authIdArray = [];
            // 获取zTreeObj对象
            var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");
            // 获取全部被勾选的节点
            var checkedNodes = zTreeObj.getCheckedNodes();
            // 遍历checkedNodes
            for (var i=0;i<checkedNodes.length;i++){
                var checkedNode = checkedNodes[i];
                var authId = checkedNode.id;
                authIdArray.push(authId);
            }
            // 发送请求执行分配
            var requestBody = {
                "authIdArray":authIdArray,
                // 为了服务器端handler方法也能统一使用List<Integer>接收数据，roleId也存入数据
                "roleId":[window.roleId]
            }
            // JSON字符串化
            requestBody = JSON.stringify(requestBody);
            // 发送ajax请求
            $.ajax({
                url:"assign/do/role/assign/auth.json",
                type:"post",
                data:requestBody,
                contentType: "application/json;charset=UTF-8",
                dataType:"json",
                success:function (response) {
                    var result = response.result;
                    if (result == "SUCCESS"){
                        layer.msg("操作成功！");
                    } else {
                        layer.msg("操作失败："+response.message);
                    }
                },
                error:function (response) {
                    layer.msg("响应状态码："+response.status+"，提示信息："+response.statusText);
                }
            });
            $('#roleAssignAuthModal').modal("hide");
        });
    });
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
                    <form class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input id="keywordInput" class="form-control has-success" type="text" placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button id="roleSearchBtn" type="button" class="btn btn-warning"><i class="glyphicon glyphicon-search"></i> 查询</button>
                    </form>
                    <button id="roleBatchRemoveBtn" type="button" class="btn btn-danger" style="float:right;margin-left:10px;"><i class=" glyphicon glyphicon-remove"></i> 删除</button>
                    <button id="showRoleAddModal" type="button" class="btn btn-primary" style="float:right;"><i class="glyphicon glyphicon-plus"></i> 新增</button>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr>
                                <th width="30">#</th>
                                <th width="30"><input id="summaryBox" type="checkbox"></th>
                                <th>名称</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody id="rolePageBody"></tbody>
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
<%@include file="/WEB-INF/role-add-modal.jsp" %>
<%@include file="/WEB-INF/role-edit-modal.jsp" %>
<%@include file="/WEB-INF/role-confirm-modal.jsp" %>
<%@include file="/WEB-INF/role-assign-auth-modal.jsp" %>
</body>
</html>