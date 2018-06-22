package com.kc.demo.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kc.demo.dao.ComQuestionMapper;
import com.kc.demo.model.Article;
import com.kc.demo.model.ComQuestion;
import com.kc.demo.service.ComQuestionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ComQuestionServicelmpl implements ComQuestionService {

    @Resource
    private ComQuestionMapper comQuestionMapper;

    /**
     * 发布问题
     * @param record
     * @return
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public int add(ComQuestion record) {
        comQuestionMapper.insert(record);
        int id = record.getId();
        return id;
    }

    private void addTimeAgoInList(List<ComQuestion> comQuestionList) {
        for (ComQuestion comQuestion:comQuestionList) {
            Date createTimeDate = comQuestion.getCreatetime();
            String timeAgo = getTimeAgoAsString(createTimeDate);
            comQuestion.setTimeAgo(timeAgo);
        }
    }
    /**
     * 根据创建时间获取距离当前时间的时长
     * @param createTimeDate
     * @return
     */
    private String getTimeAgoAsString(Date createTimeDate) {
        Timestamp nowTime = new Timestamp(System.currentTimeMillis());
        String timeAgo = "";
        if(createTimeDate != null) {
            Timestamp articleCreateTime = new Timestamp(createTimeDate.getTime());
            long dayAgo = (nowTime.getTime()-articleCreateTime.getTime())/(1000*60*60*24);
            long hourAgo = (nowTime.getTime()-articleCreateTime.getTime())/(1000*60*60);
            long minuteAgo = (nowTime.getTime()-articleCreateTime.getTime())/(1000*60);
            if(dayAgo>=1){
                timeAgo = (int)dayAgo+"天前";
            } else if (hourAgo>0&&hourAgo<24) {
                timeAgo = (int)hourAgo+"小时前";
            } else if (minuteAgo>0) {
                timeAgo = (int)minuteAgo+"分钟前";
            }

        }
        return timeAgo;
    }


    /**
     * 根据问题类型获取问题列表
     * @param questionTypeId
     * @return
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class,Exception.class})
    public Map<String,Object> getQuestionListByQuestionTypeId(Integer questionTypeId, Integer pageNum, Integer pageSize) {
        Map<String,Object> resultMap = new HashMap<>();
        PageHelper.startPage(pageNum, pageSize);
        List<ComQuestion> comQuestionList = comQuestionMapper.selectListByQuestionTypeId(questionTypeId);
        addTimeAgoInList(comQuestionList);
        PageInfo<ComQuestion> pageInfo = new PageInfo<>(comQuestionList);
        resultMap.put("total",pageInfo.getTotal());
        resultMap.put("list",pageInfo.getList());
        return resultMap;
    }

    /**
     * 根据问题标题获取问题列表
     * @param titleKey
     * @return
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class,Exception.class})
    public Map<String,Object> getQuestionListByTitleKey (String titleKey,Integer pageNum,Integer pageSize) {
        Map<String,Object> resultMap = new HashMap<>();
        ComQuestion comQuestion = new ComQuestion();
        comQuestion.setTitle(titleKey);
        PageHelper.startPage(pageNum, pageSize);
        List<ComQuestion> comQuestionList = comQuestionMapper.selectByFilter(comQuestion);
        addTimeAgoInList(comQuestionList);
        PageInfo<ComQuestion> pageInfo = new PageInfo<>(comQuestionList);
        resultMap.put("total",pageInfo.getTotal());
        resultMap.put("list",pageInfo.getList());
        return resultMap;
    }

    /**
     * 根据问题主题获取问题列表
     * @param topic
     * @return
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class,Exception.class})
    public Map<String,Object> getQuestionListByTopic (String topic,Integer pageNum,Integer pageSize) {
        Map<String,Object> resultMap = new HashMap<>();
        ComQuestion comQuestion = new ComQuestion();
        comQuestion.setTopicid(2);
        PageHelper.startPage(pageNum, pageSize);
        List<ComQuestion> comQuestionList = comQuestionMapper.selectByFilter(comQuestion);
        addTimeAgoInList(comQuestionList);
        PageInfo<ComQuestion> pageInfo = new PageInfo<>(comQuestionList);
        resultMap.put("total",pageInfo.getTotal());
        resultMap.put("list",pageInfo.getList());
        return resultMap;
    }

    /**
     * 对问题点赞
     * @param userId
     * @param questionId
     * @return
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class,Exception.class})
    public int praiseQuestion(Integer userId,Integer questionId) {

        return 0;
    }

    /**
     * 对问题点踩
     * @param userId
     * @param questionId
     * @return
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class,Exception.class})
    public int treadQuestion(Integer userId,Integer questionId){
        return 0;
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class,Exception.class})
    public int addQuestionImages(MultipartFile imgFile, Integer articleId) throws FileNotFoundException {
        return 0;
    }
}
