package com.osama.daif.bloodbank.view.fragment.homeCycle.donation;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.adapter.DonationListRecyclerAdapter;
import com.osama.daif.bloodbank.adapter.SpinnerAdapter2;
import com.osama.daif.bloodbank.data.model.donation.Donation;
import com.osama.daif.bloodbank.data.model.donation.DonationData;
import com.osama.daif.bloodbank.helper.HelperMethods;
import com.osama.daif.bloodbank.helper.OnEndLess;
import com.osama.daif.bloodbank.view.activity.HomeCycleActivity;

import java.util.ArrayList;
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

public class DonationListFragment extends Fragment {

    @BindView(R.id.fragment_home_donation_rv)
    RecyclerView fragmentHomeDonationRv;
    @BindView(R.id.fragment_home_donation_sp_spinner_blood_type)
    Spinner fragmentHomeDonationSpSpinnerBloodType;
    @BindView(R.id.fragment_home_donation_sp_spinner_government)
    Spinner fragmentHomeDonationSpSpinnerGovernment;
    @BindView(R.id.fragment_home_donation_blood_type_layout)
    LinearLayout fragmentHomeDonationBloodTypeLayout;
    @BindView(R.id.fragment_home_donation_government_layout)
    LinearLayout fragmentHomeDonationGovernmentLayout;

    //no internet layout
    @BindView(R.id.lyt_no_connection)
    LinearLayout lytNoConnection;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private FloatingActionButton fab;
    private Unbinder unbinder = null;

    private LinearLayoutManager linearLayoutManager;
    private List<DonationData> donationList = new ArrayList<> ( );
    private DonationListRecyclerAdapter donationListAdapter;
    private int loadedPage = 1;
    private int maxPage = 0;
    private OnEndLess onEndLess;

    private SpinnerAdapter2 bloodTypeAdapter, governoratesAdapter;

    private HomeCycleActivity homeCycleActivity;

    private long backPressedTime;
    private Toast backToast;
    String apiToken;
    int bloodTypeId;


