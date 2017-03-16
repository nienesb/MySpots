package com.teamdating.myspots;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by j.boeser on 16-3-2017.
 */

public class SpotAdapter extends CursorAdapter {

    public SpotAdapter (Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.activity_list_row, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        PlaceCursorWrapper cursorWrapper = (PlaceCursorWrapper) cursor;
        SpotItem hotspot = cursorWrapper.getPlace();

        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(hotspot.getName());
        TextView city = (TextView) view.findViewById(R.id.city);
        city.setText(hotspot.getCity());
        TextView longitude = (TextView) view.findViewById(R.id.longitude);
        longitude.setText((int) hotspot.getLongitude());
        TextView latitude = (TextView) view.findViewById(R.id.latitude);
        latitude.setText((int) hotspot.getLatitude());
    }
}
