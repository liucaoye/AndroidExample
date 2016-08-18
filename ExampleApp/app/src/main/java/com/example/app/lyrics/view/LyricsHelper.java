package com.example.app.lyrics.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.example.app.R;
import com.example.app.lyrics.model.LyricModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LIUYAN
 * @date 2016/1/18 0018
 * @time 18:50
 */
public class LyricsHelper {

    private static final String LYRIC_TITLE_MARK = "ti:";
    private static final String LYRIC_AUTHOR_MARK = "ar:";
    private static final String LYRIC_ALBUM_MARK = "al:";
    private static final String LYRIC_LEFT_BRACKETS_MARK = "[";
    private static final String LYRIC_RIGHT_BRACKETS_MARK = "]";
    private static final String LYRIC_COLON = ":";

    private int mLrcStartTime;

    private boolean mIsRunning;

    private List<LyricModel> mLyricList = null;

    private Context mContext;
    private Handler mHandler;

    private int mCurListIndex;

    public LyricsHelper(Context context, Handler handler) {
        mLyricList = new ArrayList<>();
        mContext = context;
        mHandler = handler;

    }

    public void start() {
        mIsRunning = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mIsRunning) {
                    if (mLyricList.size() == 0) {
                        readLyc();
                    } else if ( mLyricList.size() > 0) {

                        for (int i = mCurListIndex; i < mLyricList.size(); i++) {
                            if(mIsRunning) {
                                mCurListIndex = i;
                                Message msg = new Message();
                                msg.arg1 = 1;
                                msg.obj = mLyricList.get(i).getLrc();
                                mHandler.sendMessage(msg);
                                Log.d("==TAG==", "duration: " + mLyricList.get(i).getDuration() + ", lrc: " + msg.obj);
                                try {
                                    Thread.sleep(mLyricList.get(i).getDuration());
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                break;
                            }


                        }
                    }
                }
            }
        }).start();
    }

    public void pause() {
        mIsRunning = false;
    }

    public void stop() {
        mIsRunning = false;
        mLyricList.clear();
    }

    private void readLyc() {
        InputStream input = null;
        BufferedReader buf = null;
        try {
            input = mContext.getResources().openRawResource(R.raw.lovelrc);
            buf = new BufferedReader(new InputStreamReader(input));

            String content = null;
            while ((content = buf.readLine()) != null) {
                readLineData(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if (input != null) {
                    input.close();
                }
                if (buf != null) {
                    buf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void readLineData(String lrcContent) {
        if (TextUtils.isEmpty(lrcContent)) {
            return;
        }
        String lrc = null;
        int duration = 0;
        int startTime = 0;

        if (lrcContent.contains(LYRIC_TITLE_MARK)) {
            lrc = lrcContent.substring(lrcContent.indexOf(LYRIC_COLON) + 1, lrcContent.indexOf(LYRIC_RIGHT_BRACKETS_MARK));
            duration = 200;
            startTime = 0;
            Log.d("==TAG==", "歌名：" + lrc);


        } else if (lrcContent.contains(LYRIC_ALBUM_MARK)) {
            lrc = lrcContent.substring(lrcContent.indexOf(LYRIC_COLON) + 1, lrcContent.indexOf(LYRIC_RIGHT_BRACKETS_MARK));
            duration = 200;
            startTime = 400;
            Log.d("==TAG==", "谱曲：" + lrc);
        } else if (lrcContent.contains(LYRIC_AUTHOR_MARK)) {
            lrc = lrcContent.substring(lrcContent.indexOf(LYRIC_COLON) + 1, lrcContent.indexOf(LYRIC_RIGHT_BRACKETS_MARK));
            duration = 200;
            startTime = 200;
            Log.d("==TAG==", "作者：" + lrc);
        } else {
            /**
             一行歌词只有一个时间的  例如：徐佳莹   《我好想你》
             [01:15.33]我好想你 好想你

             一行歌词有多个时间的  例如：草蜢 《失恋战线联盟》
             [02:34.14][01:07.00]当你我不小心又想起她
             [02:45.69][02:42.20][02:37.69][01:10.60]就在记忆里画一个叉
             **/

            // [02:34.14][01:07.00]  转换成 02:34.14]01:07.00]
            lrc = lrcContent.substring(lrcContent.lastIndexOf(LYRIC_RIGHT_BRACKETS_MARK) + 1, lrcContent.length());

            String timeStr = null;
            if (TextUtils.isEmpty(lrc)) {
                timeStr = lrcContent;
            } else {
                timeStr = lrcContent.substring(0, lrcContent.indexOf(lrc));
            }

            Log.d("==TAG==", timeStr + lrc);

            timeStr = timeStr.replace(LYRIC_LEFT_BRACKETS_MARK, "");
            String[] array = timeStr.split(LYRIC_RIGHT_BRACKETS_MARK);

            for (int i = 0; i < array.length; i++) {
                // 02:34.14
                int currentTime = getMillisecond(array[i]);
                //  逻辑有问题
                if (mLyricList.size() > 0) {
                    mLyricList.get(mLyricList.size() - 1).setDuration(currentTime - mLrcStartTime);
                }
                LyricModel object = new LyricModel();
                object.setStartTime(currentTime);
                object.setLrc(lrc);
                mLyricList.add(object);

                mLrcStartTime = currentTime;
            }
        }


//        LyricModel object = new LyricModel();
//        if (duration >= 0) {
//            object.setDuration(duration);
//        }
//        object.setStartTime(startTime);
//        object.setLrc(lrc);
//        mLyricObjectMap.put(startTime, object);
    }

    private int getMillisecond(String time) {
        time = time.replace(":", "@").replace(".", "@");
        String[] array = time.split("@");
        // 毫秒ms
        int currentTime =  (Integer.valueOf(array[0]) * 60 + Integer.valueOf(array[1])) * 1000 + Integer.valueOf(array[2]);
        return currentTime;
    }
}
