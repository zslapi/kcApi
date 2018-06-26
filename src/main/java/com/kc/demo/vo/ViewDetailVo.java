package com.kc.demo.vo;

import com.kc.demo.model.ComAnswer;
import com.kc.demo.model.CommunityImages;
import com.kc.demo.model.PraiseTread;
import com.kc.demo.model.UserInfo;
import com.kc.demo.util.Constants;
import com.kc.demo.util.StringUtil;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ViewDetailVo {

    private String title;

    private String content;

    private List imageUrl;

    private Integer articleId;

    private Integer comQuestionId;

    private Integer comAnswerId;

    protected Integer comCommentId;

    private String nickname;

    private String headimageurl;

    private String topic;

    private String timeAgo;

    private Date createTime;

    private String praiseCount;

    private String treadCount;

    private Boolean ispraise;

    private Boolean isTread;

    private Boolean iscollection;

    private String questionTitle;

    private String questionContent;

    private HashMap<String,Object> answerMap;

    public HashMap<String, Object> getAnswerMap() {
        return answerMap;
    }

    public void setAnswerMap(HashMap<String, Object> answerMap) {
        this.answerMap = answerMap;
    }

    public Boolean getIspraise() {
        return this.ispraise;
    }

    public void setIspraise(boolean ispraise) {
        this.ispraise = ispraise ;
    }

    public Boolean getIsTread() {
        return this.isTread;
    }

    public void setIsTread(boolean isTread) {
        this.isTread = isTread;
    }


    public String getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(String praiseCount) {
        this.praiseCount = praiseCount;
    }

    public String getTreadCount() {
        return treadCount;
    }

    public void setTreadCount(String treadCount) {
        this.treadCount = treadCount;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public List getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(List imageUrl) {
        this.imageUrl = imageUrl;
    }


    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Integer getComQuestionId(){ return comQuestionId; }

    public void setComQuestionId(Integer comQuestionId) { this.comQuestionId = comQuestionId ;}

    public Integer getComAnswerId() { return  comAnswerId ; }

    public void setComAnswerId(Integer comAnswerId){ this.comAnswerId = comAnswerId;}

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

    public Date getCreateTime() { return createTime;}

    public void setCreateTime(Date createTime) {this.createTime = createTime;}

    public String getNickname() {return nickname;}

    public void setNickname(String nickname) { this.nickname = nickname;}

    public String getHeadimageurl() {return headimageurl;}

    public void setHeadimageUrl(String headimageurl) { this.headimageurl = headimageurl;}

    public static void setUserInfo(UserInfo userInfo,ViewDetailVo detailVo){
        if(userInfo!=null){
            if(userInfo.getNickname()!=null && userInfo.getHeadimageurl()!=null){
                detailVo.setNickname(userInfo.getNickname());
                detailVo.setHeadimageUrl(userInfo.getHeadimageurl());
            }
        }else {
            detailVo.setNickname(null);
            detailVo.setHeadimageUrl(null);
        }
    }

    public String getQuestionTitle(){return questionTitle;}

    public void setQuestionTitle(String questionTitle){this.questionTitle = questionTitle;}

    public String getQuestionContent(){return this.questionContent;}

    public void setQuestionContent(String questionContent){this.questionContent = questionContent;}

    public Boolean getIscollection() {
        return iscollection;
    }

    public void setIscollection(Boolean iscollection) {
        this.iscollection = iscollection;
    }
}
