/**
 * 
 */
package com.movitech.mbox.modules.sys.dao;

import com.movitech.mbox.common.persistence.TreeDao;
import com.movitech.mbox.common.persistence.annotation.MyBatisDao;
import com.movitech.mbox.modules.sys.entity.Area;

/**
 * 区域DAO接口
 * @author ThinkGem
 * @version 2014-05-16
 */
@MyBatisDao
public interface AreaDao extends TreeDao<Area> {
    
}
