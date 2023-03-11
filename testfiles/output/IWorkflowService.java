package com.baison.e3.middleware.workflow.service;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;

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
    @ResponseBody
    @PostMapping(value = "/deployProcess", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    void deployProcess(@RequestHeader(value="key") String key, @RequestHeader(value="processDefXML") String processDefXML);

    /**
     * OK<br/>
     * 从流程引擎中卸载流程<br/>
     * jun.chen
     *
     * @param key
     */
    @ResponseBody
    @PostMapping(value = "/unDeployProcess", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    void unDeployProcess(@RequestHeader(value="key") String key);

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
    @ResponseBody
    @PostMapping(value = "/getDeployedProcInfos", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    List<ProcessInfo> getDeployedProcInfos(@RequestHeader(value="userId") String userId, @RequestBody List<String> groupIds, @RequestParam(value="isAdmin") boolean isAdmin);

    /**
     * 根据流程定义key，获取流程定义中的表单变量
     *
     * @param key
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getProcFormVariablesByKey", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    Map<String, ?> getProcFormVariablesByKey(@RequestHeader(value="key") String key);

    /**
     * 根据流程定义名称获取对应的key
     *
     * @param processDefinitionName
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getProcessKey", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    String getProcessKey(@RequestHeader(value="processDefinitionName") String processDefinitionName);

    /**
     * 发起流程实例
     *
     * @param key
     * @param userId
     * @param dataId
     * @param processTemplateId
     */
    @ApiOperation(value = "startProcessByUserId", nickname = "startProcessByUserId")
    @ResponseBody
    @PostMapping(value = "/startProcessByUserId", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    String startProcess(@RequestHeader(value="token") String token, @RequestHeader(value="key") String key, @RequestHeader(value="userId") String userId, @RequestHeader(value="dataId") String dataId, @RequestHeader(value="processTemplateId") String processTemplateId);

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
    @ResponseBody
    @PostMapping(value = "/saveProcess", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    void saveProcess(@RequestHeader(value="token") String token, @RequestHeader(value="key") String key, @RequestHeader(value="dataId") String dataId, @RequestHeader(value="userId") String userId, @RequestBody List<String> groupIds, @RequestHeader(value="processTemplateId") String processTemplateId, @RequestBody Map<String, ?> variables);

    /**
     * 如果是管理员可以获取所有任务，否则根据用户ID或用户角色IDs获取任务。
     *
     * @param userId
     * @param groupIds
     * @param isAdmin  是否是管理员
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getTasks", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    List<TaskInfo> getTasks(@RequestHeader(value="token") String token, @RequestHeader(value="userId") String userId, @RequestBody List<String> groupIds, @RequestParam(value="isAdmin") boolean isAdmin);

    /**
     * 根据用户和模块查找任务列表
     *
     * @param _userId
     * @param billId
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getUserModelTask", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    List<TaskInfo> getUserModelTask(@RequestHeader(value="token") String token, @RequestHeader(value="refModel") String refModel);

    /**
     * 根据用户和单据主键查找任务
     *
     * @param _userId
     * @param billId
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getUserTask", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    List<TaskInfo> getUserTask(@RequestHeader(value="token") String token, @RequestHeader(value="billId") String billId, @RequestHeader(value="refModel") String refModel);

    /**
     * 根据模块查找用户的任务
     *
     * @param token
     * @param pageSize
     * @param pageIndex
     * @param refModel
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getPageUserTasks", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    Map<String, Object> getPageUserTasks(@RequestHeader(value="token") String token, @RequestParam(value="pageSize") int pageSize, @RequestParam(value="pageIndex") int pageIndex, @RequestHeader(value="refModel") String refModel);

    /**
     * 根据任务Id，获取任务信息
     *
     * @param id
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getTaskById", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    TaskInfo getTaskById(@RequestHeader(value="id") String id);

    @ResponseBody
    @PostMapping(value = "/queryTaskById", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    TaskInfo queryTaskById(@RequestHeader(value="token") String token, @RequestHeader(value="id") String id);

    /**
     * 根据流程定义key获取流程引擎中的流程定义信息
     *
     * @param key
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getProcInfoByKey", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ProcessInfo getProcInfoByKey(@RequestHeader(value="key") String key);

    /**
     * 根据流程定义id获取流程引擎中的流程定义信息
     *
     * @param id
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getProcInfoById", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ProcessInfo getProcInfoById(@RequestHeader(value="id") String id);

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
    @ResponseBody
    @PostMapping(value = "/submitProcess", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    void submitProcess(@RequestHeader(value="token") String token, @RequestBody ServiceResult result, @RequestHeader(value="key") String key, @RequestHeader(value="dataId") String dataId, @RequestHeader(value="userId") String userId, @RequestBody List<String> groupIds, @RequestBody Map<String, ?> variables);

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
    @ResponseBody
    @PostMapping(value = "/giveupProcess", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    void giveupProcess(@RequestHeader(value="token") String token, @RequestBody ServiceResult result, @RequestHeader(value="key") String key, @RequestHeader(value="dataId") String dataId, @RequestHeader(value="userId") String userId, @RequestBody List<String> groupIds, @RequestBody Map<String, ?> variables);

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
    @ResponseBody
    @PostMapping(value = "/approveProcess", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    void approveProcess(@RequestHeader(value="token") String token, @RequestBody ServiceResult result, @RequestHeader(value="key") String key, @RequestHeader(value="dataId") String dataId, @RequestHeader(value="userId") String userId, @RequestBody List<String> groupIds, @RequestBody Map<String, ?> variables);

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
    @ResponseBody
    @PostMapping(value = "/refuseProcess", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    void refuseProcess(@RequestHeader(value="token") String token, @RequestBody ServiceResult result, @RequestHeader(value="key") String key, @RequestHeader(value="dataId") String dataId, @RequestHeader(value="userId") String userId, @RequestBody List<String> groupIds, @RequestBody Map<String, ?> variables);

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
    @ResponseBody
    @PostMapping(value = "/callChildProcess", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    void callChildProcess(@RequestHeader(value="token") String token, @RequestHeader(value="parentKey") String parentKey, @RequestHeader(value="parentDataId") String parentDataId, @RequestHeader(value="childDataId") String childDataId, @RequestHeader(value="userId") String userId, @RequestBody List<String> groupIds, @RequestBody Map<String, ?> variables);

    /**
     * （可忽略） 从平台表中获取流程定义信息
     *
     * @param token
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getProcessDEFInfos", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    List<ProcessDefInfo> getProcessDEFInfos(@RequestHeader(value="token") String token, @RequestBody E3Selector selector);

    /**
     * （可忽略） 从平台表中获取流程定义分页信息
     *
     * @param token
     * @param selector
     * @param pageSize
     * @param pageIndex
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/queryProcessDEFInfoPage", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    List<ProcessDefInfo> queryProcessDEFInfoPage(@RequestHeader(value="token") String token, @RequestBody E3Selector selector, @RequestParam(value="pageSize") int pageSize, @RequestParam(value="pageIndex") int pageIndex);

    /**
     * （可忽略） 根据id，从平台表中获取流程定义信息
     *
     * @param token
     * @param id
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getProcessDefInfoById", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult getProcessDefInfoById(@RequestHeader(value="token") String token, @RequestHeader(value="id") String id);

    /**
     * （可忽略） 根据id，从平台表中获取流程定义组信息
     *
     * @param token
     * @param id
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getProcessDefGroupInfoById", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult getProcessDefGroupInfoById(@RequestHeader(value="token") String token, @RequestHeader(value="id") String id);

    /**
     * （可忽略） 从平台表中获取流程定义组信息
     *
     * @param token
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getProcessDefGroupsInfos", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult getProcessDefGroupsInfos(@RequestHeader(value="token") String token, @RequestBody E3Selector selector, @RequestParam(value="pageSize") int pageSize, @RequestParam(value="pageIndex") int pageIndex);

    /**
     * （可忽略） 保存流程定义组信息到平台表中
     *
     * @param token
     * @param processDefGroup
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/saveProcessDefGroup", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult saveProcessDefGroup(@RequestHeader(value="token") String token, @RequestBody ProcessGroup processDefGroup);

    /**
     * （可忽略） 根据流程组id，流程定义组
     *
     * @param token
     * @param id
     */
    @ResponseBody
    @PostMapping(value = "/deleteProcessDefGroupById", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult deleteProcessDefGroupById(@RequestHeader(value="token") String token, @RequestHeader(value="id") String id);

    /**
     * （可忽略） 根据key，从平台表中获取备份流程定义信息
     *
     * @param token
     * @param key
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getProcessDefInfoCopyByKey", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult getProcessDefInfoCopyByKey(@RequestHeader(value="token") String token, @RequestBody E3Selector selector, @RequestParam(value="pageSize") int pageSize, @RequestParam(value="pageIndex") int pageIndex);

    /**
     * （可忽略） 保存备份流程定义信息到平台表中
     *
     * @param token
     * @param procDefCopy
     */
    @ResponseBody
    @PostMapping(value = "/saveProcessDefInfoCopy", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult saveProcessDefInfoCopy(@RequestHeader(value="token") String token, @RequestBody ProcessDefInfoCopy procDefCopy);

    /**
     * （可忽略） 根据模板key，从平台表中删除对应备份流程定义信息
     *
     * @param key
     */
    @ResponseBody
    @PostMapping(value = "/deleteProcessDefInfoCopyByKey", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult deleteProcessDefInfoCopyByKey(@RequestHeader(value="token") String token, @RequestHeader(value="key") String key);

    /**
     * 根据key，判断是否有对应流程实例
     *
     * @param key
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/hasProcessInstanceByKey", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    boolean hasProcessInstanceByKey(@RequestHeader(value="key") String key);

    /**
     * (OK) 获取流程实例并转换
     *
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getProcessInstance", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    List<ProcessInstanceInfo> getProcessInstance();

    /**
     * （OK） 根据流程procInstanceId，获取流程实例
     *
     * @param procInstanceId
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getProcessInstanceById", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ProcessInstanceInfo getProcessInstanceById(@RequestHeader(value="procInstanceId") String procInstanceId);

    /**
     * （OK） 根据流程procInstanceId，暂停流程实例
     *
     * @param id
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/suspendProcessInstanceById", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    void suspendProcessInstanceById(@RequestHeader(value="id") String id);

    /**
     * （OK） 根据流程procInstanceId，激活流程实例
     *
     * @param id
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/activateProcessInstanceById", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    void activateProcessInstanceById(@RequestHeader(value="id") String id);

    /**
     * （OK） 根据流程procInstanceId，删除流程实例
     *
     * @param id
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/deleteProcessInstanceById", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    void deleteProcessInstanceById(@RequestHeader(value="id") String id);

    /**
     * （可忽略） 获取流程定义集
     *
     * @param token
     * @param mainCondition
     * @param detialConditon
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getProcessDefs", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    List<ProcessDefInfo> getProcessDefs(@RequestHeader(value="token") String token, @RequestBody E3Selector selector, @RequestParam(value="pageSize") int pageSize, @RequestParam(value="pageIndex") int pageIndex);

    /**
     * （可忽略） 保存流程定义明细
     *
     * @param processDefDetail
     */
    @ResponseBody
    @PostMapping(value = "/saveProcessDefDetail", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult saveProcessDefDetail(@RequestHeader(value="token") String token, @RequestBody ProcessDefInfoDetail processDefDetail);

    /**
     * （可忽略） 根据id，从平台表中删除流程定义
     *
     * @param token
     * @param id
     */
    @ResponseBody
    @PostMapping(value = "/deleteProcessDefById", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult deleteProcessDefById(@RequestHeader(value="token") String token, @RequestHeader(value="id") String id);

    /**
     * （可忽略） 启用
     *
     * @param token
     * @param id
     */
    @ResponseBody
    @PostMapping(value = "/enableProcessDefDetailById", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult enableProcessDefDetailById(@RequestHeader(value="token") String token, @RequestHeader(value="id") String id);

    /**
     * （可忽略） 禁用
     *
     * @param token
     * @param id
     */
    @ResponseBody
    @PostMapping(value = "/disableProcessDefDetailById", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult disableProcessDefDetailById(@RequestHeader(value="token") String token, @RequestHeader(value="id") String id);

    /**
     * （可忽略） 设为默认版本
     *
     * @param token
     * @param id
     */
    @ResponseBody
    @PostMapping(value = "/defaultProcessDefDetailById", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult defaultProcessDefDetailById(@RequestHeader(value="token") String token, @RequestHeader(value="id") String id);

    /**
     * （可忽略） 设为默认编辑
     *
     * @param token
     * @param id
     */
    @ResponseBody
    @PostMapping(value = "/editProcessDefDetailById", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult editProcessDefDetailById(@RequestHeader(value="token") String token, @RequestHeader(value="id") String id);

    /**
     * （可忽略） 通过明细Id获取工作流明细
     *
     * @param token
     * @param ids
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getProcessDefInfoDetailById", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    List<ProcessDefInfoDetail> getProcessDefInfoDetailById(@RequestHeader(value="token") String token, @RequestBody String[] ids);

    /**
     * （可忽略） 保存流程定义信息到平台表中
     *
     * @param token
     * @param processDef
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/saveProcessDef", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ProcessDefInfo saveProcessDef(@RequestHeader(value="token") String token, @RequestBody ProcessDefInfo processDef);

    /**
     * （OK） 用户处理任务
     *  @param token
     * @param workFlowHandleActionRequest
     */
    @ResponseBody
    @PostMapping(value = "/handleAction", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    void handleAction(@RequestHeader(value="token") String token, @RequestBody WorkFlowHandleActionRequest workFlowHandleActionRequest);

    /**
     * (OK) 任务认领
     *
     * @param taskInfo
     * @param userId
     */
    @ResponseBody
    @PostMapping(value = "/assignTask", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    void assignTask(@RequestBody TaskInfo taskInfo, @RequestHeader(value="userId") String userId);

    /**
     * (OK) 根据流程Key获取Task信息
     *
     * @param processKey
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getTasksByProcessKey", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    List<TaskInfo> getTasksByProcessKey(@RequestHeader(value="processKey") String processKey);

    /**
     *
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getHistory", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult getHistory();

    /**
     * 根据用户Id查询用户发起的流程
     *
     * @param token
     * @param userId
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getUserProcessInfo", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    List<ProcessInfo> getUserProcessInfo(@RequestHeader(value="token") String token, @RequestBody E3Selector selector, @RequestParam(value="pageSize") int pageSize, @RequestParam(value="pageIndex") int pageIndex);

    /**
     * 获取用户发起的流程
     *
     * @param token
     * @param selector
     * @param pageSize
     * @param pageIndex
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getPageUserProcessInfo", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    Map<String, Object> getPageUserProcessInfo(@RequestHeader(value="token") String token, @RequestBody E3Selector selector, @RequestParam(value="pageSize") int pageSize, @RequestParam(value="pageIndex") int pageIndex);

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
    @ResponseBody
    @PostMapping(value = "/doAction", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult doAction(@RequestHeader(value="token") String token, @RequestHeader(value="key") String key, @RequestHeader(value="dataId") String dataId, @RequestBody Map<String, ?> variables, @RequestParam(value="type") int type);

    /**
     * 查询Activiti历史任务 转换为HistoryTaskInfo
     *
     * @param token
     * @param selector
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/queryHistoryTasks", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    Map<String, Object> queryHistoryTasks(@RequestHeader(value="token") String token, @RequestBody E3Selector selector, @RequestParam(value="pageIndex") int pageIndex, @RequestParam(value="pageSize") int pageSize);


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
    @ResponseBody
    @PostMapping(value = "/queryUserAuthTask", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult queryUserAuthTask(@RequestHeader(value="token") String token, @RequestHeader(value="refModel") String refModel, @RequestHeader(value="metaCode") String metaCode, @RequestParam(value="startDate") Date startDate, @RequestParam(value="endDate") Date endDate);

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
    @ResponseBody
    @PostMapping(value = "/doActionByUserId", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult doActionByUserId(@RequestHeader(value="token") String token, @RequestHeader(value="tuserId") String tuserId, @RequestHeader(value="key") String key, @RequestHeader(value="dataId") String dataId, @RequestBody Map<String, ?> variables, @RequestParam(value="type") int type);

    /**
     * 查询历史任务通过过程Id
     *
     * @param token
     * @param processInfoId
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getHistoryTask", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult getHistoryTask(@RequestHeader(value="token") String token, @RequestHeader(value="processInfoId") String processInfoId, @RequestHeader(value="refModel") String refModel);

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
    @ResponseBody
    @PostMapping(value = "/billOnValidateWorkflow", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    public ServiceResult billOnValidateWorkflow(@RequestHeader(value="token") String token, @RequestHeader(value="metaCode") String metaCode, @RequestHeader(value="billId") String billId, @RequestHeader(value="refModel") String refModel);

    @ResponseBody
    @PostMapping(value = "/getListCount", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    public long getListCount(@RequestHeader(value="token") String token, @RequestBody E3Selector selector);

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
    @ResponseBody
    @PostMapping(value = "/startProcessBySelector", produces = "x-application/hessian2", consumes = "x-application/hessian2")
    ServiceResult startProcess(@RequestHeader(value="token") String token, @RequestBody E3Selector selector, @RequestHeader(value="metaCode") String metaCode, @RequestHeader(value="dataId") String dataId, @RequestHeader(value="code") String code, @RequestHeader(value="displayName") String displayName, @RequestHeader(value="authority") String authority, @RequestHeader(value="refModel") String refModel, @RequestHeader(value="description") String description, @RequestParam(value="channelId") Long channelId, @RequestParam(value="brandId") Long brandId);
}
