package com.ics.journaler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class dbHelper extends SQLiteOpenHelper {

    // jolene - connection to the db and setting up the schema
    // database info - cover of journal book
    private static final String DATABASE_NAME = "Journaler.db";
    private static final int DATABASE_VERSION = 1;

    // table name - your single journal book
    private static final String TABLE_JOURNAL_ENTRIES = "journal_entries";

    // table columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title_of_entry";
    private static final String COLUMN_CONTENT = "journal_entry";
    private static final String COLUMN_CREATED_AT = "created_at";
    private static final String COLUMN_UPDATED_AT = "updated_at";

    // sql command - blueprint for every journal page
    public static final String CREATE_JOURNAL_TABLE =
            "CREATE TABLE " + TABLE_JOURNAL_ENTRIES + " ( " +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT NOT NULL, " +
                    COLUMN_CONTENT + " TEXT NOT NULL, " +
                    COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    COLUMN_UPDATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP " +
                    ")";

    // constructor
    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // onCreate - like setting up your brand new journal book
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_JOURNAL_TABLE);
            Log.d("dbHelper", "Journal table created successfully");
        } catch (Exception e) {
            Log.e("dbHelper", "Error creating journal table: " + e.getMessage());
        }
    }

    // onUpgrade - like reorganizing your journal when you need a new format
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("dbHelper", "Upgrading database from version " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOURNAL_ENTRIES);
        onCreate(db);
    }

    //Ray - Setting up the CRUD
    // Insert-adding a new journal entry
    public boolean insertEntry(String title, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_CONTENT, content);

        long result = db.insert(TABLE_JOURNAL_ENTRIES, null, values);
        return result != -1;
    }

    // Update-editing an old journal page
    public boolean updateEntry(int id, String title, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_CONTENT, content);
        values.put(COLUMN_UPDATED_AT, "datetime('now')");

        int rows = db.update(TABLE_JOURNAL_ENTRIES, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    // Delete
    public boolean deleteEntry(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_JOURNAL_ENTRIES, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    // Read all-Reviewing your whole journal
    public Cursor getAllEntries() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_JOURNAL_ENTRIES, null);
    }

    // Reading a single entry -Re-reading a specific journal page
    public Cursor getEntry(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_JOURNAL_ENTRIES + " WHERE " + COLUMN_ID + "=?",
                new String[]{String.valueOf(id)});
    }
}
