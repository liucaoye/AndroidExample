package com.example.app.lockscreen.activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.app.R;
import com.example.app.lockscreen.receiver.DeviceManagerBc;

public class LockSreenActivity extends AppCompatActivity implements View.OnClickListener{

    private DevicePolicyManager devicePolicyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_sreen);

        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);

        findViewById(R.id.button_register).setOnClickListener(this);
        findViewById(R.id.button_unregister).setOnClickListener(this);
        findViewById(R.id.button_lock_sreen).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_register:
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, new ComponentName(this, DeviceManagerBc.class));
                startActivity(intent);
                break;
            case R.id.button_unregister:
                devicePolicyManager.removeActiveAdmin(new ComponentName(this, DeviceManagerBc.class));
                break;
            case R.id.button_lock_sreen:
                devicePolicyManager.lockNow();
                break;

        }
    }
}
