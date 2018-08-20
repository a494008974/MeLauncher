package com.mylove.module_base.component;


import android.content.Context;

import com.mylove.module_base.base.BaseApplication;
import com.mylove.module_base.bean.DaoSession;
import com.mylove.module_base.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2018/7/13.
 */
@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    BaseApplication getApplication();

    Context getContext();

    OkHttpClient.Builder getOkHttp();

    DaoSession getDaoSession();
}
