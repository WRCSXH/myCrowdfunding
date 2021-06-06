<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<div id="roleAssignAuthModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">给角色分配权限</h4>
            </div>
            <div class="modal-body">
                <form role="form">
                    <h4>请为以下模块分配相应的权限：</h4>
                    <ul id="authTreeDemo" class="ztree" style="user-select: none;"></ul>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button id="roleAssignAuthBtn" type="button" class="btn btn-primary">确认分配</button>
            </div>
        </div>
    </div>
</div>
