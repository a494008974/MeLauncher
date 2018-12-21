package com.mylove.launcher.model;


import com.mylove.launcher.bean.Contanst;
import com.mylove.module_base.bean.Banner;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by Administrator on 2018/8/14.
 */

public interface LauncherApiSerivce {

    @GET(Contanst.SERVER_DIR+"/banners")
    Observable<List<Banner>> getBanner();
}
