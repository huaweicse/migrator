package com.baison.e3.middleware.workflow.service;

import com.baison.e3.middleware.workflow.model.*;
import e3.middleware.query.E3Selector;
import e3.middleware.result.model.ServiceResult;
import io.swagger.annotations.ApiOperation;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 工作流服务
 *
 * @author jun.chen
 */
public interface IWorkflowService {

    public static String REFMODEL = "refModel";

    public static String DATAIDKEY = "dataId";

    public static String FOUNDERKEY = "founder";

    public static String ACTIONKEY = "action";

    public static String START = "start";

    public static String SUBMIT = "submit";

    public static String GIVEUP = "giveup";

    public static String SAVE = "save";

    public static String APPROVE = "approve";

    public static String REFUSE = "refuse";

    public static String CALLSUBPROCESS = "callSubprocess";

    public static String HISTORY = "history";

    public static String METADATAID = "metadataId";

    public static String PARENTMETADATAID = "parentMetadataId";

    public static String PARENTDATAID = "parentDataId";

    public static String CHILDDATAID = "childDataId";

    public static String PROCESSTEMPLATEIDKEY = "processTemplateId";

    public static String PARAEXCL = "paraExcl";

    public static String TASKVISIABLE = "taskVisiable";

    public static String DATACODE = "datacode";

    public static String DISPLAYNAME = "displayname";
    /**
     * 执行对象
     */
    public static String EXCUTEBEAN = "excuteBean";
    /**
     * 执行中的选项,用于判断流程走向
     */
    public static String SELECTION = "operateCode";

    public static String TASKDESCRIPTION = "taskDescription";
    /**
     * 默认签收人
     */
    public static String DEFACTOR = "defActor";

    public static String CHANNEL = "channel";

    public static String BRAND = "brand";

    public static String REMARK = "remark";

    /**
     * OK<br/>
     * 部署流程到流程引擎中
     *
     * @param key
     * @param processDefXML <br/>
     *                      在 processDefXML中,id需要满足命名规范<br/>
     *                      如不能以数字开头
     */
    void deployProcess(String key, String processDefXML);

    /**
     * OK<br/>
     * 从流程引擎中卸载流程<br/>
     * jun.chen
     *
     * @param key
     */
    void unDeployProcess(String key);

    // /**
    // * 根据用户id，获取历史数据(用历史任务查询接口替换)
    // *
    // * @param token
    // * @param userId
    // * @param finished
    // * 流程是否已经完成
    // * @return
    // */
    // List<HistoryTaskInfo> getHistoryInfosByUserId(String token, String userId,
    // boolean finished);

    // /**
    // * 根据流程实例id,获取历史数据（findbyId）
    // *
    // * @param token
    // * @param processInstanceId
    // * @return
    // */
    // List<HistoryTaskInfo> getHistoryInfosByProcessId(String token, String
    // processInstanceId);

    /**
     * 如果是管理员可以获取所有流程引擎中的流程定义，否则根据用户ID或用户角色IDs从流程引擎中获取流程定义。
     *
     * @param userId  可以为null
     * @param groups  可以为null
     * @param isAdmin 是否是管理员
     * @return
     */
    List<ProcessInfo> getDeployedProcInfos(String userId, List<String> groupIds, boolean isAdmin);

    /**
     * 根据流程定义key，获取流程定义中的表单变量
     *
     * @param key
     * @return
     */
    Map<String, ?> getProcFormVariablesByKey(String key);

    /**
     * 根据流程定义名称获取对应的key
     *
     * @param processDefinitionName
     * @return
     */
    String getProcessKey(String processDefinitionName);

    /**
     * 发起流程实例
     *
     * @param key
     * @param userId
     * @param dataId
     * @param processTemplateId
     */
    @ApiOperation(value = "startProcessByUserId", nickname = "startProcessByUserId")
    String startProcess(String token, String key, String userId, String dataId, String processTemplateId);

