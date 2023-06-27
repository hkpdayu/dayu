package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.model.TreeModel;
import com.xxxx.crm.vo.Module;

import java.util.List;

public interface ModuleMapper extends BaseMapper<Module,Integer> {
    //查询特定的所有资源列表
    public List<TreeModel> queryAllModules();
    //查询所有的资源数据
    public List<Module> queryModuleList();

    //通过层级与模块名查询资源对象
    Module queryModuleByGradeAndModuleName(Integer grade, String moduleName);

    //通过层级与url查询资源对象
    Module queryModuleByGradeAndUrl(Integer grade, String url);

    //通过权限码查询资源对象
    Module queryModuleByOptValue(String optValue);


    //查询指定资源是否存在子记录
    Integer queryModuleByParentId(Integer id);
}