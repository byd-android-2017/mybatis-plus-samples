package com.baomidou.mybatisplus.samples.deluxe.mapper;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.samples.deluxe.config.MyBaseMapper;
import com.baomidou.mybatisplus.samples.deluxe.entity.User;
import com.baomidou.mybatisplus.samples.deluxe.model.UserPage;

/**
 * @author miemie
 * @since 2018-08-12
 */
public interface UserMapper extends MyBaseMapper<User> {

    /**
     * 自定义分页查询
     *
     * @param userPage 单独 user 模块使用的分页
     * @return 分页数据
     */
    UserPage selectUserPage(UserPage userPage);

    List<User> findList(@Param(Constants.ENTITY) User user);

    List<User> customerSqlSegment(@Param(Constants.WRAPPER) Wrapper<User> ew);

}
