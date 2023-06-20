package com.didiglobal.turbo.demo.controller;

import com.didiglobal.turbo.demo.pojo.request.CreateFlowRequest;
import com.didiglobal.turbo.demo.pojo.response.BaseResponse;
import com.didiglobal.turbo.demo.pojo.response.CreateFlowResponse;
import com.didiglobal.turbo.demo.service.AfterSaleServiceImpl;
import com.didiglobal.turbo.engine.engine.ProcessEngine;
import com.didiglobal.turbo.engine.model.InstanceData;
import com.didiglobal.turbo.engine.param.CommitTaskParam;
import com.didiglobal.turbo.engine.param.RollbackTaskParam;
import com.didiglobal.turbo.engine.param.StartProcessParam;
import com.didiglobal.turbo.engine.result.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/work")
public class WorkController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkController.class);

    @Resource
    private ProcessEngine processEngine;


    /**
     * 开启流程
     * @param startProcessParam 开启流程参数
     * @return
     */
    @RequestMapping(value = "/startFlow", method = {RequestMethod.POST})
    public BaseResponse<CommitTaskResult> createFlow(@RequestBody StartProcessParam startProcessParam) {
        startProcessParam.setFlowDeployId(startProcessParam.getFlowDeployId());
        List<InstanceData> variables = startProcessParam.getVariables();
        variables.add(new InstanceData("user_id", "userId"));
        startProcessParam.setVariables(variables);
        StartProcessResult startProcessResult = processEngine.startProcess(startProcessParam);

        CommitTaskParam commitTaskParam = new CommitTaskParam();
        commitTaskParam.setFlowInstanceId(startProcessResult.getFlowInstanceId());
        commitTaskParam.setTaskInstanceId(startProcessResult.getActiveTaskInstance().getNodeInstanceId());

        commitTaskParam.setVariables(startProcessParam.getVariables());

        CommitTaskResult commitTaskResult = processEngine.commitTask(commitTaskParam);
        BaseResponse<CommitTaskResult> baseResponse =  BaseResponse.make(commitTaskResult);
        return baseResponse;
    }

    /**
     * 提交流程
     * @param commitTaskParam 提交任务参数
     * @return
     */
    @RequestMapping(value = "/commitFlow", method = {RequestMethod.POST})
    public BaseResponse<CommitTaskResult> commitFlow(@RequestBody CommitTaskParam commitTaskParam) {
        CommitTaskResult commitTaskResult = processEngine.commitTask(commitTaskParam);
        BaseResponse<CommitTaskResult> baseResponse =  BaseResponse.make(commitTaskResult);
        return baseResponse;
    }

    /**
     * 回退
     * @param rollbackTaskParam 回滚任务参数
     * @return
     */
    @RequestMapping(value = "/rollback", method = {RequestMethod.POST})
    public BaseResponse<RollbackTaskResult> rollback(@RequestBody RollbackTaskParam rollbackTaskParam) {
        RollbackTaskResult rollbackTaskResult = processEngine.rollbackTask(rollbackTaskParam);
        BaseResponse<RollbackTaskResult> baseResponse =  BaseResponse.make(rollbackTaskResult);
        return baseResponse;
    }

    /**
     * 获取历史节点处理用户
     * @param commitTaskParam
     * @return
     */
    @RequestMapping(value = "/getHistoryUserTaskList", method = {RequestMethod.POST})
    public BaseResponse<NodeInstanceListResult> getHistoryUserTaskList(@RequestBody CommitTaskParam commitTaskParam) {
        NodeInstanceListResult historyUserTaskList = processEngine.getHistoryUserTaskList(commitTaskParam.getFlowInstanceId());
        BaseResponse<NodeInstanceListResult> baseResponse =  BaseResponse.make(historyUserTaskList);
        return baseResponse;
    }

    /**
     * 获取流程全部节点提交的参数
     * @param commitTaskParam
     * @return
     */
    @RequestMapping(value = "/getInstanceData", method = {RequestMethod.POST})
    public BaseResponse<InstanceDataListResult> getInstanceData(@RequestBody CommitTaskParam commitTaskParam) {
        InstanceDataListResult instanceData = processEngine.getInstanceData(commitTaskParam.getFlowInstanceId());
        BaseResponse<InstanceDataListResult> baseResponse =  BaseResponse.make(instanceData);
        return baseResponse;
    }















}
