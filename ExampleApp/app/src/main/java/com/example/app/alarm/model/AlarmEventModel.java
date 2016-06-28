package com.example.app.alarm.model;

/**
 * @author LIUYAN
 * @date 2016/6/28 0028
 * @time 15:02
 */
public class AlarmEventModel {
    private String time;
    private String event;

    public AlarmEventModel(String time, String event) {
        this.time = time;
        this.event = event;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
