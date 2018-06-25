package com.kc.demo.dao;

import com.kc.demo.model.ComAnswer;

import java.util.List;

public interface ComAnswerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ComAnswer record);

    int insertSelective(ComAnswer record);

    ComAnswer selectByPrimaryKey(Integer id);

    List<ComAnswer> selectByComQuestionId(Integer comQuestionId);

    ComAnswer selectMaxCountsByQueId(Integer comQuestionId);

    int updateByPrimaryKeySelective(ComAnswer record);

    int updateByPrimaryKey(ComAnswer record);

    ComAnswer selectPraiseCount(Integer comAnswerId);

    ComAnswer selectTreadCount(Integer comAnswerId);

    ComAnswer selectPraiseTreadCount(Integer comAnswerId);

    ComAnswer selectByComAnswerId(Integer comAnswerId);
}
