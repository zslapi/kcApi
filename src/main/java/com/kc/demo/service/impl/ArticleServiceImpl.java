package com.kc.demo.service.impl;

import com.kc.demo.dao.ArticleImagesMapper;
import com.kc.demo.dao.ArticleMapper;
import com.kc.demo.dao.UserFollowMapper;
import com.kc.demo.model.Article;
import com.kc.demo.model.ArticleImages;
import com.kc.demo.model.UserFollow;
import com.kc.demo.service.ArticleService;
import com.kc.demo.util.Constants;
import com.kc.demo.util.ImageUtil;
import com.kc.demo.util.StringUtil;
import com.kc.demo.vo.ArticleDetailVo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.*;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private UserFollowMapper userFollowMapper;

    @Resource
    private ArticleImagesMapper articleImagesMapper;

    @Override
    public int add(Article record) {
        articleMapper.insert(record);
        int id = record.getId();
        return id;
    }

    @Override
    public List<Article> getArticleListByArticleTypeId(Integer articleTypeId) {
//        Article a = new Article();
//        a.setArticletypeid(articleTypeId);
        List<Article> articleList = articleMapper.selectListByArticleTypeId(articleTypeId);
        addTimeAgoInList(articleList);
        return articleList;
    }

    @Override
    public List<Article> getUserFollowArticles(Integer userId) {
        List<UserFollow> followList = userFollowMapper.selectByUserId(userId);
        List<Integer> userIds = new ArrayList<>();
        for(UserFollow follow:followList){
            userIds.add(follow.getFollowuserid());
        }
        List<Article> articles = articleMapper.selectByUserIds(userIds);
        addTimeAgoInList(articles);
        return articles;
    }

    private void addTimeAgoInList(List<Article> articleList) {
        for (Article article:articleList) {
            Date createTimeDate = article.getCreatetime();
            String timeAgo = getTimeAgoAsString(createTimeDate);
            article.setTimeAgo(timeAgo);
        }
    }

    @Override
    public List<Article> getArticleListByTitleKey(String titleKey) {
        Article article = new Article();
        article.setTitle(titleKey);
        return articleMapper.selectByFilter(article);
    }

    @Override
    public List<Article> getArticleListByTopic(String topic) {
        Article article = new Article();
        article.setTitle(topic);
        article.setArticletypeid(2);
        return articleMapper.selectByFilter(article);
    }

    @Override
    public int praiseArticle(Integer articleId) {
        Article article = articleMapper.selectPraiseCount(articleId);
        if(article==null){
            return 0;
        }
        String praiseCount =  article.getPraisecount();
        int count = 0;
        if(!StringUtil.isEmpty(praiseCount)){
            count = Integer.parseInt(praiseCount);
        }
        count = count + 1;
        article.setPraisecount(String.valueOf(count));
        return articleMapper.updateByPrimaryKeySelective(article);
    }

    @Override
    public int treadArticle(Integer articleId) {
        Article article = articleMapper.selectThreadCount(articleId);
        if(article==null){
            return 0;
        }
        String treadcount = article.getTreadcount();
        int count = 0;
        if(!StringUtil.isEmpty(treadcount)){
            count = Integer.parseInt(treadcount);
        }
        count = count + 1;
        article.setTreadcount(String.valueOf(count));
        return articleMapper.updateByPrimaryKeySelective(article);
    }

    @Override
    public int addArticleImages(MultipartFile imgFile, String fileName, Integer articleId, String path) {
        ArticleImages articleImages = new ArticleImages();

        articleImages.setOriginalname(imgFile.getOriginalFilename());
        articleImages.setFilename(fileName);
        articleImages.setArticleid(articleId);
        articleImages.setImgtype(imgFile.getContentType());
        articleImages.setFilepath(path);
        return articleImagesMapper.insert(articleImages);
    }

    @Override
    public ArticleDetailVo getArticleDetail(Integer articleId) {
        Article article = articleMapper.selectByPrimaryKey(articleId);
        ArticleDetailVo detailVo = new ArticleDetailVo();
        detailVo.setTitle(article.getTitle());
        detailVo.setContent(article.getContent());
        detailVo.setPraiseCount(article.getPraisecount());
        detailVo.setTreadCount(article.getTreadcount());
        detailVo.setArticleId(article.getId());
        Date createTimeDate = article.getCreatetime();
        String timeAgo = getTimeAgoAsString(createTimeDate);
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

    private String getTopicNameById (Integer id) {
        if("".equals(String.valueOf(id)) || "null".equals(String.valueOf(id))) {
            return "";
        }
        Article topic = articleMapper.selectByPrimaryKey(id);
        String name = topic.getTitle();
        return name;
    }


}
