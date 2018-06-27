package com.kc.demo.controller;

import com.kc.demo.config.MyConfig;
import com.kc.demo.jobs.PreviewImageTask;
import com.kc.demo.model.Article;
import com.kc.demo.service.ArticleService;
import com.kc.demo.service.ComAnswerService;
import com.kc.demo.service.ComQuestionService;
import com.kc.demo.util.*;
import com.kc.demo.vo.ViewDetailVo;
import com.kc.demo.vo.Result;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.String;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;


@Controller
@RequestMapping(value = "/article")
public class ArticleController {
    private Logger logger = LogManager.getLogger(ArticleController.class);

    @Resource
    private ArticleService articleService;

    @Resource
    private ComQuestionService comQuestionService;

    @Resource
    private ComAnswerService comAnswerService;

    @Resource
    private MyConfig myConfig;

    /**
     * 发布文章
     * @param userid
     * @param title
     * @param content
     * @param articletype
     * @param topicid
     * @return
     */
    @RequestMapping(value = "/publish" ,method = RequestMethod.POST)
    public @ResponseBody
    Result publishArticle(@RequestParam(value = "userid", required = false) String userid,
                          @RequestParam(value = "title", required = false) String title,
                          @RequestParam(value = "content", required = false) String content,
                          @RequestParam(value = "articletype", required = false) String articletype,
                          @RequestParam(value = "topicid", required = false) String topicid) {
        Result result = new Result();
        if (StringUtil.isEmpty(userid) || StringUtil.isEmpty(content) || StringUtil.isEmpty(articletype)) {
            result.setStatusCode("1001");
            result.setErrorMsg("请填写必要内容");
            return result;
        }
        Article article = new Article();
        article.setUserid(Integer.parseInt(userid));
        article.setTitle(title);
        article.setContent(content);
        article.setArticletypeid(Integer.parseInt(articletype));
        article.setTopicid(topicid);
        int articleid;
        try {
            articleid = articleService.add(article);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatusCode("500");
            result.setErrorMsg(e.getMessage());
            return result;
        }
        result.setStatusCode("200");
        result.setData(articleid);
        return result;
    }

    /**
     * 详情预览
     * @param userId
     * @param typeId (0文章，1问题，2回答)
     * @param contentId
     * @return
     */
    @RequestMapping("/detail/view")
    public @ResponseBody
    Result getArticleDetailView(@RequestParam(value = "userid", required = false) Integer userId ,
                                @RequestParam(value = "typeid", required = false) Integer typeId ,
                                @RequestParam(value = "contentid", required = false)Integer contentId,
                                @RequestParam(value = "pagenum", required = false)Integer pageNum,
                                @RequestParam(value = "pagesize", required = false)Integer pageSize) {
        Result result = new Result();
        ViewDetailVo detail = null;
        try {
            switch (typeId)
            {
                case 0:
                    detail = articleService.getArticleDetail(userId,contentId);
                    break;
                case 1:{
                    detail = comQuestionService.getComQuestionDetail(userId,contentId,pageNum,pageSize);
                    break;
                }
                case 2:{
                    detail = comAnswerService.getComAnswerDetail(userId,contentId);
                    break;
                }
            }
            result.setStatusCode("200");
            result.setData(detail);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatusCode("500");
            result.setErrorMsg(e.getMessage());
            return result;
        }
        return result;
    }

    /**
     * 根据板块id获取文章问题列表
     *
     * @param articleTypeId
     * @return
     */
    @RequestMapping(value = "/section/{articletypeid}")
    public @ResponseBody
    Result getArticleSectionList(@PathVariable(value = "articletypeid", required = false) Integer articleTypeId,
                                 @RequestParam(value = "pagenum") Integer pageNum,
                                 @RequestParam(value = "pagesize") Integer pageSize) {
        Result result = new Result();
        Map<String,Object> data = new HashMap<>();
        Map<String,Object> dataArticle = null;
        Map<String,Object> dataComQuestion = null;
        try {
            dataArticle = articleService.getArticleListByArticleTypeId(articleTypeId,pageNum,pageSize/2);
            dataComQuestion = comQuestionService.getQuestionListByQuestionTypeId(articleTypeId,pageNum,pageSize/2);
            List<Object> listArticle =(List<Object>) dataArticle.get("list");
            List<Object> listComquestion =(List<Object>)dataComQuestion.get("list");
            Long totalArticle = (Long)dataArticle.get("total");
            Long totalComquestion = (Long)dataComQuestion.get("total");
            listComquestion.addAll(listArticle);
            data.put("total",totalArticle+totalComquestion);
            data.put("list",listComquestion);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatusCode("500");
            result.setErrorMsg(e.getMessage());
            return result;
        }
        result.setStatusCode("200");
        result.setData(data);
        return result;
    }

