package com.qwertynetwork.myapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private SQLiteDatabase sqlDB;
    private ContentValues sv;
    private Cursor cursor;

    public DBHelper(@Nullable Context context) {
        super(context, MyConstants.DB_NAME, null, MyConstants.VERSION_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MyConstants.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MyConstants.DROP_TABLE);
        onCreate(db);
    }

    public void openDB() {
        sqlDB = this.getWritableDatabase();
    }

    public void insertToDB(String title, String description, String uri) {
        sv = new ContentValues();
        sv.put(MyConstants.TITLE, title);
        sv.put(MyConstants.DESCRIPTION, description);
        sv.put(MyConstants.URI, uri);
        sqlDB.insert(MyConstants.TABLE_NAME, null, sv);
    }
    public List<String> getFromDB() {
        List<String> tempList = new ArrayList<>();
        cursor = sqlDB.query(MyConstants.TABLE_NAME, null, null, null,null,null,null);
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(MyConstants.TITLE));
            tempList.add(title);
        }
        cursor.close();
        return tempList;
    }

    public void closeDB() {
        this.close();
    }
}
