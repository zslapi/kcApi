package com.kc.demo.controller;

import com.kc.demo.model.Comment;
import com.kc.demo.service.CommentService;
import com.kc.demo.util.StringUtil;
import com.kc.demo.vo.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private CommentService commentService;

    @RequestMapping("/publish")
    public @ResponseBody Result publishComment (@RequestParam(value = "userid", required = false) String userid,
                           @RequestParam(value = "article", required = false) String article,
                           @RequestParam(value = "content", required = false) String content,
                           @RequestParam(value = "parent", required = false) String parent)
    {
        Result result = new Result();
        if(StringUtil.isEmpty(userid) || StringUtil.isEmpty(content) || StringUtil.isEmpty(article)){
            result.setStatusCode("1001");
            result.setErrorMsg("请填写必要内容");
            return result;
        }
        Comment comment = new Comment();
        comment.setUserid(Integer.parseInt(userid));
        comment.setArticleid(Integer.parseInt(article));
        comment.setContent(content);
        if(!StringUtil.isEmpty(parent)){
            comment.setParentid(Integer.parseInt(parent));
        }

        try {
            int commentId = commentService.add(comment);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatusCode("500");
            result.setErrorMsg(e.getMessage());
            return result;
        }

        result.setStatusCode("200");
        return result;
    }

    @RequestMapping("/praise")
    public @ResponseBody Result praiseComment (@RequestParam("commentId") Integer commentId,@RequestParam("userId") Integer userId) {
        Result result = new Result();
        try {
            commentService.praiseComment(commentId,userId);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatusCode("500");
            result.setErrorMsg(e.getMessage());
            return result;
        }
        result.setStatusCode("200");
        return result;
    }

    @RequestMapping("/tread")
    public @ResponseBody Result threadComment (@RequestParam("commentId") Integer commentId,@RequestParam("userId") Integer userId) {
        Result result = new Result();
        try {
            commentService.treadComment(commentId,userId);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatusCode("500");
            result.setErrorMsg(e.getMessage());
            return result;
        }
        result.setStatusCode("200");
        return result;
    }
}
