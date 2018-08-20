package com.mylove.launcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.mylove.launcher.bean.BannerBean;
import com.mylove.launcher.bean.Contanst;
import com.mylove.launcher.event.HttpEvent;
import com.mylove.launcher.bean.StyleBean;
import com.mylove.launcher.bean.StyleContanst;
import com.mylove.launcher.component.DaggerLauncherComponent;
import com.mylove.launcher.contract.MainContract;
import com.mylove.launcher.fragment.FragmentCheck;
import com.mylove.launcher.fragment.FragmentStyleOne;
import com.mylove.launcher.fragment.FragmentStyleTwo;
import com.mylove.launcher.i.IMainAction;
import com.mylove.launcher.module.LauncherModule;
import com.mylove.launcher.presenter.MainPresenter;
import com.mylove.module_base.base.BaseActivity;
import com.mylove.module_base.base.BaseApplication;
import com.mylove.module_base.base.BaseFragment;
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

    @BindView(R.id.launcher_image_view)
    ImageView mImageView;

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
        initFocusBorder();

        boolean defaultData = (Boolean)SPUtil.get(this, Contanst.DEFAULT_DATA,true);
        if(defaultData){
            initDefaultData();
            SPUtil.put(this,Contanst.DEFAULT_DATA,!defaultData);
        }

        StyleBean styleBean = (StyleBean)SPUtil.readObject(this, StyleContanst.STYLE_KEY, StyleContanst.getDefault());
        showFragment(styleBean.getFragment());
        showBackStyle(styleBean.getStyle());
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

    private void showBackStyle(String style) {
        switch (style){
            case StyleContanst.BANNER:
                    mPresenter.showBanner(this);
                break;
            case StyleContanst.PICTURE:
                    mPresenter.showPicture(this);
                break;
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
            mFocusBorderOne.setVisible(true);
            return mFocusBorderOne;
        }
    }

    public void showFragment(String classStr){
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
                showBackStyle(styleBean.getStyle());
                SPUtil.saveObject(this, StyleContanst.STYLE_KEY,styleBean);
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
    public void showBanner(List<BannerBean> bizhis) {
        if(mViewFlipper == null) return;
        if(mImageView != null) mImageView.setVisibility(View.GONE);
        for (BannerBean bizhi : bizhis){
            mViewFlipper.addView(createShowView(bizhi));
        }
        mViewFlipper.setInAnimation(this,R.anim.launcher_fade_in);
        mViewFlipper.setOutAnimation(this,R.anim.launcher_fade_out);
        mViewFlipper.setAutoStart(true);
        mViewFlipper.setFlipInterval(8000);
        mViewFlipper.startFlipping();
        mViewFlipper.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPicture() {
        if(mImageView == null) return;
        if(mViewFlipper != null) mViewFlipper.setVisibility(View.GONE);
        mImageView.setImageResource(R.drawable.launcher_style_picture);
        mImageView.setVisibility(View.VISIBLE);
    }

    public View createShowView(BannerBean bizhi){
        ImageView imageView = new ImageView(this);
        ImageLoaderHelper.getInstance().loadForCache(this,bizhi.getImage(),imageView);
        return imageView;
    }

    public void register() {
        mNetWorkChangeReceiver = new NetWorkChangeReceiver();
        IntentFilter filterNECT = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filterNECT.addAction("android.net.wifi.STATE_CHANGE");
        filterNECT.addAction("android.net.ethernet.STATE_CHANGE");
        registerReceiver(mNetWorkChangeReceiver, filterNECT);

        mTimeReceiver = new TimeReceiver();
        IntentFilter filterTime = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(mTimeReceiver, filterTime);
    }

    public void unregister() {
        try {
            if (mNetWorkChangeReceiver != null) {
                unregisterReceiver(mNetWorkChangeReceiver);
            }

            if (mTimeReceiver != null) {
                unregisterReceiver(mTimeReceiver);
            }
        }catch(Exception e){

        }
    }

    private TimeReceiver mTimeReceiver;

    @Override
    public void showCheck(View view) {
        if(fragmentCheck == null){
            fragmentCheck = FragmentCheck.newInstance();
        }

        if (!fragmentCheck.isAdded()){
            fragmentCheck.show(getSupportFragmentManager(),"check");
        }

    }

    @Override
    public void onItemClick(TvRecyclerView parent, View itemView, int position) {
        System.out.println("onItemClick ...................");
    }

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
                    switch (mNetworkInfo.getType()) {
                        case  ConnectivityManager.TYPE_WIFI:
                            netImageView.setImageResource(R.drawable.wifi_normal);
                            break;
                        case  ConnectivityManager.TYPE_ETHERNET:
                            netImageView.setImageResource(R.drawable.net_normal);
                            break;
                    }
                }else{
                    netImageView.setImageResource(R.drawable.net_no_normal);
                }
            }
        }
    }

}
