package com.kc.demo.model;

import java.util.Date;

public class CommunityImages {
    private Integer id;

    private String originalname;

    private String filename;

    private String imgtype;

    private Integer comquestionid;

    private Integer comanswerid;

    private Date createtime;

    private String filepath;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginalname() {
        return originalname;
    }

    public void setOriginalname(String originalname) {
        this.originalname = originalname == null ? null : originalname.trim();
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename == null ? null : filename.trim();
    }

    public String getImgtype() {
        return imgtype;
    }

    public void setImgtype(String imgtype) {
        this.imgtype = imgtype == null ? null : imgtype.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath == null ? null : filepath.trim();
    }

    public Integer getComquestionid() { return comquestionid; }

    public void setComquestionid(Integer comquestionid) { this.comquestionid = comquestionid ;}

    public Integer getComanswerid() { return  comanswerid; }

    public void setComanswerid(Integer comanswerid) { this.comanswerid = comanswerid ;}
}
