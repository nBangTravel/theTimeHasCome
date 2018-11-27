package soy.dow.nbang.nbangtravel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static String now_travel = null;
    public static final String DATABASE_NAME = "db";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*CHECKLIST*/
        db.execSQL("CREATE TABLE " + CheckListContract.ConstantEntry.TABLE_NAME + " (" +
                CheckListContract.ConstantEntry._ID + " INTEGER PRIMARY KEY, " +
                CheckListContract.ConstantEntry.COLUMN_NAME_TITLE + " " + "TEXT," +
                CheckListContract.ConstantEntry.COLUMN_NAME_TRAVEL + " TEXT" + ")");

        /*DIARY*/
        db.execSQL("CREATE TABLE " + DiaryContract.ConstantEntry.TABLE_NAME + " (" +
                DiaryContract.ConstantEntry._ID + " INTEGER PRIMARY KEY, " +
                DiaryContract.ConstantEntry.COLUMN_NAME_DATE + " TEXT," +
                DiaryContract.ConstantEntry.COLUMN_NAME_TITLE + " TEXT," +
                DiaryContract.ConstantEntry.COLUMN_NAME_PICTURE + " BLOB,"+
                DiaryContract.ConstantEntry.COLUMN_NAME_CONTENT + " TEXT," +
                DiaryContract.ConstantEntry.COLUMN_NAME_TRAVEL + " TEXT" +")");

        /*Accounting Book*/
        db.execSQL("CREATE TABLE " + AccountingContract.ConstantEntry.TABLE_NAME + " (" +
                AccountingContract.ConstantEntry._ID + " INTEGER PRIMARY KEY, " +
                AccountingContract.ConstantEntry.COLUMN_NAME_DATE + " TEXT," +
                AccountingContract.ConstantEntry.COLUMN_NAME_TITLE + " TEXT," +
                AccountingContract.ConstantEntry.COLUMN_NAME_PARTICIPATOR + " TEXT," +
                AccountingContract.ConstantEntry.COLUMN_NAME_PRICE + " REAL,"+
                AccountingContract.ConstantEntry.COLUMN_NAME_CURRENCY + " TEXT,"+
                AccountingContract.ConstantEntry.COLUMN_NAME_TRAVEL + " TEXT"+")");

        /*TRAVELS*/
        db.execSQL("CREATE TABLE " + HomeContract.ConstantEntry.TABLE_NAME + " (" +
                HomeContract.ConstantEntry._ID + " INTEGER PRIMARY KEY, " +
                HomeContract.ConstantEntry.COLUMN_NAME_TRAVEL + " TEXT, " +
                HomeContract.ConstantEntry.COLUMN_NAME_MEMBERS + " TEXT" + ")");
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CheckListContract.ConstantEntry.TABLE_NAME);
        onCreate(db);
    }

}
