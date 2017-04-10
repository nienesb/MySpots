package com.teamdating.myspots.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by j.boeser on 16-3-2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "spots.db";
    private static final int VERSION = 1;
    private static String _id;
    private static String TITLE;
    private static String CITY;
    private static double LATITUDE;
    private static double LONGITUDE;
    public static final String[] ALL_COLUMNS = new String[]{"_id", "name", "city", "latitude", "longitude"};

   // public static final String[] ALL_COLUMNS = new String[]{_id, TITLE, CITY, String.valueOf(LATITUDE), String.valueOf(LONGITUDE)};


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + SpotsDBSchema.SpotsTable.NAME + "( " +
                SpotsDBSchema.SpotsTable.Colums._id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SpotsDBSchema.SpotsTable.Colums.TITLE + ", " +
                SpotsDBSchema.SpotsTable.Colums.CITY + ", " +
                SpotsDBSchema.SpotsTable.Colums.LATITUDE + " REAL," +
                SpotsDBSchema.SpotsTable.Colums.LONGITUDE + " REAL" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST" + SpotsDBSchema.SpotsTable.NAME);
        onCreate(db);
    }
}
