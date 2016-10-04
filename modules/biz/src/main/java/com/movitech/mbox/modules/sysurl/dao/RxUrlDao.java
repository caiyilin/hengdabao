/**
 * 
 */
package com.movitech.mbox.modules.sysurl.dao;

import org.apache.ibatis.annotations.Param;

import com.movitech.mbox.common.persistence.CrudDao;
import com.movitech.mbox.common.persistence.annotation.MyBatisDao;
import com.movitech.mbox.modules.sysurl.entity.RxUrl;

/**
 * 系统链接配置DAO接口
 * @author coki
 * @version 2016-03-18
 */
@MyBatisDao
public interface RxUrlDao extends CrudDao<RxUrl> {
    public String getUrlByCode(@Param("code")String code);
}