    /**
     * 入口( 待测试 )<br/>
     * 保存流程实例任务，根据key和dataId判断任务是否存在，如果不存在先发起流。
     *
     * @param key
     * @param dataId
     * @param userId
     * @param groups
     * @param processTemplateId
     * @param variables         可以为null
     */
    void saveProcess(String token, String key, String dataId, String userId, List<String> groupIds,
                     String processTemplateId, Map<String, ?> variables) throws Exception;

    /**
     * 如果是管理员可以获取所有任务，否则根据用户ID或用户角色IDs获取任务。
     *
     * @param userId
     * @param groupIds
     * @param isAdmin  是否是管理员
     * @return
     */
    List<TaskInfo> getTasks(String token, String userId, List<String> groupIds, boolean isAdmin);

    /**
     * 根据用户和模块查找任务列表
     *
     * @param _userId
     * @param billId
     * @return
     */
    List<TaskInfo> getUserModelTask(String token, String refModel);

    /**
     * 根据用户和单据主键查找任务
     *
     * @param _userId
     * @param billId
     * @return
     */
    List<TaskInfo> getUserTask(String token, String billId, String refModel);

    /**
     * 根据模块查找用户的任务
     *
     * @param token
     * @param pageSize
     * @param pageIndex
     * @param refModel
     * @return
     */
    Map<String, Object> getPageUserTasks(String token, int pageSize, int pageIndex, String refModel);

    /**
     * 根据任务Id，获取任务信息
     *
     * @param id
     * @return
     */
    TaskInfo getTaskById(String id);

    TaskInfo queryTaskById(String token, String id);

    /**
     * 根据流程定义key获取流程引擎中的流程定义信息
     *
     * @param key
     * @return
     */
    ProcessInfo getProcInfoByKey(String key);

    /**
     * 根据流程定义id获取流程引擎中的流程定义信息
     *
     * @param id
     * @return
     */
    ProcessInfo getProcInfoById(String id);

    /**
     * 根据key、dataId、userId和groupIds找到对应任务并提交
     *
     * @param token
     * @param key
     * @param dataId
     * @param userId
     * @param groupIds
     * @param variables
     */
    void submitProcess(String token, ServiceResult result, String key, String dataId, String userId,
                       List<String> groupIds, Map<String, ?> variables) throws Exception;

    /**
     * 根据key、dataId、userId和groupIds找到对应任务并放弃/删除
     *
     * @param token
     * @param key
     * @param dataId
     * @param userId
     * @param groupIds
     * @param variables
     */
    void giveupProcess(String token, ServiceResult result, String key, String dataId, String userId,
                       List<String> groupIds, Map<String, ?> variables) throws Exception;

    /**
     * 根据key、dataId、userId和groupIds找到对应任务并同意
     *
     * @param token
     * @param key
     * @param dataId
     * @param userId
     * @param groupIds
     * @param variables
     */
    void approveProcess(String token, ServiceResult result, String key, String dataId, String userId,
                        List<String> groupIds, Map<String, ?> variables) throws Exception;

    /**
     * 根据key、dataId、userId和groupIds找到对应任务并拒绝
     *
     * @param token
     * @param key
     * @param dataId
     * @param userId
     * @param groupIds
     * @param variables
     */
    void refuseProcess(String token, ServiceResult result, String key, String dataId, String userId,
                       List<String> groupIds, Map<String, ?> variables) throws Exception;

    /**
     * 根据key、dataId、userId和groupIds找到对应任务并调用子任务
     *
     * @param token
     * @param parentKey
     * @param parentDataId
     * @param childDataId
     * @param userId
     * @param groupIds
     * @param variables
     */
    void callChildProcess(String token, String parentKey, String parentDataId, String childDataId, String userId,
                          List<String> groupIds, Map<String, ?> variables) throws Exception;

    /**
     * （可忽略） 从平台表中获取流程定义信息
     *
     * @param token
     * @return
     */
    List<ProcessDefInfo> getProcessDEFInfos(String token, E3Selector selector);

    /**
     * （可忽略） 从平台表中获取流程定义分页信息
     *
     * @param token
     * @param selector
     * @param pageSize
     * @param pageIndex
     * @return
     */
    List<ProcessDefInfo> queryProcessDEFInfoPage(String token, E3Selector selector, int pageSize, int pageIndex);

