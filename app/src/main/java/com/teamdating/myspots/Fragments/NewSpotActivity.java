package com.teamdating.myspots.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teamdating.myspots.Adapter.PlaceCursorWrapper;
import com.teamdating.myspots.Adapter.SpotAdapter;
import com.teamdating.myspots.Adapter.SpotsDataSource;
import com.teamdating.myspots.Database.DatabaseHelper;
import com.teamdating.myspots.Database.PlacesProvider;
import com.teamdating.myspots.Database.SpotsDBSchema;
import com.teamdating.myspots.Model.SpotItem;
import com.teamdating.myspots.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.R.attr.id;
import static com.teamdating.myspots.R.id.latitude;
import static com.teamdating.myspots.R.id.longitude;

public class NewSpotActivity extends AppCompatActivity implements OnMapReadyCallback {

    private EditText mNameEditText;
    private String mPlaceFilter;
    private Marker mMarker;
    private SpotItem mPlace;
    private Uri mUri;
    private int PERMISSIONS_REQUEST_LOCATION;
    private GoogleMap mMap;
    private String mAction;
    private SpotsDataSource mDatasource;
    private PlaceCursorWrapper mCursor;
    private SpotAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatasource = new SpotsDataSource(this);
        mCursor = new PlaceCursorWrapper(mCursor);
        mAdapter = new SpotAdapter(mCursor);

        mNameEditText = (EditText) findViewById(R.id.edit_text_place_name);
        mUri = getIntent().getParcelableExtra(PlacesProvider.CONTENT_ITEM_TYPE);
        mAction = getIntent().getAction();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        populateMap();
    }

    // check if action is edit or insert
    public void populateMap() {
        mPlace = new SpotItem(null,null, latitude, longitude);
        // If the mUri is NULL, the activity is setup empty and seen as a new mPlace
        if (mUri == null) {
            mAction = Intent.ACTION_INSERT;
            setTitle("New Place");
        } else {
            // If the mUri is not NULL, the activity is loaded with the data from the database
            setTitle("Edit Place");
            mAction = Intent.ACTION_EDIT;
            mPlaceFilter = SpotsDBSchema.SpotsTable.Colums._id + "=" + mUri.getLastPathSegment();
            PlaceCursorWrapper cursor = new PlaceCursorWrapper(getContentResolver().query(
                    PlacesProvider.CONTENT_URI,
                    DatabaseHelper.ALL_COLUMNS,
                    mPlaceFilter,
                    null,
                    null
            ));
            cursor.moveToFirst();
            mPlace = cursor.getPlace();
            cursor.close();
            // Set the values in the view
            mNameEditText.setText(mPlace.getName());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mAction.equals(Intent.ACTION_INSERT)) {
            getMenuInflater().inflate(R.menu.context_menu, menu);
        }

        if (mAction.equals(Intent.ACTION_EDIT)) {
            getMenuInflater().inflate(R.menu.edit_menu, menu);
        }

        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        enableMyLocation();

        // remove the marker when a new one is placed
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                marker.remove();
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
            }
        });

        // add new marker on long click
       mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                addMarker(latLng);
                // set the LatLng values each time a new marker is created
                mPlace.setLatitude(mMarker.getPosition().latitude);
                mPlace.setLongitude(mMarker.getPosition().longitude);
                mPlace.setCity(showCityName(mPlace.getLatitude(), mPlace.getLongitude()));
            }
        });

        // Tijdelijke fix om if else te omzeilen 1==2 toegevoegd
       if (mPlace != null && mPlace.getLatitude() != 0.0 && mPlace.getLongitude() != 0.0 && 1==2) {
            // add marker and animate camera
            LatLng latLng = new LatLng(mPlace.getLatitude(), mPlace.getLongitude());
            addMarker(latLng);
            CameraUpdate markerLocation = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            mMap.animateCamera(markerLocation);
        } else {
            LatLng latLng = new LatLng(52.092876, 5.104480);
            CameraUpdate markerLocation = CameraUpdateFactory.newLatLngZoom(latLng, 6);
            mMap.animateCamera(markerLocation);
        }
    }

    private void enableMyLocation() {
        // Check if location permissions are granted.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request the permission.
            // The dialog box asking for permission is generated by this call.
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_LOCATION
            );
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                // location-related task you need to do.
                enableMyLocation();
            } else {
                // Otherwise, disable functionality that requires permission(s)
                // and let your user know about it.
            }
        }
    }

    // This method returns a String value representing the name of the city given any coordinates.
    // When no city is found a default value will be returned
    
    private String showCityName(double latitude, double longitude) {
        String cityName = "*No City*";
        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            cityName = addresses.get(0).getLocality();
        } catch (Exception e) {
            Toast.makeText(NewSpotActivity.this, "No city could be found", Toast.LENGTH_SHORT).show();
        }
        return cityName;
    }

    private List<Double> showLatLong(String locationName) {
        List<Double> list = new ArrayList<Double>();
        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(locationName, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0) {
            list.add(addresses.get(0).getLatitude());
            list.add(addresses.get(0).getLongitude());

            return list;
        }
        return list;
    }

    private void addMarker(LatLng latLng) {
        mMap.clear();
        mMarker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(mNameEditText.getText().toString())
                .snippet("city: " + showCityName(latLng.latitude, latLng.longitude))
                .draggable(false)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
    }

    private void finishEditing() {
        // get the string from the EditText and set it in the object
        String name = mNameEditText.getText().toString().trim();
        List<Double> latlonng = showLatLong(name);
        mPlace = new SpotItem(name, showCityName(latlonng.get(0),latlonng.get(1)), latlonng.get(0),latlonng.get(1));

        switch (mAction) {
            case Intent.ACTION_INSERT:
                if (name.length() == 0) {
                    setResult(RESULT_CANCELED);
                } else {
                    mDatasource.addSpots(mPlace);
                    Toast.makeText(NewSpotActivity.this, "Item is added", Toast.LENGTH_SHORT).show();
                }
                break;
            case Intent.ACTION_EDIT:
                if (name.length() == 0) {
                    mDatasource.deleteSpot(id);
                } else {
                    mDatasource.updateSpot(mPlace);
                }
        }
        finish();
    }

    // @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ((int) item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.delete_place:
                mDatasource.deleteSpot(id);
                break;
            case R.id.add_place:
                finishEditing();
                break;
        }
        return true;
    }
}
