package com.mylove.launcher.model;

import com.mylove.module_base.base.BaseApplication;
import com.mylove.module_base.bean.Banner;
import com.mylove.module_base.bean.BannerDao;
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
    private DaoSession daoSession;

    private LauncherApi(LauncherApiSerivce launcherApiSerivce) {
        this.launcherApiSerivce = launcherApiSerivce;
        daoSession = BaseApplication.getAppContext().getDaoSession();
    }

    public static LauncherApi getInstance(LauncherApiSerivce launcherApiSerivce) {
        if (sInstance == null)
            sInstance = new LauncherApi(launcherApiSerivce);
        return sInstance;
    }

    public Observable<List<Banner>> getBanner() {
        return launcherApiSerivce.getBanner();
    }

    public List<Element> fetchElements(String like) {
        if (daoSession == null) return null;
        List<Element> elements = null;
        if(like != null){
            elements = daoSession.getElementDao().queryBuilder().where(ElementDao.Properties.Tag.like(like+"%")).orderAsc(ElementDao.Properties.Tag).list();
        }else{
            elements = daoSession.getElementDao().queryBuilder().orderAsc(ElementDao.Properties.Tag).list();
        }
        return elements;
    }

    public List<Element> fetchElements() {
        return fetchElements(null);
    }

    public void saveElement(Element element){
        if (daoSession == null) return;
        daoSession.getElementDao().insertOrReplaceInTx(element);
    }

    public Element fetchElement(String tag){
        if (daoSession == null) return null;
        return daoSession.getElementDao().load(tag);
    }

    public List<Banner> fetchBanners(){
        if (daoSession == null) return null;
        List<Banner> banners = daoSession.getBannerDao().queryBuilder().orderAsc(BannerDao.Properties.Id).list();
        return banners;
    }

    public void saveBanner(Banner banner) {
        if (daoSession == null) return;
        daoSession.getBannerDao().insertOrReplaceInTx(banner);
    }
}
