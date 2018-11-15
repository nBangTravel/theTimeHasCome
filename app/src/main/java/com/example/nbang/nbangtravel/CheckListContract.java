package com.example.nbang.nbangtravel;

import android.provider.BaseColumns;
//데이터베이스 : 실제 테이블 저장하는 곳
public final class CheckListContract {
    private CheckListContract() {}

    /* package scope constants are defined */
    public static String first = "커피마시기";
    public static String second = "사진찍기";

    /* Inner class that defines the table contents */
    public static abstract class ConstantEntry implements BaseColumns {
        public static final String TABLE_NAME = "checklist";
        public static final String COLUMN_NAME_TITLE = "title";
    }
}
