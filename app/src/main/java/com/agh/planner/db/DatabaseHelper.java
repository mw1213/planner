package com.agh.planner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Plan.db";
    public static final String TABLE_NAME = "plan_table";
    public static final String ID_COLUMN = "id";
    public static final String DESCRIPTION_COLUMN = "description";
    public static final String DATE_COLUMN = "date";
    public static final String TEMPERATURE_COLUMN = "temperature";
    public static final String WEATHER_COLUMN = "weather_type";    // sunny, cloudy, rainy etc..


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_NAME + " (" +
                ID_COLUMN+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                DESCRIPTION_COLUMN + " TEXT," +
                DATE_COLUMN + " TEXT," +
                TEMPERATURE_COLUMN + " REAL," +
                WEATHER_COLUMN + " TEXT"
                 + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean addPlan(String description, String date, Float temp, String weatherType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DESCRIPTION_COLUMN, description);
        contentValues.put(DATE_COLUMN, date);
        contentValues.put(TEMPERATURE_COLUMN, temp);
        contentValues.put(WEATHER_COLUMN, weatherType);
        return db.insert(TABLE_NAME, null, contentValues) != -1;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return res;
    }

    public int deleteRow(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, ID_COLUMN + "= ?", new String[]{String.valueOf(id)});
    }
}
