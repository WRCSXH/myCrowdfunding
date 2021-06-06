// 声明一个函数，加载Auth的树形结构
function fillAuthTree(){
    // 发送ajax请求查询Auth数据
    var ajaxReturn = $.ajax({
        url:"assign/get/all/auth.json",
        type:"post",
        dataType:"json",
        async:false
    });
    if (ajaxReturn.status != 200){
        layer.msg("失败！响应状态码："+ajaxReturn.status+"，提示信息："+ajaxReturn.statusText);
        return;
    }
    // 从服务器端返回的总的JSON数据
    var resultEntity = ajaxReturn.responseJSON;
    if (resultEntity.result == "FAILED"){
       layer.msg(resultEntity.message);
       return;
    }
    // 从服务器端查询得到的authList不再通过服务器端组装，而是交给zTree组装
    var zSetting = {
        check: {
            // 设置zTree的节点上显示默认的复选框
            enable: true
        },
        data: {
            key:{
                // 使用"title"代替默认的"name"显示节点名
                name:"title",
            },
            simpleData: {
                // 使用简单数组格式的数据
                enable: true,
                // 使用"categoryId"代替默认的"pId"关联父节点
                pIdKey: "categoryId",
            }
        },
    };
    // 从响应结果中获取Auth的JSON数据
    var zNodes = resultEntity.data;
    // 生成树形结构
    $.fn.zTree.init($("#authTreeDemo"),zSetting,zNodes);
    // 获取zTreeObj对象
    var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");
    // 调用zTreeObj对象的方法展开全部节点
    zTreeObj.expandAll(true);
    // 查询已分配的authIdList
    ajaxReturn = $.ajax({
        url:"assign/get/assigned/auth/id/by/role/id.json",
        type:"post",
        data:{
            "roleId":window.roleId
        },
        dataType:"json",
        // 设置同步
        async:false
    });
    if (ajaxReturn.status != 200){
        layer.msg("失败！响应状态码："+ajaxReturn.status+"，提示信息："+ajaxReturn.statusText);
        return;
    }
    // 从服务器端返回的总的JSON数据
    resultEntity = ajaxReturn.responseJSON;
    if (resultEntity.result == "FAILED"){
        layer.msg(resultEntity.message);
        return;
    }
    // 从响应结果中获取已分配的authIdList
    var authIdList = resultEntity.data;
    // 遍历authIdList
    for (var i=0;i<authIdList.length;i++){
        var authId = authIdList[i];
        // 根据id查询树形结构中对应的节点，即需要被勾选的节点
        var treeNode = zTreeObj.getNodeByParam("id",authId);
        // 将treeNode设置为被勾选
        var checked = true;
        // checkTypeFlag设置为false表示表示只修改此节点勾选状态，无任何勾选联动操作，不联动是为了避免把不该勾选的选上
        var checkTypeFlag = false;
        // 执行
        zTreeObj.checkNode(treeNode,checked,checkTypeFlag);
    }
}

// 声明一个函数，打开确认删除角色的模态框
function showRoleConfirmModal(roleArray) {
    // 每次打开先清理旧的数据
    $("#roleNamesDiv").empty();
    // 声明全局变量roleIdArray数组
    window.roleIdArray = [];
    // 遍历roleArray数组
    for (var i=0;i<roleArray.length;i++){
        var role = roleArray[i];
        var roleId = role.roleId;
        var roleName = role.roleName;
        // 打开模态框
        $("#roleConfirmModal").modal("show");
        // 填充选中的要被删除的角色
        $("#roleNamesDiv").append(roleName+"<br>");
        // 使用push()函数将每一个roleId填充到roleIdArray数组中
        roleIdArray.push(roleId);
    }
}

// 执行分页，生成页面效果，任何时候调用这个函数都会重新加载页面
function generatePage() {
    // 取消全选按钮
    $("#summaryBox").prop("checked",false);
    // 获取分页数据
    var pageInfo = getPageInfoRemote();
    // 填充表格
    fillTableBody(pageInfo);
}

