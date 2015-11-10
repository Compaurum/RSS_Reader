package com.example.compaurum.rss_reader.DBHelper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.compaurum.rss_reader.Interfaces.Constants;
import com.example.compaurum.rss_reader.parser.Item;
import com.example.compaurum.rss_reader.parser.Items;

import java.util.ArrayList;

/**
 * Created by compaurum on 09.11.2015.
 */
public class MyDBTools implements Constants {
    SQLiteDatabase db;

    public MyDBTools(DBHelper db) {
        this.db = db.getWritableDatabase();
    }

    public void insert(Items items){
        for (Item item : items){
            insert(item);
        }
    }
    public void insert(Item item){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Fields.title.name(), item.getTitle());
        contentValues.put(Fields.link.name(), item.getLink());
        contentValues.put(Fields.fulltext.name(), item.getFullText());
        db.insert(TABLE_NAME, null, contentValues);
    }
    public void deleteAll(){
        db.delete(TABLE_NAME, null, null);
    }

    public Items selectAll(){
        Items items = null;
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            items = new Items();
            do {
                Item item = new Item();
                item.setTitle(cursor.getString(Fields.title.ordinal()));
                item.setLink(cursor.getString(Fields.link.ordinal()));
                item.setFullText(cursor.getString(Fields.fulltext.ordinal()));
                items.add(item);
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d(LOG_TAG,
                        Fields.id.name() + " = " + cursor.getInt(Fields.id.ordinal()) + ", " +
                        Fields.title.name() + " = " + cursor.getString(Fields.title.ordinal()) + ", " +
                        Fields.link.name() + " = " + cursor.getString(Fields.link.ordinal()) + ", " +
                        Fields.fulltext.name() + " = " + cursor.getString(Fields.fulltext.ordinal()));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (cursor.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");
        cursor.close();
        return items;
    }
}
