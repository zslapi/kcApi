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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 暂时不用
     * @param wechatid
     * @param token
     * @param nickname
     * @param headimageurl
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public int wechatRegister(String wechatid, String token, String nickname, String headimageurl) throws Exception {
        int userid = createWechatUserAccount(wechatid, token, nickname, headimageurl);

        return userid;
    }

    private int createWechatUserAccount(String wechatid, String token, String nickname, String headimageurl) {

        return 0;
    }

    /**
     * 微信用户登录
     * @param wechatid
     * @param token
     * @param nickname
     * @param headimageurl
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public int wechatLogin(String wechatid, String token, String nickname, String headimageurl) throws Exception {
        Login login = loginMapper.selectByWechatKeys(wechatid,token);
        if(login == null){
            //新增userinfo
            UserInfo userInfo = new UserInfo();
            userInfo.setNickname(nickname);
            userInfo.setName(nickname);
            userInfo.setHeadimageurl(headimageurl);
            userInfoMapper.insert(userInfo);
            int userid = userInfo.getId();
            //新增login
            login = new Login();
            login.setToken(token);
            login.setWechatid(wechatid);
            login.setUserid(userid);
            loginMapper.insert(login);
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
