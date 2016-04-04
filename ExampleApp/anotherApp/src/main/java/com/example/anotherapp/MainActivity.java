package com.example.anotherapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.app.aidl.IMyAidlInterface;


/**
 * @author LIUYAN
 * @date 2015/11/30
 * @time 16:38
 * <p/>
 * 方法一：serviceIntent.setComponent(new ComponentName("com.example.app", "com.example.app.aidl.service.AidlService"));
 * 方法二：AIDL 和远程service通信
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ServiceConnection {

    private Button mBindBtn;
    private Button mUnbindBtn;
    private Button mContactBtn;

    private EditText mEditText;
    private Button mSyncBtn;

    private Intent serviceIntent;

    private IMyAidlInterface mBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initIntent();
        initView();
        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bindBtn:

                bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
                break;
            case R.id.unBindBtn:
                unbindService(connection);
                mBind = null;
                break;
            case R.id.contactBtn:
                break;
            case R.id.syncBtn:
                if (mBind != null) {
                    try {
                        mBind.setData(mEditText.getText().toString());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
//            mBind = (IMyAidlInterface) binder;
            mBind = IMyAidlInterface.Stub.asInterface(binder);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void initIntent() {
        serviceIntent = new Intent();
        serviceIntent.setComponent(new ComponentName("com.example.app", "com.example.app.aidl.service.AidlService"));
    }

    private void initView() {
        mBindBtn = (Button) findViewById(R.id.bindBtn);
        mUnbindBtn = (Button) findViewById(R.id.unBindBtn);
        mContactBtn = (Button) findViewById(R.id.contactBtn);

        mEditText = (EditText) findViewById(R.id.etText);
        mSyncBtn = (Button) findViewById(R.id.syncBtn);
    }

    private void initListener() {
        mBindBtn.setOnClickListener(this);
        mUnbindBtn.setOnClickListener(this);
        mContactBtn.setOnClickListener(this);
        mSyncBtn.setOnClickListener(this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
