package com.example.app.lyrics.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.app.lyrics.helper.LrcHelper;
import com.example.app.lyrics.model.LyricModel;

import java.util.List;
import java.util.TreeMap;


/**
 * @author LIUYAN
 * @date 2016/7/6 0006
 * @time 11:52
 */
public class LrcService extends Service {

    private MediaPlayer mMediaPlayer;

    private int currentTime;
    private int duration;
    private int mIndex;

    private TreeMap<Integer, LyricModel> mLyricObjectMap;
    private List<LyricModel> mLyricList;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void initLrc() {
        mLyricList = LrcHelper.getInstance().getLyric();
        mHandler.post(new Runnable() {
            @Override
            public void run() {

            }
        });

    }

    private Handler mHandler = new Handler();

    public int getLrcIndex() {
        if (mMediaPlayer.isPlaying()) {
            currentTime = mMediaPlayer.getCurrentPosition();
            duration = mMediaPlayer.getDuration();
        }
        if (currentTime < duration) {
            for (int i = 0; i < mLyricList.size(); i++) {
                if (i < mLyricList.size() - 1) {
                    if (currentTime < mLyricList.get(i).getStartTime() && i == 0) {
                        mIndex = i;
                    }
                    if (currentTime > mLyricList.get(i).getStartTime() && currentTime < mLyricList.get(i + 1).getStartTime()) {
                        mIndex = i;
                    }
                }
                if (i == mLyricList.size() - 1 && currentTime > mLyricList.get(i).getStartTime()) {
                    mIndex = i;
                }
            }
        }
        return mIndex;
    }
}
