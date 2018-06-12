package com.kc.demo.dao;

import com.kc.demo.model.ArticleImages;

import java.util.List;

public interface ArticleImagesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ArticleImages record);

    int insertSelective(ArticleImages record);

    ArticleImages selectByPrimaryKey(Integer id);

    List<ArticleImages> selectArticleImageList(Integer articleId);

    int updateByPrimaryKeySelective(ArticleImages record);

    int updateByPrimaryKey(ArticleImages record);
}