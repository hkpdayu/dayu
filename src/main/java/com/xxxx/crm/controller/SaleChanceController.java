package com.xxxx.crm.controller;

import com.xxxx.crm.annoation.RequirePermission;
import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.enums.StateStatus;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.service.SaleChanceService;
import com.xxxx.crm.utils.CookieUtil;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/sale_chance")
public class SaleChanceController extends BaseController {
    @Resource
    private SaleChanceService saleChanceService;

    /**
     * 用户营销机会数据列表 权限资源码：101001
     * @param query
     * @param flag
     * @param request
     * @return
     */
    @RequirePermission(code = "101001")
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery query,Integer flag,HttpServletRequest request){
        //查询参数，flag等于1时表示当前查询为开发计划数据
        if (null != flag && flag == 1){
            //设置分配状态
            query.setState(StateStatus.STATED.getType());
            //获取 当前登录用户的id
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            //设置查询分配人的参数
            query.setAssignMan(userId);
        }

        return saleChanceService.querySaleChanceByParams(query);
    }


    /**
     * 添加营销机会 101002
     * 添加营销机会数据
     */
    @RequirePermission(code = "101002")
    @RequestMapping("/add")
    @ResponseBody
    public ResultInfo addSaleChance(HttpServletRequest request, SaleChance saleChance){
        //从cookie中获取用户姓名
        String userName = CookieUtil.getCookieValue(request,"userName");
        //设置营销机会的创建人
        saleChance.setCreateMan(userName);
        //添加营销机会的数据
        saleChanceService.addSaleChance(saleChance);
        return success("营销机会数据添加成功！");

    }

    /**
     * 进入营销机会页面 权限资源码：1010
     * @return
     */
    @RequirePermission(code = "1010")
    @RequestMapping("/index")
    public String index(){
        return "saleChance/sale_chance";
    }
    /**
     * 打开营销机会添加或更新页面
     */
    @RequestMapping("/openSaleChanceDialog")
    public String openSaleChanceDialog(Integer saleChanceId, Model model){
        //如果saleChanceId不为空，表示是更新操作，更新之前需要查询被修改的数据
        if (null != saleChanceId){
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(saleChanceId);
            //将数据放到作用域中
            model.addAttribute("saleChance",saleChance);
        }
        return "saleChance/add_update";
    }
    /**
     * 更新营销机会 101004
     * 更新营销机会数据
     */
    @RequirePermission(code = "101004")
    @RequestMapping("/update")
    @ResponseBody
    public  ResultInfo updateSaleChance(SaleChance saleChance){
        //更新营销机会的数据
        saleChanceService.updateSaleChance(saleChance);
        return success("营销机会数据更新成功！");
    }
    /**
     * 删除营销机会 101003
     * 删除营销机会数据
     */
    @RequirePermission(code = "101003")
    @RequestMapping("/delete")
    @ResponseBody
    public ResultInfo deleteSaleChance(Integer[] ids){
        //删除营销机会的数据
        saleChanceService.deleteSaleChance(ids);
        return success("营销机会数据删除成功！");

    }
    /**
     * 更新营销机会的开发状态
     */
    @RequestMapping("/updateSaleChanceDevResult")
    @ResponseBody
    public ResultInfo updateSaleChanceDevResult(Integer id ,Integer devResult){

        //调用service层的方法
        saleChanceService.updateSaleChanceDevResult(id,devResult);
        return success("开发状态更新成功！");
    }

}
