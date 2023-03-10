package com.baison.e3.middleware.business.anta.api.service;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;

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
    @ResponseBody
	                                                    @NotNull List<String> filedCodeList);

}
