package com.kc.demo.service;

import com.kc.demo.model.ComAnswer;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.Map;

public interface ComAnswerService {
    int add(ComAnswer comAnswer);

    Map<String,Object> getComAnswerListByQueId(Integer comQuestionId,Integer pageNum,Integer pageSize);

    int praiseComAnswer(Integer userId ,Integer comAnswerId);

    int treadComAnswer(Integer userId, Integer comAnswerId);

    int addComAnswerImages(MultipartFile imgFile, Integer comAnswerId) throws FileNotFoundException;


}
