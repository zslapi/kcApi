package com.kc.demo.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kc.demo.config.MyConfig;
import com.kc.demo.dao.*;
import com.kc.demo.jobs.SaveImagesTask;
import com.kc.demo.model.*;
import com.kc.demo.service.ComQuestionService;
import com.kc.demo.util.Constants;
import com.kc.demo.util.StringUtil;
import com.kc.demo.util.ThreadPoolUtil;
import com.kc.demo.vo.PraiseTreadVo;
import com.kc.demo.vo.ViewDetailVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.security.Key;
import java.sql.Timestamp;
import java.util.*;
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

    @Resource
    private PraiseTreadMapper praiseTreadMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource ComAnswerMapper comAnswerMapper;

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
        int result = 0;
        ComQuestion comQuestion = comQuestionMapper.selectPraiseTreadCount(questionId);
        if(comQuestion==null){
            return result;
        }
        String praiseCountStr =  comQuestion.getPraisecount();
        String treadCountStr = comQuestion.getTreadcount();
        int praiseCount = 0;
        int treadCount = 0;
        if(!StringUtil.isEmpty(praiseCountStr) && !StringUtil.isEmpty(treadCountStr)){
            praiseCount = Integer.parseInt(praiseCountStr);
            treadCount = Integer.parseInt(treadCountStr);
        }
        HashMap hashMap = new HashMap();
        hashMap.put("userId",userId);
        hashMap.put("contentId",questionId);
        hashMap.put("typeId",2);
        try {
            PraiseTread praiseTread = praiseTreadMapper.selectByTypeIdConId(hashMap);
            PraiseTread praiseTreadIn = new PraiseTread();
            praiseTreadIn.setUserid(userId);
            praiseTreadIn.setContentid(questionId);
            praiseTreadIn.setTypeid(2);
            if(praiseTread == null) {
                praiseTreadIn.setIspraise(true);
                praiseCount += 1;
                praiseTreadMapper.insert(praiseTreadIn);
            }else {
                if(praiseTread.getIspraise() == null) {
                    praiseTreadIn.setIspraise(true);
                    praiseCount += 1;
                    if(praiseTread.getIstread() != null && praiseTread.getIstread() == true){
                        praiseTreadIn.setIstread(false);
                        treadCount -= 1;

                    }
                } else{
                    if(praiseTread.getIspraise() == true){
                        praiseTreadIn.setIspraise(false);
                        praiseCount -= 1;
                    }else{
                        praiseTreadIn.setIspraise(true);
                        praiseCount += 1;
                        if(praiseTread.getIstread() != null && praiseTread.getIstread() == true){
                            praiseTreadIn.setIstread(false);
                            treadCount -= 1;
                        }
                    }
                }
                praiseTreadMapper.updatePraiseTread(praiseTreadIn);
            }
            comQuestion.setPraisecount(String.valueOf(praiseCount));
            comQuestion.setTreadcount(String.valueOf(treadCount));
            result = comQuestionMapper.updateByPrimaryKeySelective(comQuestion);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
        }
        return result;
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
        int result = 0;
        ComQuestion comQuestion = comQuestionMapper.selectPraiseTreadCount(questionId);
        if(comQuestion==null){
            return result;
        }
        String praiseCountStr =  comQuestion.getPraisecount();
        String treadCountStr = comQuestion.getTreadcount();
        int praiseCount = 0;
        int treadCount = 0;
        if(!StringUtil.isEmpty(praiseCountStr) && !StringUtil.isEmpty(treadCountStr)){
            praiseCount = Integer.parseInt(praiseCountStr);
            treadCount = Integer.parseInt(treadCountStr);
        }
        HashMap hashMap = new HashMap();
        hashMap.put("userId",userId);
        hashMap.put("contentId",questionId);
        hashMap.put("typeId",2);
        try {
            PraiseTread praiseTread = praiseTreadMapper.selectByTypeIdConId(hashMap);
            PraiseTread praiseTreadIn = new PraiseTread();
            praiseTreadIn.setUserid(userId);
            praiseTreadIn.setContentid(questionId);
            praiseTreadIn.setTypeid(2);
            if(praiseTread == null) {
                praiseTreadIn.setIstread(true);
                treadCount += 1;
                praiseTreadMapper.insert(praiseTreadIn);
            }else {
                if(praiseTread.getIstread() == null) {
                    praiseTreadIn.setIstread(true);
                    treadCount += 1;
                    if(praiseTread.getIspraise() != null && praiseTread.getIspraise() == true){
                        praiseTreadIn.setIspraise(false);
                        praiseCount -= 1;
                    }
                } else{
                    if(praiseTread.getIstread() == true){
                        praiseTreadIn.setIstread(false);
                        treadCount -= 1;
                    }else{
                        praiseTreadIn.setIstread(true);
                        treadCount += 1;
                        if(praiseTread.getIspraise() != null && praiseTread.getIspraise() == true){
                            praiseTreadIn.setIspraise(false);
                            praiseCount -= 1;
                        }
                    }
                }
                praiseTreadMapper.updatePraiseTread(praiseTreadIn);
            }
            comQuestion.setPraisecount(String.valueOf(praiseCount));
            comQuestion.setTreadcount(String.valueOf(treadCount));
            result = comQuestionMapper.updateByPrimaryKeySelective(comQuestion);
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
        return result;
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

    public ViewDetailVo getComQuestionDetail(Integer userId, Integer questionId,Integer pageNum,Integer pageSize){
        ComQuestion comQuestion = comQuestionMapper.selectByPrimaryKey(questionId);
        ViewDetailVo detailVo = new ViewDetailVo();
        detailVo.setTitle(comQuestion.getTitle());
        detailVo.setContent(comQuestion.getContent());
        detailVo.setPraiseCount(comQuestion.getPraisecount());
        detailVo.setTreadCount(comQuestion.getTreadcount());
        detailVo.setArticleId(comQuestion.getId());
        detailVo.setCreateTime(comQuestion.getCreatetime());
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(comQuestion.getUserid());
        ViewDetailVo.setUserInfo(userInfo,detailVo);
        HashMap hashMap = new HashMap();
        hashMap.put("userId",userId);
        hashMap.put("contentId",questionId);
        PraiseTread praiseTread = praiseTreadMapper.selectByArticleId(hashMap);
        PraiseTreadVo.detailPraiseTread(praiseTread,detailVo);
        detailVo.setNickName(userInfo.getNickname());
        detailVo.setHeadImageUrl(userInfo.getHeadimageurl());
        Date createTimeDate = comQuestion.getCreatetime();
        String timeAgo = StringUtil.getTimeAgoAsString(createTimeDate);
        detailVo.setTimeAgo(timeAgo);
        String topicName = getTopicNameById(comQuestion.getTopicid());
        detailVo.setTopic(topicName);
        List<CommunityImages> imagesList = communityImagesMapper.selectCommunityImageListByQueId(comQuestion.getId());
        List<String> imageUrlList = new ArrayList<>();
        for (int i=0;i<imagesList.size();i++) {
            CommunityImages image = imagesList.get(i);
            imageUrlList.add(Constants.serverUrl()+myConfig.getImagesComQuestionPath()+image.getFilename());
        }
        detailVo.setImageUrl(imageUrlList);

        HashMap<String,Object> resultMap = new HashMap<>();
        PageHelper.startPage(pageNum, pageSize);
        List<ComAnswer> comAnswerList = comAnswerMapper.selectByComQuestionId(questionId);
        PageInfo<ComAnswer> pageInfo = new PageInfo<>(comAnswerList);
        resultMap.put("total",pageInfo.getTotal());
        resultMap.put("list",pageInfo.getList());
        detailVo.setAnswerMap(resultMap);
        return detailVo;
    }


    private String getTopicNameById (Integer id) {
        if("".equals(String.valueOf(id)) || "null".equals(String.valueOf(id))) {
            return "";
        }
        String name = null;
        ComQuestion topic = comQuestionMapper.selectByPrimaryKey(id);
        if(topic!=null){
            name = topic.getTitle();
        }
        return name;
    }
}
