layui.use(['table', 'treetable'], function () {
    var $ = layui.jquery;
    var table = layui.table;
    var treeTable = layui.treetable;
    treeTable.render({
        treeColIndex: 1,
        treeSpid: -1,
        treeIdName: 'id',
        treePidName: 'parentId',
        elem: '#munu-table',
        url: ctx+'/module/list',
        toolbar: "#toolbarDemo",
        treeDefaultClose:true,
        page: true,
        cols: [[
            {type: 'numbers'},
            {field: 'moduleName', minWidth: 100, title: '菜单名称'},
            {field: 'optValue', title: '权限码'},
            {field: 'url', title: '菜单url'},
            {field: 'createDate', title: '创建时间'},
            {field: 'updateDate', title: '更新时间'},
            {
                field: 'grade', width: 80, align: 'center', templet: function (d) {
                    if (d.grade == 0) {
                        return '<span class="layui-badge layui-bg-blue">⽬录</span>';
                    }
                    if(d.grade==1){
                        return '<span class="layui-badge-rim">菜单</span>';
                    }
                    if (d.grade == 2) {
                        return '<span class="layui-badge layui-bg-gray">按钮</span>';
                    }
                }, title: '类型'
            },
            {templet: '#auth-state', width: 180, align: 'center', title: '操作'}
        ]],
        done: function () {
            layer.closeAll('loading');
        }
    });
    /**
     * 监听头部工具栏
     */
    table.on('toolbar(munu-table)', function(data){
       if (data.event=="expand"){
           //全部展开
           treeTable.expandAll("#munu-table");
       }else if (data.event=="fold"){
           //全部折叠
           treeTable.foldAll("#munu-table");
       }else if (data.event=="add"){
           // 添加目录 层级=0 父菜单=-1
           openAddModuleDialog(0, -1)
       }
    });
    /**
     * 监听行工具栏
     */
    table.on('tool(munu-table)',function (data) {
        // 判断lay-event属性
        if (data.event == "add") {
            // 添加子项
            // 判断当前的层级（如果是三级菜单，就不能添加）
            if (data.data.grade == 2) {
                layer.msg("暂不支持添加四级菜单！",{icon:5});
                return;
            }
            // 一级|二级菜单   grade=当前层级+1，parentId=当前资源的ID
            openAddModuleDialog(data.data.grade+1, data.data.id);

        } else if (data.event == "edit") {
            // 修改资源
            openUpdateModuleDialog(data.data.id);

        } else if (data.event == "del") {
            // 删除资源
            deleteModule(data.data.id);
        }
    });

    /**
     * 删除资源
     * @param id
     */
    function deleteModule(id) {
        layer.confirm('确定删除当前菜单？', {icon: 3, title: "菜单管理"}, function (index) {
            $.post(ctx+"/module/delete",{id:id},function (data) {
                if(data.code==200){
                    layer.msg("操作成功！");
                    //刷新页面
                    window.location.reload();
                }else{
                    layer.msg(data.msg, {icon: 5});
                }
            });
        });
    }

    /**
     * 打开修改资源框
     */
    function openUpdateModuleDialog(id) {
        var title = "<h3>资源管理 - 修改资源</h3>";
        var url = ctx + "/module/toUpdateModulePage?id=" + id;

        layui.layer.open({
            type:2,
            title:title,
            content:url,
            area:["700px","450px"],
            maxmin:true
        });

    }

    /**
     * 打开添加资源的对话框
     * @param grade 层级
     * @param parentId 父菜单ID
     */
    function openAddModuleDialog(grade, parentId) {
        var title = "<h3>资源管理 - 添加资源</h3>";
        var url = ctx + "/module/toAddModulePage?grade=" + grade + "&parentId=" + parentId;

        layui.layer.open({
            type:2,
            title:title,
            content:url,
            area:["700px","450px"],
            maxmin:true
        });
    }


});