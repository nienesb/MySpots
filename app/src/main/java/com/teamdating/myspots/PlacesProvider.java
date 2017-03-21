package com.teamdating.myspots;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by j.boeser on 16-3-2017.
 */

public class PlacesProvider extends ContentProvider{

    SQLiteDatabase mDatabase;
    private static final String AUTHORITY = "com.teamdating.myspots";
    private static final String BASE_PATH = "locations";
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int LOCATIONS = 1;
    private static final int LOCATION_ID = 2;
    public static final String CONTENT_ITEM_TYPE = "Locations";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    static {
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH, LOCATIONS);
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH + "/#", LOCATION_ID);
    }

    @Override
    public boolean onCreate() {
        DatabaseHelper helper = new DatabaseHelper(getContext());
        mDatabase = helper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        if (URI_MATCHER.match(uri) == LOCATION_ID) {
            selection = DatabaseHelper.ALL_COLUMNS + "=?" + uri.getLastPathSegment();
        }
        // The data is filtered in the UI so the 'selection' argument is passed with it
        return mDatabase.query(SpotsDBSchema.SpotsTable.NAME, DatabaseHelper.ALL_COLUMNS, selection, null, null, null, null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long id = mDatabase.insert(SpotsDBSchema.SpotsTable.NAME, null, values);
        //Create the URI to pass back that includes the new primary key value.
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return mDatabase.delete(SpotsDBSchema.SpotsTable.NAME, selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return mDatabase.update(SpotsDBSchema.SpotsTable.NAME, values, selection, selectionArgs);
    }
}
