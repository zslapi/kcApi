package com.kc.demo.controller;

import com.kc.demo.config.MyConfig;
import com.kc.demo.dao.ComAnswerMapper;
import com.kc.demo.jobs.PreviewImageTask;
import com.kc.demo.model.ComAnswer;
import com.kc.demo.service.ComAnswerService;
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
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Controller
@RequestMapping("/comanswer")
public class ComAnswerController {

    @Resource
    private ComAnswerService comAnswerService;

    @Resource
    private MyConfig myConfig;

    /**
     * 写答案
     * @param userid
     * @param comquestionid
     * @param title
     * @param content
     * @return
     */
    @RequestMapping(value = "/publish",method = RequestMethod.POST)
    public @ResponseBody
    Result publishComAnswer(@RequestParam(value = "userid", required = false) String userid,
                            @RequestParam(value = "comquestionid", required = false) String comquestionid,
                            @RequestParam(value = "title", required = false) String title,
                            @RequestParam(value = "content", required = false) String content)
    {
        Result result = new Result();
        if(StringUtil.isEmpty(userid) || StringUtil.isEmpty(content) || StringUtil.isEmpty(comquestionid)){
            result.setStatusCode("1001");
            result.setErrorMsg("请填写必要内容");
            return result;
        }
        ComAnswer comAnswer = new ComAnswer();
        comAnswer.setUserid(Integer.parseInt(userid));
        comAnswer.setComquestionid(Integer.parseInt(comquestionid));
        comAnswer.setContent(content);
        comAnswer.setTitle(title);
        int comAnswerid;
        try {
            comAnswerid = comAnswerService.add(comAnswer);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatusCode("500");
            result.setErrorMsg(e.getMessage());
            return result;
        }
        result.setStatusCode("200");
        result.setData(comAnswerid);
        return result;
    }

    /**
     * 上传答案图片
     * @param imgFile
     * @param comAnswerId
     * @param request
     * @return
     */
    @RequestMapping("/img/upload")
    public @ResponseBody Result uploadImg(@RequestParam("imgFile") MultipartFile imgFile, @RequestParam("comanswerid") Integer comAnswerId, HttpServletRequest request)  {
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
            articleImgId = comAnswerService.addComAnswerImages(imgFile,comAnswerId);
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
     * 点赞答案
     * @param userId
     * @param comAnswerId
     * @return
     */
    @RequestMapping("/praise")
    public @ResponseBody
    Result praiseComAnswer(@RequestParam(value = "userid") Integer userId,
                         @RequestParam(value = "comanswerid") Integer comAnswerId) {
        Result result = new Result();
        try {
            comAnswerService.praiseComAnswer(userId,comAnswerId);
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
     * 踩踏答案
     *@param userId
     *@param comAnswerId
     * @return
     */
    @RequestMapping("/tread")
    public @ResponseBody
    Result threadComAnswer(@RequestParam(value = "userid") Integer userId,
                         @RequestParam(value = "comanswerid") Integer comAnswerId) {
        Result result = new Result();
        try {
            comAnswerService.treadComAnswer(userId,comAnswerId);
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
     * 收藏答案
     *
     * @param comAnswerId
     * @return
     */
    @RequestMapping("/collection")
    public @ResponseBody
    Result collectionComAnswer(@RequestParam(value = "userid") Integer userId,
                             @RequestParam(value = "comanswerid") Integer comAnswerId) {
        Result result = new Result();
        try {
            comAnswerService.collectionComAnswer(userId,comAnswerId);
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
     * 预览答案图片
     * @param fileName
     * @return
     */
    @RequestMapping(value="/images/{fileName}",produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public ResponseEntity<?> getFile(@PathVariable String fileName) {
        Callable<Object> task = new PreviewImageTask(fileName,myConfig.getImagesComAnswerPath());
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
