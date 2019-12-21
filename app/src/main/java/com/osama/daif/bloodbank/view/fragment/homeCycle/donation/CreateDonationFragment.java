package com.osama.daif.bloodbank.view.fragment.homeCycle.donation;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.adapter.SpinnerAdapter2;
import com.osama.daif.bloodbank.data.model.donation.Donation;
import com.osama.daif.bloodbank.view.activity.BaseActivity;
import com.osama.daif.bloodbank.view.activity.GetAddressMapActivity;
import com.osama.daif.bloodbank.view.activity.HomeCycleActivity;
import com.osama.daif.bloodbank.view.fragment.BaseFragment;
import com.osama.daif.bloodbank.view.fragment.HomeContainerFragment;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.osama.daif.bloodbank.data.api.RetrofitClient.getClient;
import static com.osama.daif.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.osama.daif.bloodbank.helper.GeneralRequest.getData;
import static com.osama.daif.bloodbank.helper.HelperMethods.replaceFragment;

public class CreateDonationFragment extends BaseFragment {
    private static final String TAG = CreateDonationFragment.class.getSimpleName ( );

    private static final int ERROR_DIALOG_REQUEST = 9001;

    @BindView(R.id.create_donation_txt_name)
    EditText createDonationTxtName;
    @BindView(R.id.create_donation_txt_age)
    EditText createDonationTxtAge;
    @BindView(R.id.create_donation_sp_blood_type)
    Spinner createDonationSpBloodType;
    @BindView(R.id.create_donation_txt_number_of_bags)
    EditText createDonationTxtNumberOfBags;
    @BindView(R.id.create_donation_txt_hospital_address)
    EditText createDonationTxtHospitalAddress;
    @BindView(R.id.create_donation_sp_government)
    Spinner createDonationSpGovernment;
    @BindView(R.id.create_donation_sp_city)
    Spinner createDonationSpCity;
    @BindView(R.id.create_donation_txt_phone)
    EditText createDonationTxtPhone;
    @BindView(R.id.create_donation_txt_notes)
    EditText createDonationTxtNotes;
    @BindView(R.id.create_donation_btn_send)
    Button createDonationBtnSend;

    private Unbinder unbinder = null;

    private SpinnerAdapter2 bloodTypeAdapter, governoratesAdapter, cityAdapter;

    private GoogleApiClient googleApiClient;
    private final static int REQUEST_LOCATION = 199;
    public static String hospitalAddress = null;
    public static String hospitalName = null;
    public static double addressLat;
    public static double addressLan;

    private HomeCycleActivity homeCycleActivity;

