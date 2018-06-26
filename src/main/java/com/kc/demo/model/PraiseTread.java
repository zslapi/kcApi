package com.kc.demo.model;

public class PraiseTread {
    private Integer id;

    private Integer userid;

    private Integer contentid;

    private Integer typeid;

    private Boolean ispraise;

    private Boolean istread;

    private Boolean iscollection;

    public Integer getId(){ return this.id; }

    public void setId(Integer id){ this.id = id; }

    public Integer getUserid(){ return this.userid; }

    public void setUserid(Integer userid){ this.userid = userid; }

    public Integer getContentid(){ return this.contentid; }

    public void setContentid(Integer contentid){ this.contentid = contentid; }

    public Integer getTypeid(){ return this.typeid; }

    public void setTypeid(Integer typeid){ this.typeid = typeid; }

    public Boolean getIspraise() { return ispraise; }

    public void setIspraise(Boolean ispraise) { this.ispraise = ispraise; }

    public Boolean getIstread() { return istread; }

    public void setIstread(Boolean istread) { this.istread = istread; }

    public void setIscollection(Boolean iscollection) {
        this.iscollection = iscollection;
    }

    public Boolean getIscollection() {
        return iscollection;
    }
}
