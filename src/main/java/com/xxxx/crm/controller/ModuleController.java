package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.model.TreeModel;
import com.xxxx.crm.service.ModuleService;
import com.xxxx.crm.vo.Module;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/module")
public class ModuleController extends BaseController {
    @Resource
    private ModuleService moduleService;
    /**
     * 查询所有的资源列表
     */
    @RequestMapping("/queryAllModules")
    @ResponseBody
    public List<TreeModel> queryAllModules(Integer roleId){
        return moduleService.queryAllModules(roleId);
    }

    /**
     * 授权页面
     */
    @RequestMapping("/toAddGrantPage")
    public String toAddGrantPage(Integer roleId, Model model){
        //将需要授权的角色id设置到请求域中
        model.addAttribute("roleId",roleId);
        return "role/grant";

    }
    /**
     * 查询所有的资源数据
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryModuleList(){
        return moduleService.queryModuleList();
    }
    /**
     * 进入菜单管理页面
     */
    @RequestMapping("/index")
    public String index(){
        return "module/module";
    }
    /**
     * 添加资源
     */
    @RequestMapping("/add")
    @ResponseBody
    public ResultInfo addModule(Module module){

        moduleService.addModule(module);
        return success("资源添加成功！");
    }

    /**
     * 打开资源添加页面
     * @param grade 层级
     * @param parentId 父id
     * @return
     */
    @RequestMapping("/toAddModulePage")
    public String toAddModulePage(Integer grade, Integer parentId, HttpServletRequest request){
        //将数据设置到作用域中
        request.setAttribute("grade",grade);
        request.setAttribute("parentId",parentId);
        return "module/add";
    }

    /**
     * 打开修改资源框
     */
    @RequestMapping("/toUpdateModulePage")
    public String openUpdateModuleDialog(Integer id,Model model){
        //将要修改的资源对象设置到作用域中
        model.addAttribute("module",moduleService.selectByPrimaryKey(id));
        return "module/update";
    }

    /**
     * 修改资源
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResultInfo updateModule(Module module){

        moduleService.updateModule(module);
        return success("资源修改成功！");
    }

    /**
     * 删除资源
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResultInfo deleteModule(Integer id){

        moduleService.deleteModule(id);
        return success("资源删除成功！");
    }
}