    public CreateDonationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_create_donation, container, false);
        unbinder = ButterKnife.bind (this, view);
        // Inflate the layout for this fragment
        initFragment ( );
        homeCycleActivity = (HomeCycleActivity) getActivity ( );
        assert homeCycleActivity != null;
        homeCycleActivity.appbarVisibility(View.VISIBLE);
        homeCycleActivity.editToolbarTxtSup (R.string.donation_request);
        homeCycleActivity.bottomNavigationVisibility (View.GONE);
        homeCycleActivity.setBackBtnVisibility (View.VISIBLE);
        homeCycleActivity.setBehavior(new AppBarLayout.ScrollingViewBehavior());
        homeCycleActivity.onBackBtnClick (v -> getActivity().onBackPressed());

        bloodTypeAdapter = new SpinnerAdapter2 (getActivity ( ));
        getData (getClient ( ).getBloodTypes ( ), bloodTypeAdapter, getResources ( ).getString (R.string.blood_type), createDonationSpBloodType);

        governoratesAdapter = new SpinnerAdapter2 (getActivity ( ));
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener ( ) {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {

                    cityAdapter = new SpinnerAdapter2 (getActivity ( ));
                    getData (getClient ( ).getCities (governoratesAdapter.selectedId), cityAdapter, getResources ( ).getString (R.string.city), createDonationSpCity);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        getData (getClient ( ).getGovernorates ( ), governoratesAdapter, getResources ( ).getString (R.string.governorate), createDonationSpGovernment, listener);
        return view;
    }

    public Boolean isServicesOk() {
        Log.d (TAG, "isServicesOk: checking google services version");
        int available = GoogleApiAvailability.getInstance ( ).isGooglePlayServicesAvailable (getActivity ( ));

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d (TAG, "isServicesOk: Google play services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance ( ).isUserResolvableError (available)) {
            //an error occured but we can resolve it
            Log.d (TAG, "isServicesOk: an error occured but you can resolve it");
            Dialog dialog = GoogleApiAvailability.getInstance ( ).getErrorDialog (getActivity ( ), available, ERROR_DIALOG_REQUEST);
            dialog.show ( );
        } else {
            Toast.makeText (homeCycleActivity, "you can't make map requests", Toast.LENGTH_SHORT).show ( );
        }
        return false;
    }

    private void enableLoc() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder (Objects.requireNonNull (getActivity ( )))
                    .addApi (LocationServices.API)
                    .addConnectionCallbacks (new GoogleApiClient.ConnectionCallbacks ( ) {
                        @Override
                        public void onConnected(@Nullable Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect ( );
                        }
                    })
                    .addOnConnectionFailedListener (connectionResult ->
                            Log.d ("Location error", "Location error " + connectionResult.getErrorCode ( )))
                    .build ( );

            googleApiClient.connect ( );

            LocationRequest locationRequest = LocationRequest.create ( );
            locationRequest.setPriority (LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval (30 * 1000);
            locationRequest.setFastestInterval (5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder ( )
                    .addLocationRequest (locationRequest);

            builder.setAlwaysShow (true); //this is the key ingredient

            SettingsClient client = LocationServices.getSettingsClient (getActivity ( ));
            Task<LocationSettingsResponse> task = client.checkLocationSettings (builder.build ( ));
            task.addOnSuccessListener (getActivity ( ), locationSettingsResponse -> {
            });

            task.addOnFailureListener (getActivity ( ), e -> {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult (getActivity ( ),
                                REQUEST_LOCATION);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            });
        }
    }

    @Override
    public void onBack() {
        super.onBack ( );
        homeCycleActivity.setBackBtnVisibility (View.GONE);
        homeCycleActivity.appbarVisibility(View.GONE);
    }

    @OnClick({R.id.create_donation_txt_hospital_address, R.id.create_donation_btn_send})
    public void onClick(View view) {
        switch (view.getId ( )) {
            case R.id.create_donation_txt_hospital_address:
                if (isServicesOk ( )) {
                    final LocationManager manager = (LocationManager) Objects.requireNonNull (getActivity ( )).getSystemService (Context.LOCATION_SERVICE);
                    assert manager != null;
                    if (!manager.isProviderEnabled (LocationManager.GPS_PROVIDER) && hasGPSDevice (getActivity ( ))) {
                        enableLoc ( );
                    } else {
                        startActivity (new Intent (baseActivity.getApplicationContext ( ), GetAddressMapActivity.class));
                    }

                }

                break;
            case R.id.create_donation_btn_send:
                setDonationDetails ( );
                break;
        }
    }

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService (Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders ( );
        if (providers == null)
            return false;
        return providers.contains (LocationManager.GPS_PROVIDER);
    }

    private void setDonationDetails() {
        String apiToken = loadUserData (getActivity ( )).getApiToken ( );

//        if (createDonationTxtName == null || bloodTypeAdapter.selectedId == 0) {
//            return;
//        }
        String name = createDonationTxtName.getText ( ).toString ( ).trim ( );
        String age = createDonationTxtAge.getText ( ).toString ( ).trim ( );
        int bloodType = bloodTypeAdapter.selectedId;
        String numberOfBags = createDonationTxtNumberOfBags.getText ( ).toString ( ).trim ( );
        String hospitalAddress = createDonationTxtHospitalAddress.getText ( ).toString ( ).trim ( );
        int city = cityAdapter.selectedId;
        String phoneNumber = createDonationTxtPhone.getText ( ).toString ( ).trim ( );
        String note = createDonationTxtNotes.getText ( ).toString ( ).trim ( );
        if (hospitalName == null){
            Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
            return;
        }

        getClient ( ).createNewDonation (apiToken, name, age, bloodType, numberOfBags, hospitalName, hospitalAddress, city, phoneNumber, note, addressLat, addressLan).enqueue (new Callback<Donation> ( ) {
            @Override
            public void onResponse(Call<Donation> call, Response<Donation> response) {
                if (response.body ( ).getStatus ( ) == 1) {
                    try {
                        replaceFragment (getActivity ( ).getSupportFragmentManager ( ), R.id.home_container_fr_frame, new HomeContainerFragment ( ));


                    } catch (Exception e) {
                        Toast.makeText (getActivity ( ), e.getMessage ( ), Toast.LENGTH_SHORT).show ( );
                    }
                }

            }

            @Override
            public void onFailure(Call<Donation> call, Throwable t) {
                Toast.makeText (getActivity ( ), t.getMessage ( ), Toast.LENGTH_SHORT).show ( );

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume ( );
        if (hospitalAddress != null) {
            createDonationTxtHospitalAddress.setText (hospitalAddress);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView ( );
        unbinder.unbind ();
    }
}
