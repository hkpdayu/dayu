package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.CusDevPlanQuery;
import com.xxxx.crm.service.CusDevPlanService;
import com.xxxx.crm.service.SaleChanceService;
import com.xxxx.crm.vo.CusDevPlan;
import com.xxxx.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("/cus_dev_plan")
public class CusDevPlanController  extends BaseController {
    @Resource
    private SaleChanceService saleChanceService;
    @Resource
    private CusDevPlanService cusDevPlanService;
    /**
     * 客户开发页面
     */
    @RequestMapping("/index")
    public String index(){

        return "cusDevPlan/cus_dev_plan";
    }
    /**
     * 进入开发计划项数据页面
     */
    @RequestMapping("/toCusDevPlanDataPage")
    public String toCusDevPlanDataPage(Model model,Integer id){
        //通过id查询营销机会数据
        SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
        //将数据存放到作用域中
        model.addAttribute("saleChance",saleChance);
        return "cusDevPlan/cus_dev_plan_data";
    }

    /**
     * 查询营销机会的计划项数据列表
     * @param query
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> queryCusDevPlanByParams (CusDevPlanQuery query) {
        return cusDevPlanService.queryCusDevPlanByParams(query);
    }
    /**
     * 添加计划项
     */
    @PostMapping("/add")
    @ResponseBody
    public ResultInfo addCusDevPlan(CusDevPlan cusDevPlan){
        cusDevPlanService.addCusDevPlan(cusDevPlan);
        return success("计划项添加成功！");
    }

    /**
     * 打开  开发计划项或更新的页面
     */
    @RequestMapping("/addOrUpdateCusDevPlanPage")
    public String addOrUpdateCusDevPlanPage(Integer saleChanceId,Integer id,Model model){
        model.addAttribute("cusDevPlan",cusDevPlanService.selectByPrimaryKey(id));
        model.addAttribute("sId",saleChanceId);
        return "cusDevPlan/add_update";
    }

    /**
     * 更新开发计划数据
     */
    @PostMapping("/update")
    @ResponseBody
    public  ResultInfo updateCusDevPlan(CusDevPlan cusDevPlan){
        //更新开发计划的数据
        cusDevPlanService.updateCusDevPlan(cusDevPlan);
        return success("开发计划数据更新成功！");
    }
    /**
     * 删除计划项
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResultInfo deleteCusDevPlan(Integer id){
        cusDevPlanService.deleteCusDevPlan(id);
        return success("计划项数据删除成功!");
    }
}
