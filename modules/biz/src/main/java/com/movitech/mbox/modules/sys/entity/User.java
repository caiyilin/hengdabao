/**
 * 
 */
package com.movitech.mbox.modules.sys.entity;

import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.movitech.mbox.common.config.Global;
import com.movitech.mbox.common.persistence.DataEntity;
import com.movitech.mbox.common.supcan.annotation.treelist.cols.SupCol;
import com.movitech.mbox.common.utils.excel.annotation.ExcelField;

/**
 * 用户Entity
 * @author ThinkGem
 * @version 2013-12-05
 */
public class User extends DataEntity<User> {

    private static final long serialVersionUID = 1L;
    /* 不需要字段
    private Office company;    // 归属公司
    private Office office;    // 归属部门
    */
    private String loginName;// 登录名（因为用员工编号做登录名，所以这个字段用工号赋值）
    private String password;// 密码
    private String no;        // 工号（员工编号唯一，而且用员工编号来做登录账户）
    private String name;    // 姓名
    private String email;    // 邮箱
    private String phone;    // 电话
    private String loginIp;    // 最后登陆IP
    private Date loginDate;    // 最后登陆日期
    private String loginFlag;    // 是否允许登陆
    private String photo;    // 头像
    private String userType;// 用户类型
    private String mobile;    // 手机

    private String oldLoginName;// 原登录名
    private String newPassword;    // 新密码
    
    private String oldLoginIp;    // 上次登陆IP
    private Date oldLoginDate;    // 上次登陆日期
    
    private Role role;    // 根据角色查询用户条件
    
    private List<Role> roleList = Lists.newArrayList(); // 拥有角色列表
    

    public User() {
        super();
        this.loginFlag = Global.YES;
    }
    
    public User(String id){
        super(id);
    }

    public User(String id, String loginName){
        super(id);
        this.loginName = loginName;
    }

    public User(Role role){
        super();
        this.role = role;
    }
    
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLoginFlag() {
        return loginFlag;
    }

    public void setLoginFlag(String loginFlag) {
        this.loginFlag = loginFlag;
    }

    @SupCol(isUnique="true", isHide="true")
    public String getId() {
        return id;
    }

    @Length(min=1, max=100, message="登录名长度必须介于 1 和 100 之间")
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    /*
    @JsonIgnore
    @NotNull(message="归属公司不能为空")
    public Office getCompany() {
        return company;
    }

    public void setCompany(Office company) {
        this.company = company;
    }
    @JsonIgnore
    @NotNull(message="归属部门不能为空")
    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }
    */

    @Length(min=0, max=200, message="手机长度必须介于 1 和 200 之间")
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    @Length(min=0, max=100, message="用户类型长度必须介于 1 和 100 之间")
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Length(min=0, max=200, message="电话长度必须介于 1 和 200 之间")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRemarks() {
        return remarks;
    }


    public Date getCreateDate() {
        return createDate;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public String getOldLoginName() {
        return oldLoginName;
    }

    public void setOldLoginName(String oldLoginName) {
        this.oldLoginName = oldLoginName;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldLoginIp() {
        if (oldLoginIp == null){
            return loginIp;
        }
        return oldLoginIp;
    }

    public void setOldLoginIp(String oldLoginIp) {
        this.oldLoginIp = oldLoginIp;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getOldLoginDate() {
        if (oldLoginDate == null){
            return loginDate;
        }
        return oldLoginDate;
    }

    public void setOldLoginDate(Date oldLoginDate) {
        this.oldLoginDate = oldLoginDate;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @JsonIgnore
    public List<Role> getRoleList() {
        return roleList;
    }
    
    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    @JsonIgnore
    public List<String> getRoleIdList() {
        List<String> roleIdList = Lists.newArrayList();
        for (Role role : roleList) {
            roleIdList.add(role.getId());
        }
        return roleIdList;
    }

    public void setRoleIdList(List<String> roleIdList) {
        roleList = Lists.newArrayList();
        for (String roleId : roleIdList) {
            Role role = new Role();
            role.setId(roleId);
            roleList.add(role);
        }
    }
    
    /**
     * 用户拥有的角色名称字符串, 多个角色名称用','分隔.
     */
//    public String getRoleNames() {
//        return Collections3.extractToString(roleList, "name", ",");
//    }
    
    public boolean isAdmin(){
        return isAdmin(this.id);
    }
    
    public static boolean isAdmin(String id){
        return id != null && "1".equals(id);
    }
    
    @Override
    public String toString() {
        return id;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    @Length(min=1, max=100, message="工号长度必须介于 1 和 100 之间")
    @ExcelField(title="员工编号", sort=0)
    public String getNo() {
        return no;
    }


    @Length(min=1, max=100, message="姓名长度必须介于 1 和 100 之间")
    @ExcelField(title="中文名", sort=2)
    public String getName() {
        return name;
    }

    @JsonIgnore
    @Length(min=1, max=100, message="密码长度必须介于 1 和 100 之间")
    @ExcelField(title="密码", sort=3)
    public String getPassword() {
        return password;
    }
    

    @Email(message="邮箱格式不正确")
    @ExcelField(title="邮箱", sort=7)
    @Length(min=0, max=200, message="邮箱长度必须介于 1 和 200 之间")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}