package org.saqib.dev.mnemonic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import org.saqib.dev.mnemonic.DatabaseContract.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Saqib on 6/15/2017.
 */

public class DBOperation {
    private Context context;
    private final String Tag = "DBOperation Class";

    public DBOperation(Context context){
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<String> getList(){
        ArrayList<String> taskList = new ArrayList<String>();
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(this.context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String[] columns = { TaskCategory._ID, TaskCategory.COL_TASKCAT};
            String sortOrder = TaskCategory.COL_TASKCAT + " ASC";
            Cursor c = db.query(TaskCategory.TABLE_NAME, columns,
                    null, null, null, null, sortOrder);
            if(c.getCount()>0) {
                while (c.moveToNext()) {
                    taskList.add(c.getString(1));
                }
                c.close();
                db.close();
                return taskList;
            }
            else{
                c.close();
                db.close();
                return new ArrayList<String>();
            }
        }
        catch (Exception ex){
            Log.d(Tag,ex.getMessage());
            return null;
        }
    }

    public ArrayList<ListItem_Task> getAllTasks(){
        ArrayList<ListItem_Task> taskList = new ArrayList<ListItem_Task>();
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(this.context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String[] columns = { Task._ID, Task.COL_TASKTITLE, Task.COL_TASKDATE, Task.COL_TASKTIME, Task.COL_STATUS, Task.COL_TASKCAT};
            String sortOrder = Task.COL_TASKTITLE + " ASC";
            Cursor c = db.query(Task.TABLE_NAME, columns,
                    null, null, null, null, sortOrder);
            if(c.getCount()>0) {
                while (c.moveToNext()) {
                    ListItem_Task newitem = new ListItem_Task();
                    newitem.setTitle(c.getString(1));
                    newitem.setDate(c.getString(2));
                    newitem.setTime(c.getString(3));
                    newitem.setStatus(c.getString(4));
                    newitem.setCategory(c.getString(5));
                    taskList.add(newitem);
                }
                c.close();
                db.close();
                return taskList;
            }
            else{
                c.close();
                db.close();
                return new ArrayList<ListItem_Task>();
            }
        }
        catch (Exception ex){
            Log.d("asdasda",ex.getMessage());
            return null;
        }
    }

    public ArrayList<ListItem_Task> SearchTasks(String keyword){
        ArrayList<ListItem_Task> taskList = new ArrayList<ListItem_Task>();
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(this.context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String[] columns = { Task._ID, Task.COL_TASKTITLE, Task.COL_TASKDATE, Task.COL_TASKTIME, Task.COL_STATUS, Task.COL_TASKCAT};
            String sortOrder = Task.COL_TASKTITLE + " ASC";

            Cursor c = db.query(true, Task.TABLE_NAME, columns, Task.COL_TASKTITLE + " LIKE ?",
                    new String[] { keyword+"%" }, null, null, null,
                    null);
            if(c.getCount()>0) {
                while (c.moveToNext()) {
                    ListItem_Task newitem = new ListItem_Task();
                    newitem.setTitle(c.getString(1));
                    newitem.setDate(c.getString(2));
                    newitem.setTime(c.getString(3));
                    newitem.setStatus(c.getString(4));
                    newitem.setCategory(c.getString(5));
                    taskList.add(newitem);
                }
                c.close();
                db.close();
                return taskList;
            }
            else{
                c.close();
                db.close();
                return new ArrayList<ListItem_Task>();
            }
        }
        catch (Exception ex){
            Log.d("dberror1",ex.getMessage());
            return null;
        }
    }

    public boolean insertTask_Category(String cat){
        ContentValues val = new ContentValues();
        val.put(TaskCategory.COL_TASKCAT,cat);
        try{
            DatabaseHelper dbhelper = new DatabaseHelper(this.context);
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            if(db.insert(TaskCategory.TABLE_NAME,null,val)>-1){
                db.close();
                return true;
            }
            else{
                db.close();
                return false;
            }
        }
        catch (Exception ex){
            Log.d(Tag,ex.getMessage());
            return false;
        }
    }

    public boolean deleteTaskCategory(String name){
        try {
            DatabaseHelper dbhelper = new DatabaseHelper(this.context);
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            String whereClause = TaskCategory.COL_TASKCAT + "=?";
            String[] whereArgs = new String[]{name};
            long res = db.delete(TaskCategory.TABLE_NAME, whereClause, whereArgs);

            String whereClause1 = Task.COL_TASKCAT + "=?";
            String[] whereArgs1 = new String[]{name};
            long res1 = db.delete(Task.TABLE_NAME, whereClause, whereArgs);
            db.close();

            if(res > -1 && res1 >-1){
                return true;
            }
            else {
                return false;
            }
        }
        catch (Exception ex){
            Log.d(Tag,ex.getMessage());
            return false;
        }
    }

    public boolean UpdateTaskCategory(String newName, String oldName){
        try {
            DatabaseHelper dbhelper = new DatabaseHelper(this.context);
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(TaskCategory.COL_TASKCAT,newName);

            String whereClause = TaskCategory.COL_TASKCAT + "=?";
            String[] whereArgs = new String[]{oldName};
            db.update(TaskCategory.TABLE_NAME,cv,whereClause,whereArgs);

            String whereClause1 = Task.COL_TASKCAT + "=?";
            String[] whereArgs1 = new String[]{oldName};
            db.update(Task.TABLE_NAME,cv,whereClause1,whereArgs1);
            db.close();
            return true;
        }
        catch (Exception ex){
            Log.d(Tag,ex.getMessage());
            return false;
        }
    }
    public boolean CompleteTask(String title, String category){
        try {
            DatabaseHelper dbhelper = new DatabaseHelper(this.context);
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(Task.COL_STATUS,1);

            String whereClause1 = Task.COL_TASKTITLE + " =? AND "+Task.COL_TASKCAT + " =? ";
            String[] whereArgs1 = new String[]{title,category};
            db.update(Task.TABLE_NAME,cv,whereClause1,whereArgs1);
            db.close();
            return true;
        }
        catch (Exception ex){
            Log.d(Tag,ex.getMessage());
            return false;
        }
    }
    public boolean Negate_CompleteTask(String title, String category){
        try {
            DatabaseHelper dbhelper = new DatabaseHelper(this.context);
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(Task.COL_STATUS,0);

            String whereClause1 = Task.COL_TASKTITLE + " =? AND "+Task.COL_TASKCAT + " =? ";
            String[] whereArgs1 = new String[]{title,category};
            db.update(Task.TABLE_NAME,cv,whereClause1,whereArgs1);
            db.close();
            return true;
        }
        catch (Exception ex){
            Log.d(Tag,ex.getMessage());
            return false;
        }
    }

    public void date(){
        Date myDate = new Date();
        long timeMilliseconds = myDate.getTime();
        //add 1 hour
        timeMilliseconds = timeMilliseconds + 3600 * 1000; //3600 seconds * 1000 milliseconds
        //To convert back to Date
        Date myDateNew = new Date(timeMilliseconds);
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public boolean insertTask(String cat, String title, String date, String time){
        ContentValues val = new ContentValues();
        val.put(Task.COL_TASKCAT,cat);
        val.put(Task.COL_STATUS,0);
        val.put(Task.COL_TASKDATE,date);
        val.put(Task.COL_TASKTIME,time);
        val.put(Task.COL_TASKTITLE,title);

        try{
            DatabaseHelper dbhelper = new DatabaseHelper(this.context);
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            if(db.insert(Task.TABLE_NAME,null,val)>-1){
                db.close();
                return true;
            }
            else{
                db.close();
                return false;
            }
        }
        catch (Exception ex){
            Log.d("asdasd",ex.getMessage());
            return false;
        }
    }

    public boolean UpdateTask(String oldcat, String oldTitle, String oldDate, String oldTime, String oldStatus,
                              String newcat, String newTitle, String newDate, String newTime, String newStatus){

        ContentValues val = new ContentValues();
        val.put(Task.COL_TASKCAT,newcat);
        val.put(Task.COL_STATUS,newStatus);
        val.put(Task.COL_TASKDATE,newDate);
        val.put(Task.COL_TASKTIME,newTime);
        val.put(Task.COL_TASKTITLE,newTitle);
        try{
            DatabaseHelper dbhelper = new DatabaseHelper(this.context);
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            String whereClause1 = Task.COL_TASKTITLE + " =? AND "+Task.COL_TASKCAT + " =? AND "+Task.COL_TASKDATE + " =? AND "+
                    Task.COL_TASKTIME + " =? AND "+Task.COL_STATUS + " =? ";
            String[] whereArgs1 = new String[]{oldTitle,oldcat,oldDate,oldTime,oldStatus};
            db.update(Task.TABLE_NAME,val,whereClause1,whereArgs1);
            return true;
        }
        catch (Exception ex){
            Log.d("asdasd",ex.getMessage());
            return false;
        }
    }

}
