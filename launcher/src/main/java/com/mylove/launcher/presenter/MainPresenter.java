package com.mylove.launcher.presenter;

import android.content.Context;

import com.mylove.launcher.bean.Contanst;
import com.mylove.launcher.contract.MainContract;
import com.mylove.launcher.webserver.controller.ActionController;
import com.mylove.launcher.model.LauncherApi;
import com.mylove.module_base.base.BasePresenter;
import com.mylove.module_base.bean.Banner;
import com.mylove.module_base.bean.Element;
import com.mylove.module_base.helper.ImageLoaderHelper;
import com.mylove.module_base.net.BaseObserver;
import com.mylove.module_base.net.RxSchedulers;

import java.util.List;

import javax.inject.Inject;

import cn.hotapk.fhttpserver.FHttpManager;
import io.reactivex.functions.Function;


/**
 * Created by Administrator on 2018/8/14.
 */

public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {
    private LauncherApi launcherApi;
    private FHttpManager fHttpManager;

    @Inject
    MainPresenter(LauncherApi launcherApi) {
        this.launcherApi = launcherApi;
    }

    public void startServer(Context context){
        fHttpManager = FHttpManager.init(context, ActionController.class);
        fHttpManager.setPort(Contanst.SERVER_PORT);
        fHttpManager.setAllowCross(true);
        fHttpManager.startServer();
    }

    public void stopServer(){
        if(fHttpManager != null){
            fHttpManager.stopServer();
        }
    }

    public void showBanner(final Context context) {
        launcherApi.getBanner()
                .compose(RxSchedulers.<List<Banner>>applySchedulers())
                .map(new Function<List<Banner>, List<Banner>>() {
                    @Override
                    public List<Banner> apply(List<Banner> banners) throws Exception {
                        for (Banner banner : banners){
                            ImageLoaderHelper.getInstance().download(context,banner.getImage());
//                            launcherApi.saveBanner(banner);
                        }
                        return banners;
                    }
                })
                .subscribe(new BaseObserver<List<Banner>>() {
                    @Override
                    public void onSuccess(List<Banner> banners) {
                        if(banners != null && banners.size() > 0 && mView != null){
                            mView.showBanner(banners);
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                    }
                });

    }

    public void saveBanner(Banner banner){launcherApi.saveBanner(banner);}

    public void saveElement(Element element){
        launcherApi.saveElement(element);
    }

    public Element fetchElement(String tag){
        return launcherApi.fetchElement(tag);
    }
}
