package com.kc.demo.service.impl;

import com.kc.demo.dao.CommentMapper;
import com.kc.demo.model.Comment;
import com.kc.demo.service.CommentService;
import com.kc.demo.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class CommentServiceImpl implements CommentService {
    @Resource
    private CommentMapper commentMapper;

    @Override
    public int add(Comment record) {
        return commentMapper.insert(record);
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public int praiseComment(Integer commentId,Integer userId) {
        Comment comment = commentMapper.selectPraiseCount(commentId);
        if(comment==null){
            return 0;
        }
        BigDecimal praiseCount =  comment.getPraisecount();
        if(praiseCount == null){
            praiseCount = BigDecimal.ZERO;
        }
        praiseCount = praiseCount.add(BigDecimal.ONE);
        comment.setPraisecount(praiseCount);

        String praiseUserIds = comment.getPraiseuserids();
        if(StringUtil.isEmpty(praiseUserIds)){
            praiseUserIds = ""+userId;
        } else {
            praiseUserIds = praiseUserIds + "," + userId;
        }
        comment.setPraiseuserids(praiseUserIds);
        return commentMapper.updateByPrimaryKeySelective(comment);
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public int treadComment(Integer commentId,Integer userId) {
        Comment comment = commentMapper.selectTreadCount(commentId);
        if(comment==null){
            return 0;
        }
        BigDecimal treadCount =  comment.getTreadcount();
        if(treadCount == null){
            treadCount = BigDecimal.ZERO;
        }
        treadCount = treadCount.add(BigDecimal.ONE);
        comment.setTreadcount(treadCount);

        String treadUserIds = comment.getTreaduserids();
        if(StringUtil.isEmpty(treadUserIds)){
            treadUserIds = ""+userId;
        } else {
            treadUserIds = treadUserIds + "," + userId;
        }
        comment.setTreaduserids(treadUserIds);
        return commentMapper.updateByPrimaryKeySelective(comment);
    }
}
