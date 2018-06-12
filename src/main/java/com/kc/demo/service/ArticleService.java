package com.kc.demo.service;

import com.kc.demo.model.Article;
import com.kc.demo.vo.ArticleDetailVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;

public interface ArticleService {
    int add(Article record);

    List<Article> getArticleListByArticleTypeId(Integer articleTypeId);

    List<Article> getUserFollowArticles(Integer userId);

    List<Article> getArticleListByTitleKey (String titleKey);

    List<Article> getArticleListByTopic (String topic);//获取话题列表

    int praiseArticle(Integer articleId);

    int treadArticle(Integer articleId);

    int addArticleImages(MultipartFile imgFile, String fileName, Integer articleId, String path) throws FileNotFoundException;

    ArticleDetailVo getArticleDetail(Integer articleId);
}
