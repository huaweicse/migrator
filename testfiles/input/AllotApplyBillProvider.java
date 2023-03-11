package com.baison.e3.middleware.stock.service.allotapply;

import bs2.middleware.orm.core.AbstractBean;
import com.alibaba.boot.hsf.annotation.HSFProvider;
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
import com.baison.e3.middleware.stock.api.service.allotapply.IAllotApplyBillService;
import com.baison.e3.middleware.stock.service.base.AbstractStockService;
import e3.middleware.query.E3Selector;
import e3.middleware.restful.result.Result;
import e3.middleware.result.model.ServiceResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@HSFProvider(serviceInterface = IAllotApplyBillService.class)
public class AllotApplyBillProvider extends AbstractStockService<AllotApplyBill> implements IAllotApplyBillService {
    @Autowired
    private IAllotApplyBillService allotApplyBillService;

    @Override
    public List<AllotApplyBill> mbQueryBySourceBillNo(String token, List<String> applySourceBillNos) {
        return allotApplyBillService.mbQueryBySourceBillNo(token, applySourceBillNos);
    }

    @Override
    public List<VipRelatePickDetail> getVipRelatePickDetailList(String allotApplyBillId, int pageSize, int pageIndex) {
        return allotApplyBillService.getVipRelatePickDetailList(allotApplyBillId, pageSize, pageIndex);
    }

    @Override
    public Long getVipRelatePickDetailCount(String allotApplyBillId) {
        return allotApplyBillService.getVipRelatePickDetailCount(allotApplyBillId);
    }

    @Override
    public ServiceResult submit(String token, List<AllotApplyBill> objects, Integer isContinue) {
        return allotApplyBillService.submit(token, objects, isContinue);
    }

    @Override
    public ServiceResult audit(String token, List<AllotApplyBill> objects, Integer isContinue) {
        return allotApplyBillService.audit(token, objects, isContinue);
    }

    /**
     * 退回审核状态到初始
     *
     * @param token
     * @param objects
     * @return
     */
    @Override
    public ServiceResult retreatAuditToInit(String token, List<AllotApplyBill> objects) {
        return allotApplyBillService.retreatAuditToInit(token, objects);
    }

    @Override
    public ServiceResult handelOutOfStock(String token, String allotApplyBillId) {
        return allotApplyBillService.handelOutOfStock(token, allotApplyBillId);
    }

    @Override
    public ServiceResult retreat(String token, List<AllotApplyBill> objects) {
        return allotApplyBillService.retreat(token, objects);
    }

    @Override
    public ServiceResult term(String token, List<AllotApplyBill> objects) {
        return allotApplyBillService.term(token, objects);
    }

    @Override
    public ServiceResult complete(String token, List<AllotApplyBill> objects) {
        return allotApplyBillService.complete(token, objects);
    }

    @Override
    public ServiceResult completeAndDiff(String token, List<AllotApplyBill> objects) {
        return allotApplyBillService.completeAndDiff(token, objects);
    }

    @Override
    @ApiOperation(value = "copySub", nickname = "copySub")
    public ServiceResult copy(String token, Object[] ids) {
        return allotApplyBillService.copy(token, ids);
    }

    @Override
    public ServiceResult execute(String token, IAllotApplyBillExecuteRequest IAllotApplyBillExecuteRequest) {
        return allotApplyBillService.execute(token, new IAllotApplyBillExecuteRequest(IAllotApplyBillExecuteRequest.getObjects(), IAllotApplyBillExecuteRequest.getDate(), IAllotApplyBillExecuteRequest.getGoodsDetailsList(), IAllotApplyBillExecuteRequest.getDownBillStatus(), IAllotApplyBillExecuteRequest.getIsContinue()));
    }

    @Override
    public List<AllotApplyGoodsDetail> queryGoodsAndStock(String token, E3Selector selector, int pageSize, int pageIndex) {
        return allotApplyBillService.queryGoodsAndStock(token, selector, pageSize, pageIndex);
    }

    @Override
    public long getListCountGoodsAndStock(String token, E3Selector selector) {
        return allotApplyBillService.getListCountGoodsAndStock(token, selector);
    }

