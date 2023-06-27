layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;


    /**
     * 计划项数据展示
     */
    var tableIns = table.render({
        elem: '#cusDevPlanList',
        url : ctx+'/cus_dev_plan/list?saleChanceId='+$("#id").val(),
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "cusDevPlanListTable",
        cols : [[
            {type: "checkbox", fixed:"center"},
            {field: "id", title:'编号',fixed:"true"},
            {field: 'planItem', title: '计划项',align:"center"},
            {field: 'exeAffect', title: '执⾏效果',align:"center"},
            {field: 'planDate', title: '执⾏时间',align:"center"},
            {field: 'createDate', title: '创建时间',align:"center"},
            {field: 'updateDate', title: '更新时间',align:"center"},
            {title: '操作',fixed:"right",align:"center", minWidth:150,
                templet:"#cusDevPlanListBar"}
        ]]
    });

    /**
     * 监听表头的添加或修改
     * 更新营销机会的状态
     * 2=开发成功
     * 3=开发失败
     */
    table.on('toolbar(cusDevPlans)',function (data) {

        if (data.event="add"){
            //开发计划项
            openAddOrUpdateCusDevPlanDialog();
        }else if (data.event="success"){
            //开发成功
            updateSaleChanceDevResult(2);

        }else if (data.event="failed"){
            //开发失败
            updateSaleChanceDevResult(3);
        }
    });

    /**
     * 更新营销机会的状态
     */
    function updateSaleChanceDevResult(devResult) {
        //获取当前营销机会的id
        var sId = $("#id").val();
        //弹出提示框询问用户
        layer.confirm("确认执⾏当前操作？", {icon:3, title:"计划项维护"}, function (index) {
            $.post(ctx + "/sale_chance/updateSaleChanceDevResult", {id:sId, devResult:devResult},
                function (data) {
                    if (data.code == 200) {
                        layer.msg("操作成功！",{icon:6});
                        // 关闭弹出层
                        layer.closeAll("iframe");
                        // 刷新⽗⻚⾯
                        parent.location.reload();
                    } else {
                        layer.msg(data.msg, {icon:5});
                    }
                });

        });
    }

    /**
     * 监听行事件
     */
    table.on('tool(cusDevPlans)',function (data) {
        if (data.event=="edit"){
            //更新计划项
            //打开添加或修改计划项的页面
            openAddOrUpdateCusDevPlanDialog(data.data.id);
        }else if (data.event=="del"){
            //打开删除计划项的页面
           deleteCusDevPlan(data.data.id);
        }
    });

    /**
     * 删除计划项数据
     */
   function deleteCusDevPlan(id) {
        // 询问⽤户是否确认删除
        layer.confirm("确定删除当前数据？", {icon:3, title:"开发计划管理"}, function (index) {
            // 发送ajax请求
            $.post(ctx + "/cus_dev_plan/delete", {id:id}, function (result) {
                if (result.code == 200) {
                    layer.msg("操作成功！",{icon:6});
                    // 重新加载表格
                    tableIns.reload();
                } else {
                    layer.msg(result.msg, {icon:5});
                }
            });
        });
    }

    /**
     * 打开开发计划项或更新
     */

    function openAddOrUpdateCusDevPlanDialog(id){
        var url = ctx+"/cus_dev_plan/addOrUpdateCusDevPlanPage?saleChanceId="+$("#id").val();
        var title = "计划项管理-添加计划项";

        //判断计划向的id是否为空(如果为空则是添加，否则就是修改)
        if (id != null&&id != ''){
            //更新计划项
            title="计划向管理--更新计划项";
            url +="&id="+id;

        }

        layui.layer.open({
            title : title,
            type : 2,
            area:["500px","300px"],
            maxmin:true,
            content : url
        });
    }

});
