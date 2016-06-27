package com.example.app.alarm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.app.R;

public class AlarmNoteActivity extends AppCompatActivity {

    private static final int INTENT_ADD_EVENT_REQUEST_CODE = 1;

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_note);

        mListView = (ListView) findViewById(R.id.list_view);
    }

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

        }
    }
}