    /**
     * 获取用户关注用户文章列表
     *
     * @param userid
     * @return
     */
    @RequestMapping("/follow/{userid}")
    public @ResponseBody
    Result getFollow(@PathVariable(value = "userid", required = false) Integer userid,
                     @RequestParam(value = "pagenum") Integer pageNum,
                     @RequestParam(value = "pagesize") Integer pageSize) {
        Result result = new Result();
        Map<String,Object> data = null;
        try {
            data = articleService.getUserFollowArticles(userid,pageNum,pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatusCode("500");
            result.setErrorMsg(e.getMessage());
            return result;
        }
        result.setStatusCode("200");
        result.setData(data);
        return result;
    }

    /**
     * 搜索文章标题关键字
     *
     * @param articletitle
     * @return
     */
    @RequestMapping("/search")
    public @ResponseBody
    Result searchByTitleKey(@RequestParam("articletitle") String articletitle,
                            @RequestParam(value = "pagenum") Integer pageNum,
                            @RequestParam(value = "pagesize") Integer pageSize) {
        Result result = new Result();
        Map<String,Object> data = null;
        try {
            data = articleService.getArticleListByTitleKey(articletitle,pageNum,pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatusCode("500");
            result.setErrorMsg(e.getMessage());
            return result;
        }
        result.setStatusCode("200");
        result.setData(data);
        return result;
    }

    /**
     * 搜索话题
     *
     * @param topic
     * @return
     */
    @RequestMapping("/topic/search")
    public @ResponseBody
    Result searchTopic(@RequestParam("topic") String topic,
                       @RequestParam(value = "pagenum") Integer pageNum,
                       @RequestParam(value = "pagesize") Integer pageSize) {
        Result result = new Result();
        Map<String,Object> data = new HashMap<>();
        Map<String ,Object> topicData = null;
        List<Object> topicsList = new ArrayList<>();
        try {
            long topicsTotal=0;
            topicData = articleService.getArticleListByTopic(topic,pageNum,pageSize);
            List<Object> listArticle =(List<Object>) topicData.get("list");
            long listTotal = (Long)topicData.get("total");
            topicsList.addAll(listArticle);
            topicsTotal=topicsTotal+ listTotal;
            data.put("total",topicsTotal);
            data.put("list",topicsList);

        } catch (Exception e) {
            e.printStackTrace();
            result.setStatusCode("500");
            result.setErrorMsg(e.getMessage());
            return result;
        }
        result.setStatusCode("200");
        result.setData(data);
        return result;
    }

    /**
     * 点赞文章
     *
     * @param articleId
     * @return
     */
    @RequestMapping("/praise")
    public @ResponseBody
    Result praiseArticle(@RequestParam(value = "userid") Integer userId,
                         @RequestParam(value = "articleid") Integer articleId) {
        Result result = new Result();
        try {
            result.setData(articleService.praiseArticle(userId,articleId));
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
     * 踩踏文章
     *
     * @param articleId
     * @return
     */
    @RequestMapping("/tread")
    public @ResponseBody
    Result threadArticle(@RequestParam(value = "userid") Integer userId,
                         @RequestParam(value = "articleid") Integer articleId) {
        Result result = new Result();
        try {
            articleService.treadArticle(userId,articleId);
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
     * 收藏文章
     *
     * @param articleId
     * @return
     */
    @RequestMapping("/collection")
    public @ResponseBody
    Result collectionArticle(@RequestParam(value = "userid") Integer userId,
                         @RequestParam(value = "articleid") Integer articleId) {
        Result result = new Result();
        try {
            articleService.collectionArticle(userId,articleId);
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
     * 上传文章图片
     * @param imgFile
     * @param articleId
     * @param request
     * @return
     */
    @RequestMapping("/img/upload")
    public @ResponseBody Result uploadImg(@RequestParam("imgFile") MultipartFile imgFile,@RequestParam("articleid") Integer articleId,HttpServletRequest request)  {
        Result result = new Result();
        if (imgFile.isEmpty() || StringUtil.isEmpty(imgFile.getOriginalFilename())) {
            result.setStatusCode("500");
            result.setErrorMsg("文件名为空");
            return result;
        }
        String contentType = imgFile.getContentType();
        if (!contentType.contains("")) {
            result.setStatusCode("500");
            result.setErrorMsg("格式化错误");
            return result;
        }
        int articleImgId = 0;
        try {
            //返回文章图片文件名地址映射表id
            articleImgId = articleService.addArticleImages(imgFile,articleId);
            if(articleImgId == 0){
                result.setStatusCode("500");
                result.setErrorMsg("文件上传任务失败");
                return result;
            }
            result.setStatusCode("200");
            result.setErrorMsg("上传成功");
            result.setData(articleImgId);
        } catch (IOException e) {
            e.printStackTrace();
            result.setStatusCode("500");
            result.setErrorMsg(e.getMessage());
            return result;
        }
        return result;
    }

    /**
     * 预览文章图片
     * @param fileName
     * @return
     */
    @RequestMapping(value="/images/{fileName}",produces = MediaType.IMAGE_PNG_VALUE)
     @ResponseBody
     public ResponseEntity<?> getFile(@PathVariable String fileName) {
        Callable<Object> task = new PreviewImageTask(fileName,myConfig.getImagesArticlePath());
        Future<Object> taskResult = ThreadPoolUtil.submit(task);
        ResponseEntity<?> responseEntity = null;
        try {
            responseEntity = (ResponseEntity<?>) taskResult.get();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
        return  responseEntity;
     }

}
