package com.teamdating.myspots;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

public class ListFragment extends Fragment implements View.OnClickListener {


    private RecyclerView.LayoutManager mLayoutManager;

    private RecyclerView mRecyclerView;
    private SpotAdapter mAdapter;
    private List<SpotItem> mItems;
    private PlaceCursorWrapper mCursor;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_list_row, container, false);

        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.idRecyclerview);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(new SpotAdapter(getContext(), mCursor));

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        SpotItem spotItemList[] = {
                new SpotItem("Zara", "Haarlem", 52.378757, 4.632956)
        };

        updateUi();

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void updateUi() {
        SpotsDataSource mDatasource = new SpotsDataSource(getContext());
        mCursor = (PlaceCursorWrapper) mDatasource.getAllSpots();
        if (mAdapter == null) {
            mAdapter = new SpotAdapter(getContext(), mCursor);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), NewSpotActivity.class);
        startActivity(intent);
    }
}


