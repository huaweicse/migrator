package com.baison.e3.middleware.retail.app.job.retail;

import com.alibaba.schedulerx.common.domain.InstanceStatus;
import com.alibaba.schedulerx.worker.domain.JobContext;
import com.alibaba.schedulerx.worker.processor.JavaProcessor;
import com.alibaba.schedulerx.worker.processor.ProcessResult;
import com.baison.e3.middleware.log.LogInfo;
import com.baison.e3.middleware.log.LogUtil;
import com.baison.e3.middleware.log.model.LogLevel;
import e3.middleware.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * DTC直营推送结算数据到新零售系统
 *
 * @author xiaowei
 * @date 2021/1/20 18:48
 **/
@Component
public class DtcZyRetailSendJob extends JavaProcessor {

	@Autowired
	private RetailFxsSend802Service retailFxsSend802Service;

	private static final Logger logger = LoggerFactory.getLogger(DtcZyRetailSendJob.class);

	@Override
	public ProcessResult process(JobContext context) {
		boolean result;
		try {
			logger.info("job-run: DTC直营数据推送 进入job");
			result = retailFxsSend802Service.pushRetailDataDtcZy(Session.ADMINTOKEN);
			logger.info("job-run: DTC直营数据推送 结束job");
		} catch (Exception e) {
			LogUtil.log(LogInfo.build().level(LogLevel.ERROR).key(RetailFxsSend802Service.DTC_ZY_LOG_KEY).exception(e)
					.message("处理异常，请检查！"));
			return new ProcessResult(InstanceStatus.FAILED, e.getMessage());
		}
		return new ProcessResult(result);
	}
}
