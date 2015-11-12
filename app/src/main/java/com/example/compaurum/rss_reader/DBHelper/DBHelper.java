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
        super(context, "MyDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table "+TABLE_NAME+" (" +
                Fields.id.name() + " integer primary key autoincrement," +
                Fields.title.name() + " text," +
                Fields.link.name() + " text, " +
                Fields.fulltext.name() + " text " +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}