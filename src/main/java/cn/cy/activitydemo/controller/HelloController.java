//package cn.cy.activitydemo.controller;
//
//import cn.cy.activitydemo.service.IUserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Controller
//@RequestMapping("/")
//public class HelloController {
//
//    @Autowired
//    private IUserService userService;
//
//    @RequestMapping("index")
//    public String index(){
//        return "index";
//    }
//
//    @RequestMapping("addUser")
//    public void addUser(){
//        Map<String,Object> map = new HashMap<>();
//        map.put("listId","121221");
//        userService.addUser(map);
//    }
//
//}
