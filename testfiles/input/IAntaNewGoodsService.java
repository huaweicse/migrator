package com.baison.e3.middleware.business.anta.api.service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 新商品服务
 *
 * @author xiaowei
 * @date 2021/12/10 17:58
 */
public interface IAntaNewGoodsService {


	/**
	 * 根据商品id,动态属性字段 查询商品信息(货号级别数据)
	 * @param token
	 * @param goodsId
	 * @param areaId
	 * @param filedCodeList
	 * @return
	 */
	List<Map<String, Object>> queryGoodsExtByFiledCodes(String token, @NotNull Long goodsId, Long areaId,
	                                                    @NotNull List<String> filedCodeList);

}