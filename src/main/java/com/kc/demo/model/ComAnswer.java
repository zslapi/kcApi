package com.kc.demo.model;

import java.math.BigDecimal;
import java.util.Date;

public class ComAnswer {
    private Integer id;

    private String title;

    private String content;

    private Integer comquestionid;

    private Integer questiontypeid;

    private Integer questionstatus;

    private String ip;

    private Integer userid;

    private Date createtime;

    private Integer topicid;

    private String location;

    private String praisecount;

    private String treadcount;

    private Integer collectionedcounts;

    private String usernickname;

    private String userheadimageurl;

    private Integer questionid;

    private String questiontitle;

    private String questioncontent;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getComquestionid() {
        return comquestionid;
    }

    public void setComquestionid(Integer comquestionid) {
        this.comquestionid = comquestionid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getTitle(){ return title;}

    public void setTitle(String title) { this.title = title;}

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getQuestionTypeid() {
        return questiontypeid;
    }

    public void setQuestionTypeid(Integer questiontypeid) {
        this.questiontypeid = questiontypeid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

    public Integer getQuestionstatus() {
        return questionstatus;
    }

    public void setQuestionstatus(Integer questionstatus) {
        this.questionstatus = questionstatus;
    }

    public String getPraisecount() {
        return praisecount;
    }

    public void setPraisecount(String praisecount) {
        this.praisecount = praisecount;
    }

    public Integer getTopicId() {
        return topicid;
    }

    public void setTopicId(Integer topicid) {
        this.topicid = topicid;
    }

    public String getTreadcount() {
        return treadcount;
    }

    public void setTreadcount(String treadcount) {
        this.treadcount = treadcount;
    }

    public Integer getCollectionedcounts() {
        return collectionedcounts;
    }

    public void setCollectionedcounts(Integer collectionedcounts) {
        this.collectionedcounts = collectionedcounts;
    }

    public String getUserNickName(){return  usernickname;}

    public void setUserNickName(String userNickName){this.usernickname = userNickName;}

    public String getUserHeadImageUrl(){return userheadimageurl;}

    public void setUserHeadImageUrl(String userHeadImageUrl){this.userheadimageurl = userHeadImageUrl;}

    public String getQuestionContent() {
        return questioncontent;
    }

    public void setQuestionContent(String questionContent) {
        this.questioncontent = questionContent;
    }

    public String getQuestionTitle() {
        return questiontitle;
    }
    public void setQuestionTitle(String questionTitle) {
        this.questiontitle = questionTitle;
    }
    public void setQuestionId(Integer questionId) {
        this.questionid = questionId;
    }

    public Integer getQuestionId() {
        return questionid;
    }

    public Integer getQuestiontypeid() {
        return questiontypeid;
    }

    public void setQuestiontypeid(Integer questiontypeid) {
        this.questiontypeid = questiontypeid;
    }
    public Integer getTopicid() {
        return topicid;
    }

    public void setTopicid(Integer topicid) {
        this.topicid = topicid;
    }

}
