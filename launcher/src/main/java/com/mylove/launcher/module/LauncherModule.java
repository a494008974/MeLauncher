package com.mylove.launcher.module;

import com.mylove.launcher.bean.Contanst;
import com.mylove.launcher.model.LauncherApi;
import com.mylove.launcher.model.LauncherApiSerivce;
import com.mylove.module_base.base.BaseApplication;
import com.mylove.module_base.utils.SystemUtils;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018/8/14.
 */
@Module
public class LauncherModule {

    @Singleton
    @Provides
    LauncherApi provideLauncherApi(OkHttpClient.Builder builder){

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(builder.build());

        return LauncherApi.getInstance(retrofitBuilder
                .baseUrl(Contanst.getServerPath(SystemUtils.getIPAddress(BaseApplication.getAppContext()))+ File.separator)
                .build().create(LauncherApiSerivce.class));
    }
}
