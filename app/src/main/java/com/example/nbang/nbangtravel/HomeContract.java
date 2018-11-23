package com.example.nbang.nbangtravel;

import android.provider.BaseColumns;

public class HomeContract {

    private HomeContract() {}

    public static abstract class ConstantEntry implements BaseColumns {
        public static final String TABLE_NAME = "travel";
        public static final String COLUMN_NAME_TRAVEL = "travel";
        public static final String COLUMN_NAME_MEMBERS = "members";
    }
}
