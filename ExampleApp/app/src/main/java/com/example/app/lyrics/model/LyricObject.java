package com.example.app.lyrics.model;

/**
 * @author LIUYAN
 * @date 2016/1/18 0018
 * @time 18:39
 */
public class LyricObject {

    /**
     * 开始时间(ms)
     * */
    private int startTime;

    /**
     * 结束时间
     * */
    private int endTime;

    /**
     * 单句歌词用词
     * */
    private int duration;

    /**
     * 单句歌词
     * */
    private String lrc;

    public LyricObject() {

    }

    public LyricObject(int startTime, int endTime, int duration, String lrc) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.lrc = lrc;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getLrc() {
        return lrc;
    }

    public void setLrc(String lrc) {
        this.lrc = lrc;
    }
}
