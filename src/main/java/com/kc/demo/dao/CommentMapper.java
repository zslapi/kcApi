package com.kc.demo.dao;

import com.kc.demo.model.Comment;

import java.util.List;

public interface CommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    Comment selectPraiseCount(Integer comAnswerId);

    Comment selectTreadCount(Integer comAnswerId);

    List<Comment> selectCommentListByparentId(Comment comment);
}