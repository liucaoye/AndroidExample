package com.example.app.download;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * @author LIUYAN
 * @date 2016/4/14 0014
 * @time 16:20
 */
public class Downloader {

    /**
     * 定义三种下载状态
     * */
    private static final int KInitStatus = 1;
    private static final int KDownloadingStatus = 2;
    private static final int KPauseStatus = 3;

    /**
     * 下载地址
     * */
    private String mUrlStr;
    private String mLocalFile;
    private int mThreadCount;
    private Handler mHandler;
    private int mFileSize;
    private int mStatus;
    /**
     * 工具类
     * */
    private Dao mDao;
    /**
     * 存放下载信息类的集合
     * */
    private List<DownloadInfo> mInfoList;


    public Downloader(Context context, String urlStr, String localFile, int threadCount, Handler handler) {
        mUrlStr = urlStr;
        mLocalFile = localFile;
        mThreadCount = threadCount;
        mHandler = handler;
        mStatus = KInitStatus;
    }

    public boolean isDownloading() {
        return mStatus == KDownloadingStatus;
    }

    /**
     * 判断是否是第一次下载
     * */
    public boolean isFirst(String url) {
        return mDao.isHasInfos(url);
    }

    private void init() {
        try {
            URL url = new URL(mUrlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            mFileSize = connection.getContentLength();

            File file = new File(mLocalFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            // 本地访问文件
            RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
            accessFile.setLength(mFileSize);
            accessFile.close();
            connection.disconnect();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

    }

//    public LoadInfo getDownloaderInfos() {
//
//    }

    public void download() {
        if (mInfoList != null) {
            if (mStatus == KDownloadingStatus) {
                return;
            }
            mStatus = KDownloadingStatus;
            for (DownloadInfo info : mInfoList) {
                new MyThread(info.getThreadId(), info.getStartPos(), info.getEndPos(), info.getCompleteSize(), info.getUrl()).start();
            }
        }
    }

    public void delete(String urlStr) {
        mDao.delete(urlStr);
    }

    public void  pause() {
        mStatus = KPauseStatus;
    }

    public void  reset() {
        mStatus = KInitStatus;
    }

    class MyThread extends Thread {
        private int threadId;
        private int startPos;
        private int endPos;
        private int completeSize;
        private String urlStr;

        public MyThread(int threadId, int startPos, int endPos, int completeSize, String urlStr) {
            this.threadId = threadId;
            this.startPos = startPos;
            this.endPos = endPos;
            this.completeSize = completeSize;
            this.urlStr = urlStr;
        }

        @Override
        public void run() {
            HttpURLConnection connection = null;
            RandomAccessFile randomAccessFile = null;
            InputStream inputStream = null;
            try {
                URL url = new URL(mUrlStr);
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Range", "bytes=" + (startPos + completeSize) + "-" + endPos);

                randomAccessFile = new RandomAccessFile(mLocalFile, "rwd");
                randomAccessFile.seek(startPos + completeSize);
                inputStream = connection.getInputStream();
                byte[] buffer = new byte[4096];
                int length = -1;
                while ((length = inputStream.read(buffer)) != -1) {
                    randomAccessFile.write(buffer, completeSize, length);
                    completeSize += length;
                    mDao.updateInfos(threadId, completeSize, urlStr);
                    Message message = Message.obtain();
                    message.what = 1;
                    message.obj = urlStr;
                    message.arg1 = length;
                    mHandler.sendMessage(message);
                    if (mStatus == KPauseStatus) {
                        return;
                    }
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                    randomAccessFile.close();
                    connection.disconnect();
                    mDao.closeDb();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
