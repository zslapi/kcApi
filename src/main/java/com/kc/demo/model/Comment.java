package com.kc.demo.model;

import java.math.BigDecimal;
import java.util.Date;

public class Comment {
    private Integer id;

    private Integer articleid;

    private Integer userid;

    private String content;

    private Date createtime;

    private Integer status;

    private String ip;

    private String location;

    private String praiseuserids;

    private BigDecimal praisecount;

    private String treaduserids;

    private BigDecimal treadcount;

    private Integer parentid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getArticleid() {
        return articleid;
    }

    public void setArticleid(Integer articleid) {
        this.articleid = articleid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getPraiseuserids() {
        return praiseuserids;
    }

    public void setPraiseuserids(String praiseuserids) {
        this.praiseuserids = praiseuserids == null ? null : praiseuserids.trim();
    }

    public BigDecimal getPraisecount() {
        return praisecount;
    }

    public void setPraisecount(BigDecimal praisecount) {
        this.praisecount = praisecount;
    }

    public String getTreaduserids() {
        return treaduserids;
    }

    public void setTreaduserids(String treaduserids) {
        this.treaduserids = treaduserids == null ? null : treaduserids.trim();
    }

    public BigDecimal getTreadcount() {
        return treadcount;
    }

    public void setTreadcount(BigDecimal treadcount) {
        this.treadcount = treadcount;
    }

    public Integer getParentid() {
        return parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }
}