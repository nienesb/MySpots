package com.teamdating.myspots.Adapter;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.teamdating.myspots.Model.SpotItem;
import com.teamdating.myspots.Database.SpotsDBSchema;

/**
 * Created by j.boeser on 16-3-2017.
 */

public class PlaceCursorWrapper extends CursorWrapper {

    public PlaceCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public SpotItem getPlace() {
        String id = getString(getColumnIndex(SpotsDBSchema.SpotsTable.Colums._id));
        String name = getString(getColumnIndex(SpotsDBSchema.SpotsTable.Colums.TITLE));
        String city = getString(getColumnIndex(SpotsDBSchema.SpotsTable.Colums.CITY));
        double latitude = getDouble(getColumnIndex(String.valueOf(SpotsDBSchema.SpotsTable.Colums.LATITUDE)));
        double longitude = getDouble(getColumnIndex(String.valueOf(SpotsDBSchema.SpotsTable.Colums.LONGITUDE)));

        SpotItem spotitem = new SpotItem(name, city, latitude, longitude);
        spotitem.setName(name);
        spotitem.setCity(city);
        spotitem.setLatitude(latitude);
        spotitem.setLongitude(longitude);

        return spotitem;
    }
}
