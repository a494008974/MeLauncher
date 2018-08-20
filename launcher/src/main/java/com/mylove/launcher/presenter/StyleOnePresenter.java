package com.mylove.launcher.presenter;

import com.mylove.launcher.contract.PictureContract;
import com.mylove.launcher.model.LauncherApi;
import com.mylove.module_base.base.BasePresenter;
import com.mylove.module_base.bean.Element;

import java.util.List;

import javax.inject.Inject;


/**
 * Created by Administrator on 2018/8/14.
 */

public class StyleOnePresenter extends BasePresenter<PictureContract.View> implements PictureContract.Presenter {
    private LauncherApi launcherApi;

    @Inject
    StyleOnePresenter(LauncherApi launcherApi) {
        this.launcherApi = launcherApi;
    }

    public List<Element> getElements(){
        List<Element> elements = launcherApi.getElements();
        if(elements.size() >= 5){
            elements = elements.subList(0,5);
        }
        return elements;
    }

}
