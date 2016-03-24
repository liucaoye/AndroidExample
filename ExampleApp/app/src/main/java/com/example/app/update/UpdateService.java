package com.example.app.update;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.app.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author LIUYAN
 * @date 2016/3/22 0022
 * @time 17:54
 */
public class UpdateService extends Service {

    private static final int NOTIFICATION_ID_UPDATE_APK = 0;

    private String mFileName;
    private File mUpdateDir;

    private NotificationManager mNotificationManager;
    private Notification mNotification;
    private Notification.Builder mBuilder;
    private PendingIntent mPendingIntent;
    private Intent mIntent;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mFileName = "test.jpg";
        mUpdateDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator + mFileName);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mIntent = new Intent(this, UpdateActivity.class);
        mPendingIntent = PendingIntent.getActivity(this, 0, mIntent, 0);

        mBuilder = new Notification.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setTicker("开始下载");
        mBuilder.setContentTitle(intent.getStringExtra("title"));
        mBuilder.setContentText("0%");
        mBuilder.setContentIntent(mPendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mNotification = mBuilder.build();
        } else {
            mNotification = mBuilder.getNotification();
        }

        mNotificationManager.notify(NOTIFICATION_ID_UPDATE_APK, mNotification);
        String downloadUrl = "http://img.hb.aicdn.com/c0b35eed3ce270da5b0c6092a17a8ceb5df317052866-gx7Kbx_fw580";
//        String downloadUrl = "http://softfile.3g.qq.com:8080/msoft/179/1105/10753/MobileQQ1.0(Android)_Build0198.apk";
        downloadFile(downloadUrl, mFileName);


        return super.onStartCommand(intent, flags, startId);
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

//                    Message startMsg = new Message();
//                    startMsg.what = 0;
//                    startMsg.arg1 = size;
//                    mHandler.sendMessage(startMsg);

                    OutputStream outputStream = new FileOutputStream(mUpdateDir);
                    int len = 0;
                    byte buffer[] = new byte[1024 * 4];
                    int hasRead = 0;
                    while ((len = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer);
                        hasRead += len;
                        Message progressMsg = new Message();
                        progressMsg.what = 1;
                        progressMsg.arg1 = hasRead * 100 / size;
                        mHandler.sendMessage(progressMsg);
                    }
                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();
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

                    break;
                case 1:
                    mBuilder.setContentText(msg.arg1 + "%");
                    mNotification = mBuilder.getNotification();
                    mNotificationManager.notify(NOTIFICATION_ID_UPDATE_APK, mNotification);
                    break;
                case 2:
                    Toast.makeText(getBaseContext(), "下载完成", Toast.LENGTH_SHORT).show();
                    installApk();
                    stopSelf();
                    break;
            }
        }
    };

    private void installApk() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(mUpdateDir), "application/vnd.android.package-archive");

        mPendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        mBuilder.setContentText("下载完成，点击安装");
        mBuilder.setContentIntent(mPendingIntent);
        mNotification = mBuilder.getNotification();
        mNotificationManager.notify(NOTIFICATION_ID_UPDATE_APK, mNotification);
    }
}
