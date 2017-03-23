package com.teamdating.myspots;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.List;

import static android.support.v7.widget.RecyclerView.*;

/**
 * Created by j.boeser on 16-3-2017.
 */

public class SpotAdapter extends RecyclerView.Adapter<SpotAdapter.MyViewHolder> {

    final Context context;
    private final List<SpotItem> spotItemList;

    public SpotAdapter(List<SpotItem> spotItemList, Context context) {
        this.spotItemList = spotItemList;
        this.context = context;
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
        return spotItemList.get(position).getId();
    }

    public void updateList(List<SpotItem> newList) {
        spotItemList.clear();
        spotItemList.addAll(newList);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SpotAdapter.MyViewHolder holder, int position) {
        holder.populateRow(getItem(position));
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

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
