package com.droidamsel.doApp.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.droidamsel.doApp.Model.doModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "doDatabase";
    private static final String TABLE_NAME = "doTable";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TASK = "task";
    private static final String COLUMN_STATUS = "status";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TASK + " TEXT, "
                + COLUMN_STATUS + " INTEGER)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public void insertTask(doModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK, model.getTask());
        values.put(COLUMN_STATUS, 0);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void updateTask(int id, String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TASK, task);
        db.update(TABLE_NAME, cv, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void updateStatus(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_STATUS, status);
        db.update(TABLE_NAME, cv, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<doModel> getAllTask() {
        List<doModel> taskList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int columnIndexId = cursor.getColumnIndex(COLUMN_ID);
        int columnIndexTask = cursor.getColumnIndex(COLUMN_TASK);
        int columnIndexStatus = cursor.getColumnIndex(COLUMN_STATUS);

        while (cursor.moveToNext()) {
            doModel task = new doModel();
            task.setId(cursor.getInt(columnIndexId));
            task.setTask(cursor.getString(columnIndexTask));
            task.setStatus(cursor.getInt(columnIndexStatus));
            taskList.add(task);
        }
        cursor.close();

        db.close();
        return taskList;
    }


}
