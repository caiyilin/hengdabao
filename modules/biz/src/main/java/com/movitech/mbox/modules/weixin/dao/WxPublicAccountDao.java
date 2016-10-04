/**
 * 
 */
package com.movitech.mbox.modules.weixin.dao;

import com.movitech.mbox.common.persistence.CrudDao;
import com.movitech.mbox.common.persistence.annotation.MyBatisDao;
import com.movitech.mbox.modules.weixin.entity.WxPublicAccount;

/**
 * 微信管理DAO接口
 * @author kyle.wu
 * @version 2015-12-07
 */
@MyBatisDao
public interface WxPublicAccountDao extends CrudDao<WxPublicAccount> {

	WxPublicAccount getByAccountId(String accountId);
    
}