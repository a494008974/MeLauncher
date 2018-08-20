package com.mylove.launcher.bean;

import com.mylove.launcher.fragment.FragmentStyleOne;
import com.mylove.launcher.fragment.FragmentStyleThree;
import com.mylove.launcher.fragment.FragmentStyleTwo;

/**
 * Created by Administrator on 2018/8/17.
 */

public class StyleContanst {

    public static final String STYLE_KEY = "STYLE";

    public static final String BANNER = "banner";
    public static final String PICTURE = "picture";

    public static final String FRAGMENT_ONE = FragmentStyleOne.class.getName();
    public static final String FRAGMENT_TWO = FragmentStyleTwo.class.getName();
    public static final String FRAGMENT_THREE = FragmentStyleThree.class.getName();


    public static String styleDefault = BANNER;
    public static String fragmentDefault = FRAGMENT_ONE;

    public static StyleBean getDefault(){
        StyleBean styleBean = new StyleBean();
        styleBean.setStyle(styleDefault);
        styleBean.setFragment(fragmentDefault);
        return styleBean;
    }
}
