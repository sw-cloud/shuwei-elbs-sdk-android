package com.shuwei.elbssdk.demo;

import android.app.Application;

import com.shuwei.elbssdk.demo.utils.Utils;
import com.szshuwei.x.collect.SWLocationClient;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Utils.init(this);
        // 初始化 ELB-SDK
        SWLocationClient.initialization(this, true);
    }
}
