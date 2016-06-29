package com.example.app.alarm.model;

/**
 * @author LIUYAN
 * @date 2016/6/28 0028
 * @time 15:02
 */
public class AlarmEventModel {
    private long id;
    private long milliTime;
    private String event;

    public AlarmEventModel(long id, long milliTime, String event) {
        this.id = id;
        this.milliTime = milliTime;
        this.event = event;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMilliTime() {
        return milliTime;
    }

    public void setMilliTime(long milliTime) {
        this.milliTime = milliTime;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
