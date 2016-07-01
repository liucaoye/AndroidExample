package com.example.app.alarm.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.app.ExampleApplication;
import com.example.app.alarm.model.AlarmEventModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LIUYAN
 * @date 2016/6/29 0029
 * @time 15:46
 */
public class AlarmNoteDB {

    private static AlarmNoteDB mAlarmNoteDB;

    private SQLiteDatabase mDatabase;

    public AlarmNoteDB() {
        mDatabase = NoteDBHelper.getInstance().getWritableDatabase();
    }

    public static AlarmNoteDB getInstance() {
        if (mAlarmNoteDB == null) {
            synchronized (AlarmNoteDB.class) {
                if (mAlarmNoteDB == null) {
                    mAlarmNoteDB = new AlarmNoteDB();
                }
            }
        }
        return mAlarmNoteDB;
    }


    public long insert(long time, String event) {
        ContentValues values = new ContentValues();
        values.put(NoteDBHelper.NOTE_TABLE_FIELD_TIME, time + "");
        values.put(NoteDBHelper.NOTE_TABLE_FIELD_EVENT, event);
        return mDatabase.insert(NoteDBHelper.TABLE_NAME, null, values);
    }

    public void delete(AlarmEventModel model) {
        mDatabase.delete(NoteDBHelper.TABLE_NAME, "where _id=?", new String[] {model.getId() + ""});
    }

    public List<AlarmEventModel> getAlarmList() {
        List<AlarmEventModel> list = new ArrayList<>();

        // TODO: execSQL 和 rawQuery区别
        //      1、db.execSQL 不带返回值
        //      2、db.rawQuery("select * from person where name like ? and age=?", new String[] {"%xx%", "4"});
        Cursor cursor = mDatabase.rawQuery("select * from " + NoteDBHelper.TABLE_NAME, null);
        while (cursor.moveToNext()) {
            long id = cursor.getLong(0); // 获取第一列的值，第一列的索引从0开始
            String time = cursor.getString(1);
            String event = cursor.getString(2);
            list.add(new AlarmEventModel(id, Long.valueOf(time), event));
        }

        return list;
    }

}
