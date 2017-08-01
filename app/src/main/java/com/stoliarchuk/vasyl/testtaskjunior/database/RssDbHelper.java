package com.stoliarchuk.vasyl.testtaskjunior.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by freak on 01.08.2017.
 */

public class RssDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "rss.db";

    public RssDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_RSS_TABLE = "CREATE TABLE " + RssContract.TABLE_NAME + " (" +

                RssContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                RssContract.COLUMN_TITLE + " TEXT NOT NULL, " +
                RssContract.COLUMN_CATEGORY + " TEXT NOT NULL, " +
                RssContract.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                RssContract.COLUMN_LINK + " TEXT NOT NULL," +
                RssContract.COLUMN_IMAGE_LINK + " TEXT NOT NULL );";

        sqLiteDatabase.execSQL(SQL_CREATE_RSS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RssContract.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
