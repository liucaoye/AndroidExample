package com.example.app.socket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author LIUYAN
 * @date 2016/1/19 0019
 * @time 13:51
 */
public class ClientThread extends Thread {

    public static final String KEY_CONTENT = "key_content";

    private Socket socket;
    private BufferedReader bufferedReader;
    private Handler mHandler;

    public ClientThread(Socket s, Handler handler) {
        socket = s;
        mHandler = handler;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {

            while (true) {
                String content = null;

                while((content = bufferedReader.readLine()) != null) {

                    Message msg = new Message();
                    msg.obj = content;
                    mHandler.sendMessage(msg);

                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
