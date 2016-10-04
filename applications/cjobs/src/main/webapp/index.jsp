<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="org.springframework.context.support.ClassPathXmlApplicationContext" %>
<%@ page import="org.springframework.scheduling.quartz.SchedulerFactoryBean" %>
<%@ page import="org.quartz.JobExecutionContext" %>
<%@ page import="java.util.List" %>
<%@ page import="org.quartz.impl.StdScheduler" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%--
  Created by IntelliJ IDEA.
  User: FengHaixin
  Date: 2016-05-17
  Time: 9:25
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Running Job Status</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <style>
        table.border{border-collapse: collapse;border: 1px solid #9CF;text-align:center;font-family:Arial,'Times New Roman','Microsoft YaHei',SimHei;}
        table.border thead td,table.set_border th{font-weight:bold;background-color:White;}
        table.border tr:nth-child(even){background-color:#EAF2D3;}
        table.border td,table.border th{border:1px solid #C3C3C3;text-align:center;}
    </style>
</head>
<%
    SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");

    WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(application);

    StdScheduler scheduler = (StdScheduler)context.getBean("quartzScheduler");
    List<JobExecutionContext> cJobs =scheduler.getCurrentlyExecutingJobs();
%>
<body>

<h1>Running Job Status</h1>
<table class="border">
    <tr>
        <th>Job</th><th>Scheduler</th><th>Is Recovering</th><th>Trigger</th><th>Refire Count</th>
    <th>Fire Time</th><th> Scheduled FireTime</th><th>Previous FireTime</th><th> Next FireTime</th>
    </tr>
<%
    if (cJobs != null && cJobs.size() > 0)
    {
        out.print("<tr>");
        for (JobExecutionContext job :cJobs)
        {
            out.print("<td>" + job.getJobDetail().getKey()+ "</td>");
            out.print("<td>" + job.getScheduler().getSchedulerName()+ "</td>");
            out.print("<td>" + job.isRecovering()+ "</td>");
            out.print("<td>" + job.getTrigger().getKey()+ "</td>");
            out.print("<td>" + job.getRefireCount()+ "</td>");
            out.print("<td>" + formatter.format(job.getFireTime())+ "</td>");
            out.print("<td>" + formatter.format(job.getScheduledFireTime())+ "</td>");
            out.print("<td>" + formatter.format(job.getPreviousFireTime())+ "</td>");
            out.print("<td>" + formatter.format(job.getNextFireTime())+ "</td>");
        }
        out.print("</tr>");
    } else {
        out.print("<tr><td colspan=9>No tasks running</td></tr>");
    }
%>
</table>
</body>
</html>


