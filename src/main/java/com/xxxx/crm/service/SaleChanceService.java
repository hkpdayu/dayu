package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.SaleChanceMapper;
import com.xxxx.crm.enums.DevResult;
import com.xxxx.crm.enums.StateStatus;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.PhoneUtil;
import com.xxxx.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {
    @Resource
    private SaleChanceMapper saleChanceMapper;

    /**
     * 多条件分页查询营销机会（BaseService 中有对应的方法）
     * @param query
     * @return
     */

    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery query){
        Map<String,Object> map = new HashMap<>();
        PageHelper.startPage(query.getPage(),query.getLimit());//使用PageHelper工具类中的startPage方法开启分页
        PageInfo<SaleChance> pageInfo = new PageInfo<>(saleChanceMapper.selectByParams(query));//根据saleChanceMapper.selectByParams(query)查询到的结果存储到pageInfo里
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 添加营销机会
     *  1. 参数校验
     *      customerName客户名称    非空
     *      linkMan联系人           非空
     *      linkPhone联系号码       非空，手机号码格式正确
     *  2. 设置相关参数的默认值
     *      createMan创建人        当前登录用户名
     *      assignMan指派人
     *          如果未设置指派人（默认）
     *              state分配状态 （0=未分配，1=已分配）
     *                  0 = 未分配
     *              assignTime指派时间
     *                  设置为null
     *              devResult开发状态 （0=未开发，1=开发中，2=开发成功，3=开发失败）
     *                  0 = 未开发 （默认）
     *          如果设置了指派人
     *               state分配状态 （0=未分配，1=已分配）
     *                  1 = 已分配
     *               assignTime指派时间
     *                  系统当前时间
     *               devResult开发状态 （0=未开发，1=开发中，2=开发成功，3=开发失败）
     *                  1 = 开发中
     *      isValid是否有效  （0=无效，1=有效）
     *          设置为有效 1= 有效
     *      createDate创建时间
     *          默认是系统当前时间
     *      updateDate
     *          默认是系统当前时间
     *  3. 执行添加操作，判断受影响的行数
     *
     * 乐字节：专注线上IT培训
     * 答疑老师微信：lezijie
     * @param saleChance
     * @return void
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addSaleChance(SaleChance saleChance){
        //1.参数校验
        checkParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        //2.设置相关参数默认值
        /*isValid是否有效  （0=无效，1=有效）设置为有效 1= 有效*/
        saleChance.setIsValid(1);
//        createDate创建时间 默认是系统当前时间
        saleChance.setCreateDate(new Date());
//        updateDate 默认是系统当前时间
        saleChance.setUpdateDate(new Date());
        //未选择分配人
        if (StringUtils.isBlank(saleChance.getAssignMan())) {
           /* state分配状态 （0=未分配，1=已分配）0 = 未分配
                assignTime指派时间 设置为null
                devResult开发状态 （0=未开发，1=开发中，2=开发成功，3=开发失败）0 = 未开发 （默认）*/
            saleChance.setState(StateStatus.UNSTATE.getType());
            saleChance.setAssignTime(null);
            saleChance.setDevResult(DevResult.UNDEV.getStatus());
        }else {
            //选择了分配人
          /*  state分配状态 （0=未分配，1=已分配）1 = 已分配
                 assignTime指派时间 默认系统当前时间
                  devResult开发状态 （0=未开发，1=开发中，2=开发成功，3=开发失败）默认：1 = 开发中*/
            saleChance.setState(StateStatus.STATED.getType());
            saleChance.setAssignTime(new Date());
            saleChance.setDevResult(DevResult.DEVING.getStatus());
        }
        //3.执行添加操作，判断受影响的行数
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance) !=1,"营销机会添加失败！");
    }
    /**
     * 营销机会数据更新
     * 当 assignMan 的值从空变为非空时，我们会将 assignTime 设置为当前系统时间，将 state 设置为已分配（1），将 devResult 设置为开发中（1）。
     * 当 assignMan 的值从非空变为空时，我们会将 assignTime 设置为 null，将 state 设置为未分配（0），将 devResult 设置为未开发（0）。
     * 当 assignMan 的值从一个非空值变为另一个非空值时，我们需要判断修改前后的 assignMan 是否相同。如果不同，就需要将 assignTime 更新为当前系统时间。否则，即 assignMan 没有发生变化，我们需要将 assignTime 设置为修改前的值，即保持不变。
     * 因此，无论是 assignTime、state 还是 devResult 在这段代码中都会根据 assignMan 的不同变化而进行更新。
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChance(SaleChance saleChance) {
        //1 通过id查询数据记录
        SaleChance temp = (SaleChance) saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        AssertUtil.isTrue(null == temp, "待更新记录不存在！");
        //2 参数校验
        checkParams(saleChance.getCustomerName(), saleChance.getLinkMan(), saleChance.getLinkPhone());
        //3 设置相关参数
        saleChance.setUpdateDate(new Date());
        //修改前未分配 修改后已分配
        if (StringUtils.isBlank(temp.getAssignMan()) && StringUtils.isNotBlank(saleChance.getAssignMan())) {
            saleChance.setState(StateStatus.STATED.getType());
            saleChance.setDevResult(DevResult.DEVING.getStatus());
            saleChance.setAssignTime(new Date());
        } else if (StringUtils.isNotBlank(temp.getAssignMan()) && StringUtils.isBlank(saleChance.getAssignMan())) {
            //修改前已分配 修改后未分配
            saleChance.setAssignMan("");
            saleChance.setState(StateStatus.UNSTATE.getType());
            saleChance.setDevResult(DevResult.UNDEV.getStatus());
            saleChance.setAssignTime(null);
        }
        //执行更新，判断结果
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) < 1, "营销机会数据更新失败！");
    }

    /*customerName:⾮空
        linkMan:⾮空
        linkPhone:⾮空 11位⼿机号*/
    private void checkParams(String customerName, String linkMan, String linkPhone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"客户名不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"联系人不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"联系电话不能为空！");
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"联系电话格式不正确！");
    }


    /**
     * 营销机会数据删除
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteSaleChance(Integer[] ids){
        //判断要删除的数据是否为空
        AssertUtil.isTrue(null==ids||ids.length == 0,"请选择要删除的数据！");
        //删除数据，判断受影响的行数
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids)<0,"营销机会数据删除失败！");
    }

    /**
     * 更新营销机会的开发状态
     * @param id
     * @param devResult
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChanceDevResult(Integer id, Integer devResult) {
        //判断id是否为空
        AssertUtil.isTrue(null==id,"待更新记录不存在！");
        AssertUtil.isTrue(null==saleChanceMapper.selectByPrimaryKey(id),"待更新记录不存在！");
        saleChanceMapper.selectByPrimaryKey(id).setDevResult(devResult);
        //判断受影响的行数
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChanceMapper.selectByPrimaryKey(id))!=1,"营销机会状态更新失败!");
    }
}
