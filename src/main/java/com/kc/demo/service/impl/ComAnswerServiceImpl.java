package com.kc.demo.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kc.demo.config.MyConfig;
import com.kc.demo.dao.ComAnswerMapper;
import com.kc.demo.dao.CommunityImagesMapper;
import com.kc.demo.dao.PraiseTreadMapper;
import com.kc.demo.dao.UserInfoMapper;
import com.kc.demo.jobs.SaveImagesTask;
import com.kc.demo.model.*;
import com.kc.demo.service.ComAnswerService;
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
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Service
public class ComAnswerServiceImpl implements ComAnswerService {

    @Resource
    private ComAnswerMapper comAnswerMapper;

    @Resource
    private MyConfig myConfig;

    @Resource
    private CommunityImagesMapper communityImagesMapper;

    @Resource
    private PraiseTreadMapper praiseTreadMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    /**
     * 发布答案
     * @param record
     * @return
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public int add(ComAnswer record) {
        comAnswerMapper.insert(record);
        int id = record.getId();
        return id;
    }

    /**
     * 对答案点赞
     * @param userId
     * @param comAnswerId
     * @return
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class,Exception.class})
    public int praiseComAnswer(Integer userId,Integer comAnswerId) {
        int result = 0;
        ComAnswer comAnswer = comAnswerMapper.selectPraiseTreadCount(comAnswerId);
        if(comAnswer==null){
            return result;
        }
        String praiseCountStr =  comAnswer.getPraisecount();
        String treadCountStr = comAnswer.getTreadcount();
        int praiseCount = 0;
        int treadCount = 0;
        if(!StringUtil.isEmpty(praiseCountStr) && !StringUtil.isEmpty(treadCountStr)){
            praiseCount = Integer.parseInt(praiseCountStr);
            treadCount = Integer.parseInt(treadCountStr);
        }
        HashMap hashMap = new HashMap();
        hashMap.put("userId",userId);
        hashMap.put("contentId",comAnswerId);
        hashMap.put("typeId",2);
        try {
            PraiseTread praiseTread = praiseTreadMapper.selectByTypeIdConId(hashMap);
            PraiseTread praiseTreadIn = new PraiseTread();
            praiseTreadIn.setUserid(userId);
            praiseTreadIn.setContentid(comAnswerId);
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
            comAnswer.setPraisecount(String.valueOf(praiseCount));
            comAnswer.setTreadcount(String.valueOf(treadCount));
            result = comAnswerMapper.updateByPrimaryKeySelective(comAnswer);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
        }
        return result;
    }

    /**
     * 对答案点踩
     * @param userId
     * @param comAnswerId
     * @return
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class,Exception.class})
    public int treadComAnswer(Integer userId,Integer comAnswerId){
        int result = 0;
        ComAnswer comAnswer = comAnswerMapper.selectPraiseTreadCount(comAnswerId);
        if(comAnswer==null){
            return result;
        }
        String praiseCountStr =  comAnswer.getPraisecount();
        String treadCountStr = comAnswer.getTreadcount();
        int praiseCount = 0;
        int treadCount = 0;
        if(!StringUtil.isEmpty(praiseCountStr) && !StringUtil.isEmpty(treadCountStr)){
            praiseCount = Integer.parseInt(praiseCountStr);
            treadCount = Integer.parseInt(treadCountStr);
        }
        HashMap hashMap = new HashMap();
        hashMap.put("userId",userId);
        hashMap.put("contentId",comAnswerId);
        hashMap.put("typeId",2);
        try {
            PraiseTread praiseTread = praiseTreadMapper.selectByTypeIdConId(hashMap);
            PraiseTread praiseTreadIn = new PraiseTread();
            praiseTreadIn.setUserid(userId);
            praiseTreadIn.setContentid(comAnswerId);
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
            comAnswer.setPraisecount(String.valueOf(praiseCount));
            comAnswer.setTreadcount(String.valueOf(treadCount));
            result = comAnswerMapper.updateByPrimaryKeySelective(comAnswer);
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class,Exception.class})
    public int collectionComAnswer(Integer userId,Integer comAnswerId){
        int result = 0;
        ComAnswer comAnswer = comAnswerMapper.selectPraiseTreadCount(comAnswerId);
        if(comAnswer==null){
            return result;
        }
        Integer collectionCounts =  comAnswer.getCollectionedcounts();
        if(collectionCounts == null){
            collectionCounts = 0;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("userId",userId);
        hashMap.put("contentId",comAnswerId);
        hashMap.put("typeId",2);
        try {
            PraiseTread praiseTread = praiseTreadMapper.selectByTypeIdConId(hashMap);
            PraiseTread praiseTreadIn = new PraiseTread();
            praiseTreadIn.setUserid(userId);
            praiseTreadIn.setContentid(comAnswerId);
            praiseTreadIn.setTypeid(2);
            if(praiseTread == null){
                praiseTreadIn.setIscollection(true);
                collectionCounts += 1;
                praiseTreadMapper.insert(praiseTreadIn);
            }else {
                if(praiseTread.getIscollection() == null)
                {
                    praiseTreadIn.setIscollection(true);
                    collectionCounts += 1;
                }else if(praiseTread.getIscollection()==false){
                    praiseTreadIn.setIscollection(true);
                    collectionCounts += 1;
                }else if(praiseTread.getIscollection()==true){
                    praiseTreadIn.setIscollection(false);
                    collectionCounts -= 1;
                }
                praiseTreadMapper.updatePraiseTread(praiseTreadIn);
            }
            comAnswer.setCollectionedcounts(collectionCounts);
            result = comAnswerMapper.updateByPrimaryKeySelective(comAnswer);

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 添加答案的图片
     * @param imgFile
     * @param comAnswerId
     * @return
     * @throws FileNotFoundException
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class,Exception.class})
    public int addComAnswerImages(MultipartFile imgFile, Integer comAnswerId) throws FileNotFoundException {
        //先保存文件
        String fileName = null;
        String path = null;
        Callable<Object> task = new SaveImagesTask(imgFile,myConfig.getImagesArticlePath());
        Future<Object> taskResult = ThreadPoolUtil.submit(task);
        Map<String,Object> resultMap = SaveImagesTask.getSaveImgPath(taskResult);
        fileName = (String) resultMap.get("fileName");
        path = (String)resultMap.get("path");
        int comQuestionImgId = 0;
        if(!StringUtil.isEmpty(fileName)&&!StringUtil.isEmpty(path)){
            CommunityImages communityImages = new CommunityImages();
            communityImages.setOriginalname(imgFile.getOriginalFilename());
            communityImages.setFilename(fileName);
            communityImages.setComanswerid(comAnswerId);
            communityImages.setImgtype(imgFile.getContentType());
            communityImages.setFilepath(path);
            communityImagesMapper.insert(communityImages);
            comQuestionImgId = communityImages.getId();
        }
        return comQuestionImgId;
    }

    /**
     * 根据问题ID获取答案列表
     * @param comQuestionId
     * @param pageNum
     * @param pageSize
     * @return
     */