    @Override
    public ServiceResult createDiffAllotApply(String token, List<AllotApplyBill> objects) {
        return allotApplyBillService.createDiffAllotApply(token, objects);
    }

    @Override
    public void update(String token, AllotApplyBill[] beans) {
        allotApplyBillService.update(token, beans);
    }

    @Override
    @ApiOperation(value = "", hidden = true)
    // TODO hide
    public void updateWmsStatus(AbstractBean bean) {
        allotApplyBillService.updateWmsStatus(bean);
    }

    @Override
    public ServiceResult retailGeneratedAllotApplyBill(String token, RetailGeneratedAllotApplyBillRequest retailGeneratedAllotApplyBillRequest) {
        return allotApplyBillService.retailGeneratedAllotApplyBill(token, new RetailGeneratedAllotApplyBillRequest(retailGeneratedAllotApplyBillRequest.getStockInfos(), retailGeneratedAllotApplyBillRequest.getBeans(), retailGeneratedAllotApplyBillRequest.getCheckQtyAvailable()));
    }

    @Override
    public ServiceResult retailShareGeneratedAllotApplyBill(String token, RetailShareGeneratedAllotApplyBillRequest retailShareGeneratedAllotApplyBillRequest) {
        return allotApplyBillService.retailShareGeneratedAllotApplyBill(token, new RetailShareGeneratedAllotApplyBillRequest(retailShareGeneratedAllotApplyBillRequest.getStockInfos(), retailShareGeneratedAllotApplyBillRequest.getBeans(), retailShareGeneratedAllotApplyBillRequest.getChasingParas()));
    }

    @Override
    public ServiceResult retailJdczGeneratedAllotApplyBill(String token, RetailJdczGeneratedAllotApplyBillRequest retailJdczGeneratedAllotApplyBillRequest) {
        return allotApplyBillService.retailJdczGeneratedAllotApplyBill(token, new RetailJdczGeneratedAllotApplyBillRequest(retailJdczGeneratedAllotApplyBillRequest.getStockInfos(), retailJdczGeneratedAllotApplyBillRequest.getBeans(), retailJdczGeneratedAllotApplyBillRequest.getChasingParas()));
    }

    @Override
    public ServiceResult retailTmallMarketGeneratedAllotApplyBill(String token, RetailTmallMarketGeneratedAllotApplyBillRequest retailTmallMarketGeneratedAllotApplyBillRequest) {
        return allotApplyBillService.retailTmallMarketGeneratedAllotApplyBill(token, new RetailTmallMarketGeneratedAllotApplyBillRequest(retailTmallMarketGeneratedAllotApplyBillRequest.getStockInfos(), retailTmallMarketGeneratedAllotApplyBillRequest.getBeans(), retailTmallMarketGeneratedAllotApplyBillRequest.getChasingParas()));
    }

    @Override
    public ServiceResult retailJdOmniChannelGeneratedAllotApplyBill(String token, RetailJdOmniChannelGeneratedAllotApplyBillRequest retailJdOmniChannelGeneratedAllotApplyBillRequest) {
        return allotApplyBillService.retailJdOmniChannelGeneratedAllotApplyBill(token, new RetailJdOmniChannelGeneratedAllotApplyBillRequest(retailJdOmniChannelGeneratedAllotApplyBillRequest.getStockInfos(), retailJdOmniChannelGeneratedAllotApplyBillRequest.getBeans(), retailJdOmniChannelGeneratedAllotApplyBillRequest.getChasingParas()));
    }

    @Override
    protected Class<? extends AllotApplyBill> getClazz() {
        return AllotApplyBill.class;
    }

    @Override
    public ServiceResult createObject(String token, AllotApplyBill[] beans) {
        return allotApplyBillService.createObject(token, beans);
    }

    @Override
    public ServiceResult modifyObject(String token, AllotApplyBill[] beans) {
        return allotApplyBillService.modifyObject(token, beans);
    }

