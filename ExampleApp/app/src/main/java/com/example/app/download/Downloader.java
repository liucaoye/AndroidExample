package com.example.app.download;

import android.content.Context;

import com.example.app.utils.LogUtils;

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
 * 知识点：
 *      1.  "r"      以只读方式打开。调用结果对象的任何 write 方法都将导致抛出 IOException。
 *          "rw"     打开以便读取和写入。如果该文件尚不存在，则尝试创建该文件。
 *          "rws"    打开以便读取和写入，对于 "rw"，还要求对文件的内容或元数据的每个更新都同步写入到底层存储设备。
 *          "rwd"    打开以便读取和写入，对于 "rw"，还要求对文件内容的每个更新都同步写入到底层存储设备。和"rws"比较：减少执行的 I/O 操作数量
 *              如果该文件位于本地存储设备上，那么当返回此类的一个方法的调用时，可以保证由该调用对此文件所做的所有更改均被写入该设备。
 *          这对确保在系统崩溃时不会丢失重要信息特别有用。如果该文件不在本地设备上，则无法提供这样的保证。
 *
 *
 *
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
    private static final int KStopStatus = 4;
    private static final int KCompleteStatus = 5;
    // 1. 磁盘满
    // 2. 插队
    // 3. 下载失败

    /**
     * 下载地址
     * */
    private String mUrlStr;
    private String mLocalFilePath;
    private int mThreadCount;
    private int mFileSize;
    private int mStatus;
    private boolean isSupportBreakPoint;

    private DownloadEventListener mEventListener;
    /**
     * 工具类
     * */
    private Dao mDao;
    /**
     * 存放下载信息类的集合
     * */
    private List<DownloadInfo> mInfoList;


    public Downloader(Context context, String urlStr, String localFilePath, int threadCount, DownloadEventListener listener) {
        mUrlStr = urlStr;
        mLocalFilePath = localFilePath;
        mThreadCount = threadCount;
        mEventListener = listener;
        mStatus = KInitStatus;
        mDao = new Dao(context);
        initDownloader();
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
            connection.setRequestProperty("Range", "bytes=0-");
            mFileSize = connection.getContentLength();

            LogUtils.e("初始化时返回：" + connection.getResponseCode() + ", 文件大小: " + mFileSize);
            // 如果支持断点下载，服务器返回206
            if (connection.getResponseCode() == HttpURLConnection.HTTP_PARTIAL) {
                //
                isSupportBreakPoint = true;
            } else {
                isSupportBreakPoint = false;
            }

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

    private void initDownloader() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                LoadInfo loadInfo = null;
                if (isFirst(mUrlStr)) {
                    initFile();
                    mInfoList = new ArrayList<>();

                    if (!isSupportBreakPoint) {
                        mThreadCount = 1;
                    }

                    int range = mFileSize / mThreadCount;
                    for (int i = 0; i <= mThreadCount - 1; i++) {
                        DownloadInfo info = null;
                        if (i == mThreadCount - 1) {
                            // 考虑不能整除的情况
                            info = new DownloadInfo(mThreadCount - 1, (mThreadCount - 1) * range, mFileSize - 1, 0, mUrlStr);
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
                    if (size == completeSize) {
                        mEventListener.downloadComplete();
                    }
                }

                mEventListener.downloadInit(loadInfo.getFileSize(), loadInfo.getCompleteSize());
            }
        }).start();

    }

    public String getLocalFilePath() {
        return mLocalFilePath;
    }

    public int getFileSize() {
        return mFileSize;
    }

    public int getStatus() {
        return mStatus;
    }

    public void start() {
        if (mInfoList != null) {
            if (mStatus == KDownloadingStatus || mStatus == KCompleteStatus) {
                // 完成接口
                return;
            }
            mStatus = KDownloadingStatus;

            for (DownloadInfo info : mInfoList) {
                new DownloadThread(info.getThreadId(), info.getStartPos(), info.getEndPos(), info.getCompleteSize(), info.getUrl()).start();
            }
        }
    }

    public void  pause() {
        if (mStatus == KDownloadingStatus) {
            mStatus = KPauseStatus;
        }
    }

    public void stop() {
        if (mStatus == KDownloadingStatus || mStatus == KPauseStatus) {
            mStatus = KStopStatus;
            mInfoList.clear();
            mDao.delete(mUrlStr);
            mEventListener.downloadCancel();
        }
    }

    public void complete() {
        mStatus = KCompleteStatus;
        mDao.closeDb();
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

                LogUtils.e("初始化时返回：" + connection.getResponseCode());
                randomAccessFile = new RandomAccessFile(mLocalFilePath, "rwd");
                randomAccessFile.seek(startPos + completeSize);
                inputStream = connection.getInputStream();
                byte[] buffer = new byte[4096];
                int length = 0;
                while ((length = inputStream.read(buffer)) != -1) {
                    randomAccessFile.write(buffer, 0, length);
                    completeSize += length;

                    mDao.updateInfos(threadId, completeSize, urlStr);
                    mInfoList.get(threadId).setCompleteSize(completeSize);

                    mEventListener.downloadProgress(length);
                    if (mStatus == KPauseStatus || mStatus == KStopStatus) {
                        break;
                    }
                }

            } catch (java.io.IOException e) {
                e.printStackTrace();
                mEventListener.downloadFailed();
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

    interface DownloadEventListener {
        void downloadProgress(int progress);
        void downloadInit(int fileSize, int completeSize);
        void downloadFailed();
        void downloadCancel();
        void downloadComplete();
    }
}
