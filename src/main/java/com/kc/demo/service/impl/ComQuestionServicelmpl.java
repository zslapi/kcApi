package com.kc.demo.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kc.demo.config.MyConfig;
import com.kc.demo.dao.ComQuestionMapper;
import com.kc.demo.dao.CommunityImagesMapper;
import com.kc.demo.jobs.SaveImagesTask;
import com.kc.demo.model.Article;
import com.kc.demo.model.ArticleImages;
import com.kc.demo.model.ComQuestion;
import com.kc.demo.model.CommunityImages;
import com.kc.demo.service.ComQuestionService;
import com.kc.demo.util.StringUtil;
import com.kc.demo.util.ThreadPoolUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.security.Key;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class ComQuestionServicelmpl implements ComQuestionService {

    @Resource
    private ComQuestionMapper comQuestionMapper;

    @Resource
    private MyConfig myConfig;

    @Resource
    private CommunityImagesMapper communityImagesMapper;

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
            String timeAgo = StringUtil.getTimeAgoAsString(createTimeDate);
            comQuestion.setTimeAgo(timeAgo);
        }
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

    /**
     * 添加问题的图片
     * @param imgFile
     * @param comQuestionId
     * @return
     * @throws FileNotFoundException
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class,Exception.class})
    public int addQuestionImages(MultipartFile imgFile, Integer comQuestionId) throws FileNotFoundException {
        //先保存文件
        String fileName = null;
        String path = null;
        Callable<Object> task = new SaveImagesTask(imgFile,myConfig.getImagesComQuestionPath());
        Future<Object> taskResult = ThreadPoolUtil.submit(task);
        Map<String,Object> resultMap = SaveImagesTask.getSaveImgPath(taskResult);
        fileName = (String) resultMap.get("fileName");
        path = (String)resultMap.get("path");
        int comQuestionImgId = 0;
        if(!StringUtil.isEmpty(fileName)&&!StringUtil.isEmpty(path)){
            CommunityImages communityImages = new CommunityImages();
            communityImages.setOriginalname(imgFile.getOriginalFilename());
            communityImages.setFilename(fileName);
            communityImages.setComquestionid(comQuestionId);
            communityImages.setImgtype(imgFile.getContentType());
            communityImages.setFilepath(path);
            communityImagesMapper.insert(communityImages);
            comQuestionImgId = communityImages.getId();
        }
        return comQuestionImgId;
    }
}
