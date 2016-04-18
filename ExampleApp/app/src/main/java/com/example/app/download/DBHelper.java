package com.example.app.download;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author LIUYAN
 * @date 2016/4/14 0014
 * @time 17:25
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "download.db";
    private static final int DB_VERSION = 1;
    public static final String KTableName = "download_info";
    public static final String KFieldNameThreadId = "thread_id";
    public static final String KFieldNameStartPos = "start_pos";
    public static final String KFieldNameEndPos = "end_pos";
    public static final String KFieldNameCompleteSize = "complete_size";
    public static final String KFieldNameUrl = "url";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + KTableName + "(_id integer PRIMARY KEY AUTOINCREMENT, "
                + KFieldNameThreadId + " integer, "
                + KFieldNameStartPos + " integer, "
                + KFieldNameEndPos + " integer, "
                + KFieldNameCompleteSize + " integer,"
                + KFieldNameUrl + " char)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
