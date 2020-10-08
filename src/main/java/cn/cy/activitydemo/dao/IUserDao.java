package cn.cy.activitydemo.dao;

import cn.cy.activitydemo.entity.TUserEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.Map;


public interface IUserDao extends BaseMapper<TUserEntity> {
        void addUser(Map<String, Object> params);
}
