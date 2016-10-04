/**
 * 
 */
package com.movitech.mbox.modules.sys.service;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.apache.shiro.session.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.movitech.mbox.common.config.Global;
import com.movitech.mbox.common.persistence.Page;
import com.movitech.mbox.common.security.Digests;
import com.movitech.mbox.common.security.shiro.session.SessionDAO;
import com.movitech.mbox.common.service.BaseService;
import com.movitech.mbox.common.utils.CacheUtils;
import com.movitech.mbox.common.utils.Encodes;
import com.movitech.mbox.common.utils.IdGen;
import com.movitech.mbox.common.utils.StringUtils;
import com.movitech.mbox.common.web.Servlets;
import com.movitech.mbox.modules.sys.dao.MenuDao;
import com.movitech.mbox.modules.sys.dao.RoleDao;
import com.movitech.mbox.modules.sys.dao.UserDao;
import com.movitech.mbox.modules.sys.dao.UserModifyRecordDao;
import com.movitech.mbox.modules.sys.entity.FrontPermission;
import com.movitech.mbox.modules.sys.entity.Menu;
import com.movitech.mbox.modules.sys.entity.Role;
import com.movitech.mbox.modules.sys.entity.SeviceUser;
import com.movitech.mbox.modules.sys.entity.User;
import com.movitech.mbox.modules.sys.entity.UserModifyRecord;
import com.movitech.mbox.modules.sys.security.SystemAuthorizingRealm;
import com.movitech.mbox.modules.sys.utils.LogUtils;
import com.movitech.mbox.modules.sys.utils.UserUtils;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 * @author ThinkGem
 * @version 2013-12-05
 */
@Service
@Transactional(readOnly = true)
public class SystemService extends BaseService implements InitializingBean {
    
    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_INTERATIONS = 1024;
    public static final int SALT_SIZE = 8;
    
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private SessionDAO sessionDao;
    @SuppressWarnings("unused")
	@Autowired
    private SystemAuthorizingRealm systemRealm;
    @Autowired
    private UserModifyRecordDao userModifyRecordDao;
    
    public SessionDAO getSessionDao() {
        return sessionDao;
    }


    private IdentityService identityService;

    //-- User Service --//
    
    /**
     * 获取用户
     * @param id
     * @return
     */
    public User getUser(String id) {
        return UserUtils.get(id);
    }
    
    /**
     * 获取数据库用户信息
     * @param id
     * @return
     */
    public User getUserFromDb(String id) {
        return userDao.get(id);
    }

    /**
     * 根据登录名获取用户
     * @param loginName
     * @return
     */
    public User getUserByLoginName(String loginName) {
        return UserUtils.getByLoginName(loginName);
    }
    
    public Page<User> findUser(Page<User> page, User user) {
        // 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
        user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "a"));
        // 设置分页参数
        user.setPage(page);
        // 执行分页查询
        page.setList(userDao.findList(user));
        return page;
    }
    
    /**
     * 无分页查询人员列表
     * @param user
     * @return
     */
    public List<User> findUser(User user){
        // 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
        user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "a"));
        List<User> list = userDao.findList(user);
        return list;
    }
    
    /**
     * 查询修改信息
     * @param user
     * @return
     */
    @SuppressWarnings("deprecation")
	public List<UserModifyRecord> findAllModify(){
        List<UserModifyRecord> list = userModifyRecordDao.findAllList();
        return list;
    }

    /**
     * 通过部门ID获取用户列表，仅返回用户id和name（树查询用户时用）
     * @param user
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<User> findUserByOfficeId(String officeId) {
        List<User> list = (List<User>)CacheUtils.get(UserUtils.USER_CACHE, UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + officeId);
        if (list == null){
            User user = new User();
//            user.setOffice(new Office(officeId));
            list = userDao.findUserByOfficeId(user);
            CacheUtils.put(UserUtils.USER_CACHE, UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + officeId, list);
        }
        return list;
    }
    
    @Transactional(readOnly = false)
    public void saveUser(User user) {
        if (StringUtils.isBlank(user.getId())){
            user.preInsert();
            userDao.insert(user);
            // 保存前台权限
//            FrontPermission fp = new FrontPermission();
//            BeanUtils.copyProperties(user, fp);
//            this.saveFrontPermissionPre(fp , user.getId(), user.getNo());
            userDao.insertUserRole(user);
        }else{
            // 清除原用户机构用户缓存
//            User oldUser = userDao.get(user.getId());
//            if (oldUser.getOffice() != null && oldUser.getOffice().getId() != null){
//                CacheUtils.remove(UserUtils.USER_CACHE, UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + oldUser.getOffice().getId());
//            }
            // 更新用户数据
            user.preUpdate();
            userDao.update(user);
        }
        if (StringUtils.isNotBlank(user.getId())){
            // 更新用户与角色关联
            if (user.getRoleList() != null && user.getRoleList().size() > 0){
                userDao.deleteUserRole(user);
                userDao.insertUserRole(user);
            }
            // 角色更新放到用户页面分配，不在新增导入的时候分配
            /*else{
                throw new ServiceException(user.getLoginName() + "没有设置角色！");
            }*/
            // 将当前用户同步到Activiti
            saveActivitiUser(user);
            // 清除用户缓存
            UserUtils.clearCache(user);
            CacheUtils.remove(UserUtils.USER_CACHE, UserUtils.USER_CACHE_ID_ + user.getId());
