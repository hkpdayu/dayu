package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.CusDevPlanMapper;
import com.xxxx.crm.query.CusDevPlanQuery;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.CusDevPlan;
import com.xxxx.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.management.Query;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CusDevPlanService extends BaseService<CusDevPlan,Integer> {
    @Resource
    private CusDevPlanMapper cusDevPlanMapper;
    /**
     * 多条件分页查询开发数据（BaseService 中有对应的方法）
     * @param query
     * @return
     */

    public Map<String,Object> queryCusDevPlanByParams(CusDevPlanQuery query){
        Map<String,Object> map = new HashMap<>();
        PageHelper.startPage(query.getPage(),query.getLimit());//使用PageHelper工具类中的startPage方法开启分页
        PageInfo<CusDevPlan> pageInfo = new PageInfo<>(cusDevPlanMapper.selectByParams(query));//根据cusDevPlanMapper.selectByParams(query)查询到的结果存储到pageInfo里
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 添加计划项
     * 1. 参数校验
     * 营销机会ID ⾮空 记录必须存在
     * 计划项内容 ⾮空
     * 计划项时间 ⾮空
     * 2. 设置参数默认值
     * is_valid
     * crateDate
     * updateDate
     * 3. 执⾏添加，判断结果
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCusDevPlan(CusDevPlan cusDevPlan){
        //1.参数校验
        checkParams(cusDevPlan);
        //2.设置默认值
        cusDevPlan.setIsValid(1);
        cusDevPlan.setCreateDate(new Date());
        cusDevPlan.setUpdateDate(new Date());
        //3.执行添加，判断结果
        AssertUtil.isTrue(cusDevPlanMapper.insertSelective(cusDevPlan)<1,"计划项添加失败！");
    }

    /**
     * 计划项更新操作
     * 1.参数校验
     * 2.设置默认值
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCusDevPlan(CusDevPlan cusDevPlan){
        //1.参数校验
        //计划项id，非空，数据存在
        AssertUtil.isTrue(null==cusDevPlan.getId()||cusDevPlanMapper.selectByPrimaryKey(cusDevPlan.getId())==null,"数据异常请稍后重试！");
        checkParams(cusDevPlan);
        //2.设置默认值
        //更新时间。系统当前时间
        cusDevPlan.setUpdateDate(new Date());
        //3.执行添加，判断结果
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan)<1,"计划项更新失败！");
    }

    /**
     * 参数检验
     * @param cusDevPlan
     */

    /**
     * 参数校验
     * 非空校验
     * @param cusDevPlan
     */
    private void checkParams(CusDevPlan cusDevPlan) {
        AssertUtil.isTrue(null==cusDevPlan.getSaleChanceId(),"请设置营销机会id！");
        AssertUtil.isTrue(StringUtils.isBlank(cusDevPlan.getPlanItem()),"请输入计划内容!");
        AssertUtil.isTrue(null==cusDevPlan.getPlanDate(),"请指定计划项日期！");
    }

    /**
     * 删除计划项
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteCusDevPlan(Integer id) {
        CusDevPlan cusDevPlan = cusDevPlanMapper.selectByPrimaryKey(id);
        //判断id是否为空或计划向数据是否为空（就是待删除记录是否存在）
        AssertUtil.isTrue(null == id||null==cusDevPlan,"待删除记录不存在！");
        //将有效码设置为0，表示该计划项被删除
        cusDevPlan.setIsValid(0);
        cusDevPlan.setUpdateDate(new Date());
        //判断受影响的行数
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan)<1,"删除计划项数据失败！");

    }
}
