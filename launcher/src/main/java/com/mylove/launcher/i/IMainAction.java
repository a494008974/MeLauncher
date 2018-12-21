package com.mylove.launcher.i;

import android.view.View;

import com.owen.tvrecyclerview.widget.TvRecyclerView;

/**
 * Created by Administrator on 2018/8/18.
 */

public interface IMainAction {
    public void showCheck(View view);

    public void showSub();

    public void showQRCode();

    public void onItemClick(TvRecyclerView parent, View itemView, int position);

    boolean fetchNetStatu();

}
