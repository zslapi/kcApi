package com.kc.demo.service;

import com.github.pagehelper.PageInfo;
import com.kc.demo.model.Article;
import com.kc.demo.vo.ArticleDetailVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;
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

    ArticleDetailVo getArticleDetail(Integer articleId);
}
