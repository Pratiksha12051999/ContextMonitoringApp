package com.example.contextmonitoringapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "HealthDataDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "health_data";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_UPLOAD_TIME = "upload_time";
    private static final String COLUMN_SYMPTOM_1 = "symptom_1";
    private static final String COLUMN_SYMPTOM_2 = "symptom_2";
    private static final String COLUMN_SYMPTOM_3 = "symptom_3";
    private static final String COLUMN_SYMPTOM_4 = "symptom_4";
    private static final String COLUMN_SYMPTOM_5 = "symptom_5";
    private static final String COLUMN_SYMPTOM_6 = "symptom_6";
    private static final String COLUMN_SYMPTOM_7 = "symptom_7";
    private static final String COLUMN_SYMPTOM_8 = "symptom_8";
    private static final String COLUMN_SYMPTOM_9 = "symptom_9";
    private static final String COLUMN_SYMPTOM_10 = "symptom_10";
    private static final String COLUMN_HEART_RATE = "heart_rate";
    private static final String COLUMN_RESPIRATORY_RATE = "respiratory_rate";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the table if it does not exist
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_UPLOAD_TIME + " TEXT, "
                + COLUMN_SYMPTOM_1 + " INTEGER, "
                + COLUMN_SYMPTOM_2 + " INTEGER, "
                + COLUMN_SYMPTOM_3 + " INTEGER, "
                + COLUMN_SYMPTOM_4 + " INTEGER, "
                + COLUMN_SYMPTOM_5 + " INTEGER, "
                + COLUMN_SYMPTOM_6 + " INTEGER, "
                + COLUMN_SYMPTOM_7 + " INTEGER, "
                + COLUMN_SYMPTOM_8 + " INTEGER, "
                + COLUMN_SYMPTOM_9 + " INTEGER, "
                + COLUMN_SYMPTOM_10 + " INTEGER, "
                + COLUMN_HEART_RATE + " INTEGER, "
                + COLUMN_RESPIRATORY_RATE + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insertHealthData(String uploadTime, int[] symptoms, int heartRate, int respiratoryRate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_UPLOAD_TIME, uploadTime);

        // Inserting each symptom into its respective column
        values.put(COLUMN_SYMPTOM_1, symptoms[0]);
        values.put(COLUMN_SYMPTOM_2, symptoms[1]);
        values.put(COLUMN_SYMPTOM_3, symptoms[2]);
        values.put(COLUMN_SYMPTOM_4, symptoms[3]);
        values.put(COLUMN_SYMPTOM_5, symptoms[4]);
        values.put(COLUMN_SYMPTOM_6, symptoms[5]);
        values.put(COLUMN_SYMPTOM_7, symptoms[6]);
        values.put(COLUMN_SYMPTOM_8, symptoms[7]);
        values.put(COLUMN_SYMPTOM_9, symptoms[8]);
        values.put(COLUMN_SYMPTOM_10, symptoms[9]);

        values.put(COLUMN_HEART_RATE, heartRate);
        values.put(COLUMN_RESPIRATORY_RATE, respiratoryRate);

        db.insert(TABLE_NAME, null, values);

        db.close();
    }
}
