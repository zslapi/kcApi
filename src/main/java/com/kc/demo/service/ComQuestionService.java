package com.kc.demo.service;

import com.kc.demo.model.ComQuestion;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.Map;

public interface ComQuestionService {
    int add(ComQuestion record);

    Map<String,Object> getQuestionListByQuestionTypeId(Integer questionTypeId, Integer pageNum, Integer pageSize);

    Map<String,Object> getQuestionListByTitleKey(String titleKey, Integer pageNum, Integer pageSize);

    Map<String,Object> getQuestionListByTopic(String topic, Integer pageNum, Integer pageSize);//获取话题列表

    int praiseQuestion(Integer userId, Integer questionId);

    int treadQuestion(Integer userId, Integer questionId);

    int addQuestionImages(MultipartFile imgFile, Integer articleId) throws FileNotFoundException;

}