// 远程访问服务器端程序获取分页数据
function getPageInfoRemote() {
    // 调用$.ajax()函数发送请求并接收它的返回值
    var ajaxResult = $.ajax({
        url:"role/get/page.json",
        type:"post",
        data:{
            "pageNum":window.pageNum,
            "pageSize":window.pageSize,
            "keyword":window.keyword
        },
        dataType:"json",
        // 同步ajax请求确保数据从服务端返回得到完整数据之后再执行以下代码
        async:false
    });
    // 判断当前响应状态码是否为200
    var statusCode = ajaxResult.status;
    // 如果当前响应状态码不是200，说明浏览器端发生了错误或其它意外情况，显示提示信息，让当前函数停止执行
    if (statusCode != 200){
        layer.msg("失败！响应状态码："+statusCode+"，提示信息："+ajaxResult.statusText);
        return null;
    }
    // 如果响应状态码是200，那么说明请求成功，获取pageInfo
    var resultEntity = ajaxResult.responseJSON;
    // 从resultEntity中获取result属性
    var result = resultEntity.result;
    // 判断result是否成功
    if (result == "FAILED"){
        layer.msg(resultEntity.message);
        return null;
    }
    // 确认result成功后，获取pageInfo
    var pageInfo = resultEntity.data;
    // 返回pageInfo
    return  pageInfo;
}

//  填充表格
function fillTableBody(pageInfo) {
    // 清除tbody中旧的内容
    $("#rolePageBody").empty();
    // 这里的清空是为了让没有搜索结果时不显示页码导航条
    $("#Pagination").empty();
    // 判断pageInfo是否有效
    if (pageInfo == null||pageInfo == undefined||pageInfo.list == null||pageInfo.list.length == 0){
        $("#rolePageBody").append("<tr><td colspan='4' align='center'>抱歉！未查询到您搜索的数据！</td></tr>");
    }
    // 使用pageInfo的list属性填充tbody
    for (var i=0;i<pageInfo.list.length;i++){
        var role = pageInfo.list[i];
        var roleId = role.id;
        var roleName = role.name;
        var numberTd = "<td>"+(i+1)+"</td>";
        var checkboxTd = "<td><input id='"+roleId+"' class='itemBox' type='checkbox'></td>";
        var roleNameTd = "<td>"+roleName+"</td>";
        var checkBtn = "<button id='"+roleId+"' type='button' class='btn btn-success btn-xs checkBtn'><i class='glyphicon glyphicon-check'></i></button>";
        var pencilBtn = "<button id='"+roleId+"' type='button' class='btn btn-primary btn-xs pencilBtn'><i class='glyphicon glyphicon-pencil'></i></button>";
        var removeBtn = "<button id='"+roleId+"' type='button' class='btn btn-danger btn-xs removeBtn'><i class='glyphicon glyphicon-remove'></i></button>";
        var buttonTd = "<td>"+checkBtn+" "+pencilBtn+" "+removeBtn+"</td>";
        var tr = "<tr>"+numberTd+checkboxTd+roleNameTd+buttonTd+"</tr>";
        $("#rolePageBody").append(tr);
    }
    // 生成分页导航条
    generateNavigator(pageInfo);
}

// 生成分页导航条
function generateNavigator(pageInfo) {
    // 获取总记录数
    var totalRecord = pageInfo.total;
    // 声明相关属性
    var properties = {
        "num_edge_entries": 3,
        "num_display_entries": 5,
        "callback": paginationCallBack,
        "items_per_page": pageInfo.pageSize,
        "current_page": pageInfo.pageNum - 1,
        "prev_text": "上一页",
        "next_text": "下一页"
    }
    // 调用pagination()函数
    $("#Pagination").pagination(totalRecord, properties);
}

// 翻页时的回调函数
function paginationCallBack(pageIndex, jQuery) {
    // 修改window对象的pageNum属性
    window.pageNum = pageIndex + 1;
    // 调用分页函数
    generatePage();
    // 取消页码超链接的默认行为
    return false;
}