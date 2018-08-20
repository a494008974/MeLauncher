package com.mylove.launcher.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;

import com.mylove.launcher.R;
import com.mylove.launcher.adapter.ListAdapter;
import com.mylove.launcher.component.DaggerLauncherComponent;
import com.mylove.launcher.contract.PictureContract;
import com.mylove.launcher.i.IMainAction;
import com.mylove.launcher.i.IMainStatus;
import com.mylove.launcher.module.LauncherModule;
import com.mylove.launcher.presenter.StyleOnePresenter;
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

public class FragmentStyleOne extends BaseFragment<StyleOnePresenter> implements PictureContract.View,IMainStatus,View.OnKeyListener {

    @BindView(R.id.picture_tv_recycle_view)
    TvRecyclerView mTvRecyclerView;

    ListAdapter listAdapter;
    List<Element> elements;
    @Override
    public int getContentLayout() {
        return R.layout.fragment_picture;
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
        setListener();

        listAdapter = new ListAdapter<Element>(getActivity(),R.layout.launcher_item) {
            @Override
            public void onBindItemHolder(CommonRecyclerViewHolder helper, Element item, int position) {
                helper.itemView.setTag(item.getTag());
                helper.itemView.setOnKeyListener(FragmentStyleOne.this);
                helper.getHolder().setText(R.id.launcher_item_name,item.getTag());
            }
        };
        elements = new ArrayList<Element>();
        listAdapter.setDatas(elements);
        mTvRecyclerView.setSpacingWithMargins(50, 50);
        mTvRecyclerView.setAdapter(listAdapter);
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
        List<Element> daoElement = mPresenter.getElements();
        listAdapter.clearDatas();
        listAdapter.appendDatas(daoElement);
        listAdapter.notifyDataSetChanged();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mTvRecyclerView.setSelection(0);
            }
        },100);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void TimeUpdate(String time) {
    }

    @Override
    public void netStatuUpdate(int status) {

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

}
