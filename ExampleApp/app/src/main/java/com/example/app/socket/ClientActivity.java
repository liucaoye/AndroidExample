package com.example.app.socket;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.app.R;
import com.example.app.utils.LogUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientActivity extends Activity implements View.OnClickListener{

    private static final String ADDRESS_IP = "192.168.31.212";
    private static final int ADDRESS_PORT = 12345;

    private EditText mEtMessage;
    private TextView mContentTv;
    private Button mSendBtn;
    private Button mConnectBtn;
    private Socket mSocket;
    private BufferedWriter mWriter;
    private BufferedReader mReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        mEtMessage = (EditText) findViewById(R.id.et_send_message);
        mContentTv = (TextView) findViewById(R.id.tv_content);

        mSendBtn = (Button) findViewById(R.id.btn_send);
        mSendBtn.setOnClickListener(this);
        mConnectBtn = (Button) findViewById(R.id.btn_connect);
        mConnectBtn.setOnClickListener(this);
    }

    private void connect() {
        try {
            // TODO: 替换成本地IP
            mSocket = new Socket(ADDRESS_IP, ADDRESS_PORT);
            mWriter = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
            mReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            LogUtils.e("建立链接!");
            AsyncTask<Void, String, Void> read = new AsyncTask<Void, String, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    String line = null;
                    try {
                        LogUtils.e("读取数据 start...... ");
                        while ((line = mReader.readLine()) != null) {
                            LogUtils.e("读取数据 line: " + line);
                            publishProgress(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onProgressUpdate(String... values) {
                    mContentTv.append(values[0]);
                }
            };
            read.execute();
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e("无法建立链接!");
        }
    }

    private void send() {
        try {
            // TODO: 必须加换行符"\n"
            mWriter.write(mEtMessage.getText().toString()+ "\n");
            mWriter.flush();
            mEtMessage.setText(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                send();
                break;
            case R.id.btn_connect:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        connect();
                    }
                }).start();
                break;
        }
    }
}
