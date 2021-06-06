// 初始化树形结构
function generateTree() {
    // 发送ajax请求获取zTree显示树形结构所需的真实数据
    $.ajax({
        url:"menu/get/whole/tree.json",
        type:"post",
        dataType:"json",
        success:function (response) {
            var result = response.result;
            if (result == "SUCCESS"){
                // 创建JSON对象存储对zTree的设置
                var setting = {
                    view:{
                        addDiyDom: addDiyDom,
                        addHoverDom:addHoverDom,
                        removeHoverDom: removeHoverDom,
                    },
                    // 特殊用途：当后台数据只能生成url属性，又不想实现点击节点跳转的功能时，可以直接修改此属性为其他不存在的属性名称
                    data: {
                        key: {
                            url: ""
                        }
                    }
                };
                // 从响应体中获取zTree显示树形结构所需的真实数据
                var zNodes = response.data;
                // 初始化树形结构
                $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            } else {
                layer.msg("显示树形结构失败："+response.message);
            }
        },
        error:function (response) {
            layer.msg("响应状态码："+response.status+"，提示信息："+response.statusText);
        }
    });
}
// 修改默认图标为数据库中的真实图标
function addDiyDom(treeId,treeNode) {
    // 由浏览器控制台的打印信息可知：
    // treeId表示整个树形结构附着的<ul>标签的id
    console.log("treeId:"+treeId);
    // treeNode表示树形结构中的每一个结点
    console.log(treeNode);
    /*
    由原型源码可知，zTree的id生成规则如下：
        示例：treeDemo_1_ico
        解释：<ul>标签的id_当前节点的序号_功能
        提示：可以通过访问treeNode的tId属性获取 “ul>标签的id_当前节点的序号” 部分
     */

    // 根据zTree的id生成规则拼接表示控制图标的<span>标签的id
    var icoSpanId = treeNode.tId+"_ico";
    // 根据spanId选中控制图标，删除旧的class样式，添加新的class样式
    $("#"+icoSpanId).removeClass().addClass(treeNode.icon);
}
// 在鼠标移入节点范围时添加按钮组
function addHoverDom(treeId,treeNode) {
    // 根据zTree的id生成规则拼接按钮组的id，便于精确定位并删除按钮组
    var btnGroupId = treeNode.tId+"_btnGroup";
    // 如果已经追加了按钮，就不用再追加了
    if ($("#"+btnGroupId).length > 0){
        return;
    }
    // 声明一个变量存储拼接好的按钮组
    var btnGroupHtml = "";
    // 准备每一种按钮
    var addBtn = "<a id='"+treeNode.id+"' class='btn btn-info dropdown-toggle btn-xs addBtn' style='margin-left:10px;padding-top:0px;' title=' 添加子节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-plus rbg '></i></a>";
    var editBtn = "<a id='"+treeNode.id+"' class='btn btn-info dropdown-toggle btn-xs editBtn' style='margin-left:10px;padding-top:0px;' title=' 修改节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-edit rbg '></i></a>";
    var removeBtn = "<a id='"+treeNode.id+"' class='btn btn-info dropdown-toggle btn-xs removeBtn' style='margin-left:10px;padding-top:0px;' title=' 删除节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-times rbg '></i></a>";
    // 获取当前节点的级别
    var level = treeNode.level;
    // 判断每一个节点的级别并生成对应的按钮组
    // 根节点，只能添加子节点
    if (level == 0){
        btnGroupHtml = addBtn;
    }
    // 分支节点，可以添加子节点，修改节点
    if (level == 1){
        btnGroupHtml = addBtn+" "+editBtn;
        // 如果分支节点没有子节点，可以删除节点
        if (treeNode.children.length == 0){
            btnGroupHtml = addBtn+" "+editBtn+" "+removeBtn;
        }
    }
    // 叶子节点，可以修改节点，删除节点
    if (level == 2){
        btnGroupHtml = editBtn+" "+removeBtn;
    }
    // 按钮组在id为"treeDemo_n_a"的超链接的后面，拼接这种超链接的id
    var anchorId = treeNode.tId+"_a";
    // 选中并在这种超链接的后面追加按钮组，按钮组的结构为<span><a><i></i></a></span>
    $("#"+anchorId).after("<span id='"+btnGroupId+"'>"+btnGroupHtml+"</span>");
}
// 在鼠标移出节点范围时删除按钮组
function removeHoverDom(treeId,treeNode) {
    // 同上
    var btnGroupId = treeNode.tId+"_btnGroup";
    // 选中并删除按钮组
    $("#"+btnGroupId).remove();
}