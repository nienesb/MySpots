package com.teamdating.myspots;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static com.teamdating.myspots.SpotsDBSchema.SpotsTable.Colums.LONGITUDE;

/**
 * Created by j.boeser on 16-3-2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "spots.db";
    private static final int VERSION = 1;
    private static String ID;
    private static String TITLE;
    private static String CITY;
    private static double LATITUDE;
    private static double LONGITUDE;
    public static final String[] ALL_COLUMNS = new String[]{ID, TITLE, CITY, String.valueOf(LATITUDE), String.valueOf(LONGITUDE)};


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + SpotsDBSchema.SpotsTable.NAME + "( " +
                SpotsDBSchema.SpotsTable.Colums._id + " integer primary key autoincrement, " +
                SpotsDBSchema.SpotsTable.Colums.TITLE + ", " +
                SpotsDBSchema.SpotsTable.Colums.CITY + ", " +
                SpotsDBSchema.SpotsTable.Colums.LATITUDE + ", " +
                SpotsDBSchema.SpotsTable.Colums.LONGITUDE + " )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST" + SpotsDBSchema.SpotsTable.NAME);
        onCreate(db);
    }
}
