<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="/WEB-INF/include-head.jsp" %>
<link rel="stylesheet" href="ztree/zTreeStyle.css" />
<script type="text/javascript" src="ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="crowd/menu.js"></script>
<script type="text/javascript">
    $(function () {
        // 调用初始化树形结构的函数
        generateTree();
        // 给添加子节点的按钮绑定单击事件
        $("#treeDemo").on("click",".addBtn",function () {
            // 将当前节点的id作为新节点的pid保存到全局变量
            window.pid = this.id;
            // 打开模态框
            $("#menuAddModal").modal("show");
            return false;
        });
        // 给添加子节点的模态框中的保存按钮绑定单击事件
        $("#menuSaveBtn").click(function () {
            // 收集表单中用户提交的数据
            var name = $.trim($("#menuAddModal [name=name]").val());
            var url = $.trim($("#menuAddModal [name=url]").val());
            // 定位被选中的单选按钮
            var icon = $("#menuAddModal [name=icon]:checked").val();
            $.ajax({
                url:"menu/save.json",
                type:"post",
                data:{
                    "pid":window.pid,
                    "name":name,
                    "url":url,
                    "icon":icon
                },
                dataType:"json",
                success:function (response) {
                    var result = response.result;
                    if (result == "SUCCESS"){
                        layer.msg("操作成功！");
                        // 重新加载树形结构
                        generateTree();

                    } else {
                        layer.msg("操作失败："+response.message);
                    }
                },
                error:function (response) {
                    layer.msg("响应状态码："+response.status+"，提示信息："+response.statusText);
                }
            });
            // 通过手动调用重置按钮的click()函数清空旧数据
            $("#menuResetBtn").click();
            // 关闭模态框
            $("#menuAddModal").modal("hide");
        });
        // 给修改节点的按钮绑定单击事件
        $("#treeDemo").on("click",".editBtn",function () {
            // 将当前节点的id保存到全局变量
            window.id = this.id;
            // 打开模态框
            $("#menuEditModal").modal("show");
            // 获取zTreeObj对象
            var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");
            // 根据属性名和属性值查询当前节点
            var key = "id";
            var value = window.id;
            var currentNode = zTreeObj.getNodeByParam(key,value);
            // 回显表单数据
            $("#menuEditModal [name=name]").val(currentNode.name);
            $("#menuEditModal [name=url]").val(currentNode.url);
            // 回显radio，被选中的radio的value属性值组成一个数组，把这个数组重新赋值给这些radio，相当于它们被选中了
            $("#menuEditModal [name=icon]").val([currentNode.icon]);
            return false;
        });
        //  给修改节点的模态框中的更新按钮绑定单击事件
        $("#menuEditBtn").click(function () {
            // 收集表单中用户提交的数据
            var name = $.trim($("#menuEditModal [name=name]").val());
            var url = $.trim($("#menuEditModal [name=url]").val());
            // 定位被选中的单选按钮
            var icon = $("#menuEditModal [name=icon]:checked").val();
            $.ajax({
                url:"menu/update.json",
                type:"post",
                data:{
                    "id":window.id,
                    "name":name,
                    "url":url,
                    "icon":icon
                },
                dataType:"json",
                success:function (response) {
                    var result = response.result;
                    if (result == "SUCCESS"){
                        layer.msg("操作成功！");
                        // 重新加载树形结构
                        generateTree();
                    } else {
                        layer.msg("操作失败："+response.message);
                    }
                },
                error:function (response) {
                    layer.msg("响应状态码："+response.status+"，提示信息："+response.statusText);
                }
            });
            // 关闭模态框
            $("#menuEditModal").modal("hide");
        });
        // 给删除节点的按钮绑定单击事件
        $("#treeDemo").on("click",".removeBtn",function () {
            // 将当前节点的id保存到全局变量
            window.id = this.id;
            // 打开模态框
            $("#menuConfirmModal").modal("show");
            // 获取zTreeObj对象
            var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");
            // 根据属性名和属性值查询当前节点
            var key = "id";
            var value = window.id;
            var currentNode = zTreeObj.getNodeByParam(key,value);
            // 填充提示信息
            $("#removeNodeSpan").html("【<i class='"+currentNode.icon+"'></i>"+currentNode.name+"】");
            return false;
        });
        //  给删除节点的模态框中的确认删除按钮绑定单击事件
        $("#menuConfirmBtn").click(function () {
            $.ajax({
                url:"menu/remove.json",
                type:"post",
                data:{
                    "id":window.id
                },
                dataType:"json",
                success:function (response) {
                    var result = response.result;
                    if (result == "SUCCESS"){
                        layer.msg("操作成功！");
                        // 重新加载树形结构
                        generateTree();
                    } else {
                        layer.msg("操作失败："+response.message);
                    }
                },
                error:function (response) {
                    layer.msg("响应状态码："+response.status+"，提示信息："+response.statusText);
                }
            });
            // 关闭模态框
            $("#menuConfirmModal").modal("hide");
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
                    <i class="glyphicon glyphicon-th-list"></i> 权限菜单列表
                    <div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal">
                        <i class="glyphicon glyphicon-question-sign"></i>
                    </div>
                </div>
                <div class="panel-body">
                    <ul id="treeDemo" class="ztree" style="user-select: none;"></ul>
                </div>
            </div>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/menu-add-modal.jsp" %>
<%@include file="/WEB-INF/menu-edit-modal.jsp" %>
<%@include file="/WEB-INF/menu-confirm-modal.jsp" %>
</body>
</html>