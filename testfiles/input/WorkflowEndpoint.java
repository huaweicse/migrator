package com.baison.e3.middleware.stock.service.workflow;

import com.alibaba.boot.hsf.annotation.HSFProvider;
import com.baison.e3.middleware.workflow.model.*;
import com.baison.e3.middleware.workflow.service.IWorkflowService;
import com.baison.e3.middleware.workflow.service.WorkFlowHandleActionRequest;

import e3.middleware.query.E3Selector;
import e3.middleware.result.model.ServiceResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

@HSFProvider(serviceInterface = IWorkflowService.class)
public class WorkflowEndpoint implements IWorkflowService {

	@Autowired
	private IWorkflowService workflowService;

	@Override
	public void deployProcess(String key, String processDefXML) {
		workflowService.deployProcess(key, processDefXML);
	}

	@Override
	public void unDeployProcess(String key) {
		workflowService.unDeployProcess(key);
	}

	@Override
	public List<ProcessInfo> getDeployedProcInfos(String userId, List<String> groupIds, boolean isAdmin) {
		return workflowService.getDeployedProcInfos(userId, groupIds, isAdmin);
	}

	@Override
	@ApiOperation(value = "", hidden = true)
	// TODO：抽象类型参数
	public Map<String, ?> getProcFormVariablesByKey(String key) {
		return workflowService.getProcFormVariablesByKey(key);
	}

	@Override
	public String getProcessKey(String processDefinitionName) {
		return workflowService.getProcessKey(processDefinitionName);
	}

	@Override
	public String startProcess(String token, String key, String userId, String dataId, String processTemplateId) {
		return workflowService.startProcess(token, key, userId, dataId, processTemplateId);
	}

	@Override
	@ApiOperation(value = "", hidden = true)
	// TODO：抽象类型参数
	public void saveProcess(String token, String key, String dataId, String userId, List<String> groupIds,
			String processTemplateId, Map<String, ?> variables) throws Exception {
		workflowService.saveProcess(token, key, dataId, userId, groupIds, processTemplateId, variables);
	}

	@Override
	public List<TaskInfo> getTasks(String token, String userId, List<String> groupIds, boolean isAdmin) {
		return workflowService.getTasks(token, userId, groupIds, isAdmin);
	}

	@Override
	public List<TaskInfo> getUserModelTask(String token, String refModel) {
		return workflowService.getUserModelTask(token, refModel);
	}

	@Override
	public List<TaskInfo> getUserTask(String token, String billId, String refModel) {
		return workflowService.getUserTask(token, billId, refModel);
	}

	@Override
	public Map<String, Object> getPageUserTasks(String token, int pageSize, int pageIndex, String refModel) {
		return workflowService.getPageUserTasks(token, pageSize, pageIndex, refModel);
	}

	@Override
	public TaskInfo getTaskById(String id) {
		return workflowService.getTaskById(id);
	}

	@Override
	public TaskInfo queryTaskById(String token, String id) {
		return workflowService.queryTaskById(token, id);
	}

	@Override
	public ProcessInfo getProcInfoByKey(String key) {
		return workflowService.getProcInfoByKey(key);
	}

	@Override
	public ProcessInfo getProcInfoById(String id) {
		return workflowService.getProcInfoById(id);
	}

	@Override
	@ApiOperation(value = "", hidden = true)
	// TODO：抽象类型参数
	public void submitProcess(String token, ServiceResult result, String key, String dataId, String userId,
			List<String> groupIds, Map<String, ?> variables) throws Exception {
		workflowService.submitProcess(token, result, key, dataId, userId, groupIds, variables);
	}

	@Override
	@ApiOperation(value = "", hidden = true)
	// TODO：抽象类型参数
	public void giveupProcess(String token, ServiceResult result, String key, String dataId, String userId,
			List<String> groupIds, Map<String, ?> variables) throws Exception {
		workflowService.giveupProcess(token, result, key, dataId, userId, groupIds, variables);
	}

	@Override
	@ApiOperation(value = "", hidden = true)
	// TODO：抽象类型参数
	public void approveProcess(String token, ServiceResult result, String key, String dataId, String userId,
			List<String> groupIds, Map<String, ?> variables) throws Exception {
		workflowService.approveProcess(token, result, key, dataId, userId, groupIds, variables);
	}

