package com.mylove.launcher.bean;

import com.mylove.module_base.bean.Element;

import java.util.List;

/**
 * Created by Administrator on 2018/4/12.
 */

public class BeanUtils {

    public static List<Element> initSize(List<Element> elements){
        for (int i =0; i<elements.size(); i++){
            switch (i){
                case 0:
                    elements.get(i).setColSpan(10);
                    elements.get(i).setRowSpan(8);
                    break;
                default:
                    elements.get(i).setColSpan(8);
                    elements.get(i).setRowSpan(4);
                    break;
            }
        }
        return elements;
    }

}
