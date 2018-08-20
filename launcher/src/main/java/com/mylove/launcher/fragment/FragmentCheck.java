package com.mylove.launcher.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mylove.launcher.R;


public class FragmentCheck extends DialogFragment {

    private static FragmentCheck fragmentCheck;

    public FragmentCheck() {
        // Required empty public constructor

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
        int style = DialogFragment.STYLE_NO_FRAME, theme = 0;
        setStyle(style,theme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check,container);
        return view;
    }
}
