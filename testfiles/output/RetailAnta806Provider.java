package com.baison.e3.middleware.stock.service.retail;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baison.e3.middleware.stock.api.service.retail.IRetailAnta806Service;

@RestController
@org.springframework.context.annotation.Lazy
@RequestMapping("/iRetailAnta806Service")
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
