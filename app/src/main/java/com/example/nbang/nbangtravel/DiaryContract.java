package com.example.nbang.nbangtravel;

import android.provider.BaseColumns;

public class DiaryContract {

    private DiaryContract() {}

    /* package scope constants are defined */
    //public static String first = "커피마시기";
    //public static String second = "사진찍기";

    public static String FIRST_DIARY = "2018-05-26";
    public static String SECOND_DIARY = "2018-08-04";

    /* Inner class that defines the table contents */
    public static abstract class ConstantEntry implements BaseColumns {
        public static final String TABLE_NAME = "diary";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_TITLE = "title";
        //사진 BLOB
        public static final String COLUMN_NAME_PICTURE = "picture";
        //내용
        public static final String COLUMN_NAME_CONTENT = "content";
    }
}
