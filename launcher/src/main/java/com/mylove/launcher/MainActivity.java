package com.mylove.launcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.mylove.launcher.bean.Contanst;
import com.mylove.launcher.event.HttpEvent;
import com.mylove.launcher.bean.StyleBean;
import com.mylove.launcher.bean.StyleContanst;
import com.mylove.launcher.component.DaggerLauncherComponent;
import com.mylove.launcher.contract.MainContract;
import com.mylove.launcher.fragment.FragmentCheck;
import com.mylove.launcher.fragment.FragmentQRCode;
import com.mylove.launcher.fragment.FragmentStyleOne;
import com.mylove.launcher.fragment.FragmentStyleTwo;
import com.mylove.launcher.fragment.FragmentSub;
import com.mylove.launcher.i.IMainAction;
import com.mylove.launcher.i.IMainStatus;
import com.mylove.launcher.module.LauncherModule;
import com.mylove.launcher.presenter.MainPresenter;
import com.mylove.module_base.base.BaseActivity;
import com.mylove.module_base.base.BaseApplication;
import com.mylove.module_base.base.BaseFragment;
import com.mylove.module_base.bean.Banner;
import com.mylove.module_base.bean.DaoSession;
import com.mylove.module_base.bean.Element;
import com.mylove.module_base.component.ApplicationComponent;
import com.mylove.module_base.focus.FocusBorder;
import com.mylove.module_base.helper.ImageLoaderHelper;
import com.mylove.module_base.module.ApplicationModule;
import com.mylove.module_base.utils.SPUtil;
import com.mylove.module_base.utils.SystemUtils;
import com.owen.tvrecyclerview.widget.TvRecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View,BaseFragment.FocusBorderHelper,IMainAction {

    @BindView(R.id.launcher_view_flipper)
    ViewFlipper mViewFlipper;

    @BindView(R.id.launcher_statu_time)
    TextView timeTextView;

    @BindView(R.id.launcher_status_week)
    TextView weekTextView;

    @BindView(R.id.launcher_status_net)
    ImageView netImageView;

    private Fragment currentFragment;
    protected FocusBorder mFocusBorderOne,mFocusBorderTwo;

    private DaoSession daoSession;

    private FragmentCheck fragmentCheck;
    private FragmentSub fragmentSub;
    private FragmentQRCode fragmentQRCode;

    private List<PackageInfo> packageInfos;
    private PackageManager packageManager;

    @Override
    public int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initInjector(ApplicationComponent appComponent) {
        DaggerLauncherComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .launcherModule(new LauncherModule())
                .build()
                .inject(this);

        daoSession = appComponent.getDaoSession();
    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        packageManager = getPackageManager();
        initFocusBorder();

        boolean defaultData = (Boolean)SPUtil.get(this, Contanst.DEFAULT_DATA,true);
        if(defaultData){
            initDefaultData();
            SPUtil.put(this,Contanst.DEFAULT_DATA,!defaultData);
        }

        StyleBean styleBean = (StyleBean)SPUtil.readObject(this, StyleContanst.STYLE_KEY, StyleContanst.getDefault());
        showFragment(styleBean.getFragment());

        initPackageInfo();
    }

    public void initPackageInfo(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                packageInfos = SystemUtils.getAllApps(MainActivity.this,3,true);
            }
        }.start();
    }

    private void initDefaultData() {
        TypedArray typedArray = getResources().obtainTypedArray(R.array.Launcher_DEFAULT);
        List<Element> elements = new ArrayList<Element>();
        String[] tagArray = getResources().getStringArray(typedArray.getResourceId(0,-1));


        for (int i=0; i<tagArray.length; i++){
            Element element = new Element();
            element.setTag(tagArray[i]);
            elements.add(element);
        }
        daoSession.getElementDao().insertOrReplaceInTx(elements);
    }

    private void initFocusBorder() {
        if(null == mFocusBorderOne) {
            mFocusBorderOne = new FocusBorder.Builder()
                    .asDrawable()
                    .borderResId(R.drawable.launcher_focused_app)
                    .animDuration(10L)
                    .noShimmer()
                    .build(this);
        }

        if(null == mFocusBorderTwo) {
            mFocusBorderTwo = new FocusBorder.Builder()
                    .asColor()
                    .borderColor(getResources().getColor(R.color.launcher_item_shadow_color))
                    .borderWidth(TypedValue.COMPLEX_UNIT_DIP, 2)
                    .shadowColor(getResources().getColor(R.color.launcher_item_shadow_color))
                    .shadowWidth(TypedValue.COMPLEX_UNIT_DIP, 20)
                    .animDuration(180L)
                    .build(this);
        }
    }

    public FocusBorder getFocusBorder() {
        mFocusBorderOne.setVisible(false);
        mFocusBorderTwo.setVisible(false);

        if(currentFragment instanceof FragmentStyleOne){
            mFocusBorderOne.setVisible(true);
            return mFocusBorderOne;
        }else if(currentFragment instanceof FragmentStyleTwo){
            mFocusBorderTwo.setVisible(true);
            return mFocusBorderTwo;
        }else{
//            mFocusBorderOne.setVisible(true);
            return mFocusBorderOne;
        }
    }

    public void showFragment(String classStr){
        if(currentFragment != null && currentFragment.getClass().getName().equals(classStr)){
            return;
        }
        try {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            currentFragment = Fragment.instantiate(this,classStr);
            transaction.replace(R.id.launcher_main,currentFragment);
            transaction.commit();
        }catch (Exception e){

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void HttpEvent(HttpEvent event){
        switch (event.getEvent()){
            case HttpEvent.CHANGE_STYLE:
                StyleBean styleBean = (StyleBean)event.getObj();
                showFragment(styleBean.getFragment());
                SPUtil.saveObject(this, StyleContanst.STYLE_KEY,styleBean);
                break;
            case HttpEvent.CHANGE_BANNER:
                mPresenter.showBanner(this);
                break;
            case HttpEvent.CHANGE_ELEMENT:
                ((IMainStatus)currentFragment).ItemViewUpdate();
                break;
            case HttpEvent.UPLOAD_EVENT:
                break;
            case HttpEvent.DOWNLOAD_EVENT:
                break;
            case HttpEvent.SUBMIT_EVENT:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void initData() {
        mPresenter.startServer(this);
        mPresenter.showBanner(this);
        register();
    }

    @Override
    public void onDestroy() {
        mPresenter.stopServer();
        unregister();
        super.onDestroy();
        BaseApplication.getAppContext().exitApp();
    }

    @Override
    public void showBanner(List<Banner> banners) {
        if(mViewFlipper == null) return;
        mViewFlipper.removeAllViews();
        for (Banner banner : banners){
            mViewFlipper.addView(createShowView(banner));
        }
        mViewFlipper.setInAnimation(this,R.anim.launcher_fade_in);
        mViewFlipper.setOutAnimation(this,R.anim.launcher_fade_out);
        if(banners.size() > 1){
            mViewFlipper.setAutoStart(true);
            mViewFlipper.setFlipInterval(8000);
            mViewFlipper.startFlipping();
        }
        mViewFlipper.setVisibility(View.VISIBLE);
    }

    public View createShowView(Banner banner){
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT));
        ImageLoaderHelper.getInstance().loadForCache(this,banner.getImage(),imageView);
        return imageView;
    }

    @Override
    public void showCheck(final View view) {
        fragmentCheck = FragmentCheck.newInstance();
        if(fragmentCheck != null){
            if (!fragmentCheck.isAdded()){
                if (packageInfos == null) {
                    packageInfos = SystemUtils.getAllApps(this,3,true);
                }
                fragmentCheck.setPackageInfos(packageInfos);

                fragmentCheck.setCheckListener(new FragmentCheck.CheckListener() {
                    @Override
                    public void onItemClick(TvRecyclerView parent, View itemView, int position, Object item) {
                        PackageInfo info = (PackageInfo)item;

                        Element element = new Element();
                        element.setTag((String)view.getTag());
                        if (!getPackageName().equals(info.applicationInfo.packageName)){
                            element.setPkg(info.applicationInfo.packageName);
                        }
                        mPresenter.saveElement(element);

                        ((IMainStatus)currentFragment).ItemViewUpdate(view,element);
                        fragmentCheck.dismiss();

                    }
                });

                fragmentCheck.show(getSupportFragmentManager(),"check");
            }
        }
    }

    @Override
    public void showSub() {
        fragmentSub = FragmentSub.newInstance();
        if(fragmentSub != null){
            if (!fragmentSub.isAdded()){
                if (packageInfos == null) {
                    packageInfos = SystemUtils.getAllApps(this,3,true);
                }
                fragmentSub.setPackageInfos(packageInfos);
                fragmentSub.show(getSupportFragmentManager(),"sub");
            }
        }
    }

    @Override
    public void showQRCode() {
        fragmentQRCode = FragmentQRCode.newInstance();
        if(fragmentQRCode != null){
            if (!fragmentQRCode.isAdded()){
                if (packageInfos == null) {
                    packageInfos = SystemUtils.getAllApps(this,3,true);
                }
                fragmentQRCode.show(getSupportFragmentManager(),"sub");
            }
        }
    }

    @Override
    public void onItemClick(TvRecyclerView parent, View itemView, int position) {
        Element element = mPresenter.fetchElement((String)itemView.getTag());
        if (element != null && element.getPkg() != null && !"".equals(element.getPkg())){
            SystemUtils.openApk(this,element.getPkg());
        }else{
            showCheck(itemView);
        }
    }



    private boolean connect;
    @Override
    public boolean fetchNetStatu() {
        return connect;
    }

    //=======================广播====================
    public void register() {
        mNetWorkChangeReceiver = new NetWorkChangeReceiver();
        IntentFilter filterNECT = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filterNECT.addAction("android.net.wifi.STATE_CHANGE");
        filterNECT.addAction("android.net.ethernet.STATE_CHANGE");
        registerReceiver(mNetWorkChangeReceiver, filterNECT);

        mTimeReceiver = new TimeReceiver();
        IntentFilter filterTime = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(mTimeReceiver, filterTime);

        mAppReceiver = new AppReceiver();
        IntentFilter filterAPP = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filterAPP.addDataScheme("package");
        filterAPP.addAction(Intent.ACTION_PACKAGE_REMOVED);
        registerReceiver(mAppReceiver, filterAPP);

        mUsbAndSDCardBroadcastReceiver = new UsbAndSDCardBroadcastReceiver();
        IntentFilter mUsbAndSDCardFilter = new IntentFilter();
        mUsbAndSDCardFilter.addAction("android.intent.action.MEDIA_MOUNTED");
        mUsbAndSDCardFilter.addAction("android.intent.action.MEDIA_REMOVED");
        mUsbAndSDCardFilter.addAction("android.intent.action.MEDIA_BAD_REMOVAL");
        mUsbAndSDCardFilter.addDataScheme("file");
        registerReceiver(mUsbAndSDCardBroadcastReceiver, mUsbAndSDCardFilter);
    }

    public void unregister() {
        try {
            if (mNetWorkChangeReceiver != null) {
                unregisterReceiver(mNetWorkChangeReceiver);
            }

            if (mTimeReceiver != null) {
                unregisterReceiver(mTimeReceiver);
            }

            if (mAppReceiver != null) {
                unregisterReceiver(mAppReceiver);
            }

            if (mUsbAndSDCardBroadcastReceiver != null) {
                unregisterReceiver(mUsbAndSDCardBroadcastReceiver);
            }
        }catch(Exception e){

        }
    }

    private AppReceiver mAppReceiver;
    public class AppReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String packageName = intent.getDataString();
            packageName = packageName.split(":")[1];
            if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {

            }else if(intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")){

            }
            initPackageInfo();
        }
    }

    String usbMountedPath = null;
    UsbAndSDCardBroadcastReceiver mUsbAndSDCardBroadcastReceiver;
    class UsbAndSDCardBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if ((intent.getAction().equals("android.intent.action.MEDIA_REMOVED"))
                 || (intent.getAction().equals("android.intent.action.MEDIA_BAD_REMOVAL"))) {

            }
            if (intent.getAction().equals("android.intent.action.MEDIA_MOUNTED")) {
                usbMountedPath = intent.getData().getPath();
            }
        }
    }

    private TimeReceiver mTimeReceiver;
    public class TimeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIME_TICK)) {
                timeTextView.setText(SystemUtils.getTime(MainActivity.this));
                weekTextView.setText(SystemUtils.getWeek());
            }
        }
    }

    NetWorkChangeReceiver mNetWorkChangeReceiver;
    public class NetWorkChangeReceiver extends BroadcastReceiver {
        private ConnectivityManager connectivityManager;
        private NetworkInfo info;
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                connectivityManager = (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if(mNetworkInfo != null && mNetworkInfo.isAvailable()){
                    connect = true;
                    switch (mNetworkInfo.getType()) {
                        case  ConnectivityManager.TYPE_WIFI:
                            netImageView.setImageResource(R.drawable.wifi_normal);
                            break;
                        case  ConnectivityManager.TYPE_ETHERNET:
                            netImageView.setImageResource(R.drawable.net_normal);
                            break;
                    }
                }else{
                    connect = false;
                    netImageView.setImageResource(R.drawable.net_no_normal);
                }
            }
        }
    }

}
