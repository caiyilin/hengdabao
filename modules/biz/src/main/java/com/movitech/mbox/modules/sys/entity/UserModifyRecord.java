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
import com.movitech.mbox.common.persistence.DataEntity;
import com.movitech.mbox.common.supcan.annotation.treelist.cols.SupCol;
import com.movitech.mbox.common.utils.excel.annotation.ExcelField;

/**
 * 用户Entity
 * @author ThinkGem
 * @version 2013-12-05
 */
public class UserModifyRecord extends DataEntity<UserModifyRecord> {

    private static final long serialVersionUID = 1L;
    private String loginName;// 登录名
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

    private String createNo;    // 创建者员工编号
    private String createEglishName;    // 创建者英文名称
    private String modifyType;    // 修改类型
    // ------------------------------新属性----------------------------------
    private String englishName;     // 英文名称
    private String position;        // 职位
    private String deptAbb;        // 部门缩写
    private String dept;        // 部门名称
    private String workPlace;        // 工作地
    private String roleNames;        // 用户后台权限
    private String workDate;    // 入职年月
    private String birthDate;    // 出生年月
    // ****************************前台权限********************************
    //  咨询家园
    private String sdll;    // 善的力量
    private String jcfwj;    // 坚持服务奖
    private String rxjg;    // 如新家规
    private String rxxw;    // 如新新闻
    private String dzhbmjs;    // 大中华部门介绍
    //  家园活动
    private String hdjy;    // 家园活动
    //  梦想家园
    private String jdz;    // 金点子
    private String wjdc;    // 问卷调查 
    private String jhgjt;    // 家辉哥讲堂
    private String rxxqgs;    // 如新心情故事
    private String yhrx;    // 悦活如新
    private String qym;    // 企业梦
    //彩色家园
    private String srzf;    // 生日祝福
    private String ctxx;    // 餐厅信息
    private String gajyz;    // 关爱加油站 
    private String bcxx;    // 班车信息
    private String jrzzxt;    // 家人自助系统
    private String jszx;    // 健身中心
    
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @SupCol(isUnique="true", isHide="true")
    public String getId() {
        return id;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemarks() {
        return remarks;
    }


    public Date getCreateDate() {
        return createDate;
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
    @ExcelField(title="员工编号", sort=1)
    public String getNo() {
        return no;
    }
    @ExcelField(title="英文名", sort=2)
    public String getEnglishName() {
        return englishName;
    }


    @Length(min=1, max=100, message="姓名长度必须介于 1 和 100 之间")
    @ExcelField(title="中文名", sort=3)
    public String getName() {
        return name;
    }

    @JsonIgnore
    @Length(min=1, max=100, message="密码长度必须介于 1 和 100 之间")
    @ExcelField(title="密码", sort=4)
    public String getPassword() {
        return password;
    }
    
    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    @ExcelField(title="职位", sort=5)
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @ExcelField(title="部门缩写", sort=6)
    public String getDeptAbb() {
        return deptAbb;
    }

    public void setDeptAbb(String deptAbb) {
        this.deptAbb = deptAbb;
    }

    @ExcelField(title="部门名称", sort=7)
    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    @Email(message="邮箱格式不正确")
    @ExcelField(title="邮箱", sort=8)
    @Length(min=0, max=200, message="邮箱长度必须介于 1 和 200 之间")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @ExcelField(title="工作地", sort=9)
    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    @ExcelField(title="入职年月（YYYY-MM-DD）", sort=10)
    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    @ExcelField(title="出生年月（YYYY-MM-DD）", sort=11)
    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }


    @ExcelField(title="善的力量", sort=12)
    @Length(min=0, max=1, message="权限设置请用'Y'或'N'")
    public String getSdll() {
        return sdll;
    }

    public void setSdll(String sdll) {
        this.sdll = sdll;
    }

    @ExcelField(title="坚持服务奖", sort=13)
    @Length(min=0, max=1, message="权限设置请用'Y'或'N'")
    public String getJcfwj() {
        return jcfwj;
    }

    public void setJcfwj(String jcfwj) {
        this.jcfwj = jcfwj;
    }

    @ExcelField(title="如新家规", sort=14)
    @Length(min=0, max=1, message="权限设置请用'Y'或'N'")
    public String getRxjg() {
        return rxjg;
    }

    public void setRxjg(String rxjg) {
        this.rxjg = rxjg;
    }

    @ExcelField(title="如新新闻", sort=15)
    @Length(min=0, max=1, message="权限设置请用'Y'或'N'")
    public String getRxxw() {
        return rxxw;
    }

    public void setRxxw(String rxxw) {
        this.rxxw = rxxw;
    }

    @ExcelField(title="大中华部门介绍", sort=16)
    @Length(min=0, max=1, message="权限设置请用'Y'或'N'")
    public String getDzhbmjs() {
        return dzhbmjs;
    }

    public void setDzhbmjs(String dzhbmjs) {
        this.dzhbmjs = dzhbmjs;
    }

    @ExcelField(title="家园活动", sort=17)
    @Length(min=0, max=1, message="权限设置请用'Y'或'N'")
    public String getHdjy() {
        return hdjy;
    }

    public void setHdjy(String hdjy) {
        this.hdjy = hdjy;
    }

    @ExcelField(title="金点子", sort=18)
    @Length(min=0, max=1, message="权限设置请用'Y'或'N'")
    public String getJdz() {
        return jdz;
    }

    public void setJdz(String jdz) {
        this.jdz = jdz;
    }

    @ExcelField(title="问卷调查", sort=19)
    @Length(min=0, max=1, message="权限设置请用'Y'或'N'")
    public String getWjdc() {
        return wjdc;
    }

    public void setWjdc(String wjdc) {
        this.wjdc = wjdc;
    }

    @ExcelField(title="家辉哥讲堂", sort=20)
    @Length(min=0, max=1, message="权限设置请用'Y'或'N'")
    public String getJhgjt() {
        return jhgjt;
    }

    public void setJhgjt(String jhgjt) {
        this.jhgjt = jhgjt;
    }

    @ExcelField(title="如新心情故事", sort=21)
    @Length(min=0, max=1, message="权限设置请用'Y'或'N'")
    public String getRxxqgs() {
        return rxxqgs;
    }

    public void setRxxqgs(String rxxqgs) {
        this.rxxqgs = rxxqgs;
    }

    @ExcelField(title="悦活如新", sort=22)
    @Length(min=0, max=1, message="权限设置请用'Y'或'N'")
    public String getYhrx() {
        return yhrx;
    }

    public void setYhrx(String yhrx) {
        this.yhrx = yhrx;
    }

    @ExcelField(title="企业梦", sort=23)
    @Length(min=0, max=1, message="权限设置请用'Y'或'N'")
    public String getQym() {
        return qym;
    }

    public void setQym(String qym) {
        this.qym = qym;
    }

    @ExcelField(title="生日祝福", sort=24)
    @Length(min=0, max=1, message="权限设置请用'Y'或'N'")
    public String getSrzf() {
        return srzf;
    }

    public void setSrzf(String srzf) {
        this.srzf = srzf;
    }

    @ExcelField(title="餐厅信息", sort=25)
    @Length(min=0, max=1, message="权限设置请用'Y'或'N'")
    public String getCtxx() {
        return ctxx;
    }

    public void setCtxx(String ctxx) {
        this.ctxx = ctxx;
    }

    @ExcelField(title="关爱加油站", sort=26)
    @Length(min=0, max=1, message="权限设置请用'Y'或'N'")
    public String getGajyz() {
        return gajyz;
    }

    public void setGajyz(String gajyz) {
        this.gajyz = gajyz;
    }

    @ExcelField(title="班车信息", sort=27)
    @Length(min=0, max=1, message="权限设置请用'Y'或'N'")
    public String getBcxx() {
        return bcxx;
    }

    public void setBcxx(String bcxx) {
        this.bcxx = bcxx;
    }

    @ExcelField(title="家人自助系统", sort=28)
    @Length(min=0, max=1, message="权限设置请用'Y'或'N'")
    public String getJrzzxt() {
        return jrzzxt;
    }

    public void setJrzzxt(String jrzzxt) {
        this.jrzzxt = jrzzxt;
    }

    @ExcelField(title="健身中心", sort=29)
    @Length(min=0, max=1, message="权限设置请用'Y'或'N'")
    public String getJszx() {
        return jszx;
    }

    public void setJszx(String jszx) {
        this.jszx = jszx;
    }

    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }

    public String getRoleNames() {
        return roleNames;
    }

    @ExcelField(title="更新账户英文名称", sort=-4)
    public String getCreateEglishName() {
        return createEglishName;
    }
    @ExcelField(title="更新账户员工编号", sort=-3)
    @Length(min=1, max=100, message="长度必须介于 1 和 100 之间")
    public String getCreateNo() {
        return createNo;
    }
    public void setCreateNo(String createNo) {
        this.createNo = createNo;
    }

    @ExcelField(title="更新时间", sort=-2)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getModifyUserDate() {
        return createDate;
    }
    
    @ExcelField(title="更新类别", sort=-1)
    public String getModifyType() {
        return modifyType;
    }
    public void setModifyType(String modifyType) {
        this.modifyType = modifyType;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public String getLoginFlag() {
        return loginFlag;
    }

    public void setLoginFlag(String loginFlag) {
        this.loginFlag = loginFlag;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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
        return oldLoginIp;
    }

    public void setOldLoginIp(String oldLoginIp) {
        this.oldLoginIp = oldLoginIp;
    }

    public Date getOldLoginDate() {
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

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public void setCreateEglishName(String createEglishName) {
        this.createEglishName = createEglishName;
    }
}