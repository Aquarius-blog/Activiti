package cn.cy.activitydemo;

import cn.cy.activitydemo.controller.Activity3Controller;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = ActivityDemoApplication.class)
public class Test2 {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private HistoryService historyService;

    @Test
    public void test01(){
        Logger logger = LoggerFactory.getLogger(Activity3Controller.class);
        logger.info("开始部署流程");
        //部署流程
        //repositoryService.createDeployment().addClasspathResource("processes.test3.xml").deploy();
        //创建流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //得到流程服务实例
        RepositoryService repositoryService = processEngine.getRepositoryService();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //部署流程描述文件
        Deployment dp = repositoryService.createDeployment()
                .addClasspathResource("processes/test4.bpmn").name("test4").deploy();
        System.out.println("流程部署的id：" + dp.getId());
        System.out.println("流程部署的name：" +dp.getName());
        //查找流程定义
        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
       // Deployment deployment = deploymentQuery.deploymentId(deploy.getId()).singleResult();
        List<Deployment> deploymentList = deploymentQuery.orderByDeploymenTime().asc().listPage(0, 100);
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

    @Test
    public void test11(){
        String a ="1";
        System.out.println(a);

    }
}
