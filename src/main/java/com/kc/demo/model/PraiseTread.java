package com.kc.demo.model;

public class PraiseTread {
    private Integer id;

    private Integer userid;

    private Integer articleid;

    private Integer commentid;

    private Boolean ispraise;

    private Boolean istread;

    public Integer getId(){ return this.id; }

    public void setId(Integer id){ this.id = id; }

    public Integer getUserid(){ return this.userid; }

    public void setUserid(Integer userid){ this.userid = userid; }

    public Integer getArticleid(){ return this.articleid; }

    public void setArticleid(Integer articleid){ this.articleid = articleid; }

    public Integer getCommentid(){ return this.commentid; }

    public void setCommentid(Integer commentid){ this.commentid = commentid; }

    public Boolean getIspraise() { return ispraise; }

    public void setIspraise(Boolean ispraise) { this.ispraise = ispraise; }

    public Boolean getIstread() { return istread; }

    public void setIstread(Boolean istread) { this.istread = istread; }
}
