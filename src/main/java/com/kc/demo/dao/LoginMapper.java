package com.kc.demo.dao;

import com.kc.demo.model.Login;

public interface LoginMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Login record);

    int insertSelective(Login record);

    Login selectByPrimaryKey(Integer id);

    Login selectByWechatKeys(String wechatId,String token);

    int updateByPrimaryKeySelective(Login record);

    int updateByPrimaryKey(Login record);
}