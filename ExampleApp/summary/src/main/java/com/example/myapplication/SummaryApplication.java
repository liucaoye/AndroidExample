package com.example.myapplication;

import android.app.Application;

/**
 * @author LIUYAN
 * @date 2016/1/29 0029
 * @time 14:32
 */
public class SummaryApplication extends Application {

    private volatile static SummaryApplication mSummaryApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mSummaryApplication = this;
    }

    public static SummaryApplication getInstance() {
        return mSummaryApplication;
    }
}
