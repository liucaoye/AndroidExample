package com.example.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.database.AnnouncementDb;

import java.util.HashSet;
import java.util.Set;

/**
 * 调研批量添加数据到数据库的各种方法的效率：
 *
 DATA_COUNT = 100;
 01-29 16:42:53.231 31268-31833/com.example.myapplication E/AnnouncementListActivity: 普通的逐条存储话费的时间是：518
 使用ContentValues存储花费的时间是：0               ----  花费的时间差不多，没有明显差距，相比下面的方法：有时较多，有时较少
 使用ContentValues和Transaction存储花费的时间是：1  ----  花费的时间差不多，没有明显差距，相比上面的方法：有时较多，有时较少
 使用SQLiteStatement存储花费的时间是：501
 使用SQLiteStatement和Transaction存储花费的时间是：18
 *
 *
 *
 DATA_COUNT = 5000;
 01-29 16:41:13.321 27808-28312/com.example.myapplication E/AnnouncementListActivity: 普通的逐条存储话费的时间是：74379
 使用ContentValues存储花费的时间是：84
 使用ContentValues和Transaction存储花费的时间是：37 （数据越多，较上面的方法效率越高）
 使用SQLiteStatement存储花费的时间是：61312
 使用SQLiteStatement和Transaction存储花费的时间是：377
 * */
public class AnnouncementListActivity extends Activity implements View.OnClickListener{

    private static final int DATA_COUNT = 500;

    private Button mStartBtn;
    private TextView mSpendTimeTv;

    private Set<String> mStringSet = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_list);

        initData();
        initView();
    }

    private void initData() {
        mStringSet = new HashSet<>();
        for (int i = 0; i < DATA_COUNT; i++) {
            mStringSet.add(i + "");
        }
    }

    private void initView() {
        mStartBtn = (Button) findViewById(R.id.btn_start);
        mSpendTimeTv = (TextView) findViewById(R.id.tv_spend_time);
        mStartBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        startSaveData();
    }

    private void startSaveData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long first = System.currentTimeMillis();
                for (String str : mStringSet) {
                    AnnouncementDb.getInstance().saveReadedAnnouncementId(str);
                }
                long second = System.currentTimeMillis();
                AnnouncementDb.getInstance().saveIdByContentValue(mStringSet);
                long third = System.currentTimeMillis();
                AnnouncementDb.getInstance().saveIdByContentValueTransaction(mStringSet);
                long four = System.currentTimeMillis();
                AnnouncementDb.getInstance().saveIdByStatement(mStringSet);
                long five = System.currentTimeMillis();
                AnnouncementDb.getInstance().saveIdByStatementTransaction(mStringSet);
                long six = System.currentTimeMillis();

                Log.e(AnnouncementListActivity.this.getClass().getSimpleName(), String.format(getString(R.string.spend_time_format), (second - first), (third - second), (four - third), (five - four), (six - five)));
            }
        }).start();
    }
}
