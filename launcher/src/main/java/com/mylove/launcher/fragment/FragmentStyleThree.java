package com.mylove.launcher.fragment;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.VideoView;

import com.mylove.launcher.R;
import com.mylove.launcher.adapter.SpannableGridAdapter;
import com.mylove.launcher.bean.BeanUtils;
import com.mylove.launcher.bean.LchItemBean;
import com.mylove.launcher.component.DaggerLauncherComponent;
import com.mylove.launcher.contract.StyleContract;
import com.mylove.launcher.i.IMainAction;
import com.mylove.launcher.i.IMainStatus;
import com.mylove.launcher.module.LauncherModule;
import com.mylove.launcher.presenter.StylePresenter;
import com.mylove.module_base.adapter.CommonRecyclerViewAdapter;
import com.mylove.module_base.adapter.CommonRecyclerViewHolder;
import com.mylove.module_base.base.BaseFragment;
import com.mylove.module_base.bean.Element;
import com.mylove.module_base.component.ApplicationComponent;
import com.mylove.module_base.helper.ImageLoaderHelper;
import com.mylove.module_base.module.ApplicationModule;
import com.owen.tvrecyclerview.widget.SimpleOnItemListener;
import com.owen.tvrecyclerview.widget.TvRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/8/15.
 */

public class FragmentStyleThree extends BaseFragment<StylePresenter> implements StyleContract.View,View.OnKeyListener,IMainStatus {

    @BindView(R.id.tv_recycler_view)
    TvRecyclerView tvRecyclerView;

    private CommonRecyclerViewAdapter mAdapter;
    List<Element> elements;
    private PackageManager packageManager;
    String urlPath;
    @Override
    public int getContentLayout() {
        return R.layout.fragment_three;
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
        tvRecyclerView.setSpacingWithMargins(2, 5);
        mAdapter = new SpannableGridAdapter<Element>(getActivity(),tvRecyclerView,R.layout.launcher_three_item){
            @Override
            public void onBindItemHolder(CommonRecyclerViewHolder helper, Element item, int position) {
                super.onBindItemHolder(helper, item, position);
                helper.itemView.setTag(item.getTag());
                helper.itemView.setTag(R.id.tag,position);
                ImageView imageView = (ImageView)helper.getHolder().getView(R.id.launcher_image_view);
                imageView.setVisibility(View.VISIBLE);
                VideoView videoView = (VideoView) helper.getHolder().getView(R.id.launcher_video_view);
                switch (position){
                    case 0:
                        imageView.setImageResource(R.drawable.module_three_img);
                        videoView.setVisibility(View.VISIBLE);
                        if(!"".equals(item.getResurl())&&item.getResurl() != null && ((IMainAction)getActivity()).fetchNetStatu()){
//                            videoView.setVideoPath("https://sharevodbd.haqu.com/bdsp/dbyy/2018022705/80ed7f79d0ea3159f275.mp4");
                            videoView.setVideoPath(item.getResurl());
//                            videoView.setVideoPath("http://192.168.16.231:9999/launcher/video?name=aaa.mp4");
                            System.out.println("<========"+item.getResurl()+"========>");
                            videoView.start();

                            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    mp.setLooping(true);
                                    mp.start();
                                }
                            });

                            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    System.out.println("setOnCompletionListener ......");
                                }
                            });

                        }
                        break;
                    default:
                        if(!"".equals(item.getResurl())&&item.getResurl() != null){
                            ImageLoaderHelper.getInstance().load(getContext(),item.getResurl(),imageView);
                        }
                        break;
                }
            }
        };
        elements = new ArrayList<Element>();
        mAdapter.setDatas(elements);
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
        ItemViewUpdate();
        mRootView.setClickable(true);
        mRootView.setFocusable(true);
        mRootView.setFocusableInTouchMode(true);
        mRootView.requestFocus();
        mRootView.setOnKeyListener(this);
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
                    ((IMainAction)getActivity()).showQRCode();
                    break;
                case KeyEvent.KEYCODE_BACK:
                    break;
                default:
                    ((IMainAction)getActivity()).showSub();
                    break;
            }
        }
        return false;
    }

    @Override
    public void ItemViewUpdate() {
        mAdapter.clearDatas();
        elements = mPresenter.getElements("3");
        if(elements.size() >= 3){
            elements = elements.subList(0,3);
        }
        mAdapter.appendDatas(BeanUtils.initSize(elements));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void ItemViewUpdate(View view, Object obj) {

    }

    @Override
    public void NetStatuChange(boolean connect) {

    }
}
