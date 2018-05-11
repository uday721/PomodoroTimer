package com.example.shanker.pomodorotimer;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "history.db";
    public static final String TABLE_HISTORY = "history";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ACTIVITY_NAME = "activityname";
   // public static final String COLUMN_TOTAL_TIME = "totaltime";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE" + TABLE_HISTORY + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT " +
                COLUMN_ACTIVITY_NAME + " TEXT " +
                ");";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(sqLiteDatabase);
    }

    public void addActivity(HistoryActivity history){
        ContentValues values = new ContentValues();
        values.put(COLUMN_ACTIVITY_NAME, history.get_activityName());
        //values.put(COLUMN_TOTAL_TIME, history.get_timeElapsed());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.insert(TABLE_HISTORY, null, values);
        sqLiteDatabase.close();
    }

    //print the database as string
    public String dataBaseToString(){
        String dbString = "";
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_HISTORY + "WHERE 1";
        Cursor c = sqLiteDatabase.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("activityname"))!=null){
               dbString += c.getString(c.getColumnIndex("activityname"));
               dbString += "\n";
            }
        }
        sqLiteDatabase.close();
        return dbString;

    }
}
