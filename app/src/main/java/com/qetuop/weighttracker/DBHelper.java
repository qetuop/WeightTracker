package com.qetuop.weighttracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brian on 6/17/17.
 */

public class DBHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "weightTracker.db";

    public static final String WEIGHT_TABLE_NAME = "weight_table";

    public static final String WEIGHT_COLUMN_ID = "id";
    public static final String WEIGHT_COLUMN_DATE = "date";
    public static final String WEIGHT_COLUMN_WEIGHT = "weight";
    public static final String WEIGHT_COLUMN_COMMENT = "comment";

    private static final String CREATE_TABLE = "CREATE TABLE "
            + WEIGHT_TABLE_NAME     + "("
            + WEIGHT_COLUMN_ID      + " integer primary key autoincrement, "
            + WEIGHT_COLUMN_DATE    + " integer not null, "
            + WEIGHT_COLUMN_WEIGHT  + " real not null, "
            + WEIGHT_COLUMN_COMMENT + " text"
            + ")";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + WEIGHT_TABLE_NAME;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    // Adding new Entry
    public void addEntry(Entry entry) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WEIGHT_COLUMN_DATE, entry.getDate());
        values.put(WEIGHT_COLUMN_WEIGHT, entry.getWeight());
        values.put(WEIGHT_COLUMN_COMMENT, entry.getComment());

        // Inserting Row
        db.insert(WEIGHT_TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Getting single Entry
    public Entry getEntry(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(WEIGHT_TABLE_NAME, new String[] {
                        WEIGHT_COLUMN_ID,
                        WEIGHT_COLUMN_DATE,
                        WEIGHT_COLUMN_WEIGHT,
                        WEIGHT_COLUMN_COMMENT },
                        WEIGHT_COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Entry entry = new Entry(Integer.parseInt(cursor.getString(0)),
                                Long.parseLong(cursor.getString(1)),
                                Float.parseFloat(cursor.getString(2)),
                                cursor.getString(3));
        // return entry
        return entry;
    }

    // Getting All Entries
    public List<Entry> getAllEntries() {
        List<Entry> entryList = new ArrayList<Entry>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + WEIGHT_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Entry entry = new Entry();
                entry.setId(Integer.parseInt(cursor.getString(0)));
                entry.setDate(Long.parseLong(cursor.getString(1)));
                entry.setWeight(Float.parseFloat(cursor.getString(2)));
                entry.setComment(cursor.getString(3));

                // Adding contact to list
                entryList.add(entry);
            } while (cursor.moveToNext());
        }

        // return contact list
        return entryList;
    }

    // Getting Entry Count
    public int getEntryCount() {
        String countQuery = "SELECT  * FROM " + WEIGHT_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    // Updating single Entry
    public int updateEntry(Entry entry) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WEIGHT_COLUMN_DATE, entry.getDate());
        values.put(WEIGHT_COLUMN_WEIGHT, entry.getWeight());
        values.put(WEIGHT_COLUMN_COMMENT, entry.getComment());

        // updating row
        return db.update(WEIGHT_TABLE_NAME, values, WEIGHT_COLUMN_ID + " = ?",
                new String[] { String.valueOf(entry.getId()) });
    }

    // Deleting single Entry
    public void deleteEntry(Entry entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(WEIGHT_TABLE_NAME, WEIGHT_COLUMN_ID + " = ?",
                new String[] { String.valueOf(entry.getId()) });
        db.close();
    }

    public void deleteAllEntries() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
        db.close();
    }
    
}
