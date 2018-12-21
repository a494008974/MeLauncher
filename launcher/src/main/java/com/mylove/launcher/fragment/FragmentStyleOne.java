package com.mylove.launcher.fragment;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;

import com.mylove.launcher.R;
import com.mylove.launcher.adapter.RecyclerAdapter;
import com.mylove.launcher.component.DaggerLauncherComponent;
import com.mylove.launcher.contract.StyleContract;
import com.mylove.launcher.i.IMainAction;
import com.mylove.launcher.i.IMainStatus;
import com.mylove.launcher.module.LauncherModule;
import com.mylove.launcher.presenter.StylePresenter;
import com.mylove.module_base.adapter.CommonRecyclerViewHolder;
import com.mylove.module_base.base.BaseFragment;
import com.mylove.module_base.bean.Element;
import com.mylove.module_base.component.ApplicationComponent;
import com.mylove.module_base.module.ApplicationModule;
import com.owen.tvrecyclerview.widget.SimpleOnItemListener;
import com.owen.tvrecyclerview.widget.TvRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/8/15.
 */

public class FragmentStyleOne extends BaseFragment<StylePresenter> implements StyleContract.View,IMainStatus,View.OnKeyListener {

    @BindView(R.id.picture_tv_recycle_view)
    TvRecyclerView mTvRecyclerView;

    private PackageManager packageManager;

    RecyclerAdapter mAdapter;
    List<Element> elements;
    @Override
    public int getContentLayout() {
        return R.layout.fragment_one;
    }

    @Override
    public void initInjector(ApplicationComponent appComponent) {
        DaggerLauncherComponent.builder()
                .launcherModule(new LauncherModule())
                .applicationModule(new ApplicationModule(getContext()))
                .build()
                .inject(this);
    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        packageManager = getActivity().getPackageManager();
        setListener();

        mAdapter = new RecyclerAdapter<Element>(getActivity(),R.layout.launcher_item) {
            @Override
            public void onBindItemHolder(CommonRecyclerViewHolder helper, Element item, int position) {
                helper.itemView.setTag(item.getTag());
                helper.itemView.setTag(R.id.tag,position);
                helper.getHolder().setImageResource(R.id.launcher_item_icon,R.drawable.default_icon);
                helper.getHolder().setText(R.id.launcher_item_name,"");
                helper.itemView.setOnKeyListener(FragmentStyleOne.this);
                PackageInfo pkgInfo = null;
                try {
                    pkgInfo = packageManager.getPackageInfo(item.getPkg(),PackageManager.GET_PERMISSIONS);
                } catch (PackageManager.NameNotFoundException e) {
                }
                if(pkgInfo == null)return;

                helper.getHolder().setImageDrawable(R.id.launcher_item_icon,packageManager.getApplicationIcon(pkgInfo.applicationInfo));
                String name = (String) packageManager.getApplicationLabel(pkgInfo.applicationInfo);
                helper.getHolder().setText(R.id.launcher_item_name,name);
            }
        };
        elements = new ArrayList<Element>();
        mAdapter.setDatas(elements);
        mTvRecyclerView.setSpacingWithMargins(50, 50);
        mTvRecyclerView.setAdapter(mAdapter);
    }

    private void setListener() {
        mTvRecyclerView.setOnItemListener(new SimpleOnItemListener() {
            @Override
            public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
                onMoveFocusBorder(itemView, 1.0f, 10);
            }

            @Override
            public void onItemClick(TvRecyclerView parent, View itemView, int position) {
                ((IMainAction)getActivity()).onItemClick(parent,itemView,position);
            }
        });

    }


    @Override
    public void initData() {
        ItemViewUpdate();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mTvRecyclerView != null){
                    mTvRecyclerView.setSelection(0);
                }
            }
        },100);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch (keyCode){
                case KeyEvent.KEYCODE_MENU:
                    ((IMainAction)getActivity()).showCheck(v);
                    break;
            }
        }
        return false;
    }

    @Override
    public void ItemViewUpdate() {
        List<Element> daoElement = mPresenter.getElements("1");
        if(daoElement.size() >= 5){
            daoElement = daoElement.subList(0,5);
        }
        mAdapter.clearDatas();
        mAdapter.appendDatas(daoElement);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void ItemViewUpdate(View view,Object obj) {
        int pos = (Integer) view.getTag(R.id.tag);
        mAdapter.changed(pos,obj);
    }
    @Override
    public void NetStatuChange(boolean connect) {

    }

}
