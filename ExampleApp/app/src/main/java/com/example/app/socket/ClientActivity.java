package com.example.app.socket;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientActivity extends Activity implements View.OnClickListener{

    private EditText mEtMessage;
    private TextView mContentTv;
    private Button mSendBtn;
    private Button mConnectBtn;
    private Socket mSocket;
    private BufferedWriter mWriter;
    private BufferedReader mReader;

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
        mSendBtn.setOnClickListener(this);
        mConnectBtn = (Button) findViewById(R.id.btn_connect);
        mConnectBtn.setOnClickListener(this);
    }

    // --------------------------------------

    private void initSocket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mSocket == null) {
                        mSocket = new Socket("192.168.202.173", 12345);
                        outputStream = mSocket.getOutputStream();
                    }

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));

                    String content = null;
                    while((content = bufferedReader.readLine()) != null) {
                        System.out.println("client thread receiver content: " + content);
                        Message msg = new Message();
                        msg.obj = content;
                        mHandler.sendMessage(msg);
                    }

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
                    outputStream.write((mEtMessage.getText().toString() + "\n").getBytes("UTF-8"));
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void connect() {
        try {
            mSocket = new Socket("", 12345);
            mWriter = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
            mReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            Toast.makeText(this, "建立链接！", Toast.LENGTH_SHORT).show();
            AsyncTask<Void, String, Void> read = new AsyncTask<Void, String, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    String line;
                    try {
                        while ((line = mReader.readLine()) != null) {
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
            Toast.makeText(this, "无法建立链接", Toast.LENGTH_SHORT).show();
        }
    }

    private void send() {
        try {
            mWriter.write(mEtMessage.getText().toString());
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
                connect();
                break;
        }
    }
}
