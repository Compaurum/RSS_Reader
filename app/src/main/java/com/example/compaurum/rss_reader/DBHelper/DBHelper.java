package com.example.compaurum.rss_reader.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.compaurum.rss_reader.Interfaces.Constants;

/**
 * Created by compaurum on 09.11.2015.
 */
public class DBHelper extends SQLiteOpenHelper implements Constants {

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "MyDB", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table "+TABLE_NAME+" (" +
                Fields.id.name() + " integer primary key autoincrement," +
                Fields.title.name() + " text," +
                Fields.link.name() + " text, " +
                Fields.fulltext.name() + " text, " +
                Fields.favorite.name() + " integer, " +
                Fields.date.name() + " integer " +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2){
            db.beginTransaction();
            try {
                db.execSQL(" alter table " + TABLE_NAME + " add column " + Fields.favorite.name() + " integer ;");
                db.execSQL(" alter table " + TABLE_NAME + " add column " + Fields.date.name() + " integer ;");
                db.setTransactionSuccessful();
            }finally {
                db.endTransaction();
            }
        }
    }
}