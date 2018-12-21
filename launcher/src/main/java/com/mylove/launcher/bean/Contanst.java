package com.mylove.launcher.bean;

import android.os.Environment;

import com.mylove.module_base.base.BaseApplication;
import com.mylove.module_base.utils.SystemUtils;

import java.io.File;

/**
 * Created by Administrator on 2018/8/18.
 */

public class Contanst {
    public static final String DEFAULT_DATA = "default_data";

    public static final int SERVER_PORT = 9999;
    public static final String SERVER_DIR = "launcher";
    public static final String UPLOAD_PATH = "upload";

    public static String APP_DIR = BaseApplication.getAppContext().getFilesDir().getPath()
        + File.separator+SERVER_DIR + File.separator +UPLOAD_PATH + File.separator;

    public static String getServerPath(String ip){
        String url = String.format("http://%s:%d",ip, Contanst.SERVER_PORT);
        return url;
    }
}
