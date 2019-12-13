package com.osama.daif.bloodbank.view.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.osama.daif.bloodbank.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GetAddressMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.toolbar_back_btn)
    ImageView toolbarBackBtn;
    @BindView(R.id.toolbar_txt_sup)
    TextView toolbarTxtSup;
    @BindView(R.id.activity_address_map_btn_choose_location)
    Button activityAddressMapBtnChooseLocation;
    @BindView(R.id.appbar_map)
    AppBarLayout appbar;

    Toolbar toolbar;

    String addressName;
    double addressLt;
    double addressLn;

    private Marker marker;
    Geocoder geocoder;

    private static final String TAG = "GetAddressMapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    public void onMapReady(GoogleMap googleMap) {
//        Toast.makeText (this, "Map Is Ready", Toast.LENGTH_SHORT).show ( );
        Log.d (TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if (mLocationPermissionGranted) {
            getDeviceLocation ( );
            mMap.setMyLocationEnabled (true);
//            mMap.getUiSettings ( ).setMyLocationButtonEnabled (false);

        }

        mMap.setOnMapClickListener (new GoogleMap.OnMapClickListener ( ) {
            @Override
            public void onMapClick(LatLng point) {
                addressLt = point.latitude;
                addressLn = point.longitude;
                List<Address> addresses = new ArrayList<> ();
                try {
                    addresses = geocoder.getFromLocation(addressLt, addressLn,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                android.location.Address address = addresses.get(0);
                if (address != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++){
                        sb.append (address.getAddressLine (i)).append ("\n");
                    }
                    addressName = sb.toString ();
                    Toast.makeText(GetAddressMapActivity.this, addressName, Toast.LENGTH_LONG).show();
                }
                //remove previously placed Marker
                if (marker != null) {
                    marker.remove();
                }
                //place marker where user just clicked
                marker = mMap.addMarker(new MarkerOptions ().position(point).title("Marker")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_get_address_map);
        ButterKnife.bind (this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient (this);
        geocoder = new Geocoder(GetAddressMapActivity.this);
        toolbar = findViewById (R.id.toolbar);
        appbar.setVisibility (View.VISIBLE);
        toolbarBackBtn.setVisibility (View.GONE);
        setTitle ("");
        toolbarTxtSup.setText (getString (R.string.Select_address));
        setSupportActionBar (toolbar);
        getLocationPermission ( );

    }

    private void getDeviceLocation() {
        Log.d (TAG, "getDeviceLocation: getting the devices current location");

        try {
            if (mLocationPermissionGranted) {
               final Task<Location> location = mFusedLocationProviderClient.getLastLocation ( );
                location.addOnCompleteListener (task -> {
                    if (task.isSuccessful ( ) && task.getResult() != null) {
                        Log.d (TAG, "onComplete: found location");
                        Location currentLocation = (Location) task.getResult ( );
                        moveCamera (new LatLng (currentLocation.getLatitude ( ), currentLocation.getLongitude ( ))
                        );

                    } else {
                        Log.d (TAG, "onComplete: current location is null");
                        Toast.makeText (GetAddressMapActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show ( );
                    }
                });

            }
        } catch (SecurityException e) {
            Log.d (TAG, "getDeviceLocation: SecurityException:" + e.getMessage ( ));
        }
    }

    private void moveCamera(LatLng latLng) {
        Log.d (TAG, "moveCamera: moving the camera to: lat:" + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.animateCamera (CameraUpdateFactory.newLatLngZoom (latLng, GetAddressMapActivity.DEFAULT_ZOOM));
        mMap.clear();
    }

    private void initMap() {
        Log.d (TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager ( ).findFragmentById (R.id.map);

        assert mapFragment != null;
        mapFragment.getMapAsync (GetAddressMapActivity.this);
    }

    private void getLocationPermission() {
        Log.d (TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission (this.getApplicationContext ( ),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission (this.getApplicationContext ( ),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap ( );
            } else {
                ActivityCompat.requestPermissions (this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions (this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d (TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionGranted = false;

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        mLocationPermissionGranted = false;
                        Log.d (TAG, "onRequestPermissionsResult: permission failed");
                        return;
                    }
                }
                Log.d (TAG, "onRequestPermissionsResult: permission granted");
                mLocationPermissionGranted = true;
                initMap ( );
            }
        }
    }


    @OnClick(R.id.activity_address_map_btn_choose_location)
    public void onClick() {

    }
}
