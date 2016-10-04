package com.movitech.mbox.cjobs.tasks.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

/**
 * @author Mike
 */
@DisallowConcurrentExecution
public class PrintCurrentTimeJobs extends QuartzJobBean
{
    private static final Log LOG = LogFactory.getLog(PrintCurrentTimeJobs.class);

    @Autowired
    private DemoJobService demoJobService;


    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException
    {
        LOG.info("begin to execute task," + new Date().toString());

//        try {
//            Thread.sleep(1000 * 15);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        demoJobService.printUserInfo();
        JobDataMap dataMap = jobExecutionContext.getMergedJobDataMap();
        Integer exeCount = Integer.parseInt(dataMap.get("exeCount").toString());
        LOG.info("end to execute task, with count:" + exeCount);
    }
}
