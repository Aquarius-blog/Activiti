package cn.cy.activitydemo.controller;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;

public class ActivityCreateTable {

    public static void main(String[] args) {
        ProcessEngineConfiguration configration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();
        ProcessEngine engine = configration.buildProcessEngine();
        System.out.println("初始化流引擎对象成功"+engine.getName());
    }
}
