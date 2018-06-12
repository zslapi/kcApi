package com.kc.demo.controller;

import com.kc.demo.model.Dictionary;
import com.kc.demo.service.DictionaryService;
import com.kc.demo.vo.DictionaryVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/dictionary")
public class DictionaryController {
    @Resource
    private DictionaryService dictionaryService;

    @RequestMapping("/getall")
    public @ResponseBody List<Dictionary> getAllDictionary (HttpServletRequest request, HttpServletResponse response) {
        return dictionaryService.initDictionary(request);
    }
}
