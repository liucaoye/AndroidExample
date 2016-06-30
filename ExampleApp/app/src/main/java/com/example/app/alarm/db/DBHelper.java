package com.example.app.alarm.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "alarmNote";
    private static final int DB_VERSION = 1;

    // 表名称
    private static final String TABLE_NAME = "note";
    private static final String NOTE_TABLE_FIELD_TIME = "time";
    private static final String NOTE_TABLE_FIELD_EVENT = "event";

    private static DBHelper mDBHelper;
    private SQLiteDatabase mDatabase;

    public static DBHelper getInstance(Context context) {
        if (mDBHelper == null) {
            synchronized (DBHelper.class) {
                if (mDBHelper == null) {
                    mDBHelper = new DBHelper(context);
                }
            }
        }
        return mDBHelper;
    }


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mDatabase = mDBHelper.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "{_id integer primary key autoincrement, " + NOTE_TABLE_FIELD_TIME + " text, " + NOTE_TABLE_FIELD_EVENT +" text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insert(long time, String event) {
        ContentValues values = new ContentValues();
        values.put(NOTE_TABLE_FIELD_TIME, time + "");
        values.put(NOTE_TABLE_FIELD_EVENT, event);
        return mDatabase.insert(TABLE_NAME, null, values);
    }

    public void delete(AlarmEventModel model) {
        mDatabase.delete(TABLE_NAME, "where _id=?", new String[] {model.getId() + ""});
    }

    public List<AlarmEventModel> getAlarmList() {
        List<AlarmEventModel> list = new ArrayList<>();

        // TODO: execSQL 和 rawQuery区别
        //      1、db.execSQL 不带返回值
        //      2、db.rawQuery("select * from person where name like ? and age=?", new String[] {"%xx%", "4"});
        Cursor cursor = mDatabase.rawQuery("select * from " + TABLE_NAME, null);
        while (cursor.moveToNext()) {
            long id = cursor.getLong(0); // 获取第一列的值，第一列的索引从0开始
            String time = cursor.getString(1);
            String event = cursor.getString(2);
            list.add(new AlarmEventModel(id, Long.valueOf(time), event));
        }

        return list;
    }
}
