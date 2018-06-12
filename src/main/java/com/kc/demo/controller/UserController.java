package com.kc.demo.controller;

import com.kc.demo.service.UserService;
import com.kc.demo.util.StringUtil;
import com.kc.demo.vo.Result;
import com.kc.demo.vo.UserCenterVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 微信用户注册
     * @param wechatid
     * @param token
     * @param nickname
     * @param headimageurl
     * @return result
     */
    @RequestMapping("/wechat/register")
    public @ResponseBody Result wechatRegister (@RequestParam(value = "wechatid", required = false) String wechatid,
                                              @RequestParam(value = "token", required = false) String token,
                                              @RequestParam(value = "nickname", required = false) String nickname,
                                              @RequestParam(value = "headimageurl", required = false) String headimageurl)
    {
        Result result = new Result();
        if(StringUtil.isEmpty(wechatid) || StringUtil.isEmpty(token) || StringUtil.isEmpty(nickname) || StringUtil.isEmpty(headimageurl)){
            result.setStatusCode("1001");
            result.setErrorMsg("请完成最少必要内容：wechatid，token，nickname，headimageurl");
            return result;
        }
        try {
            int userid = userService.wechatRegister(wechatid,token,nickname,headimageurl);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatusCode("500");
            result.setErrorMsg(e.getMessage());
            return result;
        }
        result.setStatusCode("200");
        return result;
    }

    @RequestMapping("/wechat/login")
    public @ResponseBody Result wechatLogin (@RequestParam(value = "wechatid", required = false) String wechatid,
                                             @RequestParam(value = "token", required = false) String token,
                                             @RequestParam(value = "nickname", required = false) String nickname,
                                             @RequestParam(value = "headimageurl", required = false) String headimageurl)
    {
       Result result = new Result();
       int userid;
       if(StringUtil.isEmpty(wechatid) || StringUtil.isEmpty(token)) {
           result.setStatusCode("1001");
           result.setErrorMsg("请完成最少登录要素：wechatid，token");
           return result;
       }
        try {
            userid = userService.wechatLogin(wechatid,token,nickname,headimageurl);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatusCode("500");
            result.setErrorMsg(e.getMessage());
            return result;
        }
        if (userid==0) {
            result.setStatusCode("500");
            result.setErrorMsg("无此用户");
            return result;
        }
        result.setStatusCode("200");
        result.setData(""+userid);
        return result;
    }

    @RequestMapping("/wechat/center")
    public @ResponseBody Result userCenter (@RequestParam(value = "wechatid", required = false) String wechatid) {
        Result result = new Result();
        if(StringUtil.isEmpty(wechatid)){
            result.setStatusCode("1002");
            result.setErrorMsg("wechatid为空");
            return result;
        }
        UserCenterVO userCenterVO;
        try {
            userCenterVO = userService.getUserCenterInfo(wechatid);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatusCode("500");
            result.setErrorMsg(e.getMessage());
            return result;
        }
        result.setStatusCode("200");
        result.setData(userCenterVO);
        return result;
    }

}