	@Override
	@ApiOperation(value = "", hidden = true)
	// TODO：抽象类型参数
	public void refuseProcess(String token, ServiceResult result, String key, String dataId, String userId,
			List<String> groupIds, Map<String, ?> variables) throws Exception {
		workflowService.refuseProcess(token, result, key, dataId, userId, groupIds, variables);
	}

	@Override
	@ApiOperation(value = "", hidden = true)
	// TODO：抽象类型参数
	public void callChildProcess(String token, String parentKey, String parentDataId, String childDataId, String userId,
			List<String> groupIds, Map<String, ?> variables) throws Exception {
		workflowService.callChildProcess(token, parentKey, parentDataId, childDataId, userId, groupIds, variables);
	}

	@Override
	public List<ProcessDefInfo> getProcessDEFInfos(String token, E3Selector selector) {
		return workflowService.getProcessDEFInfos(token, selector);
	}

	@Override
	public List<ProcessDefInfo> queryProcessDEFInfoPage(String token, E3Selector selector, int pageSize,
			int pageIndex) {
		return workflowService.queryProcessDEFInfoPage(token, selector, pageSize, pageIndex);
	}

	@Override
	public ServiceResult getProcessDefInfoById(String token, String id) {
		return workflowService.deleteProcessDefById(token, id);
	}

	@Override
	public ServiceResult getProcessDefGroupInfoById(String token, String id) {
		return workflowService.getProcessDefGroupInfoById(token, id);
	}

	@Override
	public ServiceResult getProcessDefGroupsInfos(String token, E3Selector selector, int pageSize, int pageIndex) {
		return workflowService.getProcessDefGroupsInfos(token, selector, pageSize, pageIndex);
	}

	@Override
	public ServiceResult saveProcessDefGroup(String token, ProcessGroup processDefGroup) {
		return workflowService.saveProcessDefGroup(token, processDefGroup);
	}

	@Override
	public ServiceResult deleteProcessDefGroupById(String token, String id) {
		return workflowService.deleteProcessDefGroupById(token, id);
	}

	@Override
	public ServiceResult getProcessDefInfoCopyByKey(String token, E3Selector selector, int pageSize, int pageIndex) {
		return workflowService.getProcessDefInfoCopyByKey(token, selector, pageSize, pageIndex);
	}

	@Override
	public ServiceResult saveProcessDefInfoCopy(String token, ProcessDefInfoCopy procDefCopy) {
		return workflowService.saveProcessDefInfoCopy(token, procDefCopy);
	}

	@Override
	public ServiceResult deleteProcessDefInfoCopyByKey(String token, String key) {
		return workflowService.deleteProcessDefInfoCopyByKey(token, key);
	}

	@Override
	public boolean hasProcessInstanceByKey(String key) {
		return workflowService.hasProcessInstanceByKey(key);
	}

	@Override
	public List<ProcessInstanceInfo> getProcessInstance() {
		return workflowService.getProcessInstance();
	}

	@Override
	public ProcessInstanceInfo getProcessInstanceById(String procInstanceId) {
		return workflowService.getProcessInstanceById(procInstanceId);
	}

	@Override
	public void suspendProcessInstanceById(String id) {
		workflowService.suspendProcessInstanceById(id);
	}

	@Override
	public void activateProcessInstanceById(String id) {
		workflowService.activateProcessInstanceById(id);
	}

	@Override
	public void deleteProcessInstanceById(String id) {
		workflowService.deleteProcessInstanceById(id);
	}

	@Override
	public List<ProcessDefInfo> getProcessDefs(String token, E3Selector selector, int pageSize, int pageIndex) {
		return workflowService.getProcessDefs(token, selector, pageSize, pageIndex);
	}

	@Override
	public ServiceResult saveProcessDefDetail(String token, ProcessDefInfoDetail processDefDetail) {
		return workflowService.saveProcessDefDetail(token, processDefDetail);
	}

	@Override
	public ServiceResult deleteProcessDefById(String token, String id) {
		return workflowService.deleteProcessDefById(token, id);
	}

	@Override
	public ServiceResult enableProcessDefDetailById(String token, String id) {
		return workflowService.enableProcessDefDetailById(token, id);
	}

