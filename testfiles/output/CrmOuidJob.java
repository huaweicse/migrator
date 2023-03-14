package com.baison.e3.middleware.retail.app.job.crm;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;

import com.baison.bap.utils.CollectionUtil;
import com.baison.bap.utils.JSONUtil;
import com.baison.e3.middleware.retail.app.job.domain.PageTask;
import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 从CRM系统获取订单的ouid和omid
 */
@Log4j2
@Component
public class CrmOuidJob  {

    @Autowired
    private CrmOuidJobService crmOuidJobService;

    @XxlJob("CrmOuidJob")
    public ReturnT<String> doJob(String param) {
        String taskName = context.getTaskName();
        Object task = context.getTask();
        String jobParameters = context.getJobParameters();
        Map<String, String> jobParamMap = new HashMap<>();
        if (StringUtils.isNotBlank(jobParameters)) {
            jobParamMap = JSONUtil.parseObject(jobParameters, HashMap.class);
        }
        log.info(String.format("查询ouid | %s", taskName));
        if (isRootTask(context)) {
            List<Long> ids = crmOuidJobService.needQueryOuidList();
            List<PageTask> tasks = new CopyOnWriteArrayList<>();
            if (!CollectionUtil.isEmpty(ids)) {
                Integer pageSize = 1000;
                String taskPageSize = jobParamMap.get("taskPageSize");
                if (StringUtils.isNotBlank(taskPageSize) && StringUtils.isNumeric(taskPageSize)) {
                    pageSize = Integer.valueOf(taskPageSize);
                }
                Lists.partition(ids, pageSize).parallelStream().forEach(subList -> {
                    Long minId = subList.get(0);
                    Long maxId = subList.get(subList.size() - 1);
                    PageTask pageTask = new PageTask(minId, maxId);
                    tasks.add(pageTask);
                    log.info(String.format("查询ouid,task | %s | %s", pageTask.getStartId(), pageTask.getEndId()));
                });
            }
            if (CollectionUtil.isEmpty(tasks)) {
                return ReturnT.SUCCESS;
            }
            return map(tasks, "PageTask");
        } else if (taskName.equals("PageTask")) {
            PageTask pageTask = (PageTask) task;
            log.info(String.format("查询ouid,PageTask | %s | %s", pageTask.getStartId(), pageTask.getEndId()));
            long startId = pageTask.getStartId();
            long endId = pageTask.getEndId();
            Map<String, String> params = new HashMap<>();
            params.put("startId", String.valueOf(startId));
            params.put("endId", String.valueOf(endId));
            params.put("crmPageSize", jobParamMap.get("crmPageSize"));
            crmOuidJobService.process(params);
            return ReturnT.SUCCESS;
        }
        return ReturnT.FAIL;
    }
}
