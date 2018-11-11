package com.example.nbang.nbangtravel;

import android.provider.BaseColumns;

public class Sample_CheckList {
    public Sample_CheckList() {}

    /* package scope constants are defined */
    public static String first = "커피마시기";
    public static String second = "사진찍기";

    /* Inner class that defines the table contents */
    public static abstract class ConstantEntry implements BaseColumns {
        public static final String TABLE_NAME = "checklist";
        public static final String COLUMN_NAME_TITLE = "title";
    }
}