    /**
     * （可忽略） 根据id，从平台表中获取流程定义信息
     *
     * @param token
     * @param id
     * @return
     */
    ServiceResult getProcessDefInfoById(String token, String id);

    /**
     * （可忽略） 根据id，从平台表中获取流程定义组信息
     *
     * @param token
     * @param id
     * @return
     */
    ServiceResult getProcessDefGroupInfoById(String token, String id);

    /**
     * （可忽略） 从平台表中获取流程定义组信息
     *
     * @param token
     * @return
     */
    ServiceResult getProcessDefGroupsInfos(String token, E3Selector selector, int pageSize, int pageIndex);

    /**
     * （可忽略） 保存流程定义组信息到平台表中
     *
     * @param token
     * @param processDefGroup
     * @return
     */
    ServiceResult saveProcessDefGroup(String token, ProcessGroup processDefGroup);

    /**
     * （可忽略） 根据流程组id，流程定义组
     *
     * @param token
     * @param id
     */
    ServiceResult deleteProcessDefGroupById(String token, String id);

    /**
     * （可忽略） 根据key，从平台表中获取备份流程定义信息
     *
     * @param token
     * @param key
     * @return
     */
    ServiceResult getProcessDefInfoCopyByKey(String token, E3Selector selector, int pageSize, int pageIndex);

    /**
     * （可忽略） 保存备份流程定义信息到平台表中
     *
     * @param token
     * @param procDefCopy
     */
    ServiceResult saveProcessDefInfoCopy(String token, ProcessDefInfoCopy procDefCopy);

    /**
     * （可忽略） 根据模板key，从平台表中删除对应备份流程定义信息
     *
     * @param key
     */
    ServiceResult deleteProcessDefInfoCopyByKey(String token, String key);

    /**
     * 根据key，判断是否有对应流程实例
     *
     * @param key
     * @return
     */
    boolean hasProcessInstanceByKey(String key);

    /**
     * (OK) 获取流程实例并转换
     *
     * @return
     */
    List<ProcessInstanceInfo> getProcessInstance();

    /**
     * （OK） 根据流程procInstanceId，获取流程实例
     *
     * @param procInstanceId
     * @return
     */
    ProcessInstanceInfo getProcessInstanceById(String procInstanceId);

    /**
     * （OK） 根据流程procInstanceId，暂停流程实例
     *
     * @param id
     * @return
     */
    void suspendProcessInstanceById(String id);

    /**
     * （OK） 根据流程procInstanceId，激活流程实例
     *
     * @param id
     * @return
     */
    void activateProcessInstanceById(String id);

    /**
     * （OK） 根据流程procInstanceId，删除流程实例
     *
     * @param id
     * @return
     */
    void deleteProcessInstanceById(String id);

    /**
     * （可忽略） 获取流程定义集
     *
     * @param token
     * @param mainCondition
     * @param detialConditon
     * @return
     */
    List<ProcessDefInfo> getProcessDefs(String token, E3Selector selector, int pageSize, int pageIndex);

    /**
     * （可忽略） 保存流程定义明细
     *
     * @param processDefDetail
     */
    ServiceResult saveProcessDefDetail(String token, ProcessDefInfoDetail processDefDetail);

    /**
     * （可忽略） 根据id，从平台表中删除流程定义
     *
     * @param token
     * @param id
     */
    ServiceResult deleteProcessDefById(String token, String id);

    /**
     * （可忽略） 启用
     *
     * @param token
     * @param id
     */
    ServiceResult enableProcessDefDetailById(String token, String id);

    /**
     * （可忽略） 禁用
     *
     * @param token
     * @param id
     */
    ServiceResult disableProcessDefDetailById(String token, String id);

    /**
     * （可忽略） 设为默认版本
     *
     * @param token
     * @param id
     */
    ServiceResult defaultProcessDefDetailById(String token, String id);

    /**
     * （可忽略） 设为默认编辑
     *
     * @param token
     * @param id
     */
    ServiceResult editProcessDefDetailById(String token, String id);

