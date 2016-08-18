package com.example.app.aidl.activity;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.app.R;
import com.example.app.aidl.service.LocalService;
import com.example.app.utils.LogUtils;

/**
 * Author: LY
 * Date: 15/11/29
 */

public class AIDLActivity extends Activity implements View.OnClickListener{

    private Button mBindBtn;
    private Button mUnbindBtn;
    private Button mContactBtn;

    private Intent serviceIntent;

    private LocalService localService;

    private static final String EXTRA_DATA = "data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);
        initIntent();
        initView();
        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        startService(serviceIntent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bindBtn:
                bindService(serviceIntent, connection, Service.BIND_AUTO_CREATE);
                break;
            case R.id.unBindBtn:
                unbindService(connection);
                break;
            case R.id.contactBtn:
                Toast.makeText(this, localService.getData(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void initIntent() {
        serviceIntent = new Intent();
        serviceIntent.setAction("aidl.service.local");
        serviceIntent.putExtra(EXTRA_DATA, "I am service");
    }

    private void initView() {
        mBindBtn = (Button) findViewById(R.id.bindBtn);
        mUnbindBtn = (Button) findViewById(R.id.unBindBtn);
        mContactBtn = (Button) findViewById(R.id.contactBtn);
    }

    private void initListener() {
        mBindBtn.setOnClickListener(this);
        mUnbindBtn.setOnClickListener(this);
        mContactBtn.setOnClickListener(this);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            LogUtils.printMethod();
            localService = ((LocalService.AidlBinder)binder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtils.printMethod();
        }
    };


}
