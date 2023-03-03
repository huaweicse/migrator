package com.baison.e3.middleware.business.anta.service;

import com.alibaba.boot.hsf.annotation.HSFProvider;
import com.baison.e3.middleware.base.BaseService;
import com.baison.e3.middleware.business.anta.api.ILogisticSproviderService;
import com.baison.e3.middleware.business.anta.api.model.LogisticSprovider;
import e3.middleware.query.E3Selector;
import e3.middleware.result.model.ServiceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 物流商服务
 * 
 * @author feng.leng
 *
 */
@HSFProvider(serviceInterface = ILogisticSproviderService.class, serviceVersion = "1.0.0")
public class LogisticSproviderProvider extends BaseService<LogisticSprovider> implements ILogisticSproviderService {

	@Autowired
	private ILogisticSproviderService logisticSproviderService;
	private static Logger logger = LoggerFactory.getLogger(LogisticSproviderProvider.class);

	@Override
	public ServiceResult createLogpro(String token, List<LogisticSprovider> LogisticSproviders) {
		return logisticSproviderService.createLogpro(token,LogisticSproviders);
	}

	@Override
	public ServiceResult removeLogpro(String token, Long[] LogisticSproviderids) {
		return logisticSproviderService.removeLogpro(token,LogisticSproviderids);
	}

	@Override
	public LogisticSprovider findLogproById(String token, Long LogisticSproviderid) {
		return logisticSproviderService.findLogproById(token,LogisticSproviderid);
	}

	@Override
	public ServiceResult modifyLogpro(String token, List<LogisticSprovider> LogisticSproviders) {
		return logisticSproviderService.modifyLogpro(token,LogisticSproviders);
	}

	@Override
	public ServiceResult enableLogpro(String token, Long[] LogisticSproviderids) {
		return logisticSproviderService.enableLogpro(token,LogisticSproviderids);
	}

	@Override
	public ServiceResult disableLogpro(String token, Long[] LogisticSproviderids) {
		return logisticSproviderService.disableLogpro(token,LogisticSproviderids);
	}

	@Override
	public LogisticSprovider[] queryLogpro(String token, E3Selector selector) {
		return  logisticSproviderService.queryPageLogpro(token, selector, -1, -1);
	}

	@Override
	public LogisticSprovider[] queryPageLogpro(String token, E3Selector selector, int pageSize, int pageIndex) {

		return logisticSproviderService.queryPageLogpro(token, selector, pageSize,
				pageIndex);
	}

	@Override
	public ServiceResult setDefault(String token, Long LogisticSproviderid) {
		return logisticSproviderService.setDefault(token, LogisticSproviderid);
	}
	
	@Override
	public Object getlogproconfig(String token, String logprotype) {
		return logisticSproviderService.getlogproconfig(token, logprotype);
	}

	@Override
	protected Class<LogisticSprovider> getClazz() {
		return LogisticSprovider.class;
	}

}
