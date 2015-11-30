package com.ivanov.denis.rss_reader.DBHelper222;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ivanov.denis.rss_reader.constants.Constants;

public class DBHelper extends SQLiteOpenHelper implements Constants {

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "MyDB", null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table " + TABLE_NAME + " (" +
                Fields.id.name() + " integer primary key autoincrement," +
                Fields.title.name() + " text," +
                Fields.link.name() + " text, " +
                Fields.fulltext.name() + " text, " +
                Fields.favorite.name() + " integer, " +
                Fields.date.name() + " integer, " +
                Fields.readed.name() + " integer " +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (newVersion) {
            case 2:
                if (oldVersion == 1) {
                    db.beginTransaction();
                    try {
                        db.execSQL(" alter table " + TABLE_NAME + " add column " + Fields.favorite.name() + " integer ;");
                        db.execSQL(" alter table " + TABLE_NAME + " add column " + Fields.date.name() + " integer ;");
                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                    }
                }
                break;
            case 3:
                if (oldVersion == 2){
                    db.beginTransaction();
                    try {
                        db.execSQL(" alter table " + TABLE_NAME + " add column " + Fields.readed.name() + " integer ;");
                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                    }
                }
                break;
        }
    }
}