package com.example.app.alarm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.app.R;
import com.example.app.alarm.model.AlarmEventModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * @author LIUYAN
 * @date 2016/6/28 0028
 * @time 15:01
 */
public class AlarmEventAdapter extends BaseAdapter {

    private Context mContext;
    private List<AlarmEventModel> mListData;

    public AlarmEventAdapter(Context context, List<AlarmEventModel> objects) {
        this.mContext = context;
        this.mListData = objects;
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.alarm_list_item, null);
            viewHolder.timeTv = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.eventTv = (TextView) convertView.findViewById(R.id.tv_event);
            convertView.setTag(viewHolder);

        }
        viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.timeTv.setText(getTimeStr(mListData.get(position).getMilliTime()));
        viewHolder.eventTv.setText(mListData.get(position).getEvent());

        return convertView;
    }

    public void updateEvent(AlarmEventModel model) {
        mListData.add(model);
        notifyDataSetChanged();
    }

    private String getTimeStr(long millisTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millisTime);
        return dateFormat.format(calendar.getTime());
    }

    class ViewHolder {
        private TextView timeTv;
        private TextView eventTv;

    }
}
