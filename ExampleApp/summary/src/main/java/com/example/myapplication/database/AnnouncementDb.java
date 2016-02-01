package com.example.myapplication.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author LIUYAN
 * @date 2016/1/29 0029
 * @time 14:40
 */
public class AnnouncementDb {

    private static final String TAG = AnnouncementDb.class.getSimpleName();
    private static final String TABLE_NAME = "Announcement";
    private static final String TABLE_ANNOUNCEMENT_ID= "Announcement_Id";
    private SQLiteDatabase db;
    private volatile static AnnouncementDb announcementDb;


    private AnnouncementDb() {
        try {
            this.db = DBHelper.getInstance().getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 单例模式
    public static AnnouncementDb getInstance() {
        if (null == announcementDb) {
            synchronized (AnnouncementDb.class) {
                if (null == announcementDb){//单例双重判断
                    announcementDb = new AnnouncementDb();
                }
            }
        }
        return announcementDb;
    }

    /**
     * 获取已读公告Id列表
     *
     * @return
     */
    public List<String> getReadedAnnouncementIds() {

        String sql = "select A.Announcement_Id from Announcement A ";
        List<String> result = new ArrayList<String>();
        Cursor cursor = null;

        try {
            cursor = DBHelper.getInstance().query(sql);

            while (cursor.moveToNext()) {
                int index = cursor.getColumnIndex(TABLE_ANNOUNCEMENT_ID);
                if (!cursor.isNull(index)) {
                    result.add(cursor.getString(index));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }

        return result;
    }

    public void saveReadedAnnouncementId(String announcementId) {
        ContentValues values = new ContentValues();
        values.put(TABLE_ANNOUNCEMENT_ID, announcementId);
        if (null == db) {
            return;
        }
        db.insert(TABLE_NAME, null, values);
    }

    /**
     * 使用contentValue批量添加数据
     *
     * */
    public void saveIdByContentValue(Set<String> stringSet) {
        if (stringSet == null) {
            return;
        }
        List<ContentValues> contentValuesList = new ArrayList<>();
        for (String announcementId : stringSet) {
            ContentValues values = new ContentValues();
            values.put(TABLE_ANNOUNCEMENT_ID, announcementId);
        }

        for (ContentValues values : contentValuesList) {
            db.insert(TABLE_NAME, null, values);
        }

    }

    public void saveIdByContentValueTransaction(Set<String> stringSet) {
        if (stringSet == null) {
            return;
        }
        List<ContentValues> contentValuesList = new ArrayList<>();
        for (String announcementId : stringSet) {
            ContentValues values = new ContentValues();
            values.put(TABLE_ANNOUNCEMENT_ID, announcementId);
        }

        beginTransaction();
        for (ContentValues values : contentValuesList) {
            db.insert(TABLE_NAME, null, values);
        }

        setTransactionSuccessful();
        endTransaction();

    }

    /**
     * 使用SQLiteStatement批量添加数据
     * */
    public void saveIdByStatement(Set<String> stringSet) {
        if (stringSet == null) {
            return;
        }
        String sql_insert = "INSERT INTO " + TABLE_NAME + " (" + TABLE_ANNOUNCEMENT_ID + ") VALUES(?)";
        SQLiteStatement statement = db.compileStatement(sql_insert);
        for (String announcementId : stringSet) {
            statement.bindString(1, announcementId);
            statement.executeInsert();
        }
    }

    public void saveIdByStatementTransaction(Set<String> stringSet) {
        if (stringSet == null) {
            return;
        }
        beginTransaction();
        String sql_insert = "INSERT INTO " + TABLE_NAME + " (" + TABLE_ANNOUNCEMENT_ID + ") VALUES(?)";
        for (String announcementId : stringSet) {
            SQLiteStatement statement = db.compileStatement(sql_insert);
            statement.bindString(1, announcementId);
            statement.executeInsert();
        }
        setTransactionSuccessful();
        endTransaction();
    }

    /**
     * 开启事务
     */
    public void beginTransaction() {
        db.beginTransaction();
    }

    /**
     * 返回当前数据库连接是否在事务中
     *
     * @return
     */
    public boolean inTransaction() {
        return db.inTransaction();
    }

    /**
     * 标识当前的事务为成功
     */
    public void setTransactionSuccessful() {
        db.setTransactionSuccessful();
    }

    /**
     * 结束事务
     */
    public void endTransaction() {
        db.endTransaction();
    }
}