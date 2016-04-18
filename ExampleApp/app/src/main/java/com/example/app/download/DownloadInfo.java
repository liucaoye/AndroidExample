package com.example.app.download;

/**
 * @author LIUYAN
 * @date 2016/4/18 0018
 * @time 11:31
 */
public class DownloadInfo {

    /**
     * 下载器ID
     * */
    private int mThreadId;
    private int mStartPos;
    private int mEndPos;
    private int mCompleteSize;
    private String mUrl;

    public DownloadInfo() {
    }

    public DownloadInfo(int threadId, int startPos, int endPos, int completeSize, String url) {
        mThreadId = threadId;
        mStartPos = startPos;
        mEndPos = endPos;
        mCompleteSize = completeSize;
        mUrl = url;
    }

    public int getThreadId() {
        return mThreadId;
    }

    public void setThreadId(int threadId) {
        mThreadId = threadId;
    }

    public int getStartPos() {
        return mStartPos;
    }

    public void setStartPos(int startPos) {
        mStartPos = startPos;
    }

    public int getEndPos() {
        return mEndPos;
    }

    public void setEndPos(int endPos) {
        mEndPos = endPos;
    }

    public int getCompleteSize() {
        return mCompleteSize;
    }

    public void setCompleteSize(int completeSize) {
        mCompleteSize = completeSize;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    public String toString() {
        return "DownloadInfo [threadId="+ mThreadId
                + ", startPos=" + mStartPos
                + ", endPos=" + mEndPos
                + ", completeSize=" + mCompleteSize
                + ", url=" + mUrl + " ]";
    }
}
