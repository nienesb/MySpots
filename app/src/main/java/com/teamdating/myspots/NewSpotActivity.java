package com.teamdating.myspots;

import android.Manifest;
import android.content.ContentValues;
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

import java.util.List;
import java.util.Locale;

import static com.teamdating.myspots.R.id.city;
import static com.teamdating.myspots.R.id.latitude;
import static com.teamdating.myspots.R.id.longitude;
import static com.teamdating.myspots.R.id.name;

public class NewSpotActivity extends AppCompatActivity implements OnMapReadyCallback {

    private EditText mNameEditText;
    private String mPlaceFilter;
    private Marker mMarker;
    private SpotItem mPlace;
    private Uri mUri;
    private int PERMISSIONS_REQUEST_LOCATION;
    private GoogleMap mMap;
    private String mAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNameEditText = (EditText) findViewById(R.id.edit_text_place_name);
        mUri = getIntent().getParcelableExtra(PlacesProvider.CONTENT_ITEM_TYPE);
        mAction = getIntent().getAction();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        public void populateMap () {
            mPlace = new SpotItem("name", "city", latitude, longitude);
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

        if (mPlace.getLatitude() != 0.0 && mPlace.getLongitude() != 0.0) {
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
        mPlace.setName(name);
        switch (mAction) {
            case Intent.ACTION_INSERT:
                if (name.length() == 0) {
                    setResult(RESULT_CANCELED);
                } else {
                    addPlace(mPlace);
                }
                break;
            case Intent.ACTION_EDIT:
                if (name.length() == 0) {
                    deletePlace();
                } else {
                    updatePlace(mPlace);
                }
        }
        finish();
    }

   // @Override
    public boolean onOptionsItemSelected(SpotItem item) {
        switch ((int) item.getId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.delete_place:
                deletePlace();
                break;
            case R.id.add_place:
                finishEditing();
                break;
    }
        return true;
    }


    private void updatePlace(SpotItem hotspots) {
        ContentValues values = new ContentValues();
        values.put(SpotsDBSchema.SpotsTable.Colums.TITLE, hotspots.getName());
        values.put(SpotsDBSchema.SpotsTable.Colums.CITY, hotspots.getCity());
        values.put(String.valueOf(SpotsDBSchema.SpotsTable.Colums.LONGITUDE), hotspots.getLongitude());
        values.put(String.valueOf(SpotsDBSchema.SpotsTable.Colums.LATITUDE), hotspots.getLatitude());
        getContentResolver().update(PlacesProvider.CONTENT_URI, values, mPlaceFilter, null);
        Toast.makeText(NewSpotActivity.this, "Place updated", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void addPlace(SpotItem hotspots) {
        ContentValues values = new ContentValues();
        values.put(SpotsDBSchema.SpotsTable.Colums.TITLE, hotspots.getName());
        values.put(SpotsDBSchema.SpotsTable.Colums.CITY, hotspots.getCity());
        values.put(String.valueOf(SpotsDBSchema.SpotsTable.Colums.LONGITUDE), hotspots.getLongitude());
        values.put(String.valueOf(SpotsDBSchema.SpotsTable.Colums.LATITUDE), hotspots.getLatitude());
        getContentResolver().insert(PlacesProvider.CONTENT_URI, values);
        Toast.makeText(NewSpotActivity.this, "Place added", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void deletePlace() {
        getContentResolver().delete(PlacesProvider.CONTENT_URI, mPlaceFilter, null);
        Toast.makeText(NewSpotActivity.this, "Place deleted", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }
}
