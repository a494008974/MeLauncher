package com.mylove.launcher.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/8/17.
 */

public class StyleBean implements Serializable{
    private String style;
    private String fragment;

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getFragment() {
        return fragment;
    }

    public void setFragment(String fragment) {
        this.fragment = fragment;
    }
}