	@Override
	public ServiceResult disableProcessDefDetailById(String token, String id) {
		return workflowService.disableProcessDefDetailById(token, id);
	}

	@Override
	public ServiceResult defaultProcessDefDetailById(String token, String id) {
		return workflowService.defaultProcessDefDetailById(token, id);
	}

	@Override
	public ServiceResult editProcessDefDetailById(String token, String id) {
		return workflowService.editProcessDefDetailById(token, id);
	}

	@Override
	public List<ProcessDefInfoDetail> getProcessDefInfoDetailById(String token, String[] ids) {
		return workflowService.getProcessDefInfoDetailById(token, ids);
	}

	@Override
	public ProcessDefInfo saveProcessDef(String token, ProcessDefInfo processDef) {
		return workflowService.saveProcessDef(token, processDef);
	}

	@Override
	public void handleAction(String token,
      WorkFlowHandleActionRequest workFlowHandleActionRequest) {
		workflowService.handleAction(token,
        new WorkFlowHandleActionRequest(workFlowHandleActionRequest.getTaskInfo(),
            workFlowHandleActionRequest.getDataId(), workFlowHandleActionRequest.getAction(),
            workFlowHandleActionRequest.getDescritpion(), workFlowHandleActionRequest.getVars(),
            workFlowHandleActionRequest.getTargetMetaDataId(), workFlowHandleActionRequest.getTargetDataId(),
            workFlowHandleActionRequest.getTargetCode(), workFlowHandleActionRequest.getTargetDisplayName()));
	}

	@Override
	public void assignTask(TaskInfo taskInfo, String userId) {
		workflowService.assignTask(taskInfo, userId);
	}

	@Override
	public List<TaskInfo> getTasksByProcessKey(String processKey) {
		return workflowService.getTasksByProcessKey(processKey);
	}

	@Override
	public ServiceResult getHistory() {
		return workflowService.getHistory();
	}

	@Override
	public List<ProcessInfo> getUserProcessInfo(String token, E3Selector selector, int pageSize, int pageIndex) {
		return workflowService.getUserProcessInfo(token, selector, pageSize, pageIndex);
	}

	@Override
	public Map<String, Object> getPageUserProcessInfo(String token, E3Selector selector, int pageSize, int pageIndex) {
		return workflowService.getPageUserProcessInfo(token, selector, pageSize, pageIndex);
	}

	@Override
	@ApiOperation(value = "", hidden = true)
	// TODO：抽象类型参数
	public ServiceResult doAction(String token, String key, String dataId, Map<String, ?> variables, int type) {
		return workflowService.doAction(token, key, dataId, variables, type);
	}

	@Override
	public Map<String, Object> queryHistoryTasks(String token, E3Selector selector, int pageIndex, int pageSize) {
		return workflowService.queryHistoryTasks(token, selector, pageIndex, pageSize);
	}

	@Override
	public ServiceResult queryUserAuthTask(String token, String refModel, String metaCode, Date startDate,
			Date endDate) {
		return workflowService.queryUserAuthTask(token, refModel, metaCode, startDate, endDate);
	}

	@Override
	@ApiOperation(value = "", hidden = true)
	// TODO：抽象类型参数
	public ServiceResult doActionByUserId(String token, String tuserId, String key, String dataId,
			Map<String, ?> variables, int type) {
		return workflowService.doActionByUserId(token, tuserId, key, dataId, variables, type);
	}

	@Override
	public ServiceResult getHistoryTask(String token, String processInfoId, String refModel) {
		return workflowService.getHistoryTask(token, processInfoId, refModel);
	}

	@Override
	public ServiceResult billOnValidateWorkflow(String token, String metaCode, String billId, String refModel) {
		return workflowService.billOnValidateWorkflow(token, metaCode, billId, refModel);
	}

	@Override
	public long getListCount(String token, E3Selector selector) {
		return workflowService.getListCount(token, selector);
	}

	@Override
	@ApiOperation(value = "startProcessWithBrandId", nickname = "startProcessWithBrandId")
	public ServiceResult startProcess(String token, E3Selector selector, String metaCode, String dataId, String code,
			String displayName, String authority, String refModel, String description, Long channelId, Long brandId) {
		return workflowService.startProcess(token, selector, metaCode, dataId, code, displayName, authority, refModel,
				description, channelId, brandId);
	}

}
