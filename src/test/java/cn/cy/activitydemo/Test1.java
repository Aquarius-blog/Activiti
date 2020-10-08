package cn.cy.activitydemo;

import org.activiti.engine.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class Test1{

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;
    @Test
    public void test01(){
        //创建一个流程成引擎对像
        ProcessEngineConfiguration conf = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
        //设置数据源
        conf.setJdbcDriver("com.mysql.jdbc.Driver");
        conf.setJdbcUrl("jdbc:mysql://localhost:3306/activity-demo");
        conf.setJdbcUsername("root");
        conf.setJdbcPassword("admin");
        //设置自动创建表
        conf.setDatabaseSchemaUpdate("true");
        //在创建引擎对象的时候自动创建表
        ProcessEngine processEngine = conf.buildProcessEngine();
    }

    @Test
    public void test02(){
        long count = repositoryService.createDeploymentQuery().count();
        System.out.println("流程数量:"+count);
    }


}
