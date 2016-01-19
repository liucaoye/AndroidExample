package com.example.app.socket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.app.R;

import java.net.Socket;

public class ClientActivity extends AppCompatActivity {

    private EditText mEtIp;
    private EditText mEtMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        mEtIp = (EditText) findViewById(R.id.et_ip);
        mEtMessage = (EditText) findViewById(R.id.et_send_message);

        findViewById(R.id.btn_connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect();
            }
        });

        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
    }

    // --------------------------------------
    Socket mSocket = null;
    public void connect() {
        mSocket = new Socket();
    }
    public void send() {

    }
}
