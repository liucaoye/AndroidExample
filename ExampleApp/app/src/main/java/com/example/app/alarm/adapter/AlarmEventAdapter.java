package com.example.app.alarm.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.example.app.alarm.model.AlarmEventModel;

import java.util.List;

/**
 * @author LIUYAN
 * @date 2016/6/28 0028
 * @time 15:01
 */
public class AlarmEventAdapter extends ArrayAdapter<AlarmEventModel> {

    public AlarmEventAdapter(Context context, int resource) {
        super(context, resource);
    }

    public AlarmEventAdapter(Context context, int resource, List<AlarmEventModel> objects) {
        super(context, resource, objects);
    }


}
