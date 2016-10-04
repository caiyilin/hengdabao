/**
 * 
 */
package com.movitech.mbox.modules.weixin.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.movitech.mbox.common.persistence.DataEntity;

/**
 * 微信管理Entity
 * @author kyle.wu
 * @version 2015-12-07
 */
public class WxPublicAccount extends DataEntity<WxPublicAccount> {
    
    private static final long serialVersionUID = 1L;
    private String accountId;        // 公众号微信ID
    private String accountName;        // 公众号名称
    private String accountType;        // 订阅号 | 服务号
    private String accountDesc;        // 公众号描述
    private String qrcode;        // 二维码图片地址
    private String appid;        // appid
    private String appsecret;        // appsecret
    private String token;        // token
    private String aeskey;        // aeskey
    private Date cdate;        // 创建日期
    private String creater;        // 创建人id
    private String welcomeText;        // 欢迎信息
    
    public WxPublicAccount() {
        super();
    }

    public WxPublicAccount(String id){
        super(id);
    }

    @Length(min=0, max=255, message="公众号微信ID长度必须介于 0 和 255 之间")
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    
    @Length(min=0, max=255, message="公众号名称长度必须介于 0 和 255 之间")
    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
    
    @Length(min=0, max=255, message="订阅号 | 服务号长度必须介于 0 和 255 之间")
    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
    
    @Length(min=0, max=500, message="公众号描述长度必须介于 0 和 500 之间")
    public String getAccountDesc() {
        return accountDesc;
    }

    public void setAccountDesc(String accountDesc) {
        this.accountDesc = accountDesc;
    }
    
    @Length(min=0, max=255, message="二维码图片地址长度必须介于 0 和 255 之间")
    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }
    
    @Length(min=0, max=255, message="appid长度必须介于 0 和 255 之间")
    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
    
    @Length(min=0, max=255, message="appsecret长度必须介于 0 和 255 之间")
    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }
    
    @Length(min=0, max=255, message="token长度必须介于 0 和 255 之间")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    @Length(min=0, max=255, message="aeskey长度必须介于 0 和 255 之间")
    public String getAeskey() {
        return aeskey;
    }

    public void setAeskey(String aeskey) {
        this.aeskey = aeskey;
    }
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCdate() {
        return cdate;
    }

    public void setCdate(Date cdate) {
        this.cdate = cdate;
    }
    
    @Length(min=0, max=128, message="创建人id长度必须介于 0 和 128 之间")
    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }
    
    public String getWelcomeText() {
        return welcomeText;
    }

    public void setWelcomeText(String welcomeText) {
        this.welcomeText = welcomeText;
    }
    
}