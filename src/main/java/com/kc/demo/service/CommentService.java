package com.kc.demo.service;

import com.kc.demo.model.Comment;

public interface CommentService {
    int add(Comment record);

    int praiseComment(Integer commentId,Integer userId);

    int treadComment(Integer commentId,Integer userId);
}
