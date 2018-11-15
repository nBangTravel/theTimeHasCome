package com.example.nbang.nbangtravel;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "db";

    //Constructor
    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //최초 DB생성시 한 번만 호출
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CheckListContract.ConstantEntry.TABLE_NAME + " (" +
                CheckListContract.ConstantEntry._ID + " INTEGER PRIMARY KEY, " +
                CheckListContract.ConstantEntry.COLUMN_NAME_TITLE + " " + "TEXT" + ")");
        ContentValues cv = new ContentValues();

        cv.put(CheckListContract.ConstantEntry.COLUMN_NAME_TITLE, CheckListContract.first);
        db.insert(CheckListContract.ConstantEntry.TABLE_NAME, CheckListContract.ConstantEntry.COLUMN_NAME_TITLE, cv);

        cv.put(CheckListContract.ConstantEntry.COLUMN_NAME_TITLE, CheckListContract.second);
        db.insert(CheckListContract.ConstantEntry.TABLE_NAME, CheckListContract.ConstantEntry.COLUMN_NAME_TITLE, cv);

    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    //버전 업데이트 되었을 경우 디비 재생성
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CheckListContract.ConstantEntry.TABLE_NAME);
        onCreate(db);
    }
}
