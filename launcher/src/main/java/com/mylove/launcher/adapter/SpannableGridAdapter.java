package com.mylove.launcher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mylove.module_base.adapter.CommonRecyclerViewAdapter;
import com.mylove.module_base.adapter.CommonRecyclerViewHolder;
import com.mylove.module_base.bean.SizeBean;
import com.owen.tvrecyclerview.widget.SpannableGridLayoutManager;

/**
 * Created by Administrator on 2018/4/12.
 */

public abstract class SpannableGridAdapter<T extends SizeBean> extends CommonRecyclerViewAdapter<T> {
    private RecyclerView mRecyclerView;
    private int layoutId;
    public SpannableGridAdapter(Context context, RecyclerView mRecyclerView,int layoutId) {
        super(context);
        this.mRecyclerView = mRecyclerView;
        this.layoutId = layoutId;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return layoutId;
    }

    @Override
    public void onBindItemHolder(CommonRecyclerViewHolder helper, T item, int position) {
        final View itemView = helper.itemView;
        final SpannableGridLayoutManager.LayoutParams lp =
                (SpannableGridLayoutManager.LayoutParams) itemView.getLayoutParams();
        final int colSpan = item.getColSpan();
        final int rowSpan = item.getRowSpan();
        if (lp.rowSpan != rowSpan || lp.colSpan != colSpan) {
            lp.rowSpan = rowSpan;
            lp.colSpan = colSpan;
            itemView.setLayoutParams(lp);
        }
    }

}
