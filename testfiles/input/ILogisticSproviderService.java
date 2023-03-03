package com.baison.e3.middleware.business.anta.api;

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
	public ServiceResult createLogpro(String token, List<LogisticSprovider> LogisticSproviders);

	/**
	 * 删除物流商
	 * 
	 * @param ctx
	 * @param LogisticSproviderids
	 * @return
	 */
	public ServiceResult removeLogpro(String token, Long[] LogisticSproviderids);

	/**
	 * 查找物流商
	 * 
	 * @param ctx
	 * @param LogisticSproviderid
	 * @return
	 */
	public LogisticSprovider findLogproById(String token, Long LogisticSproviderid);

	/**
	 * 更新物流商
	 * 
	 * @param ctx
	 * @param LogisticSproviders
	 * @return
	 */
	public ServiceResult modifyLogpro(String token, List<LogisticSprovider> LogisticSproviders);

	/**
	 * 启用物流商
	 * 
	 * @param ctx
	 * @param LogisticSproviders
	 * @return
	 */
	public ServiceResult enableLogpro(String token, Long[] LogisticSproviders);

	/**
	 * 停用物流商
	 * 
	 * @param ctx
	 * @param brandids
	 * @return
	 */
	public ServiceResult disableLogpro(String token, Long[] LogisticSproviders);

	/**
	 * 查询物流商数据总记录数
	 * 
	 * @param ctx
	 * @param selector
	 * @return
	 */
	public long getListCount(String token, E3Selector selector);

	/**
	 * 物流商查询接口
	 * 
	 * @param ctx
	 * @param selector
	 * @return
	 */
	public LogisticSprovider[] queryLogpro(String token, E3Selector selector);

	/**
	 * 物流商分页查询接口
	 * 
	 * @param ctx
	 * @param selector
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	public LogisticSprovider[] queryPageLogpro(String token, E3Selector selector,
			int pageSize, int pageIndex);

	/**
	 * 将某条数据设置为默认值
	 * 
	 * @param token
	 * @param LogisticSproviderid
	 * @return
	 */
	public ServiceResult setDefault(String token, Long LogisticSproviderid);
	
	/**
	 * 获取物流商的字段参数
	 * @param logprotype
	 * @return
	 */
	public Object getlogproconfig(String token, String logprotype);
}
