package cn.cy.activitydemo.controller;


import org.activiti.engine.*;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class ActivityController {


    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;

    //部署流程
    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment =
                repositoryService.createDeployment()
                        .addClasspathResource("processes/test1.xml").name("test1").deploy();
        System.out.println("流程部署的id：" + deployment.getId());
        System.out.println("流程部署的name：" +deployment.getName());
    }

    @RequestMapping(value = "/startDemo",method = {RequestMethod.GET,RequestMethod.POST})
    public void startDemo(){
        //查询部署数量
        long count = repositoryService.createDeploymentQuery().count();
        System.out.println("流程数量:"+count);
        Map<String,Object> map = new HashMap<>();
        map.put("apply","zs");
        map.put("approve","ls");

        //启动流程
        ExecutionEntity pi1 = (ExecutionEntity) runtimeService.startProcessInstanceById("");

        List<Task> tq=taskService.createTaskQuery().taskAssignee("zs").list();
        System.out.println(tq.size());
        String assignee = "zs";//当前任务办理人
        List<Task> tasks = taskService//与任务相关的Service
                .createTaskQuery()//创建一个任务查询对象
                .taskAssignee(assignee)
                .list();
        if(tasks !=null && tasks.size()>0){
            for(Task task:tasks){
                System.out.println("任务ID:"+task.getId());
                System.out.println("任务的办理人:"+task.getAssignee());
                System.out.println("任务名称:"+task.getName());
                System.out.println("任务的创建时间:"+task.getCreateTime());
                System.out.println("流程实例ID:"+task.getProcessInstanceId());
            }
        }
    }


}
