package com.movitech.mbox.cjobs.core;

import java.text.ParseException;

import org.springframework.scheduling.quartz.CronTriggerFactoryBean;

/**
 * @author Mike
 */
public class PersistableCronTriggerFactoryBean extends CronTriggerFactoryBean
{

    @Override
    public void afterPropertiesSet() throws ParseException
    {
        super.afterPropertiesSet();
        getJobDataMap().remove(getObject().getJobKey().getName());
    }
}


