]<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.movitech.mbox.modules.util.PropertiesUtil"%>  
<html>
<head>
    <title><%= PropertiesUtil.showGJH("角色管理")%></title>
    <meta name="decorator" content="default"/>
</head>
<body>
    <ul class="nav nav-tabs">
        <li class="active"><a href="${ctx}/sys/role/"><%= PropertiesUtil.showGJH("角色列表")%></a></li>
        <shiro:hasPermission name="sys:role:edit"><li><a href="${ctx}/sys/role/form"><%= PropertiesUtil.showGJH("角色添加")%></a></li></shiro:hasPermission>
    </ul>
    <sys:message content="${message}"/>
    <table id="contentTable" class="table table-striped table-bordered table-condensed">
        <tr><th><%= PropertiesUtil.showGJH("角色名称")%></th><th><%= PropertiesUtil.showGJH("备注")%></th>
        <shiro:hasPermission name="sys:role:edit"><th><%= PropertiesUtil.showGJH("操作")%></th></shiro:hasPermission></tr>
        <c:forEach items="${list}" var="role">
            <tr>
                <td><a href="form?id=${role.id}">${role.name}</a></td>
                <td><a href="form?id=${role.id}">${role.remarks}</a></td>
                <shiro:hasPermission name="sys:role:edit"><td>
                    <c:if test="${(role.sysData eq fns:getDictValue('是', 'yes_no', '1') && fns:getUser().admin)||!(role.sysData eq fns:getDictValue('是', 'yes_no', '1'))}">
                        <a href="${ctx}/sys/role/form?id=${role.id}"><%= PropertiesUtil.showGJH("修改")%></a>
                    </c:if>
                    
                    <c:if test="${role.id != '3'}">
                        <a href="${ctx}/sys/role/delete?id=${role.id}" onclick="return confirmx('确认要删除该角色吗？', this.href)"><%= PropertiesUtil.showGJH("删除")%></a>
                    </c:if>
                </td></shiro:hasPermission>    
            </tr>
        </c:forEach>
    </table>
</body>
</html>