package com.kc.demo.controller;

import com.kc.demo.config.MyConfig;
import com.kc.demo.jobs.PreviewImageTask;
import com.kc.demo.model.ComQuestion;
import com.kc.demo.service.ComQuestionService;
import com.kc.demo.util.StringUtil;
import com.kc.demo.util.ThreadPoolUtil;
import com.kc.demo.vo.Result;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Controller
@RequestMapping("/comquestion")
public class ComQuestionController {

    @Resource
    private ComQuestionService comQuestionService;

    /**
     * 发布问题
     *
     * @param userid
     * @param title
     * @param content
     * @param questiontype
     * @param topicid
     * @return
     */
    @RequestMapping(value = "/publish",method = RequestMethod.POST)
    public @ResponseBody
    Result PublishQuestion(@RequestParam(value = "userid", required = false) String userid,
                           @RequestParam(value = "title", required = false) String title,
                           @RequestParam(value = "content", required = false) String content,
                           @RequestParam(value = "questiontype", required = false) String questiontype,
                           @RequestParam(value = "topicid", required = false) Integer topicid){
        Result result = new Result();
        if (StringUtil.isEmpty(userid) || StringUtil.isEmpty(content) || StringUtil.isEmpty(questiontype)) {
            result.setStatusCode("1001");
            result.setErrorMsg("请填写必要内容");
            return result;
        }
        ComQuestion comQuestion = new ComQuestion();
        comQuestion.setUserid(Integer.parseInt(userid));
        comQuestion.setTitle(title);
        comQuestion.setContent(content);
        comQuestion.setQuestiontypeid(Integer.parseInt(questiontype));
        comQuestion.setTopicid(topicid);
        int comQuestionId;
        try {
            comQuestionId = comQuestionService.add(comQuestion);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatusCode("500");
            result.setErrorMsg(e.getMessage());
            return result;
        }
        result.setStatusCode("200");
        result.setData(comQuestionId);
        return result;
    }

    /**
     * 上传问题图片
     * @param imgFile
     * @param comQuestionId
     * @param request
     * @return
     */
    @RequestMapping("/img/upload")
    public @ResponseBody Result uploadImg(@RequestParam("imgFile") MultipartFile imgFile, @RequestParam("comquestionid") Integer comQuestionId, HttpServletRequest request)  {
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
            articleImgId = comQuestionService.addQuestionImages(imgFile,comQuestionId);
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
     * 预览问题图片
     * @param filename
     * @return
     */
    @RequestMapping(value="/images/{filename}",produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public ResponseEntity<?> getFile(@PathVariable String filename) {
        Callable<Object> task = new PreviewImageTask(filename,myConfig.getImagesComQuestionPath());
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


    /**
     * 根据板块id获取问题列表
     * @param comQuestionTypeId
     * @return
     */
    @RequestMapping(value = "/section/{comquestiontypeid}")
    public @ResponseBody
    Result getArticleSectionList(@PathVariable(value = "comquestiontypeid", required = false) Integer comQuestionTypeId,
                                 @RequestParam(value = "pagenum") Integer pageNum,
                                 @RequestParam(value = "pagesize") Integer pageSize) {
        Result result = new Result();
        Map<String,Object> data = null;
        try {
            data = comQuestionService.getQuestionListByQuestionTypeId(comQuestionTypeId,pageNum,pageSize);
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
     * 搜索问题标题关键字
     *
     * @param comQuestionTitle
     * @return
     */
    @RequestMapping("/search")
    public @ResponseBody
    Result searchByTitleKey(@RequestParam("comquestiontitle") String comQuestionTitle,
                            @RequestParam(value = "pagenum") Integer pageNum,
                            @RequestParam(value = "pagesize") Integer pageSize) {
        Result result = new Result();
        Map<String,Object> data = null;
        try {
            data = comQuestionService.getQuestionListByTitleKey(comQuestionTitle,pageNum,pageSize);
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
        Map<String,Object> data = null;
        try {
            data = comQuestionService.getQuestionListByTopic(topic,pageNum,pageSize);
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
     * 点赞问题
     * @param userId
     * @param comQuestionId
     * @return
     */
    @RequestMapping("/praise")
    public @ResponseBody
    Result praiseComQuestion(@RequestParam(value = "userid") Integer userId,
                         @RequestParam(value = "comquestionid") Integer comQuestionId) {
        Result result = new Result();
        try {
            comQuestionService.praiseQuestion(userId,comQuestionId);
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
     * 踩踏问题
     *@param userId
     *@param comQuestionId
     * @return
     */
    @RequestMapping("/tread")
    public @ResponseBody
    Result threadComQuestion(@RequestParam(value = "userid") Integer userId,
                         @RequestParam(value = "comquestionid") Integer comQuestionId) {
        Result result = new Result();
        try {
            comQuestionService.treadQuestion(userId,comQuestionId);
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
