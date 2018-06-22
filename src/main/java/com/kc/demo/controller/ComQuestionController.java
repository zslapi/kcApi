package com.kc.demo.controller;

import com.kc.demo.model.ComQuestion;
import com.kc.demo.service.ComQuestionService;
import com.kc.demo.util.StringUtil;
import com.kc.demo.vo.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

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
    @RequestMapping("/publish")
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
        try {
            comQuestionService.add(comQuestion);
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
