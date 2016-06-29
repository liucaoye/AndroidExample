package com.example.app.alarm.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.app.alarm.activity.AddEventActivity;
import com.example.app.alarm.broadcase.AlarmBroadcastReceiver;
import com.example.app.alarm.model.AlarmEventModel;

/**
 * @author LIUYAN
 * @date 2016/6/29 0029
 * @time 21:33
 */
public class AlarmHelper {

    private static AlarmHelper alarmHelper;

    private Context mContext;

    public AlarmHelper(Context context) {
        mContext = context;
    }

    public static AlarmHelper getInstance(Context context) {
        if (alarmHelper == null) {
            synchronized (AlarmHelper.class) {
                if (alarmHelper == null) {
                    alarmHelper = new AlarmHelper(context);
                }
            }
        }
        return alarmHelper;
    }


    public void setAlarm(AlarmEventModel model) {
        // TODO: alarm详情：http://blog.csdn.net/wangxingwu_314/article/details/8060312
        AlarmManager am = (AlarmManager) mContext.getSystemService(mContext.ALARM_SERVICE);
        Intent intent = new Intent(mContext, AlarmBroadcastReceiver.class);
        intent.putExtra(AddEventActivity.EXTRA_KEY_MILLIS_TIME, model.getMilliTime());
        intent.putExtra(AddEventActivity.EXTRA_KEY_EVENT, model.getEvent());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, (int) model.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);

        am.set(AlarmManager.RTC_WAKEUP, model.getMilliTime(), pendingIntent);
        am.setRepeating(AlarmManager.RTC_WAKEUP, model.getMilliTime(),  60 * 1000, pendingIntent);
    }

    public void cancelAlarm(AlarmEventModel model) {
        AlarmManager am = (AlarmManager) mContext.getSystemService(mContext.ALARM_SERVICE);
        Intent intent = new Intent(mContext, AlarmBroadcastReceiver.class);
        intent.putExtra(AddEventActivity.EXTRA_KEY_MILLIS_TIME, model.getMilliTime());
        intent.putExtra(AddEventActivity.EXTRA_KEY_EVENT, model.getEvent());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, (int) model.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.cancel(pendingIntent);
    }
}
