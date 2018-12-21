package com.mylove.launcher.presenter;

import com.mylove.launcher.contract.StyleContract;
import com.mylove.launcher.model.LauncherApi;
import com.mylove.module_base.base.BasePresenter;
import com.mylove.module_base.bean.Element;

import java.util.List;

import javax.inject.Inject;


/**
 * Created by Administrator on 2018/8/14.
 */

public class StylePresenter extends BasePresenter<StyleContract.View> implements StyleContract.Presenter {
    private LauncherApi launcherApi;

    @Inject
    StylePresenter(LauncherApi launcherApi) {
        this.launcherApi = launcherApi;
    }

    public List<Element> getElements(String like){
        return launcherApi.fetchElements(like);
    }
}