//            // 清除权限缓存
//            systemRealm.clearAllCachedAuthorizationInfo();
        }
    }
    
    @Transactional(readOnly = false)
    public void updateUser(User user) {
        user.preUpdate();
        userDao.updateByNo(user);
        // 根据用户编号获取用户ID
        user = userDao.getByNo(user);
        // 保存前台权限
//        FrontPermission fp = new FrontPermission();
        
        // 清除用户缓存
        UserUtils.clearCache(user);
        CacheUtils.remove(UserUtils.USER_CACHE, UserUtils.USER_CACHE_ID_ + user.getId());
        
//        BeanUtils.copyProperties(user, fp);
//        this.saveFrontPermissionPre(fp , user.getId(), user.getNo());
    }
    
    /**
     * 前台修改用户方法，后台的角色列表不需要修改，不然显示会有问题
     * @param user
     */
    @Transactional(readOnly = false)
    public void updateUserInfo(User user) {
        user.preUpdate();
        userDao.updateUserInfo(user);
        // 同步到Activiti
        deleteActivitiUser(user);
        // 清除用户缓存
        UserUtils.clearCache(user);
        CacheUtils.remove(UserUtils.USER_CACHE, UserUtils.USER_CACHE_ID_ + user.getId());
//        // 清除权限缓存
//        systemRealm.clearAllCachedAuthorizationInfo();
    }
    
    @Transactional(readOnly = false)
    public void deleteUser(User user) {
        userDao.delete(user);
        // 同步到Activiti
        deleteActivitiUser(user);
        // 清除用户缓存
        UserUtils.clearCache(user);
        CacheUtils.remove(UserUtils.USER_CACHE, UserUtils.USER_CACHE_ID_ + user.getId());
//        // 清除权限缓存
//        systemRealm.clearAllCachedAuthorizationInfo();
    }
    
    @Transactional(readOnly = false)
    public int deleteUserByNo(User user) {
        int ret = 0;
        // 获取带ID的用户信息，没有ID无法清除缓存
        user = userDao.getByNo(user);
        
        ret = userDao.deleteByNo(user);
        // 同步到Activiti
        deleteActivitiUser(user);
        // 清除用户缓存
        UserUtils.clearCache(user);
        CacheUtils.remove(UserUtils.USER_CACHE, UserUtils.USER_CACHE_ID_ + user.getId());
        // 清除前台权限
//        this.deleteFrontPermission(user.getNo());
        // 清除后台基本权限
        userDao.deleteRolesByNo(user.getNo());
        return ret;
    }
    
    @Transactional(readOnly = false)
    public void updatePasswordById(String id, String loginName, String newPassword) {
        User user = new User(id);
        user.setPassword(entryptPassword(newPassword));
        userDao.updatePasswordById(user);
        // 清除用户缓存
        UserUtils.clearCache(user);
        CacheUtils.remove(UserUtils.USER_CACHE, UserUtils.USER_CACHE_ID_ + user.getId());
        UserUtils.clearCache(user);
//        // 清除权限缓存
//        systemRealm.clearAllCachedAuthorizationInfo();
    }
    
    @Transactional(readOnly = false)
    public void updateUserLoginInfo(User user) {
        // 保存上次登录信息
        user.setOldLoginIp(user.getLoginIp());
        user.setOldLoginDate(user.getLoginDate());
        // 更新本次登录信息
        user.setLoginIp(StringUtils.getRemoteAddr(Servlets.getRequest()));
        user.setLoginDate(new Date());
        userDao.updateLoginInfo(user);
    }
    
    /**
     * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
     */
    public static String entryptPassword(String plainPassword) {
        byte[] salt = Digests.generateSalt(SALT_SIZE);
        byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
        return Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword);
    }
    
    /**
     * 验证密码
     * @param plainPassword 明文密码
     * @param password 密文密码
     * @return 验证成功返回true
     */
    public static boolean validatePassword(String plainPassword, String password) {
        byte[] salt = Encodes.decodeHex(password.substring(0,16));
        byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
        return password.equals(Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword));
    }
    
    /**
     * 获得活动会话
     * @return
     */
    public Collection<Session> getActiveSessions(){
        return sessionDao.getActiveSessions(false);
    }
    
    //-- Role Service --//
    
    public Role getRole(String id) {
        return roleDao.get(id);
    }

    public Role getRoleByName(String name) {
        Role r = new Role();
        r.setName(name);
        return roleDao.getByName(r);
    }
    
    public Role getRoleByEnname(String enname) {
        Role r = new Role();
        r.setEnname(enname);
        return roleDao.getByEnname(r);
    }
    
    public List<Role> findRole(Role role){
        return roleDao.findList(role);
    }
    
    public List<Role> findAllRole(){
        return UserUtils.getRoleList();
    }
    
    @Transactional(readOnly = false)
    public void saveRole(Role role) {
        if (StringUtils.isBlank(role.getId())){
            role.preInsert();
//            if(role.getOffice()==null){
//            	role.setOffice(UserUtils.getUser().getOffice());
//            }
            roleDao.insert(role);
            // 同步到Activiti
            saveActivitiGroup(role);
        }else{
            role.preUpdate();
            roleDao.update(role);
        }
        // 更新角色与菜单关联
        roleDao.deleteRoleMenu(role);
        if (role.getMenuList().size() > 0){
            roleDao.insertRoleMenu(role);
        }
        // 更新角色与部门关联
        roleDao.deleteRoleOffice(role);
        if (role.getOfficeList().size() > 0){
            roleDao.insertRoleOffice(role);
        }
        // 同步到Activiti
        saveActivitiGroup(role);
        // 清除用户角色缓存
        UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
//        // 清除权限缓存
//        systemRealm.clearAllCachedAuthorizationInfo();
    }

    @Transactional(readOnly = false)
    public void deleteRole(Role role) {
        roleDao.delete(role);
        // 同步到Activiti
        deleteActivitiGroup(role);
        // 清除用户角色缓存
        UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
//        // 清除权限缓存
//        systemRealm.clearAllCachedAuthorizationInfo();
    }
    
    @Transactional(readOnly = false)
    public Boolean outUserInRole(Role role, User user) {
        List<Role> roles = user.getRoleList();
        for (Role e : roles){
            if (e.getId().equals(role.getId())){
                roles.remove(e);
                saveUser(user);
                return true;
            }
        }
        return false;
    }
    
    @Transactional(readOnly = false)
    public User assignUserToRole(Role role, User user) {
        if (user == null){
            return null;
        }
        List<String> roleIds = user.getRoleIdList();
        if (roleIds.contains(role.getId())) {
            return null;
        }
        user.getRoleList().add(role);
        saveUser(user);
        return user;
    }

    //-- Menu Service --//
    
    public Menu getMenu(String id) {
        return menuDao.get(id);
    }

    public List<Menu> findAllMenu(){
        return UserUtils.getMenuList();
    }
    
    @Transactional(readOnly = false)
    public void saveMenu(Menu menu) {
        
        // 获取父节点实体
        menu.setParent(this.getMenu(menu.getParent().getId()));
        
        // 获取修改前的parentIds，用于更新子节点的parentIds
        String oldParentIds = menu.getParentIds(); 
        
        // 设置新的父节点串
        menu.setParentIds(menu.getParent().getParentIds()+menu.getParent().getId()+",");

        // 保存或更新实体
        if (StringUtils.isBlank(menu.getId())){
            menu.preInsert();
            menuDao.insert(menu);
        }else{
            menu.preUpdate();
            menuDao.update(menu);
        }
        
        // 更新子节点 parentIds
        Menu m = new Menu();
        m.setParentIds("%,"+menu.getId()+",%");
        List<Menu> list = menuDao.findByParentIdsLike(m);
        for (Menu e : list){
            e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
            menuDao.updateParentIds(e);
        }
        // 清除用户菜单缓存
        UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
//        // 清除权限缓存
//        systemRealm.clearAllCachedAuthorizationInfo();
        // 清除日志相关缓存
        CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
    }

    @Transactional(readOnly = false)
    public void updateMenuSort(Menu menu) {
        menuDao.updateSort(menu);
        // 清除用户菜单缓存
        UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
//        // 清除权限缓存
//        systemRealm.clearAllCachedAuthorizationInfo();
        // 清除日志相关缓存
        CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
    }

    @Transactional(readOnly = false)
    public void deleteMenu(Menu menu) {
        menuDao.delete(menu);
        // 清除用户菜单缓存
        UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
//        // 清除权限缓存
//        systemRealm.clearAllCachedAuthorizationInfo();
        // 清除日志相关缓存
        CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
    }
    
    /**
     * 获取Key加载信息
     */
    public static boolean printKeyLoadMessage(){
        return true;
    }
    
    ///////////////// Synchronized to the Activiti //////////////////
    
    // 已废弃，同步见：ActGroupEntityServiceFactory.java、ActUserEntityServiceFactory.java

    /**
     * 是需要同步Activiti数据，如果从未同步过，则同步数据。
     */
    private static boolean isSynActivitiIndetity = true;
    public void afterPropertiesSet() throws Exception {
        if (!Global.isSynActivitiIndetity()){
            return;
        }
        if (isSynActivitiIndetity){
            isSynActivitiIndetity = false;
            // 同步角色数据
            List<Group> groupList = identityService.createGroupQuery().list();
            if (groupList.size() == 0){
                 Iterator<Role> roles = roleDao.findAllList(new Role()).iterator();
                 while(roles.hasNext()) {
                     Role role = roles.next();
                     saveActivitiGroup(role);
                 }
            }
             // 同步用户数据
            List<org.activiti.engine.identity.User> userList = identityService.createUserQuery().list();
            if (userList.size() == 0){
                 Iterator<User> users = userDao.findAllList(new User()).iterator();
                 while(users.hasNext()) {
                     saveActivitiUser(users.next());
                 }
            }
        }
    }
    
    private void saveActivitiGroup(Role role) {
        if (!Global.isSynActivitiIndetity()){
            return;
        }
        String groupId = role.getEnname();
        
        // 如果修改了英文名，则删除原Activiti角色
        if (StringUtils.isNotBlank(role.getOldEnname()) && !role.getOldEnname().equals(role.getEnname())){
            identityService.deleteGroup(role.getOldEnname());
        }
        
        Group group = identityService.createGroupQuery().groupId(groupId).singleResult();
        if (group == null) {
            group = identityService.newGroup(groupId);
        }
        group.setName(role.getName());
        group.setType(role.getRoleType());
        identityService.saveGroup(group);
        
        // 删除用户与用户组关系
        List<org.activiti.engine.identity.User> activitiUserList = identityService.createUserQuery().memberOfGroup(groupId).list();
        for (org.activiti.engine.identity.User activitiUser : activitiUserList){
            identityService.deleteMembership(activitiUser.getId(), groupId);
        }

        // 创建用户与用户组关系
        List<User> userList = findUser(new User(new Role(role.getId())));
        for (User e : userList){
            String userId = e.getLoginName();//ObjectUtils.toString(user.getId());
            // 如果该用户不存在，则创建一个
            org.activiti.engine.identity.User activitiUser = identityService.createUserQuery().userId(userId).singleResult();
            if (activitiUser == null){
                activitiUser = identityService.newUser(userId);
                activitiUser.setFirstName(e.getName());
                activitiUser.setLastName(StringUtils.EMPTY);
                activitiUser.setEmail(e.getEmail());
                activitiUser.setPassword(StringUtils.EMPTY);
                identityService.saveUser(activitiUser);
            }
            identityService.createMembership(userId, groupId);
        }
    }

    public void deleteActivitiGroup(Role role) {
        if (!Global.isSynActivitiIndetity()){
            return;
        }
        if(role!=null) {
            String groupId = role.getEnname();
            identityService.deleteGroup(groupId);
        }
    }

    private void saveActivitiUser(User user) {
        if (!Global.isSynActivitiIndetity()){
            return;
        }
        String userId = user.getLoginName();//ObjectUtils.toString(user.getId());
        org.activiti.engine.identity.User activitiUser = identityService.createUserQuery().userId(userId).singleResult();
        if (activitiUser == null) {
            activitiUser = identityService.newUser(userId);
        }
        activitiUser.setFirstName(user.getName());
        activitiUser.setLastName(StringUtils.EMPTY);
        activitiUser.setEmail(user.getEmail());
        activitiUser.setPassword(StringUtils.EMPTY);
        identityService.saveUser(activitiUser);
        
        // 删除用户与用户组关系
        List<Group> activitiGroups = identityService.createGroupQuery().groupMember(userId).list();
        for (Group group : activitiGroups) {
            identityService.deleteMembership(userId, group.getId());
        }
        // 创建用户与用户组关系
        for (Role role : user.getRoleList()) {
             String groupId = role.getEnname();
             // 如果该用户组不存在，则创建一个
             Group group = identityService.createGroupQuery().groupId(groupId).singleResult();
            if(group == null) {
                group = identityService.newGroup(groupId);
                group.setName(role.getName());
                group.setType(role.getRoleType());
                identityService.saveGroup(group);
            }
            identityService.createMembership(userId, role.getEnname());
        }
    }

    private void deleteActivitiUser(User user) {
        if (!Global.isSynActivitiIndetity()){
            return;
        }
        if(user!=null) {
            String userId = user.getLoginName();//ObjectUtils.toString(user.getId());
            identityService.deleteUser(userId);
        }
    }
    
    ///////////////// Synchronized to the Activiti end //////////////////
    
    
    public User getByEnglishName(User user) {
        return userDao.getByEnglishName(user);
    }

    @Transactional(readOnly = false)
    public void saveUserModifyRecord(UserModifyRecord entity) {
        entity.preInsert();
        userModifyRecordDao.insert(entity);
    }

    
    /**
     * 坚持服务奖查询
     * @return
     */
    public Page<SeviceUser> serviceInfo(Page<SeviceUser> page, SeviceUser para) {
        para.setPage(page);
        page.setList(userDao.serviceInfo(para));
        return page;
    }

    /**
     * 保存前台权限
     * @param userID
     * @param permission
     */
    @Transactional(readOnly = false)
    public void saveFrontPermission(String userID, String userNo, String permission) {
        String id = IdGen.uuid();
        userModifyRecordDao.insertFrontPermission(id, userID, userNo, permission);
    }

    /**
     * 删除前台权限
     * @param userID
     * @param permission
     */
    @Transactional(readOnly = false)
    public void deleteFrontPermission(String userNO) {
        userModifyRecordDao.deleteFrontPermission(userNO);
    }

    /**
     * 获取前台权限
     * @param userID
     */
    public List<String> getFrontPermission(String userID) {
        return userModifyRecordDao.getFrontPermission(userID);
    }
    
    /**
     * 保存用户前台权限
     * @param fp 权限实体
     * @param userID 用户ID
     */
    private void saveFrontPermissionPre(FrontPermission fp, String userID, String userNO) {
        // 先删除前台权限后再保存
        this.deleteFrontPermission(userNO);
        // 保存
        Method[] ms = fp.getClass().getMethods();
        for (int i = 0; i < ms.length; i++) {
            Method method = ms[i];
            if(method.getName().startsWith("get") && !method.getName().equals("getClass")) {
                try {
                    Object invoke = method.invoke(fp);
                    if(invoke != null) {
                        String value = invoke.toString().toUpperCase();
                        if("Y".equals(value)) {
                            this.saveFrontPermission(userID, userNO, method.getName().substring(3).toLowerCase());
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * 获取登录名数据库数量
     * @param user
     * @return
     */
    public int getUserNumByLoginName(String loginName) {
        return userDao.getUserNumByLoginName(loginName);
    }
    
}
