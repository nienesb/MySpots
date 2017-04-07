package com.teamdating.myspots;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import static android.R.attr.id;
import static android.app.Activity.RESULT_OK;

public class ListFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private SpotAdapter mAdapter;
    private SpotsDataSource mDatasource;
    private List<SpotItem> mItems;
    private PlaceCursorWrapper mCursor;
    private static final int PLACE_DETAIL_REQUEST_CODE = 1234;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatasource = new SpotsDataSource(getContext());
        mCursor = new PlaceCursorWrapper(mCursor);
        mItems = new ArrayList<>();

        View view = inflater.inflate(R.layout.activity_list, container, false);

        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.idRecyclerview);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setAdapter(new SpotAdapter(mCursor));

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        getLoaderManager().initLoader(0, null, this).forceLoad();
        updateUi();

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void updateUi() {
        mCursor = (PlaceCursorWrapper) mDatasource.getAllSpots();
        if (mAdapter == null) {
            mAdapter = new SpotAdapter(mCursor);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.swapCursor(mCursor);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), NewSpotActivity.class);
        startActivityForResult(intent, PLACE_DETAIL_REQUEST_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), PlacesProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Take the data represented by the cursor object, and pass it to the cursor adaptor
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_DETAIL_REQUEST_CODE && resultCode == RESULT_OK) {
            restartLoader();
            updateUi();
        }
    }
}