    @Override
    public AllotApplyBill[] queryPageDetails(String token, E3Selector querySelector, int pageSize, int pageIndex) {
        return allotApplyBillService.queryPageDetails(token, querySelector, pageSize, pageIndex);
    }

    @Override
    public ServiceResult updateDetailsPrice(String token, UpdateDetailsPriceRequest updateDetailsPriceRequest) {
        return allotApplyBillService.updateDetailsPrice(token, new UpdateDetailsPriceRequest(updateDetailsPriceRequest.getIds(), updateDetailsPriceRequest.getSaveBeans()));
    }

    @Override
    public ServiceResult autoJdVmiGeneratedAllotApplyBill(String token, String billId) {
        return allotApplyBillService.autoJdVmiGeneratedAllotApplyBill(token, billId);
    }

    @Override
    public ServiceResult setBillAssignStrategy(String token, AllotApplyBill bill, Integer assignDirect) {
        return allotApplyBillService.setBillAssignStrategy(token, bill, assignDirect);
    }

    @Override
    public ServiceResult updateAllotApplyJitInfoByBillId(String token, AllotApplyBill bill) {
        return allotApplyBillService.updateAllotApplyJitInfoByBillId(token, bill);
    }

    @Override
    public ServiceResult saveImportDatas(String token, AbstractBean[] saveBeans, String billId) {
        return allotApplyBillService.saveImportDatas(token, saveBeans, billId);
    }

    @Override
    public long getExportListCount(String token, E3Selector selector, boolean isList) {
        return allotApplyBillService.getExportListCount(token, selector, isList);
    }

    @Override
    public List<ApplyExportModel> getExportQueryPage(String token, E3Selector selector, int index, int pageSize) {
        return allotApplyBillService.getExportQueryPage(token, selector, index, pageSize);
    }

    @Override
    public List<ApplyExportListModel> getListExportQueryPage(String token, E3Selector selector, int index, int pageSize) {
        return allotApplyBillService.getListExportQueryPage(token, selector, index, pageSize);
    }

    @Override
    public String importHandle(String token, ImportHandleRequest importHandleRequest) {
        return allotApplyBillService.importHandle(token, new ImportHandleRequest(importHandleRequest.getDtos(), importHandleRequest.getAddressDtos(), importHandleRequest.getAsyncTask()));
    }

    @Override
    public ServiceResult removeObject(String token, String[] ids) {
        return allotApplyBillService.removeObject(token, ids);
    }

    @Override
    public ServiceResult setJitxBillAssignStrategy(String token, AllotApplyBill bill) {
        return allotApplyBillService.setJitxBillAssignStrategy(token, bill);
    }

    @Override
    public List<AllotApplyBill> selectSampleAllocateBill(List<String> applyIds) {
        return allotApplyBillService.selectSampleAllocateBill(applyIds);
    }

    @Override
    public List<AllotApplyBill> queryAllotApplyBillSimpleList(AllotApplyBillParamDTO param) {
        return allotApplyBillService.queryAllotApplyBillSimpleList(param);
    }

    @Override
    public List<AllotApplyBill> selectTimeOutDocuments() {
        return allotApplyBillService.selectTimeOutDocuments();
    }

    /**
     * 创建并审核调拨申请单
     *
     * @param token
     * @param dto
     * @return
     */
    @Override
    public Result<Object> createAndAuditBill(String token, CreateAllocationDto dto) {
        return allotApplyBillService.createAndAuditBill(token,dto);
    }

    /**
     * 执行调拨申请单 返回分步调拨单id
     *
     * @param token
     * @param dto
     * @return
     */
    @Override
    public Result<Object> executeBill(String token, ExecAllocationDto dto) {
        return allotApplyBillService.executeBill(token,dto);
    }

    /**
     * 根据调拨申请单id取消
     *
     * @param token
     * @param id
     * @return
     */
    @Override
    public Result<Object> cancelBillById(String token, String id) {
        return allotApplyBillService.cancelBillById(token,id);
    }

    @Override
    public List<EarlyWarningStockModel> getBillEarlyWarning(String token,boolean isOverdue) {
        return allotApplyBillService.getBillEarlyWarning(token,isOverdue);
    }
}
