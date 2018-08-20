package com.mylove.module_base.module;

import android.content.Context;

import com.mylove.module_base.base.BaseApplication;
import com.mylove.module_base.bean.DaoSession;
import com.mylove.module_base.net.RetrofitConfig;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2018/7/13.
 */
@Module
public class ApplicationModule {
    private Context mContext;

    public ApplicationModule(Context mContext) {
        this.mContext = mContext;
    }

    @Provides
    public BaseApplication getApplication(){
        return (BaseApplication)mContext.getApplicationContext();
    }

    @Provides
    public DaoSession getDaoSession(){
        return getApplication().getDaoSession();
    }

    @Provides
    public Context getContext(){
        return mContext;
    }

    @Singleton
    @Provides
    OkHttpClient.Builder provideOkHttpClient() {
        // 指定缓存路径,缓存大小10Mb
        Cache cache = new Cache(new File(BaseApplication.getAppContext().getCacheDir(), "HttpCache"),
                1024 * 1024 * 30);
        return new OkHttpClient().newBuilder().cache(cache)
                .retryOnConnectionFailure(true)
                .addInterceptor(RetrofitConfig.sLoggingInterceptor)
                .addInterceptor(RetrofitConfig.sRewriteCacheControlInterceptor)
                .addNetworkInterceptor(RetrofitConfig.sRewriteCacheControlInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS);
    }

}
