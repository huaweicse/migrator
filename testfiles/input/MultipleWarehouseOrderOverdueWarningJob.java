package com.baison.e3.middleware.distribute.app.job.bill;

import bs2.middleware.service.IBaseJobService;
import com.alibaba.schedulerx.worker.domain.JobContext;
import com.alibaba.schedulerx.worker.processor.JavaProcessor;
import com.alibaba.schedulerx.worker.processor.ProcessResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 仓库工单超期预警定时任务
 *
 * @Author ShawLen
 * @DateTime 2023/2/9 2:38 下午
 */
@Component
public class MultipleWarehouseOrderOverdueWarningJob extends JavaProcessor {

    @Autowired
    @Qualifier("multipleWarehouseOrderOverdueWarningService")
    private IBaseJobService baseJobService;

    @Override
    public ProcessResult process(JobContext context) {
        Map<String,String> map = new HashMap<>();
        String jobParameters = context.getJobParameters();
        map.put("isOverdue",jobParameters);
        boolean result = baseJobService.process(map);
        return new ProcessResult(result);
    }

}
