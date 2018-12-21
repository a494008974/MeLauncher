package com.mylove.launcher.bean;

import com.mylove.module_base.bean.SizeBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/4/12.
 */
public class LchItemBean extends SizeBean {
    private String name;
    private String img;

    private int colSpan;
    private int rowSpan;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getColSpan() {
        return this.colSpan;
    }
    public void setColSpan(int colSpan) {
        this.colSpan = colSpan;
    }
    public int getRowSpan() {
        return this.rowSpan;
    }
    public void setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
    }
}