    @Override
    @Transactional(rollbackFor = {RuntimeException.class,Exception.class})
    public Map<String,Object> getComAnswerListByQueId(Integer comQuestionId,Integer pageNum,Integer pageSize){
        Map<String,Object> resultMap = new HashMap<>();
        PageHelper.startPage(pageNum, pageSize);
        List<ComAnswer> comAnswerList = comAnswerMapper.selectByComQuestionId(comQuestionId);
        List<ViewDetailVo> detailVoList = new ArrayList<>();
        for (ComAnswer comAnswer : comAnswerList)
        {
            detailVoList.add(getComAnswerDetail(comAnswer.getUserid(),comAnswer.getId()));
        }
        PageInfo<ViewDetailVo> pageInfo = new PageInfo<>(detailVoList);
        resultMap.put("total",pageInfo.getTotal());
        resultMap.put("list",pageInfo.getList());
        return resultMap;
    }

    @Override
    public ViewDetailVo getComAnswerDetail(Integer userId, Integer comAnswerId){
        return getAnswertDetail(userId,comAnswerId);
    }

    public ViewDetailVo getAnswertDetail(Integer userId, Integer comAnswerId){
        ComAnswer comAnswer = comAnswerMapper.selectByComAnswerId(comAnswerId);
        ViewDetailVo detailVo = new ViewDetailVo();
        if(comAnswer==null){
            return detailVo ;
        }
        detailVo.setTitle(comAnswer.getTitle());
        detailVo.setContent(comAnswer.getContent());
        detailVo.setPraisecount(comAnswer.getPraisecount());
        detailVo.setTreadcount(comAnswer.getTreadcount());
        detailVo.setArticleId(comAnswer.getId());
        detailVo.setCreateTime(comAnswer.getCreatetime());
        detailVo.setComQuestionId(comAnswer.getQuestionId());
        detailVo.setQuestionTitle(comAnswer.getQuestionTitle());
        detailVo.setQuestionContent(comAnswer.getQuestionContent());
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(comAnswer.getUserid());
        ViewDetailVo.setUserInfo(userInfo,detailVo);
        HashMap hashMap = new HashMap();
        hashMap.put("userId",userId);
        hashMap.put("contentId",comAnswerId);
        hashMap.put("typeId",2);
        PraiseTread praiseTread = praiseTreadMapper.selectByTypeIdConId(hashMap);
        PraiseTreadVo.detailPraiseTread(praiseTread,detailVo);
        Date createTimeDate = comAnswer.getCreatetime();
        String timeAgo = StringUtil.getTimeAgoAsString(createTimeDate);
        detailVo.setTimeAgo(timeAgo);
        String topicName = getTopicNameById(comAnswer.getTopicId());
        detailVo.setTopic(topicName);
        List<CommunityImages> imagesList = communityImagesMapper.selectCommunityImageListByQueId(comAnswer.getId());
        List<String> imageUrlList = new ArrayList<>();
        for (int i=0;i<imagesList.size();i++) {
            CommunityImages image = imagesList.get(i);
            imageUrlList.add(Constants.serverUrl()+ "comanswer/images/" +image.getFilename());
        }
        detailVo.setImageUrl(imageUrlList);
        return detailVo;
    }

    private String getTopicNameById (String  topicStr) {
        if("".equals(String.valueOf(topicStr)) || "null".equals(String.valueOf(topicStr))) {
            return "";
        }
        String name = "";
        String[] topicArr= topicStr.split(",");
        for(String str : topicArr)
        {
            ComAnswer topic = comAnswerMapper.selectByPrimaryKey(Integer.parseInt(str));
            if(topic != null){
                if(name == ""){
                    name = topic.getTitle();
                }else {
                    name = name + "," + topic.getTitle();
                }
            }
        }
        return name;
    }
//    private String getTopicNameById (Integer id) {
//        if("".equals(String.valueOf(id)) || "null".equals(String.valueOf(id))) {
//            return "";
//        }
//        ComAnswer topic = comAnswerMapper.selectByPrimaryKey(id);
//
//        String name = null;
//        if(topic!=null){
//            name = topic.getTitle();
//        }
//        return name;
//    }
}
