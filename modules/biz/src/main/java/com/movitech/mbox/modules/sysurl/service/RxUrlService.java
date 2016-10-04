/**
 * 
 */
package com.movitech.mbox.modules.sysurl.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.movitech.mbox.common.persistence.Page;
import com.movitech.mbox.common.service.CrudService;
import com.movitech.mbox.modules.sysurl.entity.RxUrl;
import com.movitech.mbox.modules.sysurl.dao.RxUrlDao;

/**
 * 系统链接配置Service
 * @author coki
 * @version 2016-03-18
 */
@Service
@Transactional(readOnly = true)
public class RxUrlService extends CrudService<RxUrlDao, RxUrl> {

    public RxUrl get(String id) {
        return super.get(id);
    }
    
    public List<RxUrl> findList(RxUrl rxUrl) {
        return super.findList(rxUrl);
    }
    
    public Page<RxUrl> findPage(Page<RxUrl> page, RxUrl rxUrl) {
        return super.findPage(page, rxUrl);
    }
    
    @Transactional(readOnly = false)
    public void save(RxUrl rxUrl) {
        super.save(rxUrl);
    }
    
    @Transactional(readOnly = false)
    public void delete(RxUrl rxUrl) {
        super.delete(rxUrl);
    }
    
    public String getUrlByCode(String code) {
        return dao.getUrlByCode(code);
    }
    
}