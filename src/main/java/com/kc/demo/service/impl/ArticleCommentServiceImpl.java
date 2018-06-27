package com.kc.demo.service.impl;

import com.github.pagehelper.PageInfo;
import com.kc.demo.dao.CommentMapper;
import com.kc.demo.model.Article;
import com.kc.demo.model.ComAnswer;
import com.kc.demo.model.Comment;
import com.kc.demo.service.ArticleCommentService;
import com.kc.demo.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleCommentServiceImpl implements ArticleCommentService {
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

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Map<String,Object> getCommentListByParentId(Integer userId, Integer parentId){
        Map<String,Object> resultMap = new HashMap<>();
        Comment comment = new Comment();
        comment.setUserid(userId);
        comment.setParentid(parentId);
        List<Comment> commentList = commentMapper.selectCommentListByparentId(comment);
//        for (Comment commentTemp : commentList){
//            commentTemp.setTypeid(0);
//        }
        PageInfo<Comment> pageInfo = new PageInfo<>(commentList);
        resultMap.put("total",pageInfo.getTotal());
        resultMap.put("list",pageInfo.getList());
        return resultMap;
    }

}
