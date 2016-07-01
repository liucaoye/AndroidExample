package com.example.app;

import android.app.Application;

import com.example.app.log.CrashHandler;

/**
 * @author LIUYAN
 * @date 2016/4/14 0014
 * @time 15:00
 */
public class ExampleApplication extends Application {

    private volatile static ExampleApplication exampleApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        exampleApplication = this;
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(this);
    }

    public static ExampleApplication getInstance() {
        return exampleApplication;
    }
}
