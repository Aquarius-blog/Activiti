package cn.cy.activitydemo;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
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
public class TestAudit {

    @Autowired
    private IdentityService identityService;
    /*创建流程引擎*/
    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    //部署流程
    @Test
    public void deploy( ){
        //部署流程描述文件
        Deployment deployment = processEngine.getRepositoryService()//获取流程定义和部署对象相关的Service
                .createDeployment()//创建部署对象
                .name("产品审核流程")//声明流程的名称
                .addClasspathResource("processes/audit.bpmn")//加载资源文件，一次只能加载一个文件
                .deploy();//完成部署
        System.out.println("部署ID："+deployment.getId());//1
        System.out.println("部署时间："+deployment.getDeploymentTime());

    }




    /*
     * 启动流程
     * 两种启动方式1 通过key  2 通过id
     * 我们通过key的方式启动 key为act_re_procdef表中的key字段值
     * */
    @Test
    public void startTask() {
        try {
            /*定义参数*/
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("productUsers", "zhangsan,lisi");//为candidateUsers的参数productUsers 指定值，用英文逗号分隔
            ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceByKey("myProcess_1",variables);
            System.out.println("流程实例ID:" + pi.getId());
            System.out.println("流程定义ID:" + pi.getProcessDefinitionId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //根据用户查询待办任务
    @Test
    public void queryTask() {
        String candidateUser = "zhangsan";
        List<Task> list = processEngine.getTaskService()// 与正在执行的任务管理相关的Service
                .createTaskQuery()// 创建任务查询对象
                /** 查询条件（where部分） */
                .taskCandidateUser(candidateUser)// 组任务的办理人查询，不是组成员，查询不到
                /** 排序 */
                .orderByTaskCreateTime().asc()// 使用创建时间的升序排列
                /** 返回结果集 */
                .list();
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
    /** 组代办任务认领*/
    @Test
    public void claim() {
        //任务ID
        String taskId = "7504";
        //认领人
        String userId = "zhangsan";
        processEngine.getTaskService().claim(taskId, userId);
    }

    /** 解除任务认领*/
    @Test
    public void setAssignee() {
        //任务ID
        String taskId = "5006";
        processEngine.getTaskService().setAssignee(taskId, null);

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

    /*
     * 根据用户组名查询
     * */
    @Test
    public void test6() {
        //组代办
        List<Task> list = processEngine.getTaskService().createTaskQuery().taskCandidateGroup("requireGroup").list();
        for (Task task : list) {
            System.out.println("任务ID:" + task.getId());
            System.out.println("任务名称:" + task.getName());
            System.out.println("任务创建时间:" + task.getCreateTime());
            System.out.println("任务委派人:" + task.getAssignee());
            System.out.println("流程实例ID:" + task.getProcessInstanceId());
            System.out.println("########################################################");
        }
    }
}
