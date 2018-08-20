package com.mylove.launcher.adapter;

/**
 * Created by Administrator on 2018/7/16.
 */

import android.content.Context;

import com.mylove.module_base.adapter.CommonRecyclerViewAdapter;
import com.mylove.module_base.bean.Element;


public abstract class ListAdapter<T> extends CommonRecyclerViewAdapter<T> {
    private Context mContext;
    private int layoutId;
    public ListAdapter(Context context, int layoutId) {
        super(context);
        this.mContext = context;
        this.layoutId = layoutId;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return this.layoutId;
    }

}
