package com.movitech.mbox.cjobs.tasks.demo;

import com.movitech.mbox.modules.sys.service.DictService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Mike
 */
@Service
public class DemoJobService
{
    private static final Log LOG = LogFactory.getLog(DemoJobService.class);

    @Autowired
    private DictService dictService;

    public void printUserInfo()
    {
        LOG.info("***      start " + "    *************");

        List<String> list = dictService.findTypeList();
        for(String s : list) {
            LOG.info("type::::: " + s);
        }

        LOG.info("*********current user information end******************");
    }
}
