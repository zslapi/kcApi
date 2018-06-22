package com.kc.demo.dao;

import com.kc.demo.model.ComQuestion;

import java.util.List;

public interface ComQuestionMapper {

    int insert(ComQuestion record);

    int insertSelective(ComQuestion record);

    ComQuestion selectByPrimaryKey(Integer id);

    List<ComQuestion> selectByFilter(ComQuestion article);

    List<ComQuestion> selectByUserIds(List userIds);

    List<ComQuestion> selectListByQuestionTypeId(Integer articleTypeId);

    int updateByPrimaryKeySelective(ComQuestion record);

    ComQuestion selectPraiseTreadCount(Integer id);

    int selectCountByUserId(Integer userId);
}
