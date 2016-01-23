package com.example.app.lyrics.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.example.app.R;


/**
 * 流程：
 *      读取lrc文件，存储到内存数据中 ——> 循环draw歌词
 * */
public class LyricsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics);


    }

}
