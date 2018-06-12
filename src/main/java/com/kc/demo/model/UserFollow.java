package com.kc.demo.model;

import java.util.Date;

public class UserFollow {
    private Integer id;

    private Integer userid;

    private Integer followuserid;

    private Date followtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getFollowuserid() {
        return followuserid;
    }

    public void setFollowuserid(Integer followuserid) {
        this.followuserid = followuserid;
    }

    public Date getFollowtime() {
        return followtime;
    }

    public void setFollowtime(Date followtime) {
        this.followtime = followtime;
    }


}