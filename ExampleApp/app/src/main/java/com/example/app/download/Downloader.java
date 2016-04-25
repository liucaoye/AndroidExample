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
import java.util.ArrayList;
import java.util.List;

/**
 * @author LIUYAN
 * @date 2016/4/14 0014
 * @time 16:20
 */
public class Downloader {

    private static final int KConnectionTime = 1000 * 60;
    public static final int KMsgInitCode = 0x10;
    public static final int KMsgDownloadingCode = 0x11;
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
    private String mLocalFilePath;
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


    public Downloader(Context context, String urlStr, String localFilePath, int threadCount, Handler handler) {
        mUrlStr = urlStr;
        mLocalFilePath = localFilePath;
        mThreadCount = threadCount;
        mHandler = handler;
        mStatus = KInitStatus;
        mDao = new Dao(context);
    }

    public boolean isDownloading() {
        return mStatus == KDownloadingStatus;
    }

    /**
     * 判断是否是第一次下载
     * */
    public boolean isFirst(String url) {
        return !mDao.isHasInfos(url);
    }

    private void initFile() {
        RandomAccessFile accessFile = null;

        try {
            URL url = new URL(mUrlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(KConnectionTime);
            mFileSize = connection.getContentLength();

            File file = new File(mLocalFilePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            // 本地访问文件
            accessFile = new RandomAccessFile(file, "rwd");
            accessFile.setLength(mFileSize);
            connection.disconnect();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (accessFile != null) {
                    accessFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void initDownloader() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                LoadInfo loadInfo = null;
                if (isFirst(mUrlStr)) {
                    initFile();
                    int range = mFileSize / mThreadCount;
                    mInfoList = new ArrayList<>();
                    for (int i = 0; i <= mThreadCount - 1; i++) {
                        DownloadInfo info = null;
                        if (i == mThreadCount - 1) {
                            // 考虑不能整除的情况
                            info = new DownloadInfo(mThreadCount - 1, (mThreadCount - 1) * range, mFileSize - 1, 0, mUrlStr);
                            mInfoList.add(info);
                        } else {
                            info = new DownloadInfo(i, i * range, (i + 1) * range - 1, 0, mUrlStr);
                        }
                        mInfoList.add(info);
                    }

                    mDao.insertInfos(mInfoList);
                    loadInfo = new LoadInfo(mFileSize, 0, mUrlStr);
                } else {
                    mInfoList = mDao.getInfos(mUrlStr);
                    int size = 0;
                    int completeSize = 0;
                    for (DownloadInfo info : mInfoList) {
                        completeSize += info.getCompleteSize();
                        size += info.getEndPos() - info.getStartPos() + 1;
                    }
                    loadInfo = new LoadInfo(size, completeSize, mUrlStr);
                }

                Message msg = Message.obtain();
                msg.what = Downloader.KMsgInitCode;
                msg.obj = loadInfo;
                mHandler.sendMessage(msg);
            }
        }).start();

    }

    public void start() {
        if (mInfoList != null) {
            if (mStatus == KDownloadingStatus) {
                return;
            }
            mStatus = KDownloadingStatus;
            for (DownloadInfo info : mInfoList) {
                new DownloadThread(info.getThreadId(), info.getStartPos(), info.getEndPos(), info.getCompleteSize(), info.getUrl()).start();
            }
        }
    }

    public void  pause() {
        mStatus = KPauseStatus;
    }

    public void complete() {
        reset();
        mDao.delete(mUrlStr);
        mDao.closeDb();

    }

    private void  reset() {
        mStatus = KInitStatus;
    }

    class DownloadThread extends Thread {
        private int threadId;
        private int startPos;
        private int endPos;
        private int completeSize;
        private String urlStr;

        public DownloadThread(int threadId, int startPos, int endPos, int completeSize, String urlStr) {
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
                URL url = new URL(urlStr);
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(KConnectionTime);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Range", "bytes=" + (startPos + completeSize) + "-" + endPos);

                randomAccessFile = new RandomAccessFile(mLocalFilePath, "rwd");
                randomAccessFile.seek(startPos + completeSize);
                inputStream = connection.getInputStream();

                byte[] buffer = new byte[4096];
                int length = 0;
                while ((length = inputStream.read(buffer)) != -1) {
                    randomAccessFile.write(buffer, 0, length);
                    completeSize += length;
                    mDao.updateInfos(threadId, completeSize, urlStr);
                    Message message = Message.obtain();
                    message.what = KMsgDownloadingCode;
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
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (randomAccessFile != null) {
                        randomAccessFile.close();
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
