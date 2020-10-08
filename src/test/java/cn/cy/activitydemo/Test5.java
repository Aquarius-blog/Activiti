package cn.cy.activitydemo;


import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = ActivityDemoApplication.class)
public class Test5 {

    @Autowired
    private IdentityService identityService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RuntimeService runtimeService;

    /*创建流程引擎*/
    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    @Test
    public void deploy(){
        //部署流程描述文件
        Deployment deployment = processEngine.getRepositoryService()//获取流程定义和部署对象相关的Service
                .createDeployment()//创建部署对象
                .name("请假审核流程")//声明流程的名称
                .addClasspathResource("processes/test5.bpmn")//加载资源文件，一次只能加载一个文件
                .deploy();//完成部署
        System.out.println("部署ID："+deployment.getId());//1
        System.out.println("部署时间："+deployment.getDeploymentTime());
    }
    //启动流程 act_ru_task:启动流程需要给第一个节点指定候选人
    @Test
    public void startTask() {
        try {
            /*定义参数*/
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("productUsers", "zhangsan");//为candidateUsers的参数productUsers 指定值，用英文逗号分隔
            //使用流程定义的key启动流程实例，key对应leave.bpmn文件中id的属性
            ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceByKey("myProcess_test",variables);
            System.out.println("流程实例ID:" + pi.getId());
            System.out.println("流程定义ID:" + pi.getProcessDefinitionId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /** 组代办任务认领*/
    @Test
    public void claim() {
        //任务ID
        String taskId = "7504";
        //认领人
        String userId = "zhangsan";
        processEngine.getTaskService().claim(taskId, userId);
    }
    /** 查询当前人的个人任务 */
    @Test
    public void test4() {
        String assignee = "zhangsan";
        List<Task> list = processEngine.getTaskService()// 与正在执行的任务管理相关的Service
                .createTaskQuery()//创建任务查询对象
                /** 查询条件（where部分） */
                .taskAssignee(assignee)// 指定个人任务查询，指定办理人
                /** 排序 */
                .orderByTaskCreateTime().asc()// 使用创建时间的升序排列
                /** 返回结果集 */
                .list();// 返回列表
        if (list != null && list.size() > 0) {
            for (Task task : list) {
                System.out.println("任务ID:" + task.getId());
                System.out.println("任务名称:" + task.getName());
                System.out.println("任务的创建时间:" + task.getCreateTime());
                System.out.println("任务的办理人:" + task.getAssignee());
                System.out.println("流程实例ID：" + task.getProcessInstanceId());
                System.out.println("执行对象ID:" + task.getExecutionId());
                System.out.println("流程定义ID:" + task.getProcessDefinitionId());
                System.out.println("########################################################");
            }
        }
    }
    /**
     * 完成任务
     */
    @Test
    public void test5() {
        TaskService taskService = processEngine.getTaskService();
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("requireGroup", "requireGroup");//用户组，requireGroup
        //添加完成任务信息，可以不要
        //taskId,processInstance,message
        taskService.addComment("7504", "5001", "产品审核同意--zhangsan");
        //完成任务方法
        taskService.complete("7504", variables);
    }

}
