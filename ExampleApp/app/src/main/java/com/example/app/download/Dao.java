package com.example.app.download;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LIUYAN
 * @date 2016/4/18 0018
 * @time 11:20
 */
public class Dao {
    private DBHelper mDBHelper;
    private SQLiteDatabase mDatabase;

    public Dao(Context context) {
        mDBHelper = new DBHelper(context);
        SQLiteDatabase database = mDBHelper.getWritableDatabase();
    }

    public boolean isHasInfos(String url) {
        String sql = "select count(*) from " + DBHelper.KTableName + " where " + DBHelper.KFieldNameUrl + "=?";
        Cursor cursor = mDatabase.rawQuery(sql, new String[]{url});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count == 0;
    }

    public void saveInfos(List<DownloadInfo> infos) {
        for (DownloadInfo info : infos) {
            String sql = "insert into " + DBHelper.KTableName + "(" + DBHelper.KFieldNameThreadId + ", "
                    + DBHelper.KFieldNameStartPos + ", " + DBHelper.KFieldNameEndPos + ", " + DBHelper.KFieldNameCompleteSize + ", "
                    + DBHelper.KFieldNameUrl + ") values (?, ?, ? , ?, ?)";
            Object[] bindArgs = {info.getThreadId(), info.getStartPos(), info.getEndPos(), info.getCompleteSize(), info.getUrl()};
            mDatabase.execSQL(sql, bindArgs);
        }
    }

    public List<DownloadInfo> getInfos(String url) {
        List<DownloadInfo> list = new ArrayList<>();
        String sql = "select " + DBHelper.KFieldNameThreadId + ", "
                + DBHelper.KFieldNameStartPos + ", " + DBHelper.KFieldNameEndPos + ", " + DBHelper.KFieldNameCompleteSize + ", "
                + DBHelper.KFieldNameUrl + " from " + DBHelper.KTableName + " where " + DBHelper.KFieldNameUrl + "=?";
        Cursor cursor = mDatabase.rawQuery(sql, new String[] {url});
        while (cursor.moveToNext()) {
            DownloadInfo info = new DownloadInfo(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getString(4));
            list.add(info);
        }
        cursor.close();
        return list;
    }

    public void updateInfos(int threadId, int completeSize, String url) {
        String sql = "update " + DBHelper.KTableName + " set " + DBHelper.KFieldNameCompleteSize + "=? where "
                + DBHelper.KFieldNameThreadId + "=? and " + DBHelper.KFieldNameUrl + "=?";
        Object[] bindArgs = {completeSize, threadId, url};
        mDatabase.execSQL(sql, bindArgs);
    }

    public void delete(String url) {
        String sql = "delete from " + DBHelper.KTableName + " where " + DBHelper.KFieldNameUrl + "=?";
        Object[] bindArgs = {url};
        mDatabase.execSQL(sql, bindArgs);
        mDatabase.close();
    }

    public void closeDb() {
        mDatabase.close();
        mDBHelper.close();
    }
}
