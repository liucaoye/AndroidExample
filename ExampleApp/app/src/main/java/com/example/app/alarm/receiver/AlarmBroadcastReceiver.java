package com.example.app.alarm.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.app.R;
import com.example.app.alarm.activity.AddEventActivity;
import com.example.app.alarm.activity.AlarmNoteActivity;
import com.example.app.alarm.db.AlarmNoteDB;
import com.example.app.alarm.model.AlarmEventModel;
import com.example.app.alarm.utils.AlarmHelper;
import com.example.app.utils.LogUtils;

import java.util.Date;
import java.util.List;

/**
 * @author LIUYAN
 * @date 2016/6/29 0029
 * @time 17:07
 *
 * 添加BOOT_COMPLETED对应的action和uses-permission
 */
public class AlarmBroadcastReceiver extends BroadcastReceiver {

    private static final int NOTIFY_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            // 开机自启动
            Toast.makeText(context, "开机自启动alarm", Toast.LENGTH_SHORT).show();
            LogUtils.e("=======开机自启动alarm==========");

            List<AlarmEventModel> alarmEventModelList = AlarmNoteDB.getInstance().getAlarmList();
            for (AlarmEventModel model : alarmEventModelList) {
                AlarmHelper.getInstance(context).setAlarm(model);
            }
        } else if (intent.getAction().equals(AlarmHelper.ACTION_ALRAM_NOTE)) {
            // push通知
            Toast.makeText(context, "alarm push通知", Toast.LENGTH_SHORT).show();

            // extra
            long millisTime = intent.getLongExtra(AddEventActivity.EXTRA_KEY_MILLIS_TIME, 0);
            String event = intent.getStringExtra(AddEventActivity.EXTRA_KEY_EVENT);

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context);
            builder.setTicker("ticker: 提醒来啦~");
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentText(event + System.currentTimeMillis());
            builder.setContentTitle("定时提醒笔记本" );
            builder.setWhen(System.currentTimeMillis());

//            Notification.FLAG_INSISTENT;//让声音、振动无限循环，直到用户响应
//            Notification.FLAG_AUTO_CANCEL;//通知被点击后，自动消失
//            Notification.FLAG_NO_CLEAR;//点击'Clear'时，不清楚该通知(QQ的通知无法清除，就是用的这个

            Intent toIntent = new Intent(context, AlarmNoteActivity.class);
            Date date = new Date(System.currentTimeMillis());
            toIntent.putExtra(AddEventActivity.EXTRA_KEY_EVENT, event + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds());
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, toIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            builder.setContentIntent(pendingIntent);

            manager.notify(NOTIFY_ID, builder.getNotification());
        }
    }
}
