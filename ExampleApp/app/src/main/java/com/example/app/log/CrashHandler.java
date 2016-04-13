package com.example.app.log;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LIUYAN
 * @date 2016/4/13 0013
 * @time 17:01
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private Map<String, String> info = new HashMap<>();

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        handleException(ex);
    }

    public boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        new Thread() {
            @Override
            public void run() {
                super.run();
            }
        }.start();
        return true;
    }

    public void collectDeviceInfo(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
