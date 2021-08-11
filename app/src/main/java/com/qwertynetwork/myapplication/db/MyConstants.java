package com.qwertynetwork.myapplication.db;

public class MyConstants {
    public static final String LIST_ITEM_INTENT = "list item intent";
    public static final String EDIT_STATE = "edit state";
    public static final String _ID = "_id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String URI = "uri";
    public static final String TABLE_NAME = "my_table";

    public static final String DB_NAME = "my_newDB.db";
    public static final int VERSION_DB = 1;

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY," +
            TITLE + " TEXT, " + URI + " TEXT, " + DESCRIPTION + " TEXT)";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final int REQUEST_CODE = 1;
}
