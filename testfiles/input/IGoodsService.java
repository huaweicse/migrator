package com.baison.e3.middleware.goods.service;

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
    ServiceResult createGoods(String token, List<E3Goods> goods);

    /**
     * 更新商品信息
     *
     * @param goods
     * @return
     * @pram token
     */
    ServiceResult modifyGoods(String token, List<E3Goods> goods);

    /**
     * 根据商品id来查询商品
     *
     * @param goodsIds
     * @return
     * @pram token
     */
    AntaGoods findByAreaId(String token, Long goodsId, Long areaId);

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
    AntaGoods findByAreaIdV2(Long goodsId, Long areaId);

    @ApiOperation(value = "findByIdWithSub", nickname = "findByIdWithSub")
    AntaGoods findById(String token, E3Selector selector, Long goodsId);

    /**
     * 根据商品id来查询商品
     *
     * @param goodsIds
     * @return
     * @pram token
     */
    @ApiOperation(value = "findByIds", nickname = "findByIds")
    AntaGoods[] findByIds(String token, Long[] goodsIds);

    @ApiOperation(value = "findByIdsWithSub", nickname = "findByIdsWithSub")
    AntaGoods[] findByIds(String token,
        GoodsFindByIdsRequest goodsFindByIdsRequest);

    AntaGoods[] findByIdsAndAreaId(String token,
        GoodsFindByIdsAndAreaIdRequest goodsFindByIdsAndAreaIdRequest);

    /**
     * 根据商品属性条件查询商品
     *
     * @param selector
     * @return
     * @pram token
     */
    AntaGoods[] findGoods(String token, E3Selector selector);

    /**
     * 获取商品查询字段信息，包含所有可查询字段集、列表显示字段集
     *
     * @param token
     * @param categoryTreeId
     * @return
     */
    E3ListFieldsInfo getQueryFields(String token, Long categoryTreeId);

    /**
     * 查询数据量
     *
     * @param selector
     * @return
     * @pram token
     */
    long getListCount(String token, E3Selector selector);

    /**
     * 分页查询商品
     *
     * @param selector
     * @param pageSize
     * @param pageIndex
     * @return
     * @pram token
     */
    AntaGoods[] queryPageGoods(String token, E3Selector selector, int pageSize, int pageIndex);

    /**
     * 分页查询商品(区分区域)
     *
     * @param selector
     * @param pageSize
     * @param pageIndex
     * @return
     * @pram token
     */
    AntaGoods[] queryPageGoodsByAreaId(String token, E3Selector selector, Long areaId, int pageSize, int pageIndex);

    /**
     * 根据商品id来删除商品
     *
     * @param ids
     * @return
     * @pram token
     */
    ServiceResult removeGoods(String token, Long[] ids);

    /**
     * 挂载商品
     *
     * @param categoryTreeId
     * @param categoryTreeNodeId 类目树节点id
     * @param goodsIds
     * @return
     * @pram token
     */
    ServiceResult mountGoods(String token, String categoryTreeId, String categoryTreeNodeId, List<Long> goodsIds);

    /**
     * E3UUIDUtil工具类生成商品的id
     *
     * @return
     */
    Long getGoodsId();

    /**
     * 启用
     *
     * @param token
     * @param ids
     * @return
     */
    public ServiceResult enableGoods(String token, Long[] ids);

    /**
     * 停用
     *
     * @param token
     * @param ids
     * @return
     */
    public ServiceResult disableGoods(String token, Long[] ids);

    /**
     * 批量更新属性值
     *
     * @param token
     * @param ids
     * @return
     */
    public ServiceResult modifyGoodsAttrValues(String token, List<E3GoodsExt> goodsExts);

    /**
     * 保存商品列表的字段信息
     *
     * @param token
     * @param listFields
     * @return
     */
    ServiceResult saveGoodsListTemplate(String token, Long categoryTreeId, List<ListField> listFields);

    /**
     * 产讯商品列表的字段信息
     *
     * @param token
     * @return
     */
    ServiceResult queryGoodsListTemplate(String token, Long categoryTreeId);

    /**
     * 获取当前档案或单据的所有字段
     *
     * @param args
     * @return
     */
    List<BusinessField> getBusinessFields(Map<String, Object> args);

    /**
     * 获取当前档案或单据的orm bean 的名称
     *
     * @param args
     * @return
     */
    String getOrmName(String token, Long categoryTreeId, String categoryTreeNodeId);

    /**
     * 更新商品的某个属性的值（动态标签）
     *
     * @param token
     * @param updateAttrValueRequest
     * @return
     */
    ServiceResult updateAttrValue(String token,
        UpdateAttrValueRequest updateAttrValueRequest);

    /**
     * 保存，数据库存在就更新，不存在就新增
     *
     * @param token
     * @param goods
     * @return
     */
    ServiceResult save(String token, List<E3Goods> goods);

    /**
     * 从selector中获取商品的ormname
     *
     * @param token
     * @param selector
     * @return
     */
    String getOrmNameFromSelector(String token, E3Selector selector);

    /**
     * 如果条件中传了类目树节点，则需要找到其所有的叶子节点，因为商品档案存储的是叶子节点的id
     *
     * @param selector
     */
    void handleSelector(String token, E3Selector selector);

    Class<? extends SimpleGoods> getSimpleGoodsClazz();

    AntaSimpleGoods[] queryAntaSimpleGoods(String token, E3Selector selector);

    /**
     * 通过商品编码查询id、价格、品牌
     */
    SimpleGoods selectIdPriceBrandByCode(String token, String GoodsCode);

    /**
     * 获取采购组
     *
     * @param brandCode 品牌code
     * @param classId   大类id
     * @return
     */
    String findEkgrp(String brandCode, String classId);

    /***
     * 获取商品大类
     * @param token
     * @param goods
     * @return
     */
    @ApiOperation(value = "getClassificationCodeByGoods", nickname = "getClassificationCodeByGoods")
    Classification getClassificationCodeByGoods(String token, AntaGoods goods);

    /***
     * 获取商品对应的大类
     * @param token
     * @param getClassificationCodeByGoodsRequest
     * @return
     */
    @ApiOperation(value = "getClassificationCodeByGoodsWithSub", nickname = "getClassificationCodeByGoodsWithSub")
    Classification getClassificationCodeByGoods(String token,
        GetClassificationCodeByGoodsRequest getClassificationCodeByGoodsRequest);

    List<Map<String, Object>> getDimSkuByGoodsIds(Long[] goodsIds);

    List<GoodsInfo> getDimSkuByIds(List<Long> ids);

    /**
     * 根据货号查询商品详情
     */
    List<GoodsInfo> getDimSkuByGoodsSn(String goodsSn, Integer areaId);

    /**
     * 根据条码和区域获取商品详情
     */
    @ApiOperation(value = "getDimSkuByBarcodeByCode", nickname = "getDimSkuByBarcodeByCode")
    List<GoodsInfo> getDimSkuByBarcode(String barcode, Integer areaId);

    @ApiOperation(value = "getDimSkuByBarcodeByCodes", nickname = "getDimSkuByBarcodeByCodes")
    List<Map<String, Object>> getDimSkuByBarcode(String[] barcodes, Integer areaId);

    List<SimpleGoods> queryJdbcTemplateSimpleGoods(String sql);

    /**
     * 根据ID 查询商品缓存信息
     *
     * @param id
     * @return
     */
    GdsGoodsCacheDTO selectCacheDTOById(Integer id);

    /**
     * 根据code 查询商品缓存信息
     *
     * @param code
     * @return
     */
    GdsGoodsCacheDTO selectCacheDTOByCode(String code);

    /**
     * 根据ID列表查询商品缓存信息
     *
     * @param idList
     * @return
     */
    List<GdsGoodsCacheDTO> selectCacheDTOsByIdList(List<Integer> idList);
}
