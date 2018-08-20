package com.mylove.launcher.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/12.
 */

public class BeanUtils {

    public static List<LchItemBean> getThree(int number, String title) {
        List<LchItemBean> lchbeans = new ArrayList<LchItemBean>();
        for (int i =0; i<number; i++){
            LchItemBean lchbean = new LchItemBean();
            lchbean.setName(title+"-"+i);
            lchbean.setImg("Img"+i);
            switch (i){
                case 0:
                    lchbean.setColSpan(23);
                    lchbean.setRowSpan(18);
                    break;
                default:
                    lchbean.setColSpan(18);
                    lchbean.setRowSpan(9);
                    break;
            }

            lchbeans.add(lchbean);
        }
        return lchbeans;
    }

    public static List<LchItemBean> getDatas(int number, String title) {
        List<LchItemBean> lchbeans = new ArrayList<LchItemBean>();
        for (int i =0; i<number; i++){
            LchItemBean lchbean = new LchItemBean();
            lchbean.setName(title+"-"+i);
            lchbean.setImg("Img"+i);
            switch (i){
                case 0:
                case 1:
                case 2:
                    lchbean.setColSpan(7);
                    lchbean.setRowSpan(6);
                    break;
                case 3:
                    lchbean.setColSpan(16);
                    lchbean.setRowSpan(12);
                    break;
                case 4:
                    lchbean.setColSpan(8);
                    lchbean.setRowSpan(6);
                    break;
                case 5:
                    lchbean.setColSpan(8);
                    lchbean.setRowSpan(6);
                    break;
                case 6:
                    lchbean.setColSpan(9);
                    lchbean.setRowSpan(12);
                    break;
                case 7:
                    lchbean.setColSpan(9);
                    lchbean.setRowSpan(6);
                    break;
                case 8:
                case 9:
                case 10:
                    lchbean.setColSpan(10);
                    lchbean.setRowSpan(6);
                    break;
                default:
                    lchbean.setColSpan(6);
                    lchbean.setRowSpan(6);
                    break;
            }

            lchbeans.add(lchbean);
        }
        return lchbeans;
    }
}
