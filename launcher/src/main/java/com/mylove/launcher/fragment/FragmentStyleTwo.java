package com.mylove.launcher.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.mylove.launcher.R;
import com.mylove.launcher.adapter.SpannableGridAdapter;
import com.mylove.launcher.bean.BeanUtils;
import com.mylove.launcher.bean.LchItemBean;
import com.mylove.launcher.i.IMainStatus;
import com.mylove.module_base.adapter.CommonRecyclerViewAdapter;
import com.mylove.module_base.adapter.CommonRecyclerViewHolder;
import com.mylove.module_base.base.BaseFragment;
import com.mylove.module_base.component.ApplicationComponent;
import com.owen.tvrecyclerview.widget.SimpleOnItemListener;
import com.owen.tvrecyclerview.widget.TvRecyclerView;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/8/15.
 */

public class FragmentStyleTwo extends BaseFragment implements IMainStatus {

    @BindView(R.id.tv_recycler_view)
    TvRecyclerView tvRecyclerView;


    private CommonRecyclerViewAdapter mAdapter;
    @Override
    public int getContentLayout() {
        return R.layout.fragment_two;
    }

    @Override
    public void initInjector(ApplicationComponent appComponent) {

    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        setListener();

        tvRecyclerView.setSpacingWithMargins(8, 8);
        mAdapter = new SpannableGridAdapter<LchItemBean>(getActivity(),tvRecyclerView,R.layout.launcher_item){
            @Override
            public void onBindItemHolder(CommonRecyclerViewHolder helper, LchItemBean item, int position) {
                super.onBindItemHolder(helper, item, position);

            }
        };
        mAdapter.setDatas(BeanUtils.getDatas(20,"main"));
        tvRecyclerView.setAdapter(mAdapter);

    }

    private void setListener() {
        tvRecyclerView.setOnItemListener(new SimpleOnItemListener() {

            @Override
            public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
                onMoveFocusBorder(itemView, 1.05f, 10);
            }

            @Override
            public void onItemClick(TvRecyclerView parent, View itemView, int position) {
            }
        });
    }

    @Override
    public void initData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvRecyclerView.setSelection(3);
            }
        },100);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void netStatuUpdate(int status) {
//        switch (status) {
//            case ConnectivityManager.TYPE_WIFI:
//                netImageView.setImageResource(R.drawable.wifi_normal);
//                break;
//            case ConnectivityManager.TYPE_ETHERNET:
//                netImageView.setImageResource(R.drawable.net_normal);
//                break;
//            default:
//                netImageView.setImageResource(R.drawable.net_no_normal);
//                break;
//        }
    }

    @Override
    public void TimeUpdate(String time) {
//        Typeface typeFace =Typeface.createFromAsset(getActivity().getAssets(),"fonts/helvetica.ttf");
//        timeTextView.setTypeface(typeFace);
//        timeTextView.setText(time);
//        weekTextView.setText(SystemUtils.getWeek());
    }

}
