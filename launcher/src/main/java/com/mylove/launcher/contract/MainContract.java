package com.mylove.launcher.contract;

import com.mylove.launcher.bean.Bizhi;
import com.mylove.module_base.base.BaseContract;

import java.util.List;

/**
 * Created by Administrator on 2018/8/14.
 */

public class MainContract implements BaseContract{

    public interface View extends BaseContract.BaseView{

    }

    public interface Presenter extends BaseContract.BasePresenter<View>{
    }

}
