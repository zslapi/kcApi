package com.kc.demo.service;

import com.kc.demo.model.Article;
import com.kc.demo.vo.ViewDetailVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.Map;

public interface ArticleService {
    int add(Article record);

    Map<String,Object> getArticleListByArticleTypeId(Integer articleTypeId, Integer pageNum, Integer pageSize);

    Map<String,Object> getUserFollowArticles(Integer userId,Integer pageNum,Integer pageSize);

    Map<String,Object> getArticleListByTitleKey (String titleKey,Integer pageNum,Integer pageSize);

    Map<String,Object> getArticleListByTopic (String topic,Integer pageNum,Integer pageSize);//获取话题列表

    int praiseArticle(Integer userId,Integer articleId);

    int treadArticle(Integer userId,Integer articleId);

    int addArticleImages(MultipartFile imgFile, Integer articleId) throws FileNotFoundException;

    ViewDetailVo getArticleDetail(Integer useId,Integer articleId);
}
