<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<div id="roleConfirmModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">删除角色</h4>
            </div>
            <div class="modal-body">
                <form role="form">
                    <h4>您确认要删除以下这些角色吗？</h4>
                    <div id="roleNamesDiv" style="text-align: center"></div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button id="roleConfirmBtn" type="button" class="btn btn-danger">确认删除</button>
            </div>
        </div>
    </div>
</div>
