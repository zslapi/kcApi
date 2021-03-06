package com.kc.demo.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kc.demo.config.MyConfig;
import com.kc.demo.dao.*;
import com.kc.demo.jobs.SaveImagesTask;
import com.kc.demo.model.*;
import com.kc.demo.model.Dictionary;
import com.kc.demo.service.ArticleService;
import com.kc.demo.util.Constants;
import com.kc.demo.util.StringUtil;
import com.kc.demo.util.ThreadPoolUtil;
import com.kc.demo.vo.PraiseTreadVo;
import com.kc.demo.vo.ViewDetailVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Service
public class ArticleServiceImpl implements ArticleService {

    private Logger logger = LogManager.getLogger(ArticleServiceImpl.class);

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private UserFollowMapper userFollowMapper;

    @Resource
    private ArticleImagesMapper articleImagesMapper;

    @Resource
    private PraiseTreadMapper praiseTreadMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private DictionaryMapper dictionaryMapper;

    @Resource
    private MyConfig myConfig;

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public int add(Article record) {
        articleMapper.insert(record);
        int id = record.getId();
        return id;
    }

    /**
     * 根据文章类型获取文章列表
     * @param articleTypeId
     * @return
     */
    @Override
    public Map<String,Object> getArticleListByArticleTypeId(Integer articleTypeId,Integer pageNum,Integer pageSize) {
        Map<String,Object> resultMap = new HashMap<>();
        PageHelper.startPage(pageNum, pageSize);
        List<Article> articleList = articleMapper.selectListByArticleTypeId(articleTypeId);
        for (Article article : articleList){
            article.setTypeid(0);
            getArticleImagesUrl(article);
        }
        addRemainFieldInList(articleList);
        PageInfo<Article> pageInfo = new PageInfo<>(articleList);
        resultMap.put("total",pageInfo.getTotal());
        resultMap.put("list",pageInfo.getList());
        return resultMap;
    }

    @Override
    public Map<String,Object> getUserFollowArticles(Integer userId,Integer pageNum,Integer pageSize) {
        Map<String,Object> resultMap = new HashMap<>();
        List<UserFollow> followList = userFollowMapper.selectByUserId(userId);
        List<Integer> userIds = new ArrayList<>();
        if(followList!=null && followList.size()!=0) {
            for(UserFollow follow:followList){
                userIds.add(follow.getFollowuserid());
            }
            PageHelper.startPage(pageNum, pageSize);
            List<Article> articles = articleMapper.selectByUserIds(userIds);
            for (Article article : articles){
                article.setTypeid(0);
                getArticleImagesUrl(article);
            }
            addRemainFieldInList(articles);
            PageInfo<Article> pageInfo = new PageInfo<>(articles);
            resultMap.put("total",pageInfo.getTotal());
            resultMap.put("list",pageInfo.getList());
        }

        return resultMap;
    }


    private void addRemainFieldInList(List<Article> articleList) {
        for (Article article:articleList) {
            Date createTimeDate = article.getCreatetime();
            String timeAgo = StringUtil.getTimeAgoAsString(createTimeDate);
            article.setContentid(0);
            article.setTimeAgo(timeAgo);
        }
    }

    @Override
    public Map<String,Object> getArticleListByTitleKey(String titleKey,Integer pageNum,Integer pageSize) {
        Map<String,Object> resultMap = new HashMap<>();
        Article article = new Article();
        article.setTitle(titleKey);
        PageHelper.startPage(pageNum, pageSize);
        List<Article> articleList = articleMapper.selectByFilter(article);
        for (Article articleTemp : articleList){
            articleTemp.setTypeid(0);
            getArticleImagesUrl(articleTemp);
        }
        addRemainFieldInList(articleList);
        PageInfo<Article> pageInfo = new PageInfo<>(articleList);
        resultMap.put("total",pageInfo.getTotal());
        resultMap.put("list",pageInfo.getList());
        return resultMap;
    }

