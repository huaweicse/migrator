package com.baison.e3.middleware.retail.app.job.retail;

import bs2.middleware.service.IBaseJobService;
import com.alibaba.schedulerx.worker.domain.JobContext;
import com.alibaba.schedulerx.worker.processor.JavaProcessor;
import com.alibaba.schedulerx.worker.processor.ProcessResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * E3+发送SAP-RETAIL接口802job
 *
 * @author feng.leng
 */
@Component
public class RetailAntaIL802Job extends JavaProcessor {

    @Autowired
    @Qualifier("retailAntaIL802JobService")
    private IBaseJobService baseJobService;

    @Override
    public ProcessResult process(JobContext context) {
        boolean result = baseJobService.process(null);
        return new ProcessResult(result);
    }
}
