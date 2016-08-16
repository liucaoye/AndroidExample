package com.example.app.lyrics.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.app.R;
import com.example.app.lyrics.service.LrcService;
import com.example.app.lyrics.view.LyricView;


/**
 * 流程：
 *      读取lrc文件，存储到内存数据中 ——> 循环draw歌词
 * */
public class LyricsActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mPlayBtn;
    private Button mPauseBtn;
    private Button mStopBtn;
    private LyricView mLyricView;
    private LrcService mLrcService;
    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics);

        mLyricView = (LyricView) findViewById(R.id.tv_lyric);
        mPlayBtn = (Button) findViewById(R.id.btn_play);
        mPauseBtn = (Button) findViewById(R.id.btn_pause);
        mStopBtn = (Button) findViewById(R.id.btn_stop);
        initListener();

        mLrcService = new LrcService();
        serviceIntent = new Intent(this, LrcService.class);
    }

    private void initListener() {
        mPlayBtn.setOnClickListener(this);
        mPauseBtn.setOnClickListener(this);
        mStopBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play:
                startService(serviceIntent);
                break;
            case R.id.btn_pause:
                break;
            case R.id.btn_stop:
                break;
        }
    }
}
