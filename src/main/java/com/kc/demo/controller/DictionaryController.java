package com.kc.demo.controller;

import com.kc.demo.model.Dictionary;
import com.kc.demo.service.DictionaryService;
import com.kc.demo.vo.DictionaryVo;
import com.kc.demo.vo.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/dictionary")
public class DictionaryController {
    @Resource
    private DictionaryService dictionaryService;

    @RequestMapping("/getall")
    public @ResponseBody List<Dictionary> getAllDictionary (HttpServletRequest request, HttpServletResponse response) {
        return dictionaryService.initDictionary(request);
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
        Map<String,Object> data = new HashMap<>();
        try {
            Dictionary dictionary = new Dictionary();
            dictionary.setName(topic);
            List<Dictionary>  listTopic= dictionaryService.selectByLikeName(dictionary);
            data.put("list",listTopic);

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

}
