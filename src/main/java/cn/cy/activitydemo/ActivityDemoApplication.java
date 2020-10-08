package cn.cy.activitydemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude={
		org.activiti.spring.boot.SecurityAutoConfiguration.class,
		SecurityAutoConfiguration.class
})
@MapperScan(basePackages = {"cn.cy.*.dao"})
public class ActivityDemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(ActivityDemoApplication.class, args);
	}

}
