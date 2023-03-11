package com.baison.e3.middleware.impl;

import com.alibaba.boot.hsf.annotation.HSFProvider;
import com.baison.e3.middleware.userauth.model.DataRange;
import com.baison.e3.middleware.userauth.service.IDataRangeService;
import com.baison.e3.middleware.userauth.service.impl.AuthBaseService;
import e3.middleware.query.E3Selector;
import e3.middleware.result.model.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;

@HSFProvider(serviceInterface = IDataRangeService.class, serviceVersion = "1.0.0")
public class DataRangeProvider extends AuthBaseService<DataRange> implements IDataRangeService {

	@Autowired
	private IDataRangeService dataRangeServiceImpl;

	@Override
	public ServiceResult createObject(String token, DataRange[] object) {
		return dataRangeServiceImpl.createObject(token,object);
	}

	@Override
	public ServiceResult modifyObject(String token, DataRange[] object) {
		return dataRangeServiceImpl.modifyObject(token,object);
	}

	@Override
	public DataRange[] findObjectById(String token, Long[] ids) {
		return dataRangeServiceImpl.findObjectById(token,ids);
	}

	@Override
	public DataRange[] findObjectById(String token, String[] ids) {
		return dataRangeServiceImpl.findObjectById(token, ids);
	}

	@Override
	public DataRange[] queryObject(String token, E3Selector selector) {
		return dataRangeServiceImpl.queryObject(token,selector);
	}

	@Override
	public DataRange[] queryPageObject(String token, E3Selector selector, int pageSize, int pageIndex) {
		return dataRangeServiceImpl.queryPageObject(token, selector, pageSize, pageIndex);
	}

	@Override
	protected Class<DataRange> getClazz() {
		return DataRange.class;
	}

	@Override
	public ServiceResult enable(String token, String id) {
		return dataRangeServiceImpl.enable(token,id);
	}

	@Override
	public ServiceResult disable(String token, String id) {

		return dataRangeServiceImpl.disable(token,id);
	}

	@Override
	protected boolean isCtrlByAuth() {
		return false;
	}

}
