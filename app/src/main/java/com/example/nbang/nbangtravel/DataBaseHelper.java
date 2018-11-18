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

        /*DIARY*/
        db.execSQL("CREATE TABLE " + DiaryContract.ConstantEntry.TABLE_NAME + " (" +
                DiaryContract.ConstantEntry._ID + " INTEGER PRIMARY KEY, " +
                DiaryContract.ConstantEntry.COLUMN_NAME_DATE + " TEXT," +
                DiaryContract.ConstantEntry.COLUMN_NAME_TITLE + " TEXT," +
                DiaryContract.ConstantEntry.COLUMN_NAME_PICTURE + " BLOB,"+
                DiaryContract.ConstantEntry.COLUMN_NAME_CONTENT + " TEXT" +")");

        /*Accounting Book*/
        db.execSQL("CREATE TABLE " + AccountingContract.ConstantEntry.TABLE_NAME + " (" +
                AccountingContract.ConstantEntry._ID + " INTEGER PRIMARY KEY, " +
                AccountingContract.ConstantEntry.COLUMN_NAME_DATE + " TEXT," +
                AccountingContract.ConstantEntry.COLUMN_NAME_TITLE + " TEXT," +
                AccountingContract.ConstantEntry.COLUMN_NAME_PARTICIPATOR + " TEXT," +
                AccountingContract.ConstantEntry.COLUMN_NAME_PRICE + " REAL,"+
                AccountingContract.ConstantEntry.COLUMN_NAME_CURRENCY + " TEXT" +")");
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
