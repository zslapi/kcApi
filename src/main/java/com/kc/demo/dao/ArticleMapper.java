package com.kc.demo.dao;

import com.kc.demo.model.Article;

import java.util.List;

public interface ArticleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Article record);

    int insertSelective(Article record);

    Article selectByPrimaryKey(Integer id);

    Article selectArticleDetailByPrimaryKey(Integer id);

    List<Article> selectByFilter(Article article);

    List<Article> selectByUserIds(List userIds);

    List<Article> selectListByArticleTypeId(Integer articleTypeId);

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKeyWithBLOBs(Article record);

    int updateByPrimaryKey(Article record);

    Article selectPraiseTreadCount(Integer id);

    int selectCountByUserId(Integer userId);
}