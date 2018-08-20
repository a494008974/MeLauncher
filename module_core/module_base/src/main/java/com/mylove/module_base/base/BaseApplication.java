package com.mylove.module_base.base;

/**
 * Created by Administrator on 2018/7/11.
 */

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDex;

import com.facebook.stetho.Stetho;
import com.mylove.module_base.bean.DaoMaster;
import com.mylove.module_base.bean.DaoSession;
import com.mylove.module_base.component.ApplicationComponent;
import com.mylove.module_base.component.DaggerApplicationComponent;
import com.mylove.module_base.module.ApplicationModule;

import com.mylove.module_base.net.down.DownloadUtil;
import com.mylove.module_base.utils.AppUtils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;
import com.zhy.autolayout.config.AutoLayoutConifg;


public class BaseApplication extends Application {

    public static boolean IS_DEBUG = true;
    private static BaseApplication mBaseApplication ;
    //Activity管理
    private ActivityControl mActivityControl;
    private String BUGLY_ID = "a29fb52485" ;

    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private ApplicationComponent applicationComponent;

    //SmartRefreshLayout 有三种方式,请参考:https://github.com/scwang90/SmartRefreshLayout
    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
//        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
//            @Override
//            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
//                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
//                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
//            }
//        });
        //设置全局的Footer构建器
//        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
//            @Override
//            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
//                //指定为经典Footer，默认是 BallPulseFooter
//                return new ClassicsFooter(context).setDrawableSize(20);
//            }
//        });
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public ActivityControl getActivityControl() {
        return mActivityControl;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mBaseApplication = this ;
        //MultiDex分包方法 必须最先初始化
        MultiDex.install(this);
    }

    public static BaseApplication getAppContext(){
        return mBaseApplication;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        Locale _UserLocale = LocaleHelper.getLanguage(this);
//        _UserLocale.getCountry();
//        _UserLocale.getLanguage();
        //系统语言改变了应用保持之前设置的语言
//        if (_UserLocale != null) {
//            LocaleHelper.setLocale(this, _UserLocale);
//        }
//        L.i("当前语言："+_UserLocale );
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mActivityControl = new ActivityControl();
        //arouter路由初始化
        RouterConfig.init(this, IS_DEBUG);
        //bugly初始化
        initBugly();
        //AutoLayout适配初始化
        AutoLayoutConifg.getInstance().useDeviceSize();
        //Stetho调试工具初始化
        Stetho.initializeWithDefaults(this);
        // 初始化Logger工具
        Logger.addLogAdapter(new AndroidLogAdapter(){
            @Override
            public boolean isLoggable(int priority, String tag) {
                return IS_DEBUG;
            }
        });

        applicationComponent = DaggerApplicationComponent
                                        .builder()
                                        .applicationModule(new ApplicationModule(this))
                                        .build();

        DownloadUtil.get().init(this);

        setDatabase();
    }

    private void setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        mHelper = new DaoMaster.DevOpenHelper(this, "launcher-db", null);
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }



    private void initBugly() {
        // 获取当前包名
        String packageName = getApplicationContext().getPackageName();
        // 获取当前进程名
        String processName = AppUtils.getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        CrashReport.initCrashReport(getApplicationContext(), BUGLY_ID, false, strategy);
    }


    /**
     * 程序终止的时候执行
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        exitApp();
    }
    /**
     * 退出应用
     */
    public void exitApp() {
        mActivityControl.finishiAll();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }


    /**
     * 低内存的时候执行
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }

    // 程序在内存清理的时候执行
    /**
     * 程序在内存清理的时候执行
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

    }
}
