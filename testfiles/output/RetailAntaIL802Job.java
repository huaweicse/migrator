package com.baison.e3.middleware.retail.app.job.retail;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;

import bs2.middleware.service.IBaseJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * E3+发送SAP-RETAIL接口802job
 *
 * @author feng.leng
 */
@Component
public class RetailAntaIL802Job  {

    @Autowired
    @Qualifier("retailAntaIL802JobService")
    private IBaseJobService baseJobService;

    @XxlJob("RetailAntaIL802Job")
    public ReturnT<String> doJob(String param) {
        boolean result = baseJobService.process(null);
        return result ? ReturnT.SUCCESS : ReturnT.FAIL;
    }
}
