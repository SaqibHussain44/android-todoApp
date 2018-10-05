package org.saqib.dev.mnemonic;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.saqib.dev.mnemonic.DatabaseContract.*;


/**
 * Created by Saqib on 5/8/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TodoDB.db";

    private static final String CREATE_TBL_CAT  = "CREATE TABLE "
            + TaskCategory.TABLE_NAME + " ("
            + TaskCategory._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TaskCategory.COL_TASKCAT + " TEXT NOT NULL)";

    private static final String CREATE_TBL_TASK  = "CREATE TABLE "
            + Task.TABLE_NAME + " ("
            + Task._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Task.COL_TASKTITLE + " TEXT NOT NULL, "
            + Task.COL_TASKDATE + " TEXT, "
            + Task.COL_TASKTIME + " TEXT, "
            + Task.COL_STATUS + " INTEGER, "
            + Task.COL_TASKCAT + " TEXT NOT NULL)";

    private static final String ROW1 = "INSERT INTO " + TaskCategory.TABLE_NAME + " ("
            + TaskCategory.COL_TASKCAT + ") Values ( 'All Lists' )";
    private static final String ROW2 = "INSERT INTO " + TaskCategory.TABLE_NAME + " ("
            + TaskCategory.COL_TASKCAT + ") Values ( 'Default' )";
    private static final String ROW3 = "INSERT INTO " + TaskCategory.TABLE_NAME + " ("
            + TaskCategory.COL_TASKCAT + ") Values ( 'Personal' )";
    private static final String ROW4 = "INSERT INTO " + TaskCategory.TABLE_NAME + " ("
            + TaskCategory.COL_TASKCAT + ") Values ( 'Home' )";
    private static final String ROW5 = "INSERT INTO " + TaskCategory.TABLE_NAME + " ("
            + TaskCategory.COL_TASKCAT + ") Values ( 'Work' )";
    private static final String ROW6 = "INSERT INTO " + TaskCategory.TABLE_NAME + " ("
            + TaskCategory.COL_TASKCAT + ") Values ( 'Shopping' )";

    public DatabaseHelper(Context context){

        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TBL_CAT);
        sqLiteDatabase.execSQL(CREATE_TBL_TASK);
        sqLiteDatabase.execSQL(ROW1);
        sqLiteDatabase.execSQL(ROW2);
        sqLiteDatabase.execSQL(ROW3);
        sqLiteDatabase.execSQL(ROW4);
        sqLiteDatabase.execSQL(ROW5);
        sqLiteDatabase.execSQL(ROW6);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //db.execSQL("DROP TABLE IF EXISTS " + YOUR_TABLE);
    }
}
