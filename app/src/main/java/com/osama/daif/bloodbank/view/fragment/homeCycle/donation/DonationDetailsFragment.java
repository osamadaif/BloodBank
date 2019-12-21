package com.osama.daif.bloodbank.view.fragment.homeCycle.donation;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.data.model.donation.DonationData;
import com.osama.daif.bloodbank.data.model.donationDetails.DonationDetails;
import com.osama.daif.bloodbank.data.model.notification.NotificationData;
import com.osama.daif.bloodbank.view.activity.HomeCycleActivity;
import com.osama.daif.bloodbank.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CALL_PHONE;
import static com.osama.daif.bloodbank.data.api.RetrofitClient.getClient;
import static com.osama.daif.bloodbank.data.local.SharedPreferencesManger.loadUserData;

public class DonationDetailsFragment extends BaseFragment implements OnMapReadyCallback {

    @BindView(R.id.fragment_donation_details_tv_name_value)
    TextView fragmentDonationDetailsTvNameValue;
    @BindView(R.id.fragment_donation_details_tv_age_value)
    TextView fragmentDonationDetailsTvAgeValue;
    @BindView(R.id.fragment_donation_details_tv_blood_type_value)
    TextView fragmentDonationDetailsTvBloodTypeValue;
    @BindView(R.id.fragment_donation_details_tv_number_of_bags_value)
    TextView fragmentDonationDetailsTvNumberOfBagsValue;
    @BindView(R.id.fragment_donation_details_tv_hospital_value)
    TextView fragmentDonationDetailsTvHospitalValue;
    @BindView(R.id.fragment_donation_details_tv_address_value)
    TextView fragmentDonationDetailsTvAddressValue;
    @BindView(R.id.fragment_donation_details_tv_phone_value)
    TextView fragmentDonationDetailsTvPhoneValue;
    @BindView(R.id.fragment_donation_details_tv_notes_value)
    TextView fragmentDonationDetailsTvNotesValue;
    @BindView(R.id.fragment_donation_details_btn_call)
    Button fragmentDonationDetailsBtnCall;
    @BindView(R.id.fragment_donation_details_progress_bar)
    ProgressBar fragmentDonationDetailsProgressBar;
    private Unbinder unbinder;

    private String apiToken;
    private LatLng point;
    private Double lat;
    private Double lon;
    private Integer id;
    private String phoneNumber;


    private GoogleMap mMap;

    private static final float DEFAULT_ZOOM = 15f;
    private static final String TAG = "DonationDetailsFragment";

    public NotificationData notificationData;
    public DonationData donationData;

    private HomeCycleActivity homeCycleActivity;
    private SupportMapFragment mapFragment;

    public DonationDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_donation_details, container, false);
        unbinder = ButterKnife.bind (this, view);
        // Inflate the layout for this fragment
        initFragment ( );
        mapFragment = (SupportMapFragment) getChildFragmentManager ( )
                .findFragmentById (R.id.fragment_donation_details_map);
        assert mapFragment != null;
        mapFragment.getMapAsync (this);
        homeCycleActivity = (HomeCycleActivity) getActivity ( );

        assert homeCycleActivity != null;
        homeCycleActivity.setBackBtnVisibility (View.VISIBLE);
        homeCycleActivity.onBackBtnClick (v -> getActivity ( ).onBackPressed ( ));
        homeCycleActivity.appbarVisibility (View.VISIBLE);
        homeCycleActivity.setBehavior (new AppBarLayout.ScrollingViewBehavior ( ));
        homeCycleActivity.bottomNavigationVisibility (View.GONE);
        apiToken = loadUserData (getActivity ( )).getApiToken ( );

        if (notificationData != null) {
            id = notificationData.getId ( );
        } else {
            id = donationData.getId ( );
        }
        getDonationDetails ( );
        return view;
    }


    private void getDonationDetails() {
        fragmentDonationDetailsProgressBar.setVisibility (View.VISIBLE);
        getClient ( ).getDonationDetails (apiToken, id).enqueue (new Callback<DonationDetails> ( ) {
            @Override
            public void onResponse(Call<DonationDetails> call, Response<DonationDetails> response) {
                if (response.body ( ).getStatus ( ) == 1) {
                    fragmentDonationDetailsProgressBar.setVisibility (View.GONE);
                    homeCycleActivity.editToolbarTxtSup (getResources ( ).getString (R.string.donation_request) + " : " + response.body ( ).getData ( ).getPatientName ( ));
                    fragmentDonationDetailsTvNameValue.setText (response.body ( ).getData ( ).getPatientName ( ));
                    fragmentDonationDetailsTvAgeValue.setText (response.body ( ).getData ( ).getPatientAge ( ));
                    fragmentDonationDetailsTvBloodTypeValue.setText (response.body ( ).getData ( ).getBloodType ( ).getName ( ));
                    fragmentDonationDetailsTvNumberOfBagsValue.setText (response.body ( ).getData ( ).getBagsNum ( ));
                    fragmentDonationDetailsTvHospitalValue.setText (response.body ( ).getData ( ).getHospitalName ( ));
                    fragmentDonationDetailsTvAddressValue.setText (response.body ( ).getData ( ).getHospitalAddress ( ));
                    fragmentDonationDetailsTvPhoneValue.setText (response.body ( ).getData ( ).getPhone ( ));
                    fragmentDonationDetailsTvNotesValue.setText (response.body ( ).getData ( ).getNotes ( ));
                    lat = Double.valueOf (response.body ( ).getData ( ).getLatitude ( ));
                    lon = Double.valueOf (response.body ( ).getData ( ).getLongitude ( ));
                    phoneNumber = response.body ( ).getData ( ).getPhone ( );
                    point = new LatLng (lat, lon);
                    mMap.addMarker (new MarkerOptions ( ).position (point).title (getResources ( ).getString (R.string.address_marker))
                            .icon (BitmapDescriptorFactory.defaultMarker (BitmapDescriptorFactory.HUE_RED)));
                    mMap.moveCamera (CameraUpdateFactory.newLatLngZoom (point, DonationDetailsFragment.DEFAULT_ZOOM));
                }
            }

            @Override
            public void onFailure(Call<DonationDetails> call, Throwable t) {
                Toast.makeText (homeCycleActivity, t.getMessage ( ), Toast.LENGTH_SHORT).show ( );

            }
        });
    }

    @Override
    public void onBack() {
        if (notificationData != null) {
            super.onBack ( );
            homeCycleActivity.setBackBtnVisibility (View.GONE);
        } else {
            homeCycleActivity.setSelection (R.id.nav_home);
            homeCycleActivity.setBackBtnVisibility (View.GONE);
        }
    }

    @OnClick({R.id.fragment_donation_details_map, R.id.fragment_donation_details_btn_call})
    public void onViewClicked(View view) {
        switch (view.getId ( )) {
            case R.id.fragment_donation_details_map:
                break;
            case R.id.fragment_donation_details_btn_call:
                if (phoneNumber != null) {
                    Intent callIntent = new Intent (Intent.ACTION_CALL);
                    callIntent.setData (Uri.parse ("tel:" + phoneNumber));
                    if (ContextCompat.checkSelfPermission (getActivity ( ), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        startActivity (callIntent);
                    } else {
                        requestPermissions (new String[]{CALL_PHONE}, 1);
                    }

                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView ( );
        unbinder.unbind ( );
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

}
