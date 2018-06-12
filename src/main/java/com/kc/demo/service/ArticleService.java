package com.kc.demo.service;

import com.kc.demo.model.Article;
import com.kc.demo.vo.ArticleDetailVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;

public interface ArticleService {
    int add(Article record);

    List<Article> getArticleListByArticleTypeId(Integer articleTypeId,Integer pageNum,Integer pageSize);

    List<Article> getUserFollowArticles(Integer userId,Integer pageNum,Integer pageSize);

    List<Article> getArticleListByTitleKey (String titleKey,Integer pageNum,Integer pageSize);

    List<Article> getArticleListByTopic (String topic,Integer pageNum,Integer pageSize);//获取话题列表

    int praiseArticle(Integer articleId);

    int treadArticle(Integer articleId);

    int addArticleImages(MultipartFile imgFile, Integer articleId) throws FileNotFoundException;

    ArticleDetailVo getArticleDetail(Integer articleId);
}
