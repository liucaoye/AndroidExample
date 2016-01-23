package com.example.app.lyrics.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.app.lyrics.model.LyricObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.TreeMap;

/**
 * @author LIUYAN
 * @date 2016/1/18 0018
 * @time 18:50
 */
public class LyricView extends View {

    private static final String LYRIC_TITLE_MARK = "ti:";
    private static final String LYRIC_AUTHOR_MARK = "ar:";
    private static final String LYRIC_ALBUM_MARK = "al:";
    private static final String LYRIC_LEFT_BRACKETS_MARK = "[";
    private static final String LYRIC_RIGHT_BRACKETS_MARK = "]";

    private String mTitle;
    private String mAuthor;
    private String mAlbum;

    private TreeMap<Integer, LyricObject> mLyricObjectMap = null;

    public LyricView(Context context) {
        super(context);
        init();
    }

    public LyricView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LyricView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mLyricObjectMap = new TreeMap<>();
        getLyric();
        Log.e("TAG", mLyricObjectMap.toString());
    }

    private void getLyric() {
        InputStream in = null;
        BufferedReader buf = null;
        try {
            in = getResources().getAssets().open("naxienian.lrc");
            buf = new BufferedReader(new InputStreamReader(in));

            String data = null;
            while ((data = buf.readLine()) != null) {
                readLineData(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readLineData(String lineData) {
        if (lineData.contains(LYRIC_TITLE_MARK)) {
            mTitle = lineData.substring(lineData.indexOf(LYRIC_TITLE_MARK), lineData.indexOf(LYRIC_RIGHT_BRACKETS_MARK));
        } else if (lineData.contains(LYRIC_ALBUM_MARK)) {
            mAlbum = lineData.substring(lineData.indexOf(LYRIC_ALBUM_MARK), lineData.indexOf(LYRIC_RIGHT_BRACKETS_MARK));
        } else if (lineData.contains(LYRIC_AUTHOR_MARK)) {
            mAuthor = lineData.substring(lineData.indexOf(LYRIC_AUTHOR_MARK), lineData.indexOf(LYRIC_RIGHT_BRACKETS_MARK));
        } else {
            lineData = lineData.replace(LYRIC_LEFT_BRACKETS_MARK, "");
            String[] array = lineData.split(LYRIC_RIGHT_BRACKETS_MARK);

            for (int i = 0; i < array.length; i++) {
                LyricObject object = new LyricObject();
                int currentTime = getMillisecond(array[i]);
                object.setStartTime(currentTime);
                object.setLrc(array[array.length - 1]);
                mLyricObjectMap.put(currentTime, object);
            }
        }
    }

    private int getMillisecond(String time) {
        time = time.replace(":", "@").replace(".", "@");
        String[] array = time.split("@");
        // 毫秒ms
        int currentTime =  (Integer.valueOf(array[0]) * 60 + Integer.valueOf(array[1])) * 1000 + Integer.valueOf(array[2]) * 10;
        return currentTime;
    }
}
