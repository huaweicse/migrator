package com.baison.e3.middleware.goods.service;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;

import com.baison.e3.middleware.business.anta.model.AntaGoods;
import com.baison.e3.middleware.business.anta.model.AntaSimpleGoods;
import com.baison.e3.middleware.business.model.BusinessField;
import com.baison.e3.middleware.excel.service.ISupportImportAndExport;
import com.baison.e3.middleware.goods.cache.dto.GdsGoodsCacheDTO;
import com.baison.e3.middleware.goods.model.product.*;
import com.baison.e3.middleware.goods.param.GetClassificationCodeByGoodsRequest;
import com.baison.e3.middleware.goods.param.GoodsFindByIdsAndAreaIdRequest;
import com.baison.e3.middleware.goods.param.GoodsFindByIdsRequest;
import com.baison.e3.middleware.goods.param.UpdateAttrValueRequest;
import com.baison.e3.middleware.model.goods.GoodsInfo;
import e3.middleware.query.E3ListFieldsInfo;
import e3.middleware.query.E3Selector;
import e3.middleware.result.model.ServiceResult;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Map;

/**
 * 商品服务
 *
 * @author cong.zhang
 */
public interface IGoodsService extends ISupportImportAndExport {

    /**
     * 新增商品
     *
     * @param goods
     * @return
     * @pram token
     */
    @ApiOperation(value = "createGoods", nickname = "createGoods")
    @ResponseBody
    @PostMapping(value = "/createGoods", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult createGoods(@RequestHeader(value="token") String token, @RequestBody List<E3Goods> goods);

    /**
     * 更新商品信息
     *
     * @param goods
     * @return
     * @pram token
     */
    @ResponseBody
    @PostMapping(value = "/modifyGoods", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult modifyGoods(@RequestHeader(value="token") String token, @RequestBody List<E3Goods> goods);

    /**
     * 根据商品id来查询商品
     *
     * @param goodsIds
     * @return
     * @pram token
     */
    @ResponseBody
    @PostMapping(value = "/findByAreaId", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    AntaGoods findByAreaId(@RequestHeader(value="token") String token, @RequestParam(value="goodsId") Long goodsId, @RequestParam(value="areaId") Long areaId);

    /**
     * 根据商品id来查询商品(仅查询商品基本属性，拓展属性，品牌，类目树，区域属性)<br />
     * 接口用于优化，不可自定义增加查询
     *
     * @param goodsId
     * @param areaId
	 * @return com.baison.e3.middleware.business.anta.model.AntaGoods
     * @date 2022/2/21 17:52
     * @author linhuaijie
     */
    @ResponseBody
    @PostMapping(value = "/findByAreaIdV2", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    AntaGoods findByAreaIdV2(@RequestParam(value="goodsId") Long goodsId, @RequestParam(value="areaId") Long areaId);

    @ApiOperation(value = "findByIdWithSub", nickname = "findByIdWithSub")
    @ResponseBody
    @PostMapping(value = "/findByIdWithSub", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    AntaGoods findById(@RequestHeader(value="token") String token, @RequestBody E3Selector selector, @RequestParam(value="goodsId") Long goodsId);

    /**
     * 根据商品id来查询商品
     *
     * @param goodsIds
     * @return
     * @pram token
     */
    @ApiOperation(value = "findByIds", nickname = "findByIds")
    @ResponseBody
    @PostMapping(value = "/findByIds", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    AntaGoods[] findByIds(@RequestHeader(value="token") String token, @RequestBody Long[] goodsIds);

    @ApiOperation(value = "findByIdsWithSub", nickname = "findByIdsWithSub")
    @ResponseBody
    @PostMapping(value = "/findByIdsWithSub", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    AntaGoods[] findByIds(@RequestHeader(value="token") String token, @RequestBody GoodsFindByIdsRequest goodsFindByIdsRequest);

    @ResponseBody
    @PostMapping(value = "/findByIdsAndAreaId", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    AntaGoods[] findByIdsAndAreaId(@RequestHeader(value="token") String token, @RequestBody GoodsFindByIdsAndAreaIdRequest goodsFindByIdsAndAreaIdRequest);

    /**
     * 根据商品属性条件查询商品
     *
     * @param selector
     * @return
     * @pram token
     */
    @ResponseBody
    @PostMapping(value = "/findGoods", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    AntaGoods[] findGoods(@RequestHeader(value="token") String token, @RequestBody E3Selector selector);

    /**
     * 获取商品查询字段信息，包含所有可查询字段集、列表显示字段集
     *
     * @param token
     * @param categoryTreeId
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getQueryFields", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    E3ListFieldsInfo getQueryFields(@RequestHeader(value="token") String token, @RequestParam(value="categoryTreeId") Long categoryTreeId);

    /**
     * 查询数据量
     *
     * @param selector
     * @return
     * @pram token
     */
    @ResponseBody
    @PostMapping(value = "/getListCount", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    long getListCount(@RequestHeader(value="token") String token, @RequestBody E3Selector selector);

    /**
     * 分页查询商品
     *
     * @param selector
     * @param pageSize
     * @param pageIndex
     * @return
     * @pram token
     */
    @ResponseBody
    @PostMapping(value = "/queryPageGoods", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    AntaGoods[] queryPageGoods(@RequestHeader(value="token") String token, @RequestBody E3Selector selector, @RequestParam(value="pageSize") int pageSize, @RequestParam(value="pageIndex") int pageIndex);

    /**
     * 分页查询商品(区分区域)
     *
     * @param selector
     * @param pageSize
     * @param pageIndex
     * @return
     * @pram token
     */
    @ResponseBody
    @PostMapping(value = "/queryPageGoodsByAreaId", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    AntaGoods[] queryPageGoodsByAreaId(@RequestHeader(value="token") String token, @RequestBody E3Selector selector, @RequestParam(value="areaId") Long areaId, @RequestParam(value="pageSize") int pageSize, @RequestParam(value="pageIndex") int pageIndex);

    /**
     * 根据商品id来删除商品
     *
     * @param ids
     * @return
     * @pram token
     */
    @ResponseBody
    @PostMapping(value = "/removeGoods", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult removeGoods(@RequestHeader(value="token") String token, @RequestBody Long[] ids);

    /**
     * 挂载商品
     *
     * @param categoryTreeId
     * @param categoryTreeNodeId 类目树节点id
     * @param goodsIds
     * @return
     * @pram token
     */
    @ResponseBody
    @PostMapping(value = "/mountGoods", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult mountGoods(@RequestHeader(value="token") String token, @RequestHeader(value="categoryTreeId") String categoryTreeId, @RequestHeader(value="categoryTreeNodeId") String categoryTreeNodeId, @RequestBody List<Long> goodsIds);

    /**
     * E3UUIDUtil工具类生成商品的id
     *
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getGoodsId", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    Long getGoodsId();

    /**
     * 启用
     *
     * @param token
     * @param ids
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/enableGoods", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    public ServiceResult enableGoods(@RequestHeader(value="token") String token, @RequestBody Long[] ids);

    /**
     * 停用
     *
     * @param token
     * @param ids
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/disableGoods", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    public ServiceResult disableGoods(@RequestHeader(value="token") String token, @RequestBody Long[] ids);

    /**
     * 批量更新属性值
     *
     * @param token
     * @param ids
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/modifyGoodsAttrValues", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    public ServiceResult modifyGoodsAttrValues(@RequestHeader(value="token") String token, @RequestBody List<E3GoodsExt> goodsExts);

    /**
     * 保存商品列表的字段信息
     *
     * @param token
     * @param listFields
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/saveGoodsListTemplate", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult saveGoodsListTemplate(@RequestHeader(value="token") String token, @RequestParam(value="categoryTreeId") Long categoryTreeId, @RequestBody List<ListField> listFields);

    /**
     * 产讯商品列表的字段信息
     *
     * @param token
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/queryGoodsListTemplate", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult queryGoodsListTemplate(@RequestHeader(value="token") String token, @RequestParam(value="categoryTreeId") Long categoryTreeId);

    /**
     * 获取当前档案或单据的所有字段
     *
     * @param args
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getBusinessFields", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    List<BusinessField> getBusinessFields(@RequestBody Map<String, Object> args);

    /**
     * 获取当前档案或单据的orm bean 的名称
     *
     * @param args
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getOrmName", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    String getOrmName(@RequestHeader(value="token") String token, @RequestParam(value="categoryTreeId") Long categoryTreeId, @RequestHeader(value="categoryTreeNodeId") String categoryTreeNodeId);

    /**
     * 更新商品的某个属性的值（动态标签）
     *
     * @param token
     * @param updateAttrValueRequest
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/updateAttrValue", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult updateAttrValue(@RequestHeader(value="token") String token, @RequestBody UpdateAttrValueRequest updateAttrValueRequest);

    /**
     * 保存，数据库存在就更新，不存在就新增
     *
     * @param token
     * @param goods
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/save", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult save(@RequestHeader(value="token") String token, @RequestBody List<E3Goods> goods);

    /**
     * 从selector中获取商品的ormname
     *
     * @param token
     * @param selector
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getOrmNameFromSelector", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    String getOrmNameFromSelector(@RequestHeader(value="token") String token, @RequestBody E3Selector selector);

    /**
     * 如果条件中传了类目树节点，则需要找到其所有的叶子节点，因为商品档案存储的是叶子节点的id
     *
     * @param selector
     */
    @ResponseBody
    @PostMapping(value = "/handleSelector", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    void handleSelector(@RequestHeader(value="token") String token, @RequestBody E3Selector selector);

    @ResponseBody
    @PostMapping(value = "/getSimpleGoodsClazz", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    Class<? extends SimpleGoods> getSimpleGoodsClazz();

    @ResponseBody
    @PostMapping(value = "/queryAntaSimpleGoods", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    AntaSimpleGoods[] queryAntaSimpleGoods(@RequestHeader(value="token") String token, @RequestBody E3Selector selector);

    /**
     * 通过商品编码查询id、价格、品牌
     */
    @ResponseBody
    @PostMapping(value = "/selectIdPriceBrandByCode", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    SimpleGoods selectIdPriceBrandByCode(@RequestHeader(value="token") String token, @RequestHeader(value="GoodsCode") String GoodsCode);

    /**
     * 获取采购组
     *
     * @param brandCode 品牌code
     * @param classId   大类id
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/findEkgrp", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    String findEkgrp(@RequestHeader(value="brandCode") String brandCode, @RequestHeader(value="classId") String classId);

    /***
     * 获取商品大类
     * @param token
     * @param goods
     * @return
     */
    @ApiOperation(value = "getClassificationCodeByGoods", nickname = "getClassificationCodeByGoods")
    @ResponseBody
    @PostMapping(value = "/getClassificationCodeByGoods", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    Classification getClassificationCodeByGoods(@RequestHeader(value="token") String token, @RequestBody AntaGoods goods);

    /***
     * 获取商品对应的大类
     * @param token
     * @param getClassificationCodeByGoodsRequest
     * @return
     */
    @ApiOperation(value = "getClassificationCodeByGoodsWithSub", nickname = "getClassificationCodeByGoodsWithSub")
    @ResponseBody
    @PostMapping(value = "/getClassificationCodeByGoodsWithSub", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    Classification getClassificationCodeByGoods(@RequestHeader(value="token") String token, @RequestBody GetClassificationCodeByGoodsRequest getClassificationCodeByGoodsRequest);

    @ResponseBody
    @PostMapping(value = "/getDimSkuByGoodsIds", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    List<Map<String, Object>> getDimSkuByGoodsIds(@RequestBody Long[] goodsIds);

    @ResponseBody
    @PostMapping(value = "/getDimSkuByIds", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    List<GoodsInfo> getDimSkuByIds(@RequestBody List<Long> ids);

    /**
     * 根据货号查询商品详情
     */
    @ResponseBody
    @PostMapping(value = "/getDimSkuByGoodsSn", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    List<GoodsInfo> getDimSkuByGoodsSn(@RequestHeader(value="goodsSn") String goodsSn, @RequestParam(value="areaId") Integer areaId);

    /**
     * 根据条码和区域获取商品详情
     */
    @ApiOperation(value = "getDimSkuByBarcodeByCode", nickname = "getDimSkuByBarcodeByCode")
    @ResponseBody
    @PostMapping(value = "/getDimSkuByBarcodeByCode", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    List<GoodsInfo> getDimSkuByBarcode(@RequestHeader(value="barcode") String barcode, @RequestParam(value="areaId") Integer areaId);

    @ApiOperation(value = "getDimSkuByBarcodeByCodes", nickname = "getDimSkuByBarcodeByCodes")
    @ResponseBody
    @PostMapping(value = "/getDimSkuByBarcodeByCodes", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    List<Map<String, Object>> getDimSkuByBarcode(@RequestBody String[] barcodes, @RequestParam(value="areaId") Integer areaId);

    @ResponseBody
    @PostMapping(value = "/queryJdbcTemplateSimpleGoods", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    List<SimpleGoods> queryJdbcTemplateSimpleGoods(@RequestHeader(value="sql") String sql);

    /**
     * 根据ID 查询商品缓存信息
     *
     * @param id
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/selectCacheDTOById", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    GdsGoodsCacheDTO selectCacheDTOById(@RequestParam(value="id") Integer id);

    /**
     * 根据code 查询商品缓存信息
     *
     * @param code
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/selectCacheDTOByCode", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    GdsGoodsCacheDTO selectCacheDTOByCode(@RequestHeader(value="code") String code);

    /**
     * 根据ID列表查询商品缓存信息
     *
     * @param idList
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/selectCacheDTOsByIdList", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    List<GdsGoodsCacheDTO> selectCacheDTOsByIdList(@RequestBody List<Integer> idList);
}
