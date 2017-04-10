package com.teamdating.myspots.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.teamdating.myspots.Model.SpotItem;
import com.teamdating.myspots.Fragments.NewSpotActivity;
import com.teamdating.myspots.Database.PlacesProvider;
import com.teamdating.myspots.R;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;
import static android.app.PendingIntent.getActivity;
import static android.support.v7.widget.RecyclerView.*;

/**
 * Created by j.boeser on 16-3-2017.
 */

public class SpotAdapter extends RecyclerView.Adapter<SpotAdapter.MyViewHolder> {

    private Context mContext;
    private PlaceCursorWrapper mCursor;
    private SpotsDataSource mDatasource;

    // A reference to the column, used to get the ID of an item.
    private int mIdColumn;

    private static final int EDITOR_REQUEST_CODE = 1234;

    private List<SpotItem> spotItemList = new ArrayList<>();

    public SpotAdapter(Cursor cursor) {
        swapCursor(cursor);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.activity_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SpotAdapter.MyViewHolder holder, final int position) {
        if (mCursor != null && mCursor.moveToPosition(position)) {

            //fetch place from cursorwrapper
            SpotItem spotitem = mCursor.getPlace();
            holder.vName.setText(spotitem.getName());
            holder.vCity.setText(spotitem.getCity());
            holder.vLongitude.setText(String.valueOf(spotitem.getLongitude()));
            holder.vLatitude.setText(String.valueOf(spotitem.getLatitude()));

            final long itemId = getItemId(position);
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), NewSpotActivity.class);
                    Uri uri = Uri.parse(PlacesProvider.CONTENT_URI + "/" + itemId);
                    intent.putExtra(PlacesProvider.CONTENT_ITEM_TYPE, uri);
                    v.getContext().startActivity(intent);
                }
            });

            holder.itemView.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mDatasource = new SpotsDataSource(v.getContext());
                    mDatasource.deleteSpot(itemId);
                    Toast.makeText(v.getContext(), "Item is deleted", Toast.LENGTH_SHORT).show();
                    notifyItemChanged(id);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    @Override
    public long getItemId(int position) {
        if (mCursor == null || !mCursor.moveToPosition(position)) {
            return 0;
        }
        return mCursor.getLong(mIdColumn);
    }

    public void swapCursor(Cursor cursor) {
        if (cursor != null) {
            mCursor = new PlaceCursorWrapper(cursor);
            //   mIdColumn = cursor.getColumnIndexOrThrow(SpotsDBSchema.SpotsTable.Colums._id);
            notifyDataSetChanged();
        } else {
            mCursor = null;
            mIdColumn = -1;
            notifyItemRangeRemoved(0, getItemCount());
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
            //v.setOnClickListener((OnClickListener) this);
        }

        public void populateRow (SpotItem spotItem) {
            vName.setText(spotItem.getName());
            vCity.setText(spotItem.getCity());
            vLongitude.setText((int) spotItem.getLongitude());
            vLatitude.setText((int) spotItem.getLatitude());
        }
    }
}
