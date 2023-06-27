package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.RoleQuery;
import com.xxxx.crm.service.RoleService;
import com.xxxx.crm.vo.Role;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {
    @Resource
    private RoleService roleService;
    /**
     * 打开角色管理页面
     */
    @RequestMapping("/index")
    public String index(){
        return "role/role";
    }
    /**
     *
     * 获取角色列表数据（多条件分页查询）
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> selectByParams(RoleQuery roleQuery){
        return roleService.queryByParamsForTable(roleQuery);
    }
    /**
     * 添加角色记录
     */
    @RequestMapping("/add")
    @ResponseBody
    public ResultInfo addRole(Role role){
        roleService.addRole(role);
        return success("角色添加成功！");

    }
    /**
     * 打开添加或更新页面
     */
    @RequestMapping("/addOrUpdateRolePage")
    public String addOrUpdateRolePage(Integer id, Model model){
        model.addAttribute("role",roleService.selectByPrimaryKey(id));
        return "role/add_update";
    }
    /**
     * 更新角色记录
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResultInfo updateRole(Role role){
        roleService.updateRole(role);
        return success("角色更新成功！");
    }
    /**
     * 删除角色记录
     */

    @RequestMapping("/delete")
    @ResponseBody
    public ResultInfo deleteRole(Integer id){
        //调用service层的方法
        roleService.deleteRole(id);
        return success("角色记录删除成功！");

    }
    /**
     *角色授权
     */
    @RequestMapping("/addGrant")
    @ResponseBody
    public ResultInfo addGrant(Integer roleId,Integer[] mIds){
        //roleId:角色id，mIds：资源id
        roleService.addGrant(roleId,mIds);
        return success("角色授权成功!");
    }
}
