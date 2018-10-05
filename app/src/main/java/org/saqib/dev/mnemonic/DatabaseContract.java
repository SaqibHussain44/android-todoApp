package org.saqib.dev.mnemonic;

import android.provider.BaseColumns;

/**
 * Created by Saqib on 5/8/2017.
 */

public class DatabaseContract {
    public DatabaseContract(){}

    public static abstract class TaskCategory implements BaseColumns{
        public static final String TABLE_NAME = "taskcat";
        public static final String COL_TASKCAT = "catname";
    }
    public static abstract class Task implements BaseColumns{
        public static final String TABLE_NAME = "task";
        public static final String COL_TASKTITLE = "todo";
        public static final String COL_TASKDATE = "date";
        public static final String COL_TASKTIME = "time";
        public static final String COL_STATUS = "status";
        public static final String COL_TASKCAT = "catname";
    }
}
