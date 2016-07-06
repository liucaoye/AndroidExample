package com.example.app.lyrics.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.example.app.R;
import com.example.app.lyrics.service.LrcService;
import com.example.app.lyrics.view.LyricView;


/**
 * 流程：
 *      读取lrc文件，存储到内存数据中 ——> 循环draw歌词
 * */
public class LyricsActivity extends AppCompatActivity {

    private Button mPlayBtn;
    private LyricView mLyricView;
    private LrcService mLrcService;
    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics);

        mLyricView = (LyricView) findViewById(R.id.tv_lyric);
        mPlayBtn = (Button) findViewById(R.id.btn_play);


        mLrcService = new LrcService();
        serviceIntent = new Intent(this, LrcService.class);
    }

}
