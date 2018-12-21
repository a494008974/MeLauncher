package com.mylove.launcher.contract;

import com.mylove.module_base.base.BaseContract;
import com.mylove.module_base.bean.Banner;

import java.util.List;

/**
 * Created by Administrator on 2018/8/14.
 */

public class MainContract implements BaseContract{

    public interface View extends BaseContract.BaseView{
        void showBanner(List<Banner> bizhis);
    }

    public interface Presenter extends BaseContract.BasePresenter<View>{
    }

}
