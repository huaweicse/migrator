package com.baison.e3.middleware.base;

import bs2.middleware.orm.core.AbstractBean;
import com.baison.e3.middleware.excel.service.ISupportImportAndExport;
import e3.middleware.query.E3Selector;
import e3.middleware.result.model.ServiceResult;
import io.swagger.annotations.ApiOperation;

/**
 * @param <T>
 * @author shiyue.feng
 */
public interface IBaseService<T extends AbstractBean> extends ISupportImportAndExport {

    /**
     * 创建
     *
     * @param token
     * @param object
     * @return
     */
    public ServiceResult createObject(String token, T[] object);

    /**
     * 修改
     *
     * @param token
     * @param object
     * @return
     */
    public ServiceResult modifyObject(String token, T[] object);

    /**
     * 删除
     *
     * @param token
     * @param ids
     */
    @ApiOperation(value = "removeObjectByIntegerIds", nickname = "removeObjectByIntegerIds")
    public ServiceResult removeObject(String token, Long[] ids);

    @ApiOperation(value = "removeObjectByStringIds", nickname = "removeObjectByStringIds")
    public ServiceResult removeObject(String token, String[] ids);

    /**
     * 主键查询
     *
     * @param token
     * @param ids
     * @return
     */
    @ApiOperation(value = "findObjectByIntegerId", nickname = "findObjectByIntegerId")
    public T[] findObjectById(String token, Long[] ids);

    @ApiOperation(value = "findObjectByStringId", nickname = "findObjectByStringId")
    public T[] findObjectById(String token, String[] ids);
    /**
     * 查询
     *
     * @param token
     * @param selector
     * @return
     */
    public T[] queryObject(String token, E3Selector selector);

    /**
     * 查询
     *
     * @param token
     * @param selector
     * @return
     */
    public T[] queryObjectNoAuth(String token, E3Selector selector);

    /**
     * 查询数量
     *
     * @param token
     * @param selector
     * @return
     */
    public long getListCount(String token, E3Selector selector);

    /**
     * 查询数量
     *
     * @param token
     * @param selector
     * @return
     */
    public long getListCountByADS(String token, E3Selector selector);

    /**
     * 分页查询
     *
     * @param token
     * @param selector
     * @param pageSize
     * @param pageIndex
     * @return
     */
    @ApiOperation(value = "queryPageObject", nickname = "queryPageObject")
    public T[] queryPageObject(String token, E3Selector selector, int pageSize,
                               int pageIndex);

    /**
     * ADS分页查询
     *
     * @param token
     * @param selector
     * @param pageSize
     * @param pageIndex
     * @return
     */
    @ApiOperation(value = "queryPageObjectByADS", nickname = "queryPageObjectByADS")
    public T[] queryPageObjectByADS(String token, E3Selector selector, int pageSize,
                                    int pageIndex);
}
