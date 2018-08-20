package com.mylove.launcher.contract;

import com.mylove.launcher.bean.BannerBean;
import com.mylove.module_base.base.BaseContract;

import java.util.List;

/**
 * Created by Administrator on 2018/8/14.
 */

public class MainContract implements BaseContract{

    public interface View extends BaseContract.BaseView{
        void showBanner(List<BannerBean> bizhis);

        void showPicture();
    }

    public interface Presenter extends BaseContract.BasePresenter<View>{
    }

}
