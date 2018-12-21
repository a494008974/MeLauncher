package com.mylove.launcher.i;

import android.view.View;

/**
 * Created by Administrator on 2018/8/17.
 */

public interface IMainStatus {
    void ItemViewUpdate();
    void ItemViewUpdate(View view, Object obj);

    void NetStatuChange(boolean connect);
}
