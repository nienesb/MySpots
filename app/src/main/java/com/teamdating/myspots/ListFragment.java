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

public class ListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private SpotsDataSource mDataSource;
    private SpotAdapter mAdapter;
    private List<SpotItem> mItems;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_list_row, container, false);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.spots_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mRecyclerView.setAdapter(new SpotAdapter(SpotAdapter.MyViewHolder.populateRow(mItems), R.layout.activity_list_row));

        SpotItem spotItemList[] = {
                new SpotItem("Zara", "Haarlem", 52.378757, 4.632956)
        };


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NewSpotActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}


