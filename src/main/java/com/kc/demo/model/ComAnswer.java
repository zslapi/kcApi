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

    private BigDecimal praisecount;

    private BigDecimal treadcount;

    private Integer collectionedcounts;


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

    public Integer getQuestiontypeid() {
        return questiontypeid;
    }

    public void setQuestiontypeid(Integer questiontypeid) {
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

    public BigDecimal getPraisecount() {
        return praisecount;
    }

    public void setPraisecount(BigDecimal praisecount) {
        this.praisecount = praisecount;
    }

    public Integer getTopicid() {
        return topicid;
    }

    public void setTopicid(Integer topicid) {
        this.topicid = topicid;
    }

    public BigDecimal getTreadcount() {
        return treadcount;
    }

    public void setTreadcount(BigDecimal treadcount) {
        this.treadcount = treadcount;
    }

    public Integer getCollectionedcounts() {
        return collectionedcounts;
    }

    public void setCollectionedcounts(Integer collectionedcounts) {
        this.collectionedcounts = collectionedcounts;
    }
}
