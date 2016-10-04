/**
 * 
 */
package com.movitech.mbox.modules.sys.entity;

import org.springframework.util.StringUtils;

import com.movitech.mbox.common.persistence.DataEntity;

/**
 * 坚持服务奖Entity
 * @author ThinkGem
 * @version 2013-12-05
 */
public class SeviceUser extends DataEntity<SeviceUser> {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @SuppressWarnings("unused")
	private String nianZi;  // 年资
    private String yueZi;  // 月资，sql去年差不看月份，只看年份，所以去月自己算。
    private String deptAbb;  // 部门英文缩写
    private String workPlace;  // 工作地
    private String englishName;  // 英文名
    private String name;  // 中文名
    private String month;  // 月份
    public String getNianZi() {
        // 用月资算年资
        return this.getNianFromYue(yueZi);
    }
    public void setNianZi(String nianZi) {
        this.nianZi = nianZi;
    }
    public String getDeptAbb() {
        return deptAbb;
    }
    public void setDeptAbb(String deptAbb) {
        this.deptAbb = deptAbb;
    }
    public String getWorkPlace() {
        return workPlace;
    }
    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }
    public String getEnglishName() {
        return englishName;
    }
    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMonth() {
        return month;
    }
    public void setMonth(String month) {
        this.month = month;
    }
    public String getYueZi() {
        return yueZi;
    }
    public void setYueZi(String yueZi) {
        this.yueZi = yueZi;
    }
    
    /**
     * 月资转为年资
     * @param yue 月资
     * @return
     */
    private String getNianFromYue(String yue) {
        String nianZi = "";
        if(!StringUtils.isEmpty(yue)) {
            int a = Integer.valueOf(yue) / 12;
            int b = Integer.valueOf(yue) % 12;
            if(b > 0) {
                nianZi = String.valueOf(a + 1);
            } else {
                nianZi = String.valueOf(a);
            }
        }
        return nianZi;
    }
}