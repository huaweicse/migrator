package com.baison.e3.middleware.apilog.service;

import com.baison.e3.middleware.apilog.model.ApiServerLog;
import com.baison.e3.middleware.apilog.model.ApiServerLogMissBarcode;
import com.baison.e3.middleware.apilog.model.ApiServerLogText;
import com.baison.e3.middleware.base.IBaseService;
import e3.middleware.query.E3Selector;

import java.io.IOException;
import java.util.List;

/**
 * API服务日志服务
 *
 * @author wangzhizhao
 * @date 2020-4-20 23:53
 */
public interface IApiServerLogService extends IBaseService<ApiServerLog> {

    /**
     * 查询日志详情
     *
     * @param token
     * @param selector
     * @return
     */
    ApiServerLogText queryApiServerLogText(String token, E3Selector selector);

    /**
     * 压缩新表日志
     *
     * @param lastTime 日志压缩不超过当前时间差
     * @return 如果未查到数据，则返回false
     * @throws IOException 异常
     */
    boolean compressLog(int lastTime) throws IOException;

    /**
     * 压缩旧表日志
     *
     * @return 如果未查到数据，则返回false
     * @throws IOException 异常
     */
    boolean compressOldLog() throws IOException;

    /**
     * 查询WMS缺少商品的调拨单
     *
     * @param sourceNoList 调拨申请单号
     * @return ApiServerLogMissBarcode
     */
    ApiServerLogMissBarcode[] queryAllotApplyMissBarcode(List<String> sourceNoList);

    /**
     * 将redis 中的数据转储到数据库中
     */
    default void transferDataFromRedisToDatabase() {
    }

    /**
     * 重命名表、创建新表、删除过期的表
     */
    default void renameAndCreateNewTable() {
    }

    /**
     * 日志存档后重置表链
     */
    default void resetTableLinkedHead() {
    }

    /**
     * 删除历史数据
     */
    default void deleteHistoryData() {
    }

    /**
     * 初始化可删除的最大id
     *
     * @param maxTimeThatCanBeDelete
     */
    default void initMaxIdThatCanBeDelete(long maxTimeThatCanBeDelete) {
    }
}


