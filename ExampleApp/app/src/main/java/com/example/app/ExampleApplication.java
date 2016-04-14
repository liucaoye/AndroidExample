package com.example.app;

import android.app.Application;

import com.example.app.log.CrashHandler;

/**
 * @author LIUYAN
 * @date 2016/4/14 0014
 * @time 15:00
 */
public class ExampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
    }
}
