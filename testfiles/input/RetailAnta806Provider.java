package com.baison.e3.middleware.stock.service.retail;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.boot.hsf.annotation.HSFProvider;
import com.baison.e3.middleware.stock.api.service.retail.IRetailAnta806Service;

@HSFProvider(serviceInterface = IRetailAnta806Service.class)
public class RetailAnta806Provider implements IRetailAnta806Service {

	@Autowired
	private IRetailAnta806Service retailAnta806Service;

	@Override
	public Object pushDataRetail(String token,Boolean rePushError) {
		return retailAnta806Service.pushDataRetail(token,rePushError);
	}

	@Override
	public void updateisconfig(String token, Object send) {
		retailAnta806Service.updateisconfig(token, send);
	}


}
