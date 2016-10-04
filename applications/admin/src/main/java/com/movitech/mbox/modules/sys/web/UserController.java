/**
 * 
 */
package com.movitech.mbox.modules.sys.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.movitech.mbox.common.beanvalidator.BeanValidators;
import com.movitech.mbox.common.config.Global;
import com.movitech.mbox.common.persistence.Page;
import com.movitech.mbox.common.utils.DateUtils;
import com.movitech.mbox.common.utils.IdGen;
import com.movitech.mbox.common.utils.StringUtils;
import com.movitech.mbox.common.utils.excel.ExportExcel;
import com.movitech.mbox.common.utils.excel.ImportExcel;
import com.movitech.mbox.common.web.BaseController;
import com.movitech.mbox.modules.sys.entity.Role;
import com.movitech.mbox.modules.sys.entity.User;
import com.movitech.mbox.modules.sys.entity.UserModifyRecord;
import com.movitech.mbox.modules.sys.service.SystemService;
import com.movitech.mbox.modules.sys.utils.UserUtils;

/**
 * 用户Controller
 * @author ThinkGem
 * @version 2013-8-29
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/user")
public class UserController extends BaseController {

    @Autowired
    private SystemService systemService;
    
    @ModelAttribute
    public User get(@RequestParam(required=false) String id) {
        if (StringUtils.isNotBlank(id)){
            return systemService.getUser(id);
        }else{
            return new User();
        }
    }

    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = {"index"})
    public String index(User user, Model model) {
        return "modules/sys/userIndex";
    }

    @RequestMapping(value = {"indexFront"})
    public String indexFront(User user, Model model) {
        return "modules/sys/userIndexFront";
    }

    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = {"list", ""})
    public String list(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<User> page = systemService.findUser(new Page<User>(request, response), user);
        model.addAttribute("page", page);
        return "modules/sys/userList";
    }
    
    @ResponseBody
    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = {"listData"})
    public Page<User> listData(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<User> page = systemService.findUser(new Page<User>(request, response), user);
        return page;
    }

    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = "form")
    public String form(User user, Model model) {
//        if (user.getCompany()==null || user.getCompany().getId()==null){
//            user.setCompany(UserUtils.getUser().getCompany());
//        }
//        if (user.getOffice()==null || user.getOffice().getId()==null){
//            user.setOffice(UserUtils.getUser().getOffice());
//        }
        model.addAttribute("user", user);
        model.addAttribute("allRoles", systemService.findAllRole());
//        return "modules/sys/userForm";
        return "modules/sys/userRoleForm";
    }

    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "save")
    public String save(User user, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        if(Global.isDemoMode()){
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + adminPath + "/sys/user/list?repage";
        }
        // 修正引用赋值问题，不知道为何，Company和Office引用的一个实例地址，修改了一个，另外一个跟着修改。
//        user.setCompany(new Office(request.getParameter("company.id")));
//        user.setOffice(new Office(request.getParameter("office.id")));
//        if(request.getParameter("company.id")==null){
//        	user.setCompany(UserUtils.getUser().getCompany());
//        }
//        if(request.getParameter("office.id")==null){
//        	user.setOffice(UserUtils.getUser().getOffice());
//        }
        // 如果新密码为空，则不更换密码
        if (StringUtils.isNotBlank(user.getNewPassword())) {
            user.setPassword(SystemService.entryptPassword(user.getNewPassword()));
        }
        if (!beanValidator(model, user)){
            return form(user, model);
        }
        if (!"true".equals(checkLoginName(user.getOldLoginName(), user.getLoginName()))){
            addMessage(model, "保存用户'" + user.getLoginName() + "'失败，登录名已存在");
            return form(user, model);
        }
        // 角色数据有效性验证，过滤不在授权内的角色
        List<Role> roleList = Lists.newArrayList();
        List<String> roleIdList = user.getRoleIdList();
        StringBuffer userRoles = new StringBuffer();
        for (Role r : systemService.findAllRole()){
            if (roleIdList.contains(r.getId())){
                roleList.add(r);
				userRoles.append(r.getName());
				userRoles.append(";");
            }
        }
        user.setRoleList(roleList);
        // 保存用户信息
        systemService.saveUser(user);
        // 清除当前用户缓存
        if (user.getLoginName().equals(UserUtils.getUser().getLoginName())){
            UserUtils.clearCache();
            //UserUtils.getCacheMap().clear();
        }
        addMessage(redirectAttributes, "保存用户'" + user.getLoginName() + "'成功");
        return "redirect:" + adminPath + "/sys/user/list?repage";
    }
    
    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "delete")
    public String delete(User user, RedirectAttributes redirectAttributes) {
        if(Global.isDemoMode()){
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + adminPath + "/sys/user/list?repage";
        }
        if (UserUtils.getUser().getId().equals(user.getId())){
            addMessage(redirectAttributes, "删除用户失败, 不允许删除当前用户");
        }else if (User.isAdmin(user.getId())){
            addMessage(redirectAttributes, "删除用户失败, 不允许删除超级管理员用户");
        }else{
            systemService.deleteUser(user);
            addMessage(redirectAttributes, "删除用户成功");
        }
        return "redirect:" + adminPath + "/sys/user/list?repage";
    }
    
    /**
     * 导出用户数据
     * @param user
     * @param request
     * @param response
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(User user, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "userInfo"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<User> page = systemService.findUser(new Page<User>(request, response, -1), user);
            new ExportExcel("用户数据", User.class).setDataList(page.getList()).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导出用户失败！失败信息："+e.getMessage());
        }
        return "redirect:" + adminPath + "/sys/user/list?repage";
    }

    /**
     * 导入用户数据
     * @param file
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        if(Global.isDemoMode()){
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + adminPath + "/sys/user/list?repage";
        }
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<User> list = ei.getDataList(User.class);
            for (User user : list){
                try{
                    if (systemService.getUserNumByLoginName(user.getLoginName()) > 0){
                        user.setPassword(SystemService.entryptPassword(user.getPassword()));
                        BeanValidators.validateWithException(validator, user);
                        systemService.saveUser(user);
                        successNum++;
                    }else{
                        failureMsg.append("<br/>登录名 "+user.getLoginName()+" 已存在; ");
                        failureNum++;
                    }
                }catch(ConstraintViolationException ex){
                    failureMsg.append("<br/>登录名 "+user.getLoginName()+" 导入失败：");
                    List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
                    for (String message : messageList){
                        failureMsg.append(message+"; ");
                        failureNum++;
                    }
                }catch (Exception ex) {
                    failureMsg.append("<br/>登录名 "+user.getLoginName()+" 导入失败："+ex.getMessage());
                }
            }
            if (failureNum>0){
                failureMsg.insert(0, "，失败 "+failureNum+" 条用户，导入信息如下：");
            }
            addMessage(redirectAttributes, "已成功导入 "+successNum+" 条用户"+failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入用户失败！失败信息："+e.getMessage());
        }
        return "redirect:" + adminPath + "/sys/user/indexFront?repage";
    }
    
    /**
     * 下载导入用户数据模板
     * @param response
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "userInfoImport.xlsx";
            List<User> list = Lists.newArrayList();
            // 新增导出数据
            User user = new User();
            list.add(user);
            new ExportExcel("用户数据(请输入所有内容，权限输入Y代表有权限 N代表没有权限)", User.class, 2).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
        }
        return "redirect:" + adminPath + "/sys/user/indexFront?repage";
    }

    /**
     * 验证登录名是否有效
     * @param oldLoginName
     * @param loginName
     * @return
     */
    @ResponseBody
    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "checkLoginName")
    public String checkLoginName(String oldLoginName, String loginName) {
        if (loginName !=null && loginName.equals(oldLoginName)) {
            return "true";
        } else if (loginName !=null && systemService.getUserByLoginName(loginName) == null) {
            return "true";
        }
        return "false";
    }

    /**
     * 用户信息显示及保存
     * @param user
     * @param model
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "info")
    public String info(User user, HttpServletResponse response, Model model) {
        User currentUser = UserUtils.getUser();
        if (StringUtils.isNotBlank(user.getName())){
            if(Global.isDemoMode()){
                model.addAttribute("message", "演示模式，不允许操作！");
                return "modules/sys/userInfo";
            }
            currentUser.setEmail(user.getEmail());
            currentUser.setPhone(user.getPhone());
//            currentUser.setMobile(user.getMobile());
            currentUser.setRemarks(user.getRemarks());
            currentUser.setPhoto(user.getPhoto());
            systemService.updateUserInfo(currentUser);
            model.addAttribute("message", "保存用户信息成功");
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("Global", new Global());
        return "modules/sys/userInfo";
    }

    /**
     * 返回用户信息
     * @return
     */
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "infoData")
    public User infoData() {
        return UserUtils.getUser();
    }
    
    /**
     * 修改个人用户密码
     * @param oldPassword
     * @param newPassword
     * @param model
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "modifyPwd")
    public String modifyPwd(String oldPassword, String newPassword, Model model) {
        User user = UserUtils.getUser();
        if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)){
            if(Global.isDemoMode()){
                model.addAttribute("message", "演示模式，不允许操作！");
                return "modules/sys/userModifyPwd";
            }
            if (SystemService.validatePassword(oldPassword, user.getPassword())){
                systemService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
                model.addAttribute("message", "修改密码成功");
            }else{
                model.addAttribute("message", "修改密码失败，旧密码错误");
            }
        }
        model.addAttribute("user", user);
        return "modules/sys/userModifyPwd";
    }
    
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required=false) String officeId, HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<User> list = systemService.findUserByOfficeId(officeId);
        for (int i=0; i<list.size(); i++){
            User e = list.get(i);
            Map<String, Object> map = Maps.newHashMap();
            map.put("id", "u_"+e.getId());
            map.put("pId", officeId);
            map.put("name", StringUtils.replace(e.getName(), " ", ""));
            mapList.add(map);
        }
        return mapList;
    }
    
//    @InitBinder
//    public void initBinder(WebDataBinder b) {
//        b.registerCustomEditor(List.class, "roleList", new PropertyEditorSupport(){
//            @Autowired
//            private SystemService systemService;
//            @Override
//            public void setAsText(String text) throws IllegalArgumentException {
//                String[] ids = StringUtils.split(text, ",");
//                List<Role> roles = new ArrayList<Role>();
//                for (String id : ids) {
//                    Role role = systemService.getRole(Long.valueOf(id));
//                    roles.add(role);
//                }
//                setValue(roles);
//            }
//            @Override
//            public String getAsText() {
//                return Collections3.extractToString((List) getValue(), "id", ",");
//            }
//        });
//    }
    


    /**
     * 导入用户数据
     * @param file
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "myImportNew", method=RequestMethod.POST)
    public String myImportNew(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<User> list = ei.getDataList(User.class);
            User login_User = UserUtils.getUser();
            // 读取excel的时候，最后一个会是空数据。去掉
            list.remove(list.size() - 1);
            for (User user : list){
                try{
                    // 如新的登录名称用员工编号，而且员工编号和英文名唯一
                    user.setLoginName(user.getNo());
                    if(systemService.getByEnglishName(user) != null) {
                        failureMsg.append("<br/>英文名 已存在; ");
                        failureNum++;
                    } else {
                        if (systemService.getUserNumByLoginName(user.getLoginName()) < 1){
                            BeanValidators.validateWithException(validator, user);
                            UserModifyRecord umr = new UserModifyRecord();
                            BeanUtils.copyProperties(user, umr);
                            umr.setModifyType("insert");
                            umr.setCreateNo(login_User.getNo());
                            umr.setId(IdGen.uuid());
//                            umr.setCreateEglishName(login_User.getEnglishName());
                            umr.setCreateNo(login_User.getNo());
                            
                            // 密码加密
                            user.setPassword(SystemService.entryptPassword(user.getPassword()));
                            systemService.saveUser(user);
                            // 保存修改记录
                            systemService.saveUserModifyRecord(umr);
                            successNum++;
                        }else{
                            failureMsg.append("<br/>员工编号 "+user.getLoginName()+" 已存在; ");
                            failureNum++;
                        }
                    }
                }catch(ConstraintViolationException ex){
                    failureMsg.append("<br/>员工编号 "+user.getLoginName()+" 导入失败：");
                    List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
                    for (String message : messageList){
                        failureMsg.append(message+"; ");
                    }
                    failureNum++;
                }catch (Exception ex) {
                    failureMsg.append("<br/>员工编号 "+user.getLoginName()+" 导入失败："+ex.getMessage());
                    failureNum++;
                }
            }
            if (failureNum>0){
                failureMsg.insert(0, "，失败 "+failureNum+" 条用户，导入信息如下：");
            }
            addMessage(redirectAttributes, "已成功导入 "+successNum+" 条用户"+failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入用户失败！失败信息："+e.getMessage());
        }
        return "redirect:" + adminPath + "/sys/user/indexFront?repage";
    }
    
    /**
     * 导入用户修改数据
     * @param file
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "myImportUpdate", method=RequestMethod.POST)
    public String myImportUpdate(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<User> list = ei.getDataList(User.class);
            User login_User = UserUtils.getUser();
            // 读取excel的时候，最后一个会是空数据。去掉
            list.remove(list.size() - 1);
            for (User user : list){
                try{
                    // 如新的登录名称用员工编号，而且员工编号和英文名唯一
                    user.setLoginName(user.getNo());
                    if ("false".equals(checkLoginName("", user.getLoginName()))){
                        BeanValidators.validateWithException(validator, user);
                        UserModifyRecord umr = new UserModifyRecord();
                        BeanUtils.copyProperties(user, umr);
                        umr.setModifyType("update");
                        umr.setCreateNo(login_User.getNo());
                        umr.setId(IdGen.uuid());
//                        umr.setCreateEglishName(login_User.getEnglishName());
                        umr.setCreateNo(login_User.getNo());
                        
                        // 密码加密
                        user.setPassword(SystemService.entryptPassword(user.getPassword()));
                        systemService.updateUser(user);
                        // 保存修改记录
                        systemService.saveUserModifyRecord(umr);
                        successNum++;
                    }else{
                        failureMsg.append("<br/>员工编号 "+user.getLoginName()+" 不存在; ");
                        failureNum++;
                    }
                }catch(ConstraintViolationException ex){
                    failureMsg.append("<br/>员工编号 "+user.getLoginName()+" 导入失败：");
                    List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
                    for (String message : messageList){
                        failureMsg.append(message+"; ");
                    }
                    failureNum++;
                }catch (Exception ex) {
                    failureMsg.append("<br/>员工编号 "+user.getLoginName()+" 导入失败："+ex.getMessage());
                }
            }
            if (failureNum>0){
                failureMsg.insert(0, "，失败 "+failureNum+" 条用户，导入信息如下：");
            }
            addMessage(redirectAttributes, "已成功更新 "+successNum+" 条用户"+failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入用户失败！失败信息："+e.getMessage());
        }
        return "redirect:" + adminPath + "/sys/user/indexFront?repage";
    }
    
    /**
     * 导入用户修改数据
     * @param file
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "myImportDelete", method=RequestMethod.POST)
    public String myImportDelete(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<User> list = ei.getDataList(User.class);
            User login_User = UserUtils.getUser();
            // 读取excel的时候，最后一个会是空数据。去掉
            list.remove(list.size() - 1);
            for (User user : list){
                try{
                    // 如新的登录名称用员工编号，而且员工编号和英文名唯一
                    if ("false".equals(checkLoginName("", user.getLoginName()))){
                        BeanValidators.validateWithException(validator, user);
                        UserModifyRecord umr = new UserModifyRecord();
                        BeanUtils.copyProperties(user, umr);
                        umr.setModifyType("delete");
                        umr.setCreateNo(login_User.getNo());
//                        umr.setCreateEglishName(login_User.getEnglishName());
                        umr.setCreateNo(login_User.getNo());
                        
                        int ret = systemService.deleteUserByNo(user);
                        if(ret == 1) {
                            // 保存修改记录
                            systemService.saveUserModifyRecord(umr);
                            successNum++;
                        }
                    }else{
                        failureMsg.append("<br/>员工编号 "+user.getLoginName()+" 不存在; ");
                        failureNum++;
                    }
                }catch(ConstraintViolationException ex){
                    failureMsg.append("<br/>员工编号 "+user.getLoginName()+" 导入失败：");
                    List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
                    for (String message : messageList){
                        failureMsg.append(message+"; ");
                    }
                    failureNum++;
                }catch (Exception ex) {
                    failureMsg.append("<br/>员工编号 "+user.getLoginName()+" 导入失败："+ex.getMessage());
                }
            }
            if (failureNum>0){
                failureMsg.insert(0, "，失败 "+failureNum+" 条用户，导入信息如下：");
            }
            addMessage(redirectAttributes, "已成功删除 "+successNum+" 条用户"+failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入用户失败！失败信息："+e.getMessage());
        }
        return "redirect:" + adminPath + "/sys/user/indexFront?repage";
    }

    
    /**
     * 导出用户数据
     * @param user
     * @param request
     * @param response
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = "exportUserInfo", method=RequestMethod.POST)
    public String exportUserInfoFile(User user, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "userInfo"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<User> page = systemService.findUser(new Page<User>(request, response, -1), user);
            new ExportExcel("用户数据", User.class).setDataList(page.getList()).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导出失败！失败信息："+e.getMessage());
        }
        return "redirect:" + adminPath + "/sys/user/list?repage";
    }

    
    /**
     * 导出用户修改数据
     * @param user
     * @param request
     * @param response
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = "exportUserModify", method=RequestMethod.POST)
    public String exportUserModifyFile(User user, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "updateRecords"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            List<UserModifyRecord> list = systemService.findAllModify();
            new ExportExcel("修改记录", UserModifyRecord.class).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导出失败！失败信息："+e.getMessage());
        }
        return "redirect:" + adminPath + "/sys/user/list?repage";
    }
}
