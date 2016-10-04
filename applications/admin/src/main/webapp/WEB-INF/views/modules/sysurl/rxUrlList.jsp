<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.movitech.mbox.modules.util.PropertiesUtil"%>  
<html>
<head>
    <title><%= PropertiesUtil.showGJH("系统链接配置管理")%></title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function() {
            
        });
        function page(n,s){
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }
    </script>
</head>
<body>
    <ul class="nav nav-tabs">
        <li class="active"><a href="${ctx}/sysurl/rxUrl/"><%= PropertiesUtil.showGJH("系统链接配置列表")%></a></li>
        <!-- <shiro:hasPermission name="sysurl:rxUrl:edit"><li><a href="${ctx}/sysurl/rxUrl/form"><%= PropertiesUtil.showGJH("系统链接配置添加")%></a></li></shiro:hasPermission>-->
    </ul>
    <form:form id="searchForm" modelAttribute="rxUrl" action="${ctx}/sysurl/rxUrl/" method="post" class="breadcrumb form-search">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <ul class="ul-form">
            <li><label><%= PropertiesUtil.showGJH("链接名称：")%></label>
                <form:input path="name" htmlEscape="false" maxlength="20" class="input-medium"/>
            </li>
            <li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
            <li class="clearfix"></li>
        </ul>
    </form:form>
    <sys:message content="${message}"/>
    <table id="contentTable" class="table table-striped table-bordered table-condensed">
        <thead>
            <tr>
                <th><%= PropertiesUtil.showGJH("链接名称")%></th>
                <th><%= PropertiesUtil.showGJH("url链接")%></th>
                <shiro:hasPermission name="sysurl:rxUrl:edit"><th>操作</th></shiro:hasPermission>
            </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.list}" var="rxUrl">
            <tr>
                <td><a href="${ctx}/sysurl/rxUrl/form?id=${rxUrl.id}">
                    ${rxUrl.name}
                </a></td>
                <td>
                    ${rxUrl.url}
                </td>
                <shiro:hasPermission name="sysurl:rxUrl:edit"><td>
                    <a href="${ctx}/sysurl/rxUrl/form?id=${rxUrl.id}">修改</a>
                    <!-- <a href="${ctx}/sysurl/rxUrl/delete?id=${rxUrl.id}" onclick="return confirmx('确认要删除该系统链接配置吗？', this.href)">删除</a>-->
                </td></shiro:hasPermission>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div class="pagination">${page}</div>
</body>
</html>