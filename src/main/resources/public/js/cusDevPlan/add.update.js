layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    /**
     * 取消按钮
     */
    $("#closeBtn").click(function () {
        // 先得到当前iframe层的索引
        var index = parent.layer.getFrameIndex(window.name);
        // 再执⾏关闭
        parent.layer.close(index);
    });

    /**
     * 表单提交监听
     */
    form.on('submit(addOrUpdateCusDevPlan)',function (data) {

        // 弹出loading层
        var index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});
        //得到表单所有的数据
        var formData = data.field;

        var url = ctx + "/cus_dev_plan/add";

        if ($('[name="id"]').val()){
            var url = ctx + "/cus_dev_plan/update";
        }
        $.post(url, data.field, function (result) {
            if (result.code == 200) {
                setTimeout(function () {
                    // 关闭弹出层（返回值为index的弹出层）
                    top.layer.close(index);
                    top.layer.msg("操作成功！");
                    // 关闭所有ifream层
                    layer.closeAll("iframe");
                    // 刷新⽗⻚⾯
                    parent.location.reload();
                }, 500);
            } else {
                layer.msg(result.msg, {icon: 5});
            }
        });
        return false;
    });
});