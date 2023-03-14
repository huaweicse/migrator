package com.baison.e3.middleware.distribute.app.job.bill;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;

import bs2.middleware.service.IBaseJobService;
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
public class MultipleWarehouseOrderOverdueWarningJob  {

    @Autowired
    @Qualifier("multipleWarehouseOrderOverdueWarningService")
    private IBaseJobService baseJobService;

    @XxlJob("MultipleWarehouseOrderOverdueWarningJob")
    public ReturnT<String> doJob(String jobParameters) {
        Map<String,String> map = new HashMap<>();
        map.put("isOverdue",jobParameters);
        boolean result = baseJobService.process(map);
        return result ? ReturnT.SUCCESS : ReturnT.FAIL;
    }

}
