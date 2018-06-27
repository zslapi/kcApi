package com.kc.demo.service;

import com.kc.demo.model.Comment;
import java.util.Map;

public interface ArticleCommentService {
    int add(Comment record);

    Map<String,Object> getCommentListByParentId(Integer userId,Integer parentId);

    int praiseComment(Integer commentId,Integer userId);

    int treadComment(Integer commentId,Integer userId);
}
