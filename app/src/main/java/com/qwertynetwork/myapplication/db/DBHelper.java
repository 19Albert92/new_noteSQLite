package com.qwertynetwork.myapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.qwertynetwork.myapplication.model.ListItem;

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
    public List<ListItem> getFromDB(String searchText) {
        List<ListItem> tempList = new ArrayList<>();
        String selection = MyConstants.TITLE + " like ?";
        cursor = sqlDB.query(MyConstants.TABLE_NAME, null, selection, new String[]{"%" + searchText + "%"},null,null,null);
        while (cursor.moveToNext()) {
            ListItem item = new ListItem();
            String title = cursor.getString(cursor.getColumnIndex(MyConstants.TITLE));
            String disc = cursor.getString(cursor.getColumnIndex(MyConstants.DESCRIPTION));
            String uri = cursor.getString(cursor.getColumnIndex(MyConstants.URI));
            int _id = cursor.getInt(cursor.getColumnIndex(MyConstants._ID));
            item.setTitle(title);
            item.setDescription(disc);
            item.setUri(uri);
            item.setId(_id);
            tempList.add(item);
        }
        cursor.close();
        return tempList;
    }

    public void closeDB() {
        this.close();
    }

    public void deleteDBForId(int id) {
        String selection = MyConstants._ID + "=" + id;
        sqlDB.delete(MyConstants.TABLE_NAME, selection, null);
    }

    public void updateItemToId(String title, String description, String uri ,int id) {
        String selection = MyConstants._ID + "=" + id;
        sv = new ContentValues();
        sv.put(MyConstants.TITLE, title);
        sv.put(MyConstants.DESCRIPTION, description);
        sv.put(MyConstants.URI, uri);
        sqlDB.update(MyConstants.TABLE_NAME, sv ,selection, null);
    }
}
