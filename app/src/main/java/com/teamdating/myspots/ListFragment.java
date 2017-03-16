package com.teamdating.myspots;

import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    private List<SpotItem> mSpots;
    private PlaceCursorWrapper cursorWrapper;
    private CursorAdapter mAdapter;
    private Cursor mCursor;
    private ListView mListView;
    private SpotsDataSource mDatasource;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragmentManager = getSupportFragmentManager();
        Fragment listFragment = mFragmentManager.findFragmentById(R.id.list_container);
        if (listFragment == null) {
            listFragment = new AnimalListFragment();
            mFragmentManager.beginTransaction()
                    .add(R.id.list_container, listFragment)
                    .commit();
        }

        mListView = (ListView) mListView.findViewById(R.id.list_view);
        mSpots = new ArrayList<SpotItem>();
        mDatasource = new SpotsDataSource(Context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_list, container, false);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        mListView = (ListView) view.findViewById(R.id.list_view);

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


