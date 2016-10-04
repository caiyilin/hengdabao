<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.movitech.mbox.modules.util.PropertiesUtil"%>  
<html>
<head>
    <title><%= PropertiesUtil.showGJH("系统链接配置管理")%></title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function() {
            //$("#name").focus();
            $("#inputForm").validate({
                submitHandler: function(form){
                    loading('正在提交，请稍等...');
                    form.submit();
                },
                errorContainer: "#messageBox",
                errorPlacement: function(error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
        });
        function setButDisable(obj) {
            if($("#inputForm").valid()) {
                $(obj).attr("disabled", true);
                // 谷歌浏览器不会提交修改
                $("#inputForm").submit();
            }
        }
    </script>
</head>
<body>
    <ul class="nav nav-tabs">
        <li><a href="${ctx}/sysurl/rxUrl/"><%= PropertiesUtil.showGJH("系统链接配置列表")%></a></li>
        <li class="active"><a href="${ctx}/sysurl/rxUrl/form?id=${rxUrl.id}"><%= PropertiesUtil.showGJH("系统链接配置")%><shiro:hasPermission name="sysurl:rxUrl:edit">${not empty rxUrl.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sysurl:rxUrl:edit">查看</shiro:lacksPermission></a></li>
    </ul><br/>
    <form:form id="inputForm" modelAttribute="rxUrl" action="${ctx}/sysurl/rxUrl/save" method="post" class="form-horizontal">
        <form:hidden path="id"/>
        <sys:message content="${message}"/>
        <!-- 
        <div class="control-group">
            <label class="control-label"><%= PropertiesUtil.showGJH("名称编码：")%></label>
            <div class="controls">
                <form:input path="code" htmlEscape="false" maxlength="20" class="input-xlarge "/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label"><%= PropertiesUtil.showGJH("链接名称：")%></label>
            <div class="controls">
                <form:input path="name" htmlEscape="false" maxlength="20" class="input-xlarge "/>
            </div>
        </div> 
        -->
        <div class="control-group">
            <label class="control-label"><%= PropertiesUtil.showGJH("url链接：")%></label>
            <div class="controls">
                <form:input path="url" htmlEscape="false" maxlength="200" class="input-xlarge "/>
            </div>
        </div>
        <div class="form-actions">
            <shiro:hasPermission name="sysurl:rxUrl:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存" onclick="setButDisable(this)"/>&nbsp;</shiro:hasPermission>
            <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
        </div>
    </form:form>
</body>
</html>