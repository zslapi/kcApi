package com.kc.demo.controller;

import com.kc.demo.dao.ComAnswerMapper;
import com.kc.demo.model.ComAnswer;
import com.kc.demo.service.ComAnswerService;
import com.kc.demo.util.StringUtil;
import com.kc.demo.vo.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequestMapping("/comanswer")
public class ComAnswerController {

    @Resource
    private ComAnswerService comAnswerService;

    @RequestMapping("/publish")
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
     * 上传问题图片
     * @param imgFile
     * @param comAnswerId
     * @param request
     * @return
     */
    @RequestMapping("/img/upload")
    public @ResponseBody Result uploadImg(@RequestParam("imgFile") MultipartFile imgFile, @RequestParam("comAnswerId") Integer comAnswerId, HttpServletRequest request)  {
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


}
