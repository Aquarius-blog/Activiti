package cn.cy.activitydemo.controller;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/test1")
public class Activity3Controller {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private HistoryService historyService;

    Logger logger = LoggerFactory.getLogger(Activity3Controller.class);
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    @RequestMapping("/startDemo")
    public void startDemo(){
        logger.info("启动startDemo");
            //部署流程
        //repositoryService.createDeployment().addClasspathResource("processes.test3.xml").deploy();
        //创建流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //得到流程服务实例
        RepositoryService repositoryService = processEngine.getRepositoryService();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //部署流程描述文件
        Deployment dp = repositoryService.createDeployment()
                .addClasspathResource("processes/test3.xml").name("test3").deploy();
            System.out.println("流程部署的id：" + dp.getId());
            System.out.println("流程部署的name：" +dp.getName());
        //查找流程定义
        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
        //Deployment deployment = deploymentQuery.deploymentId(deploy.getId()).singleResult();
        List<Deployment> deploymentList = deploymentQuery.orderByDeploymenTime().asc().listPage(0, 100);
            for(Deployment deployment : deploymentList){
        }
        List<ProcessDefinition> definitionList = repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionKey().asc().listPage(0, 100);
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                .deploymentId(dp.getId()).list();
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                .deploymentId(dp.getId()).singleResult();
        //获取查询器
            for (int i=0;i<list.size();i++){
            ProcessDefinition processDefinition = list.get(i);
            System.out.println("流程定义id：" + processDefinition.getId());
            System.out.println("流程定义名称：" + processDefinition.getName());
            System.out.println("流程定义key：" + processDefinition.getKey());
            System.out.println("流程定义版本：" + processDefinition.getVersion());
            System.out.println("流程部署id：" + processDefinition.getDeploymentId());
        }
        //启动流程
        ProcessInstance pi = runtimeService.startProcessInstanceById(pd.getId());
        long count = runtimeService.createProcessInstanceQuery().count();
            System.out.println("启动流程数量"+count);

        // 启动流程实例
        //String procId = runtimeService.startProcessInstanceByKey("financialReport").getId();
        // ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceById(deployment.getId());
        //repositoryService.createDeploymentQuery().deploymentId();

            this.runtimeService.startProcessInstanceById(pi.getId());
        // 获得第一个任务
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("sales").list();
            for (Task task : tasks) {
            System.out.println("Following task is available for sales group: " + task.getName());
            // 认领任务
            taskService.claim(task.getId(), "fozzie");
        }
        tasks = taskService.createTaskQuery().taskAssignee("fozzie").list();
            for (Task task : tasks) {
            System.out.println("Task for fozzie: " + task.getName());
            // 执行(完成)任务
            taskService.complete(task.getId());
        }
        // 现在fozzie的可执行任务数就为0了
            System.out.println("Number of tasks for fozzie: " + taskService.createTaskQuery().taskAssignee("fozzie").count());
        // 获得第二个任务
        tasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
            for (Task task : tasks) {
            System.out.println("Following task is available for accountancy group:" + task.getName());
            taskService.claim(task.getId(), "kermit");
        }
        // 完成第二个任务结束流程
            for (Task task : tasks) {
            taskService.complete(task.getId());
        }
        // 核实流程是否结束,输出流程结束时间
    //        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
    //                .processInstanceId(procId).singleResult();
    //        System.out.println("Process instance end time: " + historicProcessInstance.getEndTime());

    }

    //查看流程图
    @RequestMapping(value = "/image", method = RequestMethod.GET)
    public void image(HttpServletResponse response,@RequestParam String processInstanceId) {
        try {
            InputStream is = getDiagram(processInstanceId);
            if (is == null){
                return;
            }
            response.setContentType("image/png");
            BufferedImage image = ImageIO.read(is);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "png", out);
            is.close();
            out.close();
        } catch (Exception e) {
            logger.error("查看流程图失败", e.getMessage());
        }
    }


    public InputStream getDiagram(String processInstanceId) {
        //获得流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        String processDefinitionId = StringUtils.EMPTY;
        if (processInstance == null) {
            //查询已经结束的流程实例
            HistoricProcessInstance processInstanceHistory =
                    historyService.createHistoricProcessInstanceQuery()
                            .processInstanceId(processInstanceId).singleResult();
            if (processInstanceHistory == null){
                return null;
            }else{
                processDefinitionId = processInstanceHistory.getProcessDefinitionId();
            }
        } else {
            processDefinitionId = processInstance.getProcessDefinitionId();
        }

        //使用宋体
        String fontName = "宋体";
        //获取BPMN模型对象
        BpmnModel model = repositoryService.getBpmnModel(processDefinitionId);
        //获取流程实例当前的节点，需要高亮显示
        List<String> currentActs = Collections.EMPTY_LIST;
        if (processInstance != null)
            currentActs = runtimeService.getActiveActivityIds(processInstance.getId());
        return processEngine.getProcessEngineConfiguration()
                .getProcessDiagramGenerator()
                .generateDiagram(model, "png", currentActs, new ArrayList<String>(),
                        fontName, fontName, fontName, null, 1.0);
    }

}
