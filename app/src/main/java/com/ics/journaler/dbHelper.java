package com.ics.journaler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class dbHelper extends SQLiteOpenHelper {
    //database info- cover of journal book
    private static final String DATABASE_NAME="Journaler.db";
    private static final int DATABASE_VERSION = 1;

    //table name - your single journal book
    private static final String TABLE_JOURNAL_ENTRIES = "journal_entries";

    //table columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title_of_entry";
    private static final String COLUMN_CONTENT = "journal_entry";
    private static final String COLUMN_CREATED_AT = "created_at";
    private static final String COLUMN_UPDATED_AT = "updated_at";

    //sql command - blue print for every journal page
    public static final String CREATE_JOURNAL_TABLE =
            "CREATE TABLE " + TABLE_JOURNAL_ENTRIES +
                    " ( " +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT NOT NULL, " +
                    COLUMN_CONTENT + " TEXT NOT NULL, " +
                    COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP, "+
                    COLUMN_UPDATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP "+
                    " ) ";

    //constructor
    public dbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //oncreate - like setting up your brand new journal book
    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL(CREATE_JOURNAL_TABLE);
            Log.d("DatabaseHelper", "Journal table created successfully");
        }catch (Exception e){
            Log.e("DatabaseHelper", "Error creating journal table: " + e.getMessage());
        }
    }

    // onUpgrade - Like reorganizing your journal when you need a new format
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("DatabaseHelper", "Upgrading database from version " + oldVersion + " to " + newVersion);

        // Drop older table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOURNAL_ENTRIES);

        // Create new table
        onCreate(db);
    }

}
