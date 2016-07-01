package com.example.app.alarm.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.app.R;
import com.example.app.alarm.adapter.AlarmEventAdapter;
import com.example.app.alarm.db.AlarmNoteDB;
import com.example.app.alarm.db.NoteDBHelper;
import com.example.app.alarm.model.AlarmEventModel;
import com.example.app.alarm.utils.AlarmHelper;

import java.util.ArrayList;
import java.util.List;

public class AlarmNoteActivity extends AppCompatActivity {

    private static final int INTENT_ADD_EVENT_REQUEST_CODE = 1;

    private ListView mListView;
    private AlarmEventAdapter mAdapter;
    private List<AlarmEventModel> mListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_note);

        initData();
        initView();
        initListener();
    }

    private void initData() {
        mListData = new ArrayList<>();
        mListData.addAll(AlarmNoteDB.getInstance().getAlarmList());
    }


    private void initView() {
        mListView = (ListView) findViewById(R.id.list_view);
        // TODO: 1、adapter特殊用法，待确定是否行得通
//        mAdapter = new ArrayAdapter<AlarmEventModel>(this, android.R.layout.simple_list_item_1);
        mAdapter = new AlarmEventAdapter(this, mListData);
        mListView.setAdapter(mAdapter);
    }

    private void initListener() {
        mListView.setOnItemLongClickListener(onItemLongClick);
    }

    private AdapterView.OnItemLongClickListener onItemLongClick =  new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            new AlertDialog.Builder(AlarmNoteActivity.this)
                    .setMessage("是否删除这条记录?")
                    .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 1、删除数据库中的数据 2、取消定时闹钟 3、更新listview
                            AlarmEventModel model = mListData.get(position);
                            AlarmNoteDB.getInstance().delete(model);
                            AlarmHelper.getInstance(AlarmNoteActivity.this).cancelAlarm(model);
                            mListData.remove(model);
                            mAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("添加事件");
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INTENT_ADD_EVENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                long millisTime = data.getLongExtra(AddEventActivity.EXTRA_KEY_MILLIS_TIME, 0);
                String event = data.getStringExtra(AddEventActivity.EXTRA_KEY_EVENT);
                addEvent(millisTime, event);
            }
        } else if (resultCode == RESULT_CANCELED) {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, AddEventActivity.class);
        startActivityForResult(intent, INTENT_ADD_EVENT_REQUEST_CODE);
        return false;
    }

    private void addEvent(long millisTime, String event) {
        // 1、存入数据库 2、设置定时闹钟 3、更新listview
        long id = AlarmNoteDB.getInstance().insert(millisTime, event);
        AlarmEventModel model = new AlarmEventModel(id, millisTime, event);
        mAdapter.updateEvent(model);
        AlarmHelper.getInstance(this).setAlarm(model);
    }


}
