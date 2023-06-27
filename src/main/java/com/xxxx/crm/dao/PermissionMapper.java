package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission,Integer> {

    //通过角色id查询角色权限记录
    Integer countPermissionByRoleId(Integer roleId);

    //通过角色id删除角色权限记录
    void deletePermissionByRoleId(Integer roleId);

    //查询角色拥有的所有的资源id的集合
    List<Integer> queryRoleHasModuleIdsByRoleId(Integer roleId);

    //通过用户id查询对应的资源列表
    List<String> queryUserHasRoleHasPermissionByUserId(Integer userId);

    //通过资源id查询权限记录
    Integer countPermissionByModuleId(Integer id);


    //通过资源id删除权限记录
    Integer deletePermissionByModuleId(Integer id);
}