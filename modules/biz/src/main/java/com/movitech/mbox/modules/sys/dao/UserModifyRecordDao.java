/**
 * 
 */
package com.movitech.mbox.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.movitech.mbox.common.persistence.CrudDao;
import com.movitech.mbox.common.persistence.annotation.MyBatisDao;
import com.movitech.mbox.modules.sys.entity.UserModifyRecord;

/**
 * 用户DAO接口
 * @author ThinkGem
 * @version 2014-05-16
 */
@MyBatisDao
public interface UserModifyRecordDao extends CrudDao<UserModifyRecord> {
    public void insertFrontPermission(@Param("id")String id, @Param("userID")String userID, @Param("userNo")String userNo, @Param("permission")String permission);
    public void deleteFrontPermission(@Param("userNO")String userNO);
    public List<String> getFrontPermission(@Param("userID")String userID);
}
