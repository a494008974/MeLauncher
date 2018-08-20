package com.mylove.launcher.component;

import com.mylove.launcher.MainActivity;
import com.mylove.launcher.fragment.FragmentStyleOne;
import com.mylove.launcher.module.LauncherModule;
import com.mylove.module_base.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Administrator on 2018/8/14.
 */
@Singleton
@Component(modules = {ApplicationModule.class, LauncherModule.class})
public interface LauncherComponent {

    void inject(MainActivity mainActivity);

    void inject(FragmentStyleOne fragmentStyleOne);
}
