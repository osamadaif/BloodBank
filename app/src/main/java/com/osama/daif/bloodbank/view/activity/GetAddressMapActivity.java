package com.osama.daif.bloodbank.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
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

import com.google.android.gms.common.api.GoogleApiClient;
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
import com.osama.daif.bloodbank.view.fragment.homeCycle.donation.CreateDonationFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.osama.daif.bloodbank.view.fragment.homeCycle.donation.CreateDonationFragment.addressLan;
import static com.osama.daif.bloodbank.view.fragment.homeCycle.donation.CreateDonationFragment.addressLat;
import static com.osama.daif.bloodbank.view.fragment.homeCycle.donation.CreateDonationFragment.hospitalAddress;
import static com.osama.daif.bloodbank.view.fragment.homeCycle.donation.CreateDonationFragment.hospitalName;

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
    String hospitalFirstName;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_address_map);
        ButterKnife.bind(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        geocoder = new Geocoder(GetAddressMapActivity.this);
        toolbar = findViewById(R.id.toolbar);
        appbar.setVisibility(View.VISIBLE);
        toolbarBackBtn.setVisibility(View.GONE);
        setTitle("");
        toolbarTxtSup.setText(getString(R.string.Select_address));
        setSupportActionBar(toolbar);
        getLocationPermission();

    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void initMap() {
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        assert mapFragment != null;
        mapFragment.getMapAsync(GetAddressMapActivity.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


//        Toast.makeText (this, "Map Is Ready", Toast.LENGTH_SHORT).show ( );
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (mLocationPermissionGranted) {

                    getDeviceLocation();
//            mMap.getUiSettings ( ).setMyLocationButtonEnabled (false);

                }
            }
        }, 1500);


        mMap.setOnMapClickListener(point -> {
            addressLt = point.latitude;
            addressLn = point.longitude;

            addressLat = addressLt;
            addressLan = addressLn;
            try {
                List<Address> addresses = geocoder.getFromLocation(addressLt, addressLn, 1);
                addressName = addresses.get(0).getAddressLine(0);
                hospitalFirstName = addresses.get(0).getAdminArea();
                hospitalName = hospitalFirstName;
                hospitalAddress = addressName;
                Toast.makeText(GetAddressMapActivity.this, addressName, Toast.LENGTH_LONG).show();

            } catch (IOException e) {
                e.printStackTrace();
            }

            //remove previously placed Marker
            if (marker != null) {
                marker.remove();
            }
            //place marker where user just clicked
            marker = mMap.addMarker(new MarkerOptions().position(point).title("Address")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                moveCamera(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude)
                );
            }
        });
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        try {
            if (mLocationPermissionGranted) {
                final Task<Location> location = mFusedLocationProviderClient.getLastLocation();

                location.addOnCompleteListener(task -> {
                    Location result = task.getResult();
                    boolean successful = task.isSuccessful();
                    if (successful && result != null) {
                        Log.d(TAG, "onComplete: found location");
                        Location currentLocation = (Location) task.getResult();
                        moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())
                        );

                    } else {
                        Log.d(TAG, "onComplete: current location is null");
                        Toast.makeText(GetAddressMapActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        } catch (SecurityException e) {
            Log.d(TAG, "getDeviceLocation: SecurityException:" + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng) {
        Log.d(TAG, "moveCamera: moving the camera to: lat:" + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, GetAddressMapActivity.DEFAULT_ZOOM));
        mMap.clear();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionGranted = false;

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        mLocationPermissionGranted = false;
                        Log.d(TAG, "onRequestPermissionsResult: permission failed");
                        return;
                    }
                }
                Log.d(TAG, "onRequestPermissionsResult: permission granted");
                mLocationPermissionGranted = true;
                initMap();
            }
        }
    }


    @OnClick(R.id.activity_address_map_btn_choose_location)
    public void onClick() {
        finish();

    }
}
