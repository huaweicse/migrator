package com.baison.e3.middleware.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;

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
public class ArchiveApiServerLogJob  {

    @Autowired
    private IApiServerLogService apiServerLogService;

    @XxlJob("ArchiveApiServerLogJob")
    public ReturnT<String> doJob(String jobParameters) throws Exception {
        apiServerLogService.renameAndCreateNewTable();
        return ReturnT.SUCCESS;
    }
}
