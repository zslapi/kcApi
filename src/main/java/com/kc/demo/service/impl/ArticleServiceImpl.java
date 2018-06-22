package com.kc.demo.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kc.demo.config.MyConfig;
import com.kc.demo.dao.ArticleImagesMapper;
import com.kc.demo.dao.ArticleMapper;
import com.kc.demo.dao.PraiseTreadMapper;
import com.kc.demo.dao.UserFollowMapper;
import com.kc.demo.jobs.SaveImagesTask;
import com.kc.demo.model.Article;
import com.kc.demo.model.ArticleImages;
import com.kc.demo.model.PraiseTread;
import com.kc.demo.model.UserFollow;
import com.kc.demo.service.ArticleService;
import com.kc.demo.util.Constants;
import com.kc.demo.util.StringUtil;
import com.kc.demo.util.ThreadPoolUtil;
import com.kc.demo.vo.ArticleDetailVo;
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
        addTimeAgoInList(articleList);
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
        for(UserFollow follow:followList){
            userIds.add(follow.getFollowuserid());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Article> articles = articleMapper.selectByUserIds(userIds);
        addTimeAgoInList(articles);
        PageInfo<Article> pageInfo = new PageInfo<>(articles);
        resultMap.put("total",pageInfo.getTotal());
        resultMap.put("list",pageInfo.getList());
        return resultMap;
    }

    /**
     * 在文章列表中增加timeAgo字段
     * @param articleList
     */
    private void addTimeAgoInList(List<Article> articleList) {
        for (Article article:articleList) {
            Date createTimeDate = article.getCreatetime();
            String timeAgo = StringUtil.getTimeAgoAsString(createTimeDate);
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
        PageInfo<Article> pageInfo = new PageInfo<>(articleList);
        resultMap.put("total",pageInfo.getTotal());
        resultMap.put("list",pageInfo.getList());
        return resultMap;
    }

    @Override
    public Map<String,Object> getArticleListByTopic(String topic,Integer pageNum,Integer pageSize) {
        Map<String,Object> resultMap = new HashMap<>();
        Article article = new Article();
        article.setTitle(topic);
        article.setArticletypeid(2);
        PageHelper.startPage(pageNum, pageSize);
        List<Article> articleList = articleMapper.selectByFilter(article);
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
        hashMap.put("articleId",articleId);
        try {
            PraiseTread praiseTread = praiseTreadMapper.selectByArticleId(hashMap);
            PraiseTread praiseTreadIn = new PraiseTread();
            praiseTreadIn.setUserid(userId);
            praiseTreadIn.setArticleid(articleId);
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
        hashMap.put("articleId",articleId);
        try {
            PraiseTread praiseTread = praiseTreadMapper.selectByArticleId(hashMap);
            PraiseTread praiseTreadIn = new PraiseTread();
            praiseTreadIn.setUserid(userId);
            praiseTreadIn.setArticleid(articleId);
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
    public ArticleDetailVo getArticleDetail(Integer articleId) {
        logger.info("测试一下：文章详情");
        Article article = articleMapper.selectByPrimaryKey(articleId);
        ArticleDetailVo detailVo = new ArticleDetailVo();
        detailVo.setTitle(article.getTitle());
        detailVo.setContent(article.getContent());
        detailVo.setPraiseCount(article.getPraisecount());
        detailVo.setTreadCount(article.getTreadcount());
        detailVo.setArticleId(article.getId());
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


    private String getTopicNameById (Integer id) {
        if("".equals(String.valueOf(id)) || "null".equals(String.valueOf(id))) {
            return "";
        }
        Article topic = articleMapper.selectByPrimaryKey(id);
        String name = topic.getTitle();
        return name;
    }


}
