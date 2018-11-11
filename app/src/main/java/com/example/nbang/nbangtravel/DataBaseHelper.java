package com.example.nbang.nbangtravel;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "db";

    public static final String TEXT_TYPE = "TEXT";
    public static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Sample_CheckList.ConstantEntry.TABLE_NAME + " (" +
                    Sample_CheckList.ConstantEntry._ID + " INTEGER PRIMARY KEY, " +
                    Sample_CheckList.ConstantEntry.COLUMN_NAME_TITLE + " " + TEXT_TYPE + ")";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Sample_CheckList.ConstantEntry.TABLE_NAME;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //this db is only a cache for online data, so its upgrade policy is
        //to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

        //initialize the table with five rows
        // Create a new map of values, where column names are the keys
        ContentValues cv = new ContentValues();

        cv.put(Sample_CheckList.ConstantEntry.COLUMN_NAME_TITLE, Sample_CheckList.first);
        db.insert(Sample_CheckList.ConstantEntry.TABLE_NAME, Sample_CheckList.ConstantEntry.COLUMN_NAME_TITLE, cv);

        cv.put(Sample_CheckList.ConstantEntry.COLUMN_NAME_TITLE, Sample_CheckList.second);
        db.insert(Sample_CheckList.ConstantEntry.TABLE_NAME, Sample_CheckList.ConstantEntry.COLUMN_NAME_TITLE, cv);

    }

}
