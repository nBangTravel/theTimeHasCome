package soy.dow.nbang.nbangtravel;

import android.provider.BaseColumns;

public final class CheckListContract {
    private CheckListContract() {}

    public static abstract class ConstantEntry implements BaseColumns {
        public static final String TABLE_NAME = "checklist";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_TRAVEL = "travelname";
    }
}
