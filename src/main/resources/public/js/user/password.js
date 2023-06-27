qlayui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);
    /**
     * ⽤户密码修改 表单提交
     */
    form.on("submit(saveBtn)",function (data) {
        //获取表单元素的内容
        var filedData = data.field;
        //发送Ajax请求，修改用户密码
        $.ajax({
            type:"post",
            url:ctx+"/user/updatePassword",
            data:{
              oldPassword:filedData.old_password,
              newPassword:filedData.new_password,
              confirmPassword:filedData.again_password

            },
            dataType:"json",
            success:function (result) {
                if (result.code==200){
                    //修改成功，用户自动退出系统
                    layer.msg("用户密码修改成功，系统将在三秒后退出",function () {
                        //退出系统后，删除对应的cookie
                        $.removeCookie("userId",{domain:"localhost",path:"/crm"});
                        $.removeCookie("userName",{domain:"localhost",path:"/crm"});
                        $.removeCookie("trueName",{domain:"localhost",path:"/crm"});
                        //跳转到登陆页面
                        window.parent.location.href= ctx +"/index";
                    });
                }else {
                    layer.msg(result.msg);
                }
            }
        });

    });
});