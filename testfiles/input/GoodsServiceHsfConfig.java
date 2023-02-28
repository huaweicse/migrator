package com.baison.e3.middleware.config;

import com.alibaba.boot.hsf.annotation.HSFConsumer;
import com.baison.e3.middleware.business.anta.api.service.IAntaGoodsService;
import com.baison.e3.middleware.business.anta.api.service.IMwPlatformGoodsService;
import com.baison.e3.middleware.goods.service.IBarcodeService;
import com.baison.e3.middleware.goods.service.ICategoryTreeService;
import com.baison.e3.middleware.goods.service.IClassificationService;
import com.baison.e3.middleware.goods.service.IPushGoodsService;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @author cong
 *
 */
@Configuration
public class GoodsServiceHsfConfig {
	@HSFConsumer
	private ICategoryTreeService categoryTreeService;

	@HSFConsumer
	private IClassificationService classificationService;

	@HSFConsumer
	private IAntaGoodsService goodsService;

	@HSFConsumer
	private IBarcodeService barcodeService;

	@HSFConsumer
	private IPushGoodsService pushGoodsService;

	@HSFConsumer
	private IMwPlatformGoodsService mwPlatformGoodsService;

}
