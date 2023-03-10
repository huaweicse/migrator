package com.baison.e3.middleware.business.anta.impl;

import com.alibaba.boot.hsf.annotation.HSFProvider;
import com.baison.e3.middleware.business.anta.api.service.IAntaNewGoodsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@HSFProvider(serviceInterface = IAntaNewGoodsService.class)
public class AntaNewGoodsProvider implements IAntaNewGoodsService {

	@Autowired
	private IAntaNewGoodsService antaNewGoodsService;

	@Override
	public List<Map<String, Object>> queryGoodsExtByFiledCodes(String token, Long goodsId, Long areaId,
	                                                           List<String> filedCodeList) {
		return antaNewGoodsService.queryGoodsExtByFiledCodes(token, goodsId, areaId, filedCodeList);
	}

}
