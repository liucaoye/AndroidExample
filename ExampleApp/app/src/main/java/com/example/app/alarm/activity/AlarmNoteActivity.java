package com.example.app.alarm.activity;

import android.content.ContentProvider;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.app.R;
import com.example.app.alarm.model.AlarmEventModel;

public class AlarmNoteActivity extends AppCompatActivity {

    private static final int INTENT_ADD_EVENT_REQUEST_CODE = 1;

    private ListView mListView;
    private ArrayAdapter<AlarmEventModel> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_note);



        initView();
        initListener();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.list_view);
        // TODO: 1、adapter特殊用法
        mAdapter = new ArrayAdapter<AlarmEventModel>(this, android.R.layout.simple_list_item_1);
        mListView.setAdapter(mAdapter);
    }

    private void initListener() {
        mListView.setOnItemLongClickListener(onItemLongClick);
    }

    private AdapterView.OnItemLongClickListener onItemLongClick =  new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("添加事件");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, AddEventActivity.class);
        startActivityForResult(intent, INTENT_ADD_EVENT_REQUEST_CODE);
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INTENT_ADD_EVENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String time = data.getStringExtra(AddEventActivity.EXTRA_KEY_TIME);
                String event = data.getStringExtra(AddEventActivity.EXTRA_KEY_EVENT);
                addEvent(time, event);
            } else if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    private void addEvent(String time, String event) {
        // 1、存入数据库 2、设置定时闹钟 3、更新listview

    }
}
