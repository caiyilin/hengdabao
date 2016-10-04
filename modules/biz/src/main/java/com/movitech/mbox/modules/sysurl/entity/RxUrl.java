/**
 * 
 */
package com.movitech.mbox.modules.sysurl.entity;

import org.hibernate.validator.constraints.Length;

import com.movitech.mbox.common.persistence.DataEntity;

/**
 * 系统链接配置Entity
 * @author coki
 * @version 2016-03-18
 */
public class RxUrl extends DataEntity<RxUrl> {
    
    private static final long serialVersionUID = 1L;
    private String code;        // 名称编码
    private String name;        // 链接名称
    private String url;        // url链接
    
    public RxUrl() {
        super();
    }

    public RxUrl(String id){
        super(id);
    }

    @Length(min=0, max=20, message="名称编码长度必须介于 0 和 20 之间")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    @Length(min=0, max=20, message="链接名称长度必须介于 0 和 20 之间")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Length(min=0, max=200, message="url链接长度必须介于 0 和 200 之间")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
}