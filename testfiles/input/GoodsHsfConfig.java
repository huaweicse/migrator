package com.baison.e3.middleware.report.config;

import com.alibaba.boot.hsf.annotation.HSFConsumer;
import com.baison.e3.middleware.goods.service.ISingleProductService;
import org.springframework.context.annotation.Configuration;

/**
* 商品服务中心hsf配置
* @author xiaowei
* @date 2021/2/6 9:33
**/
@Configuration
public class GoodsHsfConfig {
	@HSFConsumer
	private ISingleProductService iSingleProductService;
}
