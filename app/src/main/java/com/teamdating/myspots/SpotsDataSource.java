package com.teamdating.myspots;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.camera2.params.StreamConfigurationMap;

/**
 * Created by j.boeser on 16-3-2017.
 */

public class SpotsDataSource {

    private SQLiteDatabase mDatabase;

    public SpotsDataSource (Context context) {
        mDatabase = new DatabaseHelper(context).getWritableDatabase();
    }

    private static ContentValues getContentValues(SpotItem hotspots) {
        ContentValues values = new ContentValues();

        values.put(SpotsDBSchema.SpotsTable.Colums.TITLE, hotspots.getName());
        values.put(SpotsDBSchema.SpotsTable.Colums.CITY, hotspots.getCity());
        values.put(String.valueOf(SpotsDBSchema.SpotsTable.Colums.LONGITUDE), hotspots.getLongitude());
        values.put(String.valueOf(SpotsDBSchema.SpotsTable.Colums.LATITUDE), hotspots.getLatitude());

        return values;
    }

    public void addSpots (SpotItem spots) {
        ContentValues values = getContentValues(spots);

        mDatabase.insert(SpotsDBSchema.SpotsTable.NAME, null, values);
    }

    public void updateSpots (SpotItem spots) {
        String idString = Long.toString(spots.getId());
        ContentValues values = getContentValues(spots);

        mDatabase.update(SpotsDBSchema.SpotsTable.NAME, values,
                SpotsDBSchema.SpotsTable.Colums._id + "=?",
                new String[] {idString});
    }

    private PlaceCursorWrapper querySpots(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                SpotsDBSchema.SpotsTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new PlaceCursorWrapper(cursor);
    }

    public Cursor getAllSpots() {
        return querySpots(null, null);
    }

    public SpotItem getSpotsById(long id) {
        PlaceCursorWrapper cursor = querySpots(SpotsDBSchema.SpotsTable.Colums._id + "=?",
                new String[] {Long.toString(id)});
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getPlace();
        } finally {
            cursor.close();
        }
    }

    public void deleteSpot(long id) {
        String idString = Long.toString(id);
        mDatabase.delete(SpotsDBSchema.SpotsTable.NAME,
                SpotsDBSchema.SpotsTable.Colums._id + "=?",
                new String[] {idString});
    }
}
