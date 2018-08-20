package com.mylove.launcher.model;

import com.mylove.launcher.bean.BannerBean;
import com.mylove.module_base.base.BaseApplication;
import com.mylove.module_base.bean.DaoSession;
import com.mylove.module_base.bean.Element;
import com.mylove.module_base.bean.ElementDao;

import java.util.List;
import io.reactivex.Observable;

/**
 * Created by Administrator on 2018/8/14.
 */

public class LauncherApi {
    public static LauncherApi sInstance;
    public LauncherApiSerivce launcherApiSerivce;

    private LauncherApi(LauncherApiSerivce launcherApiSerivce) {
        this.launcherApiSerivce = launcherApiSerivce;
    }

    public static LauncherApi getInstance(LauncherApiSerivce launcherApiSerivce) {
        if (sInstance == null)
            sInstance = new LauncherApi(launcherApiSerivce);
        return sInstance;
    }


    public Observable<List<BannerBean>> getBanner() {
        return launcherApiSerivce.getBanner();
    }

    public List<Element> getElements() {
        DaoSession daoSession = BaseApplication.getAppContext().getDaoSession();
        List<Element> elements = daoSession.getElementDao().queryBuilder().orderAsc(ElementDao.Properties.Tag).list();
        return elements;
    }
}
