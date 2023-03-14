package com.baison.e3.middleware.config;

import org.springframework.cloud.openfeign.FeignClient;

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
	@FeignClient(name = "${feign.client.GoodsServiceHsfConfig}", contextId = "iCategoryTreeService", path = "/iCategoryTreeService")
  public interface ICategoryTreeServiceExt extends ICategoryTreeService{}

	@FeignClient(name = "${feign.client.GoodsServiceHsfConfig}", contextId = "iClassificationService", path = "/iClassificationService")
  public interface IClassificationServiceExt extends IClassificationService{}

	@FeignClient(name = "${feign.client.GoodsServiceHsfConfig}", contextId = "iAntaGoodsService", path = "/iAntaGoodsService")
  public interface IAntaGoodsServiceExt extends IAntaGoodsService{}

	@FeignClient(name = "${feign.client.GoodsServiceHsfConfig}", contextId = "iBarcodeService", path = "/iBarcodeService")
  public interface IBarcodeServiceExt extends IBarcodeService{}

	@FeignClient(name = "${feign.client.GoodsServiceHsfConfig}", contextId = "iPushGoodsService", path = "/iPushGoodsService")
  public interface IPushGoodsServiceExt extends IPushGoodsService{}

	@FeignClient(name = "${feign.client.GoodsServiceHsfConfig}", contextId = "iMwPlatformGoodsService", path = "/iMwPlatformGoodsService")
  public interface IMwPlatformGoodsServiceExt extends IMwPlatformGoodsService{}

	@FeignClient(name = "${feign.client.GoodsServiceHsfConfig}", contextId = "iODSShopRefusalRecordService", path = "/iODSShopRefusalRecordService")
  public interface IODSShopRefusalRecordServiceExt extends IODSShopRefusalRecordService{}
			"90000"))
	private IRetailOrderBillService retailOrderBillService;
	@FeignClient(name = "${feign.client.GoodsServiceHsfConfig}", contextId = "iAceSapService", path = "/iAceSapService")
  public interface IAceSapServiceExt extends IAceSapService{}
}
