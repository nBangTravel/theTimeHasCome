package com.example.nbang.nbangtravel;

import android.provider.BaseColumns;

public class DiaryContract {

    private DiaryContract() {}

    public static abstract class ConstantEntry implements BaseColumns {
        public static final String TABLE_NAME = "diary";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_PICTURE = "picture";
        public static final String COLUMN_NAME_CONTENT = "content";
    }



}
