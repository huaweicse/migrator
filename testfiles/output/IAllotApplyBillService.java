package com.baison.e3.middleware.stock.api.service.allotapply;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;

import bs2.middleware.orm.core.AbstractBean;
import com.baison.e3.middleware.base.IE3BaseService;
import com.baison.e3.middleware.stock.api.dto.AllotApplyBillParamDTO;
import com.baison.e3.middleware.stock.api.dto.allotapplybill.CreateAllocationDto;
import com.baison.e3.middleware.stock.api.dto.allotapplybill.ExecAllocationDto;
import com.baison.e3.middleware.stock.api.model.adjustment.UpdateDetailsPriceRequest;
import com.baison.e3.middleware.stock.api.model.allotapply.IAllotApplyBillExecuteRequest;
import com.baison.e3.middleware.stock.api.model.allotapply.ImportHandleRequest;
import com.baison.e3.middleware.stock.api.model.allotapply.RetailGeneratedAllotApplyBillRequest;
import com.baison.e3.middleware.stock.api.model.allotapply.RetailJdOmniChannelGeneratedAllotApplyBillRequest;
import com.baison.e3.middleware.stock.api.model.allotapply.RetailJdczGeneratedAllotApplyBillRequest;
import com.baison.e3.middleware.stock.api.model.allotapply.RetailShareGeneratedAllotApplyBillRequest;
import com.baison.e3.middleware.stock.api.model.allotapply.RetailTmallMarketGeneratedAllotApplyBillRequest;
import com.baison.e3.middleware.stock.api.model.overdue.EarlyWarningStockModel;
import com.baison.e3.middleware.stock.api.model.allotapply.AllotApplyBill;
import com.baison.e3.middleware.stock.api.model.allotapply.AllotApplyGoodsDetail;
import com.baison.e3.middleware.stock.api.model.allotapply.ApplyExportListModel;
import com.baison.e3.middleware.stock.api.model.allotapply.ApplyExportModel;
import com.baison.e3.middleware.stock.api.model.vip.jit.VipRelatePickDetail;
import e3.middleware.query.E3Selector;
import e3.middleware.restful.result.Result;
import e3.middleware.result.model.ServiceResult;
import io.swagger.annotations.ApiOperation;

import java.util.List;

public interface IAllotApplyBillService extends IE3BaseService<AllotApplyBill> {


