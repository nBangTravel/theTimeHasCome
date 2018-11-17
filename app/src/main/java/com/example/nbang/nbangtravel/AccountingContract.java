package com.example.nbang.nbangtravel;

import android.provider.BaseColumns;

public class AccountingContract {

    private AccountingContract() {}
    /* Inner class that defines the table contents */
    public static abstract class ConstantEntry implements BaseColumns {
        public static final String TABLE_NAME = "accounting";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_PARTICIPATOR = "participator";
        public static final String COLUMN_NAME_PRICE = "price";
        public static final String COLUMN_NAME_CURRENCY = "currency";
    }
}
