package com.example.app.alarm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.app.R;
import com.example.app.utils.LogUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddEventActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_KEY_MILLIS_TIME = "time";
    public static final String EXTRA_KEY_EVENT = "event";

    private EditText mTimeEditText;
    private EditText mEventEditText;
    private Button mCancelButton;
    private Button mSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        initView();
        initListener();
    }

    private void initView() {
        mTimeEditText = (EditText) findViewById(R.id.et_input_time);
        mEventEditText = (EditText) findViewById(R.id.et_input_event);
        mCancelButton = (Button) findViewById(R.id.btn_cancel);
        mSaveButton = (Button) findViewById(R.id.btn_save);
    }

    private void initListener() {
        mCancelButton.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.btn_save:
                sendData();
                break;
        }
    }

    private void sendData() {
        String time = mTimeEditText.getText().toString().trim();
        String event = mEventEditText.getText().toString().trim();
        if (TextUtils.isEmpty(time)) {
            Toast.makeText(this, "请输入时间", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(event)) {
            Toast.makeText(this, "请输入事件", Toast.LENGTH_SHORT).show();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        try {
            Date date = dateFormat.parse(time);
            String parseTime = dateFormat.format(date);
            LogUtils.e("input time: " + time + ", parse time: " + parseTime);
            Intent intent = new Intent();
            intent.putExtra(EXTRA_KEY_MILLIS_TIME, date.getTime());
            intent.putExtra(EXTRA_KEY_EVENT, event);
            setResult(RESULT_OK, intent);
            finish();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
