package com.kc.demo.dao;

import com.kc.demo.model.UserFollow;

import java.util.List;

public interface UserFollowMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserFollow record);

    int insertSelective(UserFollow record);

    UserFollow selectByPrimaryKey(Integer id);

    List<UserFollow> selectByUserId(Integer userid);

    int updateByPrimaryKeySelective(UserFollow record);

    int updateByPrimaryKey(UserFollow record);

    int selectCountByUserId(Integer userid);
}