package cn.cy.activitydemo;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ActivityDemoApplication.class)
public class TestMyProcess {

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

    //部署流程流程定义1：act_re_procdef 所有流程：act_re_deployment 文件保存表：act_ge_bytearray
    @Test
    public void deploy(){
        //部署流程描述文件
        Deployment deployment = processEngine.getRepositoryService()//获取流程定义和部署对象相关的Service
                .createDeployment()//创建部署对象
                .name("转办流程")//声明流程的名称
                .addClasspathResource("processes/MyProcess.bpmn")//加载资源文件，一次只能加载一个文件
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
            ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceByKey("myProcess_1",variables);
            System.out.println("流程实例ID:" + pi.getId());
            System.out.println("流程定义ID:" + pi.getProcessDefinitionId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
