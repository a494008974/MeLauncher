package com.mylove.launcher.fragment;

import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.zxing.WriterException;
import com.mylove.launcher.R;
import com.mylove.launcher.adapter.RecyclerAdapter;
import com.mylove.launcher.bean.Contanst;
import com.mylove.launcher.utils.EncodingHandler;
import com.mylove.module_base.adapter.CommonRecyclerViewHolder;
import com.mylove.module_base.base.BaseApplication;
import com.mylove.module_base.focus.FocusBorder;
import com.mylove.module_base.utils.SystemUtils;
import com.owen.tvrecyclerview.widget.SimpleOnItemListener;
import com.owen.tvrecyclerview.widget.TvRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class FragmentQRCode extends DialogFragment {

    private static FragmentQRCode fragmentQRCode;
    Unbinder unbinder;

    private View view;

    @BindView(R.id.qrcode_img)
    ImageView qrcodeImg;

    protected FocusBorder mFocusBorder;
    private RecyclerAdapter mAdapter;

    private PackageManager packageManager;

    public FragmentQRCode() {
        // Required empty public constructor
    }

    public static FragmentQRCode newInstance() {
        if (fragmentQRCode == null){
            fragmentQRCode = new FragmentQRCode();
        }
        return fragmentQRCode;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME,R.style.Fullscreen_QRCode);
        packageManager = getActivity().getPackageManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_qrcode,container);
        unbinder = ButterKnife.bind(this, view);
        try {
            Bitmap bitmap = EncodingHandler.createQRCode(Contanst.getServerPath(SystemUtils.getIPAddress(BaseApplication.getAppContext())),400);
            qrcodeImg.setImageBitmap(bitmap);
        } catch (WriterException e) {

        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        fragmentQRCode = null;
    }
}
