package com.example.app.aidl.service;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.app.aidl.IMyAidlInterface;
import com.example.app.utils.LogUtils;

/**
 * @author LIUYAN
 * @date 2015/11/30
 * @time 16:38
 *
 * 调用远程service
 *
 */
public class AIDLService extends Service {

    private String data = "默认";

    private boolean running = false;

    public class AidlImpl extends IMyAidlInterface.Stub {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void getData() throws RemoteException {

        }

        @Override
        public void setData(String name) throws RemoteException {
            data = name;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new AidlImpl();
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
        new Thread() {
            @Override
            public void run() {
                super.run();
                running = true;
                while (running) {
                    Log.d("AidlService::onCreate", "-----"+ data);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
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
