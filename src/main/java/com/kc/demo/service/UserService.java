package com.kc.demo.service;

import com.kc.demo.vo.UserCenterVO;

public interface UserService {
    int wechatRegister(String wechatid, String token, String nickname, String headimageurl) throws Exception;

    int wechatLogin(String wechatid, String token, String nickname, String headimageurl) throws Exception;

    UserCenterVO getUserCenterInfo (String wechatid) throws Exception;
}