    private void getArticleImagesUrl(Article articleTemp){
        List<ArticleImages> imagesList = articleImagesMapper.selectArticleImageList(articleTemp.getId());
        String imageUrl = null;
        if(imagesList !=null  && imagesList.size()>0){
            ArticleImages image = imagesList.get(0);
            imageUrl = Constants.serverUrl()+ "article/images/" +image.getFilename();
        }
        articleTemp.setImageurl(imageUrl);
    }

    @Override
    public Map<String,Object> getArticleListByTopic(String topic,Integer pageNum,Integer pageSize) {
        Map<String,Object> resultMap = new HashMap<>();
        Article article = new Article();
        article.setTitle(topic);
        article.setArticletypeid(2);
        PageHelper.startPage(pageNum, pageSize);
        List<Article> articleList = articleMapper.selectByFilter(article);
        for (Article articleTemp : articleList){
            articleTemp.setTypeid(0);
            getArticleImagesUrl(articleTemp);
        }
        addRemainFieldInList(articleList);
        PageInfo<Article> pageInfo = new PageInfo<>(articleList);
        resultMap.put("total",pageInfo.getTotal());
        resultMap.put("list",pageInfo.getList());
        return resultMap;
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public int praiseArticle(Integer userId,Integer articleId) {
        int result = 0;
        Article article = articleMapper.selectPraiseTreadCount(articleId);
        if(article==null){
            return result;
        }
        String praiseCountStr =  article.getPraisecount();
        String treadCountStr = article.getTreadcount();
        int praiseCount = 0;
        int treadCount = 0;
        if(!StringUtil.isEmpty(praiseCountStr) && !StringUtil.isEmpty(treadCountStr)){
            praiseCount = Integer.parseInt(praiseCountStr);
            treadCount = Integer.parseInt(treadCountStr);
        }
        HashMap hashMap = new HashMap();
        hashMap.put("userId",userId);
        hashMap.put("contentId",articleId);
        hashMap.put("typeId",0);
        try {
            PraiseTread praiseTread = praiseTreadMapper.selectByTypeIdConId(hashMap);
            PraiseTread praiseTreadIn = new PraiseTread();
            praiseTreadIn.setUserid(userId);
            praiseTreadIn.setContentid(articleId);
            praiseTreadIn.setTypeid(0);
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
            article.setPraisecount(String.valueOf(praiseCount));
            article.setTreadcount(String.valueOf(treadCount));
            result = articleMapper.updateByPrimaryKeySelective(article);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public int treadArticle(Integer userId,Integer articleId) {
        int result = 0;
        Article article = articleMapper.selectPraiseTreadCount(articleId);
        if(article==null){
            return result;
        }
        String praiseCountStr =  article.getPraisecount();
        String treadCountStr = article.getTreadcount();
        int praiseCount = 0;
        int treadCount = 0;
        if(!StringUtil.isEmpty(praiseCountStr) && !StringUtil.isEmpty(treadCountStr)){
            praiseCount = Integer.parseInt(praiseCountStr);
            treadCount = Integer.parseInt(treadCountStr);
        }
        HashMap hashMap = new HashMap();
        hashMap.put("userId",userId);
        hashMap.put("contentId",articleId);
        hashMap.put("typeId",0);
        try {
            PraiseTread praiseTread = praiseTreadMapper.selectByTypeIdConId(hashMap);
            PraiseTread praiseTreadIn = new PraiseTread();
            praiseTreadIn.setUserid(userId);
            praiseTreadIn.setContentid(articleId);
            praiseTreadIn.setTypeid(0);
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
            article.setPraisecount(String.valueOf(praiseCount));
            article.setTreadcount(String.valueOf(treadCount));
            result = articleMapper.updateByPrimaryKeySelective(article);
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class,Exception.class})
    public int collectionArticle(Integer userId,Integer articleId){
        int result = 0;
        Article article = articleMapper.selectPraiseTreadCount(articleId);
        if(article==null){
            return result;
        }
        Integer collectionCounts =  article.getCollectionedcounts();
        if(collectionCounts == null){
            collectionCounts = 0;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("userId",userId);
        hashMap.put("contentId",articleId);
        hashMap.put("typeId",0);
        try {
            PraiseTread praiseTread = praiseTreadMapper.selectByTypeIdConId(hashMap);
            PraiseTread praiseTreadIn = new PraiseTread();
            praiseTreadIn.setUserid(userId);
            praiseTreadIn.setContentid(articleId);
            praiseTreadIn.setTypeid(0);
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
            article.setCollectionedcounts(collectionCounts);
            result = articleMapper.updateByPrimaryKeySelective(article);

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public int addArticleImages(MultipartFile imgFile, Integer articleId) {
        //先保存文件
        String fileName = null;
        String path = null;
        Callable<Object> task = new SaveImagesTask(imgFile,myConfig.getImagesArticlePath());
        Future<Object> taskResult = ThreadPoolUtil.submit(task);
        Map<String,Object> resultMap =  SaveImagesTask.getSaveImgPath(taskResult);
        fileName = (String)resultMap.get("fileName");
        path = (String)resultMap.get("path");
        int articleImgId = 0;
        if(!StringUtil.isEmpty(fileName)&&!StringUtil.isEmpty(path)){
            ArticleImages articleImages = new ArticleImages();

            articleImages.setOriginalname(imgFile.getOriginalFilename());
            articleImages.setFilename(fileName);
            articleImages.setArticleid(articleId);
            articleImages.setImgtype(imgFile.getContentType());
            articleImages.setFilepath(path);
            articleImagesMapper.insert(articleImages);
            articleImgId = articleImages.getId();

        }
        return articleImgId;
    }

    @Override
    public ViewDetailVo getArticleDetail(Integer userId,Integer articleId) {
        logger.info("测试一下：文章详情");
        Article article = articleMapper.selectByPrimaryKey(articleId);
        if(article == null)
            return null;
        ViewDetailVo detailVo = new ViewDetailVo();
        detailVo.setTitle(article.getTitle());
        detailVo.setContent(article.getContent());
        detailVo.setPraisecount(article.getPraisecount());
        detailVo.setTreadcount(article.getTreadcount());
        detailVo.setArticleId(article.getId());
        detailVo.setCreateTime(article.getCreatetime());
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(article.getUserid());
        ViewDetailVo.setUserInfo(userInfo,detailVo);
        HashMap hashMap = new HashMap();
        hashMap.put("userId",userId);
        hashMap.put("contentId",articleId);
        hashMap.put("typeId",0);
        PraiseTread praiseTread = praiseTreadMapper.selectByTypeIdConId(hashMap);
        PraiseTreadVo.detailPraiseTread(praiseTread,detailVo);
        Date createTimeDate = article.getCreatetime();
        String timeAgo = StringUtil.getTimeAgoAsString(createTimeDate);
        detailVo.setTimeAgo(timeAgo);
        String topicName = getTopicNameById(article.getTopicid());
        detailVo.setTopic(topicName);
        List<ArticleImages> imagesList = articleImagesMapper.selectArticleImageList(article.getId());
        List<String> imageUrlList = new ArrayList<>();
        for (int i=0;i<imagesList.size();i++) {
            ArticleImages image = imagesList.get(i);
            imageUrlList.add(Constants.serverUrl()+"article/images/"+image.getFilename());
        }
        detailVo.setImageUrl(imageUrlList);
        return detailVo;
    }


    private String getTopicNameById (String topicStr) {
        if("".equals(String.valueOf(topicStr)) || "null".equals(String.valueOf(topicStr))) {
            return "";
        }
        String name = "";
        String[] topicArr= topicStr.split(",");
        for(String str : topicArr)
        {
//            Article topic = articleMapper.selectByPrimaryKey(Integer.parseInt(str));
            Dictionary dictionary = dictionaryMapper.selectByPrimaryKey(Integer.parseInt(str));
            if(dictionary != null){
                if(name == ""){
                    name = dictionary.getName();
                }else {
                    name = name + "," + dictionary.getName();
                }
            }
        }
        return name;
    }




}
