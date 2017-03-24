package com.teamdating.myspots;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.List;

import javax.sql.DataSource;

import static android.R.attr.id;
import static android.support.v7.widget.RecyclerView.*;

/**
 * Created by j.boeser on 16-3-2017.
 */

public class SpotAdapter extends RecyclerView.Adapter<SpotAdapter.MyViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    private int mIdColumn;
    private DataSource mDataSource;
    private List<SpotItem> spotItemList;

    public SpotAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        mCursor = cursor;
        mDataSource = (DataSource) new SpotsDataSource(context);
    }

    @Override
    public int getItemCount() {
        return spotItemList.size();
    }

    private SpotItem getItem(int position) {
        return spotItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (mCursor == null || !mCursor.moveToPosition(position)) {
            return 0;
        }
        return mCursor.getLong(mIdColumn);
    }

    public void updateList(List<SpotItem> newList) {
        spotItemList.clear();
        spotItemList.addAll(newList);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.activity_list_row, parent, false);
        SpotAdapter.MyViewHolder viewHolder = new SpotAdapter.MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SpotAdapter.MyViewHolder holder, final int position) {
        if (mCursor != null && mCursor.moveToPosition(position)) {
            SpotItem spotItem = SpotsDataSource.getSpotsById(id);
            holder.populateRow(getItem(position));
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCursor.moveToPosition(position);
                    SpotItem clickedItem = SpotsDataSource.getSpotsById(id);
                    Intent intent = new Intent(mContext, NewSpotActivity.class);
                }
            });
        }
    }

    public void swapCursor(Cursor cursor) {
        if (cursor != null) {
            mCursor = cursor;
            mIdColumn = cursor.getColumnIndexOrThrow(String.valueOf(DatabaseHelper.ALL_COLUMNS));
        } else {
            mCursor = null;
            mIdColumn = -1;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        protected TextView vName;
        protected TextView vCity;
        protected TextView vLongitude;
        protected TextView vLatitude;

        public MyViewHolder (View v) {
            super(v);
            vName = (TextView) v.findViewById(R.id.name);
            vCity = (TextView) v.findViewById(R.id.city);
            vLongitude = (TextView) v.findViewById(R.id.longitude);
            vLatitude = (TextView) v.findViewById(R.id.latitude);

            v.setOnClickListener((OnClickListener) this);
        }

        public void populateRow (SpotItem spotItem) {
            vName.setText(spotItem.getName());
            vCity.setText(spotItem.getCity());
            vLongitude.setText((int) spotItem.getLongitude());
            vLatitude.setText((int) spotItem.getLatitude());
        }
    }
}
