package com.example.app.aidl.service;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.app.utils.LogUtils;

/**
 * Author: LY
 * Date: 15/11/29
 */

public class AIDLService extends Service {

    private AidlBinder binder = new AidlBinder();

    class AidlBinder extends Binder {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
        LogUtils.printMethod();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.printMethod();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.printMethod();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.printMethod();
        return super.onStartCommand(intent, flags, startId);
    }
}
