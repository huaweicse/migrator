package com.baison.e3.middleware.business.anta.api;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;

import com.baison.e3.middleware.business.anta.api.model.LogisticSprovider;
import com.baison.e3.middleware.excel.service.ISupportImportAndExport;
import e3.middleware.query.E3Selector;
import e3.middleware.result.model.ServiceResult;

import java.util.List;

/**
 * 物流商服务
 * @author feng.leng
 *
 */
public interface ILogisticSproviderService extends ISupportImportAndExport {
	/**
	 * 创建物流商档案
	 * 
	 * @param ctx
	 * @param LogisticSproviders
	 * @return
	 */
    @ResponseBody
    @PostMapping(value = "/createLogpro", produces = "x-application/hessian2", consumes = "x-application/hessian2")
	public ServiceResult createLogpro(@RequestHeader(value="token") String token, @RequestBody List<LogisticSprovider> LogisticSproviders);

	/**
	 * 删除物流商
	 * 
	 * @param ctx
	 * @param LogisticSproviderids
	 * @return
	 */
    @ResponseBody
    @PostMapping(value = "/removeLogpro", produces = "x-application/hessian2", consumes = "x-application/hessian2")
	public ServiceResult removeLogpro(@RequestHeader(value="token") String token, @RequestBody Long[] LogisticSproviderids);

	/**
	 * 查找物流商
	 * 
	 * @param ctx
	 * @param LogisticSproviderid
	 * @return
	 */
    @ResponseBody
    @PostMapping(value = "/findLogproById", produces = "x-application/hessian2", consumes = "x-application/hessian2")
	public LogisticSprovider findLogproById(@RequestHeader(value="token") String token, @RequestParam(value="LogisticSproviderid") Long LogisticSproviderid);

	/**
	 * 更新物流商
	 * 
	 * @param ctx
	 * @param LogisticSproviders
	 * @return
	 */
    @ResponseBody
    @PostMapping(value = "/modifyLogpro", produces = "x-application/hessian2", consumes = "x-application/hessian2")
	public ServiceResult modifyLogpro(@RequestHeader(value="token") String token, @RequestBody List<LogisticSprovider> LogisticSproviders);

	/**
	 * 启用物流商
	 * 
	 * @param ctx
	 * @param LogisticSproviders
	 * @return
	 */
    @ResponseBody
    @PostMapping(value = "/enableLogpro", produces = "x-application/hessian2", consumes = "x-application/hessian2")
	public ServiceResult enableLogpro(@RequestHeader(value="token") String token, @RequestBody Long[] LogisticSproviders);

	/**
	 * 停用物流商
	 * 
	 * @param ctx
	 * @param brandids
	 * @return
	 */
    @ResponseBody
    @PostMapping(value = "/disableLogpro", produces = "x-application/hessian2", consumes = "x-application/hessian2")
	public ServiceResult disableLogpro(@RequestHeader(value="token") String token, @RequestBody Long[] LogisticSproviders);

	/**
	 * 查询物流商数据总记录数
	 * 
	 * @param ctx
	 * @param selector
	 * @return
	 */
    @ResponseBody
    @PostMapping(value = "/getListCount", produces = "x-application/hessian2", consumes = "x-application/hessian2")
	public long getListCount(@RequestHeader(value="token") String token, @RequestBody E3Selector selector);

	/**
	 * 物流商查询接口
	 * 
	 * @param ctx
	 * @param selector
	 * @return
	 */
    @ResponseBody
    @PostMapping(value = "/queryLogpro", produces = "x-application/hessian2", consumes = "x-application/hessian2")
	public LogisticSprovider[] queryLogpro(@RequestHeader(value="token") String token, @RequestBody E3Selector selector);

	/**
	 * 物流商分页查询接口
	 * 
	 * @param ctx
	 * @param selector
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
    @ResponseBody
    @PostMapping(value = "/queryPageLogpro", produces = "x-application/hessian2", consumes = "x-application/hessian2")
	public LogisticSprovider[] queryPageLogpro(@RequestHeader(value="token") String token, @RequestBody E3Selector selector, @RequestParam(value="pageSize") int pageSize, @RequestParam(value="pageIndex") int pageIndex);

	/**
	 * 将某条数据设置为默认值
	 * 
	 * @param token
	 * @param LogisticSproviderid
	 * @return
	 */
    @ResponseBody
    @PostMapping(value = "/setDefault", produces = "x-application/hessian2", consumes = "x-application/hessian2")
	public ServiceResult setDefault(@RequestHeader(value="token") String token, @RequestParam(value="LogisticSproviderid") Long LogisticSproviderid);
	
	/**
	 * 获取物流商的字段参数
	 * @param logprotype
	 * @return
	 */
    @ResponseBody
    @PostMapping(value = "/getlogproconfig", produces = "x-application/hessian2", consumes = "x-application/hessian2")
	public Object getlogproconfig(@RequestHeader(value="token") String token, @RequestHeader(value="logprotype") String logprotype);
}
