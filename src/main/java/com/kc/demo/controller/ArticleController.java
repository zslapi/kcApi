package com.kc.demo.controller;

import com.kc.demo.config.MyConfig;
import com.kc.demo.jobs.PreviewArticleImageTask;
import com.kc.demo.model.Article;
import com.kc.demo.service.ArticleService;
import com.kc.demo.util.*;
import com.kc.demo.vo.ArticleDetailVo;
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
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;


@Controller
@RequestMapping("/article")
public class ArticleController {
    private Logger logger = LogManager.getLogger(ArticleController.class);

    @Resource
    private ArticleService articleService;

    /**
     * 发布文章
     *
     * @param userid
     * @param title
     * @param content
     * @param articletype
     * @param topicid
     * @return
     */
    @RequestMapping("/publish")
    public @ResponseBody
    Result publishArticle(@RequestParam(value = "userid", required = false) String userid,
                          @RequestParam(value = "title", required = false) String title,
                          @RequestParam(value = "content", required = false) String content,
                          @RequestParam(value = "articletype", required = false) String articletype,
                          @RequestParam(value = "topicid", required = false) Integer topicid) {
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

    @RequestMapping("/detail/view/{articleId}")
    public @ResponseBody
    Result getArticleDetailView(@PathVariable(value = "articleId") Integer articleId) {
        Result result = new Result();
        ArticleDetailVo detail = null;

        try {
            detail = articleService.getArticleDetail(articleId);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatusCode("500");
            result.setErrorMsg(e.getMessage());
            return result;
        }
        result.setStatusCode("200");
        result.setData(detail);
        return result;
    }

    /**
     * 根据板块id获取文章列表
     *
     * @param articletypeid
     * @return
     */
    @RequestMapping(value = "/section/{articletypeid}")
    public @ResponseBody
    Result getArticleSectionList(@PathVariable(value = "articletypeid", required = false) Integer articletypeid,
                                 @RequestParam(value = "pageNum") Integer pageNum,
                                 @RequestParam(value = "pageSize") Integer pageSize) {
        Result result = new Result();
        Map<String,Object> data = null;
        try {
            data = articleService.getArticleListByArticleTypeId(articletypeid,pageNum,pageSize);
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
                     @RequestParam(value = "pageNum") Integer pageNum,
                     @RequestParam(value = "pageSize") Integer pageSize) {
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
                            @RequestParam(value = "pageNum") Integer pageNum,
                            @RequestParam(value = "pageSize") Integer pageSize) {
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
                       @RequestParam(value = "pageNum") Integer pageNum,
                       @RequestParam(value = "pageSize") Integer pageSize) {
        Result result = new Result();
        Map<String,Object> data = null;
        try {
            data = articleService.getArticleListByTopic(topic,pageNum,pageSize);
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
                         @RequestParam(value = "articleId") Integer articleId) {
        Result result = new Result();
        try {
            articleService.praiseArticle(userId,articleId);
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
                         @RequestParam("articleId") Integer articleId) {
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

    @RequestMapping("/img/upload")
    public @ResponseBody Result uploadImg(@RequestParam("imgFile") MultipartFile imgFile,@RequestParam("articleId") Integer articleId,HttpServletRequest request)  {
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

    @Resource
    private MyConfig myConfig;
    /**
     * 预览文章图片
     * @param fileName
     * @return
     */
    @RequestMapping(value="/images/{fileName}",produces = MediaType.IMAGE_PNG_VALUE)
     @ResponseBody
     public ResponseEntity<?> getFile(@PathVariable String fileName) {
        Callable<Object> task = new PreviewArticleImageTask(fileName,myConfig.getImagesPath());
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
