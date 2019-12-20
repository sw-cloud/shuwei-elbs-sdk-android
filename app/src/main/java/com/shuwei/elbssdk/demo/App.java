package com.shuwei.elbssdk.demo;

import android.app.Application;

import com.shuwei.elbssdk.demo.utils.Utils;
import com.szshuwei.x.collect.SWLocationClient;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Utils.init(this);
        // 初始化场景识别SDK，如果需要开启开发者日志，第二个参数传: true
        SWLocationClient.initialization(this, true);
    }
}
