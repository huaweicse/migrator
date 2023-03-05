package com.baison.e3.middleware.base;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;

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
    @ResponseBody
    @PostMapping(value = "/queryGoodsDetailByBillId", produces = "x-application/hessian2", consumes = "x-application/hessian2")
	public List<T> queryGoodsDetailByBillId(@RequestHeader(value="token") String token, @RequestHeader(value="billId") String billId, @RequestBody E3Selector selector);

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
    @ResponseBody
    @PostMapping(value = "/queryPageObjectByADSWithSub", produces = "x-application/hessian2", consumes = "x-application/hessian2")
	public T[] queryPageObjectByADS(@RequestHeader(value="token") String token, @RequestBody E3Selector selector, @RequestParam(value="pageSize") int pageSize, @RequestParam(value="pageIndex") int pageIndex, @RequestParam(value="selectSub") boolean selectSub);
	
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
    @ResponseBody
    @PostMapping(value = "/queryPageObjectWithSub", produces = "x-application/hessian2", consumes = "x-application/hessian2")
	T[] queryPageObject(@RequestHeader(value="token") String token, @RequestBody E3Selector selector, @RequestParam(value="pageSize") int pageSize, @RequestParam(value="pageIndex") int pageIndex, @RequestParam(value="selectSub") boolean selectSub);

	/**
	 * 分页查询子表
	 * 
	 * @param token
	 * @param querySelector
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
    @ResponseBody
    @PostMapping(value = "/queryPageDetails", produces = "x-application/hessian2", consumes = "x-application/hessian2")
	T[] queryPageDetails(@RequestHeader(value="token") String token, @RequestBody E3Selector querySelector, @RequestParam(value="pageSize") int pageSize, @RequestParam(value="pageIndex") int pageIndex);

	/**
	 * 获取指令执行器
	 * 
	 * @param token
	 * @param instruction
	 * @param dbChooser
	 * @return
	 */
    @ResponseBody
    @PostMapping(value = "/getE3InstructionInvoker", produces = "x-application/hessian2", consumes = "x-application/hessian2")
	IE3InstructionInvoker getE3InstructionInvoker(@RequestHeader(value="token") String token, @RequestBody IE3Instruction instruction, @RequestHeader(value="dbChooser") String dbChooser);

}
