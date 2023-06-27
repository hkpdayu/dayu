package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.ModuleMapper;
import com.xxxx.crm.dao.PermissionMapper;
import com.xxxx.crm.dao.RoleMapper;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.Permission;
import com.xxxx.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RoleService extends BaseService<Role,Integer> {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private ModuleMapper moduleMapper;
    /**
     * 添加角色记录
     * 1.参数校验
     * 2.设置默认值
     * 3.返回受影响的行数
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addRole(Role role){
        //1.参数校验
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"角色名称不能为空！");
        //通过角色名称查询角色记录
        Role temp = roleMapper.selectByRoleName(role.getRoleName());
        //判断角色记录是否存在（添加操作时，如果角色记录存在表示角色名称不可用）
        AssertUtil.isTrue(temp != null,"角色名称已存在，请重新输入！");
        //2.设置默认值
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        //3.判断受影响的行数
        AssertUtil.isTrue(roleMapper.insertSelective(role)<1,"角色添加失败！");

    }

    /**
     * 更新角色记录
     * 参数校验
     * 设置默认值
     * 返回受影响行数
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRole(Role role) {
        //参数校验
        //判断角色id是否存在，角色记录是否存在（待修改记录是否存在）
        AssertUtil.isTrue(null==role.getId()||null==roleMapper.selectByPrimaryKey(role.getId()),"待修改记录不存在！");
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"请输入角色名！");
        Role temp = roleMapper.selectByRoleName(role.getRoleName());
        AssertUtil.isTrue(null!=temp&&!(temp.getId().equals(role.getId())),"该角色已存在！");
        //设置默认值
        role.setUpdateDate(new Date());
        //返回受影响行数
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role)<1,"角色记录修改失败！");

    }

    /**
     * 删除角色记录
     * 1.参数校验
     * 2.设置默认值
     * 3.返回受影响的行数
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRole(Integer id) {
        //1.参数校验（判断要删除的角色记录是否存在）
        AssertUtil.isTrue(null==id||null==roleMapper.selectByPrimaryKey(id),"待删除的角色记录不存在！");
        //设置默认值，设置有效码为0
        roleMapper.selectByPrimaryKey(id).setIsValid(0);
        //返回受影响的行数
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(roleMapper.selectByPrimaryKey(id))<1,"角色记录删除失败！");

    }

    /**
     * 角色授权
     * 将对应的角色id与资源id，添加到对应的权限表中
     * 直接添加权限会出现重复的权限数据
     * 推荐：
     *  现将已有的权限记录删除，再将需要的权限记录添加
     *  1.通过 角色id找到对应的权限记录
     *  2.如果权限记录存在，则删除角色对应的权限记录
     *  3.是否有权限记录，最后批量添加权限记录
     * @param roleId
     * @param mIds
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addGrant(Integer roleId, Integer[] mIds) {
        //通过角色id找到对应的权限记录
        Integer count= permissionMapper.countPermissionByRoleId(roleId);
        //2.如果权限记录存在，则删除角色对应的权限记录
        if (count>0){
            //删除权限记录
            permissionMapper.deletePermissionByRoleId(roleId);
        }
        //3.是否有权限记录，最后批量添加权限记录
        if (mIds !=null&&mIds.length>0){
            //定义permission集合
            List<Permission> permissionList = new ArrayList<>();
            //遍历资源id数组
            for (Integer mId : mIds) {
                Permission permission = new Permission();
                permission.setModuleId(mId);
                permission.setRoleId(roleId);
                permission.setAclValue(moduleMapper.selectByPrimaryKey(mId).getOptValue());
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                //将对象设置到集合中
                permissionList.add(permission);
                
            }
            //返回受影响的行数
            AssertUtil.isTrue(permissionMapper.insertBatch(permissionList)!=permissionList.size(),"角色记录添加失败！00");
        }

    }
}
