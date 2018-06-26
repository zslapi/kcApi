package com.kc.demo.model;

import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.util.Date;

public class ComQuestion {
    private Integer id;

    private Integer comQuestionId;

    private String title;

    private String content;

    private Integer userid;

    private Integer questiontypeid;

    private Integer typeid;

    private Integer questionstatus;

    private String ip;

    private String location;

    private Integer collectionedcounts;

    private String praisecount;

    private String treadcount;

    private String nickname;

    private String headimageurl;

    private Date createtime;

    private String topicid;

    private String timeAgo;

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public Integer getComQuestionIdId() { return comQuestionId; }

    public void setComQuestionId(Integer comQuestionId) { this.comQuestionId = comQuestionId; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getQuestiontypeid() {
        return questiontypeid;
    }

    public void setQuestiontypeid(Integer questiontypeid) {
        this.questiontypeid = questiontypeid;
    }

    public Integer getQuestionstatus() {
        return questionstatus;
    }

    public void setQuestionstatus(Integer questionstatus) {
        this.questionstatus = questionstatus;
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

    public Integer getCollectionedcounts() {
        return collectionedcounts;
    }

    public void setCollectionedcounts(Integer collectionedcounts) {
        this.collectionedcounts = collectionedcounts;
    }

    public String getPraisecount() {
        return praisecount;
    }

    public void setPraisecount(String praisecount) {
        this.praisecount = praisecount == null ? null : praisecount.trim();
    }

    public String getTreadcount() {
        return treadcount;
    }

    public void setTreadcount(String treadcount) {
        this.treadcount = treadcount == null ? null : treadcount.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getTopicid() {return topicid;}

    public void setTopicid(String topicid) {this.topicid = topicid;}

    public String getTimeAgo() {return timeAgo;}

    public void setTimeAgo(String timeAgo) {this.timeAgo = timeAgo;}

    public Integer getTypeid() {
        return typeid;
    }

    public void setTypeid(Integer typeid) {
        this.typeid = typeid;
    }

    public String getNickname(){return  nickname;}

    public void setNickname(String nickname){this.nickname = nickname;}

    public String getHeadimageurl(){return headimageurl;}

    public void setHeadimageurl(String headimageurl){this.headimageurl = headimageurl;}
}
