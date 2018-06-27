package com.kc.demo.controller;

import com.kc.demo.model.Comment;
import com.kc.demo.service.ArticleCommentService;
import com.kc.demo.util.StringUtil;
import com.kc.demo.vo.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private ArticleCommentService articleCommentService;

    @RequestMapping(value = "/publish",method = RequestMethod.POST)
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
            int commentId = articleCommentService.add(comment);
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
    public @ResponseBody Result praiseComment (@RequestParam("commentid") Integer commentId,@RequestParam("userid") Integer userId) {
        Result result = new Result();
        try {
            articleCommentService.praiseComment(commentId,userId);
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
    public @ResponseBody Result threadComment (@RequestParam("commentid") Integer commentId,@RequestParam("userid") Integer userId) {
        Result result = new Result();
        try {
            articleCommentService.treadComment(commentId,userId);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatusCode("500");
            result.setErrorMsg(e.getMessage());
            return result;
        }
        result.setStatusCode("200");
        return result;
    }

    /**
     * 获取评论列表
     * @param userId
     * @param typeId
     * @param contentId
     * @return
     */
    @RequestMapping("/commentlist")
    public @ResponseBody Result getCommentList(@RequestParam("userid") Integer userId,
                                               @RequestParam("typeid") Integer typeId,
                                               @RequestParam("contentid") Integer contentId){
        Result result = new Result();

        try {
            switch (typeId){
                case 0:{
                }
                case 1:{

                }
                case 2:{

                }
            }
        }catch (Exception e){

        }

        return result;
    }
}
