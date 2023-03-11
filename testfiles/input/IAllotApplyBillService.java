package com.baison.e3.middleware.stock.api.service.allotapply;

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


    List<AllotApplyBill> mbQueryBySourceBillNo(String token, List<String> applySourceBillNos);

    /***
     * 获取关联拣货单明细
     * @param allotApplyBillId
     * @param pageSize
     * @param pageIndex
     * @return
     */
    List<VipRelatePickDetail> getVipRelatePickDetailList(String allotApplyBillId, int pageSize, int pageIndex);

    /**
     * 获取关联拣货单明细数量
     *
     * @param allotApplyBillId
     * @return
     */
    Long getVipRelatePickDetailCount(String allotApplyBillId);

    /**
     * 提交
     *
     * @param token
     * @param objects
     * @param isContinue 0-正常校验 1-不做负库存校验 2-不做溢出校验和负库存校验
     * @return
     */
    public ServiceResult submit(String token, List<AllotApplyBill> objects, Integer isContinue);

    /**
     * 通过Id审核
     *
     * @param token
     * @param objects
     * @param isContinue
     * @return
     */
    public ServiceResult audit(String token, List<AllotApplyBill> objects, Integer isContinue);

    /**
     * 退回审核状态到初始
     *
     * @param token
     * @param objects
     * @return
     */
    public ServiceResult retreatAuditToInit(String token, List<AllotApplyBill> objects);


    /**
     * 处理库存不足
     *
     * @param token
     * @param allotApplyBillId
     * @return
     */
    public ServiceResult handelOutOfStock(String token, String allotApplyBillId);

    /**
     * 退回
     *
     * @param token
     * @param objects
     * @return
     */
    public ServiceResult retreat(String token, List<AllotApplyBill> objects);

    /**
     * 终止
     *
     * @param token
     * @param objects
     * @return
     */
    public ServiceResult term(String token, List<AllotApplyBill> objects);

    /**
     * 完成
     *
     * @param token
     * @param objects
     * @return
     */
    public ServiceResult complete(String token, List<AllotApplyBill> objects);

    /**
     * 完成调拨申请单及分步调拨单
     * 并设置调拨申请单差异状态为 DiffStatus.DIFF_NOT_HANDLE
     *
     * @param token
     * @param objects
     * @return
     */
    public ServiceResult completeAndDiff(String token, List<AllotApplyBill> objects);

    /**
     * 单据终止后复制
     *
     * @param token
     * @param ids
     * @return
     */
    @ApiOperation(value = "copySub", nickname = "copySub")
    public ServiceResult copy(String token, Object[] ids);

    /***
     * 执行
     */
    public ServiceResult execute(String token, IAllotApplyBillExecuteRequest IAllotApplyBillExecuteRequest);

    /**
     * 查询该单据商品明细差异数、库存数
     *
     * @param token
     * @param selector
     * @param pageSize
     * @param pageIndex
     * @return
     */
    public List<AllotApplyGoodsDetail> queryGoodsAndStock(String token, E3Selector selector, int pageSize,
                                                          int pageIndex);

    /**
     * 查询该单据商品明细差异数、库存数 总条数
     *
     * @param token
     * @param selector
     * @return
     */
    public long getListCountGoodsAndStock(String token, E3Selector selector);

    /**
     * 生成差异调拨申请单
     *
     * @param token
     * @param objects
     * @return
     */
    public ServiceResult createDiffAllotApply(String token, List<AllotApplyBill> objects);

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
    void update(String token, AllotApplyBill[] beans);

    void updateWmsStatus(AbstractBean bean);

    /**
     * jitx oxo 处理
     *
     * @param token
     * @param retailGeneratedAllotApplyBillRequest
     * @return
     */
    ServiceResult retailGeneratedAllotApplyBill(String token, RetailGeneratedAllotApplyBillRequest retailGeneratedAllotApplyBillRequest);

    /**
     * 销售订单共享库存 处理
     *
     * @param token
     * @param retailShareGeneratedAllotApplyBillRequest
     * @return
     */
    ServiceResult retailShareGeneratedAllotApplyBill(String token, RetailShareGeneratedAllotApplyBillRequest retailShareGeneratedAllotApplyBillRequest);

    /**
     * 京东厂直 处理
     *
     * @param token
     * @param retailJdczGeneratedAllotApplyBillRequest
     * @return
     */
    ServiceResult retailJdczGeneratedAllotApplyBill(String token, RetailJdczGeneratedAllotApplyBillRequest retailJdczGeneratedAllotApplyBillRequest);

    /**
     * 天猫超市 处理
     *
     * @param token
     * @param retailTmallMarketGeneratedAllotApplyBillRequest
     * @return
     */
    ServiceResult retailTmallMarketGeneratedAllotApplyBill(String token, RetailTmallMarketGeneratedAllotApplyBillRequest retailTmallMarketGeneratedAllotApplyBillRequest);

    /**
     * 京东全渠道商仓 处理
     *
     */
    ServiceResult retailJdOmniChannelGeneratedAllotApplyBill(String token, RetailJdOmniChannelGeneratedAllotApplyBillRequest retailJdOmniChannelGeneratedAllotApplyBillRequest);

    /**
     * 明细刷价,所有单据导入都需要使用
     *
     * @param token
     * @param updateDetailsPriceRequest
     * @return
     */
    ServiceResult updateDetailsPrice(String token, UpdateDetailsPriceRequest updateDetailsPriceRequest);

    /**
     * ITX单据类型分配策略设值
     *
     * @param token token
     * @param bill  调拨申请单
     */
    ServiceResult setJitxBillAssignStrategy(String token, AllotApplyBill bill);

    ServiceResult autoJdVmiGeneratedAllotApplyBill(String token, String billId);

    /**
     * 单据类型分配策略设值
     *
     * @param token        token
     * @param bill         调拨申请单
     * @param assignDirect 分配方向
     */
    ServiceResult setBillAssignStrategy(String token, AllotApplyBill bill, Integer assignDirect);

    /**
     * 根据调拨单id更新jit单据推送oms状态
     *
     * @param token
     * @param bill
     * @return
     */
    public ServiceResult updateAllotApplyJitInfoByBillId(String token, AllotApplyBill bill);


    long getExportListCount(String token, E3Selector selector, boolean isList);

    List<ApplyExportModel> getExportQueryPage(String token, E3Selector selector, int index, int pageSize);


    /**
     * 调拨单主表导出
     *
     * @param token
     * @param selector
     * @param index
     * @param pageSize
     * @return
     */
    List<ApplyExportListModel> getListExportQueryPage(String token, E3Selector selector, int index, int pageSize);

    /**
     * 导入调拨申请单
     *
     * @param token
     * @param importHandleRequest
     * @return
     */
    String importHandle(String token, ImportHandleRequest importHandleRequest);

    List<AllotApplyBill> selectSampleAllocateBill(List<String> applyIds);

    List<AllotApplyBill> queryAllotApplyBillSimpleList(AllotApplyBillParamDTO param);

    /**
     * 查询超期单据
     */
    List<AllotApplyBill> selectTimeOutDocuments();

    /**
     * 创建并审核调拨申请单，返回调拨申请单id
     *
     * @param token
     * @param dto
     * @return
     */
    Result<Object> createAndAuditBill(String token, CreateAllocationDto dto);

    /**
     * 执行调拨申请单 返回分步调拨单id
     *
     * @param token
     * @param dto
     * @return
     */
    Result<Object> executeBill(String token, ExecAllocationDto dto);

    /**
     * 根据调拨申请单id取消
     *
     * @param token
     * @param id
     * @return
     */
    Result<Object> cancelBillById(String token, String id);

    /**
     * 获取超期需预警的单据
     *
     * @return
     */
    List<EarlyWarningStockModel>  getBillEarlyWarning(String token,boolean isOverdue);
}
