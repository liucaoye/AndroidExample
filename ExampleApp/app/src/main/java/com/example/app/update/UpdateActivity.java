package com.example.app.update;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.app.R;
import com.umeng.update.UmengDownloadListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateActivity extends FragmentActivity{

    public static final String UPDATE_INFO= "update_info";
    public static final String FORCE_UPDATE = "force_update";
    public static final String CLIENT_UPDATE = "client_update";

    private ProgressBar mProgressBar;

    private UpdateResponse mUpdateResponse;
    private boolean isForceUpdate;
    private boolean isClientUpdate;
    private String downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFinishOnTouchOutside(false);
        setContentView(R.layout.activity_update);

        mProgressBar = (ProgressBar) findViewById(R.id.pb_download);
        initData();
        initListener();
//        downloadUrl = "http://apk.r1.market.hiapk.com/data/upload/apkres/2016/1_26/9/com.laijin.simplefinance_090913.apk";
//        downloadUrl = "http://mail.yinker.com/cgi-bin/download?mailid=ZL0315-fpErBVFBS6dPeyh~jaqvZ6f&filename=%BC%F2%C0%ED%B2%C62.6%D0%E8%C7%F3%CE%C4%B5%B5.pdf&sid=GvXPUgbGFPgQB1QC,7";
        downloadUrl = "http://gdown.baidu.com/data/wisegame/f98d235e39e29031/baiduxinwen.apk";
        if (isClientUpdate) {
            initClientUpdate();
        } else {
            initUmengUpdate();
        }

    }

    private void initData() {
        Intent intent = getIntent();
        mUpdateResponse = (UpdateResponse) intent.getSerializableExtra(UPDATE_INFO);
        isForceUpdate = intent.getBooleanExtra(FORCE_UPDATE, false);
        isClientUpdate = intent.getBooleanExtra(CLIENT_UPDATE, false);
    }

    private void initListener() {
        UmengUpdateAgent.setDownloadListener(new UmengDownloadListener() {
            @Override
            public void OnDownloadStart() {
            }

            @Override
            public void OnDownloadUpdate(int progress) {
                mProgressBar.setProgress(progress);
            }

            @Override
            public void OnDownloadEnd(int result, String path) {
                File file = new File(path);
                UmengUpdateAgent.startInstall(UpdateActivity.this, file);
            }
        });
    }

    private void initUmengUpdate() {
        UmengUpdateAgent.setUpdateAutoPopup(false);
        File file = UmengUpdateAgent.downloadedFile(this, mUpdateResponse);
        if (file == null) {
            UmengUpdateAgent.startDownload(this, mUpdateResponse);
        } else {
            UmengUpdateAgent.startInstall(this, file);
        }
    }

    private void initClientUpdate() {
        downloadFile(downloadUrl, "jianlc.jpg");
    }

    private void downloadFile(final String linkUrl, final String fileName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(linkUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = connection.getInputStream();
                    int size = connection.getContentLength();

                    Message startMsg = new Message();
                    startMsg.what = 0;
                    startMsg.arg1 = size;
                    mHandler.sendMessage(startMsg);

                    OutputStream outputStream = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + fileName);
                    int len = 0;
                    byte buffer[] = new byte[1024 * 4];
                    int hasRead = 0;
                    while ((len = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer);
                        hasRead += len;
                        Message progressMsg = new Message();
                        progressMsg.what = 1;
                        progressMsg.arg1 = hasRead;
                        mHandler.sendMessage(progressMsg);
                    }

                    Message endMsg = new Message();
                    endMsg.what = 2;
                    mHandler.sendMessage(endMsg);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mProgressBar.setMax(msg.arg1);
                    break;
                case 1:
                    mProgressBar.setProgress(msg.arg1);
                    break;
                case 2:
                    Toast.makeText(UpdateActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
//                    installApk();
                    break;
            }
        }
    };

    private void installApk() {
        File apkFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/jianlc.jpg");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + apkFile.toString()), "application/vnd.android.package-archive");
        startActivity(intent);
    }
}
