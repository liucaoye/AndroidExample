package com.example.app.alarm.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.app.ExampleApplication;
import com.example.app.alarm.model.AlarmEventModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LIUYAN
 * @date 2016/6/28 0028
 * @time 16:51
 *
 * 知识点：
 *      1. execSQL 和 rawQuery区别
 *      2. db.query的使用
 */
public class NoteDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "alarmNote";
    private static final int DB_VERSION = 1;

    // 表名称
    public static final String TABLE_NAME = "note";
    public static final String NOTE_TABLE_FIELD_TIME = "time";
    public static final String NOTE_TABLE_FIELD_EVENT = "event";

    private static NoteDBHelper mDBHelper;

    public static NoteDBHelper getInstance() {
        if (mDBHelper == null) {
            synchronized (NoteDBHelper.class) {
                if (mDBHelper == null) {
                    mDBHelper = new NoteDBHelper(ExampleApplication.getInstance().getApplicationContext());
                }
            }
        }
        return mDBHelper;
    }


    public NoteDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(_id integer primary key autoincrement, " + NOTE_TABLE_FIELD_TIME + " text, " + NOTE_TABLE_FIELD_EVENT +" text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
