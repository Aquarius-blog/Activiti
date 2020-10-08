package cn.cy.activitydemo.service;

import cn.cy.activitydemo.entity.TUserEntity;
import com.baomidou.mybatisplus.service.IService;

import java.util.Map;

public interface IUserService extends IService<TUserEntity> {
    void addUser(Map<String,Object> map);
}
