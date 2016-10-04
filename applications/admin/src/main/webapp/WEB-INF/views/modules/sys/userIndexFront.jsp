<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.movitech.mbox.modules.util.PropertiesUtil"%>  
<html>
<head>
    <title><%= PropertiesUtil.showGJH("前台用户管理")%></title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treeview.jsp" %>
    <style type="text/css">
        .ztree {overflow:auto;margin:0;_margin-top:10px;padding:10px 0 0 10px;}
    </style>
    <script type="text/javascript">
	    function imp(type) {
	    	var url = "";
	    	//新增导入
	    	if(type == "new") {
	    		var str = $("#impnew").val();
                top.$.jBox.confirm(str,"系统提示",function(v,h,f){
                    if(v=="ok"){
                        url = "${ctx}/sys/user/myImportNew";
                        document.importForm.action = url;
                        document.importForm.submit();
                    }
                },{buttonsFocus:1});
                top.$('.jbox-body .jbox-icon').css('top','55px');
	    	} else if(type == "update") {     //修改导入
                var str = $("#impupdate").val();
                top.$.jBox.confirm(str,"系统提示",function(v,h,f){
                    if(v=="ok"){
                        url = "${ctx}/sys/user/myImportUpdate";
                        document.importForm.action = url;
                        document.importForm.submit();
                    }
                },{buttonsFocus:1});
                top.$('.jbox-body .jbox-icon').css('top','55px');
	    	} else if(type == "delete") {     //删除导入
                var str = $("#impdelete").val();
                top.$.jBox.confirm(str,"系统提示",function(v,h,f){
                    if(v=="ok"){
                        url = "${ctx}/sys/user/myImportDelete";
                        document.importForm.action = url;
                        document.importForm.submit();
                    }
                },{buttonsFocus:1});
                top.$('.jbox-body .jbox-icon').css('top','55px');
            }
	    }
	    function expUserInfo() {
            var url = "";
            var str = $("#expUserInfo").val();
            top.$.jBox.confirm(str,"系统提示",function(v,h,f){
                if(v=="ok"){
                    url = "${ctx}/sys/user/exportUserInfo";
                    document.importForm.action = url;
                    document.importForm.submit();
                }
            },{buttonsFocus:1});
            top.$('.jbox-body .jbox-icon').css('top','55px');
	    }
        function expUserModify() {
            var url = "";
            var str = $("#expUserModify").val();
            top.$.jBox.confirm(str,"系统提示",function(v,h,f){
                if(v=="ok"){
                    url = "${ctx}/sys/user/exportUserModify";
                    document.importForm.action = url;
                    document.importForm.submit();
                }
            },{buttonsFocus:1});
            top.$('.jbox-body .jbox-icon').css('top','55px');
        }
    </script>
</head>
<body>
    <sys:message content="${message}"/>
    <input type="hidden" id="impnew" value='<%= PropertiesUtil.showGJH("确定新增导入吗？")%>'>
    <input type="hidden" id="impupdate" value='<%= PropertiesUtil.showGJH("确定修改导入吗？")%>'>
    <input type="hidden" id="impdelete" value='<%= PropertiesUtil.showGJH("确定删除导入吗？")%>'>
    <input type="hidden" id="expUserInfo" value='<%= PropertiesUtil.showGJH("确定信息导出吗？")%>'>
    <input type="hidden" id="expUserModify" value='<%= PropertiesUtil.showGJH("确定修改记录导出吗？")%>'>
    <div id="content" class="row-fluid">
        <div id="right" style="float:none;">
	        <form id="importForm" name="importForm" action="" method="post" enctype="multipart/form-data"
	            class="form-search" style="padding-left:20px;text-align:center;" onsubmit="loading('正在导入，请稍等...');"><br/>
	            <input id="uploadFile" name="file" type="file" style="width:330px"/><br/><br/>
	            <table style="border:0;width:90%" cellpadding="5">
                <tr>
				    <td>
				    <input id="btnImportSubmit" class="btn btn-primary" onclick="imp('new')" value='<%= PropertiesUtil.showGJH("新增导入")%>'/>
				    </td>
				    <td>
                    <input id="btnImportSubmit" class="btn btn-primary" onclick="imp('update')" value='<%= PropertiesUtil.showGJH("修改导入")%>'/>
                    </td>
				    <td>
                    <input id="btnImportSubmit" class="btn btn-primary" onclick="imp('delete')" value='<%= PropertiesUtil.showGJH("删除导入")%>'/>
                    </td>
				</tr>
				<tr class="">
				    <td>
				        <input id="btnImportSubmit" class="btn btn-primary" onclick="expUserInfo()" value='<%= PropertiesUtil.showGJH("信息导出")%>'/>
                    </td>
				    <td>
				        <input id="btnImportSubmit" class="btn btn-primary" onclick="expUserModify()" value='<%= PropertiesUtil.showGJH("修改记录导出")%>'/>
                    </td>
				    <td>
				        <a href="${ctx}/sys/user/import/template"><%= PropertiesUtil.showGJH("下载模板")%></a>
                    </td>
				</tr>
				</table>
	        </form>
        </div>
    </div>
</body>
</html>