    @ResponseBody
    @PostMapping(value = "/mbQueryBySourceBillNo", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    List<AllotApplyBill> mbQueryBySourceBillNo(@RequestHeader(value="token") String token, @RequestBody List<String> applySourceBillNos);

    /***
     * 获取关联拣货单明细
     * @param allotApplyBillId
     * @param pageSize
     * @param pageIndex
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getVipRelatePickDetailList", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    List<VipRelatePickDetail> getVipRelatePickDetailList(@RequestHeader(value="allotApplyBillId") String allotApplyBillId, @RequestParam(value="pageSize") int pageSize, @RequestParam(value="pageIndex") int pageIndex);

    /**
     * 获取关联拣货单明细数量
     *
     * @param allotApplyBillId
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getVipRelatePickDetailCount", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    Long getVipRelatePickDetailCount(@RequestHeader(value="allotApplyBillId") String allotApplyBillId);

    /**
     * 提交
     *
     * @param token
     * @param objects
     * @param isContinue 0-正常校验 1-不做负库存校验 2-不做溢出校验和负库存校验
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/submit", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    public ServiceResult submit(@RequestHeader(value="token") String token, @RequestBody List<AllotApplyBill> objects, @RequestParam(value="isContinue") Integer isContinue);

    /**
     * 通过Id审核
     *
     * @param token
     * @param objects
     * @param isContinue
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/audit", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    public ServiceResult audit(@RequestHeader(value="token") String token, @RequestBody List<AllotApplyBill> objects, @RequestParam(value="isContinue") Integer isContinue);

    /**
     * 退回审核状态到初始
     *
     * @param token
     * @param objects
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/retreatAuditToInit", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    public ServiceResult retreatAuditToInit(@RequestHeader(value="token") String token, @RequestBody List<AllotApplyBill> objects);


    /**
     * 处理库存不足
     *
     * @param token
     * @param allotApplyBillId
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/handelOutOfStock", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    public ServiceResult handelOutOfStock(@RequestHeader(value="token") String token, @RequestHeader(value="allotApplyBillId") String allotApplyBillId);

    /**
     * 退回
     *
     * @param token
     * @param objects
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/retreat", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    public ServiceResult retreat(@RequestHeader(value="token") String token, @RequestBody List<AllotApplyBill> objects);

    /**
     * 终止
     *
     * @param token
     * @param objects
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/term", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    public ServiceResult term(@RequestHeader(value="token") String token, @RequestBody List<AllotApplyBill> objects);

    /**
     * 完成
     *
     * @param token
     * @param objects
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/complete", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    public ServiceResult complete(@RequestHeader(value="token") String token, @RequestBody List<AllotApplyBill> objects);

    /**
     * 完成调拨申请单及分步调拨单
     * 并设置调拨申请单差异状态为 DiffStatus.DIFF_NOT_HANDLE
     *
     * @param token
     * @param objects
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/completeAndDiff", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    public ServiceResult completeAndDiff(@RequestHeader(value="token") String token, @RequestBody List<AllotApplyBill> objects);

    /**
     * 单据终止后复制
     *
     * @param token
     * @param ids
     * @return
     */
    @ApiOperation(value = "copySub", nickname = "copySub")
    @ResponseBody
    @PostMapping(value = "/copySub", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    public ServiceResult copy(@RequestHeader(value="token") String token, @RequestBody Object[] ids);

    /***
     * 执行
     */
    @ResponseBody
    @PostMapping(value = "/execute", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    public ServiceResult execute(@RequestHeader(value="token") String token, @RequestBody IAllotApplyBillExecuteRequest IAllotApplyBillExecuteRequest);

    /**
     * 查询该单据商品明细差异数、库存数
     *
     * @param token
     * @param selector
     * @param pageSize
     * @param pageIndex
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/queryGoodsAndStock", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    public List<AllotApplyGoodsDetail> queryGoodsAndStock(@RequestHeader(value="token") String token, @RequestBody E3Selector selector, @RequestParam(value="pageSize") int pageSize, @RequestParam(value="pageIndex") int pageIndex);

    /**
     * 查询该单据商品明细差异数、库存数 总条数
     *
     * @param token
     * @param selector
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getListCountGoodsAndStock", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    public long getListCountGoodsAndStock(@RequestHeader(value="token") String token, @RequestBody E3Selector selector);

    /**
     * 生成差异调拨申请单
     *
     * @param token
     * @param objects
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/createDiffAllotApply", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    public ServiceResult createDiffAllotApply(@RequestHeader(value="token") String token, @RequestBody List<AllotApplyBill> objects);

//    /**
//     * 调拨出库通知单审核时推送到WMS
//     * @param token token
//     * @param bills 单据
//     */
//	ServiceResult pushDateToWms(String token, AllotApplyBill[] bills);

    /***
     * 调拨出库通知单终止时推送到WMS
     * @param token token
     * @param billArrays 单据
     */
//    ServiceResult pushCancelDataToWms(String token, AllotApplyBill[] billArrays);

    /**
     * 修改方法（不做校验 只做保存 用于修改对象的某些字段字段）
     *
     * @param token
     * @param beans
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/update", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    void update(@RequestHeader(value="token") String token, @RequestBody AllotApplyBill[] beans);

    @ResponseBody
    @PostMapping(value = "/updateWmsStatus", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    void updateWmsStatus(@RequestBody AbstractBean bean);

    /**
     * jitx oxo 处理
     *
     * @param token
     * @param retailGeneratedAllotApplyBillRequest
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/retailGeneratedAllotApplyBill", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult retailGeneratedAllotApplyBill(@RequestHeader(value="token") String token, @RequestBody RetailGeneratedAllotApplyBillRequest retailGeneratedAllotApplyBillRequest);

    /**
     * 销售订单共享库存 处理
     *
     * @param token
     * @param retailShareGeneratedAllotApplyBillRequest
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/retailShareGeneratedAllotApplyBill", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult retailShareGeneratedAllotApplyBill(@RequestHeader(value="token") String token, @RequestBody RetailShareGeneratedAllotApplyBillRequest retailShareGeneratedAllotApplyBillRequest);

    /**
     * 京东厂直 处理
     *
     * @param token
     * @param retailJdczGeneratedAllotApplyBillRequest
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/retailJdczGeneratedAllotApplyBill", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult retailJdczGeneratedAllotApplyBill(@RequestHeader(value="token") String token, @RequestBody RetailJdczGeneratedAllotApplyBillRequest retailJdczGeneratedAllotApplyBillRequest);

    /**
     * 天猫超市 处理
     *
     * @param token
     * @param retailTmallMarketGeneratedAllotApplyBillRequest
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/retailTmallMarketGeneratedAllotApplyBill", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult retailTmallMarketGeneratedAllotApplyBill(@RequestHeader(value="token") String token, @RequestBody RetailTmallMarketGeneratedAllotApplyBillRequest retailTmallMarketGeneratedAllotApplyBillRequest);

    /**
     * 京东全渠道商仓 处理
     *
     */
    @ResponseBody
    @PostMapping(value = "/retailJdOmniChannelGeneratedAllotApplyBill", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult retailJdOmniChannelGeneratedAllotApplyBill(@RequestHeader(value="token") String token, @RequestBody RetailJdOmniChannelGeneratedAllotApplyBillRequest retailJdOmniChannelGeneratedAllotApplyBillRequest);

    /**
     * 明细刷价,所有单据导入都需要使用
     *
     * @param token
     * @param updateDetailsPriceRequest
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/updateDetailsPrice", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult updateDetailsPrice(@RequestHeader(value="token") String token, @RequestBody UpdateDetailsPriceRequest updateDetailsPriceRequest);

    /**
     * ITX单据类型分配策略设值
     *
     * @param token token
     * @param bill  调拨申请单
     */
    @ResponseBody
    @PostMapping(value = "/setJitxBillAssignStrategy", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult setJitxBillAssignStrategy(@RequestHeader(value="token") String token, @RequestBody AllotApplyBill bill);

    @ResponseBody
    @PostMapping(value = "/autoJdVmiGeneratedAllotApplyBill", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult autoJdVmiGeneratedAllotApplyBill(@RequestHeader(value="token") String token, @RequestHeader(value="billId") String billId);

    /**
     * 单据类型分配策略设值
     *
     * @param token        token
     * @param bill         调拨申请单
     * @param assignDirect 分配方向
     */
    @ResponseBody
    @PostMapping(value = "/setBillAssignStrategy", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult setBillAssignStrategy(@RequestHeader(value="token") String token, @RequestBody AllotApplyBill bill, @RequestParam(value="assignDirect") Integer assignDirect);

    /**
     * 根据调拨单id更新jit单据推送oms状态
     *
     * @param token
     * @param bill
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/updateAllotApplyJitInfoByBillId", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    public ServiceResult updateAllotApplyJitInfoByBillId(@RequestHeader(value="token") String token, @RequestBody AllotApplyBill bill);


    @ResponseBody
    @PostMapping(value = "/getExportListCount", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    long getExportListCount(@RequestHeader(value="token") String token, @RequestBody E3Selector selector, @RequestParam(value="isList") boolean isList);

    @ResponseBody
    @PostMapping(value = "/getExportQueryPage", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    List<ApplyExportModel> getExportQueryPage(@RequestHeader(value="token") String token, @RequestBody E3Selector selector, @RequestParam(value="index") int index, @RequestParam(value="pageSize") int pageSize);


    /**
     * 调拨单主表导出
     *
     * @param token
     * @param selector
     * @param index
     * @param pageSize
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getListExportQueryPage", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    List<ApplyExportListModel> getListExportQueryPage(@RequestHeader(value="token") String token, @RequestBody E3Selector selector, @RequestParam(value="index") int index, @RequestParam(value="pageSize") int pageSize);

    /**
     * 导入调拨申请单
     *
     * @param token
     * @param importHandleRequest
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/importHandle", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    String importHandle(@RequestHeader(value="token") String token, @RequestBody ImportHandleRequest importHandleRequest);

    @ResponseBody
    @PostMapping(value = "/selectSampleAllocateBill", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    List<AllotApplyBill> selectSampleAllocateBill(@RequestBody List<String> applyIds);

    @ResponseBody
    @PostMapping(value = "/queryAllotApplyBillSimpleList", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    List<AllotApplyBill> queryAllotApplyBillSimpleList(@RequestBody AllotApplyBillParamDTO param);

    /**
     * 查询超期单据
     */
    @ResponseBody
    @PostMapping(value = "/selectTimeOutDocuments", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    List<AllotApplyBill> selectTimeOutDocuments();

    /**
     * 创建并审核调拨申请单，返回调拨申请单id
     *
     * @param token
     * @param dto
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/createAndAuditBill", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    Result<Object> createAndAuditBill(@RequestHeader(value="token") String token, @RequestBody CreateAllocationDto dto);

    /**
     * 执行调拨申请单 返回分步调拨单id
     *
     * @param token
     * @param dto
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/executeBill", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    Result<Object> executeBill(@RequestHeader(value="token") String token, @RequestBody ExecAllocationDto dto);

    /**
     * 根据调拨申请单id取消
     *
     * @param token
     * @param id
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/cancelBillById", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    Result<Object> cancelBillById(@RequestHeader(value="token") String token, @RequestHeader(value="id") String id);

    /**
     * 获取超期需预警的单据
     *
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getBillEarlyWarning", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    List<EarlyWarningStockModel>  getBillEarlyWarning(@RequestHeader(value="token") String token, @RequestParam(value="isOverdue") boolean isOverdue);
}
