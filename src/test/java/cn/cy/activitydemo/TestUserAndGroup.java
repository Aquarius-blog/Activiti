package cn.cy.activitydemo;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = ActivityDemoApplication.class)
public class TestUserAndGroup {

    @Autowired
    private IdentityService identityService;
    /*创建流程引擎*/
    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    //创建Activiti用户
    @Test
    public void addUser( ){
        //项目中每创建一个新用户，对应的要创建一个Activiti用户,两者的userId和userName一致
        //添加用户
        User user1 = identityService.newUser("zhangsan");
        user1.setFirstName("张三");
        user1.setLastName("张");
        user1.setPassword("123456");
        user1.setEmail("zhangsan@qq.com");
        identityService.saveUser(user1);

        User user2 = identityService.newUser("lisi");
        user2.setFirstName("李四");
        user2.setLastName("李");
        user2.setPassword("123456");
        user2.setEmail("lisi@qq.com");
        identityService.saveUser(user2);

        User user3 = identityService.newUser("wangwu");
        user3.setFirstName("王五");
        user3.setLastName("王");
        user3.setPassword("123456");
        user3.setEmail("wangwu@qq.com");
        identityService.saveUser(user3);

        User user4 = identityService.newUser("wuliu");
        user4.setFirstName("吴六");
        user4.setLastName("吴");
        user4.setPassword("123456");
        user4.setEmail("wuliu@qq.com");
        identityService.saveUser(user4);


    }

    //查询用户
    @Test
    public void queryUser( ){
        User user = identityService.createUserQuery().userId("user1").singleResult();
        System.out.println(user.getId());
        System.out.println(user.getFirstName());
        System.out.println(user.getLastName());
        System.out.println(user.getPassword());
        System.out.println(user.getEmail());

    }

    //创建Activiti用户组
    @Test
    public void addGroup( ){
        Group group1 = identityService.newGroup("group1");
        group1.setName("员工组");
        group1.setType("员工组");
        identityService.saveGroup(group1);

        Group group2 = identityService.newGroup("group2");
        group2.setName("总监组");
        group2.setType("总监阻");
        identityService.saveGroup(group2);

        Group group3 = identityService.newGroup("group3");
        group3.setName("经理组");
        group3.setType("经理组");
        identityService.saveGroup(group3);

        Group group4 = identityService.newGroup("group4");
        group4.setName("人力资源组");
        group4.setType("人力资源组");
        identityService.saveGroup(group4);

    }

    //5、通过用户组id查询Activiti用户组
    @Test
    public void queryGroup( ){
        Group group = identityService.createGroupQuery().groupId("group1").singleResult();
        System.out.println(group.getId());
        System.out.println(group.getName());
        System.out.println(group.getType());
    }

    //6、创建Activiti（用户-用户组）关系ACT_ID_MEMBERSHIP
    @Test
    public void addMembership( ){

        identityService.createMembership("zhangsan", "group1");//user1 在员工阻
        identityService.createMembership("lisi", "group2");//user2在总监组
        identityService.createMembership("wangwu", "group3");//user3在经理组
        identityService.createMembership("wuliu", "group4");//user4在人力资源组

    }

    //7、查询属于组group1的用户
    @Test
    public void queryUserListByGroup( ){

        //查询属于组group1的用户
        List<User> usersInGroup = identityService.createUserQuery().memberOfGroup("group1").list();
        for (User user : usersInGroup) {
            System.out.println(user.getFirstName());
        }
    }

    //8、查询user1所属于的组
    @Test
    public void queryGroupListByUser( ){

        //查询user1所属于的组
        List<Group> groupsForUser = identityService.createGroupQuery().groupMember("user1").list();
        for (Group group : groupsForUser) {
            System.out.println(group.getName());
        }

    }
}
