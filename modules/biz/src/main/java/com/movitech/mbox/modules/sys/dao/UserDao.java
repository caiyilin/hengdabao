/**
 * 
 */
package com.movitech.mbox.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.movitech.mbox.common.persistence.CrudDao;
import com.movitech.mbox.common.persistence.annotation.MyBatisDao;
import com.movitech.mbox.modules.sys.entity.SeviceUser;
import com.movitech.mbox.modules.sys.entity.User;

/**
 * 用户DAO接口
 * @author ThinkGem
 * @version 2014-05-16
 */
@MyBatisDao
public interface UserDao extends CrudDao<User> {
    
    /**
     * 根据登录名称查询用户
     * @param loginName
     * @return
     */
    public User getByLoginName(User user);
    
    /**
     * 根据员工编号查询用户
     * @param loginName
     * @return
     */
    public User getByNo(User user);

    /**
     * 通过OfficeId获取用户列表，仅返回用户id和name（树查询用户时用）
     * @param user
     * @return
     */
    public List<User> findUserByOfficeId(User user);
    
    /**
     * 查询全部用户数目
     * @return
     */
    public long findAllCount(User user);
    
    /**
     * 更新用户密码
     * @param user
     * @return
     */
    public int updatePasswordById(User user);
    
    /**
     * 更新登录信息，如：登录IP、登录时间
     * @param user
     * @return
     */
    public int updateLoginInfo(User user);

    /**
     * 删除用户角色关联数据
     * @param user
     * @return
     */
    public int deleteUserRole(User user);
    
    /**
     * 插入用户角色关联数据
     * @param user
     * @return
     */
    public int insertUserRole(User user);
    
    /**
     * 更新用户信息
     * @param user
     * @return
     */
    public int updateUserInfo(User user);
    
    /**
     * 根据英文名称查询用户
     * @param loginName
     * @return
     */
    public User getByEnglishName(User user);
    
    /**
     * 根据员工编号更新用户信息
     * @param user
     * @return
     */
    public int updateByNo(User user);
    
    /**
     * 根据员工编号更新用户信息
     * @param user
     * @return
     */
    public int deleteByNo(User user);
    
    /**
     * 坚持服务奖查询
     * @param nianZi 年资
     * @param dept 部门
     * @param gzd 工作地
     * @return
     */
    public List<SeviceUser> serviceInfo(SeviceUser para);
    
    /**
     * 删除员工后台权限
     * @param user
     * @return
     */
    public int deleteRolesByNo(@Param("userNO")String userNO);
    
    /**
     * 获取登录名数据库数量
     * @param user
     * @return
     */
    public int getUserNumByLoginName(@Param("loginName")String loginName);

}
