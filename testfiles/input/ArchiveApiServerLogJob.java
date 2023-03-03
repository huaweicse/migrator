package com.baison.e3.middleware.job;

import com.alibaba.schedulerx.worker.domain.JobContext;
import com.alibaba.schedulerx.worker.processor.JavaProcessor;
import com.alibaba.schedulerx.worker.processor.ProcessResult;
import com.baison.e3.middleware.apilog.service.IApiServerLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 存档api服务日志
 *
 * @author liuSW
 * @date 2022/2/14  17:39
 */
@Component
public class ArchiveApiServerLogJob extends JavaProcessor {

    @Autowired
    private IApiServerLogService apiServerLogService;

    @Override
    public ProcessResult process(JobContext context) throws Exception {
        apiServerLogService.renameAndCreateNewTable();
        return new ProcessResult(true);
    }
}