    public DonationListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_home_donation, container, false);
        unbinder = ButterKnife.bind (this, view);
        fab = getActivity ( ).findViewById (R.id.home_container_fragment_f_a_btn_add);
        apiToken = loadUserData (getActivity ( )).getApiToken ( );
        lytNoConnection.setVisibility (View.GONE);
        homeCycleActivity = (HomeCycleActivity) getActivity ( );
        initRecyclerView ( );
        initSpinners ( );
        return view;
    }

    private void initRecyclerView() {
        linearLayoutManager = new LinearLayoutManager (getActivity ( ));
        fragmentHomeDonationRv.setLayoutManager (linearLayoutManager);

        onEndLess = new OnEndLess (linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= maxPage) {
                    if (maxPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        getDonations (current_page);
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };
        fragmentHomeDonationRv.setHasFixedSize (true);
        donationListAdapter = new DonationListRecyclerAdapter (getContext ( ), getActivity ( ), donationList);
        fragmentHomeDonationRv.setAdapter (donationListAdapter);
        fragmentHomeDonationRv.addOnScrollListener (onEndLess);
        fragmentHomeDonationRv.addOnScrollListener (new RecyclerView.OnScrollListener ( ) {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged (recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy < 0 && !fab.isShown ( ))
                    fab.show ( );
                else if (dy > 0 && fab.isShown ( ))
                    fab.hide ( );
            }
        });

        if (donationList.size ( ) == 0) {
            checkIfNoConnection (loadedPage);
        }
    }

    private void initSpinners() {
        if (bloodTypeAdapter == null) {
            bloodTypeAdapter = new SpinnerAdapter2 (getActivity ( ));
//            getData (getClient ( ).getBloodTypes ( ), bloodTypeAdapter, getResources ( ).getString (R.string.blood_type), fragmentHomeDonationSpSpinnerBloodType);
        } else {
            fragmentHomeDonationSpSpinnerBloodType.setAdapter (bloodTypeAdapter);
        }
        getData (getClient ( ).getBloodTypes ( ), bloodTypeAdapter, getResources ( ).getString (R.string.blood_type), fragmentHomeDonationSpSpinnerBloodType);
        new AdapterView.OnItemSelectedListener ( ) {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    bloodTypeId = bloodTypeAdapter.selectedId;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };

        if (governoratesAdapter == null) {
            governoratesAdapter = new SpinnerAdapter2 (getActivity ( ));
//            getData (getClient ( ).getGovernorates ( ), governoratesAdapter, getResources ( ).getString (R.string.governorate), fragmentHomeDonationSpSpinnerGovernment);

        } else {
            fragmentHomeDonationSpSpinnerGovernment.setAdapter (governoratesAdapter);
        }

        if (donationList.size ( ) == 0) {
            checkIfNoConnection (loadedPage);
        }
        governoratesAdapter = new SpinnerAdapter2 (getActivity ( ));
        AdapterView.OnItemSelectedListener governoratesListener = new AdapterView.OnItemSelectedListener ( ) {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {
                    progressBar.setVisibility (View.VISIBLE);
                    getClient ( ).getAllDonationsRequestsFilter (apiToken, bloodTypeId, governoratesAdapter.selectedId, 1).enqueue (new Callback<Donation> ( ) {
                        @Override
                        public void onResponse(Call<Donation> call, Response<Donation> response) {
                            try {
                                if (response.body ( ).getStatus ( ) == 1) {
                                    progressBar.setVisibility (View.GONE);
                                    noInternetVisibility (View.VISIBLE, View.GONE);
                                    donationList.clear ();
                                    maxPage = response.body ( ).getData ( ).getLastPage ( );
                                    donationList.addAll (response.body ( ).getData ( ).getData ( ));
                                    donationListAdapter.notifyDataSetChanged ( );
                                }
                            } catch (Exception e) {
                                setNoConnection ( );
                            }
                        }
                        @Override
                        public void onFailure(Call<Donation> call, Throwable t) {
                            setNoConnection ( );
                        }
                    });
                }
//                else {
//                    donationList.clear ();
//                    initRecyclerView ();
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        getData (getClient ( ).getGovernorates ( ), governoratesAdapter, getResources ( ).getString (R.string.governorate), fragmentHomeDonationSpSpinnerGovernment, governoratesListener);
    }

    private void getDonations(int page) {
        progressBar.setVisibility (View.VISIBLE);
        getClient ( ).getAllDonations (apiToken, page).enqueue (new Callback<Donation> ( ) {
            @Override
            public void onResponse(Call<Donation> call, Response<Donation> response) {
                try {
                    if (response.body ( ).getStatus ( ) == 1) {
                        progressBar.setVisibility (View.GONE);
                        noInternetVisibility (View.VISIBLE, View.GONE);
                        maxPage = response.body ( ).getData ( ).getLastPage ( );
                        donationList.addAll (response.body ( ).getData ( ).getData ( ));
//                        donationListAdapter.notifyItemRangeInserted (donationListAdapter.getItemCount (),donationList.size () -1 );
                        donationListAdapter.notifyDataSetChanged ();
                    }
                } catch (Exception e) {
                    progressBar.setVisibility (View.GONE);
                    noInternetVisibility (View.VISIBLE, View.GONE);
                    homeCycleActivity.useSnackBar (e.getMessage ());
                }
            }

            @Override
            public void onFailure(Call<Donation> call, Throwable t) {
//                Toast.makeText (getActivity(), t.getMessage ( ), Toast.LENGTH_SHORT).show ( );
                setNoConnection ( );
            }
        });

    }

    private void noInternetVisibility(int postsVisibility, int noInternetVisibility) {
        fragmentHomeDonationRv.setVisibility (postsVisibility);
        fragmentHomeDonationBloodTypeLayout.setVisibility (postsVisibility);
        fragmentHomeDonationGovernmentLayout.setVisibility (postsVisibility);
        lytNoConnection.setVisibility (noInternetVisibility);
    }

    private void checkIfNoConnection(int page) {
        noInternetVisibility (View.GONE, View.GONE);
        progressBar.setVisibility (View.VISIBLE);
        getDonations (page);
        homeCycleActivity.setBadgeData ();
    }

    private void setNoConnection() {
//        homeCycleActivity.setSelection (R.id.nav_home);

        new Handler ( ).postDelayed (() -> {
            progressBar.setVisibility (View.GONE);
            noInternetVisibility (View.GONE, View.VISIBLE);
            fab.hide ( );
        }, 1000);


    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView ( );
//        unbinder.unbind ( );
//    }

    @OnClick(R.id.lyt_no_connection)
    public void onViewClicked() {
        checkIfNoConnection (loadedPage);
        initSpinners ( );

    }

}
