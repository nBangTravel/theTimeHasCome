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

    //최초 DB생성 시 한 번만 호출
    @Override
    public void onCreate(SQLiteDatabase db) {

        /*CHECKLIST*/
        db.execSQL("CREATE TABLE " + CheckListContract.ConstantEntry.TABLE_NAME + " (" +
                CheckListContract.ConstantEntry._ID + " INTEGER PRIMARY KEY, " +
                CheckListContract.ConstantEntry.COLUMN_NAME_TITLE + " " + "TEXT" + ")");
        ContentValues cv_check = new ContentValues();

        cv_check.put(CheckListContract.ConstantEntry.COLUMN_NAME_TITLE, CheckListContract.first);
        db.insert(CheckListContract.ConstantEntry.TABLE_NAME, CheckListContract.ConstantEntry.COLUMN_NAME_TITLE, cv_check);

        cv_check.put(CheckListContract.ConstantEntry.COLUMN_NAME_TITLE, CheckListContract.second);
        db.insert(CheckListContract.ConstantEntry.TABLE_NAME, CheckListContract.ConstantEntry.COLUMN_NAME_TITLE, cv_check);

        /*DIARY*/
        db.execSQL("CREATE TABLE " + DiaryContract.ConstantEntry.TABLE_NAME + " (" +
                DiaryContract.ConstantEntry._ID + " INTEGER PRIMARY KEY, " +
                DiaryContract.ConstantEntry.COLUMN_NAME_DATE + " " + "DATE, " +
                DiaryContract.ConstantEntry.COLUMN_NAME_TITLE + " " + "TEXT" + ")");
        ContentValues cv_diary = new ContentValues();

        cv_diary.put(DiaryContract.ConstantEntry.COLUMN_NAME_DATE, DiaryContract.FIRST_DIARY);
        cv_diary.put(DiaryContract.ConstantEntry.COLUMN_NAME_TITLE, "여행 출바알~!");
        db.insert(DiaryContract.ConstantEntry.TABLE_NAME, DiaryContract.ConstantEntry.COLUMN_NAME_DATE, cv_diary);

        cv_diary.put(DiaryContract.ConstantEntry.COLUMN_NAME_DATE, DiaryContract.SECOND_DIARY);
        cv_diary.put(DiaryContract.ConstantEntry.COLUMN_NAME_TITLE, "함부르크 최고<3");
        db.insert(DiaryContract.ConstantEntry.TABLE_NAME, DiaryContract.ConstantEntry.COLUMN_NAME_DATE, cv_diary);
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
