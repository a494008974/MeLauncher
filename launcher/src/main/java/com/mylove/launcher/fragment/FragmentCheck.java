package com.mylove.launcher.fragment;

import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mylove.launcher.R;
import com.mylove.launcher.adapter.RecyclerAdapter;
import com.mylove.launcher.adapter.SpannableGridAdapter;
import com.mylove.launcher.bean.BeanUtils;
import com.mylove.launcher.bean.LchItemBean;
import com.mylove.module_base.adapter.CommonRecyclerViewAdapter;
import com.mylove.module_base.adapter.CommonRecyclerViewHolder;
import com.mylove.module_base.focus.FocusBorder;
import com.owen.tvrecyclerview.widget.SimpleOnItemListener;
import com.owen.tvrecyclerview.widget.TvRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class FragmentCheck extends DialogFragment {

    private static FragmentCheck fragmentCheck;
    Unbinder unbinder;

    private View view;

    @BindView(R.id.tv_recycler_view_check)
    TvRecyclerView tvRecyclerView;

    protected FocusBorder mFocusBorder;
    private RecyclerAdapter mAdapter;

    private CheckListener mCheckListener;

    List<PackageInfo> packageInfos;
    private PackageManager packageManager;

    public FragmentCheck() {
        // Required empty public constructor

    }

    public void setCheckListener(CheckListener mCheckListener) {
        this.mCheckListener = mCheckListener;
    }

    public void setPackageInfos(List<PackageInfo> packageInfos) {
        this.packageInfos = packageInfos;
    }

    public static FragmentCheck newInstance() {
        if (fragmentCheck == null){
            fragmentCheck = new FragmentCheck();
        }
        return fragmentCheck;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME,R.style.Fullscreen_Dialog);
        packageManager = getActivity().getPackageManager();
    }

    private void initFocusBorder(ViewGroup viewGroup) {
        if(null == mFocusBorder) {
            mFocusBorder = new FocusBorder.Builder()
                    .asColor()
                    .borderColor(getResources().getColor(R.color.launcher_item_shadow_color))
                    .borderWidth(TypedValue.COMPLEX_UNIT_DIP, 2)
                    .shadowColor(getResources().getColor(R.color.launcher_item_shadow_color))
                    .shadowWidth(TypedValue.COMPLEX_UNIT_DIP, 20)
                    .animDuration(180L)
                    .build(viewGroup);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_check,container);
        unbinder = ButterKnife.bind(this, view);
        initFocusBorder((ViewGroup)view);
        setListener();
        tvRecyclerView.setSpacingWithMargins(10, 10);
        mAdapter = new RecyclerAdapter<PackageInfo>(getActivity(),R.layout.launcher_item) {
            @Override
            public void onBindItemHolder(CommonRecyclerViewHolder helper, PackageInfo item, int position) {
                    helper.getHolder().setImageDrawable(R.id.launcher_item_icon,packageManager.getApplicationIcon(item.applicationInfo));
                    String name = (String) packageManager.getApplicationLabel(item.applicationInfo);
                    helper.getHolder().setText(R.id.launcher_item_name,name);
            }
        };
        mAdapter.setDatas(packageInfos);
        tvRecyclerView.setAdapter(mAdapter);
        return view;
    }

    private void setListener() {
        tvRecyclerView.setOnItemListener(new SimpleOnItemListener() {
            @Override
            public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
                onMoveFocusBorder(itemView, 1.0f, 10);
            }

            @Override
            public void onItemClick(TvRecyclerView parent, View itemView, int position) {
                if (mCheckListener != null){
                    mCheckListener.onItemClick(parent, itemView, position, mAdapter.getItem(position));
                }
            }
        });
    }

    protected void onMoveFocusBorder(View focusedView, float scale, float roundRadius) {
        if(null != mFocusBorder) {
            mFocusBorder.onFocus(focusedView, FocusBorder.OptionsFactory.get(scale, scale, roundRadius));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        fragmentCheck = null;
    }

    public interface CheckListener{
        void onItemClick(TvRecyclerView parent, View itemView, int position, Object item);
    }
}
