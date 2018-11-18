package com.example.nbang.nbangtravel;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "db";

    //Constructor
    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Resources res = context.getResources();
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
                DiaryContract.ConstantEntry.COLUMN_NAME_DATE + " TEXT," +
                DiaryContract.ConstantEntry.COLUMN_NAME_TITLE + " TEXT," +
                DiaryContract.ConstantEntry.COLUMN_NAME_PICTURE + " BLOB,"+
                DiaryContract.ConstantEntry.COLUMN_NAME_CONTENT + " TEXT" +")");

        ContentValues cv_diary = new ContentValues();
        cv_diary.put(DiaryContract.ConstantEntry.COLUMN_NAME_DATE, DiaryContract.FIRST_DIARY);
        cv_diary.put(DiaryContract.ConstantEntry.COLUMN_NAME_TITLE, "여행 출바알~!");
        cv_diary.put(DiaryContract.ConstantEntry.COLUMN_NAME_PICTURE, R.mipmap.first_diary);
        cv_diary.put(DiaryContract.ConstantEntry.COLUMN_NAME_CONTENT, "오늘은 여행을 출발했다. 빨리 종강했으면 좋겠다");
        db.insert(DiaryContract.ConstantEntry.TABLE_NAME, DiaryContract.ConstantEntry.COLUMN_NAME_DATE, cv_diary);

        cv_diary.put(DiaryContract.ConstantEntry.COLUMN_NAME_DATE, DiaryContract.SECOND_DIARY);
        cv_diary.put(DiaryContract.ConstantEntry.COLUMN_NAME_TITLE, "함부르크 최고<3");
        cv_diary.put(DiaryContract.ConstantEntry.COLUMN_NAME_PICTURE, R.mipmap.second_diary);
        cv_diary.put(DiaryContract.ConstantEntry.COLUMN_NAME_CONTENT, "함부르크 또가고싶다. 함부르크 최고다 종강은 언제할까");
        db.insert(DiaryContract.ConstantEntry.TABLE_NAME, DiaryContract.ConstantEntry.COLUMN_NAME_DATE, cv_diary);

        /*Accounting Book*/
        db.execSQL("CREATE TABLE " + AccountingContract.ConstantEntry.TABLE_NAME + " (" +
                AccountingContract.ConstantEntry._ID + " INTEGER PRIMARY KEY, " +
                AccountingContract.ConstantEntry.COLUMN_NAME_DATE + " TEXT," +
                AccountingContract.ConstantEntry.COLUMN_NAME_TITLE + " TEXT," +
                AccountingContract.ConstantEntry.COLUMN_NAME_PARTICIPATOR + " TEXT," +
                AccountingContract.ConstantEntry.COLUMN_NAME_PRICE + " REAL,"+
                AccountingContract.ConstantEntry.COLUMN_NAME_CURRENCY + " TEXT" +")");

        ContentValues cv_accounting = new ContentValues();
        cv_accounting.put(AccountingContract.ConstantEntry.COLUMN_NAME_DATE, DiaryContract.FIRST_DIARY);
        cv_accounting.put(AccountingContract.ConstantEntry.COLUMN_NAME_TITLE, "빕스먹었다");
        cv_accounting.put(AccountingContract.ConstantEntry.COLUMN_NAME_PARTICIPATOR, "김소영");
        cv_accounting.put(AccountingContract.ConstantEntry.COLUMN_NAME_PRICE, 35400);
        cv_accounting.put(AccountingContract.ConstantEntry.COLUMN_NAME_CURRENCY, "WON");
        db.insert(AccountingContract.ConstantEntry.TABLE_NAME, AccountingContract.ConstantEntry.COLUMN_NAME_DATE, cv_accounting);
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
