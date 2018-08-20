package com.mylove.launcher.model;


import com.mylove.launcher.bean.BannerBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by Administrator on 2018/8/14.
 */

public interface LauncherApiSerivce {
    String launcherUrl = "http://zmapi.dangbei.net/";

//    http://zmapi.dangbei.net/thirdpart/bizhi/

    @GET("thirdpart/bizhi")
    Observable<List<BannerBean>> getBanner();
}
