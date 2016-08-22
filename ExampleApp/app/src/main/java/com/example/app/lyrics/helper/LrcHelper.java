package com.example.app.lyrics.helper;

import android.content.Context;
import android.util.Log;

import com.example.app.ExampleApplication;
import com.example.app.lyrics.model.LyricModel;
import com.example.app.lyrics.model.LyricModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * @author LIUYAN
 * @date 2016/7/6 0006
 * @time 11:35
 */
public class LrcHelper {

    private static final String LYRIC_TITLE_MARK = "ti:";
    private static final String LYRIC_AUTHOR_MARK = "ar:";
    private static final String LYRIC_ALBUM_MARK = "al:";
    private static final String LYRIC_LEFT_BRACKETS_MARK = "[";
    private static final String LYRIC_RIGHT_BRACKETS_MARK = "]";

    private String mTitle;
    private String mAuthor;
    private String mAlbum;

    private static LrcHelper mLrcHelper;
    private Context mContext;

    public LrcHelper(Context context) {
        mContext = context;
        init();
    }

    public static LrcHelper getInstance() {
        if (mLrcHelper == null) {
            synchronized (LrcHelper.class) {
                if (mLrcHelper == null) {
                    mLrcHelper = new LrcHelper(ExampleApplication.getInstance().getApplicationContext());
                }
            }
        }
        return mLrcHelper;
    }

    private void init() {
    }

    public List<LyricModel> getLyric() {

        List<LyricModel> list = new ArrayList<>();
        InputStream in = null;
        BufferedReader buf = null;
        try {
            in = mContext.getResources().getAssets().open("naxienian.lrc");
            buf = new BufferedReader(new InputStreamReader(in));

            String data = null;
            while ((data = buf.readLine()) != null) {
                readLineData(list, data);
            }
            in.close();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<LyricModel> readLineData(List<LyricModel> list, String lineData) {
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
                LyricModel object = new LyricModel();
                int currentTime = getMillisecond(array[i]);
                object.setStartTime(currentTime);
                object.setLrc(array[array.length - 1]);
                list.add(object);
            }
        }
        return list;
    }

    private int getMillisecond(String time) {
        time = time.replace(":", "@").replace(".", "@");
        String[] array = time.split("@");
        // 毫秒ms
        int currentTime =  (Integer.valueOf(array[0]) * 60 + Integer.valueOf(array[1])) * 1000 + Integer.valueOf(array[2]) * 10;
        return currentTime;
    }

}
