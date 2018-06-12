package com.kc.demo.service.impl;

import com.kc.demo.dao.ArticleMapper;
import com.kc.demo.dao.LoginMapper;
import com.kc.demo.dao.UserFollowMapper;
import com.kc.demo.dao.UserInfoMapper;
import com.kc.demo.model.Login;
import com.kc.demo.model.UserInfo;
import com.kc.demo.service.UserService;
import com.kc.demo.vo.UserCenterVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private LoginMapper loginMapper;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private UserFollowMapper userFollowMapper;

    @Override
    public int wechatRegister(String wechatid, String token, String nickname, String headimageurl) throws Exception {
        int userid = createWechatUserAccount(wechatid, token, nickname, headimageurl);

        return userid;
    }

    private int createWechatUserAccount(String wechatid, String token, String nickname, String headimageurl) {
        //新增userinfo
        UserInfo userInfo = new UserInfo();
        userInfo.setNickname(nickname);
        userInfo.setName(nickname);
        userInfo.setHeadimageurl(headimageurl);
        userInfoMapper.insert(userInfo);
        int userid = userInfo.getId();
        //新增login
        Login login = new Login();
        login.setToken(token);
        login.setWechatid(wechatid);
        login.setUserid(userid);
        loginMapper.insert(login);
        return userid;
    }

    /**
     * 微信用户登录
     *
     * @param wechatid1
     * @param s
     * @param wechatid
     * @param token
     * @return
     * @throws Exception
     */
    @Override
    public int wechatLogin(String wechatid, String token, String nickname, String headimageurl) throws Exception {
        Login login = loginMapper.selectByWechatKeys(wechatid,token);
        if(login == null){
            int userid = createWechatUserAccount(wechatid, token, nickname, headimageurl);
            return userid;
        }
        return login.getUserid();
    }

    @Override
    public UserCenterVO getUserCenterInfo(String wechatid) throws Exception {
        UserCenterVO userCenterVO = new UserCenterVO();
        UserInfo userInfo = userInfoMapper.selectByWeChatId(wechatid);
        int userId = userInfo.getId();
        userCenterVO.setUserId(userId);
        userCenterVO.setUserName(userInfo.getName());
        userCenterVO.setUserHeadImgUrl(userInfo.getHeadimageurl());

        //<知识力>,<记录>,<收藏>暂时写死
        userCenterVO.setZslCount(99);
        userCenterVO.setRecordsCount(39);
        userCenterVO.setCollectionsCount(99);

        //获取个人创作count
        int creationCount = articleMapper.selectCountByUserId(userId);
        userCenterVO.setCreationCount(creationCount);
        int followCount = userFollowMapper.selectCountByUserId(userId);
        userCenterVO.setFollowCount(followCount);
        return userCenterVO;
    }
}
