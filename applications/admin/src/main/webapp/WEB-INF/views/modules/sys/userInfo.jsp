<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.movitech.mbox.modules.util.PropertiesUtil"%>  
<html>
<head>
    <title><%= PropertiesUtil.showGJH("个人信息")%></title>
    <meta name="decorator" content="default"/>
    <script src="${ctxStatic}/uploadify/jquery.uploadify.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function() {
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
            
	            //上传缩略图
	          $("#imgsrc-for-smallImg").uploadify({
	              height        : 30,
	              swf           : '${ctxStatic}/uploadify/uploadify.swf',
	              uploader      : '${pageContext.request.contextPath}/servlet/Upload',
	              width         : 300,
	              buttonText    : '点击上传',
	              queueSizeLimit: 1,
	              fileTypeExts  : '*.jpeg;*.png;*.jpg',
	              fileSizeLimit : 200,
	              onUploadComplete : function(file,swfuploadifyQueue){
	              },
	              onUploadSuccess : function(file,data,response) {//上传完成时触发（每个文件触发一次）
	                  if(response&&data){
	                      $("#smallImg-imgsrc-show").attr("src",data);
	                      $("#photo").val(data);
	                  }else{
	                      alert("上传失败！");
	                  }
	              }
	          });
        });
    </script>
</head>
<body>
    <ul class="nav nav-tabs">
        <li class="active"><a href="${ctx}/sys/user/info"><%= PropertiesUtil.showGJH("个人信息")%></a></li>
        <li><a href="${ctx}/sys/user/modifyPwd"><%= PropertiesUtil.showGJH("修改密码")%></a></li>
    </ul><br/>
    <form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/info" method="post" class="form-horizontal"><%--
        <form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
        <sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
        <sys:message content="${message}"/>
        <div class="control-group">
            <label class="control-label"><%= PropertiesUtil.showGJH("头像:")%></label>
            <div class="controls" style="position:relative;overflow:hidden;">
                 <form:hidden id="photo" path="photo" htmlEscape="false" maxlength="200" class="input-xlarge required"/>
                 <input type="file" id="imgsrc-for-smallImg">
                 <span class="help-inline"></span>
                 <c:if test="${user.photo!=null}">
                     <img id="smallImg-imgsrc-show" style="height:200px;width:200px;cursor:pointer;" src="${user.photo}" />
                 </c:if>
                 <c:if test="${user.photo==null}">
                     <img id="smallImg-imgsrc-show" style="height:80px;width:100px;cursor:pointer;" src="${ctxStatic}/uploadify/nopic.jpg" />
                 </c:if>
                 <span style="display:block;position:absolute;top:8px;left:35%;color:red;"><%= PropertiesUtil.showGJH("* 提示：图片大小不能大于200K。")%></span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label"><%= PropertiesUtil.showGJH("姓名:")%></label>
            <div class="controls">
                <form:input path="name" htmlEscape="false" maxlength="50" class="required" readonly="true"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label"><%= PropertiesUtil.showGJH("邮箱:")%></label>
            <div class="controls">
                <form:input path="email" htmlEscape="false" maxlength="50" class="email" readonly="true"/>
            </div>
        </div>
        <div class="form-actions">
            <input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>
        </div>
    </form:form>
</body>
</html>