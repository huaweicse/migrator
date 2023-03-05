package com.baison.e3.middleware.base;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;

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
    @ResponseBody
    @PostMapping(value = "/createObject", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    public ServiceResult createObject(@RequestHeader(value="token") String token, @RequestBody T[] object);

    /**
     * 修改
     *
     * @param token
     * @param object
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/modifyObject", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    public ServiceResult modifyObject(@RequestHeader(value="token") String token, @RequestBody T[] object);

    /**
     * 删除
     *
     * @param token
     * @param ids
     */
    @ApiOperation(value = "removeObjectByIntegerIds", nickname = "removeObjectByIntegerIds")
    @ResponseBody
    @PostMapping(value = "/removeObjectByIntegerIds", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    public ServiceResult removeObject(@RequestHeader(value="token") String token, @RequestBody Long[] ids);

    @ApiOperation(value = "removeObjectByStringIds", nickname = "removeObjectByStringIds")
    @ResponseBody
    @PostMapping(value = "/removeObjectByStringIds", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    public ServiceResult removeObject(@RequestHeader(value="token") String token, @RequestBody String[] ids);

    /**
     * 主键查询
     *
     * @param token
     * @param ids
     * @return
     */
    @ApiOperation(value = "findObjectByIntegerId", nickname = "findObjectByIntegerId")
    @ResponseBody
    @PostMapping(value = "/findObjectByIntegerId", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    public T[] findObjectById(@RequestHeader(value="token") String token, @RequestBody Long[] ids);

    @ApiOperation(value = "findObjectByStringId", nickname = "findObjectByStringId")
    @ResponseBody
    @PostMapping(value = "/findObjectByStringId", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    public T[] findObjectById(@RequestHeader(value="token") String token, @RequestBody String[] ids);
    /**
     * 查询
     *
     * @param token
     * @param selector
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/queryObject", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    public T[] queryObject(@RequestHeader(value="token") String token, @RequestBody E3Selector selector);

    /**
     * 查询
     *
     * @param token
     * @param selector
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/queryObjectNoAuth", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    public T[] queryObjectNoAuth(@RequestHeader(value="token") String token, @RequestBody E3Selector selector);

    /**
     * 查询数量
     *
     * @param token
     * @param selector
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getListCount", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    public long getListCount(@RequestHeader(value="token") String token, @RequestBody E3Selector selector);

    /**
     * 查询数量
     *
     * @param token
     * @param selector
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getListCountByADS", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    public long getListCountByADS(@RequestHeader(value="token") String token, @RequestBody E3Selector selector);

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
    @ResponseBody
    @PostMapping(value = "/queryPageObject", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    public T[] queryPageObject(@RequestHeader(value="token") String token, @RequestBody E3Selector selector, @RequestParam(value="pageSize") int pageSize, @RequestParam(value="pageIndex") int pageIndex);

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
    @ResponseBody
    @PostMapping(value = "/queryPageObjectByADS", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    public T[] queryPageObjectByADS(@RequestHeader(value="token") String token, @RequestBody E3Selector selector, @RequestParam(value="pageSize") int pageSize, @RequestParam(value="pageIndex") int pageIndex);
}
