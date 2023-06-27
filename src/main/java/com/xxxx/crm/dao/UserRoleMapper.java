package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.UserRole;

public interface UserRoleMapper extends BaseMapper<UserRole,Integer> {

    //通过用户id查询角色记录
    Integer countUserRoleByUserId(Integer userId);

    //通过用户id删除角色记录
    Integer deleteUserRoleByUserId(Integer userId);
}