package com.example.compaurum.rss_reader.DBHelper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.compaurum.rss_reader.Interfaces.Constants;
import com.example.compaurum.rss_reader.parser.Item;
import com.example.compaurum.rss_reader.parser.Items;

public class MyDBTools implements Constants {
    private SQLiteDatabase db;

    public MyDBTools(DBHelper db) {
        this.db = db.getWritableDatabase();
    }

    public void insert(Items items){
        for (Item item : items){
            insert(item);
        }
    }

    private boolean isExist(Item item){
        String[] columns = new String[] {Fields.date.name()};
        String selection = Fields.date.name() + " = ? and " + Fields.title.name() + " = ? ";
        String[] selection_args = new String[]{
                String.valueOf(item.getMpubDate().getTime()),
                item.getTitle()
        };
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selection_args, null, null, null);
        return (cursor.getCount() > 0);
    }

    public void delete(Item item){
        String whereClause = Fields.date.name() + " = ? and " + Fields.title.name() + " = ? ";
        String[] whereArgs = new String[]{
                String.valueOf(item.getMpubDate().getTime()),
                item.getTitle()
        };
        db.delete(TABLE_NAME, whereClause, whereArgs);
    }

    public void update(Item item){
        String whereClause = Fields.date.name() + " = ? and " + Fields.title.name() + " = ? ";
        String[] whereArgs = new String[]{
                String.valueOf(item.getMpubDate().getTime()),
                item.getTitle()
        };
        ContentValues contentValues = new ContentValues();
        contentValues.put(Fields.favorite.name(), item.isFavorite());

        db.update(TABLE_NAME, contentValues, whereClause, whereArgs);
    }

    public void insert(Item item){
        if (isExist(item)){
            Log.d("DATABASE", "Already exist");
            return;
        }
        Log.d("DATABASE", "Inserted");
        ContentValues contentValues = new ContentValues();
        contentValues.put(Fields.title.name(), item.getTitle());
        contentValues.put(Fields.link.name(), item.getLink());
        contentValues.put(Fields.fulltext.name(), item.getFullText());
        contentValues.put(Fields.favorite.name(), item.isFavorite());
        contentValues.put(Fields.date.name(), item.getMpubDate().getTime());
        db.insertOrThrow(TABLE_NAME, null, contentValues);
    }

    public void deleteAll(){
        db.delete(TABLE_NAME, null, null);
    }

    public Items selectAll(){
        Items items = null;
        String orderBy = Fields.date.name() + " desc ";
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, orderBy);
        try {
            if (cursor.moveToFirst()) {
                items = new Items();
                do {
                    Item item = new Item();
                    item.setTitle(cursor.getString(Fields.title.ordinal()));
                    item.setLink(cursor.getString(Fields.link.ordinal()));
                    item.setFullText(cursor.getString(Fields.fulltext.ordinal()));
                    item.setFavorite(cursor.getInt(Fields.favorite.ordinal()) == 1 ? true : false);
                    item.setMpubDate(cursor.getLong(Fields.date.ordinal()));
                    items.add(item);
                } while (cursor.moveToNext());
            } else
                Log.d(LOG_TAG, "0 rows");
        } finally {
            cursor.close();
        }
        return items;
    }
}
