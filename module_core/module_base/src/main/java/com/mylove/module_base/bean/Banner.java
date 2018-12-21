package com.mylove.module_base.bean;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/8/14.
 */
@Entity
public class Banner {

    /**
     * id : 393
     * title : 武动乾坤
     * image : http://imgzm.qun7.com/uploads/20180808/5b6a7e0c27a0a.jpg
     * classname : 3
     * url : id=891513
     * sort : 1
     * status : 1
     * param1 :
     * pubdate : 1533705753
     */
    @Id
    @Expose private String id;
    @Expose private String title;
    @Expose private String image;
    @Expose private String classname;
    @Expose private String url;
    @Expose private String sort;
    @Expose private String status;
    @Expose private String param1;
    @Expose private String pubdate;

    @Generated(hash = 1431414324)
    public Banner(String id, String title, String image, String classname,
            String url, String sort, String status, String param1, String pubdate) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.classname = classname;
        this.url = url;
        this.sort = sort;
        this.status = status;
        this.param1 = param1;
        this.pubdate = pubdate;
    }

    @Generated(hash = 2026719322)
    public Banner() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }
}
