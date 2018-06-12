package com.kc.demo.controller;

import com.kc.demo.model.Article;
import com.kc.demo.service.ArticleService;
import com.kc.demo.util.Constants;
import com.kc.demo.util.FileUtil;
import com.kc.demo.util.ImageUtil;
import com.kc.demo.util.StringUtil;
import com.kc.demo.vo.ArticleDetailVo;
import com.kc.demo.vo.Result;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.spi.http.HttpHandler;
import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/article")
public class ArticleController {
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
    Result getArticleSectionList(@PathVariable(value = "articletypeid", required = false) String articletypeid) {
        Result result = new Result();
        List<Article> data = null;
        try {
            int articleTypeId = Integer.parseInt(articletypeid);
            data = articleService.getArticleListByArticleTypeId(articleTypeId);
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
    Result getFollow(@PathVariable(value = "userid", required = false) String userid) {
        Result result = new Result();
        List<Article> data = null;
        try {
            int userId = Integer.parseInt(userid);
            data = articleService.getUserFollowArticles(userId);
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
    Result searchByTitleKey(@RequestParam("articletitle") String articletitle) {
        Result result = new Result();
        List<Article> data = null;
        try {
            data = articleService.getArticleListByTitleKey(articletitle);
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
    Result searchTopic(@RequestParam("topic") String topic) {
        Result result = new Result();
        List<Article> data = null;
        try {
            data = articleService.getArticleListByTopic(topic);
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
    Result praiseArticle(@RequestParam("articleId") Integer articleId) {
        Result result = new Result();
        try {
            articleService.praiseArticle(articleId);
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
    Result threadArticle(@RequestParam("articleId") Integer articleId) {
        Result result = new Result();
        try {
            articleService.treadArticle(articleId);
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
        String fileName = null;
        try {
            //获取路径
            String filePath = Constants.articleImgFilePath();
            //logger.info("图片保存路径={}", filePath);
            System.out.print("图片保存路径={"+filePath+"}");
            fileName = ImageUtil.saveImg(imgFile, filePath);
            articleService.addArticleImages(imgFile,fileName,articleId,filePath);
            result.setStatusCode("200");
            result.setErrorMsg("上传成功");
            result.setData(fileName);
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
         try {
             InputStream inputStream = new FileInputStream(new File(Constants.articleImgFilePath()+fileName));
             InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
             HttpHeaders headers = new HttpHeaders();
             return new ResponseEntity<>(inputStreamResource,headers, HttpStatus.OK);
         } catch (FileNotFoundException e) {
             e.printStackTrace();
             return ResponseEntity.notFound().build();
         }
     }

}