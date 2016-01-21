package com.qihoo.permmgr;

import android.app.Application;

/**
 * 武汉虹旭
 * Created by YiuChoi on 2016/1/21 0021.
 */
public class HookApplication extends Application {

    private static HookApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static HookApplication getInstance() {
        return instance;
    }
}
