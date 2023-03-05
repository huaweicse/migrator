package com.baison.e3.middleware.base;

import bs2.middleware.orm.core.AbstractBean;
import com.baison.e3.middleware.copy.ISupportCopyService;
import e3.middleware.query.E3Selector;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * 
 * 基础服务接口
 * 
 * @author xiaohui.su
 *
 * @param <T>
 */
public interface IE3BaseService<T extends AbstractBean> extends IBaseService<T>, ISupportCopyService{
	/**
	 * 根据主表ID查询明细信息(注意 selector中不支持对象查询且selectFields用的是表字段)
	 * @param token
	 * @param billId
	 * @param selector
	 * @return
	 */
	public List<T> queryGoodsDetailByBillId(String token, String billId, E3Selector selector);

	/**
	 * ADS分页查询
	 * @param token
	 * @param selector
	 * @param pageSize
	 * @param pageIndex
	 * @param selectSub
	 * @return
	 */
	@ApiOperation(value = "queryPageObjectByADSWithSub", nickname = "queryPageObjectByADSWithSub")
	public T[] queryPageObjectByADS(String token, E3Selector selector, int pageSize, 
			int pageIndex, boolean selectSub);
	
	/**
	 * 分页查询
	 * 
	 * @param token
	 * @param selector
	 * @param pageSize
	 * @param pageIndex
	 * @param selectSub
	 * @return
	 */
	@ApiOperation(value = "queryPageObjectWithSub", nickname = "queryPageObjectWithSub")
	T[] queryPageObject(String token, E3Selector selector, int pageSize, int pageIndex,
			boolean selectSub);

	/**
	 * 分页查询子表
	 * 
	 * @param token
	 * @param querySelector
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	T[] queryPageDetails(String token, E3Selector querySelector, int pageSize, int pageIndex);

	/**
	 * 获取指令执行器
	 * 
	 * @param token
	 * @param instruction
	 * @param dbChooser
	 * @return
	 */
	IE3InstructionInvoker getE3InstructionInvoker(String token, IE3Instruction instruction, String dbChooser);

}
