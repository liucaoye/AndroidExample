package com.example.myapplication.listview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 自动滚动
 *
 *
 * */
public class AutoScrollListViewActivity extends Activity {

    private static final int LIST_DATA_COUNT = 50;

    private ListView mAutoScrollListView;
    private AutoScrollAdapter mScrollAdapter;
    private List<String> mListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_scroll_list_view);

        initData();
        mAutoScrollListView = (ListView) findViewById(R.id.auto_scroll_list_view);
        mScrollAdapter = new AutoScrollAdapter(this, R.layout.item_single_text, R.id.text, mListData);
        mAutoScrollListView.setAdapter(mScrollAdapter);
        mAutoScrollListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        startScroll();
    }

    private void initData() {
        mListData = new ArrayList<>();
        for (int i = 0; i < LIST_DATA_COUNT; i++) {
            mListData.add("=== 第" + i + "行数据 ===");
        }
    }



    private void startScroll() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
            }
        }, 0, 1000);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAutoScrollListView.smoothScrollBy((int) (getResources().getDimension(R.dimen.list_view_scroll_height) / 4), 1000);
        }
    };



//    定义一个TimerTask, 让每秒执行一次操作，操作中的处理为listView.smoothScrollToPosition(index);
//    index +=4;
//    if(index >= listView.getCount()) {
//        index = 0;
//
//    }
}
