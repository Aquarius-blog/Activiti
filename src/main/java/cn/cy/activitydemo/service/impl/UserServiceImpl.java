package cn.cy.activitydemo.service.impl;

import cn.cy.activitydemo.dao.IUserDao;
import cn.cy.activitydemo.entity.TUserEntity;
import cn.cy.activitydemo.service.IUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<IUserDao, TUserEntity> implements IUserService {
    @Override
    public void addUser(Map<String, Object> map) {
        baseMapper.addUser(map);
    }
}
