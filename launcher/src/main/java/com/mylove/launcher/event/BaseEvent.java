package com.mylove.launcher.event;

/**
 * Created by Administrator on 2018/8/17.
 */

public class BaseEvent {
    private int event;
    private Object obj;

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
