package com.example.app.socket;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.app.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ClientActivity extends Activity {

    private EditText mEtMessage;
    private TextView mContentTv;
    private Button mSendBtn;
    private Socket mSocket;
    private OutputStream outputStream;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String content = (String) msg.obj;
            mContentTv.append(content);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        mEtMessage = (EditText) findViewById(R.id.et_send_message);
        mContentTv = (TextView) findViewById(R.id.tv_content);

        mSendBtn = (Button) findViewById(R.id.btn_send);
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });

        initSocket();

    }

    // --------------------------------------

    private void initSocket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mSocket = new Socket("192.168.1.112", 12345);
                    new ClientThread(mSocket, mHandler).start();
                    outputStream = mSocket.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void sendData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    outputStream.write(mEtMessage.getText().toString().getBytes("UTF-8"));
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


}
