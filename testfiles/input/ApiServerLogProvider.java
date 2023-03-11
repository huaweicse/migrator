package com.baison.e3.middleware.apilog;

import bs2.middleware.orm.core.AbstractBean;
import com.alibaba.boot.hsf.annotation.HSFProvider;
import com.baison.e3.middleware.apilog.model.ApiServerLog;
import com.baison.e3.middleware.apilog.model.ApiServerLogMissBarcode;
import com.baison.e3.middleware.apilog.model.ApiServerLogText;
import com.baison.e3.middleware.apilog.service.IApiServerLogService;
import com.baison.e3.middleware.base.BaseService;
import e3.middleware.query.E3Selector;
import e3.middleware.result.model.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

/**
 * Api服务日志 provider
 *
 * @author wangzhizhao
 * @date 2020-4-21 0:12
 */
@HSFProvider(serviceInterface = IApiServerLogService.class, serviceVersion = "1.0.0")
public class ApiServerLogProvider extends BaseService<ApiServerLog> implements IApiServerLogService {

    @Autowired
    private IApiServerLogService apiServerLogService;

    @Override
    protected Class<? extends AbstractBean> getClazz() {
        return ApiServerLog.class;
    }

    @Override
    public ApiServerLog[] queryPageObject(String token, E3Selector selector, int pageSize, int pageIndex){
        return apiServerLogService.queryPageObject(token, selector, pageSize, pageIndex);
    }

    @Override
    public long getListCount(String token, E3Selector selector) {
        return apiServerLogService.getListCount(token, selector);
    }

    @Override
    public ServiceResult createObject(String token, ApiServerLog[] object) {
        return apiServerLogService.createObject(token, object);
    }

    @Override
    public ServiceResult modifyObject(String token, ApiServerLog[] object) {
        return apiServerLogService.modifyObject(token, object);
    }

    @Override
    public ApiServerLogText queryApiServerLogText(String token, E3Selector selector) {
        return apiServerLogService.queryApiServerLogText(token, selector);
    }

    @Override
    public boolean compressLog(int lastTime) throws IOException {
        return apiServerLogService.compressLog(lastTime);
    }

    @Override
    public boolean compressOldLog() throws IOException {
        return apiServerLogService.compressOldLog();
    }

    @Override
    public ApiServerLogMissBarcode[] queryAllotApplyMissBarcode(List<String> sourceNoList) {
        return apiServerLogService.queryAllotApplyMissBarcode(sourceNoList);
    }

}
