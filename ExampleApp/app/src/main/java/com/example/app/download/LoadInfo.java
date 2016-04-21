package com.example.app.download;

/**
 * @author LIUYAN
 * @date 2016/4/18 0018
 * @time 19:10
 */
public class LoadInfo {
    public int mFileSize;
    public int mCompleteSize;
    public String mUrlStr;

    public LoadInfo(int fileSize, int completeSize, String urlStr) {
        mFileSize = fileSize;
        mCompleteSize = completeSize;
        mUrlStr = urlStr;
    }

    public LoadInfo() {

    }

    public int getFileSize() {
        return mFileSize;
    }

    public void setFileSize(int fileSize) {
        mFileSize = fileSize;
    }

    public int getCompleteSize() {
        return mCompleteSize;
    }

    public void setCompleteSize(int completeSize) {
        mCompleteSize = completeSize;
    }

    public String getUrlStr() {
        return mUrlStr;
    }

    public void setUrlStr(String urlStr) {
        mUrlStr = urlStr;
    }

    @Override
    public String toString() {
        return "LoadInfo[fileSize=" + mFileSize + ", completeSize=" + mCompleteSize + ", urlStr=" + mUrlStr + "]";
    }
}