    /**
     * （可忽略） 通过明细Id获取工作流明细
     *
     * @param token
     * @param ids
     * @return
     */
    List<ProcessDefInfoDetail> getProcessDefInfoDetailById(String token, String[] ids);

    /**
     * （可忽略） 保存流程定义信息到平台表中
     *
     * @param token
     * @param processDef
     * @return
     */
    ProcessDefInfo saveProcessDef(String token, ProcessDefInfo processDef);

    /**
     * （OK） 用户处理任务
     *  @param token
     * @param workFlowHandleActionRequest
     */
    void handleAction(String token,
        WorkFlowHandleActionRequest workFlowHandleActionRequest);

    /**
     * (OK) 任务认领
     *
     * @param taskInfo
     * @param userId
     */
    void assignTask(TaskInfo taskInfo, String userId);

    /**
     * (OK) 根据流程Key获取Task信息
     *
     * @param processKey
     * @return
     */
    List<TaskInfo> getTasksByProcessKey(String processKey);

    /**
     *
     * @return
     */
    ServiceResult getHistory();

    /**
     * 根据用户Id查询用户发起的流程
     *
     * @param token
     * @param userId
     * @return
     */
    List<ProcessInfo> getUserProcessInfo(String token, E3Selector selector, int pageSize, int pageIndex);

    /**
     * 获取用户发起的流程
     *
     * @param token
     * @param selector
     * @param pageSize
     * @param pageIndex
     * @return
     */
    Map<String, Object> getPageUserProcessInfo(String token, E3Selector selector, int pageSize, int pageIndex);

    /**
     * 执行具体任务
     *
     * @param token
     * @param key
     * @param dataId
     * @param variables
     * @param type
     * @return
     */
    ServiceResult doAction(String token, String key, String dataId, Map<String, ?> variables, int type);

    /**
     * 查询Activiti历史任务 转换为HistoryTaskInfo
     *
     * @param token
     * @param selector
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Map<String, Object> queryHistoryTasks(String token, E3Selector selector, int pageIndex, int pageSize);


    /**
     * 查询用户具有的任务<br/>仅限于流程的发起操作人的用户类型为商店
     *
     * @param token
     * @param refModel
     * @param metaCode
     * @param startDate 任务开始时间
     * @param endDate   任务结束时间
     * @return
     */
    ServiceResult queryUserAuthTask(String token, String refModel, String metaCode, Date startDate, Date endDate);

    /**
     * 按照用户Id直接进行任务的完成
     *
     * @param token
     * @param tuserId
     * @param key
     * @param dataId
     * @param variables
     * @param type
     * @return
     */
    ServiceResult doActionByUserId(String token, String tuserId, String key, String dataId, Map<String, ?> variables, int type);

    /**
     * 查询历史任务通过过程Id
     *
     * @param token
     * @param processInfoId
     * @return
     */
    ServiceResult getHistoryTask(String token, String processInfoId, String refModel);

    /**
     * @param token
     * @param metaCode
     * @param obs
     * @param refModel
     * @return ServiceResult
     * @Description: 判断单据是否启动了工作流
     * @author h <jihui.yan@baisonmail.com>
     * @date 2019年1月8日上午11:20:37
     */
    public ServiceResult billOnValidateWorkflow(String token, String metaCode, String billId, String refModel);

    public long getListCount(String token, E3Selector selector);

    /**
     * (OK) 首先发布<br/>
     * 发布流程任务
     *
     * @param token
     * @param selector    查询流程定义明细的条件
     * @param metaCode    业务对象Code
     * @param dataId
     * @param code
     * @param displayName 显示名称
     * @param authority   权威
     * @param refModel    所属模块
     * @param description 描述
     * @param channelId   组织
     * @param brandId     品牌
     */
    @ApiOperation(value = "startProcessBySelector", nickname = "startProcessBySelector")
    ServiceResult startProcess(String token, E3Selector selector, String metaCode, String dataId, String code, String displayName, String authority, String refModel, String description, Long channelId, Long brandId);
}
