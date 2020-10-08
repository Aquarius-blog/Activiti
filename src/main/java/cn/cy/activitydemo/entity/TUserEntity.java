package cn.cy.activitydemo.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("t_user")
public class TUserEntity {

    @TableId
    private String listId;
}
