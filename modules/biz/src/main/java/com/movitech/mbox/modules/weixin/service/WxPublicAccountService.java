/**
 * 
 */
package com.movitech.mbox.modules.weixin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.movitech.mbox.common.persistence.Page;
import com.movitech.mbox.common.service.CrudService;
import com.movitech.mbox.modules.weixin.entity.WxPublicAccount;
import com.movitech.mbox.modules.weixin.dao.WxPublicAccountDao;

/**
 * 微信管理Service
 * @author kyle.wu
 * @version 2015-12-07
 */
@Service
@Transactional(readOnly = true)
public class WxPublicAccountService extends CrudService<WxPublicAccountDao, WxPublicAccount> {
	
	@Autowired
	private WxPublicAccountDao wxPublicAccountDao;
	
    public WxPublicAccount get(String id) {
        return super.get(id);
    }
    
    public List<WxPublicAccount> findList(WxPublicAccount wxPublicAccount) {
        return super.findList(wxPublicAccount);
    }
    
    public Page<WxPublicAccount> findPage(Page<WxPublicAccount> page, WxPublicAccount wxPublicAccount) {
        return super.findPage(page, wxPublicAccount);
    }
    
    @Transactional(readOnly = false)
    public void save(WxPublicAccount wxPublicAccount) {
        super.save(wxPublicAccount);
    }
    
    @Transactional(readOnly = false)
    public void delete(WxPublicAccount wxPublicAccount) {
        super.delete(wxPublicAccount);
    }
    
    public WxPublicAccount getByAccountId(String accountId){
    	return wxPublicAccountDao.getByAccountId(accountId);
    }
    
}